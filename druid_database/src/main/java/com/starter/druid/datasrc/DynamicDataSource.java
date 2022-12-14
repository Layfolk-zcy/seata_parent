package com.starter.druid.datasrc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author 后端技术框架
 * @version 2.0.0
 * @title: 数据库连接池Druid实现模块
 * @projectName staging-framework-starters
 * @description: TODO 获取数据源（依赖于 spring），定义一个类继承AbstractRoutingDataSource实现determineCurrentLookupKey方法，该方法可以实现数据库的动态切换
 * @date 2022/6/26 下午13:14
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    public static  DynamicDataSource build() {
        return new DynamicDataSource();
    }

    /**
     * 获取与数据源相关的key
     * 此key是Map<String,DataSource> resolvedDataSources 中与数据源绑定的key值
     * 在通过determineTargetDataSource获取目标数据源时使用
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }

}

