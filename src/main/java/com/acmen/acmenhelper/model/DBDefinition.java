package com.acmen.acmenhelper.model;

/**
 * 数据库模型接口，具体的数据库类要实现这个类
 * @author gaowenfeng
 * @date 2018/5/16
 */
public interface DBDefinition {

    String getDriverClass();

    String getUrl();

    String getUsername();

    String getPassword();
}
