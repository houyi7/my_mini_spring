package com.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectiveMethodInvocation implements MethodInvocation {
    protected final Object proxy;
    protected final Object target;
    protected final Method method;
    protected final Object[]arguments;
    protected final Class<?>targetClass;
    protected final List<Object>interceptorsAndDynamicMethodMatches;

    private int currentInterceptorIndex=-1;


    public ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatches) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.targetClass = targetClass;
        this.interceptorsAndDynamicMethodMatches = interceptorsAndDynamicMethodMatches;
    }
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Object proceed() throws Throwable {
        if(this.currentInterceptorIndex ==this.interceptorsAndDynamicMethodMatches.size()-1){
            return method.invoke(target,arguments);
        }
        Object interceptorInterceptionAdivce=interceptorsAndDynamicMethodMatches.get(++currentInterceptorIndex);
        return ((MethodInterceptor)interceptorInterceptionAdivce).invoke(this);
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }

}
