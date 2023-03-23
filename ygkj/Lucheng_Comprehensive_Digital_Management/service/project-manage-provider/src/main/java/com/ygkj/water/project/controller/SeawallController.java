package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.gragh.model.AttSeawallBase;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.SeawallControllerApi;
import com.ygkj.project.model.CompanyBindingPrcd;
import com.ygkj.project.vo.request.SeawallQueryReqVo;
import com.ygkj.project.vo.request.SeawallWarnReqVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.service.ICompanyBindingPrcdService;
import com.ygkj.water.project.service.SeawallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author xq
 * @Description
 * @Date 2021/9/14
 */
@RestController
@RequestMapping("seawall")
@Api(tags = "海塘")
public class SeawallController implements SeawallControllerApi {

    @Autowired
    SeawallService seawallService;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private ICompanyBindingPrcdService iCompanyBindingPrcdService;

    @PostMapping("exportSeawall")
    @Override
    public void exportSeawall(HttpServletResponse response, SeawallQueryReqVo reqVo) {
        seawallService.exportSeawall(response, reqVo);
    }

    @GetMapping("pageSeawall")
    @Override
    public CommonResult pageSeawall(SeawallQueryReqVo reqVo, HttpServletRequest request) {
        String userId = JwtTokenUtil.getUserId(request.getHeader("authorization"));
        SysUser sysUser = userFeignClient.detailInfo(userId);
        if ("3".equals(sysUser.getUserType())){
           //查询绑定的海塘
            QueryWrapper<CompanyBindingPrcd> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("wyh_id", sysUser.getDeptId());
            CompanyBindingPrcd prcdServiceOne = iCompanyBindingPrcdService.getOne(queryWrapper);
            if (prcdServiceOne == null){
               return CommonResult.failed("物业化公司未绑定海塘数据");
            }
            String prcd = prcdServiceOne.getPrcd();
            reqVo.setCodes(Arrays.asList(prcd.split(",")));
        }
        return CommonResult.success(seawallService.pageSeawall(reqVo));
    }

    @GetMapping("stats4PageSeawall")
    @Override
    public CommonResult stats4PageSeawall(SeawallQueryReqVo reqVo) {
        return CommonResult.success(seawallService.stats4PageSeawall(reqVo));
    }

    @GetMapping("seawallHeatMap")
    @Override
    public CommonResult seawallHeatMap() {
        return CommonResult.success(seawallService.seawallHeatMap());
    }

    @GetMapping("seawallUnitCWindow")
    @Override
    public CommonResult seawallUnitCWindow(SeawallWarnReqVo reqVo) {
        return CommonResult.success(seawallService.seawallUnitCWindow(reqVo.isForceNewestWarnState(), reqVo.getDataTime()));
    }

    @GetMapping("seawallUnitWarnCard")
    @Override
    public CommonResult seawallUnitWarnCard(SeawallWarnReqVo reqVo) {
        return CommonResult.success(seawallService.seawallUnitWarnCard(reqVo.getWarnType(), reqVo.isForceNewestWarnState(), reqVo.getDataTime()));
    }

    @GetMapping("warnSeawallList")
    @Override
    public CommonResult warnSeawallList(SeawallWarnReqVo reqVo) {
        return CommonResult.success(seawallService.warnSeawallList(reqVo.getWarnType(), reqVo.isForceNewestWarnState(), reqVo.getDataTime()));
    }

    @PostMapping("exportWarnSeawallList")
    @Override
    public void exportWarnSeawallList(HttpServletResponse response, @RequestBody SeawallWarnReqVo reqVo) {
        seawallService.exportWarnSeawallList(response, reqVo.getWarnType(), reqVo.isForceNewestWarnState(), reqVo.getDataTime());
    }

    @GetMapping("seawallAqjd")
    @Override
    public CommonResult seawallAqjd() {
        return CommonResult.success(seawallService.seawallAqjd());
    }

    @GetMapping("seawallHtdj")
    @Override
    public CommonResult seawallHtdj() {
        return CommonResult.success(seawallService.seawallHtdj());
    }

    @GetMapping("listSt/{seawallCode}")
    @ApiOperation("关联测站")
    public CommonResult listSt(@PathVariable("seawallCode") String seawallCode) {
        return CommonResult.success(seawallService.listSt(seawallCode));
    }

    @PostMapping("forecastRainfall")
    @ApiOperation("预报降雨")
    public CommonResult forecastRainfall(String seawallCode) {
        return CommonResult.success(seawallService.forecastRainfall(seawallCode));
    }

    @PostMapping("editWarningLevel")
    @ApiOperation("修改海塘警戒水位")
    public CommonResult editWarningLevel(AttSeawallBase base) {
        return CommonResult.success(seawallService.updateById(base));
    }
}
