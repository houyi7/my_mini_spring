package ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;
import testclass.Car;

public class FactoryBeanTest {
    @Test
    public void test(){

        ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:prototype.xml");
        Car car = (Car) applicationContext.getBean("car");

//        System.out.println(car.getBand());

    }
}
