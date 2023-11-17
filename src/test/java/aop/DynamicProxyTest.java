package aop;

import com.springframework.adapter.AfterReturningAdvice;
import com.springframework.adapter.AfterReturningAdviceInterceptor;
import com.springframework.aop.AdvisedSupport;
import com.springframework.aop.TargetSource;
import com.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.springframework.aop.framework.CglibAopProxy;
import com.springframework.aop.framework.JdkDynamicAopProxy;
import com.springframework.aop.framework.ProxyFactory;
import org.junit.Test;
import testclass.WorldService;
import testclass.WorldServiceImpl;

import java.lang.reflect.Method;

public class DynamicProxyTest {

    private AdvisedSupport advisedSupport;

    @Test
    public void test(){
        WorldService worldService=new WorldServiceImpl();
        advisedSupport=new ProxyFactory();
        String expression="execution(* testclass.WorldService.explode(..))";
        AspectJExpressionPointcutAdvisor advisor=new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(expression);
        AfterReturningAdviceInterceptor methodInterceptor =new AfterReturningAdviceInterceptor(new AfterReturningAdvice(){
            @Override
            public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
                System.out.println("AfterAdvice: do something after the earth explodes");
            }
        });

        advisor.setAdvice(methodInterceptor);
        TargetSource targetSource=new TargetSource(worldService);
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.addAdvisor(advisor);
//        WorldService proxy = (WorldService) new JdkDynamicAopProxy(advisedSupport).getProxy();
//        proxy.explode();
        WorldService proxy = (WorldService) new CglibAopProxy(advisedSupport).getProxy();
        proxy.explode();

    }

}
