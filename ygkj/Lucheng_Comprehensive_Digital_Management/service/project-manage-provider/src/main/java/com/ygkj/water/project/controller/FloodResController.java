package com.ygkj.water.project.controller;

import com.ygkj.project.FloodResControllerApi;
import com.ygkj.project.vo.request.ResProjectReqVo;
import com.ygkj.project.vo.request.RainReqVo;
import com.ygkj.project.vo.request.TyphoonProjectReqVo;
import com.ygkj.project.vo.request.WaterReqVo;
import com.ygkj.utils.SnowFlake;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.FloodResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("floodRes")
public class FloodResController implements FloodResControllerApi {

    @Resource
    private FloodResService floodResService;

    @Override
    @PostMapping("listRain")
    public CommonResult listRain(RainReqVo reqVo) {
        return CommonResult.success(floodResService.listRain(reqVo));
    }

    @Override
    @GetMapping("rainHistogram")
    public CommonResult rainHistogram(String stCode,Integer days) {
        return CommonResult.success(floodResService.rainHistogram(stCode,days));
    }

    @Override
    @PostMapping("listResProject")
    public CommonResult listResProject(ResProjectReqVo reqVo) {
        return CommonResult.success(floodResService.listResProject(reqVo));
    }

    @Override
    @PostMapping("listWater")
    public CommonResult listWater(WaterReqVo reqVo) {
        return CommonResult.success(floodResService.listWater(reqVo));
    }

//    @Override
//    @GetMapping("waterCurve")
//    public CommonResult waterCurve(String stCode) {
//        return CommonResult.success(floodResService.waterCurve(stCode));
//    }


    @Override
    @PostMapping("listTyphoonProject")
    public CommonResult listTyphoonProject(TyphoonProjectReqVo reqVo) {
        return CommonResult.success(floodResService.listTyphoonProject(reqVo));
    }

    @Override
    @PostMapping("listTyphoonAllRealProject")
    public CommonResult listTyphoonAllRealProject(TyphoonProjectReqVo reqVo) {
        return CommonResult.success(floodResService.listTyphoonAllRealProject(reqVo));
    }

    @Override
    @PostMapping("/listTyphoonAllRealProjectNewestVersion")
    public CommonResult listTyphoonAllRealProjectNewestVersion(TyphoonProjectReqVo reqVo) {
        return CommonResult.success(floodResService.listTyphoonAllRealProjectNewestVersion(reqVo));
    }

    @Override
    @PostMapping("countTyphoonAllRealProject")
    public CommonResult countTyphoonAllRealProject(TyphoonProjectReqVo reqVo) {
        return CommonResult.success(floodResService.countTyphoonAllRealProject(reqVo));
    }


    @Autowired
    private SnowFlake snowFlake;

    @GetMapping("/id")
    public CommonResult generateId(){
        return CommonResult.success(String.valueOf(snowFlake.nextId()));
    }
}
