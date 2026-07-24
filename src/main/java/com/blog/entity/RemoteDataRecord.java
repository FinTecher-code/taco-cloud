package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("remote_data_record")
public class RemoteDataRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String source;
    private String dataType;
    private String dataContent;
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
