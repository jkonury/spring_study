package com.jkonury.www;

import com.jkonury.www.domain.Type;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.RequestDispatcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("type", new Type());
        model.addAttribute("type1", new Type());

        return "home";
    }

    @ModelAttribute("types")
    public List<Type> types() {
        List<Type> list = new ArrayList<>();

        list.add(new Type(1, "관리자"));
        list.add(new Type(2, "회원"));
        list.add(new Type(3, "손님"));

        return list;
    }

    @ModelAttribute("interests")
    public Map<String, String> interests() {
        Map<String, String> interests = new HashMap<>();

        interests.put("A", "Java");
        interests.put("B", "C#");
        interests.put("C", "Python");
        interests.put("D", "Ruby");

        return interests;
    }

    @RequestMapping
    public void t() throws Exception {

    }
}
