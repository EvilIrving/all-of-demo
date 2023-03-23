package com.ygkj.gragh;

import com.ygkj.gragh.vo.request.CameraQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "视频监控", value = "视频监控")
public interface CameraControllerApi {

    @ApiOperation("视频数据查询（列表和地图通用）")
    CommonResult list(CameraQueryVo cameraQueryVo);
}
