package springbook.user.domain;

import lombok.Data;

/**
 * @author JiHong Jang
 * @since 2014.05.05
 */
@Data
public class User {
    String id;
    String name;
    String password;

    public User() {}

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
