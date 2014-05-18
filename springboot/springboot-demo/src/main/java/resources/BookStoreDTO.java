package resources;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiHong Jang
 * @since 2014.05.05
 */
@Data
public class BookStoreDTO extends ResourceSupport{
    private String title;

    private List<BookDTO> books = new ArrayList<BookDTO>();

    public void add(BookDTO bookDTO) {
        getBooks().add(bookDTO);
    }
}
