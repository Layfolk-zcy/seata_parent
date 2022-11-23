package com.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author 微服务底座平台
 * @version 2.0.0
 * @title: StorageDao
 * @projectName: seata_parent
 * @description: dao
 * @date: 2022-11-23 16:06
 **/
@Repository
public class StorageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int deduct(int num) {
        String sql = "UPDATE order_item " +
                "SET number = ? WHERE ID = 1";
        return jdbcTemplate.update(sql, num);
    }
}
