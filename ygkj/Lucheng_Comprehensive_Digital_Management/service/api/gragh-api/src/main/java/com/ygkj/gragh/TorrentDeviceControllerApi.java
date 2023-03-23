package com.ygkj.gragh;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @create 2022-04-08 16:45
 * @description 山洪-体感数据推送
 */
@Api(tags = "山洪-体感数据推送", value = "山洪-体感数据推送")
public interface TorrentDeviceControllerApi {

    @ApiOperation("推送数据")
    CommonResult pushData(String timingData);

}
