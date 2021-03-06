package springbook.user.domain;

import lombok.Data;

/**
 * @author JiHong Jang
 * @since 2014.05.11
 */
@Data
public class User {
    String id;
    String name;
    String password;
    String email;
    Level level;
    int login;
    int recommend;

    public User() {
    }

    public User(String id, String name, String password, String email, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은  업그레이드가 불가능합니다");
        }
        else {
            this.level = nextLevel;
        }
    }
}
