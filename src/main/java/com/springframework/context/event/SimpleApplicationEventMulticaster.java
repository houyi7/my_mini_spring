package com.springframework.context.event;

import com.springframework.beans.BeansException;
import com.springframework.beans.factory.BeanFactory;
import com.springframework.context.ApplicationEvent;
import com.springframework.context.ApplicationEventPublisher;
import com.springframework.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{
    public SimpleApplicationEventMulticaster(BeanFactory beanFactory){
        setBeanFactory(beanFactory);
    }


    @Override
    public void multicastEvent(ApplicationEvent event) {
        for(ApplicationListener<ApplicationEvent>applicationListener:applicationListeners){
            if(supportsEvent(applicationListener,event)){
                applicationListener.onApplicationEvent(event);
            }
        }
    }

    private boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
        Type type =applicationListener.getClass().getGenericInterfaces()[0];
        Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        String typeName = actualTypeArgument.getTypeName();
        Class<?>eventClassName;
        try {
            eventClassName =Class.forName(typeName);
        }catch (ClassNotFoundException e){
            throw new BeansException("wrong event class name: " + typeName);
        }

        return eventClassName.isAssignableFrom(event.getClass());

    }
}
