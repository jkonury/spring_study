package springbook.user.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author JiHong Jang
 * @since 2014.05.05
 */

@Getter
@Setter
public class User {
    String id;
    String name;
    String password;
}
