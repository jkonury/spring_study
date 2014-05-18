package springbook.user.dao;

import org.hamcrest.Matcher;
import springbook.user.domain.User;

import java.util.List;

/**
 * @author JiHong Jang
 * @since 2014.05.06
 */
public interface UserDao {

    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

    void update(User user);

}
