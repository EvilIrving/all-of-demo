package com.ygkj.consumer.auth.client.authentication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: huangzh
 * @Date: 2020/9/24 09:17
 * @Description:
 */
@FeignClient(contextId = "pictureManagement", name = "authentication-system")
public interface PictureManagementClient {

    @GetMapping("/pictureManagement/waterBaseTotal")
    Object waterBaseTotal(@RequestParam(value = "waterType", required = false) String waterType);

    @GetMapping("/pictureManagement/waterCountTotal")
    Object waterCountTotal();

    @GetMapping("/pictureManagement/typeAreaRatio")
    Object typeAreaRatio();

    @GetMapping("/pictureManagement/townWaterDetail")
    Object townWaterDetail();

    @GetMapping("/pictureManagement/waterTypeInfo")
    Object waterTypeInfo(
            @RequestParam(value = "waterType", required = false) String waterType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "grade", required = false) String grade);

    @GetMapping("/pictureManagement/waterChangeStatistics")
    Object waterChangeStatistics();

    @GetMapping("/pictureManagement/changeInfoList")

    Object changeInfoList(@RequestParam(value = "waterType", required = false) String waterType,
                          @RequestParam(value = "waterName", required = false) String waterName);

    @GetMapping("/pictureManagement/projectCount")
    Object projectCount();

    @GetMapping("/pictureManagement/queryProjectInfo")
    Object queryProjectInfo(
            @RequestParam(value = "projectType", required = false) String projectType,
            @RequestParam(value = "areaName", required = false) String areaName,
            @RequestParam(value = "keyword", required = false) String keyword);
}
