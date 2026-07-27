package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "评论内容不能为空")
    private String content;
}
