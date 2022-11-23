package com.controller;

import com.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 微服务底座平台
 * @version 2.0.0
 * @title: StorageController
 * @projectName: seata_parent
 * @description: controller
 * @date: 2022-11-23 16:06
 **/
@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping("/storage")
    public int deduct(int num) {
        return storageService.deduct(num);
    }

}
