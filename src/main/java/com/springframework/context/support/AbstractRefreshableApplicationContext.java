package com.springframework.context.support;

import com.springframework.beans.BeansException;
import com.springframework.beans.factory.support.DefaultListableBeanFactory;

public abstract class AbstractRefreshableApplicationContext  extends AbstractApplicationContext{

    private DefaultListableBeanFactory beanFactory;
    protected final void refreshBeanFactory()throws BeansException{
        DefaultListableBeanFactory beanFactory=creatBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);


    private DefaultListableBeanFactory creatBeanFactory() {
      return new DefaultListableBeanFactory();
    }
    public DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }


}
