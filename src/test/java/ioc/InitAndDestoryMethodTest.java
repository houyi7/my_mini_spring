package ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

public class InitAndDestoryMethodTest {
    @Test
    public void test(){
        ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:initDistoryMethod.xml");
        applicationContext.registerShutdownHook();
    }


}
