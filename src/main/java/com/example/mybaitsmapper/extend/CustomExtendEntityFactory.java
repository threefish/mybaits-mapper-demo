/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mybaitsmapper.extend;

import io.mybatis.provider.EntityColumn;
import io.mybatis.provider.EntityFactory;
import io.mybatis.provider.EntityField;
import io.mybatis.provider.extend.Extend;
import io.mybatis.provider.extend.ExtendEntityColumn;
import io.mybatis.provider.extend.ExtendEntityFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 通过 SPI 工厂扩展 EntityColumn 和 EntityTable
 *
 * @author liuzh
 */
public class CustomExtendEntityFactory extends ExtendEntityFactory {

    private static final char SEPARATOR = '_';


    @Override
    public Optional<List<EntityColumn>> createEntityColumn(EntityField field) {
        Optional<List<EntityColumn>> optionalEntityColumns = next().createEntityColumn(field);
        if (optionalEntityColumns.isPresent()) {
            return optionalEntityColumns.map(columns -> columns.stream().map(ExtendEntityColumn::new).collect(Collectors.toList()));
        } else if (field.isAnnotationPresent(Extend.Column.class)) {
            Extend.Column column = field.getAnnotation(Extend.Column.class);
            String columnName = column.value();
            if (columnName.isEmpty()) {
                columnName = field.getName();
            }
            // 将字段统一设置为驼峰自动转下划线格式
            return Optional.of(Arrays.asList(new ExtendEntityColumn(new EntityColumn(field, toUnderlineName(columnName), column.id()))));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int getOrder() {
        return EntityFactory.DEFAULT_ORDER - 1;
    }

    private String toUnderlineName(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) {
                        sb.append(SEPARATOR);
                    }
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }


}
