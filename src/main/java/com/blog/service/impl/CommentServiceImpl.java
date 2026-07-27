package com.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;

import java.util.List;

public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public List<Comment> pageByArticle(Long articleId) {

        return CommentMapper.selectByArticle(articleId);
    }

    @Override
    public void deleteById(int deleted) {
        CommentMapper;
    }
}
