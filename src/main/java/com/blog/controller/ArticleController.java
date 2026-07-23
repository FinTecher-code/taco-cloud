package com.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.dto.ApiResult;
import com.blog.entity.Article;
import com.blog.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 分页列表（默认第1页，每页10条）
     */
    @GetMapping
    public ApiResult<IPage<Article>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {

        IPage<Article> result;
        if (keyword != null && !keyword.isBlank()) {
            result = articleService.search(page, size, keyword);
        } else if (categoryId != null) {
            result = articleService.pageByCategory(page, size, categoryId);
        } else {
            result = articleService.pageWithUserAndCategory(page, size);
        }
        return ApiResult.success(result);
    }

    /**
     * 文章详情（同时增加浏览量）
     */
    @GetMapping("/{id}")
    public ApiResult<Article> getById(@PathVariable Long id) {
        Article article = articleService.view(id);
        return ApiResult.success(article);
    }

    @PostMapping
    public ApiResult<Article> add(@Valid @RequestBody Article article) {
        article.setViewCount(0);
        article.setLikeCount(0);
        if (article.getStatus() == null) {
            article.setStatus(0); // 默认草稿
        }
        articleService.save(article);
        return ApiResult.success(article);
    }

    @PutMapping("/{id}")
    public ApiResult<Article> update(@PathVariable Long id, @RequestBody Article article) {
        article.setId(id);
        articleService.updateById(article);
        return ApiResult.success(article);
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        articleService.removeById(id);
        return ApiResult.success();
    }

    /**
     * 点赞
     */
    @PostMapping("/{id}/like")
    public ApiResult<Void> like(@PathVariable Long id) {
        articleService.like(id);
        return ApiResult.success();
    }
}
