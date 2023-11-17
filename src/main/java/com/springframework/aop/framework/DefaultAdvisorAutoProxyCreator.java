package com.springframework.aop.framework;

import com.springframework.aop.Advisor;
import com.springframework.aop.ClassFilter;
import com.springframework.aop.Pointcut;
import com.springframework.aop.TargetSource;
import com.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.springframework.beans.BeansException;
import com.springframework.beans.PropertyValues;
import com.springframework.beans.factory.BeanFactory;
import com.springframework.beans.factory.BeanFactoryAware;
import com.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor , BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;

    private Set<Object> earlyProxyReferences = new HashSet<>();


    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass)
                || Pointcut.class.isAssignableFrom(beanClass)
                || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }

        return bean;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }

    private Object wrapIfNecessary(Object bean, String beanName) {
        if(isInfrastructureClass(bean.getClass())){
            return bean;
        }
        Collection<AspectJExpressionPointcutAdvisor> values = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        ProxyFactory proxyFactory=new ProxyFactory();
        for(AspectJExpressionPointcutAdvisor advisor:values){
            ClassFilter classFilter=advisor.getPointcut().getClassFilter();
            if(classFilter.matches(bean.getClass())){
                TargetSource targetSource=new TargetSource(bean);
                proxyFactory.setTargetSource(targetSource);
                proxyFactory.addAdvisor(advisor);
                proxyFactory.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            }
            if (!proxyFactory.getAdvisors().isEmpty()) {
                return proxyFactory.getProxy();
            }
        }
        return bean;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }
}
