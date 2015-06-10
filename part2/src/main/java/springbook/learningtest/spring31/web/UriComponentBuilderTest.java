package springbook.learningtest.spring31.web;

import org.junit.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class UriComponentBuilderTest {
    @Test
    public void urlCB() throws Exception {
        UriComponents uriComponents =
            UriComponentsBuilder.fromUriString("http://example.com/hotels/{hotel}/bookings/{booking}").build();

        System.out.println(uriComponents.expand("42", "21").encode().toString());

        int userId = 10;
        int orderId = 20;
        UriComponents uc =
            UriComponentsBuilder.fromUriString("http://www.myshop.com/users/{user}/orders/{order}").build();

        System.out.println(uc.expand(userId, orderId).encode().toString());

        UriComponents uc2 = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("www.myshop.com")
            .path("/users/{user}/order/{order}")
            .build();

        System.out.println(uc2.expand("test", 10).encode().toString());

    }
}
