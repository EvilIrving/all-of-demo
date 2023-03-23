package com.ygkj.gragh.controller;

import com.ygkj.gragh.CameraControllerApi;
import com.ygkj.gragh.service.CameraRecordService;
import com.ygkj.gragh.vo.request.CameraQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/camera")
public class CameraController implements CameraControllerApi {

    @Autowired
    private CameraRecordService cameraRecordService;

    @PostMapping("")
    @Override
    public CommonResult list(CameraQueryVo cameraQueryVo) {
        return CommonResult.success(cameraRecordService.cameras(cameraQueryVo));
    }
}
