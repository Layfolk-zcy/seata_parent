package com.starter.druid.datasrc;

import com.starter.druid.bean.DbMasterType;
import com.starter.druid.bean.DbSlaveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 后端技术框架
 * @version 2.0.0
 * @title: 数据库连接池Druid实现模块
 * @projectName staging-framework-starters
 * @description: TODO 数据源切换处理
 * @date 2022/6/26 下午13:14
 */
public class DynamicDataSourceContextHolder {

    public static String dataType = "";

    public static final Logger log = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    /**
     *此类提供线程局部变量。这些变量不同于它们的正常对应关系是每个线程访问一个线程(通过get、set方法),有自己的独立初始化变量的副本。
     */
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 设置当前线程的数据源变量
     */
    public static void setDataSourceType(String dataSourceType) {
        if(!dataType.equals(dataSourceType)){
            dataType = dataSourceType;
            if("MASTER".equals(dataSourceType)){
                log.info("当前{}数据库，正在操作使用{}数据源", DbMasterType.CURRENT.getValue(),dataSourceType);
            }else{
                log.info("当前{}数据库，正在操作使用{}数据源", DbSlaveType.CURRENT.getValue(),dataSourceType);
            }
        }
        contextHolder.set(dataSourceType);
    }

    /**
     * 获取当前线程的数据源变量
     */
    public static String getDataSourceType() {
        return contextHolder.get();
    }

    /**
     * 删除与当前线程绑定的数据源变量
     */
    public static void removeDataSourceType() {
        contextHolder.remove();
    }

}

