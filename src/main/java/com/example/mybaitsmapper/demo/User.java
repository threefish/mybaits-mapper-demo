package com.example.mybaitsmapper.demo;

import io.mybatis.activerecord.EntityRecord;
import io.mybatis.activerecord.ExampleRecord;
import io.mybatis.provider.extend.Extend;
import lombok.Data;

/**
 * @author huc
 */
@Data
@Extend.Table("user")
public class User implements EntityRecord<User, Long>, ExampleRecord<User, Long> {
    @Extend.Column(id = true)
    private Long id;
    @Extend.Column
    private String userName;
    @Extend.Column("sex")
    private String sex;
}
