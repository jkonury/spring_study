package springbook.learningtest.spring31.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;
import springbook.learningtest.spring.web.AbstractDispatcherServletTest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class FlashMapTest extends AbstractDispatcherServletTest {
    @Test
//    @Ignore
    public void flashMapTest() throws Exception {
        setClasses(PostController.class, RedirectController.class, OtherController.class);
        runService("/flash");

        assertThat(this.getModelAndView().getViewName(), is("redirect:/redirect"));

        HttpSession sessionSaved = request.getSession();

        // 1st request
        initRequest("/redirect");
        request.setSession(sessionSaved);
        runService();

        sessionSaved = request.getSession();

        // 2nd request
        initRequest("/redirect");
        request.setSession(sessionSaved);

        try {
            runService();
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assertThat(e.getCause(), instanceOf(NullPointerException.class));
        }
    }

    @Test
    public void flashMapWithRequestPath() throws ServletException, IOException {
        setClasses(PostController.class, RedirectController.class, OtherController.class);
        runService("/flash");

        HttpSession sessionSaved = request.getSession();

        // /other
        initRequest("/other");
        request.setSession(sessionSaved);
        try {
            runService();
            fail();
        } catch(Exception e) {
            System.out.println("========================================================================================================================================================");
            System.out.println(e.getMessage());
            assertThat(e.getCause(), instanceOf(NullPointerException.class));
        }

        sessionSaved = request.getSession();

        // /redirect
        initRequest("/redirect");
        request.setSession(sessionSaved);
        runService();
    }

    @Controller
    static class PostController {
        @RequestMapping("/flash")
        public String flash(HttpServletRequest request) {
            FlashMap fm = RequestContextUtils.getOutputFlashMap(request);
            fm.put("message", "Saved");
            fm.setTargetRequestPath("/redirect");

            return "redirect:/redirect";
        }
    }

    @Controller
    static class RedirectController {
        @RequestMapping
        public String next(HttpServletRequest request) {
            assertThat(RequestContextUtils.getInputFlashMap(request).get("message"), is("Saved"));

            return "next";
        }
    }

    @Controller
    static class OtherController {
        @RequestMapping("/other")
        public String next(HttpServletRequest request) {
            assertThat(RequestContextUtils.getInputFlashMap(request).get("message"), is("Saved"));

            return "next";
        }
    }
}
