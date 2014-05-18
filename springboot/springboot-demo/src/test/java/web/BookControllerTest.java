package web;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author JiHong Jang
 * @since 2014.05.05
 */
public class BookControllerTest {

    @Test
    public void bookForm() throws Exception {

        // Given
        BookController bookController = new BookController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        // When
        ResultActions perform = mockMvc.perform(get("/book"));


        // Then
        perform.andExpect(status().isOk());
        perform.andDo(print());


    }
}
