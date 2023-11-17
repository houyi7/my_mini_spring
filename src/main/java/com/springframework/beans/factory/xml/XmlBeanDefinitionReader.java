package com.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import com.springframework.beans.BeansException;
import com.springframework.beans.PropertyValue;
import com.springframework.beans.factory.config.BeanDefinition;
import com.springframework.beans.factory.config.BeanReference;
import com.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import com.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import com.springframework.core.io.Resource;
import com.springframework.core.io.ResourceLoader;
import com.sun.deploy.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.plugin2.ipc.InProcEvent;

import javax.print.attribute.standard.MediaSize;
import javax.xml.transform.sax.SAXResult;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.PlatformLoggingMXBean;
import java.util.List;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public static final String BEAN_ELEMENT = "bean";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String INIT_METHOD_ATTRIBUTE = "init-method";
    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
    public static final String SCOPE_ATTRIBUTE = "scope";
    public static final String LAZYINIT_ATTRIBUTE = "lazyInit";
    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    public static final String COMPONENT_SCAN_ELEMENT = "component-scan";


    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            InputStream inputStream=resource.getInputStream();
            try {
                doLoadBeanDefinitions(inputStream);
            }finally {
                inputStream.close();
            }
        } catch (IOException  | DocumentException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }

    }

    private void doLoadBeanDefinitions(InputStream inputStream) throws DocumentException {
        SAXReader reader=new SAXReader();
        Document document=reader.read(inputStream);

        Element root=document.getRootElement();

        Element componentScan =root.element(COMPONENT_SCAN_ELEMENT);
        if(componentScan!=null){
            String scanPath=componentScan.attributeValue(BASE_PACKAGE_ATTRIBUTE);
            if (StrUtil.isEmpty(scanPath)){
                throw new BeansException("The value of base-package attribute can not be empty or null");
            }
            scanPackage(scanPath);
        }

        List<Element>beanList=root.elements(BEAN_ELEMENT);
        for(Element bean:beanList){
            String beanId = bean.attributeValue(ID_ATTRIBUTE);
            String beanName = bean.attributeValue(NAME_ATTRIBUTE);
            String className = bean.attributeValue(CLASS_ATTRIBUTE);
            String initMethodName = bean.attributeValue(INIT_METHOD_ATTRIBUTE);
            String destroyMethodName = bean.attributeValue(DESTROY_METHOD_ATTRIBUTE);
            String beanScope = bean.attributeValue(SCOPE_ATTRIBUTE);
            String lazyInit = bean.attributeValue(LAZYINIT_ATTRIBUTE);
            Class<?> clazz;

            try {
                clazz =Class.forName(className);
            } catch (ClassNotFoundException e) {
               throw new RuntimeException(e);
            }
            beanName=StrUtil.isEmpty(beanId)?beanName:beanId;
            if(StrUtil.isEmpty(beanName)){
                beanName=StrUtil.lowerFirst(clazz.getSimpleName());
            }
            BeanDefinition beanDefinition=new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethodName);
            beanDefinition.setDestroyMethodName(destroyMethodName);
            beanDefinition.setLazyInit(Boolean.parseBoolean(lazyInit));
            if (StrUtil.isNotEmpty(beanScope)) {
                beanDefinition.setScope(beanScope);
            }
            List<Element>propertyList=bean.elements(PROPERTY_ELEMENT);
            for(Element property: propertyList){
                String propertyNameAttribute=property.attributeValue(NAME_ATTRIBUTE);
                String propertyValueAttribute=property.attributeValue(VALUE_ATTRIBUTE);
                String propertyRefAttribute=property.attributeValue(REF_ATTRIBUTE);

                if(StrUtil.isEmpty(propertyNameAttribute)){
                    throw new BeansException("The name attribute cannot be null");

                }
                Object value=propertyValueAttribute;
                if (StrUtil.isNotEmpty(propertyRefAttribute)) {
                    value = new BeanReference(propertyRefAttribute);
                }
                beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(propertyNameAttribute,value));

            }
            if(getRegistry().containsBeanDefinition(beanName)){
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            getRegistry().registerBeanDefinition(beanName,beanDefinition);
        }
    }

    private void scanPackage(String scanPath) {
        String[] strings = StrUtil.splitToArray(scanPath, ',');
        ClassPathBeanDefinitionScanner scanner=new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(strings);
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }
}
