package com.ygkj.digitization.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.vo.request.StationQueryVo;

public interface RainfallReportService {

    JSONObject riverwaterLevel(StationQueryVo queryVo);

}
