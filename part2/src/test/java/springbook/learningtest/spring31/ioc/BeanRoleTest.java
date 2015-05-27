package springbook.learningtest.spring31.ioc;

import org.junit.Test;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class BeanRoleTest {
    @Test
    public void simpleConfig() throws Exception {
        GenericApplicationContext context = new GenericXmlApplicationContext(BeanRoleTest.class, "beanrole.xml");
        BeanDefinitionUtils.printBeanDefinitions(context);

        SimpleConfig config = context.getBean(SimpleConfig.class);
        assertThat(config.hello, is(notNullValue()));
        config.hello.sayHello();
    }
}