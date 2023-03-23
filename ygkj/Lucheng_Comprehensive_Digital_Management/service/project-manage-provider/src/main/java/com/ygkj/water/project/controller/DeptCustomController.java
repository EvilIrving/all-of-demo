package com.ygkj.water.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.DeptCustom;
import com.ygkj.project.model.SysDept;
import com.ygkj.project.vo.request.SysDeptVo;
import com.ygkj.project.vo.response.DeptCustomVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.aop.annotation.LogAnnotation;
import com.ygkj.water.project.service.DeptCustomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wgf
 * @description 预警自定义部门表
 * @date 2021-10-21
 */
@Api(value = "预警自定义部门", tags = "预警自定义部门")
@RestController
@RequestMapping(value = "/deptCustom")
public class DeptCustomController {

    @Resource
    private DeptCustomService deptCustomService;

    /**
     * 新增
     * @author wgf
     * @date 2021/10/21
     **/
    @ApiOperation(value = "新增自定义部门")
    @PostMapping("/insert")
    @LogAnnotation(title = "风险预警-人员管理",action = "新增自定义部门")
    public CommonResult insert(DeptCustom deptCustom) {
        deptCustomService.insert(deptCustom);
        return CommonResult.success();
    }

    /**
     * 刪除
     * @author wgf
     * @date 2021/10/21
     **/
    @ApiOperation(value = "根据id删除自定义部门")
    @GetMapping("/delete")
    @LogAnnotation(title = "风险预警-人员管理",action = "删除自定义部门")
    public CommonResult delete(String id) {
        List<String> strings = deptCustomService.deptIds(id);
        if (deptCustomService.removeByIds(strings)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**s
     * 更新
     *
     * @author wgf
     * @date 2021/10/21
     **/
    @ApiOperation(value = "更新自定义部门")
    @PostMapping("/update")
    @LogAnnotation(title = "风险预警-人员管理",action = "更新自定义部门")
    public CommonResult update(DeptCustom deptCustom) {
        if (deptCustomService.updateById(deptCustom)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 自定义部门树查询
     */
    @ApiOperation(value = "自定义部门树查询")
    @PostMapping("/customTree")
    public CommonResult customTree() {
        //查询根目录
        DeptCustom deptCustom = deptCustomService.getOne(new QueryWrapper<DeptCustom>().eq("pid", "0"));
        List<DeptCustom> deptCustoms = deptCustomService.list();
        //递归查询目录树
        DeptCustomVo deptCustomVo = new DeptCustomVo();
        BeanUtils.copyProperties(deptCustom, deptCustomVo);
        DeptCustomVo deptCustomVos = customVoList(deptCustom, deptCustomVo, deptCustoms);
        return CommonResult.success(deptCustomVos);
    }

    private DeptCustomVo customVoList(DeptCustom deptCustom, DeptCustomVo deptCustomVo, List<DeptCustom> deptCustoms) {
        if (deptCustom != null) {
            //子集
            List<DeptCustomVo> customVos = new ArrayList<>();
            for (DeptCustom custom1 : deptCustoms) {
                if (custom1.getPid().equals(deptCustom.getId())) {
                    DeptCustomVo vo = new DeptCustomVo();
                    BeanUtils.copyProperties(custom1, vo);
                    customVoList(custom1, vo, deptCustoms);
                    customVos.add(vo);
                }
                if (customVos.size() > 0) {
                    deptCustomVo.setList(customVos);
                }
            }
        }
        return deptCustomVo;
    }

    @ApiOperation(value = "浙政钉部门树查询")
    @PostMapping("/zzdTree")
    public CommonResult zzdTree(String deptId) {
        //查询根目录 id 是 67011533 的 鹿城区农业农村局
        if (StringUtils.isBlank(deptId)){
            deptId = "67011533";
        }
        SysDept sysDept = deptCustomService.getZzdDeptById(deptId);
        //递归查询目录树
        SysDeptVo sysDeptVo = new SysDeptVo();
        SysDeptVo deptVo = sysDeptVoList(sysDept, sysDeptVo);
        return CommonResult.success(deptVo);
    }

    public SysDeptVo sysDeptVoList(SysDept sysDept, SysDeptVo sysDeptVo) {
        if (sysDept != null) {
            //子集
            List<SysDept> sysDepts = deptCustomService.getDeptList(sysDept.getId());
            BeanUtils.copyProperties(sysDept, sysDeptVo);
            List<SysDeptVo> sysDeptVos = new ArrayList<>();
            for (SysDept dept : sysDepts) {
                SysDeptVo deptVo = new SysDeptVo();
                BeanUtils.copyProperties(dept, deptVo);
                sysDeptVoList(dept,deptVo);
                sysDeptVos.add(deptVo);
            }
            if (sysDeptVos.size() > 0){
                sysDeptVo.setList(sysDeptVos);
            }
        }
        return sysDeptVo;
    }

    @ApiOperation(value = "部门列表查询")
    @PostMapping("/deptList")
    public CommonResult deptList(String deptIds) {
        if (StringUtils.isBlank(deptIds)){
            deptIds = "67011533,67038494";
        }
        List<SysDept> sysDepts = deptCustomService.getDeptList(deptIds);
        return CommonResult.success(sysDepts);
    }
}