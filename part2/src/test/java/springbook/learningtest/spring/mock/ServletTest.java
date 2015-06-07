package springbook.learningtest.spring.mock;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class ServletTest {
    @Test
    public void getMethodServlet() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/hello");
        req.addParameter("name", "Spring");
        MockHttpServletResponse res = new MockHttpServletResponse();

        SimpleGetServlet servlet = new SimpleGetServlet();
        servlet.service(req, res);
        servlet.init();

        assertThat(res.getContentAsString(), is("<html><body>hello Spring</body></html>"));
        assertThat(res.getContentAsString().contains("hello Spring"), is(true));
    }
}
