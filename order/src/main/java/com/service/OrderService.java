package com.service;

import com.bean.Category;
import com.dao.OrderDao;
import com.feign.StorageFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 微服务底座平台
 * @version 2.0.0
 * @title: OrderService
 * @projectName: seata_parent
 * @description: service
 * @date: 2022-11-23 16:13
 **/
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private StorageFeign storageFeign;

    @Transactional(rollbackFor = Exception.class)
    public int insert(Category category) {
        final int result = orderDao.insert(category);
        /*storageFeign.deduct(6);
        int i = 5 / 0;*/
        return result;
    }
}
