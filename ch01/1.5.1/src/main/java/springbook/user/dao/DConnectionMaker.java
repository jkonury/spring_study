package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author JiHong Jang
 * @since 2014.05.01
 */
public class DConnectionMaker implements ConnectionMaker {
    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        Connection c = DriverManager.getConnection(
                "jdbc:hsqldb:hsql://localhost/users",
                "sa",
                "");
        return c;
    }
}
