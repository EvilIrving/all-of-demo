package com.ygkj.flood;

import com.ygkj.flood.vo.request.ResponsibleReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @create 2022-05-09 9:36
 * @description
 */
@Api(tags = "山洪灾害 通知责任人")
public interface ResponsibleControllerApi {

    @ApiOperation("渔船信息 - 列表")
    CommonResult boatList(ResponsibleReqVo boatBase);

    @ApiOperation("渔船信息 - 新增或更新")
    CommonResult boatSaveOrUpdate(ResponsibleReqVo boatBase);

    @ApiOperation("渔船信息 - 删除")
    CommonResult boatDelete(String id);

    @ApiOperation("种粮大户 - 列表")
    CommonResult growGrainList(ResponsibleReqVo base);

    @ApiOperation("种粮大户 - 新增或更新")
    CommonResult growGrainSaveOrUpdate(ResponsibleReqVo base);

    @ApiOperation("种粮大户 - 删除")
    CommonResult growGrainDelete(String id);


    @ApiOperation("根据类别筛选 通知人列表")
    @ApiImplicitParam(name = "type",value = "boat 船,farmer 种粮,slj 水利局通讯录,torrent 山洪灾害预警责任人 ,three 三个责任人")
    CommonResult responsibleListByType(String type, String key);


}
