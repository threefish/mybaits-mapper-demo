package com.example.mybaitsmapper.core;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@Slf4j
public class AuditingProcessor {

    static AuditorAware<String> auditorAware = () -> Optional.of("xxxx");

    /**
     * 对实体参数执行自动填充
     *
     * @param operatType
     * @param parameterObject
     */
    public static void executeFill(AuditingOperatType operatType, Object parameterObject) {
        List<AuditingEntityColumn> auditingEntityColumns = AuditingEntityFactory.createAuditingEntityColumns(parameterObject.getClass());
        if (operatType == AuditingOperatType.INSERT) {
            operatInsertType(parameterObject, auditingEntityColumns);
        } else {
            operatUpdateType(parameterObject, auditingEntityColumns);
        }
    }

    /**
     * 插入时：对实体参数执行自动填充
     *
     * @param parameterObject
     * @param auditingEntityColumns
     */
    private static void operatInsertType(Object parameterObject, List<AuditingEntityColumn> auditingEntityColumns) {
        auditingEntityColumns.forEach(auditingEntityColumn -> {
            ColumnFillType type = auditingEntityColumn.getType();
            final Optional<?> currentAuditor = AuditorAwareContextHelper.getAuditorAware().getCurrentAuditor();
            // 获取参数原始值
            final Object fieldValue = getFieldValue(auditingEntityColumn, parameterObject);
            if (type == ColumnFillType.CREATED_BY && Objects.isNull(fieldValue)) {
                setFieldValue(auditingEntityColumn, parameterObject, currentAuditor);
            } else if (type == ColumnFillType.CREATED_DATE) {
                setFieldValue(auditingEntityColumn, parameterObject, getCurrentDateValue(auditingEntityColumn));
            } else if (type == ColumnFillType.LAST_MODIFIED_BY) {
                setFieldValue(auditingEntityColumn, parameterObject, currentAuditor);
            } else if (type == ColumnFillType.LAST_MODIFIED_DATE) {
                setFieldValue(auditingEntityColumn, parameterObject, getCurrentDateValue(auditingEntityColumn));
            }
        });
    }

    /**
     * 更新时：对实体参数执行自动填充
     *
     * @param parameterObject
     * @param auditingEntityColumns
     */
    private static void operatUpdateType(Object parameterObject, List<AuditingEntityColumn> auditingEntityColumns) {
        auditingEntityColumns.forEach(auditingEntityColumn -> {
            ColumnFillType type = auditingEntityColumn.getType();
            final Optional<?> currentAuditor = AuditorAwareContextHelper.getAuditorAware().getCurrentAuditor();
            if (type == ColumnFillType.LAST_MODIFIED_BY) {
                setFieldValue(auditingEntityColumn, parameterObject, currentAuditor);
            } else if (type == ColumnFillType.LAST_MODIFIED_DATE) {
                setFieldValue(auditingEntityColumn, parameterObject, getCurrentDateValue(auditingEntityColumn));
            }
        });
    }

    /**
     * 获取当前日期
     * @param auditingEntityColumn
     * @return
     */
    private static Optional<?> getCurrentDateValue(AuditingEntityColumn auditingEntityColumn) {
        if (auditingEntityColumn.getFiledType() == LocalDateTime.class) {
            return Optional.of(LocalDateTime.now());
        }
        if (auditingEntityColumn.getFiledType() == java.util.Date.class) {
            return Optional.of(new Date());
        }
        if (auditingEntityColumn.getFiledType() == LocalDate.class) {
            return Optional.of(LocalDate.now());
        }
        if (auditingEntityColumn.getFiledType() == LocalTime.class) {
            return Optional.of(LocalTime.now());
        }
        throw new UnsupportedOperationException("不支持的日期审计字段类型:" + auditingEntityColumn.getFiledType());
    }

    /**
     * 获取参数字段值
     *
     * @return
     */
    private static Object getFieldValue(AuditingEntityColumn auditingEntityColumn, Object parameterObject) {
        try {
            return auditingEntityColumn.getGetterMethod().invoke(parameterObject);
        } catch (Exception e) {
            log.debug("无法获取{}字段原始值，反射调用失败！参数：{}", auditingEntityColumn.getName(), parameterObject, e);
        }
        return null;
    }

    /**
     * 设置参数字段值
     *
     * @return
     */
    private static void setFieldValue(AuditingEntityColumn auditingEntityColumn, Object parameterObject, Optional<?> value) {
        value.ifPresent(o -> {
            try {
                auditingEntityColumn.getSetterMethod().invoke(parameterObject, value.get());
            } catch (Exception e) {
                log.debug("无法设置{}字段值，反射调用失败！参数：{}", auditingEntityColumn.getName(), parameterObject, e);
            }
        });
    }

}
