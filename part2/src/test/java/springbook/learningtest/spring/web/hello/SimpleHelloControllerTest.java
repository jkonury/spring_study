package springbook.learningtest.spring.web.hello;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

public class SimpleHelloControllerTest extends AbstractDispatcherServletTest {
    @Test
    public void helloController() throws Exception {
        ModelAndView mav = setRelativeLocations("spring-servlet.xml")
            .setClasses(HelloSpring.class)
            .initRequest("/hello", RequestMethod.GET).addParameter("name", "Spring")
            .runService()
            .getModelAndView();

        assertThat(mav.getViewName(), is("/WEB-INF/view/hello.jsp"));
        assertThat(mav.getModel().get("message"), is("Hello Spring"));
    }

    @Test
    public void helloControllerWithAssertMethos() throws Exception {
        this.setRelativeLocations("spring-servlet.xml")
            .setClasses(HelloSpring.class)
            .initRequest("/hello", RequestMethod.GET).addParameter("name", "Spring")
            .runService()
            .assertModel("message", "Hello Spring")
            .assertViewName("/WEB-INF/view/hello.jsp");
    }

    @Test
    public void helloControllerWithServletPath() throws Exception {
        this.setRelativeLocations("spring-servlet.xml")
            .setClasses(HelloSpring.class)
            .setServletPath("/app")
            .initRequest("/app/hello", RequestMethod.GET).addParameter("name", "Spring")
            .runService()
            .assertModel("message", "Hello Spring")
            .assertViewName("/WEB-INF/view/hello.jsp");
    }
}
