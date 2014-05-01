package springbook.learningtest.junit;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


/**
 * @author JiHong Jang
 * @since 2014.05.01
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:junit.xml")
public class JUnitTest {
    @Autowired
    ApplicationContext context;

    static Set<JUnitTest> testObjects = new HashSet<JUnitTest>();
    static ApplicationContext contextObject = null;

    @Test
    public void test1() throws Exception {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == this.context, is(true));

    }

    @Test
    public void test2() throws Exception {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == this.context, is(true));
        contextObject = this.context;
    }

    @Test
    public void test3() throws Exception {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

//        assertThat(contextObject, either(is(nullValue())).or(is(this.contextObject)));
        contextObject = this.context;
    }
}
