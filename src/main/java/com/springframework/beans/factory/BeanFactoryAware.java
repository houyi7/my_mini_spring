package com.springframework.beans.factory;

import com.springframework.beans.BeansException;

public interface BeanFactoryAware extends Aware{
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
