package testclass;

import com.springframework.beans.BeansException;
import com.springframework.beans.factory.ApplicationContextAware;
import com.springframework.beans.factory.BeanFactory;
import com.springframework.beans.factory.BeanFactoryAware;
import com.springframework.context.ApplicationContext;

public class HelloService implements BeanFactoryAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private BeanFactory beanFactory;




    public String sayHello() {
        System.out.println("hello");
        return "hello";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
