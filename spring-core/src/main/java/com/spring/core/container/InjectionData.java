package com.spring.core.container;


public interface InjectionData {

    /**
     * 设置bean
     */
    void setBean(BeanDefinition beanDefinition);

    /**
     * 返回依赖的bean
     */
    BeanDefinition getBean();

    /**
     * 设置依赖的默认名称
     */
    void setDefaultName(String defaultName);

    /**
     * 获取依赖的默认名称
     */
    String getDefaultName();

    /**
     * 获取指定的依赖的名称
     */
    String getRefName();

    /**
     * 获取依赖的类型
     */
    Class<?> getType();

    /**
     * 判断依赖是否匹配
     */
    boolean isMatch(BeanDefinition beanDefinition);

}
