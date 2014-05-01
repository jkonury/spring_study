package springbook.user.dao;

/**
 * @author JiHong Jang
 * @since 2014.05.01
 */
public class UserDaoFactory {
    public UserDao userDao() {
        UserDao dao = new UserDao(connectionMaker());
        return dao;
    }

    public ConnectionMaker connectionMaker() {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        return connectionMaker;
    }
}
