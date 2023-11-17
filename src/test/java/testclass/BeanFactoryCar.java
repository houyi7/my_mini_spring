package testclass;

import com.springframework.beans.factory.BeanFactoryAware;
import com.springframework.beans.factory.FactoryBean;

public class BeanFactoryCar implements FactoryBean<Car> {
    String band;

    public void setBand(String band) {
        this.band = band;
    }

    @Override
    public Car getObject() throws Exception {
        Car car=new Car();
//        car.setBand(band);
        return car;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
