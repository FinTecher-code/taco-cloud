package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.Article;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public IPage<Article> pageWithUserAndCategory(int pageNum, int pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        return articleMapper.selectPageWithUserAndCategory(page);
    }

    @Override
    public IPage<Article> pageByCategory(int pageNum, int pageSize, Long categoryId) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        return articleMapper.selectPageByCategory(page, categoryId);
    }

    @Override
    public IPage<Article> search(int pageNum, int pageSize, String keyword) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        return articleMapper.searchByTitle(page, keyword);
    }

    @Override
    @Transactional
    public Article view(Long id) {
        Article article = getById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        article.setViewCount(article.getViewCount() + 1);
        updateById(article);
        return article;
    }

    @Override
    @Transactional
    public void like(Long id) {
        Article article = getById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        article.setLikeCount(article.getLikeCount() + 1);
        updateById(article);
    }
}
