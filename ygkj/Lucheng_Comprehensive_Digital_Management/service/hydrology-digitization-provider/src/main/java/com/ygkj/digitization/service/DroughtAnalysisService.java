package com.ygkj.digitization.service;

import com.ygkj.digitization.vo.request.DroughtReqVo;
import com.ygkj.digitization.vo.request.RainfallReqVo;
import com.ygkj.digitization.vo.response.DayRainResVo;
import com.ygkj.digitization.vo.response.RainfallResVo;

import java.util.List;
import java.util.Map;

public interface DroughtAnalysisService {

    /**
     * 时间段降雨量
     *
     * @param reqVo
     * @return
     */
    Map<String, List<RainfallResVo>> listRainfall(RainfallReqVo reqVo);

    /**
     * 根据日期返回对应的预警结果
     *
     * @param date
     * @return
     */
    Object droughtWarning(DroughtReqVo reqVo);

    Object getDroughtStandard();

    /**
     * 近三十日降雨量
     *
     * @return
     */
    List<DayRainResVo> listDayRainfall();
}
