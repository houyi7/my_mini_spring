package com.springframework.aop;

import com.springframework.aop.framework.AdvisorChainFactory;
import com.springframework.aop.framework.DefaultAdvisorChainFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdvisedSupport {
    private boolean proxyTargetClass =true;

    private TargetSource targetSource;

    private MethodMatcher methodMatcher;

    protected transient Map<Integer, List<Object>>methodCache;

    AdvisorChainFactory advisorChainFactory =new DefaultAdvisorChainFactory();

    private List<Advisor>  advisors=new ArrayList<>();
    public AdvisedSupport() {
        this.methodCache = new ConcurrentHashMap<>(32);
    }
    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }

    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

    public void setMethodMatcher(MethodMatcher methodMatcher) {
        this.methodMatcher = methodMatcher;
    }

    public Map<Integer, List<Object>> getMethodCache() {
        return methodCache;
    }

    public void setMethodCache(Map<Integer, List<Object>> methodCache) {
        this.methodCache = methodCache;
    }

    public AdvisorChainFactory getAdvisorChainFactory() {
        return advisorChainFactory;
    }

    public void setAdvisorChainFactory(AdvisorChainFactory advisorChainFactory) {
        this.advisorChainFactory = advisorChainFactory;
    }

    public List<Advisor> getAdvisors() {
        return advisors;
    }
    public void addAdvisor(Advisor advisor) {
        advisors.add(advisor);
    }

    public void setAdvisors(List<Advisor> advisors) {
        this.advisors = advisors;
    }
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        Integer cacheKey=method.hashCode();
        List<Object>cached=methodCache.get(cacheKey);
        if(cached==null){
            cached=advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(
                    this,method,targetClass);
            methodCache.put(cacheKey,cached);
        }
        return cached;
    }
}
