package com.springframework.beans.factory;

import com.springframework.context.ApplicationContext;

public interface ApplicationContextAware extends  Aware{
    void setApplicationContext(ApplicationContext applicationContext);
}
