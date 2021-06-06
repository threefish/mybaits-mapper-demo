package com.example.mybaitsmapper.demo;

import cn.hutool.core.util.ReflectUtil;
import com.example.mybaitsmapper.annotation.*;
import com.example.mybaitsmapper.core.AuditingEntityColumn;
import com.example.mybaitsmapper.core.AuditingEntityColumnType;
import com.example.mybaitsmapper.core.EntityAware;
import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.SqlScript;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 黄川 huchuc@vip.qq.com
 * date: 2021/6/6
 */
public class InserProvider {

    private static final Map<Class<?>, List<AuditingEntityColumn>> AUDITING_ENTITY_CATCH = new HashMap();

    /**
     * 获取类字段
     *
     * @param entityClass
     * @return
     */
    public static Field[] getFields(Class<?> entityClass) {
        Class<?> cc = entityClass;
        Map<String, Field> map = new LinkedHashMap<String, Field>();
        while (null != cc && cc != Object.class) {
            Field[] fs = cc.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                int m = f.getModifiers();
                if (Modifier.isStatic(m) || Modifier.isFinal(m)) {
                    continue;
                }
                if (f.getName().startsWith("this$")) {
                    continue;
                }
                if (map.containsKey(fs[i].getName())) {
                    continue;
                }
                map.put(fs[i].getName(), fs[i]);
            }
            cc = cc.getSuperclass();
        }
        return map.values().toArray(new Field[map.size()]);
    }


    /**
     * @param entityClass
     * @return
     */
    public static List<AuditingEntityColumn> createAuditingEntityColumns(Class<?> entityClass) {
        List<AuditingEntityColumn> auditingEntityColumns = AUDITING_ENTITY_CATCH.get(entityClass);
        if (Objects.isNull(auditingEntityColumns)) {
            AuditingEntity annotation = entityClass.getAnnotation(AuditingEntity.class);
            if (Objects.nonNull(annotation)) {
                List<AuditingEntityColumn> list = new ArrayList<>();
                // 需要自动设置审计字段
                Field[] fields = getFields(entityClass);
                Arrays.stream(fields).forEach(field -> {
                    Method setterMethod = ReflectUtil.getMethod(entityClass, "set" + upperFirst(field.getName()), field.getType());
                    Method getterMethod = ReflectUtil.getMethod(entityClass, "get" + upperFirst(field.getName()));
                    CreatedBy createdBy = field.getAnnotation(CreatedBy.class);
                    CreatedDate createdDate = field.getAnnotation(CreatedDate.class);
                    LastModifiedBy lastModifiedBy = field.getAnnotation(LastModifiedBy.class);
                    LastModifiedDate lastModifiedDate = field.getAnnotation(LastModifiedDate.class);
                    if (Objects.nonNull(createdBy)) {
                        list.add(new AuditingEntityColumn(createdBy.type(), field.getName(), setterMethod, getterMethod));
                    } else if (Objects.nonNull(createdDate)) {
                        list.add(new AuditingEntityColumn(createdDate.type(), field.getName(), setterMethod, getterMethod));
                    } else if (Objects.nonNull(lastModifiedBy)) {
                        list.add(new AuditingEntityColumn(lastModifiedBy.type(), field.getName(), setterMethod, getterMethod));
                    } else if (Objects.nonNull(lastModifiedDate)) {
                        list.add(new AuditingEntityColumn(lastModifiedDate.type(), field.getName(), setterMethod, getterMethod));
                    }
                });
                auditingEntityColumns = list;
                AUDITING_ENTITY_CATCH.put(entityClass, list);
            }
        }
        return auditingEntityColumns;
    }


    /**
     * 将字符串首字母大写
     *
     * @param s 字符串
     * @return 首字母大写后的新字符串
     */
    private static String upperFirst(CharSequence s) {
        int len = s.length();
        char c = s.charAt(0);
        return new StringBuilder(len).append(Character.toUpperCase(c)).append(s.subSequence(1, len)).toString();
    }

    /**
     * 保存实体
     *
     * @param providerContext 上下文
     * @return cacheKey
     */
    public static String insert(ProviderContext providerContext, Object parameterObject) {
        EntityAware entityAware = new EntityAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                return Optional.of("xxxx");
            }
        };
        List<AuditingEntityColumn> auditingEntityColumns = createAuditingEntityColumns(parameterObject.getClass());
        auditingEntityColumns.forEach(auditingEntityColumn -> {
            AuditingEntityColumnType type = auditingEntityColumn.getType();
            switch (type) {
                case CREATED_BY:
                    Optional currentAuditor = entityAware.getCurrentAuditor();
                    currentAuditor.ifPresent(o -> {
                        try {
                            Object val = auditingEntityColumn.getGetterMethod().invoke(parameterObject);
                            // 字段值为null则执行填充
                            if (Objects.isNull(val)) {
                                auditingEntityColumn.getSetterMethod().invoke(parameterObject, currentAuditor.get());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    break;
                case LAST_MODIFIED_BY:
                    break;
                case CREATED_DATE:
                    break;
                case LAST_MODIFIED_DATE:
                    break;
                default:
            }
        });
        return SqlScript.caching(providerContext, entity -> "INSERT INTO " + entity.table()
                + "(" + entity.insertColumnList() + ")"
                + " VALUES (" + entity.insertColumns().stream()
                .map(EntityColumn::variables).collect(Collectors.joining(",")) + ")");
    }
}
