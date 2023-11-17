package com.springframework.beans.factory.config;

public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);
    void addSingleton(String beanName, Object singleton);
}
