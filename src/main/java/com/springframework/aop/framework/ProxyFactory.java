package com.springframework.aop.framework;

import com.springframework.aop.AdvisedSupport;

import javax.jws.Oneway;

public class ProxyFactory extends AdvisedSupport {


    public Object getProxy(){
        return createAopProxy().getProxy();
    }

    private AopProxy createAopProxy(){
        if(this.isProxyTargetClass()||this.getTargetSource().getTargetClass().length==0){
            return new CglibAopProxy(this);
        }
        return new JdkDynamicAopProxy(this);
    }
}
