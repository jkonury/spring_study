package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author JiHong Jang
 * @since 2014.05.01
 */
public interface ConnectionMaker {
    public abstract Connection makeConnection() throws ClassNotFoundException, SQLException;
}
