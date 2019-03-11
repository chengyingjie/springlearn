package com.spring.core.container;


import com.spring.core.exception.SpringLearnException;

public interface Injector {

    /**
     * 判断当前bean是否依赖beanDefination，如果是，返回true，否则返回false
     */
    boolean hasDependence(BeanDefinition beanDefinition);

    /**
     * 注入依赖
     */
    Object inject(Object instance, BeanDefinition beanDefinition) throws SpringLearnException;
}
