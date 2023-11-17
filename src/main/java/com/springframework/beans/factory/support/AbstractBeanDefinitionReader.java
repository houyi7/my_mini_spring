package com.springframework.beans.factory.support;

import com.springframework.beans.BeansException;
import com.springframework.core.io.DefaultResourceLoader;
import com.springframework.core.io.ResourceLoader;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    private BeanDefinitionRegistry beanDefinitionRegistry;
    private ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry, ResourceLoader resourceLoader) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.resourceLoader = resourceLoader;
    }
    protected  AbstractBeanDefinitionReader (BeanDefinitionRegistry beanDefinitionRegistry){
        this(beanDefinitionRegistry,new DefaultResourceLoader());
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return beanDefinitionRegistry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void loadBeanDefinitions(String[]locations) throws BeansException {
        for (String location:locations){
            loadBeanDefinitions(location);
        }
    }
}
