package com.spring.core.container;

import com.alibaba.fastjson.JSON;
import com.spring.core.exception.ErrorCode;
import com.spring.core.exception.SpringLearnException;

import java.util.Map;


public class Resolver {

    private BeanContainer beanContainer;

    public Resolver(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
    }

    public void resolve(BeanDefinition beanDefinition) throws SpringLearnException {

        //如果已经解析过了，则返回
        if (beanDefinition.isResolved()) {
            return;
        }

        //优先解析父类
        Class<?> superClass = beanDefinition.getClazz().getSuperclass();
        if (superClass != null && superClass != Object.class) {
            for (BeanDefinition bean : beanContainer.getBeans(superClass).values()) {
                if (bean != beanDefinition) {
                    //递归解析父类
                    resolve(bean);
                }
            }
        }

        InjectionProvider injectionProvider = beanDefinition.getInjectorProvider();
        if (injectionProvider != null) {
            //如果有构造器注入，则先解析构造器注入依赖
            if (injectionProvider.getConstructorParameterDatas() != null) {
                for (InjectionData paramInjectorData : injectionProvider.getConstructorParameterDatas()) {
                    doResolve(beanDefinition, paramInjectorData, injectionProvider);
                }
            }

            //如果有字段注入，则解析字段注入依赖
            if (injectionProvider.getFieldInjectorDatas() != null) {
                for (InjectionData fieldInjectorData : injectionProvider.getFieldInjectorDatas()) {
                    doResolve(beanDefinition, fieldInjectorData, injectionProvider);
                }
            }
        }

        beanDefinition.setResolved(true);
    }

    private void doResolve(BeanDefinition beanDefinition, InjectionData injectionData, InjectionProvider injectionProvider) {

        BeanDefinition refBean = null;

        Map<String, BeanDefinition> beanDefinationMap = beanContainer.getBeanDefinations();
        //判断依赖组件是否存在，先查找指定名称的依赖，如果不存在，则按找默认名称去查找，仍然不存在，则再按类型匹配
        if (injectionData.getRefName() != null && beanDefinationMap.containsKey(injectionData.getRefName())) {
            refBean = beanDefinationMap.get(injectionData.getRefName());
        } else if (beanDefinationMap.containsKey(injectionData.getDefaultName())) {
            refBean = beanDefinationMap.get(injectionData.getDefaultName());
        } else {
            for (BeanDefinition bean : beanDefinationMap.values()) {
                if (bean.isType(injectionData.getType()) || bean.isSubType(injectionData.getType())) {
                    refBean = bean;
                    break;
                }
            }
        }

        //判断依赖是否存在，如果不存在，则抛出异常。如果依赖存在，但有相互引用的情况，也抛出异常
        if (refBean == null) {
            return;
        }

        if (beanDefinition == refBean || injectionProvider.hasDependence(beanDefinition)) {
            throw new SpringLearnException(ErrorCode.ILLEGAL_PARAM.getCode(), "循环依赖, beanDefinition:" + JSON.toJSONString(beanDefinition));
        }

        //设置依赖信息
        injectionData.setBean(refBean);
    }
}
