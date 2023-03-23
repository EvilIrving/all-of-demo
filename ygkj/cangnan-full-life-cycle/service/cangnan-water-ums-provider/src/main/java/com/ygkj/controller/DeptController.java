package com.ygkj.controller;

import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.entity.SysDept;
import com.ygkj.entity.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.DeptService;
import com.ygkj.utils.DataResult;
import com.ygkj.vo.req.DeptAddReqVO;
import com.ygkj.vo.req.DeptPageReqVO;
import com.ygkj.vo.req.DeptUpdateReqVO;
import com.ygkj.vo.req.UserPageUserByDeptReqVO;
import com.ygkj.vo.resp.DeptRespNodeVO;
import com.ygkj.vo.resp.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * @ClassName: DeptController
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 11:37
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 11:37
 * @Version: 0.0.1
 */
@RequestMapping("/sys")
@RestController
@Api(tags = "组织模块-机构管理")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @PostMapping("/dept")
    @ApiOperation(value = "新增组织接口")
    @LogAnnotation(title = "机构管理",action = "新增组织")
    ////@RequiresPermissions("sys:dept:add")
    public DataResult<SysDept> addDept(@RequestBody @Valid DeptAddReqVO vo){
        DataResult<SysDept> result=DataResult.success();
        result.setData(deptService.addDept(vo));
        return result;
    }
    @DeleteMapping("/dept/{id}")
    @ApiOperation(value = "删除组织接口")
    @LogAnnotation(title = "机构管理",action = "删除组织")
    ////@RequiresPermissions("sys:dept:deleted")
    public DataResult deleted(@PathVariable("id") String id){
        deptService.deleted(id);
        return DataResult.success();
    }
    @PutMapping("/dept")
    @ApiOperation(value = "更新组织信息接口")
    @LogAnnotation(title = "机构管理",action = "更新组织信息")
    ////@RequiresPermissions("sys:dept:update")
    public DataResult updateDept(@RequestBody @Valid DeptUpdateReqVO vo){
        deptService.updateDept(vo);
        return DataResult.success();
    }
    @GetMapping("/dept/{id}")
    @ApiOperation(value = "查询组织详情接口")
    @LogAnnotation(title = "机构管理",action = "查询组织详情")
    ////@RequiresPermissions("sys:dept:detail")
    public DataResult<SysDept> detailInfo(@PathVariable("id") String id){
        DataResult<SysDept> result=DataResult.success();
        result.setData(deptService.detailInfo(id));
        return result;
    }
    @PostMapping("/depts")
    @ApiOperation(value = "分页获取组织信息接口")
    @LogAnnotation(title = "机构管理",action = "分页获取组织信息")
    ////@RequiresPermissions("sys:dept:list")
    public DataResult<PageVO<SysDept>> pageInfo(@RequestBody DeptPageReqVO vo){
        DataResult<PageVO<SysDept>> result=DataResult.success();
        result.setData(deptService.pageInfo(vo));
        return result;
    }
    @GetMapping("/dept/tree")
    @ApiOperation(value = "树型组织列表接口")
    @LogAnnotation(title = "机构管理",action = "树型组织列表")
    public List<SysDept> getTree(@RequestParam(required = false) String deptId){
        return deptService.deptTreeList(deptId);
    }

    @GetMapping("/dept/getDeptTree")
    @ApiOperation(value = "组织列表接口")
    @LogAnnotation(title = "机构管理",action = "组织列表")
    public List<DeptRespNodeVO> getDeptTree(@RequestParam(required = false) String deptId){
        return deptService.getDeptTree(deptId);
    }

    @PostMapping("/dept/users")
    @ApiOperation(value = "分页获取组织下所有用户接口")
    @LogAnnotation(title = "机构管理",action = "分页获取组织下所有用户")
    ////@RequiresPermissions("sys:user:list")
    public DataResult<PageVO<SysUser>> pageDeptUserInfos(@RequestBody @Valid UserPageUserByDeptReqVO vo){
        DataResult<PageVO<SysUser>> result=DataResult.success();
        result.setData(deptService.pageDeptUserInfo(vo));
        System.out.println("fsdfds");
        return result;
    }

    @GetMapping("/depts")
    @ApiOperation(value = "获取机构列表接口")
    @LogAnnotation(title = "机构管理",action = "获取所有组织机构")
    ////@RequiresPermissions("sys:dept:list")
    public DataResult<List<SysDept>> getDeptAll(){
        DataResult<List<SysDept>> result=DataResult.success();
        result.setData(deptService.selectAll());
        return result;
    }


    @ApiOperation(value = "当前用户所在部门")
    @GetMapping("/currentUserDepartment")
    public SysDept currentUserDepartment(){
        String departmentId = JwtTokenUtil.getCurrentUserDepartmentId().orElse("");
        if (StringUtils.isNotBlank(departmentId)){
           return deptService.detailInfo(departmentId);
        }
        return null;
    }
}
