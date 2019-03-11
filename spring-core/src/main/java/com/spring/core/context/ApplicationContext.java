package com.spring.core.context;

import com.spring.core.exception.SpringLearnException;

import java.util.Map;

/**
 * 应用上下文
 */
public interface ApplicationContext {

    /**
     * 根据beanName获取实例
     */
    Object getBean(String beanName) throws SpringLearnException;

    /**
     * 获取第一个与clazz匹配的实例
     */
    <T> T getBean(Class<T> clazz) throws SpringLearnException;

    /**
     * 获取所有与clazz匹配的实例
     */
    <T> Map<String, T> getBeans(Class<T> clazz) throws SpringLearnException;
}
