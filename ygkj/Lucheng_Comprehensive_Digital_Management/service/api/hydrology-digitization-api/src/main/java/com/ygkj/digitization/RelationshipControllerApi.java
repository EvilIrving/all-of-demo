package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.RelBaseResVo;
import com.ygkj.digitization.vo.response.RelDetailResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "关联管理")
public interface RelationshipControllerApi {

    @ApiOperation("编辑水库/水位测站关联关系")
    CommonResult edit(RelBaseReqVo reqVo, HttpServletRequest request);

    @ApiOperation("删除水库/水位测站关联关系")
    CommonResult del(String ids);

    @ApiOperation(value = "水库/水位测站关联关系列表", response = RelBaseResVo.class)
    CommonResult list(RelListReqVo reqVo);

    @ApiOperation(value = "导出水库/水位测站关联关系列表")
    void exportList(RelListReqVo reqVo, HttpServletResponse response);

    @ApiOperation(value = "水库/水位测站关联关系详细信息", response = RelDetailResVo.class)
    CommonResult select(RelDetailReqVo reqVo);

    @ApiOperation("编辑潮位测站关联关系")
    CommonResult editTide(RelSeawallReqVo reqVo);

    @ApiOperation("删除潮位测站关联关系")
    CommonResult delTide(String stCodes);

    @ApiOperation("潮位测站关联关系列表")
    CommonResult listTide(RelListReqVo reqVo);

    @ApiOperation(value = "导出水库/水位测站关联关系列表")
    void exportListTide(RelListReqVo reqVo, HttpServletResponse response);

    @ApiOperation("潮位测站关联关系详细信息")
    CommonResult selectTide(String stCode);

    @ApiOperation("水库或海塘工程列表")
    CommonResult listProject(ProjectListReqVo reqVo);

    @ApiOperation("测站列表")
    CommonResult listSt(StListReqVo reqVo);
}
