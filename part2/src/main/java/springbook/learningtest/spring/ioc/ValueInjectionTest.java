package springbook.learningtest.spring.ioc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ValueInjectionTest {
    @Test
    @Ignore
    public void valueInjection() throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanSP.class, ConfigSP.class, DatabasePropertyPlaceHolder.class);
        BeanSP bean = context.getBean(BeanSP.class);

        assertThat(bean.name, is("Linux"));
        assertThat(bean.username, is("Spring"));

        assertThat(bean.hello.name, is("Spring"));
    }

    static class BeanSP {
        @Value("#{systemProperties['os.name']}")
        String name;
        @Value("${database.username}")
        String username;
        @Value("${os.name}")
        String osname;
        @Autowired
        Hello hello;
    }

    static class ConfigSP {
        @Bean
        public Hello hello(@Value("${database.username}") String username) {
            Hello hello = new Hello();
            hello.name = username;
            return hello;
        }
    }

    static class Hello {
        String name;
    }

    static class DatabasePropertyPlaceHolder extends PreferencesPlaceholderConfigurer {
        public DatabasePropertyPlaceHolder() {
            this.setLocation(new ClassPathResource("database.properties", getClass()));
        }
    }

    @Test
    @Ignore
    public void importResource() throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigIR.class);
        BeanSP bean = context.getBean(BeanSP.class);

        assertThat(bean.name, is("Linux"));
        assertThat(bean.username, is("Spring"));
    }

    @ImportResource("/springbook/learningtest/spring/ioc/properties2.xml")
    @Configuration
    static class ConfigIR {
        @Bean
        public BeanSP beanSp() {
            return new BeanSP();
        }
        @Bean
        public Hello hello() {
            return new Hello();
        }
    }

    @Test
    public void propertyEditor() throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BeanPE.class);
        BeanPE bean = context.getBean(BeanPE.class);

        assertThat(bean.charset, is(Charset.forName("UTF-8")));
        assertThat(bean.intarr, is(new int[]{1, 2, 3}));
        assertThat(bean.flag, is(true));
        assertThat(bean.rate, is(1.2));
        assertThat(bean.file.exists(), is(true));
    }

    static class BeanPE {
        @Value("UTF-8")
        Charset charset;
        @Value("1, 2, 3")
        int[] intarr;
        @Value("true")
        boolean flag;
        @Value("1.2")
        double rate;
        @Value("classpath:test-applicationContext.xml")
        File file;
    }

    @Test
    public void collectionInject() throws Exception {
        ApplicationContext context = new GenericXmlApplicationContext(new ClassPathResource("collection.xml", getClass()));
        BeanC bean = context.getBean(BeanC.class);

        assertThat(bean.nameList.size(), is(3));
        assertThat(bean.nameList.get(0), is("Spring"));
        assertThat(bean.nameList.get(1), is("IoC"));
        assertThat(bean.nameList.get(2), is("DI"));

        assertThat(bean.nameSet.size(), is(3));

        assertThat(bean.ages.get("Kim"), is(30));
        assertThat(bean.ages.get("Lee"), is(35));
        assertThat(bean.ages.get("Ahn"), is(40));

        assertThat(bean.settings.get("username"), is("Spring"));
        assertThat(bean.settings.get("password"), is("Book"));

        assertThat(bean.beans.size(), is(2));
    }

    static class BeanC {
        List<String> nameList;
        Set<String> nameSet;
        Map<String, Integer> ages;
        Properties settings;
        List beans;

        public void setNameList(List<String> nameList) {
            this.nameList = nameList;
        }

        public void setNameSet(Set<String> nameSet) {
            this.nameSet = nameSet;
        }

        public void setAges(Map<String, Integer> ages) {
            this.ages = ages;
        }

        public void setSettings(Properties settings) {
            this.settings = settings;
        }

        public void setBeans(List beans) {
            this.beans = beans;
        }
    }
}
