package com.springframework.beans.factory;

import com.springframework.beans.BeansException;

public interface BeanFactory {
    Object getBean(String name) throws BeansException;


    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    <T> T getBean(Class<T> requiredType) throws BeansException;

    boolean containsBean(String name);
}
