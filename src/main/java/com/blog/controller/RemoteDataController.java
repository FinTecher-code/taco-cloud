package com.blog.controller;

import com.blog.dto.ApiResult;
import com.blog.dto.RemoteDataPushRequest;
import com.blog.entity.RemoteDataRecord;
import com.blog.service.RemoteDataService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * 远程数据接收与展示控制器
 * <p>
 * 提供三个接口给外部使用：
 *   1. POST /api/remote-data/push      → 远端系统推送数据（核心入口）
 *   2. GET  /api/remote-data           → 查询已接收的数据（给页面加载历史数据用）
 *   3. GET  /api/remote-data/subscribe → SSE 订阅（给页面实时推送用）
 * <p>
 * @RestController = @Controller + @ResponseBody
 * 表示这个类的所有方法都返回 JSON，不走视图模板
 */
@RestController
@RequestMapping("/api/remote-data")
public class RemoteDataController {

    /**
     * 注入业务 Service
     * 使用构造器注入（Spring 推荐的注入方式），替代 @Autowired
     */
    private final RemoteDataService remoteDataService;

    /**
     * 构造器注入
     * Spring 会自动找到 RemoteDataService 的实现类（RemoteDataServiceImpl）并传进来
     */
    public RemoteDataController(RemoteDataService remoteDataService) {
        this.remoteDataService = remoteDataService;
    }

    // ==================== 接口1：接收远端推送 ====================

    /**
     * 远端系统调用的推送接口
     * <p>
     * 调用方式：POST /api/remote-data/push
     * 请求体格式（JSON）：
     * {
     *   "source": "order-system",      // 数据来源（哪个系统发的）
     *   "dataType": "order",           // 数据类型（什么业务）
     *   "dataContent": "{"orderId":1001,"amount":99.9}"  // 具体数据（JSON字符串）
     * }
     * <p>
     * 响应格式：
     * {
     *   "code": 200,
     *   "message": "OK",
     *   "data": { ... }   // 入库后的完整记录（含数据库自动生成的 id 和 createdAt）
     * }
     *
     * @param request 请求体中的推送数据，@Valid 会触发参数校验（比如 @NotBlank）
     * @return 统一响应格式 ApiResult，里面包裹着入库后的记录
     */
    @PostMapping("/push")
    public ApiResult<RemoteDataRecord> push(@Valid @RequestBody RemoteDataPushRequest request) {

        // 调用 Service 层的 push 方法：
        //   1. 创建 RemoteDataRecord 实体
        //   2. 存入 MySQL 数据库
        //   3. 通过 SSE 推送给所有在线的浏览器
        RemoteDataRecord record = remoteDataService.push(
                request.getSource(),        // 来源：order-system
                request.getDataType(),      // 类型：order
                request.getDataContent()    // 内容：{...JSON...}
        );

        // 返回统一成功响应，data 字段是入库后的完整记录
        return ApiResult.success(record);
    }

    // ==================== 接口2：查询历史数据 ====================

    /**
     * 查询已接收的数据列表
     * <p>
     * 浏览器打开看板页面时，先调这个接口加载历史数据显示在表格中。
     * 后续有新数据通过 SSE 实时推送，不需要再手动刷新。
     * <p>
     * 调用方式：GET /api/remote-data?limit=100
     *
     * @param limit 最多返回多少条记录（默认100），按接收时间倒序
     * @return 数据列表，包裹在 ApiResult 中
     */
    @GetMapping
    public ApiResult<List<RemoteDataRecord>> list(
            @RequestParam(defaultValue = "100") int limit) {

        // 调用 Service 层查询最新数据
        List<RemoteDataRecord> records = remoteDataService.listLatest(limit);

        // 返回统一成功响应，data 字段是数据列表
        return ApiResult.success(records);
    }

    // ==================== 接口3：SSE 订阅 ====================

    /**
     * 浏览器订阅 SSE 实时推送
     * <p>
     * 这个接口返回的不是普通的 JSON，而是一个 SSE（Server-Sent Events）流。
     * 浏览器用 JavaScript 的 EventSource API 连接这个地址后，
     * 每当有新的数据推送过来，服务端就会主动把数据推送给浏览器。
     * <p>
     * 调用方式：GET /api/remote-data/subscribe
     * 返回类型是 SseEmitter，Spring 会自动将其转换成 SSE 协议的响应流。
     * <p>
     * SSE 工作原理：
     *   - 浏览器发起一个普通的 HTTP GET 请求
     *   - 服务端不关闭连接，一直保持打开状态
     *   - 服务端有数据时，通过 SseEmitter.send() 把数据推送给浏览器
     *   - 浏览器通过 EventSource 监听事件，收到数据后自动更新页面
     *   - 如果连接断开，浏览器会自动重连
     *
     * @return SseEmitter 对象，代表一个 SSE 长连接
     */
    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        // 调用 Service 层创建一个新的 SSE 连接
        // Service 层会把这个连接存入 emitters 列表
        // 后续有数据推送时，会遍历这个列表给每个浏览器都发一份
        return remoteDataService.createSseEmitter();
    }

}
