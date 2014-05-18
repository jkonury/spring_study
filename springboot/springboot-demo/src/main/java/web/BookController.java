package web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import domain.Book;
import repository.BookRepository;
import resources.BookDTO;
import resources.BookStoreDTO;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author JiHong Jang
 * @since 2014.05.05
 */
@Controller
public class BookController {
    @Autowired
    BookRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<BookStoreDTO> home() {
        BookStoreDTO store = new BookStoreDTO();
        store.setTitle("hong's Bookstore");

        for (Book book : repository.findAll()) {
            store.add(modelMapper.map(book, BookDTO.class));

        }

        store.add(linkTo(methodOn(BookController.class).home()).withSelfRel());

        return new ResponseEntity<BookStoreDTO>(store, HttpStatus.OK);
    }

    @RequestMapping(value = "/book", method = RequestMethod.GET)
    public String bookForm(Model model) {
        model.addAttribute("book", new Book());
        return "/form";
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public String bookSubmit(@ModelAttribute Book book, BindingResult error) {
        if(error.hasErrors()) {
            return "/form";
        }
        repository.save(book);
        return "redirect:/";
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<BookDTO> book(@PathVariable int id) {
        Book book = repository.findOne(id);
        BookDTO bookDTO = modelMapper.map(book, BookDTO.class);

        bookDTO.add(linkTo(methodOn(BookController.class).book(id)).withSelfRel());
        bookDTO.add(linkTo(methodOn(BookController.class).home()).withRel("stroe"));


        return new ResponseEntity<BookDTO>(bookDTO, HttpStatus.OK);
    }
}
