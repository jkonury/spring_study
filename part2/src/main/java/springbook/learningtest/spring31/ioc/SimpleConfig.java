package springbook.learningtest.spring31.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SimpleConfig {
    @Autowired
    Hello hello;

    @Bean
    Hello hello() {
        return new Hello();
    }
}

class Hello {
    @PostConstruct
    public void init() {
        System.out.println("Init");
    }

    public void sayHello() {
        System.out.println("Hello");
    }
}
