package com.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.Article;

public interface ArticleService extends IService<Article> {

    /**
     * 分页查询（联查用户名和分类名）
     */
    IPage<Article> pageWithUserAndCategory(int pageNum, int pageSize);

    /**
     * 按分类查
     */
    IPage<Article> pageByCategory(int pageNum, int pageSize, Long categoryId);

    /**
     * 搜索
     */
    IPage<Article> search(int pageNum, int pageSize, String keyword);

    /**
     * 浏览文章（增加浏览量）
     */
    Article view(Long id);

    /**
     * 点赞
     */
    void like(Long id);
}
