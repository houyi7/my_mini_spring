package com.springframework.aop;

public interface ClassFilter {
    boolean matches(Class<?>clazz);
}
