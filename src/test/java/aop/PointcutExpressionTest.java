package aop;

import com.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.junit.Test;
import testclass.HelloService;

import java.lang.reflect.Method;

public class PointcutExpressionTest {
    @Test
    public void test() throws NoSuchMethodException {
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut("execution(* testclass.HelloService.*(..))");
        Class<HelloService> helloServiceClass = HelloService.class;
        Method sayHello = helloServiceClass.getDeclaredMethod("sayHello");
        System.out.println(aspectJExpressionPointcut.matches(helloServiceClass));
        System.out.println(aspectJExpressionPointcut.matches(sayHello,helloServiceClass));

    }
}
