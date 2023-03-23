package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.SysDataManagementRoleAddVo;
import com.ygkj.data.management.vo.request.SysDataManagementRoleQueryVo;
import com.ygkj.data.management.vo.request.SysDataManagementUserRoleAddVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "角色管理", value = "角色管理")
@Validated
public interface SysDataManagementRoleControllerApi {

    @ApiOperation("角色列表查询")
    CommonResult roles(SysDataManagementRoleQueryVo queryVo);

    @ApiOperation("角色新增")
    CommonResult add(SysDataManagementRoleAddVo addVo);

    @ApiOperation("角色修改")
    CommonResult update(SysDataManagementRoleAddVo addVo);

    @ApiOperation("角色删除")
    CommonResult delete(String id);

    @ApiOperation("获取所有审批流程头部角色")
    CommonResult heads(String deptId);

    @ApiOperation("用户角色关联")
    CommonResult bindUserRoleRelation(List<SysDataManagementUserRoleAddVo> list);

    @ApiOperation("字段对应的角色列表")
    @ApiImplicitParam(name = "columnId", paramType = "query", value = "字段id", dataType = "string")
    CommonResult selectCandidateColumnRoles(@NotBlank(message = "请指定一个字段！") String columnId);

    @ApiOperation("绑定字段到角色，支持批量")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleIds", value = "角色id，多个用逗号隔开", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "columnIds", value = "字段id，多个用逗号隔开", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "tableId", value = "表id", paramType = "query", dataType = "string")})
    CommonResult bindColumnPermissionToRole(@NotBlank(message = "请指定至少一个角色！") String roleIds,
                                            @NotBlank(message = "请指定至少一个字段！") String columnIds,
                                            @NotBlank(message = "请指定一张表！") String tableId);

    @ApiOperation("绑定数据行到角色，支持批量")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleIds", value = "角色id，多个用逗号隔开", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "primaryValues", value = "主键的值，多个用逗号隔开", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "tableId", value = "表id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "busType", value = "业务类型 BASIC 基础数据  REALTIME 实时数据 ", paramType = "query", dataType = "string")})
    CommonResult bindRowPermissionToRole(String roleIds, String primaryValues, String tableId, String busType);


    @ApiOperation("用户列表")
    @ApiImplicitParam(name = "username", paramType = "query", value = "用户名称", dataType = "string")
    CommonResult simpleUserList(String username);

    @ApiOperation("查询用户角色（包含全角色列表和用户拥有的角色）")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "busType", value = "业务类型 BASIC 基础数据(默认)  REALTIME 实时数据", paramType = "query", dataType = "string")})
    CommonResult candidateUserRoles(String userId, String roleName, String busType);


    @ApiOperation("询行及列的相关责任人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "primaryValues", value = "主键的值，多个用逗号隔开", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "tableId", value = "表id", paramType = "query", dataType = "string")})
    CommonResult getRowPermmisionAndDuty(String primaryValues, String tableId);
}
