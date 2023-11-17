package ioc;

import com.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;
import testclass.Lisenter.CustomEvent;

public class EventAndEventListenerTest {
    @Test
    public void test(){
        ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:eventandlistener.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext));
        applicationContext.registerShutdownHook();
    }

}
