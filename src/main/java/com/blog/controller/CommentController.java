package com.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.dto.ApiResult;
import com.blog.dto.CommentRequest;
import com.blog.entity.Comment;
import com.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ApiResult<IPage<Comment>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long articleId) {
        return ApiResult.success(commentService.page(page, size, articleId));
    }

    @PostMapping
    public ApiResult<Comment> add(@Valid @RequestBody CommentRequest request) {
        Comment comment = commentService.addComment(
                request.getArticleId(),
                request.getNickname(),
                request.getContent()
        );
        return ApiResult.success(comment);
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        commentService.removeComment(id);
        return ApiResult.success();
    }
}
