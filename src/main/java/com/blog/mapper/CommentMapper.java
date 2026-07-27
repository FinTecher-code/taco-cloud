package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("""
            SELECT c.*, a.title AS article_title
            FROM comment c
            LEFT JOIN article a ON c.article_id = a.id
            WHERE c.deleted = 0
            ORDER BY c.created_at DESC
            """)
    IPage<Comment> selectPageWithArticle(Page<?> page);

    @Select("""
            SELECT c.*, a.title AS article_title
            FROM comment c
            LEFT JOIN article a ON c.article_id = a.id
            WHERE c.deleted = 0 AND c.article_id = #{articleId}
            ORDER BY c.created_at DESC
            """)
    IPage<Comment> selectPageByArticle(Page<?> page, @Param("articleId") Long articleId);
}
