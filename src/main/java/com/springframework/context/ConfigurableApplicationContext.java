package com.springframework.context;

import com.springframework.beans.BeansException;
import com.springframework.core.convert.ConversionService;

public interface ConfigurableApplicationContext extends ApplicationContext {
   void refresh() throws BeansException;
//
//    /**
//     * 关闭应用上下文
//     */
    void close();

//    /**
//     * 向虚拟机中注册一个钩子方法，在虚拟机关闭之前执行关闭容器等操作
     void registerShutdownHook();

}
