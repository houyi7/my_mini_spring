package com.springframework.beans.factory.config;

import com.springframework.beans.PropertyValues;

public class BeanDefinition {
    public static String SCOPE_SINGLETON="singleton";
    public static String SCOPE_PROTOTYPE="prototype";

    private Class beanClass;
    private PropertyValues propertyValues;
    private String initMethodName;
    private String destroyMethodName;
    /**
     * 作用域 默认单例Bean
     */
    private String scope = SCOPE_SINGLETON;

    private boolean singleton = true;

    private boolean prototype = false;

    /*/*
        懒加载
     */
    private boolean lazyInit=false;

    public BeanDefinition(Class beanClass) {
        this(beanClass, null);
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    public boolean isSingleton() {
        return this.singleton;
    }

    public static String getScopeSingleton() {
        return SCOPE_SINGLETON;
    }

    public static void setScopeSingleton(String scopeSingleton) {
        SCOPE_SINGLETON = scopeSingleton;
    }

    public static String getScopePrototype() {
        return SCOPE_PROTOTYPE;
    }

    public static void setScopePrototype(String scopePrototype) {
        SCOPE_PROTOTYPE = scopePrototype;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public String getScope() {
        return scope;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public boolean isPrototype() {
        return this.prototype;
    }


}
