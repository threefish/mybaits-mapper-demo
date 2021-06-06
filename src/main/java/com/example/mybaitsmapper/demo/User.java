package com.example.mybaitsmapper.demo;

import com.example.mybaitsmapper.annotation.AuditingEntity;
import com.example.mybaitsmapper.annotation.CreatedBy;
import com.example.mybaitsmapper.annotation.LastModifiedBy;
import com.example.mybaitsmapper.base.BaseEntity;
import io.mybatis.activerecord.EntityRecord;
import io.mybatis.activerecord.ExampleRecord;
import io.mybatis.provider.extend.Extend;
import lombok.Data;

/**
 * @author huc
 */
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
    @LastModifiedBy
    private String updateBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
