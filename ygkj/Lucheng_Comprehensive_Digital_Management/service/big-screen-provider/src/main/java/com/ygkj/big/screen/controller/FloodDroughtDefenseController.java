package com.ygkj.big.screen.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.FloodDroughtDefenseControllerApi;
import com.ygkj.big.screen.mapper.MessageRecordMapper;
import com.ygkj.big.screen.mapper.MetaDbMapper;
import com.ygkj.big.screen.service.DrillService;
import com.ygkj.big.screen.service.FloodDroughtDefenseService;
import com.ygkj.big.screen.service.impl.WarningMessageManager;
import com.ygkj.big.screen.vo.request.FddProjStaStatsReqVo;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

/**
 * @author xq
 * @Description
 * @Date 2021/5/1
 */
@RestController
@RequestMapping("floodDrought")
public class FloodDroughtDefenseController implements FloodDroughtDefenseControllerApi {

    @Autowired
    FloodDroughtDefenseService defenseService;

    @Autowired
    MetaDbMapper metaDbMapper;

    @Autowired
    private WarningMessageManager warningMessageManager;

    @Autowired
    private MessageRecordMapper messageRecordMapper;

    @Autowired
    private DrillService drillService;

    @GetMapping("weatherForecast")
    @Override
    public CommonResult weatherForecast() {
        return defenseService.weatherForecast();
    }

    @GetMapping("rainWindCurve")
    @Override
    public CommonResult rainWindCurve() {
        return CommonResult.success(defenseService.rainWindCurve());
    }

    @GetMapping("rainfall")
    @Override
    public CommonResult rainfall(String areaName, String endTime, String drillAffairId) {
//        if (drill){
//            try {
//                areaName = StringUtils.isBlank(areaName) ? "温州市" : areaName;
//                JSONObject params = new JSONObject();
//                params.put("areaName", areaName);
//                params.put("endTime", endTime);
//                String result = metaDbMapper.result("/waterAndRain/rainfallStatistic", params.toJSONString());
//                JSONObject json = JSON.parseObject(result);
//                JSONArray array = new JSONArray();
//                JSONArray array1 = json.getJSONObject("data").getJSONArray("rainfall_1_data");
//                array1.forEach(o -> {
//                    ((JSONObject) o).put("type", "1");
//                });
//                array.addAll(array1);
//                JSONArray array2 = json.getJSONObject("data").getJSONArray("rainfall_3_data");
//                array2.forEach(o -> {
//                    ((JSONObject) o).put("type", "3");
//                });
//                array.addAll(array2);
//                List<MessageRecord> list = warningMessageManager.submit(MessageTypes.HEAVY_RAIN.type(), array);
//                messageRecordMapper.batchInsert(list);
//                return JSON.toJavaObject(json, CommonResult.class);
//            }catch (Exception ignore){
//                return CommonResult.success();
//            }
//        }
        String caseId = null;
        if (StringUtils.isNotBlank(drillAffairId)
                && StringUtils.isNotBlank((caseId = drillService.getDrillCaseIdByAffairId(drillAffairId)))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String result = null;
            try {
                result = drillService.getDrillData(caseId, "实时降雨", "/floodDrought/rainfall", null, sdf.parse(endTime), 0);
                if (result != null) {
                    JSONObject json = JSON.parseObject(result);
                    JSONArray array = new JSONArray();
                    JSONArray array1 = json.getJSONObject("data").getJSONArray("rainfall_1_data");
                    array1.forEach(o -> {
                        ((JSONObject) o).put("type", "1");
                    });
                    array.addAll(array1);
                    JSONArray array2 = json.getJSONObject("data").getJSONArray("rainfall_3_data");
                    array2.forEach(o -> {
                        ((JSONObject) o).put("type", "3");
                    });
                    array.addAll(array2);
//                    List<MessageRecord> list = warningMessageManager.submit(MessageTypes.HEAVY_RAIN.type(), array);
//                    messageRecordMapper.batchInsert(list);
                    return JSON.toJavaObject(json, CommonResult.class);
                }
            } catch (Exception ignore) {
                return CommonResult.success();
            }
        }
        return defenseService.rainfallStatistic(areaName, endTime);
    }

    @GetMapping("predictRainfallStatistic")
    @Override
    public CommonResult predictRainfallStatistic() {
        return defenseService.predictRainfallStatistic();
    }

    @GetMapping("rainfallSum")
    @Override
    public CommonResult rainfallSum(@RequestParam(value = "hours", required = false) String hours) {
        return CommonResult.success(defenseService.rainfallSum(hours));
    }

    @GetMapping("rainfallForecast")
    @Override
    public CommonResult rainfallForecast(@RequestParam(value = "hours", required = false) String hours) {
        // todo 这里以后需要前端穿小时参数没时间了,先这么做
        return CommonResult.success(defenseService.rainfallForecast(hours));
    }

    @GetMapping("areaRainfallSingle")
    @Override
    public CommonResult areaRainfallSingle(@RequestParam("hour") Integer hour) {
        return CommonResult.success(defenseService.areaRainfallSingle(hour));
    }

    @GetMapping("areaRainfallForecastSingle")
    @Override
    public CommonResult areaRainfallForecastSingle(@RequestParam("hour") Integer hour) {
        return CommonResult.success(defenseService.areaRainfallForecastSingle(hour));
    }

    @GetMapping("waterloggingList")
    @Override
    public CommonResult waterloggingList() {
        return defenseService.waterloggingList();
    }

    @GetMapping("innerRiverList")
    @Override
    public CommonResult innerRiverList() {
        return defenseService.innerRiverList();
    }

    @GetMapping("rsvrStats")
    @Override
    public CommonResult rsvrStats() {
        return defenseService.rsvrStats();
    }

    @GetMapping("wagaStList")
    @Override
    public CommonResult wagaStList() {
        return defenseService.wagaStList();
    }

    @GetMapping("projStaStats")
    @Override
    public CommonResult projStaStats(FddProjStaStatsReqVo reqVo) {
        return CommonResult.success(defenseService.projStaStats(reqVo));
    }

    @GetMapping("tideStList")
    @Override
    public CommonResult tideStList() {
        return defenseService.tideStList();
    }

    @GetMapping("/dikeAndSeawallLengthCount")
    @Override
    public CommonResult dikeAndSeawallLengthCount() {
        return defenseService.dikeAndSeawallLengthCount();
    }

    @GetMapping("/emergencySupportCount")
    @Override
    public CommonResult emergencySupportCount() {
        return defenseService.emergencySupportCount();
    }

    @GetMapping("/projectCount")
    @Override
    public CommonResult projectCount(@RequestParam(value = "type") String type, @RequestParam(value = "stType", required = false) String stType) {
        return defenseService.projectCount(type, stType);
    }

    @GetMapping("/pastHoursRainfall")
    @Override
    public CommonResult pastHoursRainfall() {
        return defenseService.pastHoursRainfall();
    }

    @Override
    @GetMapping("/reservoirArchivesCount")
    public CommonResult reservoirArchivesCount() {
        return CommonResult.success(defenseService.reservoirArchivesCount());
    }

    @Override
    @GetMapping("/attStBaseCountByType")
    public CommonResult attStBaseCountByType(String type, String stType, Boolean isWrp) {
        return defenseService.attStBaseCountByType(type, stType, isWrp);
    }

    @Override
    @PostMapping("countOverAlarm")
    public CommonResult countOverAlarm() {
        return CommonResult.success(defenseService.countOverAlarm());
    }
}
