package com.springframework.aop.framework;

import com.springframework.aop.AdvisedSupport;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.aspectj.weaver.reflect.ReflectionFastMatchInfo;

import java.lang.reflect.Method;
import java.util.List;

public class CglibAopProxy implements AopProxy{

    private  final AdvisedSupport advised;

    public CglibAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        // 创建动态代理增强类
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
        enhancer.setCallback(new DynamicAdvisedInterceptor(advised));
        return enhancer.create();
    }


    private class DynamicAdvisedInterceptor implements MethodInterceptor {

        private final AdvisedSupport advised;


        private DynamicAdvisedInterceptor(AdvisedSupport advised) {
            this.advised = advised;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            Object target=advised.getTargetSource().getTarget();
            Class<?>targetClass=target.getClass();
            Object retVal=null;
            List<Object>chain=advised.getInterceptorsAndDynamicInterceptionAdvice(method,targetClass);
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(o, target, method, objects, targetClass, chain, methodProxy);
            if (chain == null || chain.isEmpty()) {
                //代理方法
                retVal = methodProxy.invoke(target, objects);
            } else {
                retVal = methodInvocation.proceed();
            }
            return retVal;
        }
    }
    private static class CglibMethodInvocation extends ReflectiveMethodInvocation{
        private final MethodProxy methodProxy;
        public CglibMethodInvocation(Object proxy, Object target, Method method,
                                     Object[] arguments, Class<?> targetClass,
                                     List<Object> interceptorsAndDynamicMethodMatchers, MethodProxy methodProxy) {
            super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatchers);
            this.methodProxy = methodProxy;
        }
        @Override
        public Object proceed() throws Throwable {
            return super.proceed();
        }
    }


}

