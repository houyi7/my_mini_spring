package ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;
import testclass.Car;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PrototypeBeanTest {

	@Test
	public void testPrototype() throws Exception {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:prototype-bean.xml");

		Car car1 = applicationContext.getBean("car", Car.class);
		Car car2 = applicationContext.getBean("car", Car.class);
		assertThat(car1 != car2).isTrue();
	}
}