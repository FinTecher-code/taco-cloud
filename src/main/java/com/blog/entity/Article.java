package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String content;
    private String summary;
    private Long userId;
    private Long categoryId;

    /**
     * 状态 0-草稿 1-已发布
     */
    private Integer status;

    private Integer viewCount;
    private Integer likeCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;

    // ---------- 非数据库字段（用于联表查询） ----------

    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String categoryName;
}
