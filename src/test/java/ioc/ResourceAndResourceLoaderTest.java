package ioc;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.UrlResource;
import com.springframework.core.io.DefaultResourceLoader;
import com.springframework.core.io.Resource;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceAndResourceLoaderTest {
    @Test
    public void test() throws Exception {
        DefaultResourceLoader resourceLoader=new DefaultResourceLoader();
        Resource resource=resourceLoader.getResource("classpath:hello.txt");
        InputStream inputStream = resource.getInputStream();
        String content= IoUtil.readUtf8(inputStream);
        System.out.println(content);

        resource=resourceLoader.getResource("src/test/resources/hello.txt");
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        resource = resourceLoader.getResource("https://github.com/DerekYRC/mini-spring/blob/main/README.md");

        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }
    
    

}
