package com.springframework.aop;

import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;

public interface PointcutAdvisor extends Advisor{
    Pointcut getPointcut();
}
