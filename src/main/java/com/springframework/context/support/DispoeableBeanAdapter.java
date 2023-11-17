package com.springframework.context.support;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.springframework.beans.BeansException;
import com.springframework.beans.factory.DisposableBean;

import java.lang.reflect.Method;

public class DispoeableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;
    private final String destroyMethodeName;

    public DispoeableBeanAdapter(Object bean, String beanName, String destroyMethode) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodeName = destroyMethode;
    }

    @Override
    public void destroy() throws Exception {

        if(bean instanceof DisposableBean){
            ((DisposableBean)bean).destroy();
        }

        if(StrUtil.isNotEmpty(destroyMethodeName)&&!(bean instanceof DisposableBean &&"destroy".equals(destroyMethodeName))){
             Method destroyMethod = bean.getClass().getDeclaredMethod(destroyMethodeName);
           // Method destroyMethod = ClassUtil.getPublicMethod(bean.getClass(), destroyMethodeName);
            if(destroyMethod==null){
                throw new BeansException("Couldn't find a destroy method named '" + destroyMethod + "' on bean with name '" + beanName + "'");
            }
            destroyMethod.invoke(bean);
        }

    }
}
