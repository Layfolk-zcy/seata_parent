package com.starter.druid.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.starter.druid.bean.DataSourceProperties;
import com.starter.druid.bean.DataSourcesType;
import com.starter.druid.bean.DbMasterType;
import com.starter.druid.bean.DbSlaveType;
import com.starter.druid.datasrc.DynamicDataSource;
import com.starter.druid.utils.AesCbcUtil;
import io.seata.rm.datasource.DataSourceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author 后端技术框架
 * @version 2.0.0
 * @title: 数据库连接池Druid实现模块
 * @projectName staging-framework-starters
 * @description: TODO 数据源配置类
 * @date 2022/6/26 下午13:14
 */
@Configuration
public class DataSourceConfiguration {

    public static final Logger log = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Value("${spring.datasource.druid.slave.enable}")
    private String enable;
    /**
     * 主库读取配置
     * @param dataSourceProperties
     * @return DataSource
     */
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DruidDataSource masterInterDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }

    /**
     * 主库配置二次改造
     */
    @Bean
    public DataSource masterDataSource(DataSourceProperties dataSourceProperties) {
        DruidDataSource datasource = masterInterDataSource(dataSourceProperties);
        try{
            datasource.setPassword(datasource.getPassword());
        }catch (Exception e){
            log.error("master数据源账号密码解密出现异常",e);
        }
        if (datasource.getDriverClassName().toUpperCase(Locale.ENGLISH).contains(DbMasterType.MYSQL.getValue())) {
            DbMasterType.CURRENT.setValue(DbMasterType.MYSQL.getValue());
        } else if (datasource.getDriverClassName().toUpperCase(Locale.ENGLISH).contains(DbMasterType.ORACLE.getValue())) {
            DbMasterType.CURRENT.setValue(DbMasterType.ORACLE.getValue());
        } else if (datasource.getDriverClassName().toUpperCase(Locale.ENGLISH).contains(DbMasterType.DB2.getValue())) {
            DbMasterType.CURRENT.setValue(DbMasterType.DB2.getValue());
        } else if (datasource.getDriverClassName().toUpperCase(Locale.ENGLISH).contains(DbMasterType.DM.getValue())) {
            DbMasterType.CURRENT.setValue(DbMasterType.DM.getValue());
        } else if (datasource.getDriverClassName().toUpperCase(Locale.ENGLISH).contains(DbMasterType.OpenGauss.getValue())) {
            DbMasterType.CURRENT.setValue(DbMasterType.OpenGauss.getValue());
        } else {
            log.error("master数据源未被识别的数据库类型：{}", datasource.getDriverClassName());
        }
        // seata AT模式下代理sql
        return new DataSourceProxy(datasource);
    }

    /**
     * 从库
     * @param dataSourceProperties
     * @return DataSource
     */
    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    public DruidDataSource slaveInterDataSource(DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.setDataSource(DruidDataSourceBuilder.create().build());
    }

    /**
     * 从库配置二次改造
     */
    @Bean
    //是否开启数据源开关---若不开启 默认使用默认数据源
    //@ConditionalOnProperty( prefix = "spring.datasource.druid.slave", name = "enable", havingValue = "true")
    public DataSource slaveDataSource(DataSourceProperties dataSourceProperties) {
        DruidDataSource datasource = null;
        //若开启则将备库加入
        if("true".equals(enable.toLowerCase(Locale.ENGLISH))){
            datasource = slaveInterDataSource(dataSourceProperties);
            try{
                datasource.setPassword(datasource.getPassword());
            }catch (Exception e){
                log.error("slave数据源账号密码解密出现异常",e);
            }
            if (datasource.getDriverClassName().toUpperCase(Locale.ENGLISH).contains(DbSlaveType.MYSQL.getValue())) {
                DbSlaveType.CURRENT.setValue(DbSlaveType.MYSQL.getValue());
            } else if (datasource.getDriverClassName().toUpperCase(Locale.ENGLISH).contains(DbSlaveType.ORACLE.getValue())) {
                DbSlaveType.CURRENT.setValue(DbSlaveType.ORACLE.getValue());
            } else if (datasource.getDriverClassName().toUpperCase(Locale.ENGLISH).contains(DbSlaveType.DB2.getValue())) {
                DbSlaveType.CURRENT.setValue(DbSlaveType.DB2.getValue());
            } else if (datasource.getDriverClassName().toUpperCase(Locale.ENGLISH).contains(DbSlaveType.DM.getValue())) {
                DbSlaveType.CURRENT.setValue(DbSlaveType.DM.getValue());
            } else if (datasource.getDriverClassName().toUpperCase(Locale.ENGLISH).contains(DbSlaveType.OpenGauss.getValue())) {
                DbSlaveType.CURRENT.setValue(DbSlaveType.OpenGauss.getValue());
            } else {
                log.error("slave数据源未被识别的数据库类型：{}", datasource.getDriverClassName());
            }
        }else{
            datasource = new DruidDataSource();
        }
        // seata AT模式下代理sql
        return new DataSourceProxy(datasource);
    }

    /**
     * 1.@DependsOn({"masterDataSource","slaveDataSource"})配合Springboot启动类，解决Druid多数据源循环依赖的问题
     * 设置数据源
     */
    @Bean(name = "dynamicDataSource")
    @Primary
    @DependsOn({"masterDataSource","slaveDataSource"})
    public DynamicDataSource dynamicDataSource(DataSource masterDataSource, DataSource slaveDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();

        DynamicDataSource dynamicDataSource = DynamicDataSource.build();
        targetDataSources.put(DataSourcesType.MASTER.name(), masterDataSource);
        //若开启则将备库加入
        if("true".equals(enable.toLowerCase(Locale.ENGLISH))){
            targetDataSources.put(DataSourcesType.SLAVE.name(), slaveDataSource);
        }
        //默认数据源配置 DefaultTargetDataSource
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        //额外数据源配置 TargetDataSources
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }
}


