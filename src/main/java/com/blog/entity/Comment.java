package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long articleId;
    private String nickname;
    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Integer deleted;
}
