package com.ygkj.water.project.controller;

import com.ygkj.project.ProjCheckControllerApi;
import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.BhLgMpAfReqVo;
import com.ygkj.project.vo.request.BhLgMpSpReqVo;
import com.ygkj.project.vo.request.ProjCheckReqVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.LcProjCheckService;
import com.ygkj.water.project.service.ProjCheckService;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-26 15:20
 * @description
 */
@Api(tags = "工程检查")
@RestController
@RequestMapping("/projCheck")
public class ProjCheckController implements ProjCheckControllerApi {

    @Resource
    private ProjCheckService projCheckService;

    @Resource
    private LcProjCheckService lcProjCheckService;


    @Override
    @PostMapping("/bhLgMpFcList")
    public CommonResult bhLgMpFcList(ProjCheckReqVo reqVo) {
        return CommonResult.success(projCheckService.bhLgMpFcList(reqVo));
    }

    @Override
    @GetMapping("/exportLgMpFc")
    public void exportLgMpFc(ProjCheckReqVo reqVo, HttpServletResponse response) {
        projCheckService.exportLgMpFc(reqVo,response);
    }

    @Override
    @PostMapping("/saveOrUpdateBhLgMpFc")
    public CommonResult saveOrUpdateBhLgMpFc(BhLgMpFc bhLgMpFc) {
        if (bhLgMpFc.getSourceType() != null && bhLgMpFc.getSourceType() == 1){
            return CommonResult.failed("标化平台数据无法更新或者新增");
        }
        Integer flag;
        try {
            if (StringUtils.isEmpty(bhLgMpFc.getId())){
                flag = projCheckService.insertBhLgMpFc(bhLgMpFc);
            }else {
                flag = projCheckService.updateBhLgMpFc(bhLgMpFc);
            }
            if (flag > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }

    @Override
    @GetMapping("/deleteBhLgMpFc")
    public CommonResult deleteBhLgMpFc(String id) {

        try {
            if (projCheckService.deleteBhLgMpFc(id) > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }

    @Override
    @PostMapping("/dailyCheckList")
    public CommonResult dailyCheckList(ProjCheckReqVo reqVo) {
        return CommonResult.success(projCheckService.dailyCheckList(reqVo));
    }

    @Override
    @GetMapping("/checkTrack")
    public CommonResult checkTrack(String id) {
        return CommonResult.success(projCheckService.checkTrack(id));
    }

    @Override
    @GetMapping("/exportDailyCheck")
    public void exportDailyCheckList(ProjCheckReqVo reqVo, HttpServletResponse response) {
        projCheckService.exportDailyCheck(reqVo,response);
    }

    @Override
    @PostMapping("/bhLgMpAcList")
    public CommonResult bhLgMpAcList(ProjCheckReqVo reqVo) {
        return CommonResult.success(projCheckService.bhLgMpAcList(reqVo));
    }

    @Override
    @PostMapping("/saveOrUpdateBhLgMpAc")
    public CommonResult saveOrUpdateBhLgMpAc(BhLgMpAc bhLgMpAc) {
        if (bhLgMpAc.getSourceType() != null && bhLgMpAc.getSourceType() == 1){
            return CommonResult.failed("标化平台数据无法更新或者新增");
        }
        Integer flag;
        try {
            if (StringUtils.isEmpty(bhLgMpAc.getId())){
                flag = projCheckService.insertBhLgMpAc(bhLgMpAc);
            }else {
                flag = projCheckService.updateBhLgMpAc(bhLgMpAc);
            }
            if (flag > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }

    @Override
    @GetMapping("/deleteBhLgMpAc")
    public CommonResult deleteBhLgMpAc(String id) {
        try {
            if (projCheckService.deleteBhLgMpAc(id) > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }


    @Override
    @PostMapping("/specialCheckList")
    public CommonResult specialCheckList(ProjCheckReqVo reqVo) {
        return CommonResult.success(projCheckService.specialCheckList(reqVo));
    }

    @Override
    @PostMapping("/saveOrUpdateSpecialCheck")
    public CommonResult saveOrUpdateSpecialCheck(BhLgPsSpecialCheck check) {
        if (check.getSourceType() != null && check.getSourceType() == 1){
            return CommonResult.failed("标化平台数据无法更新或者新增");
        }
        Integer flag;
        try {
            if (StringUtils.isEmpty(check.getId())){
                flag = projCheckService.insertSpecialCheck(check);
            }else {
                flag = projCheckService.updateSpecialCheck(check);
            }
            if (flag > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }

    @Override
    @GetMapping("/deleteSpecialCheck")
    public CommonResult deleteSpecialCheck(String id) {
        try {
            if (projCheckService.deleteSpecialCheck(id) > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }


    @Override
    @PostMapping("/mpAfList")
    public CommonResult mpAfList(ProjCheckReqVo reqVo) {
        return CommonResult.success(lcProjCheckService.mpAfList(reqVo));
    }

    @Override
    @PostMapping("/mpAfSaveOrUpdate")
    public CommonResult mpAfSaveOrUpdate(BhLgMpAfReqVo bhLgMpAf) {

        Integer flag;
        try {
            if (StringUtils.isEmpty(bhLgMpAf.getId())){
                flag = lcProjCheckService.mpAfInsert(bhLgMpAf);
            }else {
                flag = lcProjCheckService.mpAfUpdate(bhLgMpAf);
            }
            if (flag > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }

    @Override
    @GetMapping("/id")
    public CommonResult mpAfDelete(String id) {

        try {
            if (lcProjCheckService.mpAfDelete(id) > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }

    @Override
    @PostMapping("/mpSpList")
    public CommonResult mpSpList(ProjCheckReqVo reqVo) {
        return CommonResult.success(lcProjCheckService.mpSpList(reqVo));
    }

    @Override
    @PostMapping("/mpSpSaveOrUpdate")
    public CommonResult mpSpSaveOrUpdate(BhLgMpSpReqVo bhLgMpSp) {
        Integer flag;
        try {
            if (StringUtils.isEmpty(bhLgMpSp.getId())){
                flag = lcProjCheckService.mpSpInsert(bhLgMpSp);
            }else {
                flag = lcProjCheckService.mpSpUpdate(bhLgMpSp);
            }
            if (flag > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }

    @Override
    @GetMapping("/mpSpDelete")
    public CommonResult mpSpDelete(String id) {
        try {
            if (lcProjCheckService.mpSpDelete(id) > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }

    @Override
    @GetMapping("/exportBhLgMpAc")
    public void exportBhLgMpAcList(ProjCheckReqVo reqVo, HttpServletResponse response) {
        projCheckService.exportBhLgMpAc(reqVo,response);
    }

    @Override
    @GetMapping("/exportSpecialCheck")
    public void exportSpecialCheck(ProjCheckReqVo reqVo, HttpServletResponse response) {
        projCheckService.exportSpecialCheck(reqVo,response);
    }

    @Override
    @GetMapping("/mpAfExport")
    public void mpAfExport(ProjCheckReqVo reqVo, HttpServletResponse response) {
        lcProjCheckService.mpAfExport(reqVo,response);
    }

    @Override
    @GetMapping("/mpSpExport")
    public void mpSpExport(ProjCheckReqVo reqVo, HttpServletResponse response) {
        lcProjCheckService.mpSpExport(reqVo,response);
    }
}
