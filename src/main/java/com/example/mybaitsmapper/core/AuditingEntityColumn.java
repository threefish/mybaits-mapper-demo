package com.example.mybaitsmapper.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author 黄川 huchuc@vip.qq.com
 * date: 2021/6/6
 */
@Data
@AllArgsConstructor
public class AuditingEntityColumn {

    private AuditingEntityColumnType type;

    private String name;

    private Method setterMethod;

    private Method getterMethod;



}
