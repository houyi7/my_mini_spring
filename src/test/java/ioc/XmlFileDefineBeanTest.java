package ioc;

import com.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.junit.Test;
import testclass.Person;

public class XmlFileDefineBeanTest {

    @Test
    public void test(){
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader=new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person.getCar());
    }
}
