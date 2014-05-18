package springbook.user.service;

import springbook.user.domain.User;

/**
 * @author JiHong Jang
 * @since 2014.05.06
 */
public interface UserService {
    void add(User user);
    void upgradeLevels();
}
