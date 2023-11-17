package ioc;

import com.springframework.beans.PropertyValue;
import com.springframework.beans.PropertyValues;
import com.springframework.beans.factory.config.BeanDefinition;
import com.springframework.beans.factory.config.BeanReference;
import com.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;
import testclass.Car;
import testclass.Person;

import static org.assertj.core.api.Assertions.assertThat;

public class PopulateBeanWithPropertyValuesTest {

    @Test
    public void test()throws Exception{
        DefaultListableBeanFactory defaultListableBeanFactory=new DefaultListableBeanFactory();

        PropertyValues pvForCar=new PropertyValues();
        pvForCar.addPropertyValue(new PropertyValue("band","maibahe"));;
        BeanDefinition beanDefinitionForCar=new BeanDefinition( Car.class,pvForCar);
        defaultListableBeanFactory.registerBeanDefinition("car",beanDefinitionForCar);


        PropertyValues propertyValues=new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name","ll"));
        propertyValues.addPropertyValue(new PropertyValue("age","18"));
        propertyValues.addPropertyValue(new PropertyValue("car",new BeanReference("car")));


        BeanDefinition beanDefinition=new BeanDefinition(Person.class,propertyValues);
        defaultListableBeanFactory.registerBeanDefinition("person",beanDefinition);
        Person person= (Person) defaultListableBeanFactory.getBean("person");
        System.out.println(person.getCar());
        assert(person.getAge()==18);
        assertThat(person.getName()).isEqualTo("ll");
    }
}
