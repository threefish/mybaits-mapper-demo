package com.example.mybaitsmapper.demo;

import com.github.pagehelper.Page;
import io.mybatis.mapper.base.EntityProvider;
import io.mybatis.provider.Caching;
import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.SqlScript;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.stream.Collectors;

/**
 * @author huc
 */
@Mapper
public interface UserMapper extends io.mybatis.mapper.Mapper<User, Long> {

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<User> listPage(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

}
