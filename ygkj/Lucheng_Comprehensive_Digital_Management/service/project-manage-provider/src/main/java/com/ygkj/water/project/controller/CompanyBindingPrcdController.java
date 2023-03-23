package com.ygkj.water.project.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.project.model.CompanyBindingPrcd;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.ICompanyBindingPrcdService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 物业化公司绑定工程表 前端控制器
 * </p>
 *
 * @author wgf
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/companyBindingPrcd")
public class CompanyBindingPrcdController {

    @Autowired
    private ICompanyBindingPrcdService iCompanyBindingPrcdService;

    @ApiOperation(value = "绑定物业化公司的工程")
    @PostMapping("/save")
    public CommonResult save(CompanyBindingPrcd companyBindingPrcd) {
        String wyhId = companyBindingPrcd.getWyhId();
        String prcd = companyBindingPrcd.getPrcd();
        if (StringUtils.isBlank(wyhId)){
            return CommonResult.failed("缺少必传参数");
        }
        //查询数据是否存在
        QueryWrapper<CompanyBindingPrcd> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("wyh_id", wyhId);
        CompanyBindingPrcd prcdServiceOne = iCompanyBindingPrcdService.getOne(queryWrapper);
        if (prcdServiceOne == null){
            //直接新增
            companyBindingPrcd.setId(IdWorker.get32UUID());
            companyBindingPrcd.setCreateTime(new Date());
            iCompanyBindingPrcdService.save(companyBindingPrcd);
            return CommonResult.success();
        } else {
            if (StringUtils.isBlank(prcd)){
                //删除数据
                iCompanyBindingPrcdService.removeById(prcdServiceOne.getId());
                return CommonResult.success();
            } else {
                //更新
                companyBindingPrcd.setId(prcdServiceOne.getId());
                iCompanyBindingPrcdService.updateById(companyBindingPrcd);
                return CommonResult.success();
            }
        }
    }

    @ApiOperation(value = "查询物业化公司对应类型的工程数据")
    @PostMapping("/getBindingPrcd")
    public CommonResult getBindingPrcd(CompanyBindingPrcd companyBindingPrcd) {
        String wyhId = companyBindingPrcd.getWyhId();
        if ( StringUtils.isBlank(wyhId)){
            return CommonResult.failed("缺少必传参数");
        }
        QueryWrapper<CompanyBindingPrcd> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("wyh_id", wyhId);
        CompanyBindingPrcd prcdServiceOne = iCompanyBindingPrcdService.getOne(queryWrapper);
        return CommonResult.success(prcdServiceOne);
    }

    @ApiOperation(value = "查询所有工程")
    @PostMapping("/getAllProject")
    public CommonResult getAllProject(String name,String type) {
        List<JSONObject> list = iCompanyBindingPrcdService.getAllProject(name,type);
        return CommonResult.success(list);
    }
}
