package com.ygkj.flood.controller;

import com.ygkj.flood.FloodProjectControllerApi;
import com.ygkj.flood.service.FloodProjectService;
import com.ygkj.flood.vo.request.ProjectListReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("floodProject")
public class FloodProjectController implements FloodProjectControllerApi {

    @Resource
    private FloodProjectService floodProjectService;

    @Override
    @GetMapping("countStorage")
    public CommonResult countStorage(Integer type, Integer resScal) {
        return CommonResult.success(floodProjectService.countStorage(type, resScal));
    }

    @Override
    @GetMapping("countAllStorageRate")
    public CommonResult countAllStorageRate() {
        return CommonResult.success(floodProjectService.countAllStorageRate());
    }

    @Override
    @PostMapping("listWaga")
    public CommonResult listWaga(ProjectListReqVo reqVo) {
        return CommonResult.success(floodProjectService.listWaga(reqVo));
    }

    //    @GetMapping("listPust")
    @Override
    public CommonResult listPust(ProjectListReqVo reqVo) {
        return CommonResult.success(floodProjectService.listPust(reqVo));
    }

    @GetMapping("listWagaProj")
    @Override
    public CommonResult listWagaProj(Integer pageSize, Integer pageNum) {
        return CommonResult.success(floodProjectService.listWagaProj(pageSize, pageNum));
    }

    @GetMapping("listPustProj")
    @Override
    public CommonResult listPustProj(Integer pageSize, Integer pageNum, String pustCode) {
        return CommonResult.success(floodProjectService.listPustProj(pageSize, pageNum, pustCode));
    }

    @GetMapping("listRsvrStorageWaterLevel")
    @Override
    public CommonResult listRsvrStorageWaterLevel() {
        return CommonResult.success(floodProjectService.listRsvrStorageWaterLevel());
    }
}
