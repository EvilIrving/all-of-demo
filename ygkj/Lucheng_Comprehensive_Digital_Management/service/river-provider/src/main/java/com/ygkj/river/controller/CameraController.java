package com.ygkj.river.controller;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.river.utils.HikvisionUtils;
import com.ygkj.river.CameraControllerApi;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/4/15
 */
@RestController
@RequestMapping("camera")
public class CameraController implements CameraControllerApi {

    @GetMapping("doCapture")
    @Override
    public CommonResult doCapture(@RequestParam("cameraIndexCode") String cameraIndexCode) {
        if (StringUtils.isBlank(cameraIndexCode)) {
            return CommonResult.failed("摄像头编码为空");
        }
        JSONObject result = HikvisionUtils.doCapture(cameraIndexCode);
        if ("0".equals(result.getString("code"))) {
            return CommonResult.success(result.getJSONObject("data"));
        } else {
            return CommonResult.failed(result.getString("msg"));
        }
    }
}
