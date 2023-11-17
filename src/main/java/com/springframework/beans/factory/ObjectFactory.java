package com.springframework.beans.factory;

import com.springframework.beans.BeansException;

public interface ObjectFactory <T>{
    T getObject()throws BeansException;
}
