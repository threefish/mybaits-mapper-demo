package com.example.mybaitsmapper.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.lang.reflect.Method;

/**
 * @author 黄川 huchuc@vip.qq.com
 * date: 2021/6/6
 */
@Data
@AllArgsConstructor
public class AuditingEntityColumn {

    /**
     * 字段值是null的情况下才自动填充
     */
    private boolean nullEffective;
    /**
     * 填充类型
     */
    @NonNull
    private ColumnFillType type;

    private String name;

    @NonNull
    private Method setterMethod;

    @NonNull
    private Method getterMethod;

    @NonNull
    private Class<?> filedType;




}
