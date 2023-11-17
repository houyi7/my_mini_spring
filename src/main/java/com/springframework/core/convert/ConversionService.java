package com.springframework.core.convert;

public interface ConversionService {
    boolean canConvert(Class<?>source ,Class<?>targetType);
    <T> T convert(Object source,Class<T>targetType);
}
