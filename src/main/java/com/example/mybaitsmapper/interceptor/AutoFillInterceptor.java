package com.example.mybaitsmapper.interceptor;

import com.example.mybaitsmapper.annotation.AuditingEntity;
import com.example.mybaitsmapper.core.AuditingProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * @author 黄川 huchuc@vip.qq.com
 * 执行更新时拦截
 */
@Slf4j
@Component
@Intercepts(@Signature(type = Executor.class, method = AutoFillInterceptor.METHOD, args = {MappedStatement.class, Object.class}))
public class AutoFillInterceptor implements Interceptor {

    public static final String METHOD = "update";

    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        doIntercept(invocation);
        return invocation.proceed();
    }

    private void doIntercept(Invocation invocation) {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object parameterObj = invocation.getArgs()[1];
        if (Objects.nonNull(parameterObj)) {
            Class<?> parameterObjClass = parameterObj.getClass();
            AuditingEntity auditingEntity = AnnotationUtils.findAnnotation(parameterObjClass, AuditingEntity.class);
            if (Objects.nonNull(auditingEntity)) {
                // 执行填充数据
                AuditingProcessor.executeFill(ms.getSqlCommandType(), parameterObj);
            }
        }
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

}
