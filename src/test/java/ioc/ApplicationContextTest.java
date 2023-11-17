package ioc;

import com.springframework.context.ApplicationContext;
import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;
import testclass.Car;
import testclass.Person;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationContextTest {

    @Test
    public void test(){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring.xml");
        Person person = applicationContext.getBean("person", Person.class);
        System.out.println(person);
        //name属性在CustomBeanFactoryPostProcessor中被修改为ivy
        assertThat(person.getName()).isEqualTo("ivy");

        Car car = applicationContext.getBean("car", Car.class);
        System.out.println(car);
        //brand属性在CustomerBeanPostProcessor中被修改为lamborghini
//        assertThat(car.getBand()).isEqualTo("lamborghini");
    }

}
