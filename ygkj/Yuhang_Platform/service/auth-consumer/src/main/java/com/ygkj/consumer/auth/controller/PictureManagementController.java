package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.api.monitor.PictureManagementControllerApi;
import com.ygkj.consumer.auth.client.authentication.PictureManagementClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: fml
 * @Date: 2020/9/24 09:15
 * @Description:
 */
@RestController
@RequestMapping("/pictureManagement")
public class PictureManagementController implements PictureManagementControllerApi {

    @Autowired
    private PictureManagementClient pictureManagementClient;

    @Override
    @GetMapping("/waterBaseTotal")
    public Object waterBaseTotal(@RequestParam(value = "waterType",required = false) String waterType){

        return pictureManagementClient.waterBaseTotal(waterType);
    }

    @Override
    @GetMapping("/waterCountTotal")
    public Object waterCountTotal() {

        return pictureManagementClient.waterCountTotal();
    }

    @Override
    @GetMapping("/typeAreaRatio")
    public Object typeAreaRatio(){

        return pictureManagementClient.typeAreaRatio();
    }

    @Override
    @GetMapping("/townWaterDetail")
    public Object townWaterDetail(){

        return pictureManagementClient.townWaterDetail();
    }

    @Override
    @GetMapping("/waterTypeInfo")
    public Object waterTypeInfo(
            @RequestParam(value = "waterType", required = false) String waterType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "grade", required = false) String grade) {

        return pictureManagementClient.waterTypeInfo(waterType, keyword, grade);
    }

    @Override
    @GetMapping("/waterChangeStatistics")
    public Object waterChangeStatistics() {

        return pictureManagementClient.waterChangeStatistics();
    }

    @Override
    @GetMapping("/changeInfoList")
    public Object changeInfoList(@RequestParam(value = "waterType", required = false) String waterType,
                                 @RequestParam(value = "waterName", required = false) String waterName) {

        return pictureManagementClient.changeInfoList(waterType, waterName);
    }

    @Override
    @GetMapping("/projectCount")
    public Object projectCount() {

        return pictureManagementClient.projectCount();
    }

    /*// 获取项目类型
    public Object queryProjectType() {

        return pictureManagementClient.queryProjectType();
    }*/

    @Override
    @GetMapping("/queryProjectInfo")
    public Object queryProjectInfo(
            @RequestParam(value = "projectType", required = false) String projectType,
            @RequestParam(value = "areaName", required = false) String areaName,
            @RequestParam(value = "keyword", required = false) String keyword) {

        return pictureManagementClient.queryProjectInfo(projectType, areaName, keyword);
    }
}
