package ioc;

import com.springframework.beans.factory.config.BeanDefinition;
import com.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;
import testclass.HelloService;

public class BeanDefinitionAndBeanDefinitionRegistryTest {
  @Test
  public void testBeanFactory()throws Exception{
      DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
      BeanDefinition beanDefinition=new BeanDefinition(HelloService.class);
      beanFactory.registerBeanDefinition("helloService",beanDefinition);
      HelloService helloService=(HelloService) beanFactory.getBean("helloService");
      helloService.sayHello();
  }
}
