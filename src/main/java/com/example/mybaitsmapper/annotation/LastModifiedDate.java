package com.example.mybaitsmapper.annotation;

import com.example.mybaitsmapper.core.AuditingEntityColumnType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 黄川 huchuc@vip.qq.com
 * @date: 2020/9/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.FIELD})
public @interface LastModifiedDate {
    AuditingEntityColumnType type() default AuditingEntityColumnType.LAST_MODIFIED_DATE;

}
