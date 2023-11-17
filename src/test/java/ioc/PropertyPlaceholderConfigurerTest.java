package ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;
import testclass.Car;



public class PropertyPlaceholderConfigurerTest {

	@Test
	public void test() throws Exception {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:propertiesConfig.xml");

		Car car = applicationContext.getBean("car", Car.class);
//		System.out.println(car.getBand());
	}
}
