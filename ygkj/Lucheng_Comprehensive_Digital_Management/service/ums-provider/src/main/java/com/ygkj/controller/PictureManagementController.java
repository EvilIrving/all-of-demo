package com.ygkj.controller;

import com.ygkj.client.WmsClient;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: fml
 * @Date: 2020/9/24 09:10
 * @Description:
 */
//@Api(tags = "一张图管理")
//@RestController
//@RequestMapping("/pictureManagement")
public class PictureManagementController {

    @Autowired
    private WmsClient wmsClient;

    @ApiOperation(value = "水域统计信息(水域类型为空查询全部)")
    @GetMapping("/waterBaseTotal")
    @ApiImplicitParam(name = "waterType", value = "水域类型")
    public Object waterBaseTotal(@RequestParam(value = "waterType", required = false) String waterType) {

        return wmsClient.waterBaseTotal(waterType);
    }

    @ApiOperation(value = "水域类型数量统计")
    @GetMapping("/waterCountTotal")
    public Object waterCountTotal() {

        return wmsClient.waterCountTotal();
    }

    @ApiOperation(value = "水域类型面积占比、数量占比")
    @GetMapping("/typeAreaRatio")
    public Object typeAreaRatio() {

        return wmsClient.typeAreaRatio();
    }

    @ApiOperation(value = "乡镇详情")
    @GetMapping("/townWaterDetail")
    public Object townWaterDetail() {

        return wmsClient.townWaterDetail();
    }

    @ApiOperation(value = "不同水域类型信息列表")
    @GetMapping("/waterTypeInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "waterType", value = "水域类型"),
            @ApiImplicitParam(name = "keyword", value = "关键词(水域名称)"),
            @ApiImplicitParam(name = "grade", value = "等级(暂未有，默认全部)")})
    public Object waterTypeInfo(
            @RequestParam(value = "waterType", required = false) String waterType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "grade", required = false) String grade) {

        return wmsClient.waterTypeInfo(waterType, keyword, grade);
    }

    @ApiOperation(value = "水域变化统计")
    @GetMapping("/waterChangeStatistics")
    public Object waterChangeStatistics() {

        return wmsClient.waterChangeStatistics();
    }

    @ApiOperation(value = "水域变化信息列表")
    @GetMapping("/changeInfoList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "waterType", value = "水域类型"),
            @ApiImplicitParam(name = "waterName", value = "关键词(水域名称)")})
    public Object changeInfoList(@RequestParam(value = "waterType", required = false) String waterType,
                                 @RequestParam(value = "waterName", required = false) String waterName) {

        return wmsClient.changeInfoList(waterType, waterName);
    }

    @ApiOperation(value = "统计项目立项数")
    @GetMapping("/projectCount")
    public Object projectCount() {

        return wmsClient.projectCount();
    }

    // 获取项目类型
    /*public Object queryProjectType() {

        return wmsClient.queryProjectType();
    }*/

    @ApiOperation(value = "水域审批-项目立项列表")
    @GetMapping("/queryProjectInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectType", value = "项目类型"),
            @ApiImplicitParam(name = "areaName", value = "行政区划"),
            @ApiImplicitParam(name = "keyword", value = "关键词(项目名称)")})
    public Object queryProjectInfo(
            @RequestParam(value = "projectType", required = false) String projectType,
            @RequestParam(value = "areaName", required = false) String areaName,
            @RequestParam(value = "keyword", required = false) String keyword) {

        return wmsClient.queryProjectInfo(projectType, areaName, keyword);
    }
}
