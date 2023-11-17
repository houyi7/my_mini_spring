package com.springframework.context.annotation;

import cn.hutool.core.util.StrUtil;
import com.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.springframework.beans.factory.config.BeanDefinition;
import com.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{
    public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor";

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages){
        for (String basePackage:basePackages){
            Set<BeanDefinition>beanDefinitions=findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition :beanDefinitions){
                // 解析bean的作用域
                String beanScope = resolveBeanScope(beanDefinition);
                if (StrUtil.isNotEmpty(beanScope)) {
                    beanDefinition.setScope(beanScope);
                }

                String beanName = determineBeanName(beanDefinition);
                //注册BeanDefinition
                registry.registerBeanDefinition(beanName, beanDefinition);
            }

        }
       registry.registerBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME, new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?>clazz=beanDefinition.getBeanClass();
        Scope scope =clazz.getAnnotation(Scope.class);
        if (scope!=null){
            return scope.value();
        }
        return StrUtil.EMPTY;
    }


}
