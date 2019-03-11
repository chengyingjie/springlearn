package com.spring.core.container;


import com.spring.core.exception.ErrorCode;
import com.spring.core.exception.SpringLearnException;

import java.util.HashMap;
import java.util.Map;

/**
 * 容器类，负责管理bean
 */
public class BeanContainer {
    private Map<String, BeanDefinition> beans = new HashMap<>();

    public void register(String beanName, BeanDefinition beanDefinition) throws SpringLearnException {
        if (beans.containsKey(beanName)) {
            throw new SpringLearnException(ErrorCode.KEY_CONFLICT);
        }
        beans.put(beanName, beanDefinition);
    }

    /**
     * 根据beanName获取实例
     */
    public Object getBean(String beanName) throws SpringLearnException {
        BeanDefinition beanDefinition = beans.get(beanName);
        return beanDefinition == null ? null : beanDefinition.getInstance();
    }

    /**
     * 获取第一个与clazz匹配的实例
     */
    public BeanDefinition getBean(Class<?> clazz) throws SpringLearnException {
        for (BeanDefinition beanDefinition : beans.values()) {
            if (beanDefinition.isType(clazz)) {
                return beanDefinition;
            }
        }
        return null;
    }

    /**
     * 获取所有与clazz匹配的实例
     */
    public Map<String, BeanDefinition> getBeans(Class<?> clazz) throws SpringLearnException {
        Map<String, BeanDefinition> beans = new HashMap<>(8);
        for (Map.Entry<String, BeanDefinition> entry : this.beans.entrySet()) {
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.isType(clazz) || beanDefinition.isSubType(clazz)) {
                beans.put(entry.getKey(), beanDefinition);
            }
        }
        return beans;
    }

    /**
     * 获取所有bean的定义
     */
    public Map<String, BeanDefinition> getBeanDefinations() {
        return beans;
    }

}
