package com.ygkj.water.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.CompanyBindingPrcd;
import com.ygkj.project.model.ProfessionalCompany;
import com.ygkj.project.vo.request.WyhUserVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.aop.annotation.LogAnnotation;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.service.ICompanyBindingPrcdService;
import com.ygkj.water.project.service.IProfessionalCompanyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 物业化公司 前端控制器
 * </p>
 *
 * @author wgf
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/professionalCompany")
public class ProfessionalCompanyController {

    @Autowired
    private IProfessionalCompanyService iProfessionalCompanyService;
    @Autowired
    private ICompanyBindingPrcdService iCompanyBindingPrcdService;
    @Resource
    private UserFeignClient userFeignClient;

    @ApiOperation(value = "新增或者修改物业化公司")
    @PostMapping("/saveOrUpdate")
    @LogAnnotation(title = "物业管理",action = "新增或者修改物业化公司")
    public CommonResult saveOrUpdate(ProfessionalCompany professionalCompany) {
        if (StringUtils.isBlank(professionalCompany.getId())){
            professionalCompany.setId(IdWorker.get32UUID());
            professionalCompany.setCreateTime(new Date());
            if(iProfessionalCompanyService.save(professionalCompany)){
                return CommonResult.success();
            }
        } else {
            iProfessionalCompanyService.updateById(professionalCompany);
            return CommonResult.success();
        }
        return CommonResult.failed("新增或者修改物业化公司失败");
    }

    /**
     * 刪除
     * @author wgf
     * @date 2021/10/21
     **/
    @ApiOperation(value = "根据id删除物业化公司")
    @Transactional
    @GetMapping("/delete")
    @LogAnnotation(title = "物业管理",action = "删除物业化公司")
    public CommonResult delete(String id) {
        if (iProfessionalCompanyService.removeById(id)) {
            //删除对应公司的用户，删除对应绑定的工程
            QueryWrapper<CompanyBindingPrcd> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("wyh_id", id);
            iCompanyBindingPrcdService.remove(queryWrapper);
            userFeignClient.delWyhPerson(id);
            return CommonResult.success();
        } else {
            return CommonResult.failed("删除失败");
        }
    }

    @ApiOperation(value = "查询物业化公司列表")
    @PostMapping("/list")
    public CommonResult list(ProfessionalCompany professionalCompany){
        String userId = JwtTokenUtil.getCurrentUserId().orElse("");
        SysUser user = userFeignClient.detailInfo(userId);
        if (!ObjectUtils.isEmpty(user)){
            if ("3".equals(user.getUserType())){
                professionalCompany.setId(user.getDeptId());
            }
        }
        professionalCompany.page();
        QueryWrapper<ProfessionalCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(professionalCompany.getCompanyName()),"company_name",professionalCompany.getCompanyName());
        queryWrapper.eq(StringUtils.isNotBlank(professionalCompany.getId()),"id",professionalCompany.getId());
        queryWrapper.orderByDesc("create_time");
        return CommonResult.success(PageUtils.getPageVO(iProfessionalCompanyService.list(queryWrapper)));
    }

    @ApiOperation(value = "通过工程查询物业化人员")
    @PostMapping("/getWyhUserByProject")
    public CommonResult getWyhUserByProject(String projectCode){
        List<WyhUserVo> wyhUserVoList = new ArrayList<>();
        QueryWrapper<CompanyBindingPrcd> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("prcd", projectCode);
        List<CompanyBindingPrcd> prcdServiceOne = iCompanyBindingPrcdService.list(queryWrapper);
        if (CollectionUtils.isNotBlank(prcdServiceOne)){
            List<String> collect = prcdServiceOne.stream().map(CompanyBindingPrcd::getWyhId).collect(Collectors.toList());
            String wyhIds = String.join(",", collect);
            WyhUserVo wyhUserVo = new WyhUserVo();
            wyhUserVo.setDeptId(wyhIds);
            wyhUserVoList = iProfessionalCompanyService.getWyhUserByDeptId(wyhUserVo);
        }

        return CommonResult.success(wyhUserVoList);
    }

    @ApiOperation(value = "查询物业化公司和水管单位列表")
    @PostMapping("/getWyhAndWaterManageList")
    public CommonResult getWyhAndWaterManageList(String key){

        QueryWrapper<ProfessionalCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(key),"company_name",key);
        queryWrapper.orderByDesc("create_time");
        List<ProfessionalCompany> list = iProfessionalCompanyService.list(queryWrapper);
        List<ProfessionalCompany> depts = iProfessionalCompanyService.getWaterManageDeptList(key);
        list.addAll(depts);
        return CommonResult.success(list);
    }



}
