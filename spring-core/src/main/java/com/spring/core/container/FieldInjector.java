package com.spring.core.container;


import com.spring.core.exception.SpringLearnException;

import java.lang.reflect.Field;
import java.util.List;


public class FieldInjector extends AbstractInjector {
    public FieldInjector(List<InjectionData> injectionData) {
        super(injectionData);
    }

    @Override
    public Object inject(Object instance, BeanDefinition beanDefinition) throws SpringLearnException {
        if (injectionData != null && injectionData.size() > 0) {
            for (InjectionData injectionData : this.injectionData) {
                FieldInjectorData fieldInjectorData = (FieldInjectorData) injectionData;
                Field field = fieldInjectorData.getField();
                field.setAccessible(true);

                BeanDefinition bean = injectionData.getBean();
                if (bean != null) {
                    try {
                        field.set(instance, bean.getInstance());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    public List<InjectionData> getFieldInjectorDatas() {
        return injectionData;
    }
}
