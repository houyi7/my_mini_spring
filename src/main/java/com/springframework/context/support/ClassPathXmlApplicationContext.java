package com.springframework.context.support;

import com.springframework.beans.BeansException;

import java.util.Map;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{
    private String []configLocations;
    public ClassPathXmlApplicationContext(String configLocation)throws BeansException{
        this(new String[]{configLocation});
    }
    public ClassPathXmlApplicationContext(String []configLocations ){
        this.configLocations=configLocations;
        refresh();
    }
    protected String[] getConfigLocations() {
        return this.configLocations;
    }



    //--------------------------------------------------------------------


}
