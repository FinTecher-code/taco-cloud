package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.RemoteDataRecord;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * RemoteDataRecord 的业务接口
 * <p>
 * IService<RemoteDataRecord> 是 MyBatis-Plus 提供的通用 Service 接口，
 * 已经内置了 save/update/remove/list/page 等常用方法，我们这个接口直接继承它。
 * <p>
 * 额外自定义的方法：
 *   1. push()          — 接收远端推送的数据，存库并通知浏览器
 *   2. createSseEmitter() — 创建 SSE 连接，用于实时推送给浏览器
 *   3. listLatest()    — 查询最新的 N 条记录，用于页面刚打开时加载历史数据
 */
public interface RemoteDataService extends IService<RemoteDataRecord> {

    /**
     * 接收远端系统推送的数据
     * <p>
     * 这个方法做的事情：
     *   1. 创建一个 RemoteDataRecord 对象
     *   2. 把 source / dataType / dataContent 填进去
     *   3. 设置状态为 "RECEIVED"
     *   4. 调用 MyBatis-Plus 的 save() 存入数据库
     *   5. 通过 SSE 通知所有在线的浏览器页面
     *
     * @param source      数据来源（例如 "order-system"）
     * @param dataType    数据类型（例如 "order"）
     * @param dataContent 数据内容（JSON 字符串）
     * @return            入库后的完整记录（含数据库自动生成的 id 和 createdAt）
     */
    RemoteDataRecord push(String source, String dataType, String dataContent);

    /**
     * 创建一个 SSE（Server-Sent Events）连接
     * <p>
     * SSE 是"服务器推送"技术，浏览器通过 EventSource API 订阅后，
     * 服务端有新数据时可以主动推送给浏览器，不需要浏览器反复轮询。
     * <p>
     * 浏览器请求 GET /api/remote-data/subscribe 时，就会调用这个方法。
     *
     * @return SseEmitter 对象，Spring 会自动把它转换成 SSE 协议的响应流
     */
    SseEmitter createSseEmitter();

    /**
     * 查询最新的 N 条数据
     * <p>
     * 页面刚打开时，先调这个方法加载历史数据展示在表格中。
     * 之后有新数据通过 SSE 实时推送过来，增量更新表格。
     *
     * @param limit 最多返回多少条（默认100条）
     * @return      按接收时间倒序排列的记录列表
     */
    List<RemoteDataRecord> listLatest(int limit);

}
