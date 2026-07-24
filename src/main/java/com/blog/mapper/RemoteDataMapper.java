package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.RemoteDataRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RemoteDataMapper extends BaseMapper<RemoteDataRecord> {
}
