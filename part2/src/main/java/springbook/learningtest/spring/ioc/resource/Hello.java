package springbook.learningtest.spring.ioc.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Hello {
    @Value("Spring")
    private String name;

    @Resource
    private Printer printer;

    public Hello() {
    }

    public Hello(String name, Printer printer) {
        this.name = name;
        this.printer = printer;
    }

    public String sayHello() {
        return "Hello " + name;
    }

    public void print() {
        this.printer.print(this.sayHello());
    }

    public void setName(String name) {
        this.name = name;
    }
}