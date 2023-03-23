package com.ygkj.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.aop.annotation.LogAnnotation;
import com.ygkj.entity.SysRes;
import com.ygkj.entity.SysResTreeVo;
import com.ygkj.service.ISysResService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author  wgf
 * @date  2021/1/6 11:44
 */
@Api(value = "系统资源",tags ="系统资源")
@RestController
@RequestMapping("/sys-res")
public class SysResController {

    @Autowired
    ISysResService sysResService;

    @PostMapping("/list")
    @ApiOperation(value = "权限菜单根据pid查询子集菜单列表", notes = "权限菜单根据pid查询子集菜单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pId", value = "父级id", paramType = "query")
    })
    public Object listJson(SysRes entity) {
        return CommonResult.success(sysResService.listPage(entity));
    }

    @ApiOperation(value="新增修改权限菜单",notes="新增修改权限菜单")
    @PostMapping("saveOrUpdate")
    @LogAnnotation(title = "菜单管理", action = "新增修改权限菜单")
    public Object saveOrUpdate(SysRes entity) {
        if (StringUtils.isNotBlank(entity.getId())) {
            if (sysResService.updateById(entity)) {
                return CommonResult.success();
            }
        } else {
            //新增
            entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            if (entity.getFlag() == null){
                entity.setFlag(1);
            }
            if (sysResService.save(entity)) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed("保存或修改失败");
    }


    @ApiOperation(value="权限菜单详情",notes="权限菜单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",paramType = "query")
    })
    @GetMapping("detail")
    public Object detail(SysRes entity) {
        return CommonResult.success(sysResService.getById(entity.getId()));
    }

    @ApiOperation(value="删除权限菜单",notes="删除权限菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id",paramType = "query")
    })
    @GetMapping("del")
    @LogAnnotation(title = "菜单管理", action = "删除权限菜单")
    public Object del(SysRes entity) {
        //查询出所有的子集也全部删除
        ArrayList<String> list = new ArrayList<>();
        list.add(entity.getId());
        List<String> resIdList = getResIdList(entity,list);
        SysRes res = new SysRes();
        res.setDelFlag(1);
        UpdateWrapper<SysRes> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", resIdList);
        if (sysResService.update(res, updateWrapper)) {
            return CommonResult.success();
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation(value="获取资源树",notes="获取资源树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flag",value = "1.登陆后菜单，2内部查看菜单",paramType = "query")
    })
    @PostMapping("sysResTree")
    public Object sysResTree(Integer flag) {
        //查询第一层的树，然后递归拿出所有的子集
        if (flag == null) {
            QueryWrapper<SysRes> qw = new QueryWrapper<>();
            qw.eq("P_ID","0");
            qw.eq("DEL_FLAG",0);
            qw.eq("FLAG", 1);
            qw.orderByAsc("SORT");
            List<SysRes> list = sysResService.list(qw);
            List<SysResTreeVo> sysResTreeVos = new ArrayList<>();
            List<SysResTreeVo> resTreeList = getResTreeList(sysResTreeVos, list);
            QueryWrapper<SysRes> qw1 = new QueryWrapper<>();
            qw1.eq("P_ID","0");
            qw1.eq("DEL_FLAG",0);
            qw1.eq("FLAG", 2);
            qw1.orderByAsc("SORT");
            List<SysRes> list1 = sysResService.list(qw1);
            List<SysResTreeVo> sysResTreeVos1 = new ArrayList<>();
            List<SysResTreeVo> resTreeList1 = getResTreeList(sysResTreeVos1, list1);
            HashMap<Object, Object> map = new HashMap<>();
            map.put("wbTree", resTreeList);
            map.put("nbTree", resTreeList1);
            return CommonResult.success(map);
        } else {
            QueryWrapper<SysRes> qw = new QueryWrapper<>();
            qw.eq("P_ID","0");
            qw.eq("DEL_FLAG",0);
            qw.eq("FLAG", flag);
            qw.orderByAsc("SORT");
            List<SysRes> list = sysResService.list(qw);
            List<SysResTreeVo> sysResTreeVos = new ArrayList<>();
            List<SysResTreeVo> resTreeList = getResTreeList(sysResTreeVos, list);
            return CommonResult.success(resTreeList);
        }
    }


    /**
     * 递归拿出传入id这条数据下面的所有的子集的id
     * @param entity
     * @param list
     * @return
     */
    private List<String> getResIdList(SysRes entity, List<String> list) {
        List<SysRes> pidSysRes = sysResService.getPidSysRes(entity);
        if (pidSysRes != null && pidSysRes.size() > 0) {
            for (SysRes pidSysRe : pidSysRes) {
                list.add(pidSysRe.getId());
                getResIdList(pidSysRe, list);
            }
        }
        return list;
    }

    private List<SysResTreeVo> getResTreeList(List<SysResTreeVo> list,  List<SysRes> sysResList) {
        if (sysResList != null && sysResList.size() > 0) {
            for (SysRes sysRes : sysResList) {
                SysResTreeVo sysResTreeVo = new SysResTreeVo();
                sysResTreeVo.setSysRes(sysRes);
                //查询子集
                List<SysRes> pidSysRes = sysResService.getPidSysRes(sysRes);
                List<SysResTreeVo> sysResTreeVos = new ArrayList<>();
                sysResTreeVo.setSubset(sysResTreeVos);
                list.add(sysResTreeVo);
                if (pidSysRes != null && pidSysRes.size() > 0) {
                    getResTreeList(sysResTreeVos, pidSysRes);
                }
            }
        }
        return list;
    }

    @PostMapping("/permissionList")
    @ApiOperation(value = "权限功能按钮的列表", notes = "权限功能按钮的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色的id", paramType = "query")
    })
    public Object permissionList(@RequestParam String id) {
        List<String> list = sysResService.permissionList(id, "3");
        return CommonResult.success(list);
    }
}
