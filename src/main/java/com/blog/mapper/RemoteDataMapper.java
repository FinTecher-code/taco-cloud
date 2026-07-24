package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.RemoteDataRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * RemoteDataRecord 的 MyBatis-Plus Mapper 接口
 * <p>
 * MyBatis-Plus 的 BaseMapper 已经内置了最常用的数据库操作方法：
 *   - insert（插入）     → 对应 SQL INSERT
 *   - deleteById（删除）  → 对应 SQL DELETE
 *   - updateById（更新）  → 对应 SQL UPDATE
 *   - selectById（查询）  → 对应 SQL SELECT
 *   - selectList（列表）  → 对应 SQL SELECT ... WHERE ...
 *   - selectPage（分页）  → 对应 SQL SELECT ... LIMIT ...
 * <p>
 * 这个接口什么都不用写，继承 BaseMapper 就自动拥有以上所有方法。
 * 如果后续需要自定义复杂 SQL 查询，在这里添加方法并用 @Select 注解即可。
 *
 * @Mapper 告诉 Spring 这是一个 MyBatis 的 Mapper，需要被扫描到并创建代理对象
 */
@Mapper
public interface RemoteDataMapper extends BaseMapper<RemoteDataRecord> {

    // 无需额外代码，BaseMapper 已提供所有基础 CRUD 方法

}
