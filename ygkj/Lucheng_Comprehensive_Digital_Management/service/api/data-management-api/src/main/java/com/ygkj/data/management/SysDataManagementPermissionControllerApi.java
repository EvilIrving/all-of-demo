package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.SysDataManagementPermissionAddVo;
import com.ygkj.data.management.vo.request.SysDataManagementRolePermissionBindVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Api(tags = "权限管理", value = "权限管理")
public interface SysDataManagementPermissionControllerApi {

    @ApiOperation("根据角色id获取权限树")
    CommonResult permissions(String roleId);

    @ApiOperation("新增权限")
    CommonResult add(@RequestBody List<SysDataManagementPermissionAddVo> list);

    @ApiOperation("修改权限")
    CommonResult update(SysDataManagementPermissionAddVo addVo);

    @ApiOperation("删除权限")
    CommonResult delete(String id);

    @ApiOperation("给角色绑定权限")
    CommonResult bindRolePermission(List<SysDataManagementRolePermissionBindVo> list);

    @ApiOperation("将数据表关联至角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "表id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, paramType = "query")
    })
    CommonResult bindTablePermission(String tableId, String roleId);
}
