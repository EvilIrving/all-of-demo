package com.ygkj.big.screen.service;

import com.ygkj.water.model.CommonResult;

/**
 * @author wgf
 * @date 2022/5/6 10:23
 * @Description:
 */
public interface FloodControlDispatchingService {
    CommonResult mapPoints();

    Object forecast(String index);

    Object plan();

    Object planSupervision();

    Object projectList(String name);

    Object dmInfo(String id, String startTime, String endTime);

    Object chooseSection();

    Object forecastSection(String sectionIds);

    Object dmWaterLevel(String id);
}
