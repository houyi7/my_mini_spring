package com.springframework.beans.factory.config;

import com.springframework.beans.BeansException;
import com.springframework.beans.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{
    Object postProcessBeforeInstantiation(Class<?>beanClass,String beanName);
    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;


    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName)
            throws BeansException;


    default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
