package com.ygkj.flood.service;

import com.ygkj.flood.enums.FloatWindowTypeEnum;
import com.ygkj.flood.model.StBase;
import com.ygkj.flood.vo.response.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;

public interface WaterLevelAnalyseService {

    CommonResult<List<WlaHisHighestWaterLevelRespVo>> hisHighestWaterLevel(String type, String stName, String stCode);

    CommonResult<List<WlaWarnNumStatisticsRespVo>> warnNumStatistics();

    CommonResult<WlaOverWarnNumRespVo> overWarnStatistics();

    CommonResult<List<WlaFloatWinWaterLevelRespVo>> floatWinWaterLevel(String type, String name, String adcd, String stCode);

    List<WarningStationResVo> selectTodayWarningStation();

    List<Map<String, Object>> getAllStWatLev(String stName);

    List<Map<String, Object>> getHistoricWaterLevelExtremum(String stName);

    /**
     * type=0 为中大型水库水位
     * type=1 为平原水位
     * type=2 为流域水位
     *
     * @param stName
     * @param type
     * @return
     */
    List<RealWaterLevel> watLevOfPlainBasInnerRiv(String stName, Integer type);

    /**
     * 查询内河的实时水位
     *
     * @param stName
     * @return
     */
    List<StBase> innerRiverWaterLevel(String stName);
}
