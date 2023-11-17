package com.springframework.aop.framework;

import com.springframework.aop.AdvisedSupport;
import com.springframework.aop.Advisor;
import com.springframework.aop.MethodMatcher;
import com.springframework.aop.PointcutAdvisor;
import jdk.internal.dynalink.support.AutoDiscovery;
import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.weaver.loadtime.definition.Definition;
import sun.security.krb5.internal.PAData;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DefaultAdvisorChainFactory implements AdvisorChainFactory{
    @Override
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(AdvisedSupport config, Method method, Class<?> targetClass) {
        Advisor[]advisors=config.getAdvisors().toArray(new Advisor[0]);
        List<Object>interceptorList=new ArrayList<>(advisors.length);
        Class<?>actualClass=(targetClass!=null?targetClass:method.getDeclaringClass())  ;
        for(Advisor advisor:advisors){
            if(advisor instanceof PointcutAdvisor){
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                if(pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)){
                    MethodMatcher mm=pointcutAdvisor.getPointcut().getMethodMatcher();
                    boolean match= mm.matches(method,actualClass);
                    if(match){
                        interceptorList.add((MethodInterceptor)advisor.getAdvice()) ;
                    }
                }
            }
        }
        return interceptorList;

    }
}
