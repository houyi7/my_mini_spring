package com.springframework.beans.factory;

import cn.hutool.core.text.StrBuilder;
import com.springframework.beans.BeansException;
import com.springframework.beans.PropertyValue;
import com.springframework.beans.factory.config.BeanDefinition;
import com.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.springframework.core.io.DefaultResourceLoader;
import com.springframework.core.io.Resource;
import com.springframework.core.io.ResourceLoader;
import sun.management.snmp.jvmmib.EnumJvmMemPoolState;

import java.io.IOException;
import java.util.Properties;
 

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {
    public static final String PLACEHOLDER_PREFIX="${";
    
    public static final String PLACHOLDER_SUFFIX="}";
    
    private String location;

    public void setLocation(String location) {
        this.location = location;
    }
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        try {
            Properties properties=loadProperties();
            processProperties(configurableListableBeanFactory,properties);
            StringValueResolver valueResolver =new PlaceholderResolvingStringValueResolver(properties);
            configurableListableBeanFactory.addEmbeddedValueResolver(valueResolver);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void processProperties(ConfigurableListableBeanFactory configurableListableBeanFactory, Properties properties) {
        String[]beanDefinitionNames=configurableListableBeanFactory.getBeanDefinitionNames();
        for (String name:beanDefinitionNames){
            BeanDefinition beanDefinition=configurableListableBeanFactory.getBeanDefinition(name);
            resolvePropertyValues(beanDefinition,properties);
            
        }
    }

    private void resolvePropertyValues(BeanDefinition beanDefinition, Properties properties) {
        for (PropertyValue propertyValue:beanDefinition.getPropertyValues().getPropertyValues()){
            Object value=propertyValue.getValue();
            if(value instanceof String){
                value=resolvePlaceholder((String)value,properties);
                beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(propertyValue.getName(),value));
            }
        }

    }

    private String resolvePlaceholder(String value, Properties properties) {
        String strVal=value;
        StringBuffer buf = new StringBuffer(strVal);
        int li=strVal.indexOf(PLACEHOLDER_PREFIX);
        int end=strVal.indexOf(PLACHOLDER_SUFFIX);
        if(li<end&&li!=-1&end!=-1){
            String propKey=strVal.substring(li+2,end);
            String propVal=properties.getProperty(propKey);
            buf.replace(li,end+1,propVal);
        }
        return buf.toString();
    }

    private Properties loadProperties() throws IOException {
        DefaultResourceLoader resourceLoader=new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        Properties properties=new Properties() ;
        properties.load(resource.getInputStream());
        return properties;
        
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver{
        private final Properties properties;
        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String strVal) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder( strVal,properties);
        }
    }
}
