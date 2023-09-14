package com.shaw.lts.adapter.utils;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * SelectUtils
 * 表链接或者其他复杂查询对象转换
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/5/25 16:17
 **/
public class SelectUtils {

    public static <T> T toPojo(Class<T> tClass, Map<String, Object> resultMap) {
        if (resultMap == null || resultMap.isEmpty()) {
            return  null;
        }
        try {
            T result = tClass.getDeclaredConstructor().newInstance();
            Field[] fields = tClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if (column == null) {
                    String name = field.getName();
                    if (resultMap.containsKey(name)) {
                        field.set(result, resultMap.get(name));
                    }
                    continue;
                }
                String name = column.name();
                if (StringUtils.isNotEmpty(name) && resultMap.containsKey(name)) {
                    field.set(result, resultMap.get(name));
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
