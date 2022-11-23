package com.service;

import com.dao.StorageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 微服务底座平台
 * @version 2.0.0
 * @title: StorageService
 * @projectName: seata_parent
 * @description: storage service
 * @date: 2022-11-23 16:30
 **/
@Service
public class StorageService {

    @Autowired
    private StorageDao storageDao;

    @Transactional
    public int deduct(int num) {
        return storageDao.deduct(num);
    }

}
