package springbook.learningtest.spring.web.atmvc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.propertyeditors.CharsetEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springbook.learningtest.spring.web.AbstractDispatcherServletTest;
import springbook.user.domain.Level;

import java.beans.PropertyEditorSupport;
import java.nio.charset.Charset;

public class BindingTest extends AbstractDispatcherServletTest{
    @Test
    public void defaultPropertyEditor() throws Exception {
        setClasses(DefaultPEController.class);
        initRequest("/user/hello").addParameter("charset", "UTF-8");
        runService();
        assertModel("charset", Charset.forName("UTF-8"));
    }

    @Controller
    static class DefaultPEController {
        @RequestMapping("/user/hello")
        public void hello(@RequestParam Charset charset, Model model) {
            model.addAttribute("charset", charset);
        }
    }

    @Test
    public void charsetEditor() throws Exception {
        CharsetEditor charsetEditor = new CharsetEditor();
        charsetEditor.setAsText("UTF-8");
        assertThat(charsetEditor.getValue(), is(instanceOf(Charset.class)));
        assertThat(charsetEditor.getValue(), is(Charset.forName("UTF-8")));
    }

    @Test
    public void levelPropertyEditor() throws Exception {
        LevelPropertyEditor levelPropertyEditor = new LevelPropertyEditor();

        levelPropertyEditor.setValue(Level.BASIC);
        assertThat(levelPropertyEditor.getAsText(), is("1"));

        levelPropertyEditor.setAsText("3");
        assertThat(levelPropertyEditor.getValue(), is(Level.GOLD));
    }


    static class LevelPropertyEditor extends PropertyEditorSupport {
        @Override
        public String getAsText() {
            // setValue에 의해 저장된 Level 타입 오브젝트를 가져와서 값을 문자로 변환
            return String.valueOf(((Level)this.getValue()).intValue());
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            // 파라미터로 제공된 문자열을 Level의 스태틱 메소드를 이용해 문자열로 변환한 뒤에 저장
            this.setValue(Level.valueOf(Integer.parseInt(text.trim())));
        }
    }

    @Test
    public void levelTypeParameter() throws Exception {
        setClasses(SearchController.class);
        initRequest("/user/search").addParameter("level", "1");
        runService();
        assertModel("level", Level.BASIC);
    }

    @Controller
    static class SearchController {
        @InitBinder
        public void initBinder(WebDataBinder dataBinder) {
            dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
        }

        @RequestMapping("/user/search")
        public void search(@RequestParam Level level, Model model) {
            model.addAttribute("level", level);
        }
    }

    @Test
    public void dataBinder() throws Exception {
        WebDataBinder dataBinder = new WebDataBinder(null);
        dataBinder.registerCustomEditor(Level.class, new LevelPropertyEditor());
        assertThat(dataBinder.convertIfNecessary("1", Level.class), is(Level.BASIC));
    }
}
