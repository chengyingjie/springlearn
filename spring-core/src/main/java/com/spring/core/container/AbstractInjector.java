package com.spring.core.container;

import java.util.List;


public abstract class AbstractInjector implements Injector {
    protected List<InjectionData> injectionData;

    public AbstractInjector(List<InjectionData> injectionData) {
        this.injectionData = injectionData;
    }

    @Override
    public boolean hasDependence(BeanDefinition beanDefinition) {
        for (InjectionData injectionData : this.injectionData) {
            if (injectionData.isMatch(beanDefinition)) {
                return true;
            }
        }
        return false;
    }

}
