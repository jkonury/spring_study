package springbook.learningtest.spring.ioc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import springbook.learningtest.spring.ioc.resource.Hello;

import javax.inject.Inject;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationConfigurationTest {
    private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";
    private String beanBasePath = basePath + "annotation/";

    @Test
    public void atResource() throws Exception {
        ApplicationContext context = new GenericXmlApplicationContext(basePath + "resource.xml");

        Hello hello = context.getBean("hello", Hello.class);

        hello.print();

        assertThat(context.getBean("myprinter").toString(), is("Hello Spring"));
    }

    @Test
    public void atAutowiredCollection() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(Client.class, ServiceA.class, ServiceB.class);
        Client client = context.getBean(Client.class);

        assertThat(client.beanBArray.length, is(2));
        assertThat(client.beanBSet.size(), is(2));
        assertThat(client.beanBMap.entrySet().size(), is(2));
        assertThat(client.beanBList.size(), is(2));
        assertThat(client.beanBCollection.size(), is(2));
    }

    static class Client {
        @Autowired Set<Service> beanBSet;
        @Autowired Service[] beanBArray;
        @Autowired Map<String, Service> beanBMap;
        @Autowired List<Service> beanBList;
        @Autowired Collection<Service> beanBCollection;
    }

    interface Service {}
    static class ServiceA implements Service{}
    static class ServiceB implements Service{}

    @Test
    public void atQualifier() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(QClient.class, QServiceA.class, QServiceB.class);
        QClient client = context.getBean(QClient.class);
        assertThat(client.service, is(instanceOf(QServiceA.class)));
    }

    static class QClient {
        @Autowired @Qualifier("main")
        Service service;
    }

    @Qualifier("main")
    static class QServiceA implements Service {}
    static class QServiceB implements Service {}

    @Test
    public void atInject() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(IClient.class, IServiceA.class, IServiceB.class);
        IClient client = context.getBean(IClient.class);
        assertThat(client.service, is(instanceOf(IServiceA.class)));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    @interface Main {}

    static class IClient {
        @Inject @Main
        Service service;
    }

    @Main
    static class IServiceA implements Service {}
    static class IServiceB implements Service {}
}
