package com.ygkj.visualization.service;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiOperation;

public interface RainDataSurfaceService {

    @ApiOperation("五公里矫正等值面数据")
    JSONArray get5kmCorrectRainData();
}
