package com.starter.druid.bean;

/**
 * @author 后端技术框架
 * @version 2.0.0
 * @title: 数据库连接池Druid实现模块
 * @projectName staging-framework-starters
 * @description: TODO slave数据库类型
 * @date 2022/6/26 下午13:14
 */
public enum DbSlaveType {
    MYSQL("MYSQL"),
    ORACLE("ORACLE"),
    DB2("DB2"),
    DM("DM"),
    OpenGauss("OPENGAUSS"),
    CURRENT("");
    private String value;

    DbSlaveType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}