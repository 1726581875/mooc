package cn.edu.lingnan;

import cn.edu.lingnan.util.ConnectUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author xiaomingzhang
 * @date 2022/1/8
 */
public class ConnectionManager {

    private final static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    /**
     * todo 优先从线程池获取
     * 获取连接对象，并设置到当前线程ThreadLocal
     * @return
     */
    public static Connection getConnection() {
        Connection conn = connectionThreadLocal.get();
        if(conn == null) {
            conn = ConnectUtil.getConnection();
            connectionThreadLocal.set(conn);
        }
        return conn;
    }

    /**
     * 关闭连接
     */
    public static void closeConnection() {
        Connection conn = connectionThreadLocal.get();
        try {
            if (conn != null) {
                if (!conn.isClosed()) {
                    conn.close();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            connectionThreadLocal.remove();
        }

    }


}
