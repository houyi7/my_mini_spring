package ioc;

import com.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.sun.org.apache.bcel.internal.generic.DLOAD;
import jdk.nashorn.internal.ir.SplitReturn;
import org.junit.Test;

import testclass.Person;
import testclass.beanProcesser.CustomBeanFactoryPostProcessor;
import testclass.beanProcesser.CustomerBeanPostProcessor;

public class BeanFactoryProcessorAndBeanPostProcessorTest {

    @Test
    public void testBeanFactotyPostProcessor()throws Exception{
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader=new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        CustomBeanFactoryPostProcessor beanFactoryPostProcessor=new CustomBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person.getName());

    }


    @Test
    public void testBeanPostProcessor()throws Exception{
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader=new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
        CustomerBeanPostProcessor  customerBeanPostProcessor=new CustomerBeanPostProcessor();
        beanFactory.addBeanPostProcessor(customerBeanPostProcessor);

        Person person = (Person) beanFactory.getBean("person");
//        System.out.println(person.getCar().getBand());

    }
}
