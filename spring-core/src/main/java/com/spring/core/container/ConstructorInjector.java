package com.spring.core.container;

import com.spring.core.exception.SpringLearnException;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;


public class ConstructorInjector extends AbstractInjector {

    private Constructor<?> constructor;

    public ConstructorInjector(Constructor<?> constructor, List<InjectionData> injectionData) {
        super(injectionData);
        this.constructor = constructor;
    }

    /**
     * 构造器注入
     */
    public Object inject(BeanDefinition beanDefinition) throws SpringLearnException {
        return inject(null, beanDefinition);
    }

    @Override
    public Object inject(Object instance, BeanDefinition beanDefinition) throws SpringLearnException {

        if (constructor == null) {
            return instance;
        }

        if (injectionData != null && injectionData.size() > 0) {
            List<Object> args = new LinkedList<>();
            //遍历构造函数的参数依赖信息
            for (InjectionData injectionData : this.injectionData) {
                BeanDefinition bean = injectionData.getBean();
                if (bean != null) {
                    //添加实例作为参数
                    args.add(bean.getInstance());
                }
            }

            if (args.size() > 0) {
                //反射调用构造器，构造对象实例
                try {
                    instance = constructor.newInstance(args.toArray());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return instance;
    }

    public List<InjectionData> getConstructorParameterDatas() {
        return injectionData;
    }
}
