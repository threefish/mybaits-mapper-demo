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
     * 保存实体，默认主键自增，并且名称为 id
     * <p>
     * 这个方法是个示例，你可以在自己的接口中使用相同的方式覆盖父接口中的配置
     *
     * @param user 实体类
     * @return 1成功，0失败
     */
    @Override
    @Lang(Caching.class)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = InserProvider.class, method = "insert")
    int insert(User user);

    Page<User> listPage(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

}
