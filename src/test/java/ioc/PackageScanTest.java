package ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;
import testclass.Car;
import testclass.Person;
import testclass.Phone;

public class PackageScanTest {

	@Test
	public void testScanPackage() throws Exception {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:package-scan.xml");

		Phone car = applicationContext.getBean("phone", Phone.class);
		System.out.println(car);
	}
}