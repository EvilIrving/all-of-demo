package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.FloodProtectClient;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.feign.MeteorologicVisualizationClient;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.mapper.AttStBaseMapper;
import com.ygkj.big.screen.model.AttStBase;
import com.ygkj.big.screen.model.StPptnR;
import com.ygkj.big.screen.service.FloodDroughtDefenseService;
import com.ygkj.big.screen.vo.request.FddProjStaStatsReqVo;
import com.ygkj.big.screen.vo.request.FloodPointReqVo;
import com.ygkj.big.screen.vo.request.SynRainRequestVo;
import com.ygkj.big.screen.vo.request.SynWaterRequestVo;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.enums.StationType;
import com.ygkj.flood.model.AttPrevTfBase;
import com.ygkj.flood.vo.request.AttPrevTfBaseVo;
import com.ygkj.gragh.rpc.GraphRpcService;
import com.ygkj.project.model.SectionConfiguration;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/5/1
 */
@Service
@Slf4j
public class FloodDroughtDefenseServiceImpl implements FloodDroughtDefenseService {

    GraphClient graphClient;
    ProjectManageClient projectManageClient;
    MeteorologicVisualizationClient mvClient;
    FloodProtectClient floodProtectClient;
    AttStBaseMapper stBaseMapper;

    @Reference(interfaceClass = GraphRpcService.class, retries = 0, methods = {
            @Method(name = "rainfallStatistic", timeout = 300000, retries = 0)
    })
    private GraphRpcService graphRpcService;

    public FloodDroughtDefenseServiceImpl(@Autowired GraphClient graphClient,
                                          @Autowired ProjectManageClient projectManageClient,
                                          @Autowired MeteorologicVisualizationClient meteorologicVisualizationClient,
                                          @Autowired FloodProtectClient floodProtectClient,
                                          @Autowired AttStBaseMapper stBaseMapper
    ) {

        this.graphClient = graphClient;
        this.projectManageClient = projectManageClient;
        this.mvClient = meteorologicVisualizationClient;
        this.floodProtectClient = floodProtectClient;
        this.stBaseMapper = stBaseMapper;
    }


    @Override
    public CommonResult weatherForecast() {
        return mvClient.weatherPredict();
    }

    @Override
    public List<ChartResVo<String, List<JSONObject>>> rainWindCurve() {
        CommonResult<List<LinkedHashMap>> rainResp = mvClient.weatherStatistic("温州市", "降雨量");
        CommonResult<List<LinkedHashMap>> windResp = mvClient.weatherStatistic("温州市", "风速");
        Map<String, Double> rainMap = Collections.emptyMap();
        Map<String, Double> windMap = Collections.emptyMap();
        List<String> rainHourList = Collections.emptyList();
        List<String> windHourList = Collections.emptyList();
        if (rainResp.getCode() == CommonCode.SUCCESS.code()
                && CollectionUtils.isNotBlank(rainResp.getData())) {
            List<LinkedHashMap> respTemp = rainResp.getData();
            List<JSONObject> list = JSONArray.parseArray(JSONObject.toJSONString(respTemp)).toJavaList(JSONObject.class);
            rainHourList = list
                    .stream().map(o -> o.getString("hour"))
                    .collect(Collectors.toList());
            rainMap = list
                    .stream()
                    .collect(Collectors.toMap(o -> o.getString("hour"), o -> o.getDouble("value")));
        }
        if (windResp.getCode() == CommonCode.SUCCESS.code()
                && CollectionUtils.isNotBlank(windResp.getData())) {
            List<LinkedHashMap> respTemp = windResp.getData();
            List<JSONObject> list = JSONArray.parseArray(JSONObject.toJSONString(respTemp)).toJavaList(JSONObject.class);
            windHourList = list
                    .stream().map(o -> o.getString("hour"))
                    .collect(Collectors.toList());
            windMap = list
                    .stream()
                    .collect(Collectors.toMap(o -> o.getString("hour"), o -> o.getDouble("value")));
        }
        rainHourList.retainAll(windHourList);
        List<JSONObject> rainValList = new ArrayList<>(rainHourList.size());
        List<JSONObject> windValList = new ArrayList<>(rainHourList.size());
        for (String s : rainHourList) {
            rainValList.add(Builder.of(JSONObject::new)
                    .with(JSONObject::put, "hour", s)
                    .with(JSONObject::put, "value", rainMap.get(s))
                    .build());
            windValList.add(Builder.of(JSONObject::new)
                    .with(JSONObject::put, "hour", s)
                    .with(JSONObject::put, "value", windMap.get(s))
                    .build());
        }
        return Arrays.asList(new ChartResVo<>("风速", windValList), new ChartResVo<>("降雨量", rainValList));

    }

