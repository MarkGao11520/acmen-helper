package com.acmen.acmenhelper.model;

import lombok.Data;
import lombok.Setter;

/**
 * Mysql接口定义
 * @author gaowenfeng
 * @date 2018/5/16
 */
@Setter
public class MysqlDBDefinition  implements DBDefinition{
    private String ip;

    private String db;

    private String url;

    private String dbName;

    private String username;

    private String password;

    private String port;


    @Override
    public String getUrl() {
        return "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getDriverClass() {
        return "com.mysql.jdbc.Driver";
    }

}
