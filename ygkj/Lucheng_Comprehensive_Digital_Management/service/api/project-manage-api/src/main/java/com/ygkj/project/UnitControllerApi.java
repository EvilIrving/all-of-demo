package com.ygkj.project;

import com.ygkj.project.model.UnitJobResponsibility;
import com.ygkj.project.model.UnitPositionSet;
import com.ygkj.project.model.UnitResponsibility;
import com.ygkj.project.vo.request.UnitPositionSetReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "管理单位")
public interface UnitControllerApi {
    @ApiOperation("鹿城区街道")
    CommonResult selectLCArea();

    @ApiOperation("所有工程")
    CommonResult selectProjects();

    @ApiOperation("管理单位-单位职责新增")
    CommonResult saveOrUpdateUnit(UnitResponsibility entity);

    @ApiOperation("管理单位-单位职责查询")
    CommonResult queryUnit(String id,String unitType,String deptId);

    @ApiOperation("管理单位-单位职责删除")
    CommonResult delUnit(String id);

    @ApiOperation("管理单位-岗位职责新增")
    CommonResult saveOrUpdateJob(UnitJobResponsibility entity);

    @ApiOperation("管理单位-岗位职责查询")
    CommonResult queryJob(String id,String unitType,String jobType,String deptId);

    @ApiOperation("管理单位-岗位职责删除")
    CommonResult delJob(String id);

    @ApiOperation("管理单位-岗位设置新增")
    CommonResult saveOrUpdatePosition(UnitPositionSet entity);

    @ApiOperation("管理单位-岗位设置分页查询")
    CommonResult queryPositions(UnitPositionSetReqVo vo);

    @ApiOperation("管理单位-岗位设置单条查询")
    CommonResult queryPosition(UnitPositionSetReqVo vo);

    @ApiOperation("管理单位-岗位设置删除")
    CommonResult delPosition(String id);
}
