package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.RemoteDataRecord;
import com.blog.mapper.RemoteDataMapper;
import com.blog.service.RemoteDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * RemoteDataRecord 的业务实现类
 * <p>
 * 继承了 ServiceImpl<RemoteDataMapper, RemoteDataRecord>，
 * 这是 MyBatis-Plus 提供的通用实现，已经实现了 IService 接口里的所有基础方法。
 * <p>
 * 核心功能：
 *   1. push() — 接收远端推送 → 存库 → 推送给浏览器
 *   2. createSseEmitter() — 管理浏览器 SSE 连接
 *   3. listLatest() — 查询最新数据
 */
@Service  // 标记为 Spring 的 Service Bean，会被自动扫描并注册
public class RemoteDataServiceImpl
        extends ServiceImpl<RemoteDataMapper, RemoteDataRecord>
        implements RemoteDataService {

    /**
     * 存放所有浏览器 SSE 连接的列表
     * <p>
     * CopyOnWriteArrayList 是线程安全的 ArrayList，适合"读多写少"的场景：
     *   - 读取（遍历推送）非常频繁
     *   - 写入（添加/移除连接）相对较少
     * <p>
     * 每次有新数据推送时，遍历这个列表，给每个浏览器都发一份。
     */
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // ==================== 接收推送 ====================

    /**
     * 接收远端系统推送的数据
     * <p>
     * 完整流程：
     *   1. 创建实体对象，填入数据
     *   2. save() 存入 MySQL → 自动生成 id 和 createdAt
     *   3. notifySubscribers() → 通过 SSE 推送给所有在线的浏览器页面
     *
     * @Transactional 表示这个方法是一个数据库事务：
     *   - 如果存库成功但通知失败，事务会回滚，数据不会丢失
     *   - 确保存库和业务逻辑的一致性
     */
    @Override
    @Transactional
    public RemoteDataRecord push(String source, String dataType, String dataContent) {

        // ----- 第1步：创建实体对象，把参数填进去 -----
        RemoteDataRecord record = new RemoteDataRecord();
        record.setSource(source);             // 来源：例如 "order-system"
        record.setDataType(dataType);          // 类型：例如 "order"
        record.setDataContent(dataContent);    // 内容：JSON 字符串
        record.setStatus("RECEIVED");          // 初始状态：已接收

        // ----- 第2步：存入数据库 -----
        // save() 是 MyBatis-Plus 提供的方法，执行 INSERT SQL
        // 插入成功后，record 对象会被自动填上数据库生成的 id 和 createdAt
        save(record);

        // ----- 第3步：通知所有在线的浏览器 -----
        // 遍历 emitters 列表，逐个发送 "new-data" 事件
        notifySubscribers(record);

        // 返回入库后的完整记录（含 id 和 createdAt）
        return record;
    }

    // ==================== SSE 连接管理 ====================

    /**
     * 为浏览器创建一个 SSE 连接
     * <p>
     * 浏览器请求 GET /api/remote-data/subscribe 时，Spring MVC 会调用这个方法。
     * 返回的 SseEmitter 对象会被 Spring 转换成 HTTP 长连接响应，
     * 之后服务端可以通过 emitter.send() 主动向浏览器推送数据。
     * <p>
     * 连接生命周期管理：
     *   - onCompletion：正常关闭时自动从列表移除
     *   - onTimeout：连接超时时自动移除
     *   - onError：发生异常时自动移除
     * 这样就能保证 emitters 列表里不会堆积已经断开的连接，避免内存泄漏。
     */
    @Override
    public SseEmitter createSseEmitter() {

        // 创建 SseEmitter，参数 0L 表示永不超时（只要浏览器不关，连接就一直保持）
        SseEmitter emitter = new SseEmitter(0L);

        // 把新连接加入列表，后续有数据推送时会遍历这个列表
        emitters.add(emitter);

        // ----- 注册连接关闭时的清理回调 -----
        // 浏览器关闭页面、刷新、或者网络断开时，自动从列表移除
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        // ----- 发送连接成功的确认消息给浏览器 -----
        // 浏览器收到这个事件后，就可以确认 SSE 连接已经建立成功了
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")              // 事件名称：connected
                    .data("SSE connected"));        // 事件数据：连接成功
        } catch (IOException e) {
            // 如果发送失败（比如浏览器已经断开），立即移除这个连接
            emitters.remove(emitter);
        }

        return emitter;
    }

    // ==================== 数据查询 ====================

    /**
     * 查询最新的 N 条记录
     * <p>
     * 页面刚加载时调这个接口，先展示历史数据。
     * LambdaQueryWrapper 是 MyBatis-Plus 提供的条件构造器，
     * 可以链式调用各种查询条件（where、orderBy、limit 等）。
     */
    @Override
    public List<RemoteDataRecord> listLatest(int limit) {

        // 创建查询条件构造器
        LambdaQueryWrapper<RemoteDataRecord> wrapper = new LambdaQueryWrapper<>();

        // 按创建时间倒序排列（最新的在前面）
        wrapper.orderByDesc(RemoteDataRecord::getCreatedAt);

        // 限制返回条数（最多 limit 条）
        wrapper.last("LIMIT " + limit);

        // 执行查询，返回结果列表
        return list(wrapper);
    }

    // ==================== 私有方法 ====================

    /**
     * 通知所有在线的浏览器：有新数据来了
     * <p>
     * 遍历 emitters 列表，给每个浏览器发送 "new-data" 事件。
     * 如果某个浏览器已经断开了连接，发送时会抛出 IOException，
     * 我们就把它从列表里移除，避免下次继续尝试发送。
     *
     * @param record 新入库的数据记录（Spring 会自动转成 JSON 格式发送）
     */
    private void notifySubscribers(RemoteDataRecord record) {

        // 遍历所有在线浏览器的 SSE 连接
        for (SseEmitter emitter : emitters) {
            try {
                // 发送事件名为 "new-data" 的消息
                // Spring 会自动把 record 对象转成 JSON 字符串
                emitter.send(SseEmitter.event()
                        .name("new-data")          // 事件名称，浏览器用 addEventListener 监听
                        .data(record));            // 事件数据，就是这条新记录
            } catch (IOException e) {
                // 发送失败 → 说明这个浏览器已经断开了
                // 从列表移除，下次不再给他发
                emitters.remove(emitter);
            }
        }
    }

}
