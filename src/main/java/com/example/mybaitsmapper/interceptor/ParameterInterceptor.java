package com.example.mybaitsmapper.interceptor;

import com.example.mybaitsmapper.annotation.AuditingEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.util.Objects;

/**
 * @author 黄川 huchuc@vip.qq.com
 * 设置参数时进行拦截
 */
@Slf4j
@Component
@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class)})
public class ParameterInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        doIntercept(invocation);
        return invocation.proceed();
    }

    private void doIntercept(Invocation invocation) throws IllegalAccessException, NoSuchFieldException {
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        parameterField.setAccessible(true);
        Object parameterObj = parameterField.get(parameterHandler);
        if (Objects.nonNull(parameterObj)) {
            Class<?> parameterObjClass = parameterObj.getClass();
            AuditingEntity auditingEntity = AnnotationUtils.findAnnotation(parameterObjClass, AuditingEntity.class);
            if (Objects.nonNull(auditingEntity)) {
                System.out.println(auditingEntity);
            }
        }
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof ParameterHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

}
