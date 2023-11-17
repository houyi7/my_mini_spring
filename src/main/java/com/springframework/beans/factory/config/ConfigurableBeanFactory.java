package com.springframework.beans.factory.config;

import com.springframework.beans.factory.HierarchicalBeanFactory;
import com.springframework.beans.factory.StringValueResolver;
import com.springframework.core.convert.ConversionService;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry{
   void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 销毁单例bean
     */
    void destroySingletons();
   void addEmbeddedValueResolver(StringValueResolver valueResolver);

    String resolveEmbeddedValue(String value);


   ConversionService getConversionService();
   void setConversionService(ConversionService conversionService );
}
