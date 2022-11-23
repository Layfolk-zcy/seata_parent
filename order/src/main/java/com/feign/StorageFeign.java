package com.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 微服务底座平台
 * @version 2.0.0
 * @title: StorageFeign
 * @projectName: seata_parent
 * @description: storage feign
 * @date: 2022-11-23 16:17
 **/
@FeignClient("storage")
public interface StorageFeign {

    /**
     * Description
     *
     * @date 2022/11/23 16:18
     * @param: num
     * @return: boolean
     */
    @GetMapping("/storage")
    int deduct(int num);
}
