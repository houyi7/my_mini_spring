package com.springframework.core.io;


import java.net.MalformedURLException;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader{
    public static final  String CLASSPATH_URL_PREFIX="classpath:";

    @Override
    public Resource getResource(String location) {
        if(location.startsWith(CLASSPATH_URL_PREFIX)){
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }else {
            try{
                URL url=new URL(location);
                return new URLResource(url);
            } catch (MalformedURLException e) {
                return  new FileSystemResource(location);
            }
        }
    }
}
