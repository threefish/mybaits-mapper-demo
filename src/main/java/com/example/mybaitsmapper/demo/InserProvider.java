package com.example.mybaitsmapper.demo;

import com.example.mybaitsmapper.core.AuditingOperatType;
import com.example.mybaitsmapper.core.AuditingProcessor;
import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.SqlScript;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.stream.Collectors;

/**
 * @author 黄川 huchuc@vip.qq.com
 * date: 2021/6/6
 */
public class InserProvider {


    /**
     * 保存实体
     *
     * @param providerContext 上下文
     * @return cacheKey
     */
    public static String insert(ProviderContext providerContext, Object parameterObject) {
        AuditingProcessor.executeFill(AuditingOperatType.INSERT, parameterObject);
        return SqlScript.caching(providerContext, entity -> "INSERT INTO " + entity.table()
                + "(" + entity.insertColumnList() + ")"
                + " VALUES (" + entity.insertColumns().stream()
                .map(EntityColumn::variables).collect(Collectors.joining(",")) + ")");
    }
}
