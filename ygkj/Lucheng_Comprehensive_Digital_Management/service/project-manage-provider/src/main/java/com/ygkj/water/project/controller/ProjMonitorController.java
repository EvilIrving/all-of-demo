package com.ygkj.water.project.controller;

import com.ygkj.project.ProjMonitorControllerApi;
import com.ygkj.project.vo.request.MoRegularMonitorVo;
import com.ygkj.project.vo.request.ProjMonitorReqVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.ProjMonitorService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-28 14:34
 * @description
 */
@Api(tags = "工程监测")
@RestController
@RequestMapping("/projMonitor")
public class ProjMonitorController implements ProjMonitorControllerApi {

    @Resource
    private ProjMonitorService projMonitorService;


    @Override
    @PostMapping("/regularMonitorList")
    public CommonResult regularMonitorList(ProjMonitorReqVo reqVo) {

        return CommonResult.success(projMonitorService.regularMonitorList(reqVo));
    }

    @Override
    @PostMapping("/regularSaveOrUpdate")
    public CommonResult regularMonitorSaveOrUpdate(MoRegularMonitorVo vo) {

        Integer flag;

        try {
            if (StringUtils.isNotBlank(vo.getId())){
                flag = projMonitorService.updateRegularMonitor(vo);
            }else {
                flag = projMonitorService.insertRegularMonitor(vo);
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
    @GetMapping("/regularDelete")
    public CommonResult regularMonitorDelete(String id) {
        try {
            if (projMonitorService.regularMonitorDelete(id) > 0){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }
    }


    @Override
    @PostMapping("/regularMonitorExport")
    public void regularMonitorExport(HttpServletResponse response, ProjMonitorReqVo reqVo) {
        projMonitorService.regularMonitorExport(response,reqVo);
    }
}
