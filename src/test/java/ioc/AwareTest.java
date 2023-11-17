package ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;
import testclass.HelloService;
import testclass.Person;

import static org.assertj.core.api.Assertions.assertThat;

public class AwareTest {
    @Test
    public void test(){
        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring.xml");


        HelloService helloService = (HelloService) context.getBean("helloService");
        assertThat(helloService.getApplicationContext()).isNotNull();
        assertThat(helloService.getBeanFactory()).isNotNull();

    }

}
