package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 远程推送数据记录 —— 对应数据库表 remote_data_record
 * <p>
 * 当远端系统（如订单系统、仓储系统）通过 HTTP POST 推送数据到我们系统时，
 * 每一条数据都会存成这个实体的一条记录，同时通过 SSE 推送给浏览器页面。
 * </p>
 *
 * @TableName 告诉 MyBatis-Plus 这个类对应数据库里的哪张表
 */
@Data                           // Lombok 自动生成 getter/setter/toString/equals/hashCode
@TableName("remote_data_record") // 映射到数据库表 remote_data_record
public class RemoteDataRecord {

    /**
     * 主键 ID，自增
     * IdType.AUTO 表示使用数据库自增策略（MySQL 的 AUTO_INCREMENT）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 数据来源标识
     * 例如："order-system"（订单系统）、"warehouse"（仓储系统）
     * 用来区分数据是从哪个远端系统推送过来的
     */
    private String source;

    /**
     * 数据类型
     * 例如："order"（订单）、"inventory"（库存）、"logistics"（物流）
     * 用来区分同一来源下的不同业务类型
     */
    private String dataType;

    /**
     * 数据内容（纯文本，通常存 JSON 字符串）
     * 远端系统推送的具体业务数据，以字符串形式存储
     * 例如：{"orderId":1001, "amount":99.9, "userId":888}
     * 用字符串存储的好处是：不管数据结构怎么变，都不需要改表结构
     */
    private String dataContent;

    /**
     * 数据处理状态
     * 当前固定为 "RECEIVED"（已接收），预留后续扩展：
     *   - RECEIVED  → 已收到，待处理
     *   - PROCESSED → 已处理完成
     *   - FAILED    → 处理失败
     */
    private String status;

    /**
     * 记录创建时间（数据入库时间）
     * FieldFill.INSERT 表示在 INSERT 时由 MyMetaObjectHandler 自动填充当前时间
     * 不需要手动 set，框架会自动处理
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

}
