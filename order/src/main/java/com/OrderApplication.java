package com;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 微服务底座平台
 * @version 2.0.0
 * @title: OrderApplication
 * @projectName: seata_parent
 * @description: 入口
 * @date: 2022-11-23 11:30
 **/
@SpringBootApplication(scanBasePackages = {"com"}, exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.**")
@EnableTransactionManagement
public class OrderApplication {
    public static void main(String[] args) {
            SpringApplication.run(OrderApplication.class, args);
    }
}
