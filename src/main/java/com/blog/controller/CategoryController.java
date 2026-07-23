package com.blog.controller;

import com.blog.dto.ApiResult;
import com.blog.entity.Category;
import com.blog.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ApiResult<List<Category>> list() {
        return ApiResult.success(categoryService.list());
    }

    @GetMapping("/{id}")
    public ApiResult<Category> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return ApiResult.error("分类不存在");
        }
        return ApiResult.success(category);
    }

    @PostMapping
    public ApiResult<Category> add(@RequestBody Category category) {
        categoryService.save(category);
        return ApiResult.success(category);
    }

    @PutMapping("/{id}")
    public ApiResult<Category> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.updateById(category);
        return ApiResult.success(category);
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        // 注意：V1 只删除分类，不处理分类下的文章
        // V2 可以考虑把分类下文章设为 uncategorized
        categoryService.removeById(id);
        return ApiResult.success();
    }
}
