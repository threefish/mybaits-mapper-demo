package com.example.mybaitsmapper.demo;

import com.example.mybaitsmapper.annotation.*;
import com.example.mybaitsmapper.base.BaseEntity;
import io.mybatis.activerecord.EntityRecord;
import io.mybatis.activerecord.ExampleRecord;
import io.mybatis.provider.extend.Extend;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author huc
 */
@Data
@AuditingEntity
@Extend.Table("user")
public class User extends BaseEntity implements EntityRecord<User, Long>, ExampleRecord<User, Long> {
    @Extend.Column(id = true)
    private Long id;
    @Extend.Column
    private String userName;
    @Extend.Column("sex")
    private String sex;

    @Extend.Column
    @CreatedBy
    private String createBy;

    @Extend.Column
    @CreatedDate
    private Date createDate;

    @Extend.Column
    @LastModifiedBy
    private String lastModifiedBy;

    @Extend.Column
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
