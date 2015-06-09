package springbook.learningtest.spring.ioc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Properties;

public class AutoRegisteredBeansTest {
    @Test
    public void autoRegisteredBean() throws Exception {
//        System.getProperties().put("os.name", "Hi");

        ApplicationContext context = new AnnotationConfigApplicationContext(SystemBean.class);
        SystemBean bean = context.getBean(SystemBean.class);
        assertThat(bean.applicationContext, equalTo(context));

        System.out.println(bean.osname);
        System.out.println(bean.path);

        System.out.println("### " + bean.systemProperties);
        System.out.println("$$$ " + bean.systemEnvironment);
    }

    static class SystemBean {
        @Resource ApplicationContext applicationContext;
        @Autowired
        BeanFactory beanFactory;
        @Autowired
        ResourceLoader resourceLoader;
        @Autowired
        ApplicationEventPublisher applicationEventPublisher;

        @Value("#{systemProperties['os.name']}")
        String osname;
        @Value("#{systemEnvironment['PATH']}")
        String path;

        @Resource
        Properties systemProperties;
        @Resource
        Map<String, String> systemEnvironment;
    }
}
