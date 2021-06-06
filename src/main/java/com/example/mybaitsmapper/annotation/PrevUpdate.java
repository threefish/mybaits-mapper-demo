package com.example.mybaitsmapper.annotation;

import java.lang.annotation.*;

/**
 * 在执行更新操作时触发
 *
 * @author 黄川 huchuc@vip.qq.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface PrevUpdate {

    /**
     * nullEffective=true时上面的赋值规则要起效必须是在[当前字段==null]时才能生效
     */
    boolean nullEffective() default false;
}
