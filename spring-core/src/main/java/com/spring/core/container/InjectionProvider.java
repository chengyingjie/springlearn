package com.spring.core.container;


import com.spring.core.annotation.Autowired;
import com.spring.core.annotation.Qualifier;
import com.spring.core.exception.ErrorCode;
import com.spring.core.exception.SpringLearnException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;


public class InjectionProvider {

    private ConstructorInjector constructorInjector;
    private FieldInjector fieldInjector;

    public InjectionProvider(Class<?> clazz) throws SpringLearnException {
        resolve(clazz);
    }

    private void resolve(Class<?> candidate) throws SpringLearnException {
        resolveConstructorInfo(candidate);
        resolveFieldInfo(candidate);
    }

    private void resolveConstructorInfo(Class<?> candidate) throws SpringLearnException {

        if (candidate.getDeclaredConstructors().length > 1) {
            throw new SpringLearnException(ErrorCode.ILLEGAL_PARAM.getCode(), "超过一个构造器注入，candidate:" + candidate.getName());
        }

        Constructor<?>[] constructors = candidate.getDeclaredConstructors();
        List<InjectionData> injectionDataInfo = getParameterInjectDatas(constructors[0].getParameters());
        this.constructorInjector = new ConstructorInjector(constructors[0], injectionDataInfo);
    }

    private void resolveFieldInfo(Class<?> candidate) throws SpringLearnException {
        List<InjectionData> fieldInjectionData = new ArrayList<>();

        for (Field field : candidate.getDeclaredFields()) {
            field.setAccessible(true);

            Autowired autoWired = field.getAnnotation(Autowired.class);
            if (autoWired == null) {
                continue;
            }

            if (Modifier.isFinal(field.getModifiers())) {
                throw new SpringLearnException(ErrorCode.ILLEGAL_PARAM.getCode(), "常量类属性字段，不可实例化，field: " + field.getName());
            }

            String refName = null;
            Qualifier qualifier = field.getAnnotation(Qualifier.class);
            if (qualifier != null) {
                refName = qualifier.value();
            }

            InjectionData injectionData = (InjectionData) new FieldInjectorData(field.getType().getSimpleName(), refName, field);
            fieldInjectionData.add(injectionData);
        }

        this.fieldInjector = new FieldInjector(fieldInjectionData);
    }

    private List<InjectionData> getParameterInjectDatas(Parameter[] parameters) {

        List<InjectionData> parameterInjectionData = new ArrayList<>();
        if (parameters.length == 0) {
            return parameterInjectionData;
        }

        for (Parameter parameter : parameters) {
            InjectionData injectionData = (InjectionData) new ParameterInjectorData(parameter.getType().getSimpleName(), parameter.getName(), parameter);
            parameterInjectionData.add(injectionData);
        }

        return parameterInjectionData;
    }


    /**
     * 注入依赖
     */
    public Object doInject(BeanDefinition beanDefinition) throws SpringLearnException {

        Object instance = null;
        if (constructorInjector != null) {
            instance = this.constructorInjector.inject(beanDefinition);
        }

        if (instance == null) {
            try {
                instance = beanDefinition.getClazz().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new SpringLearnException(ErrorCode.SYSTEM_EXCEPTION.getCode(), "实例化bean失败, beanName:" + beanDefinition.getName(), e);
            }
        }

        if (fieldInjector != null) {
            instance = this.fieldInjector.inject(instance, beanDefinition);
        }

        return instance;
    }

    /**
     * 判断当前bean是否依赖beanDefination，如果是，返回true，否则返回false
     */
    public boolean hasDependence(BeanDefinition beanDefinition) {

        if (constructorInjector != null && constructorInjector.hasDependence(beanDefinition)) {
            return true;
        }

        if (fieldInjector != null && fieldInjector.hasDependence(beanDefinition)) {
            return true;
        }

        return false;
    }

    public List<InjectionData> getConstructorParameterDatas() {
        if (constructorInjector != null) {
            List<InjectionData> injectionData = constructorInjector.getConstructorParameterDatas();
            if (injectionData != null) {
                return injectionData;
            }
        }
        return null;
    }

    public List<InjectionData> getFieldInjectorDatas() {
        if (fieldInjector != null) {
            List<InjectionData> injectionData = fieldInjector.getFieldInjectorDatas();
            if (injectionData != null) {
                return injectionData;
            }
        }
        return null;
    }
}
