package springbook.learningtest.spring.ioc;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import springbook.learningtest.spring.ioc.bean.AnnotatedHello;
import springbook.learningtest.spring.ioc.bean.AnnotatedHelloConfig;
import springbook.learningtest.spring.ioc.bean.Hello;
import springbook.learningtest.spring.ioc.bean.Printer;
import springbook.learningtest.spring.ioc.bean.StringPrinter;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ApplicationContextTest {
    private String basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + "/";

    @Test
    public void registerBean() throws Exception {
        // IoC 컨테이너 생성, 생성과 동시에 컨테이너로 동작한다.
        StaticApplicationContext ac = new StaticApplicationContext();
        // Hello 클래스를 hello1 이라는 이름의 싱글톤 빈으로 컨테이너에 등록한다.
        ac.registerSingleton("hello1", Hello.class);

        // IoC 컨테이너가 등록한 빈을 생성했는지 확인하기 위해 빈을 요청하고 Null이 아닌지 확인한다.
        Hello hello1 = ac.getBean("hello1", Hello.class);
        assertThat(hello1, is(notNullValue()));

        // 빈 메타정보를 담은 오브젝트를 만든다. 빈 클래스는 Hello로 지정한다.
        // <bean class="springbook.learningtest.spring.ioc.bean.Hello"/>에 해당하는 메타정보
        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        // 빈의 name 프로퍼티에 들어갈 값을 지정한다.
        // <property name="name" value="Spring"/>
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        // 앞에서 생성한 빈 메타정보를 hello2라는 이름을 가진 빈으로 해서 등록한다.
        // <bean id="hello2" class=""/>
        ac.registerBeanDefinition("hello2", helloDef);

        // BeanDefinition으로 등록된 빈이 컨테이너에 의해 만들어지고 프로퍼티 설정이 됐는지 확인
        Hello hello2 = ac.getBean("hello2", Hello.class);
        assertThat(hello2.sayHello(), is("Hello Spring"));

        // 처음 등록한 빈과 두 번째 등록한 빈이 모두 동일한 Hello 클래스지만 별개의 오브젝트로 생성됐다.
        assertThat(hello1, is(not(hello2)));

        assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
    }

    // DI 정보 테스트
    @Test
    public void registerBeanWithDependency() throws Exception {
        StaticApplicationContext ac = new StaticApplicationContext();

        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));

        ac.registerBeanDefinition("hello", helloDef);

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void genericApplicationContext() throws Exception {
        GenericApplicationContext ac = new GenericApplicationContext();

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
        // XmlBeanDefinitionReader는 기본적으로 클래스패스로 정의된 리소스로부터 파일을 읽는다.
        reader.loadBeanDefinitions("springbook/learningtest/spring/ioc/genericApplicationContext.xml");

        ac.refresh(); // 모든 메타정보가 등록이 완료됐으니 애플리케이션 컨테이너를 초기화하라는 명령어

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void genericXmlApplicationContext() throws Exception {
        // 애플리케이션 컨텍스트 생성과 동시에 XML 파일을 읽어오고 초기화까지 수행
        GenericApplicationContext ac = new GenericXmlApplicationContext(basePath + "genericApplicationContext.xml");

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test(expected = BeanCreationException.class)
    public void createContextWithoutParent() throws Exception {
        ApplicationContext child = new GenericXmlApplicationContext(basePath + "childContext.xml");
    }

    @Test
    public void contextHierachy() throws Exception {
        ApplicationContext parent = new GenericXmlApplicationContext(basePath + "parentContext.xml");

        GenericApplicationContext child = new GenericApplicationContext(parent);
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(child);
        reader.loadBeanDefinitions(basePath + "childContext.xml");
        child.refresh(); // reader 를 사용해서 설정을 읽은 경우에는 반드시 refresh()를 통해 초기화해야 한다.

        Printer printer = child.getBean("printer", Printer.class);
        assertThat(printer, is(notNullValue()));

        Hello hello = child.getBean("hello", Hello.class);
        assertThat(hello, is(notNullValue()));

        hello.print();
        assertThat(printer.toString(), is("Hello Child"));
    }

    @Test
    public void simpleBeanScanning() throws Exception {
        // @Component 가 붙은 클래스를 스캔할 패키지를 넣어서 컨텍스트를 만들어준다. 생성과 동시에 자동으로 스캔과 등록이 진행
        ApplicationContext context = new AnnotationConfigApplicationContext("springbook.learningtest.spring.ioc.bean");
        // 자동등록되는 빈의 아이디는 클래스 이름의 첫 글자를 소문자로 바꿔서 사용
        AnnotatedHello hello = context.getBean("annotatedHello", AnnotatedHello.class);
        assertThat(hello, is(notNullValue()));
    }

    @Test
    public void filteredBeanScanning() throws Exception {
        ApplicationContext context = new GenericXmlApplicationContext(basePath + "filteredScanningContext.xml");
        Hello hello = context.getBean("hello", Hello.class);
        assertThat(hello, is(notNullValue()));
    }

    @Test
    public void configurationBean() throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);

        AnnotatedHello hello = context.getBean("annotatedHello", AnnotatedHello.class);
        assertThat(hello, is(notNullValue()));

        AnnotatedHelloConfig config = context.getBean("annotatedHelloConfig", AnnotatedHelloConfig.class);
        assertThat(config, is(notNullValue()));

        assertThat(config.annotatedHello(), is(sameInstance(hello)));
        assertThat(config.annotatedHello(), is(config.annotatedHello()));

        System.out.println("systemProperties : " + context.getBean("systemProperties").getClass());
    }

    @Test
    public void constructorArgName() throws Exception {
        ApplicationContext context = new GenericXmlApplicationContext(basePath + "constructorInjection.xml");

        Hello hello = context.getBean("hello", Hello.class);
        hello.print();

        assertThat(context.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void autowire() throws Exception {
        ApplicationContext context = new GenericXmlApplicationContext(basePath + "autowire.xml");

        Hello hello = context.getBean("hello", Hello.class);
        hello.print();

        assertThat(context.getBean("printer").toString(), is("Hello Spring"));
    }
}