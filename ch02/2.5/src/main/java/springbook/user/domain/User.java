package springbook.user.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author JiHong Jang
 * @since 2014.05.01
 */

@Getter
@Setter
public class User {
    String id;
    String name;
    String password;

    public User() {
    }

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
