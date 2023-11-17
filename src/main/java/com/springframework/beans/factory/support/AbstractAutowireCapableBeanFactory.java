package com.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.springframework.beans.BeansException;
import com.springframework.beans.PropertyValue;
import com.springframework.beans.PropertyValues;
import com.springframework.beans.factory.BeanFactoryAware;
import com.springframework.beans.factory.DisposableBean;
import com.springframework.beans.factory.InitializingBean;
import com.springframework.beans.factory.ObjectFactory;
import com.springframework.beans.factory.config.*;
import com.springframework.context.support.DispoeableBeanAdapter;
import com.springframework.core.convert.ConversionService;
import javafx.concurrent.ScheduledService;

import java.lang.reflect.Method;
import java.util.function.DoublePredicate;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {



    private InstantiationStrategy instantiationStrategy=new SimpleInstantiationStrategy();



    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
        if (bean != null) {
            return bean;
        }
        return doCreateBean(beanName,beanDefinition);
    }

    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean;

        bean=createBeanInstance(beanDefinition);
        if(beanDefinition.isSingleton()){
            Object finalBean=bean;
            addSingletonFactory(beanName, new ObjectFactory<Object>() {
                @Override
                public Object getObject() throws BeansException {
                    return getEarlyBeanReference(beanName,beanDefinition,finalBean);     //这里也可以是代理对象的加入方法
                }
            });
        }
        boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, bean);
        if (!continueWithPropertyPopulation) {
            return bean;
        }
        //在设置bean属性之前，允许BeanPostProcessor修改属性值
        applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
        //-----------------------------------------
        //为bean填充属性
        applyPropertyValues(beanName,bean,beanDefinition);

        //执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
        bean = initializeBean(beanName,bean,beanDefinition);        //这里是代理对象的植入方法


        registerDisposableBeanIfNecessary(beanName,bean,beanDefinition);


        Object exposedObject= bean;
        if (beanDefinition.isSingleton()) {
            //如果有代理对象，此处获取代理对象
            exposedObject = getSingleton(beanName);
            addSingleton(beanName, exposedObject);
        }
        return exposedObject;

    }

    private void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for (BeanPostProcessor beanPostProcessor:getBeanPostProcessors()){
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                PropertyValues propertyValues = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()){
                    beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                }
            }
        }
    }

    private boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
        boolean continueWithPropertyPopulation = true;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                if (!((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessAfterInstantiation(bean, beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
        return continueWithPropertyPopulation;
    }

    private void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if(beanDefinition.isSingleton()){
            if(bean instanceof DisposableBean ||StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())){
                registerDisposableBean(beanName,new DispoeableBeanAdapter(bean,beanName,beanDefinition.getDestroyMethodName()));
            }
        }
    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        Object wrappedBean =applyBeanPostProcessorsBeforeInitialization(bean,beanName);

         try {
        invokeInitMethods(beanName,wrappedBean,beanDefinition);
         }catch (Throwable t){
             throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", t);
           }

        wrappedBean=applyBeanPostProcessorsAfterInitialization(bean,beanName);
        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) throws Throwable{
            if(wrappedBean instanceof InitializingBean){
                ((InitializingBean)wrappedBean).afterPropertiesSet();
            }
        String initMethodName = beanDefinition.getInitMethodName();
        if(StrUtil.isNotEmpty(initMethodName)){

                Method initMethod= ClassUtil.getPublicMethod(beanDefinition.getBeanClass(),initMethodName);
            if (initMethod == null) {
                throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
            }
            initMethod.invoke(wrappedBean);
          }
    }

    private void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {

        for(PropertyValue propertyValue:beanDefinition.getPropertyValues().getPropertyValues()){
            String name=propertyValue.getName();
            Object value=propertyValue.getValue();
            if(value instanceof BeanReference){
                BeanReference beanReference=(BeanReference) value;
                value=getBean(beanReference.getBeanName());
            }else {
                Class<?>sourceType=value.getClass();
                Class<?> targetType = (Class<?>) TypeUtil.getFieldType(bean.getClass(), name);
                ConversionService conversionService=getConversionService();
                if(conversionService!=null){
                    if(conversionService.canConvert(sourceType,targetType)){
                        value=conversionService.convert(value,targetType);
                    }
                }
            }
            BeanUtil.setFieldValue(bean,name,value);
        }

    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result=existingBean;
        for(BeanPostProcessor beanPostProcessor:getBeanPostProcessors()){
            Object current=beanPostProcessor.postProcessBeforeInitialization(result,beanName);
            if(current==null){
                current=result;
            }
            result=current;
        }
        return result;
    }
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result=existingBean;
        for(BeanPostProcessor beanPostProcessor:getBeanPostProcessors()){
            Object current=beanPostProcessor.postProcessAfterInitialization(result,beanName);
            if(current==null){
                current=result;
            }
            result=current;
        }
        return result;
    }

    private Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object finalBean) {
        Object exposedObject=finalBean;
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                exposedObject = ((InstantiationAwareBeanPostProcessor) bp).getEarlyBeanReference(exposedObject, beanName);
                if (exposedObject == null) {
                    return exposedObject;
                }
            }
        }
        return exposedObject;
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) {
        return getInstantiationStrategy().instantiate(beanDefinition);
    }
    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }
    private Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean=applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(),beanName);
        if (bean != null) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }


    protected Object applyBeanPostProcessorsBeforeInstantiation(Class beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}


