package com.ygkj.big.screen.drill;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.utils.GisRainfallSurfaceUtils;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.request.DrillParamReqVo;
import com.ygkj.big.screen.vo.response.UnitDataResVo;
import com.ygkj.gragh.vo.request.Rainfall4DrillReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.Builder;
import com.ygkj.water.model.CommonResult;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/8
 */
//@Component
public class RainfallDrillStrategy extends AbstractDrillStrategy {

    //    @Resource
    GraphClient graphClient;

    //    @Autowired
    GenerateDistributionIDService idService;

    GisRainfallSurfaceUtils gisRainfallSurfaceUtils;

    int[] hours = new int[]{0, 1, 3, 6, 24, 72};

    public RainfallDrillStrategy(ApplicationContext context) {
        super(context);
        graphClient = context.getBean(GraphClient.class);
        idService = context.getBean(GenerateDistributionIDService.class);
        gisRainfallSurfaceUtils = context.getBean(GisRainfallSurfaceUtils.class);
    }

    @Override
    public Map<String, List<UnitDataResVo>> buildData(DrillParamReqVo reqVo, Date endTime) {
        BsDrillCase drillCase = reqVo.getDrillCase();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tempEndTime = format.format(endTime);
        String tempStartTime = format.format(drillCase.getStartTime());
        String dataGroupId = reqVo.getDataGroupId();
        int timeIndex = reqVo.getTimeIndex();
        List<BsDrillStcase> stcaseList = reqVo.getStcaseList();
        String stcds = stcaseList.stream().map(BsDrillStcase::getDrillStCode).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));
        JSONObject rainfallData = rainfallStatisticDrill(stcds, tempEndTime, tempStartTime);
        UnitDataResVo statisticData = buildUnitData(rainfallData, "/floodDrought/rainfall", null, timeIndex, endTime, drillCase.getId(), drillCase.getUnitName(), dataGroupId, idService.nextID());
        List<UnitDataResVo> grandTotalReservoirRainfall = new ArrayList<>(hours.length);
        for (int hour : hours) {
            CommonResult commonResult = graphClient.grandTotalReservoirRainfall4Drill(Builder.of(Rainfall4DrillReqVo::new)
                    .with(Rainfall4DrillReqVo::setEndTime, tempEndTime)
                    .with(Rainfall4DrillReqVo::setStcds, stcds)
                    .with(Rainfall4DrillReqVo::setStartTime, tempStartTime)
                    .with(Rainfall4DrillReqVo::setHour, hour)
                    .build());

            JSONObject params = new JSONObject(true);
            params.put("hour", hour);
            params.put("endTime", tempEndTime);
            UnitDataResVo tempData = buildUnitData(commonResult, "/waterDisasterPrevention/grandTotalReservoirRainfall", params.toJSONString(), timeIndex, endTime, drillCase.getId(), drillCase.getUnitName(), dataGroupId, idService.nextID());
            grandTotalReservoirRainfall.add(tempData);
        }

        List<UnitDataResVo> grandTotalMaxRainfall = new ArrayList<>(hours.length);
        for (int hour : hours) {
            CommonResult commonResult = graphClient.grandTotalMaxRainfallStatistic4Drill(Builder.of(Rainfall4DrillReqVo::new)
                    .with(Rainfall4DrillReqVo::setEndTime, tempEndTime)
                    .with(Rainfall4DrillReqVo::setStcds, stcds)
                    .with(Rainfall4DrillReqVo::setStartTime, tempStartTime)
                    .with(Rainfall4DrillReqVo::setHour, hour)
                    .build());
            JSONObject params = new JSONObject(true);
            params.put("hour", hour);
            params.put("endTime", tempEndTime);
            UnitDataResVo tempData = buildUnitData(commonResult, "/waterDisasterPrevention/grandTotalMaxRainfallStatistic", params.toJSONString(), timeIndex, endTime, drillCase.getId(), drillCase.getUnitName(), dataGroupId, idService.nextID());
            grandTotalMaxRainfall.add(tempData);
        }

        Map<String, List<UnitDataResVo>> resultMap = new HashMap<>();
        resultMap.put("rainfall", Arrays.asList(statisticData));
        resultMap.put("grandTotalReservoirRainfall", grandTotalReservoirRainfall);
        resultMap.put("grandTotalMaxRainfallStatistic", grandTotalMaxRainfall);
        return resultMap;
    }

    private JSONObject rainfallStatisticDrill(String stcds, String endTime, String startTime) {
        CommonResult commonResult = graphClient.rainfallStatistic4Drill(Builder.of(Rainfall4DrillReqVo::new)
                .with(Rainfall4DrillReqVo::setEndTime, endTime)
                .with(Rainfall4DrillReqVo::setStcds, stcds)
                .with(Rainfall4DrillReqVo::setStartTime, startTime)
                .build());
        JSONObject commonResultObj = (JSONObject) JSONObject.toJSON(commonResult);
        return commonResultObj;
    }
}
