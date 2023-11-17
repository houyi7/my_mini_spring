package com.springframework.beans.factory.support;

import com.springframework.beans.BeansException;
import com.springframework.beans.factory.BeanFactory;
import com.springframework.beans.factory.FactoryBean;
import com.springframework.beans.factory.StringValueResolver;
import com.springframework.beans.factory.config.BeanDefinition;
import com.springframework.beans.factory.config.BeanPostProcessor;
import com.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.springframework.core.convert.ConversionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegister implements ConfigurableBeanFactory {

    private ConversionService conversionService;
    private final List<BeanPostProcessor>beanPostProcessors=new ArrayList<>();
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<StringValueResolver>();
    private Map<String ,Object >factoryBeanObjectCache=new HashMap<>();
   protected abstract Object createBean(String beanName,BeanDefinition beanDefinition);
    protected abstract BeanDefinition getBeanDefinition(String name)throws BeansException;
    @Override
    public Object getBean(String name) throws BeansException {
        Object sharedInstance=getSingleton(name);
        if(sharedInstance!=null){
            return getObjectForBeanInstance(sharedInstance,name);
        }
        BeanDefinition beanDefinition=getBeanDefinition(name);
        Object bean=createBean(name,beanDefinition);
        return getObjectForBeanInstance(bean,name);
    }

    private Object getObjectForBeanInstance(Object beanInstance, String name) {
        Object object = beanInstance;

        if(object instanceof FactoryBean){
            FactoryBean fb = (FactoryBean) object;
            try {
                if(fb.isSingleton()){
                    object = this.factoryBeanObjectCache.get(name);
                    if(object==null){
                        object=fb.getObject();
                        factoryBeanObjectCache.put(name,object);
                    }
                }else {
                    object=fb.getObject();
                }
            } catch (Exception e) {
                throw new BeansException("FactoryBean threw exception on object[" + name + "] creation", e);
            }

        }
        return object;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return ((T) getBean(name));
    }
    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }

    protected abstract boolean containsBeanDefinition(String name);

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }
    @Override
    public ConversionService getConversionService() {
        return conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }
}
