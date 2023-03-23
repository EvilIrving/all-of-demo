package com.ygkj.controller;

import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.auth.VO.request.PermissionAddReqVO;
import com.ygkj.auth.VO.request.PermissionPageReqVO;
import com.ygkj.auth.VO.request.PermissionUpdateReqVO;
import com.ygkj.auth.VO.request.SysPermissionPageQueryVo;
import com.ygkj.entity.SysPermission;
import com.ygkj.service.PermissionService;
import com.ygkj.service.RolePermissionService;
import com.ygkj.utils.DataResult;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.vo.resp.PermissionRespNode;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ClassName: PermissionController
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/20 14:05
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/20 14:05
 * @Version: 0.0.1
 */
@RequestMapping("/sys")
@RestController
@Api(tags = "组织模块-菜单权限管理")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @PostMapping("/permission")
    @ApiOperation(value = "新增菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "新增菜单权限")
    //@RequiresPermissions("sys:permission:add")
    public DataResult<SysPermission> addPermission(@RequestBody @Valid PermissionAddReqVO vo){
        DataResult<SysPermission> result=DataResult.success();
        result.setData(permissionService.addPermission(vo));
        return result;
    }
    @DeleteMapping("/permission/{id}")
    @ApiOperation(value = "删除菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "删除菜单权限")
    //@RequiresPermissions("sys:permission:deleted")
    public DataResult deleted(@PathVariable("id") String id){
        DataResult result=DataResult.success();
        permissionService.deleted(id);
        return result;
    }
    @PostMapping("/permissionUpdate")
    @ApiOperation(value = "更新菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "更新菜单权限")
    //@RequiresPermissions("sys:permission:update")
    public DataResult updatePermission(@RequestBody @Valid PermissionUpdateReqVO vo){
        DataResult result=DataResult.success();
        permissionService.updatePermission(vo);
        return result;
    }
    @GetMapping("/permission/{id}")
    @ApiOperation(value = "查询菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "查询菜单权限")
    //@RequiresPermissions("sys:permission:detail")
    public DataResult<SysPermission> detailInfo(@PathVariable("id") String id){
        DataResult<SysPermission> result=DataResult.success();
        result.setData(permissionService.detailInfo(id));
        return result;
    }

    @PostMapping("/permissions")
    @ApiOperation(value = "分页查询菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "分页查询菜单权限")
    //@RequiresPermissions("sys:permission:list")
    public DataResult<PageVO<SysPermission>> pageInfo(@RequestBody(required = false) PermissionPageReqVO vo){
        DataResult<PageVO<SysPermission>> result=DataResult.success();
        result.setData(permissionService.pageInfo(vo));
        return result;

    }
    @GetMapping("/permissions")
    @ApiOperation(value = "获取所有菜单权限接口")
    @LogAnnotation(title = "菜单权限管理",action = "获取所有菜单权限")
    //@RequiresPermissions("sys:permission:list")
    public DataResult<List<SysPermission>> getAllMenusPermission(){
        DataResult<List<SysPermission>> result=DataResult.success();
        result.setData(permissionService.selectAll());
        return result;
    }
    @GetMapping("/permission/tree")
    @ApiOperation(value = "获取所有权限树（只含目录和菜单）")
    @LogAnnotation(title = "菜单权限管理",action = "获取所有目录菜单树")
    //@RequiresPermissions(value = {"sys:permission:update","sys:permission:add"},logical = Logical.OR)
    public DataResult<List<PermissionRespNode>> getAllMenusPermissionTree(@RequestParam(required = false) String permissionId){
        DataResult<List<PermissionRespNode>> result=DataResult.success();
        result.setData(permissionService.selectAllMenuByTree(permissionId));
        return result;
    }

    @GetMapping("/permission/tree/all")
    @ApiOperation(value = "获取所有权限树（目录、菜单和按钮）")
//    @LogAnnotation(title = "菜单权限管理",action = "获取所有目录菜单树")
    //@RequiresPermissions(value = {"sys:role:update","sys:role:add"},logical = Logical.OR)
    public DataResult<List<PermissionRespNode>> getAllPermissionTree(){
        DataResult<List<PermissionRespNode>> result=DataResult.success();
        result.setData(permissionService.selectAllByTree());
        return result;
    }

    @GetMapping("/permission/getRolesAllPermissions")
    @ApiOperation(value = "获取当前角色对应的所有权限树（目录、菜单和按钮）")
    @LogAnnotation(title = "菜单权限管理",action = "获取当前角色对应的所有权限树")
    public CommonResult<List<PermissionRespNode>> getRolesAllPermissions(String roleId){
        return CommonResult.success(permissionService.getRolesAllPermissions(roleId));
    }

    @GetMapping("/permission/getPermissionsByUserId")
    @ApiOperation(value = "获取当前角色对应的所有权限")
    @LogAnnotation(title = "菜单权限管理",action = "获取当前角色对应的所有权限")
    public CommonResult getPermissionsByUserId(String userId,String system){
        return CommonResult.success(permissionService.getPermission(userId,system));
    }
    @GetMapping("/permission/getRolesPermissionList")
    @ApiOperation(value = "获取当前角色对应的所有权限（列表形式）")
    @LogAnnotation(title = "菜单权限管理",action = "获取当前角色对应的所有权限（列表形式）")
    public CommonResult getRolesPermissionList(String roleId){
        return CommonResult.success(permissionService.getRolesPermissionList(roleId));
    }

    @GetMapping("/permission/roles")
    @ApiOperation(value = "系统聚合角色")
    public CommonResult polymerizationRoles(){
        return CommonResult.success(permissionService.polymerizationRoles());
    }

    @PostMapping("/permission/jobs")
    @ApiOperation(value = "我的工作")
    public CommonResult currentUserJobs(SysPermissionPageQueryVo queryVo){
        return CommonResult.success(permissionService.currentUserJobs(queryVo));
    }
}
