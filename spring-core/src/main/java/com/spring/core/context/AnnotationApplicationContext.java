package com.spring.core.context;

import com.spring.core.annotation.*;
import com.spring.core.container.BeanContainer;
import com.spring.core.container.BeanDefinition;
import com.spring.core.container.InjectionProvider;
import com.spring.core.container.Resolver;
import com.spring.core.enums.ScopeTypeEnum;
import com.spring.core.exception.ErrorCode;
import com.spring.core.exception.SpringLearnException;
import com.spring.core.utils.ClassLoaderUtil;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ApplicationContext的实现类，负责扫描注解，并将bean注册到容器中
 */
public class AnnotationApplicationContext implements ApplicationContext {

    private String[] scanPaths;
    private BeanContainer beanContainer;

    public AnnotationApplicationContext(String... scanPaths) throws SpringLearnException {

        if (scanPaths.length < 1) {
            throw new SpringLearnException(ErrorCode.ILLEGAL_PARAM);
        }

        this.beanContainer = new BeanContainer();
        this.scanPaths = scanPaths;
        this.doScan();
    }

    private void doScan() throws SpringLearnException {

        Set<Class<?>> candidates = new HashSet<>();
        for (String scanPath : scanPaths) {
            candidates.addAll(ClassLoaderUtil.getClasses(scanPath));
        }

        for (Class<?> candidate : candidates) {
            if (candidate.isAnnotation() || candidate.isInterface() || Modifier.isAbstract(candidate.getModifiers())) {
                continue;
            }

            String name = getComponentName(candidate);
            if (name != null) {
                boolean isSingleton = false;

                Scope scope = candidate.getAnnotation(Scope.class);
                if (scope != null) {
                    if (ScopeTypeEnum.SINGLETON.equals(scope.value())) {
                        isSingleton = true;
                    } else if (!ScopeTypeEnum.PROTOTYPE.equals(scope.value())) {
                        throw new SpringLearnException(ErrorCode.ILLEGAL_PARAM);
                    }
                } else if (candidate.getAnnotation(Singleton.class) != null) {
                    isSingleton = true;
                }

                if ("".equals(name)) {
                    name = candidate.getSimpleName();
                }

                BeanDefinition beanDefinition = new BeanDefinition(candidate, isSingleton, name);
                beanDefinition.setInjectorProvider(new InjectionProvider(candidate));

                beanContainer.register(beanDefinition.getName(), beanDefinition);
            }
        }

        Map<String, BeanDefinition> beanDefinationMap = beanContainer.getBeanDefinations();
        Resolver resolver = new Resolver(beanContainer);
        for (Map.Entry<String, BeanDefinition> entry : beanDefinationMap.entrySet()) {
            resolver.resolve(entry.getValue());
        }
    }

    @Override
    public Object getBean(String beanName) {
        return beanContainer.getBean(beanName);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        BeanDefinition beanDefinition = beanContainer.getBean(clazz);
        if (beanDefinition != null) {
            return (T) beanDefinition.getInstance();
        }
        return null;
    }

    @Override
    public <T> Map<String, T> getBeans(Class<T> clazz) {
        Map<String, T> beanMap = new HashMap<>(8);
        for (Map.Entry<String, BeanDefinition> entry : beanContainer.getBeans(clazz).entrySet()) {
            beanMap.put(entry.getKey(), (T) entry.getValue().getInstance());
        }
        return beanMap;
    }

    private String getComponentName(Class<?> candidate) {

        Component component = candidate.getAnnotation(Component.class);
        if (component != null) {
            return component.value();
        }

        Controller controller = candidate.getAnnotation(Controller.class);
        if (controller != null) {
            return controller.value();
        }

        Service service = candidate.getAnnotation(Service.class);
        if (service != null) {
            return service.value();
        }

        Repository repository = candidate.getAnnotation(Repository.class);
        if (repository != null) {
            return repository.value();
        }

        return null;
    }
}
