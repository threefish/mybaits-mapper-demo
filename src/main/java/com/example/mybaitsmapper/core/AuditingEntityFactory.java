package com.example.mybaitsmapper.core;

import cn.hutool.core.util.ReflectUtil;
import com.example.mybaitsmapper.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
public class AuditingEntityFactory {

    private static final Map<Class<?>, List<AuditingEntityColumn>> AUDITING_ENTITY_CATCH = new HashMap();

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
                        list.add(new AuditingEntityColumn(createdBy.nullEffective(), ColumnFillType.CREATED_BY, field.getName(), setterMethod, getterMethod, field.getType()));
                    } else if (Objects.nonNull(createdDate)) {
                        list.add(new AuditingEntityColumn(false, ColumnFillType.CREATED_DATE, field.getName(), setterMethod, getterMethod, field.getType()));
                    } else if (Objects.nonNull(lastModifiedBy)) {
                        list.add(new AuditingEntityColumn(false, ColumnFillType.LAST_MODIFIED_BY, field.getName(), setterMethod, getterMethod, field.getType()));
                    } else if (Objects.nonNull(lastModifiedDate)) {
                        list.add(new AuditingEntityColumn(false, ColumnFillType.LAST_MODIFIED_DATE, field.getName(), setterMethod, getterMethod, field.getType()));
                    }
                });
                auditingEntityColumns = list;
                AUDITING_ENTITY_CATCH.put(entityClass, list);
            }
        }
        return auditingEntityColumns;
    }


    /**
     * 获取类字段
     *
     * @param entityClass
     * @return
     */
    private static Field[] getFields(Class<?> entityClass) {
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
}
