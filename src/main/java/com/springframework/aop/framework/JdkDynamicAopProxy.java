package com.springframework.aop.framework;

import com.springframework.aop.AdvisedSupport;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
   private final AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(),advisedSupport.getTargetSource().getTargetClass(), this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target=advisedSupport.getTargetSource().getTarget();
        Class<?>targetClass=advisedSupport.getTargetSource().getTarget().getClass();
        List<Object>chain=advisedSupport.getInterceptorsAndDynamicInterceptionAdvice(method,targetClass);
        Object retVal = null;
        // 获取拦截器链
        if(chain==null){
            return method.invoke(target,args);
        }else {
            MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);

            retVal=invocation.proceed();
        }
        return retVal;
    }
}