    @Override
    public synchronized CommonResult rainfallStatistic(String areaName, String endTime) {
        CommonResult result = RainfallSurfaceDataManager.getRainfallSurfaceData(areaName);
        if (result != null && result.getCode() == 0l) {
            return result;
        }
//        result = graphClient.rainfallStatistic(areaName, endTime);
        result = CommonResult.success(graphRpcService.rainfallStatistic(areaName, endTime));
        return result;
    }

    @Override
    public CommonResult predictRainfallStatistic() {
        return mvClient.predictRainfallStatistic();
    }

    @Override
    public List<ChartResVo<Integer, List<StPptnR>>> rainfallSum(String hours) {
        SynRainRequestVo reqVo = Builder.of(SynRainRequestVo::new)
                .with(SynRainRequestVo::setMax, 1000000D)
                .with(SynRainRequestVo::setMin, -10D)
                .build();

        List<Integer> hourList = Collections.emptyList();
        if (StringUtils.isBlank(hours)) {
            hourList = Arrays.asList(72, 24, 12, 6, 3, 1);
        } else {
            hourList = Arrays.asList(hours.split(",")).stream().map(o -> {
                try {
                    return Integer.parseInt(o);
                } catch (Exception e) {
                    return -1;
                }
            }).filter(o -> o > 0 && o <= 72).collect(Collectors.toList());
        }
        List<ChartResVo<Integer, List<StPptnR>>> result = new ArrayList<>(7);
        for (Integer hour : hourList) {
            reqVo.setMinutes(hour * 60);
            CommonResult<List<StPptnR>> hResp = graphClient.rainfall(reqVo);
            if (hResp.getCode() == CommonCode.SUCCESS.code()
                    && CollectionUtils.isNotBlank(hResp.getData())) {
                result.add(Builder.of(ChartResVo<Integer, List<StPptnR>>::new)
                        .with(ChartResVo::setKey, hour)
                        .with(ChartResVo::setValue, hResp.getData()).build());
            } else {
                result.add(Builder.of(ChartResVo<Integer, List<StPptnR>>::new)
                        .with(ChartResVo::setKey, hour)
                        .with(ChartResVo::setValue, new ArrayList<StPptnR>()).build());
            }
        }
        return result;
    }

