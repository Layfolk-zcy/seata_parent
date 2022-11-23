package com.dao;

import com.bean.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * @author 微服务底座平台
 * @version 2.0.0
 * @title: OraderDao
 * @projectName: seata_parent
 * @description: dao
 * @date: 2022-11-23 16:07
 **/
@Repository
public class OrderDao {

    Logger logger = LoggerFactory.getLogger(OrderDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insert(Category category) {
        int rec = 0;
        try {
            String sql = "INSERT INTO category (ID, NAME)" +
                    "VALUES(:id, :name)";
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", category.getId());
            map.put("name", category.getName());
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            rec = namedParameterJdbcTemplate.update(sql, map);
        } catch (Exception e) {
            logger.error("数据库应用信息新增功能异常", e);
            throw new RuntimeException("数据库应用信息新增功能异常");
        }
        return rec;
    }
}
