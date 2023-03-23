package com.ygkj.controller;

import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.constants.Constant;
import com.ygkj.entity.SysRole;
import com.ygkj.service.RoleService;
import com.ygkj.utils.DataResult;
import com.ygkj.vo.req.RoleAddReqVO;
import com.ygkj.vo.req.RolePageReqVO;
import com.ygkj.vo.req.RoleUpdateReqVO;
import com.ygkj.vo.resp.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;


/**
 * @ClassName: RoleController
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 11:37
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 11:37
 * @Version: 0.0.1
 */
@RequestMapping("/sys")
@RestController
@Api(tags = "组织模块-角色管理")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/role")
    @ApiOperation(value = "新增角色接口")
    @LogAnnotation(title = "角色管理",action = "新增角色")
    public DataResult<SysRole> addRole(@RequestBody @Valid RoleAddReqVO vo){
        DataResult<SysRole> result=DataResult.success();
        result.setData(roleService.addRole(vo));
        return result;
    }
    @DeleteMapping("/role/{id}")
    @ApiOperation(value = "删除角色接口")
    @LogAnnotation(title = "角色管理",action = "删除角色")
    public DataResult deleted(@PathVariable("id") String id){
        roleService.deletedRole(id);
        return DataResult.success();
    }
    @PutMapping("/role")
    @ApiOperation(value = "更新角色信息接口")
    @LogAnnotation(title = "角色管理",action = "更新角色信息")
    public DataResult updateDept(@RequestBody @Valid RoleUpdateReqVO vo, HttpServletRequest request){
        roleService.updateRole(vo,request.getHeader(Constant.ACCESS_TOKEN));
        return DataResult.success();
    }
    @GetMapping("/role/{id}")
    @ApiOperation(value = "查询角色详情接口")
    @LogAnnotation(title = "角色管理",action = "查询角色详情")
    public DataResult<SysRole> detailInfo(@PathVariable("id") String id){
        DataResult<SysRole> result=DataResult.success();
        result.setData(roleService.detailInfo(id));
        return result;
    }
    @PostMapping("/roles")
    @ApiOperation(value = "分页获取角色信息接口")
    @LogAnnotation(title = "角色管理",action = "分页获取角色信息")
    public DataResult<PageVO<SysRole>> pageInfo(@RequestBody(required = false) RolePageReqVO vo){
        vo = Optional.ofNullable(vo).orElse(new RolePageReqVO());
        DataResult<PageVO<SysRole>> result=DataResult.success();
        result.setData(roleService.pageInfo(vo));
        return result;
    }

}
