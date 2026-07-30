package com.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {
    /**
     * 按文章查看评论
     */
    IPage<Comment> page(int pageNum, int pageSize, Long articleId);

    Comment addComment(Long articleId, String nickname, String content);

    /**
     * 按照id删除
     */
    void removeComment(Long id);
}

