package springbook.learningtest.spring31.web;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
import springbook.learningtest.spring.web.ConfigurableDispatcherServlet;

public class AbstractDispatcherServlet31Test extends AbstractDispatcherServletTest {

    @Override
    protected ConfigurableDispatcherServlet createDispatcherServlet() {
        return new ConfigurableDispatcherServlet() {
            @Override
            protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
                AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
                context.register(this.classes);
                context.setServletContext(getServletContext());
                context.setServletConfig(getServletConfig());
                context.refresh();

                return context;
            }
        };
    }

    @Override
    public AnnotationConfigWebApplicationContext getContext() {
        return (AnnotationConfigWebApplicationContext) super.getContext();
    }
}
