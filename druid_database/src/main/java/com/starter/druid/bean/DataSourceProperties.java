package com.starter.druid.bean;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author 后端技术框架
 * @version 2.0.0
 * @title: 数据库连接池Druid实现模块
 * @projectName staging-framework-starters
 * @description: TODO 数据源配置文件
 * @date 2022/6/26 下午13:14
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DataSourceProperties {
    public static final Logger log = LoggerFactory.getLogger(DataSourceProperties.class);
    //TODO 配置初始化大小
    private int initialSize;
    //TODO 配置初始化最小
    private int minIdle;
    //TODO 配置初始化最大
    private int maxActive;
    //TODO 配置获取连接等待超时的时间
    private int maxWait;
    //TODO  配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    private int timeBetweenEvictionRunsMillis;
    //TODO  配置一个连接在池中最小生存的时间，单位是毫秒
    private int minEvictableIdleTimeMillis;
    //TODO  配置一个连接在池中最大生存的时间，单位是毫秒
    private int maxEvictableIdleTimeMillis;
    //TODO  用来检测连接是否有效的sql，要求是一个查询语句
    private String validationQuery;
    //TODO 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测
    private boolean testWhileIdle;
    //TODO 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
    private boolean testOnBorrow;
    //TODO 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
    private boolean testOnReturn;
    //TODO 通过connectProperties属性来打开mergeSql功能；慢SQL记录
    private String connectionProperties;
    //TODO 打开PSCache，并且指定每个连接上PSCache的大小
    private boolean poolPreparedStatements;
    //TODO PSCache 最大连接数量
    private int maxPoolPreparedStatementPerConnectionSize;
    //TODO 合并多个DruidDataSource的监控数据
    private boolean useGlobalDataSourceStat;
    //TODO 按需开启过滤器
    private boolean isonfilters;
    //TODO 配置多个英文逗号分隔(统计，sql注入，log4j过滤) 防御sql注入的filter:wall
    private String filters;

    public DruidDataSource setDataSource(DruidDataSource datasource) {
        // 配置初始化大小、最小、最大
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);
        // 配置获取连接等待超时的时间
        datasource.setMaxWait(maxWait);
        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // 配置一个连接在池中最小、最大生存的时间，单位是毫秒
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
        // 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
        datasource.setValidationQuery(validationQuery);
        /** 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
        datasource.setTestWhileIdle(testWhileIdle);
        /** 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnBorrow(testOnBorrow);
        /** 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnReturn(testOnReturn);
        /** 通过connectProperties属性来打开mergeSql功能；慢SQL记录 */
        datasource.setConnectionProperties(connectionProperties);
        /** 配置多个英文逗号分隔(统计，sql注入，log4j过滤) 防御sql注入的filter:wall */
        //datasource.setFilters(filters);
        /** 打开PSCache，并且指定每个连接上PSCache的大小 */
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        /** PSCache 最大连接数量 */
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        /** 合并多个DruidDataSource的监控数据 */
        datasource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
        /** 按需开启过滤器 */
        if (isonfilters) {
            /** 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙 */
            try {
                datasource.setFilters(filters);
            } catch (Exception e) {
                log.error("加载数据源过滤器出现异常", e);
            }
        }
        return datasource;
    }

    /**
     * 去除监控页面底部的广告
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
    public FilterRegistrationBean removeDruidFilterRegistrationBean(DruidStatProperties properties) {
        // 获取web监控页面的参数
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        // 提取common.js的配置路径
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        final String filePath = "support/http/resources/js/common.js";
        // 创建filter进行过滤
        Filter filter = new Filter() {
            public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, javax.servlet.FilterChain filterChain) throws IOException, ServletException {
                filterChain.doFilter(servletRequest, servletResponse);
                // 重置缓冲区，响应头不会被重置
                servletResponse.resetBuffer();
                // 获取common.js
                String text = Utils.readFromResource(filePath);
                // 正则替换banner, 除去底部的广告信息
                text = text.replaceAll("<a.*?banner\"></a><br/>", "");
                text = text.replaceAll("powered.*?shrek.wang</a>", "");
                servletResponse.getWriter().write(text);
            }

            @Override
            public void destroy() {
            }
        };
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }

    /**
     * 配置 Druid 监控界面
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean srb = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //设置控制台管理用户--默认使用Security进行安全认证
        //srb.addInitParameter("loginUsername","staging");
        //srb.addInitParameter("loginPassword","staging");
        //是否可以重置数据
        srb.addInitParameter("resetEnable", "false");
        return srb;

    }

    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.webStatFilter.enabled", havingValue = "true")
    public FilterRegistrationBean statFilter() {
        //创建过滤器
        FilterRegistrationBean frb = new FilterRegistrationBean(new WebStatFilter());
        //设置过滤器过滤路径
        frb.addUrlPatterns("/*");
        //忽略过滤的形式
        frb.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return frb;
    }

    public static Logger getLog() {
        return log;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public int getMaxEvictableIdleTimeMillis() {
        return maxEvictableIdleTimeMillis;
    }

    public void setMaxEvictableIdleTimeMillis(int maxEvictableIdleTimeMillis) {
        this.maxEvictableIdleTimeMillis = maxEvictableIdleTimeMillis;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public String getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public boolean isPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public int getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public boolean isUseGlobalDataSourceStat() {
        return useGlobalDataSourceStat;
    }

    public void setUseGlobalDataSourceStat(boolean useGlobalDataSourceStat) {
        this.useGlobalDataSourceStat = useGlobalDataSourceStat;
    }

    public boolean isIsonfilters() {
        return isonfilters;
    }

    public void setIsonfilters(boolean isonfilters) {
        this.isonfilters = isonfilters;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }
}

