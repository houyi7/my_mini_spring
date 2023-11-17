package com.springframework.adapter;

import com.springframework.aop.Advisor;

import net.sf.cglib.proxy.MethodProxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class AfterReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice {
    public void setAdvice(AfterReturningAdvice advice) {
        this.advice = advice;
    }

    private AfterReturningAdvice advice;
    public AfterReturningAdviceInterceptor(){}

    public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object retVal= mi.proceed();
        advice.afterReturning(retVal,  mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }
}
