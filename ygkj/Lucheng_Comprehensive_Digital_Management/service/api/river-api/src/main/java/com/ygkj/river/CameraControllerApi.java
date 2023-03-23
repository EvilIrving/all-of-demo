package com.ygkj.river;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/4/15
 */
@Api(tags = "摄像头接口")
public interface CameraControllerApi {
    @ApiOperation("抓拍")
    CommonResult doCapture(String cameraIndexCode);
}
