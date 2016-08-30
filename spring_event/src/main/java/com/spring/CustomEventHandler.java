package com.spring;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CustomEventHandler implements ApplicationListener<CustomEvent> {

    public void onApplicationEvent(CustomEvent event) {
        System.out.println("이벤트 실행");
        System.out.println(event.toString());
    }

}