package com.springframework.beans.factory.support;

import com.springframework.beans.factory.DisposableBean;
import com.springframework.beans.factory.ObjectFactory;
import com.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegister implements SingletonBeanRegistry {

    private Map<String ,Object> singletonObjects=new HashMap<>();
    private Map<String,Object>earlySingletonObject=new HashMap<>();
    private Map<String , ObjectFactory<?>>singletonFactories=new HashMap<>();
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();
    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject=singletonObjects.get(beanName);
        if(singletonObject==null){
            singletonObject=earlySingletonObject.get(beanName);
            if(singletonObject==null){
                ObjectFactory<?>singletonFactory=singletonFactories.get(beanName);
                if(singletonFactory!=null){
                    singletonObject= singletonFactory.getObject();
                    earlySingletonObject.put(beanName,singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    @Override
    public void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject); // 1
        earlySingletonObject.remove(beanName); // 2
        singletonFactories.remove(beanName); // 3
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        singletonFactories.put(beanName, singletonFactory);
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    public void destroySingletons(){

        ArrayList<String>beanNames=new ArrayList<>(disposableBeans.keySet());
        for(String beanName:beanNames){
            DisposableBean remove = disposableBeans.remove(beanName);
            try {
                remove.destroy();
            } catch (Exception e) {
                throw new RuntimeException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }
}
