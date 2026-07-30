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
select c.*,a.title AS article_title
    from comment c\s
    left join article a on c.article_id = a.id\s
    where c.deleted = 0\s
            order by c.created_at desc

""")
    IPage<Comment> selectPageWithArticle(Page<?> page);

    @Select("""
select c.*,a.title as article_title
from comment c 
left join article a on c.article_id = a.id 
where c.deleted = 0 and c.article_id = #{articleId}
order by c.created_at desc
"""
    )
    IPage<Comment> selectPageByArticle(Page<?> page, @Param("articleId") Long articleId);
}
