package com.ygkj.project;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/4/12
 */
@Api(tags = "工程管理-运维分析")
public interface OpsStatisticControllerApi {

    @ApiOperation("流域摄像头数量")
    CommonResult cameraNumOfBas();

    @ApiOperation("工程摄像头数量")
    CommonResult cameraNumOfProj();

    @ApiOperation("行政区划摄像头数量")
    CommonResult cameraNumOfAdcd();

    @ApiOperation("行政区划在线摄像头")
    CommonResult onlineCameraNumOfAdcd();
}
