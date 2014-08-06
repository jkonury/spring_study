package com.jkonury.www.quartz;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author JiHong Jang
 * @since 2014.08.06
 */
public class QuartzTest {
    public static void main (String[] args) throws Exception {
        new ClassPathXmlApplicationContext("context-quartz.xml");
    }
}
