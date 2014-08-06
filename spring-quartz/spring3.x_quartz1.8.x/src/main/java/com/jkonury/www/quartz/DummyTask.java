package com.jkonury.www.quartz;

import org.springframework.stereotype.Component;

/**
 * @author JiHong Jang
 * @since 2014.08.06
 */
@Component
public class DummyTask {
    public void print() {
        System.out.println("Spring 3.2 + Quartz 1.8 ");
    }
}
