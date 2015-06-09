/*
p389
*/
package springbook.learningtest.spring.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public abstract class AbstractDispatcherServletTest implements AfterRunService{
    // 서블릿 목 오브젝트는 서브클래스에서 직접 접근 가능하다.
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected MockServletConfig config = new MockServletConfig("spring");
    protected MockHttpSession session;

    private ConfigurableDispatcherServlet dispatcherServlet;

    // 서블릿 컨텍스트를 생성을 위한 설정 메타정보는 클래스, 클래스패스, 상대 클래스패스
    // 세가지로 지정할 수 있다.
    private Class<?>[] classes;
    private String[] locations;
    private String[] relateiveLocations;
    private String servletPath;

    public AbstractDispatcherServletTest setLocations(String ... locations) {
        this.locations = locations;
        return this;
    }

    public AbstractDispatcherServletTest setRelativeLocations(String... relateiveLocations) {
        this.relateiveLocations = relateiveLocations;
        return this;
    }

    public AbstractDispatcherServletTest setClasses(Class<?> ... classes) {
        this.classes = classes;
        return this;
    }

    public AbstractDispatcherServletTest setServletPath(String servletPath) {
        if (this.request == null) {
            this.servletPath = servletPath;
        } else {
            this.request.setServletPath(servletPath);
        }
        return this;
    }

    public AbstractDispatcherServletTest initRequest(String requestUri, String method) {
        this.request = new MockHttpServletRequest(method, requestUri);
        this.response = new MockHttpServletResponse();
        if (this.servletPath != null) {
            this.setServletPath(this.servletPath);
        }
        return this;
    }

    public AbstractDispatcherServletTest initRequest(String requestUri, RequestMethod method) {
        return this.initRequest(requestUri, method.toString());
    }

    public AbstractDispatcherServletTest initRequest(String requestUri) {
        return this.initRequest(requestUri, RequestMethod.GET);
    }

    public AbstractDispatcherServletTest addParameter(String name, String value) {
        if (this.request == null) {
            throw new IllegalStateException("request가 초기화되지 않았습니다.");
        }
        this.request.addParameter(name, value);
        return this;
    }

    public AbstractDispatcherServletTest buildDispatcherServlet() throws ServletException {
        if (this.classes == null && this.locations == null && this.relateiveLocations == null) {
            throw new IllegalStateException("classes와 locations 중 하나는 설정해야 합니다.");
        }
        this.dispatcherServlet = new ConfigurableDispatcherServlet();
        this.dispatcherServlet.setClasses(this.classes);
        this.dispatcherServlet.setLocations(this.locations);
        if (this.relateiveLocations != null) {
            this.dispatcherServlet.setRelativeLocations(getClass(), this.relateiveLocations);
        }
        this.dispatcherServlet.init(this.config);

        return this;
    }

    protected ConfigurableDispatcherServlet createDispatcherServlet() {
        return new ConfigurableDispatcherServlet();
    }

    public AfterRunService runService() throws ServletException, IOException {
        if (this.dispatcherServlet == null) {
            buildDispatcherServlet();
        }
        if (this.request == null) {
            throw new IllegalStateException("request가 준비되지 않았습니다.");
        }
        this.dispatcherServlet.service(this.request, this.response);
        return this;
    }

    public AfterRunService runService(String requestUri) throws ServletException, IOException {
        initRequest(requestUri);
        runService();
        return this;
    }

    public AfterRunService runService(String requestUri, String method) throws ServletException, IOException {
        initRequest(requestUri, method);
        runService();
        return this;
    }

    @Override
    public ConfigurableWebApplicationContext getContext() {
        if (this.dispatcherServlet == null) {
            throw new IllegalStateException("DispatcherServlet이 준비되지 않았습니다");
        }
        return (ConfigurableWebApplicationContext) this.dispatcherServlet.getWebApplicationContext();
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        if (this.dispatcherServlet == null) {
            throw new IllegalStateException("DispatcherServlet이 준비되지 않았습니다");
        }
        return this.getContext().getBean(beanType);
    }

    @Override
    public ModelAndView getModelAndView() {
        return this.dispatcherServlet.getModelAndView();
    }

    @Override
    public AfterRunService assertViewName(String viewname) {
        assertThat(this.getModelAndView().getViewName(), is(viewname));
        return this;
    }

    @Override
    public AfterRunService assertModel(String name, Object value) {
        assertThat(this.getModelAndView().getModel().get(name), is(value));
        return this;
    }

    @Override
    public String getContentAsString() throws UnsupportedEncodingException {
        return this.response.getContentAsString();
    }

    @After
    public void closeServletContext() {
        if (this.dispatcherServlet != null) {
            ((ConfigurableApplicationContext)dispatcherServlet.getWebApplicationContext()).close();
        }
    }
}
