package springbook.learningtest.spring31.ioc;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class BeanDefinitionUtils {
    public static void printBeanDefinitions(ConfigurableApplicationContext context) {
        List<List<String>> roleBeanInfos = new ArrayList<>();
        roleBeanInfos.add(new ArrayList<>());
        roleBeanInfos.add(new ArrayList<>());
        roleBeanInfos.add(new ArrayList<>());

        ConfigurableListableBeanFactory factory = context.getBeanFactory();
        for (String name : factory.getBeanDefinitionNames()) {
            int role = factory.getBeanDefinition(name).getRole();
            List<String> beanInfos = roleBeanInfos.get(role);
            beanInfos.add(role + "\t" + name + "\t" + context.getBean(name).getClass().getName());
        }

        for (List<String> beanInfos : roleBeanInfos) {
            beanInfos.forEach(System.out::println);
        }
    }
}
