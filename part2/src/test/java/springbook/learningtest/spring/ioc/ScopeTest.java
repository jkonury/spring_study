package springbook.learningtest.spring.ioc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ScopeTest {
    @Test
    public void singletonScope() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(SingletonBean.class, SingletonClientBean.class);

        Set<SingletonBean> bean = new HashSet<>();

        // DL에서 싱글톤 확인
        bean.add(context.getBean(SingletonBean.class));
        bean.add(context.getBean(SingletonBean.class));
        assertThat(bean.size(), is(1));

        // DI에서 싱글톤 확인
        bean.add(context.getBean(SingletonClientBean.class).bean1);
        bean.add(context.getBean(SingletonClientBean.class).bean2);
        assertThat(bean.size(), is(1));
    }

    static class SingletonBean {}

    static class SingletonClientBean {
        @Autowired SingletonBean bean1;
        @Autowired SingletonBean bean2;
    }

    @Test
    public void prototypeeScope() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(PrototypeBean.class, PrototypeClientBean.class);

        Set<PrototypeBean> bean = new HashSet<>();

        bean.add(context.getBean(PrototypeBean.class));
        assertThat(bean.size(), is(1));
        bean.add(context.getBean(PrototypeBean.class));
        assertThat(bean.size(), is(2));

        bean.add(context.getBean(PrototypeClientBean.class).bean1);
        assertThat(bean.size(), is(3));
        bean.add(context.getBean(PrototypeClientBean.class).bean2);
        assertThat(bean.size(), is(4));
    }

    @Component("prototypeBean")
    @Scope("prototype")
    static class PrototypeBean {}

    static class PrototypeClientBean {
        @Autowired PrototypeBean bean1;
        @Autowired PrototypeBean bean2;
    }

    @Test
    public void objectFactory() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(PrototypeBean.class, ObjectFactoryConfig.class);
        ObjectFactory<PrototypeBean> factoryBean = context.getBean("prototypeBeanFactory", ObjectFactory.class);

        Set<PrototypeBean> bean = new HashSet<>();
        for (int i = 1; i <= 4; i++) {
            bean.add(factoryBean.getObject());
            assertThat(bean.size(), is(i));
        }
    }

    @Configuration
    static class ObjectFactoryConfig {
        @Bean
        public ObjectFactoryCreatingFactoryBean prototypeBeanFactory() {
            ObjectFactoryCreatingFactoryBean factoryBean = new ObjectFactoryCreatingFactoryBean();
            factoryBean.setTargetBeanName("prototypeBean");
            return factoryBean;
        }
    }

    @Test
    public void serviceLocatorFactoryBean() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(PrototypeBean.class, ServiceLocatorConfig.class);
        PrototypeBeanFactory factory = context.getBean(PrototypeBeanFactory.class);

        Set<PrototypeBean> bean = new HashSet<>();

        for (int i = 1; i <= 4; i++) {
            bean.add(factory.getPrototypeBean());
            assertThat(bean.size(), is(i));
        }
    }

    interface PrototypeBeanFactory {
        PrototypeBean getPrototypeBean();
    }

    @Configuration
    static class ServiceLocatorConfig {
        @Bean
        public ServiceLocatorFactoryBean prototypeBeanFactory() {
            ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
            factoryBean.setServiceLocatorInterface(PrototypeBeanFactory.class);
            return factoryBean;
        }
    }

    @Test
    public void provideryTest() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(PrototypeBean.class, ProviderClient.class);
        ProviderClient client = context.getBean(ProviderClient.class);
        
        Set<PrototypeBean> bean = new HashSet<>();
        for (int i = 1; i <= 4; i++) {
            bean.add(client.prototypeBeanProvider.get());
            assertThat(bean.size(), is(i));
        }
    }

    static class ProviderClient {
        @Inject
        Provider<PrototypeBean> prototypeBeanProvider;
    }

    static class AnnotationConfigDispatcherServlet extends DispatcherServlet {
        private Class<?>[] classes;

        public AnnotationConfigDispatcherServlet(Class<?>... classes) {
            this.classes = classes;
        }

        @Override
        protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
            AbstractRefreshableWebApplicationContext wac = new AbstractRefreshableWebApplicationContext() {
                @Override
                protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
                    AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
                    reader.register(classes);
                }
            };

            wac.setServletContext(getServletContext());
            wac.setServletConfig(getServletConfig());
            wac.refresh();

            return wac;
        }
    }

    MockHttpServletResponse response = new MockHttpServletResponse();
    @Test
    public void requestScope() throws Exception {
        MockServletConfig config = new MockServletConfig(new MockServletContext(), "spring");
        DispatcherServlet servlet = new AnnotationConfigDispatcherServlet(HelloController.class, HelloService.class, RequestBean.class, BeanCounter.class);
        servlet.init(new MockServletConfig());

        BeanCounter counter = servlet.getWebApplicationContext().getBean(BeanCounter.class);

        servlet.service(new MockHttpServletRequest("GET", "/hello"), this.response);
        assertThat(counter.addCounter, is(2));
        assertThat(counter.size(), is(1));


        servlet.service(new MockHttpServletRequest("GET", "/hello"), this.response);
        assertThat(counter.addCounter, is(4));
        assertThat(counter.size(), is(2));

        for (String name : ((AbstractRefreshableWebApplicationContext)servlet.getWebApplicationContext()).getBeanFactory().getRegisteredScopeNames()) {
            System.out.println(name);
        }

    }

    @RequestMapping("/")
    static class HelloController {
        @Autowired
        HelloService helloService;
        @Autowired
        Provider<RequestBean> requestBeanProvider;
        @Autowired
        BeanCounter beanCounter;
        @RequestMapping("hello")
        public String hello() {
            beanCounter.addCounter++;
            beanCounter.add(requestBeanProvider.get());
            helloService.hello();
            return "";
        }
    }

    static class HelloService {
        @Autowired
        Provider<RequestBean> requestBeanProvider;
        @Autowired
        BeanCounter beanCounter;

        public void hello() {
            beanCounter.addCounter++;
            beanCounter.add(requestBeanProvider.get());
        }
    }

    @Scope("request")
    static class RequestBean {}
    static class BeanCounter extends HashSet {
        int addCounter = 0;
    }
}
