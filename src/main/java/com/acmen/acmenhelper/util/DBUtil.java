package com.acmen.acmenhelper.util;

import com.acmen.acmenhelper.exception.GlobalException;
import com.acmen.acmenhelper.model.DBDefinition;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author gaowenfeng
 * @date 2018/5/16
 */
public class DBUtil {
    public static Connection getConnection(DBDefinition dbDefinition) throws GlobalException {

        Connection con = null;
        try {
            //创建驱动器
            Class.forName(dbDefinition.getDriverClass());
            con = DriverManager.getConnection(dbDefinition.getUrl(), dbDefinition.getUsername(), dbDefinition.getPassword());
        } catch (Exception e) {
            throw new GlobalException(1 , "获取数据库连接异常" , e);
        }
        return con;
    }

    /**
     * 关闭 connection
     *
     * @param rs
     * @param conn
     */
    public static void killConnection(ResultSet rs, Connection conn) {
        try {
            if (null != rs) {
                rs.close();
            }
            if (null != conn) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new GlobalException(1 , "关闭数据库连接异常" , e);
        }
    }
}