    @Override
    public List<ChartResVo<Integer, List<StPptnR>>> rainfallForecast(String hours) {
        Double zeroD = 0D;
        List<AttStBase> stBases = stBaseMapper.selectByCondition(Builder.of(HashMap<String, Object>::new).with(HashMap::put, "stType", StationType.RAINFALL.type()).build());
        List<StPptnR> h1List = new ArrayList<>(stBases.size());
        List<StPptnR> h3List = new ArrayList<>(stBases.size());
        List<StPptnR> h6List = new ArrayList<>(stBases.size());
        for (AttStBase stBase : stBases) {
            StPptnR h1Obj = new StPptnR();
            StPptnR h3Obj = new StPptnR();
            StPptnR h6Obj = new StPptnR();
            h1Obj.setLongitude(stBase.getStLong() == null ? zeroD : stBase.getStLong().doubleValue());
            h1Obj.setLatitude(stBase.getStLong() == null ? zeroD : stBase.getStLong().doubleValue());
            h1Obj.setStType(stBase.getStType());
            h1Obj.setStcd(stBase.getStCode());
            h1Obj.setMgstcd(stBase.getStCode());
            h1Obj.setStationName(stBase.getStName());
            BeanUtils.copyProperties(h1Obj, h3Obj);
            BeanUtils.copyProperties(h1Obj, h6Obj);
            CommonResult<LinkedHashMap> forest = mvClient.caculateRainfall(String.valueOf(stBase.getStLong()), String.valueOf(stBase.getStLat()));

            if (CommonResult.success().getCode() == CommonCode.SUCCESS.code() && forest.getData() != null) {
                JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(forest.getData()));
                try {
                    h1Obj.setDrp(data.getDoubleValue("rainfall_1"));
                } catch (Exception e) {
                    h1Obj.setDrp(0D);
                }
                try {
                    h3Obj.setDrp(data.getDoubleValue("rainfall_3"));
                } catch (Exception e) {
                    h3Obj.setDrp(0D);
                }
                try {
                    h6Obj.setDrp(data.getDoubleValue("rainfall_6"));
                } catch (Exception e) {
                    h6Obj.setDrp(0D);
                }
            } else {
                h1Obj.setDrp(zeroD);
                h3Obj.setDrp(zeroD);
                h6Obj.setDrp(zeroD);
            }
            h1List.add(h1Obj);
            h3List.add(h3Obj);
            h6List.add(h6Obj);
        }
        return Arrays.asList(Builder.of(ChartResVo<Integer, List<StPptnR>>::new)
                        .with(ChartResVo::setKey, 1)
                        .with(ChartResVo::setValue, h1List).build(),
                Builder.of(ChartResVo<Integer, List<StPptnR>>::new)
                        .with(ChartResVo::setKey, 3)
                        .with(ChartResVo::setValue, h3List).build(),
                Builder.of(ChartResVo<Integer, List<StPptnR>>::new)
                        .with(ChartResVo::setKey, 6)
                        .with(ChartResVo::setValue, h6List).build());
    }

    /**
     * 面雨量需要的格式
     * {"features":[{"attributes":{"x":120.729444,"y":27.883611,"value":0},"geometry":{"x":120.729444,"y":27.883611}}]}
     *
     * @param hour
     * @return
     */
    @Override
    public ArealRainResVo areaRainfallSingle(Integer hour) {
        SynRainRequestVo reqVo = Builder.of(SynRainRequestVo::new)
                .with(SynRainRequestVo::setMax, 1000000D)
                .with(SynRainRequestVo::setMin, -10D)
                .with(SynRainRequestVo::setMinutes, hour * 60)
                .build();
        reqVo.setMinutes(hour * 60);
        CommonResult<List<StPptnR>> hResp = graphClient.rainfall(reqVo);
        if (hResp.getCode() == CommonCode.SUCCESS.code()
                && CollectionUtils.isNotBlank(hResp.getData())) {
            return mapStPptnR2gGisFormat(hResp.getData());
        }
        return Builder.of(ArealRainResVo::new)
                .with(ArealRainResVo::setFeatures, Collections.emptyList())
                .build();
    }

    private ArealRainResVo mapStPptnR2gGisFormat(List<StPptnR> sources) {
        List<ArealRainPointResVo> features = new ArrayList<>(sources.size());
        for (StPptnR source : sources) {
            ArealRainPointResVo target = new ArealRainPointResVo();
            target.setAttributes(Builder.of(ArealRainAttrResVo::new)
                    .with(ArealRainAttrResVo::setX, source.getLongitude())
                    .with(ArealRainAttrResVo::setY, source.getLatitude())
                    .with(ArealRainAttrResVo::setValue, source.getDrp())
                    .build());

            target.setGeometry(Builder.of(ArealRainGeoResVo::new)
                    .with(ArealRainGeoResVo::setX, source.getLongitude())
                    .with(ArealRainGeoResVo::setY, source.getLatitude())
                    .build());
            features.add(target);
        }
        return Builder.of(ArealRainResVo::new)
                .with(ArealRainResVo::setFeatures, features)
                .build();
    }


    @Override
    public ArealRainResVo areaRainfallForecastSingle(Integer hour) {
        Double zeroD = 0D;
        List<AttStBase> stBases = stBaseMapper.selectByCondition(Builder.of(HashMap<String, Object>::new).with(HashMap::put, "stType", StationType.RAINFALL.type()).build());
        List<StPptnR> hList = new ArrayList<>(stBases.size());
        for (AttStBase stBase : stBases) {
            StPptnR hObj = new StPptnR();
            hObj.setLongitude(stBase.getStLong() == null ? zeroD : stBase.getStLong().doubleValue());
            hObj.setLatitude(stBase.getStLong() == null ? zeroD : stBase.getStLong().doubleValue());
            hObj.setStType(stBase.getStType());
            hObj.setStcd(stBase.getStCode());
            hObj.setMgstcd(stBase.getStCode());
            hObj.setStationName(stBase.getStName());
            CommonResult<LinkedHashMap> forest = mvClient.caculateRainfall(String.valueOf(stBase.getStLong()), String.valueOf(stBase.getStLat()));
            if (CommonResult.success().getCode() == CommonCode.SUCCESS.code() && forest.getData() != null) {
                JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(forest.getData()));
                try {
                    switch (hour) {
                        case 1:
                            hObj.setDrp(data.getDoubleValue("rainfall_1"));
                            break;
                        case 3:
                            hObj.setDrp(data.getDoubleValue("rainfall_3"));
                            break;
                        case 6:
                            hObj.setDrp(data.getDoubleValue("rainfall_6"));
                            break;
                        default:
                            hObj.setDrp(zeroD);
                    }
                } catch (Exception e) {
                    hObj.setDrp(zeroD);
                }
            } else {
                hObj.setDrp(zeroD);
            }
            hList.add(hObj);
        }
        return mapStPptnR2gGisFormat(hList);
    }


    @Override
    public CommonResult waterloggingList() {
        return floodProtectClient.listFloodPoint(new FloodPointReqVo());
    }

    @Override
    public CommonResult innerRiverList() {
        return floodProtectClient.innerRiverWaterLevel(null);
    }

    @Override
    public CommonResult rsvrStats() {
        CommonResult<List<LinkedHashMap>> commonResult = floodProtectClient.listRsvrStorageWaterLevel();
        List<LinkedHashMap> data = commonResult.getData();
        data = data.stream().filter(linkedHashMap -> Integer.valueOf(String.valueOf(linkedHashMap.get("engGrad"))) < 4).collect(Collectors.toList());
        data.sort(Comparator.comparing(linkedHashMap -> Double.parseDouble(String.valueOf(linkedHashMap.get("meiFloodLimitWaterLevel")))
                - Double.parseDouble(String.valueOf(linkedHashMap.get("waterLevel")))));
        return CommonResult.success(data);
    }

    @Override
    public CommonResult wagaStList() {
        CommonResult<List<AttStBase>> listCommonResult = graphClient
                .waterStations(Builder.of(SynWaterRequestVo::new)
                        .with(SynWaterRequestVo::setStationType, StationType.GATE.type()).build());
        List<AttStBase> data = listCommonResult.getData();
        data.sort(Comparator.comparing(attStBase -> attStBase.getWarningLevel() == null
                ? 0 - attStBase.getUpz() : attStBase.getWarningLevel() - attStBase.getUpz()));
        return CommonResult.success(data);
    }

    @Override
    public List<LinkedHashMap> projStaStats(FddProjStaStatsReqVo reqVo) {
        List<LinkedHashMap> result = new ArrayList<>(20);
        if (StringUtils.isBlank(reqVo.getType()) || "工程".equals(reqVo.getType())) {
            CommonResult<List<LinkedHashMap>> response = projectManageClient.countProjTypeNumByAdcdBasin(reqVo.getAdcd(), reqVo.getBasin());
            if (response.getCode() == CommonCode.SUCCESS.code()
                    && CollectionUtils.isNotBlank(response.getData())) {
                result.addAll(response.getData());
            }
        }
        if (StringUtils.isBlank(reqVo.getType()) || "测站".equals(reqVo.getType())) {
            CommonResult<List<LinkedHashMap>> response = graphClient.countStTypeNumByAdcdBasin(reqVo.getAdcd(), reqVo.getBasin());
            if (response.getCode() == CommonCode.SUCCESS.code()
                    && CollectionUtils.isNotBlank(response.getData())) {
                result.addAll(response.getData());
            }
        }
        return result;
    }

    @Override
    public CommonResult tideStList() {
        CommonResult<List<AttStBase>> listCommonResult = graphClient
                .waterStations(Builder.of(SynWaterRequestVo::new)
                        .with(SynWaterRequestVo::setStationType, StationType.TIDE.type()).build());
        List<AttStBase> data = listCommonResult.getData();
        data.sort(Comparator.comparing(attStBase -> attStBase.getWarningLevel() == null
                ? 0 - attStBase.getTdz() : attStBase.getWarningLevel() - attStBase.getTdz()));
        return CommonResult.success(data);
    }

    @Override
    public CommonResult dikeAndSeawallLengthCount() {
        return projectManageClient.dikeAndSeawallLengthCount();
    }

    @Override
    public CommonResult emergencySupportCount() {
        return floodProtectClient.emergencySupportCount();
    }

    @Override
    public CommonResult projectCount(String type, String stType) {
        return projectManageClient.projectCount(type, stType);
    }

    @Override
    public CommonResult pastHoursRainfall() {
        return graphClient.pastHoursRainfall();
    }

    @Override
    public JSONObject countOverAlarm() {
        JSONObject result = new JSONObject();
        //雨量预警
        CommonResult rainOverAlarm = rainfallStatistic(null, null);
        if (rainOverAlarm.getCode() == 0){
            JSONObject rainOverResult = (JSONObject) rainOverAlarm.getData();
            if (rainOverResult.getInteger("1hourBeyond30") >= rainOverResult.getInteger("3hourBeyond60") ){
                result.put("rainAlarmNum", rainOverResult.getInteger("1hourBeyond30"));
            }else {
                result.put("rainAlarmNum", rainOverResult.getInteger("3hourBeyond60"));
            }
        }else {
            result.put("rainAlarmNum", 0);
        }

        //水位预警
        int skNum = 0, hdNum = 0, zbNum = 0, cwNum = 0;
        SynWaterRequestVo skRequestVo = new SynWaterRequestVo();
        skRequestVo.setStationType("RR");
        CommonResult skWaterOverAlarm = graphClient.waterStations(skRequestVo);
        if (skWaterOverAlarm.getCode() == 0){
            List<AttStBase> skWaterOverResult = (List<AttStBase>) skWaterOverAlarm.getData();
            if (CollectionUtils.isNotBlank(skWaterOverResult)){
                for (AttStBase attStBase : skWaterOverResult) {
                    if (attStBase.getLimitLevel() != null && attStBase.getLimitLevel() > 0 && attStBase.getWaterLevel() > attStBase.getLimitLevel()){
                        skNum++;
                    }
                }
            }
        }
        SynWaterRequestVo hdRequestVo = new SynWaterRequestVo();
        hdRequestVo.setStationType("ZZ");
        CommonResult hdWaterOverAlarm = graphClient.waterStations(hdRequestVo);
        if (hdWaterOverAlarm.getCode() == 0){
            List<AttStBase> hdWaterOverResult = (List<AttStBase>) skWaterOverAlarm.getData();
            if (CollectionUtils.isNotBlank(hdWaterOverResult)){
                for (AttStBase attStBase : hdWaterOverResult) {
                    if ((attStBase.getWarningLevel() != null && attStBase.getWarningLevel() > 0 && attStBase.getWaterLevel() > attStBase.getWarningLevel()) ||
                            (attStBase.getGuaranteeLevel() != null && attStBase.getGuaranteeLevel() > 0 && attStBase.getWaterLevel() > attStBase.getGuaranteeLevel())){
                        hdNum++;
                    }
                }
            }
        }
        SynWaterRequestVo zbRequestVo = new SynWaterRequestVo();
        zbRequestVo.setStationType("DD");
        CommonResult zbWaterOverAlarm = graphClient.waterStations(zbRequestVo);
        if (zbWaterOverAlarm.getCode() == 0){
            List<AttStBase> zbWaterOverResult = (List<AttStBase>) skWaterOverAlarm.getData();
            if (CollectionUtils.isNotBlank(zbWaterOverResult)){
                for (AttStBase attStBase : zbWaterOverResult) {
                    if ((attStBase.getWarningLevel() != null && attStBase.getWarningLevel() > 0 && attStBase.getUpz() > attStBase.getWarningLevel()) ||
                            (attStBase.getGuaranteeLevel() != null && attStBase.getGuaranteeLevel() > 0 && attStBase.getUpz() > attStBase.getGuaranteeLevel())){
                        zbNum++;
                    }
                }
            }
        }
        SynWaterRequestVo cwRequestVo = new SynWaterRequestVo();
        cwRequestVo.setStationType("TT");
        CommonResult cwWaterOverAlarm = graphClient.waterStations(cwRequestVo);
        if (cwWaterOverAlarm.getCode() == 0){
            List<AttStBase> cwWaterOverResult = (List<AttStBase>) skWaterOverAlarm.getData();
            if (CollectionUtils.isNotBlank(cwWaterOverResult)){
                for (AttStBase attStBase : cwWaterOverResult) {
                    if (attStBase.getWarningLevel() != null && attStBase.getWarningLevel() > 0 && attStBase.getTdz() > attStBase.getWarningLevel()){
                        cwNum++;
                    }
                }
            }
        }
        result.put("waterAlarmNum", skNum + hdNum + zbNum + cwNum);
        //水文预警
        result.put("hydrologyAlarmNum", 0);
        //潮位预警
        CommonResult tideOverAlarm = projectManageClient.forecastMaxHour(new SectionConfiguration());
        if (tideOverAlarm.getCode() == 0){
            List<LinkedHashMap> tideOverResult = (List<LinkedHashMap>) tideOverAlarm.getData();
            List<JSONObject> list = JSONArray.parseArray(JSON.toJSONString(tideOverResult), JSONObject.class);
            if (CollectionUtils.isNotBlank(tideOverResult)){
                List<JSONObject> warning = list.stream().filter(o -> o.getBoolean("warning")).collect(Collectors.toList());
                result.put("tideAlarmNum", warning.size());
            }else {
                result.put("tideAlarmNum", 0);
            }
        }else {
            result.put("tideAlarmNum", 0);
        }
        //山洪预警
        AttPrevTfBaseVo vo = new AttPrevTfBaseVo();
        vo.setIsAlarm("true");
        CommonResult torrentOverAlarm = floodProtectClient.listPrevObj(vo);
        if (torrentOverAlarm.getCode() == 0){
            List<AttPrevTfBase> torrentOverResult = (List<AttPrevTfBase>) torrentOverAlarm.getData();
            if (CollectionUtils.isNotBlank(torrentOverResult)){
                List<AttPrevTfBase> collect = torrentOverResult.stream().filter(o -> "true".equals(o.getIsAlarm())).collect(Collectors.toList());
                result.put("torrentAlarmNum", collect.size());
            }else {
                result.put("torrentAlarmNum", 0);
            }
        }else {
            result.put("torrentAlarmNum", 0);
        }
        return result;
    }


    @Override
    public CommonResult reservoirArchivesCount() {
        return projectManageClient.reservoirArchivesCount();
    }

    @Override
    public CommonResult attStBaseCountByType(String type, String stType, Boolean isWrp) {
        return projectManageClient.attStBaseCountByType(type, stType, isWrp);
    }
}
