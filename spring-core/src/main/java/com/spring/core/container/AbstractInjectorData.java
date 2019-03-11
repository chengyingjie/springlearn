package com.spring.core.container;


public abstract class AbstractInjectorData implements InjectionData {
    /**
     * 默认依赖名称
     */
    private String defalultName;
    /**
     * 指定依赖名称
     */
    private String refName;
    /**
     * 依赖的BeanDefination实例
     */
    private BeanDefinition bean;


    public AbstractInjectorData(String defalultName, String refName) {
        this.defalultName = defalultName;
        this.refName = refName;
    }

    @Override
    public void setDefaultName(String defaultName) {
        this.defalultName = defaultName;
    }

    @Override
    public String getDefaultName() {
        return defalultName;
    }

    @Override
    public String getRefName() {
        return refName;
    }

    @Override
    public void setBean(BeanDefinition bean) {
        this.bean = bean;
    }

    @Override
    public BeanDefinition getBean() {
        return this.bean;
    }

    @Override
    public boolean isMatch(BeanDefinition beanDefinition) {
        if (refName != null && refName.equals(beanDefinition.getName())) {
            return true;
        } else if (defalultName.equals(beanDefinition.getName())) {
            return true;
        } else {
            Class<?> type = getType();
            return beanDefinition.isType(type);
        }
    }
}
