package com.springframework.beans.factory.config;

import com.springframework.beans.BeansException;

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean,String beanName) throws BeansException;
    Object postProcessAfterInitialization(Object bean,String beanName) throws BeansException;

}
