package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页查询文章（联查用户名和分类名）
     */
    @Select("""
            SELECT a.*, u.nickname AS username, c.name AS category_name
            FROM article a
            LEFT JOIN user u ON a.user_id = u.id
            LEFT JOIN category c ON a.category_id = c.id
            WHERE a.deleted = 0
            ORDER BY a.created_at DESC
            """)
    IPage<Article> selectPageWithUserAndCategory(Page<?> page);

    /**
     * 按分类查询（联查）
     */
    @Select("""
            SELECT a.*, u.nickname AS username, c.name AS category_name
            FROM article a
            LEFT JOIN user u ON a.user_id = u.id
            LEFT JOIN category c ON a.category_id = c.id
            WHERE a.deleted = 0 AND a.category_id = #{categoryId}
            ORDER BY a.created_at DESC
            """)
    IPage<Article> selectPageByCategory(Page<?> page, @Param("categoryId") Long categoryId);

    /**
     * 搜索文章（标题模糊匹配）
     */
    @Select("""
            SELECT a.*, u.nickname AS username, c.name AS category_name
            FROM article a
            LEFT JOIN user u ON a.user_id = u.id
            LEFT JOIN category c ON a.category_id = c.id
            WHERE a.deleted = 0 AND a.title LIKE CONCAT('%', #{keyword}, '%')
            ORDER BY a.created_at DESC
            """)
    IPage<Article> searchByTitle(Page<?> page, @Param("keyword") String keyword);
}
