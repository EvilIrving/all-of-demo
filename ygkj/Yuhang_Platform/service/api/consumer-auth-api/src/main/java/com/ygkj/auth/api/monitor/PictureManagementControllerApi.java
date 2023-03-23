package com.ygkj.auth.api.monitor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: fml
 * @Date: 2020/9/24 09:19
 * @Description:
 */
@Api(value = "PictureManagementControllerApi", tags = "一张图管理")
public interface PictureManagementControllerApi {

    @ApiOperation(value = "水域统计信息(水域类型为空查询全部)")
    @ApiImplicitParam(name = "waterType", value = "水域类型")
    Object waterBaseTotal(String waterType);

    @ApiOperation(value = "水域类型数量统计")
    Object waterCountTotal();

    @ApiOperation(value = "水域类型面积占比、数量占比")
    Object typeAreaRatio();

    @ApiOperation(value = "乡镇详情")
    Object townWaterDetail();

    @ApiOperation(value = "不同水域类型信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "waterType", value = "水域类型"),
            @ApiImplicitParam(name = "keyword", value = "关键词(水域名称)"),
            @ApiImplicitParam(name = "grade", value = "等级(暂未有，默认全部)")})
    Object waterTypeInfo(String waterType, String keyword, String grade);

    @ApiOperation(value = "水域变化统计")
    Object waterChangeStatistics();

    @ApiOperation(value = "水域变化信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "waterType", value = "水域类型"),
            @ApiImplicitParam(name = "waterName", value = "关键词(水域名称)")})
    Object changeInfoList(String waterType, String waterName);

    @ApiOperation(value = "统计项目立项数")
    Object projectCount();

    /*// 获取项目类型
    Object queryProjectType() {

        return pictureManagementClient.queryProjectType();
    }*/

    @ApiOperation(value = "水域审批-项目立项列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectType", value = "项目类型"),
            @ApiImplicitParam(name = "areaName", value = "行政区划"),
            @ApiImplicitParam(name = "keyword", value = "关键词(项目名称)")})
    Object queryProjectInfo(String projectType, String areaName, String keyword);
}
