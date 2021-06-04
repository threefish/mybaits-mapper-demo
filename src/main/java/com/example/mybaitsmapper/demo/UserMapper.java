package com.example.mybaitsmapper.demo;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author huc
 */
@Mapper
public interface UserMapper extends io.mybatis.mapper.Mapper<User, Long> {

    Page<User> listPage(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

}
