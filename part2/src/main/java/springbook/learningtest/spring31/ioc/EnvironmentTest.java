package springbook.learningtest.spring31.ioc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentTest {
    @Configuration
    static class SimpleEnvConfig {}

    @Configuration
    @Profile({"p1", "p2"})
    static class ProfileConfig {}

    @Component
    @Profile("p1")
    static class ProfileBean {}

    @Configuration
    @Import({ProfileBean.class, ProfileConfig.class})
    static class ActiveProfileConfig {}

    @Test
    public void defaultProfile() throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimpleEnvConfig.class);

        assertThat(context.getEnvironment().getDefaultProfiles().length, is(1));
        assertThat(context.getEnvironment().getDefaultProfiles()[0], is("default"));
    }

    static List<String> applicationBeans(GenericApplicationContext context) {
        List<String> beans = new ArrayList<>();
        for (String name : context.getBeanDefinitionNames()) {
            if (context.getBeanDefinition(name).getRole() == BeanDefinition.ROLE_APPLICATION &&
                !name.contains("ImportAwareBeanPostProcessor")) {
                beans.add(name);
            }
        }
        return beans;
    }

    @Test
    public void activeProfiles() throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ActiveProfileConfig.class);
        context.refresh();

        assertThat(applicationBeans(context).size(), is(1));
        assertThat(applicationBeans(context).contains("environmentTest.ActiveProfileConfig"), is(true));

        context = new AnnotationConfigApplicationContext();
        context.register(ActiveProfileConfig.class);
        context.getEnvironment().setActiveProfiles("p1");
        context.refresh();
        assertThat(applicationBeans(context).size(), is(3));

        context = new AnnotationConfigApplicationContext();
        context.register(ActiveProfileConfig.class);
        context.getEnvironment().setActiveProfiles("p2");
        context.refresh();
        assertThat(applicationBeans(context).size(), is(2));
        assertThat(applicationBeans(context).contains("environmentTest.ProfileConfig"), is(false));
        assertThat(applicationBeans(context).contains("environmentTest.ProfileBean"), is(false));

        List<String> strings = applicationBeans(context);
        System.out.println(strings);
    }

    @Configuration
    @PropertySource("classpath:springbook/learningtest/spring31/ioc/db.xml")
    static class SimplePropertySource {
        @Autowired
        Environment env;
        @Value("${name}")
        String name;

        @Bean
        static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Test
    public void propertySourcePlaceholderConfigurer() throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SimplePropertySource.class);
        SimplePropertySource source = context.getBean(SimplePropertySource.class);
        assertThat(source.name, is("토비"));

        Environment env = context.getBean(Environment.class);
        assertThat(env.getProperty("name"), is("토비"));
    }
    
    @Test
    public void embeddedDb() throws Exception {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext(getClass(), "edb.xml");
        context.getBean(DataSource.class);
    }

    @Test
    public void profiles() throws Exception {
        // dev profile
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.getEnvironment().setActiveProfiles("dev");
        context.load(getClass(), "profile.xml");
        context.refresh();
        assertThat(context.getBean(DataSource.class), instanceOf(BasicDataSource.class));
        context.close();
    }

    @Test
    public void activeProfile() throws Exception {
        // system property
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "dev");

        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.load(getClass(), "profile.xml");
        context.refresh();
        assertThat(context.getBean(DataSource.class), instanceOf(BasicDataSource.class));
        context.close();
    }

//    이 테스트는 테스트 실행 환경에 VM 파라미터로 -Dspring.profiles.active=dev를 지정하고 수행해야 한다.
//	@Test
//	public void activeProfileCommandLineParameter() {
//		GenericXmlApplicationContext ac2 = new GenericXmlApplicationContext();
//		ac2.load(getClass(), "profile.xml");
//		ac2.refresh();
//		assertThat(ac2.getBean(DataSource.class), is(BasicDataSource.class));
//		ac2.close();
//	}
}
