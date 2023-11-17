package com.springframework.beans.factory.support;

import com.springframework.beans.BeansException;
import com.springframework.beans.factory.ConfigurableListableBeanFactory;
import com.springframework.beans.factory.config.BeanDefinition;

import java.util.*;
import java.util.jar.JarEntry;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory,BeanDefinitionRegistry {

   private Map<String ,BeanDefinition>beanDefinitionMap=new HashMap<>();





    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
            beanDefinitionMap.put(beanName,beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition= (BeanDefinition) beanDefinitionMap.get(beanName);
        if (beanDefinition ==null){
            throw new BeansException("No bean named '" + beanName + "' is defined");
        }
        return beanDefinition;
    }
    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        List<String> beanNames = new ArrayList<>();
        Set<Map.Entry<String, BeanDefinition>> entries = beanDefinitionMap.entrySet();
        for (Map.Entry<String, BeanDefinition>entry:entries){
            if(requiredType.isAssignableFrom(entry.getValue().getBeanClass())){
                beanNames.add(entry.getKey());
            }
        }
        if(beanNames.size()>1){
            throw new  BeansException(requiredType + "expected single bean but found " +
                    beanNames.size() + ": " + beanNames);
        }
        return (T)getBean(beanNames.get(0));

    }



    /**
     * 是否包含指定名称的BeanDefinition
     *
     * @param beanName
     * @return
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    /**
     * 提前实例化所有单例实例
     *
     * @throws BeansException
     */
    @Override
    public void preInstantiateSingletons() throws BeansException {
        beanDefinitionMap.forEach((beanName,beanDefinition)->{
            if(beanDefinition.isSingleton()&&!beanDefinition.isLazyInit())
            getBean(beanName);
        });
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String,T>result=new HashMap<>();
        beanDefinitionMap.forEach((beanName,beanDefinition)->{

            if(type.isAssignableFrom(beanDefinition.getBeanClass())){
                result.put(beanName,(T)getBean(beanName));
            }
        });
        return result;

    }

    /**
     * 返回定义的所有bean的名称
     *
     * @return
     */
    @Override
    public String[] getBeanDefinitionNames() {
        Set<String> beanNames = beanDefinitionMap.keySet();
        return beanNames.toArray(new String[beanNames.size()]);
    }


    /**
     * 执行BeanPostProcessors的postProcessAfterInitialization方法
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */

    /**
     * 销毁单例bean
     */









    //-------------------------------------------------------


}
