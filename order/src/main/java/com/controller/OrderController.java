package com.controller;

import com.bean.Category;
import com.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 微服务底座平台
 * @version 2.0.0
 * @title: OrderController
 * @projectName: seata_parent
 * @description: controller
 * @date: 2022-11-23 16:07
 **/
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GlobalTransactional
    @GetMapping("/order")
    public int getResult(@RequestBody Category category) {
        return orderService.insert(category);
    }
}
