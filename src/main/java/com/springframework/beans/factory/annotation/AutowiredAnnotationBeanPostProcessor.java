package com.springframework.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.TypeUtil;
import com.springframework.beans.BeansException;
import com.springframework.beans.PropertyValues;
import com.springframework.beans.factory.BeanFactory;
import com.springframework.beans.factory.BeanFactoryAware;
import com.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.springframework.core.convert.ConversionService;


import javax.swing.text.html.ObjectView;
import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory configurableListableBeanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        configurableListableBeanFactory=(ConfigurableListableBeanFactory)beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        Class<?>clazz=bean.getClass();
        Field []fields=clazz.getDeclaredFields();
        for (Field field:fields){
            Value value=field.getAnnotation(Value.class);
            if(value!=null){
                Object val=value.value();
                val=configurableListableBeanFactory.resolveEmbeddedValue((String)val);
                Class<?>sourceType=val.getClass();
                Class<?>targetType=(Class<?>) TypeUtil.getType(field);

                ConversionService conversionService = configurableListableBeanFactory.getConversionService();
                if (conversionService != null) {
                    if (conversionService.canConvert(sourceType, targetType)) {
                        val = conversionService.convert(val, targetType);
                    }
                }

                BeanUtil.setFieldValue(bean, field.getName(), val);
            }

            Autowired autowired=field.getAnnotation(Autowired.class);
            if(autowired!=null){
                Class<?>fieldType=field.getType();
                String dependentBeanName=null;
                Object dependentBean=null;
                Qualifier  qualifier=field.getAnnotation(Qualifier.class);
                if(qualifier!=null){
                    dependentBeanName=qualifier.value();
                    dependentBean=configurableListableBeanFactory.getBean(dependentBeanName,fieldType);
                }else {
                    dependentBean =configurableListableBeanFactory.getBean(fieldType);
                }
                BeanUtil.setFieldValue(bean,field.getName(),dependentBean);
            }

        }
        return pvs;
    }
}
