package com.starter.druid.inter;


import com.starter.druid.bean.DataSourcesType;

import java.lang.annotation.*;

/**
 * @author 后端技术框架
 * @version 2.0.0
 * @title: 数据库连接池Druid实现模块
 * @projectName staging-framework-starters
 * @description: TODO 数据源自定义注解
 * @date 2022/6/26 下午13:14
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    DataSourcesType name() default DataSourcesType.MASTER;
}

