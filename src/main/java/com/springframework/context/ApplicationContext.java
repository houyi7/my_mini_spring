package com.springframework.context;

import com.springframework.beans.factory.HierarchicalBeanFactory;
import com.springframework.beans.factory.ListableBeanFactory;
import com.springframework.core.io.ResourceLoader;

public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
