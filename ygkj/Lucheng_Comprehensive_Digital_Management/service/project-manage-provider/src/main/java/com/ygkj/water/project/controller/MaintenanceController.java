package com.ygkj.water.project.controller;

import com.ygkj.project.MaintenanceControllerApi;
import com.ygkj.project.vo.request.BhLgMfdReqVo;
import com.ygkj.project.vo.request.MtDmDailyTaskReqVo;
import com.ygkj.project.vo.request.MtDmPlanReqVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.BhLgMfdService;
import com.ygkj.water.project.service.MtDailyTaskService;
import com.ygkj.water.project.service.MtDmPlanService;
import io.swagger.annotations.Api;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-18 16:56
 * @description 维修养护接口
 */
@Api(tags = "物业管理事项-维修养护")
@RestController
@RequestMapping("/maintenance")
public class MaintenanceController implements MaintenanceControllerApi {

    @Resource
    private MtDmPlanService mtDmPlanService;
    @Resource
    private MtDailyTaskService mtDailyTaskService;
    @Resource
    private BhLgMfdService bhLgMfdService;

    @Override
    @PostMapping("/saveOrUpdateMtDmPlan")
    public CommonResult saveOrUpdateMtDmPlan(MtDmPlanReqVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            mtDmPlanService.insert(vo);
            return CommonResult.success("保存成功!");
        } else {
            mtDmPlanService.update(vo);
            return CommonResult.success("修改成功!");
        }
    }

    @Override
    @GetMapping("/delMtDmPlan")
    public CommonResult delMtDmPlan(String id) {
        int flag = mtDmPlanService.delete(id);
        if (flag >0 ) {
            return CommonResult.success("保存成功!");
        } else {
            return CommonResult.success("修改成功!");
        }
    }

    @Override
    @PostMapping("/loadPageMtDmPlan")
    public CommonResult loadPageMtDmPlan(MtDmPlanReqVo vo) {
        return CommonResult.success(mtDmPlanService.loadPage(vo));
    }

    @Override
    @GetMapping("exportMtDmPlan")
    public void exportMtDmPlan(MtDmPlanReqVo vo , HttpServletResponse response) {
        mtDmPlanService.exportMtDmPlan(vo,response);
    }

    @Override
    @GetMapping("/getFileUrl")
    public CommonResult getYgFile(String pid, String fileId) {
        return CommonResult.success(mtDmPlanService.getYgFile(pid,fileId));
    }

    @Override
    @PostMapping("/saveOrUpdateMtDailyTask")
    public CommonResult saveOrUpdateMtDailyTask(MtDmDailyTaskReqVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            mtDailyTaskService.insert(vo);
            return CommonResult.success("保存成功!");
        } else {
            mtDailyTaskService.update(vo);
            return CommonResult.success("修改成功!");
        }
    }

    @Override
    @GetMapping("/delMtDailyTask")
    public CommonResult delMtDailyTask(String id) {

        int flag = mtDailyTaskService.deleteByTaskId(id);
        if (flag >0 ) {
            return CommonResult.success("保存成功!");
        } else {
            return CommonResult.success("修改成功!");
        }

    }

    @Override
    @PostMapping("/loadPageMtDailyTask")
    public CommonResult loadPageMtDailyTask(MtDmDailyTaskReqVo vo) {
        return CommonResult.success(mtDailyTaskService.loadPage(vo));
    }


    @Override
    @PostMapping("/saveOrUpdateMtFund")
    public CommonResult saveOrUpdateMtFund(BhLgMfdReqVo vo) {
        try{
            if (StringUtils.isEmpty(vo.getId())) {
                bhLgMfdService.insert(vo);
                return CommonResult.success("保存成功!");
            } else {
                bhLgMfdService.update(vo);
                return CommonResult.success("修改成功!");
            }
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }

    @Override
    @GetMapping("/delMtFund")
    public CommonResult delMtFund(String id) {
        try{
            return bhLgMfdService.delete(id) > 0? CommonResult.success() : CommonResult.failed();
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/loadPageMtFund")
    public CommonResult loadPageMtFund(BhLgMfdReqVo vo) {
        try{
            return CommonResult.success(bhLgMfdService.loadPage(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @GetMapping("/exportMtFund")
    @Override
    public void exportMtFund(BhLgMfdReqVo vo, HttpServletResponse response) {
        bhLgMfdService.exportMtFund(vo,response);
    }





}
