package com.ygkj.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.entity.*;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.ISysJobResService;
import com.ygkj.service.ISysJobService;
import com.ygkj.service.ISysResService;
import com.ygkj.service.UserService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wgf
 * @since 2021-01-06
 */
@Api(value = "系统角色",tags ="系统角色")
@RestController
@RequestMapping("/sys-job")
public class SysJobController {

    @Autowired
    private ISysJobService sysJobService;
    @Autowired
    private ISysResService sysResService;
    @Autowired
    private ISysJobResService sysJobResService;
    @Autowired
    private UserService userService;

    @PostMapping("/list")
    @ApiOperation(value = "查询所有角色", notes = "查询所有角色")
    public Object listJson(String id) {
        String userName = JwtTokenUtil.getCurrentUserName().orElse("");
        QueryWrapper<SysJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(id),"ID",id);
        if ("admin".equals(userName)){
            return CommonResult.success(sysJobService.list(queryWrapper));
        }else {
            queryWrapper.ne("ID","admin");
            return CommonResult.success(sysJobService.list(queryWrapper));
        }
    }

    @ApiOperation(value = "新增修改角色", notes = "新增修改角色")
    @PostMapping("saveOrUpdate")
    public Object saveOrUpdate(SysJob entity) {
        if (StringUtils.isNotBlank(entity.getId())) {
            //更新
            sysJobService.updateById(entity);
            return CommonResult.success();

        } else {
            entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            entity.setCreateTime(new Date());

            QueryWrapper<SysJob> jobWrapper = new QueryWrapper<>();
            jobWrapper.like("JOB_NAME","默认管理员");
            SysJob sysJob = sysJobService.getOne(jobWrapper);
            if (ObjectUtils.isNotEmpty(sysJob)){
                return CommonResult.failed("已存在默认管理员");
            }
            //添加角色
            if (sysJobService.save(entity)) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed("保存或修改失败");
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query")
    })
    @GetMapping("del")
    @Transactional(rollbackFor = Exception.class)
    public Object del(SysJob entity) {
        try{
            if (sysJobService.removeById(entity.getId())) {

                //同时删除绑定的角色资源数据
                sysJobService.removeJobResAll(entity.getId());
                //同时删除用户中绑定该角色的角色id
                userService.removeJob(entity.getId());
                return CommonResult.success();
            }
        }catch (Exception e){
        }
        return CommonResult.failed("删除失败");
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据id查询角色", notes = "根据id查询角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query")
    })
    public Object getById(@RequestParam String id) {
        return CommonResult.success(sysJobService.getById(id));
    }


    @ApiOperation(value = "绑定角色与资源的关系", notes = "绑定角色与资源的关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wbIds", value = "wbIds 外部的资源id集合", paramType = "query"),
            @ApiImplicitParam(name = "nbIds", value = "nbIds 内部的资源id集合", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "query"),
    })
    @PostMapping("bindResJob")
    @Transactional(rollbackFor = Exception.class)
    public Object bindResJob(@RequestParam("id") String id, @RequestParam("wbIds") String wbIds, @RequestParam("nbIds") String nbIds) {
        //清除原先的绑定关系，再次进行绑定
        ArrayList<SysJobRes> sysJobRes = new ArrayList<>();
        if (StringUtils.isNotBlank(wbIds)) {
            String[] split = wbIds.split(",");
            if (split.length > 0) {
                sysJobService.removeJobRes(id, 1);
                for (String resId : split) {
                    SysJobRes entity = new SysJobRes();
                    entity.setId(IdWorker.get32UUID());
                    entity.setJobId(id);
                    entity.setResId(resId);
                    entity.setFlag(1);
                    sysJobRes.add(entity);
                }
            }
        }else {
            sysJobService.removeJobRes(id, 1);
        }
        if (StringUtils.isNotBlank(nbIds)) {
            String[] split1 = nbIds.split(",");
            if (split1.length > 0) {
                sysJobService.removeJobRes(id, 2);
                for (String resId : split1) {
                    SysJobRes entity = new SysJobRes();
                    entity.setId(IdWorker.get32UUID());
                    entity.setJobId(id);
                    entity.setResId(resId);
                    entity.setFlag(2);
                    sysJobRes.add(entity);
                }
            }
        }else {
            sysJobService.removeJobRes(id, 2);
        }
        if (sysJobRes.size() > 0) {
            if (sysJobResService.saveBatch(sysJobRes)) {
                return CommonResult.success();
            } else {
                return CommonResult.failed("角色绑定资源失败");
            }
        } else {
            return CommonResult.failed("没有需要更新绑定的资源");
        }
    }

    @PostMapping("/jobResTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query"),
    })
    @ApiOperation(value = "角色已经绑定的资源树", notes = "角色已经绑定的资源树")
    public Object jobResTree(String id) {

        //查询第一层flag是1的已绑定的树，然后递归拿出所有的已绑定的子集
        List<SysRes> list = sysResService.getBandingList(id, "0", 1);
        //查询出绑定的资源id
        List<String> jobRes = sysJobService.getJobResId(id);
        List<SysJobResTree> tree = new ArrayList<>();
        List<SysJobResTree> resTreeList = getResJobTreeList(tree, list, jobRes);

        List<SysRes> list1 = sysResService.getBandingList(id, "0", 2);
        List<SysJobResTree> tree1 = new ArrayList<>();
        List<SysJobResTree> resTreeList1 = getResJobTreeList(tree1, list1, jobRes);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("wbTree", resTreeList);
        map.put("nbTree", resTreeList1);
        return CommonResult.success(map);
    }


    /**
     * 递归形式获取树形资源绑定数
     *
     * @param list       返回格式
     * @param sysResList 所有的资源
     * @param jobRes     角色绑定的资源
     * @return
     */
    private List<SysJobResTree> getResJobTreeList(List<SysJobResTree> list, List<SysRes> sysResList, List<String> jobRes) {
        if (sysResList != null && sysResList.size() > 0) {
            for (SysRes sysRes : sysResList) {
                SysJobResTree sysJobResTree = new SysJobResTree();
                sysJobResTree.setId(sysRes.getId());
                sysJobResTree.setPId(sysRes.getPId());
                sysJobResTree.setRemark(sysRes.getRemark());
                sysJobResTree.setResIcon(sysRes.getResIcon());
                sysJobResTree.setResName(sysRes.getResName());
                sysJobResTree.setResType(sysRes.getResType());
                sysJobResTree.setResUrl(sysRes.getResUrl());
                sysJobResTree.setComponent(sysRes.getComponent());
                sysJobResTree.setName(sysRes.getName());
                sysJobResTree.setHideSecond(sysRes.getHideSecond());
                sysJobResTree.setRedirect(sysRes.getRedirect());
                if (jobRes.contains(sysRes.getId())) {
                    //查询子集
                    List<SysRes> pidSysRes = sysResService.getPidSysRes(sysRes);
                    List<SysJobResTree> sysJobResTreeVos = new ArrayList<>();
                    sysJobResTree.setSysJobResTrees(sysJobResTreeVos);
                    list.add(sysJobResTree);
                    if (pidSysRes != null && pidSysRes.size() > 0) {
                        getResJobTreeList(sysJobResTreeVos, pidSysRes, jobRes);
                    }
                }
            }
        }
        return list;
    }

    @PostMapping("/jobResIds")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query")
    })
    @ApiOperation(value = "查询角色绑定的资源id，如果绑定的资源有子集id，该id要去除（前端回显需要这个格式）", notes = "查询角色绑定的资源id，如果绑定的资源有子集id，该id要去除（前端回显需要这个格式）")
    public Object jobResIds(String id) {
        //查询绑定的资源id有下面绑定的子节点的资源的id去除
        List<String> jobRes = sysJobService.getBangdingJobResId(id);
        return CommonResult.success(jobRes);
    }
}
