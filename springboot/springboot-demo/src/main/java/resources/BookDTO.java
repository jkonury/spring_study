package resources;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 * @author JiHong Jang
 * @since 2014.05.05
 */
@Data
public class BookDTO extends ResourceSupport{
    private String title;

    private String price;
}
