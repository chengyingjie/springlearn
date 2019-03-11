package com.spring.core.container;

import java.lang.reflect.Field;

/**
 * 属性字段依赖信息
 */
public class FieldInjectorData extends AbstractInjectorData {

    private Field field;

    public FieldInjectorData(String defalultName, String refName, Field field) {
        super(defalultName, refName);
        this.field = field;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

    public Field getField() {
        return field;
    }

}
