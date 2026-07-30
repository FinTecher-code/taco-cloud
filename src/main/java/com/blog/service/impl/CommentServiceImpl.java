package com.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public IPage<Comment> page(int pageNum, int pageSize, Long articleId) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        if (articleId == null) {
            return commentMapper.selectPageByArticle(page, articleId);
        }
        return commentMapper.selectPageWithArticle(page);
    }

    @Override
    @Transactional
    public Comment addComment(Long articleId, String nickname, String content) {
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setNickname(nickname);
        comment.setContent(content);
        save(comment);
        return comment;
    }

    @Override
    public void removeComment(Long id) {
        removeById(id);
    }
}
