package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.SQLException;

/**
 * @author JiHong Jang
 * @since 2014.05.01
 */
public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new UserDaoFactory().userDao();

        User user = new User();
        user.setId("jkonury");
        user.setName("장지홍");
        user.setPassword("married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");

        System.out.println(dao.deleteAll());
    }
}
