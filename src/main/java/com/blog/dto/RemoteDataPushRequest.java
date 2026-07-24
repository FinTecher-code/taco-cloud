package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 远端系统推送数据时，HTTP 请求体的数据结构
 * <p>
 * 远端系统调用 POST /api/remote-data/push 时，
 * 必须按照这个格式发送 JSON 数据，我们的程序才能正确解析。
 * </p>
 *
 * 使用示例（HTTP 请求体 JSON）：
 * {
 *   "source": "order-system",
 *   "dataType": "order",
 *   "dataContent": "{\"orderId\":1001,\"amount\":99.9}"
 * }
 */
@Data   // Lombok 自动生成 getter/setter
public class RemoteDataPushRequest {

    /**
     * 数据来源标识
     * 必填（@NotBlank 表示不能为 null 也不能是空字符串）
     * 例如："order-system"、"warehouse"、"第三方API"
     */
    @NotBlank(message = "数据来源不能为空")
    private String source;

    /**
     * 数据类型
     * 必填（@NotBlank）
     * 例如："order"（订单）、"inventory"（库存变化）、"logistics"（物流状态）
     */
    @NotBlank(message = "数据类型不能为空")
    private String dataType;

    /**
     * 数据内容
     * 必填（@NotBlank）
     * 建议使用 JSON 字符串格式，这样能承载任意结构的数据
     * 例如商品数据：{"sku":"A001", "name":"iPhone", "price":6999}
     * 例如订单数据：{"orderId":1001, "items":[...], "total":199.8}
     */
    @NotBlank(message = "数据内容不能为空")
    private String dataContent;

}
