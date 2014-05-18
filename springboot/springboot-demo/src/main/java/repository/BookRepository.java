package repository;

import domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author JiHong Jang
 * @since 2014.05.05
 */
public interface BookRepository extends JpaRepository<Book, Integer> {
}
