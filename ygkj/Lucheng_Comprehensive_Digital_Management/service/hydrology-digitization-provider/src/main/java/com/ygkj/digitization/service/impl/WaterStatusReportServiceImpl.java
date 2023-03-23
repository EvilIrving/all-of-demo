package com.ygkj.digitization.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.digitization.feign.GraghClient;
import com.ygkj.digitization.feign.ProjectManageClient;
import com.ygkj.digitization.mapper.AttAdcdBaseMapper;
import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.mapper.ProjectMapper;
import com.ygkj.digitization.mapper.SynRainAndWaterMapper;
import com.ygkj.digitization.model.ReservoirCapacityCurve;
import com.ygkj.digitization.service.WaterStatusReportService;
import com.ygkj.digitization.vo.request.MrwlCurveReqVo;
import com.ygkj.digitization.vo.request.MrwlReqVo;
import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.digitization.vo.request.WaterStatusReportReqVo;
import com.ygkj.digitization.vo.response.ZqCurveConfigResVo;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.vo.request.MultiWaterRainReqVo;
import com.ygkj.gragh.vo.request.RivernetWaterLevelReqVo;
import com.ygkj.gragh.vo.request.StationRequestVo;
import com.ygkj.gragh.vo.request.SynWaterRequestVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.gragh.vo.response.MultiWaterRainResVo;
import com.ygkj.gragh.vo.response.StPrrwtResVo;
import com.ygkj.project.vo.request.RsvrReplayReqVo;
import com.ygkj.project.vo.request.RsvrStationWaterReqVo;
import com.ygkj.project.vo.response.RsvrReplayResVo;
import com.ygkj.project.vo.response.RsvrStationWaterResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/6/8
 */
@Service("waterStatusReportServiceImpl")
public class WaterStatusReportServiceImpl implements WaterStatusReportService {

    @Autowired
    GraghClient graghClient;

    @Autowired
    ProjectManageClient projectManageClient;

    @Resource
    AttStBaseMapper stationMapper;

    @Resource
    SynRainAndWaterMapper rainAndWaterMapper;

    @Resource
    AttAdcdBaseMapper adcdBaseMapper;

    @Resource
    ProjectMapper projectMapper;


    final DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
    final long millis7d = 7 * 24 * 3600 * 1000;
    final long millis1d = 24 * 3600 * 1000;
    final long millisHalfDay = 12 * 3600 * 1000;
    final long millis1h = 3600 * 1000;
    final long millis5m = 5 * 60 * 1000;

    /**
     * 河网水位
     *
     * @return
     */
    @Override
    public CommonResult rivernetWaterLevel(Date dataTime, String name) {
        if (dataTime == null) {
            dataTime = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CommonResult<List<LinkedHashMap>> resp = graghClient.listRivernet(Builder.of(RivernetWaterLevelReqVo::new)
                .with(RivernetWaterLevelReqVo::setDataTime, sdf.format(dataTime))
                .with(RivernetWaterLevelReqVo::setMainStName, name)
                .build());
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            List<LinkedHashMap> data = resp.getData();
            Map<String, String> adcdMap = selectWzCounty();
            for (LinkedHashMap temp : data) {
                String areaCode = String.valueOf(temp.get("areaCode"));
                temp.put("areaName", adcdMap.get(areaCode));
            }
            data = data.stream().sorted(Comparator.comparing(o -> String.valueOf(o.get("areaName")))).collect(Collectors.toList());
            resp.setData(data);
        }
        return resp;
    }

    /**
     * 列出山区河道水位的流量曲线配置列表
     *
     * @return
     */
    @Override
    public Map<String, List<ZqCurveConfigResVo>> listMrwlZqCurveConfig() {
        /**
         * 1. 查出五个山区河道水位测站
         * 2. 然后根据测站编码去查询曲线
         */
        List<String> stCodeList = stationMapper.selectStCodeByMark("山区河道");
        if (CollectionUtils.isBlank(stCodeList)) {
            return Collections.emptyMap();
        }
        String stcds = String.join(StringUtils.JOIN_DELIMITER, stCodeList);
        List<com.ygkj.gragh.model.AttStBase> stBases = stationMapper.selectStationByCondition(Builder.of(StationRequestVo::new)
                .with(StationRequestVo::setStationCodes, stcds)
                .build());
        Map<String, String> stationMap = stBases.stream().collect(Collectors.toMap(AttStBase::getStCode, AttStBase::getStName));
        List<ZqCurveConfigResVo> configs = stationMapper.selectZqCurveConfig(stcds);
        for (ZqCurveConfigResVo config : configs) {
            config.setStName(stationMap.get(config.getMgstcd()));
        }
        Map<String, Optional<ZqCurveConfigResVo>> tempMap = configs.stream().collect(Collectors.groupingBy(ZqCurveConfigResVo::getMgstcd, Collectors.maxBy(Comparator.comparing(ZqCurveConfigResVo::getBgtm))));
        tempMap.forEach((key, value) -> {
            if (value.isPresent()) {
                ZqCurveConfigResVo temp = value.get();
                temp.setNewest(true);
            }
        });
        return configs.stream().collect(Collectors.groupingBy(ZqCurveConfigResVo::getStName));
    }

    /**
     * 山区河道水位流量曲线
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<StPrrwtResVo> mrwlCurve(MrwlCurveReqVo reqVo) {
        if (StringUtils.isEmpty(reqVo.getStCode())) {
            return Collections.emptyList();
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(reqVo.getLnnm())) {
            String lnnm = stationMapper.selectNewestLnnm(reqVo.getStCode());
            if (org.apache.commons.lang3.StringUtils.isBlank(lnnm)) {
                return Collections.emptyList();
            }
            reqVo.setLnnm(lnnm);
        }
        if (reqVo.getHours() == null) {
            reqVo.setHours(24);
        }
        Date startTime = null;
        Date endTime = null;
        if (reqVo.getDataTime() == null) {
            endTime = new Date();
            startTime = new Date(endTime.getTime() - reqVo.getHours() * millis1h);
        } else {
            startTime = DateUtils.clearDateTimePrecision(reqVo.getDataTime(), Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);
            endTime = new Date(startTime.getTime() + reqVo.getHours() * millis1h);
        }

        List<StPrrwtResVo> waterLevels = this.rainAndWaterMapper.selectRiverByTimeInterval(echoWaterRainTableByDateInternal(StationType.RIVER.table(), startTime, endTime), startTime, endTime, Arrays.asList(reqVo.getStCode()));
        if (CollectionUtils.isBlank(waterLevels)) {
            return Collections.emptyList();
        }
        List<StZqrlB> zqrlList = this.stationMapper.selectZqCurveBySingleStcdLnnm(reqVo.getStCode(), reqVo.getLnnm());
        for (StPrrwtResVo waterLevel : waterLevels) {
            BigDecimal flow = computeFlow(zqrlList, BigDecimal.valueOf(waterLevel.getZ()));
            if (flow != null) {
                waterLevel.setFlow(flow.doubleValue());
            }
        }
        return waterLevels;
    }

    /**
     * 山区河道水位
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<JSONObject> mountainRiverWaterLevel(MrwlReqVo reqVo) {
        Date dataTime = reqVo.getDataTime();
        // 先查询山区河道的测站
        List<String> stCodeList = stationMapper.selectStCodeByMark("山区河道");
        if (CollectionUtils.isBlank(stCodeList)) {
            return Collections.emptyList();
        }
        String stcds = String.join(StringUtils.JOIN_DELIMITER, stCodeList);
        CommonResult<List<AttStBase>> resp = graghClient.waterStationsBody(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.RIVER.type())
                .with(SynWaterRequestVo::setDataTime, dataTime)
                .with(SynWaterRequestVo::setStCodes, stcds)
                .with(SynWaterRequestVo::setAreaCode, reqVo.getAreaCode())
                .with(SynWaterRequestVo::setBasinCode, reqVo.getBas())
                .build());
        if (CollectionUtils.isBlank(reqVo.getLnnmMap())) {
            List<ZqCurveConfigResVo> configs = stationMapper.selectZqCurveConfig(stcds);
            if (CollectionUtils.isNotBlank(configs)) {
                Map<String, Optional<ZqCurveConfigResVo>> tempMap = configs.stream().collect(Collectors.groupingBy(ZqCurveConfigResVo::getMgstcd, Collectors.maxBy(Comparator.comparing(ZqCurveConfigResVo::getBgtm))));
                Map<String, String> lnnmMap = new HashMap<>(tempMap.size());
                tempMap.forEach((key, value) -> {
                    value.ifPresent(zqCurveConfigResVo -> lnnmMap.put(key, zqCurveConfigResVo.getLnnm()));
                });
                reqVo.setLnnmMap(lnnmMap);
            }
        }

        List<JSONObject> result = Collections.emptyList();
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            List<AttStBase> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData()), AttStBase.class);
            result = new ArrayList<>(data.size());
            Map<String, String> adcdMap = selectWzCounty();
            List<StZqrlB> zqrlList = this.stationMapper.selectZqCurveByStcdLnnm(reqVo.getLnnmMap());
            zqrlList = zqrlList == null ? Collections.emptyList() : zqrlList;
            Map<String, List<StZqrlB>> zqrlGroupMap = zqrlList.stream().collect(Collectors.groupingBy(StZqrlB::getMgstcd));
            for (AttStBase temp : data) {
                JSONObject e = JSONObject.parseObject(JSONObject.toJSONString(temp, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat));
                e.put("areaName", adcdMap.get(temp.getAreaCode()));
                e.put("flow", computeFlow(zqrlGroupMap.get(temp.getStCode()), BigDecimal.valueOf(temp.getWaterLevel())));
                e.put("flowOverWarn", false);
                result.add(e);
            }
            result = result.stream().sorted(Comparator.comparing(o -> String.valueOf(o.get("areaName")))).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 计算根据单站的库容曲线和水位求库容
     *
     * @param curveList         单站库容曲线记录
     * @param currentWaterLevel 水位
     * @return 库容
     */
    private BigDecimal computeFlow(List<StZqrlB> curveList, BigDecimal currentWaterLevel) {
        if (org.springframework.util.CollectionUtils.isEmpty(curveList) || currentWaterLevel == null) {
            return null;
        }
        // 将曲线按水位升序
        curveList = curveList.stream().sorted(Comparator.comparing(StZqrlB::getZ)).collect(Collectors.toList());

        StZqrlB before = null, after = null;
        for (StZqrlB reservoirCapacityCurve : curveList) {
            //水位直接找到相等的值，直接返回
            if (reservoirCapacityCurve.getZ().compareTo(currentWaterLevel) == 0) {
                return reservoirCapacityCurve.getQ();
            } else if (reservoirCapacityCurve.getZ().doubleValue() < currentWaterLevel.doubleValue()) {
                before = reservoirCapacityCurve;
            } else if (reservoirCapacityCurve.getZ().doubleValue() > currentWaterLevel.doubleValue() && after == null) {
                after = reservoirCapacityCurve;
            }
        }
        //当传入水位小于最小水位返回最小库容
        if (before == null) {
            return null;
        }
        //当传入水位大于最大水位返回最大库容
        if (after == null) {
            return null;
        }

        // a = (x1-x2)/(y1-y2)
        BigDecimal a = (before.getQ().subtract(after.getQ()))
                .divide(before.getZ().subtract(after.getZ()), 20, BigDecimal.ROUND_DOWN);
        // b =   y1 -a*x1
        BigDecimal b = before.getQ().subtract(a.multiply(before.getZ()));

        BigDecimal capacity = a.multiply(currentWaterLevel).add(b);
        //返回结果保留四位小数
        return capacity.setScale(4, BigDecimal.ROUND_HALF_DOWN);
    }


    /**
     * 河道水位统计表
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<JSONObject> riverWaterStats(Date start, Date end, String name) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }
        List<AttRnBase> rnList = this.stationMapper.selectRnByCondition(Builder.of(RivernetWaterLevelReqVo::new)
                .with(RivernetWaterLevelReqVo::setMainStName, name)
                .build());
        String stcds = rnList.stream().map(AttRnBase::getMainStCode).collect(Collectors.joining(StringUtils.JOIN_DELIMITER));
        CommonResult<List<LinkedHashMap>> resp = graghClient.multiWaterRains(Builder.of(MultiWaterRainReqVo::new)
                .with(MultiWaterRainReqVo::setStType, StationType.RIVER.type())
                .with(MultiWaterRainReqVo::setStart, start)
                .with(MultiWaterRainReqVo::setEnd, end)
//                .with(MultiWaterRainReqVo::setDisplay, true)
                .with(MultiWaterRainReqVo::setStcds, stcds)
                .build());
        List<JSONObject> result = Collections.emptyList();
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            List<MultiWaterRainResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData()), MultiWaterRainResVo.class);
            result = new ArrayList<>(data.size());
            Comparator<StPrrwtResVo> waterLevelComparator = (o1, o2) -> {
                if (o1.getZ() != null && o2.getZ() != null) {
                    return o1.getZ().compareTo(o2.getZ());
                } else if (o1.getZ() == null & o2.getZ() != null) {
                    return -1;
                } else if (o1.getZ() != null & o2.getZ() == null) {
                    return 1;
                } else {
                    return 0;
                }
            };
            Map<String, String> adcdMap = selectWzCounty();
            for (MultiWaterRainResVo temp : data) {
//                河道站名、开始水位、结束水位，平均水位、最高水位、最高水位出现时间、最低水位、最低水位出现时间。
                JSONObject e = new JSONObject();
                e.put("stName", temp.getStName());
                e.put("areaName", adcdMap.get(temp.getAreaCode()));
                Double startLevel = null;
                Double endLevel = null;
                Double avgLevel = null;
                Double highestLevel = null;
                Date highestLevelTm = null;
                Double lowestLevel = null;
                Date lowestLevelTm = null;
                List<StPrrwtResVo> waterRains = temp.getWaterRains();
                if (CollectionUtils.isNotBlank(waterRains)) {
                    waterRains = waterRains.stream().sorted(Comparator.comparing(StPrrwtResVo::getTm)).collect(Collectors.toList());
                    startLevel = waterRains.get(0).getZ();
                    endLevel = waterRains.get(waterRains.size() - 1).getZ();
                    OptionalDouble avgLevelOpt = waterRains.stream().filter(o -> o.getZ() != null).mapToDouble(StPrrwtResVo::getZ).average();
                    if (avgLevelOpt.isPresent()) {
                        avgLevel = avgLevelOpt.getAsDouble();
                    }
                    Optional<StPrrwtResVo> highestLevelOpt = waterRains.stream().max(waterLevelComparator);
                    Optional<StPrrwtResVo> lowestLevelOpt = waterRains.stream().min(waterLevelComparator);
                    if (highestLevelOpt.isPresent()) {
                        highestLevel = highestLevelOpt.get().getZ();
                        highestLevelTm = highestLevelOpt.get().getTm();
                    }
                    if (lowestLevelOpt.isPresent()) {
                        lowestLevel = lowestLevelOpt.get().getZ();
                        lowestLevelTm = lowestLevelOpt.get().getTm();
                    }
                }
                e.put("startLevel", startLevel);
                e.put("endLevel", endLevel);
                e.put("highestLevel", highestLevel);
                e.put("highestLevelTm", highestLevelTm);
                e.put("lowestLevel", lowestLevel);
                e.put("lowestLevelTm", lowestLevelTm);
                e.put("avgLevel", avgLevel);
                result.add(e);
            }
            result = result.stream().sorted(Comparator.comparing(o -> String.valueOf(o.get("areaName")))).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 河道日平均水位
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<JSONObject> riverDayAvgWater(Date start, Date end, String name) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }
        List<AttRnBase> rnList = this.stationMapper.selectRnByCondition(Builder.of(RivernetWaterLevelReqVo::new)
                .with(RivernetWaterLevelReqVo::setMainStName, name)
                .build());
        String stcds = rnList.stream().map(AttRnBase::getMainStCode).collect(Collectors.joining(StringUtils.JOIN_DELIMITER));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<JSONObject> result = Collections.emptyList();
        CommonResult<List<LinkedHashMap>> resp = graghClient.multiWaterRains(Builder.of(MultiWaterRainReqVo::new)
                .with(MultiWaterRainReqVo::setStType, StationType.RIVER.type())
                .with(MultiWaterRainReqVo::setStart, start)
                .with(MultiWaterRainReqVo::setEnd, end)
//                .with(MultiWaterRainReqVo::setDisplay, true)
                .with(MultiWaterRainReqVo::setStcds, stcds)
                .build());
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            List<MultiWaterRainResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData()), MultiWaterRainResVo.class);
            result = new ArrayList<>(data.size());
            Map<String, String> adcdMap = selectWzCounty();
            for (MultiWaterRainResVo temp : data) {
                List<StPrrwtResVo> waterRainsTemp = temp.getWaterRains();
                temp.setWaterRains(null);
                JSONObject tempResult = JSONObject.parseObject(JSONObject.toJSONString(temp, SerializerFeature.WriteMapNullValue));
                tempResult.put("areaName", adcdMap.get(temp.getAreaCode()));
                List<ChartResVo<String, Double>> avgValues = Collections.emptyList();
                if (CollectionUtils.isNotBlank(waterRainsTemp)) {
                    avgValues = new ArrayList<>(30);
                    Date tempDate = start;
                    Map<String, Double> map = waterRainsTemp.stream().collect(Collectors.groupingBy(o -> sdf.format(o.getTm()), Collectors.averagingDouble(StPrrwtResVo::getZ)));
                    while (!tempDate.after(end)) {
                        String key = sdf.format(tempDate);
                        Double avg = map.get(key);
                        avgValues.add(Builder.of(ChartResVo<String, Double>::new).with(ChartResVo::setKey, key).with(ChartResVo::setValue, avg == null ? null : Double.valueOf(String.format("%.2f", avg))).build());
                        tempDate = new Date(tempDate.getTime() + millis1d);
                    }
                }
                tempResult.put("avgValues", avgValues);
                result.add(tempResult);
            }
            result = result.stream().sorted(Comparator.comparing(o -> String.valueOf(o.get("areaName")))).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 河道水位超警表
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<JSONObject> riverOverWarn(Date start, Date end, String name) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }
        List<AttRnBase> rnList = this.stationMapper.selectRnByCondition(Builder.of(RivernetWaterLevelReqVo::new)
                .with(RivernetWaterLevelReqVo::setMainStName, name)
                .build());
        String stcds = rnList.stream().map(AttRnBase::getMainStCode).collect(Collectors.joining(StringUtils.JOIN_DELIMITER));

        // 所属区域、站名、最高水位（米）、出现时间、警戒水位（米）、超过警戒（米）、保证水位（米）、超过保证（米）
        CommonResult<List<LinkedHashMap>> resp = graghClient.multiWaterRains(Builder.of(MultiWaterRainReqVo::new)
                .with(MultiWaterRainReqVo::setStType, StationType.RIVER.type())
                .with(MultiWaterRainReqVo::setStart, start)
                .with(MultiWaterRainReqVo::setEnd, end)
//                .with(MultiWaterRainReqVo::setDisplay, true)
                .with(MultiWaterRainReqVo::setStcds, stcds)
                .build());
        List<JSONObject> result = Collections.emptyList();
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            List<MultiWaterRainResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData()), MultiWaterRainResVo.class);
            result = new ArrayList<>(data.size());
            Comparator<StPrrwtResVo> waterLevelComparator = (o1, o2) -> {
                if (o1.getZ() != null && o2.getZ() != null) {
                    return o1.getZ().compareTo(o2.getZ());
                } else if (o1.getZ() == null & o2.getZ() != null) {
                    return -1;
                } else if (o1.getZ() != null & o2.getZ() == null) {
                    return 1;
                } else {
                    return 0;
                }
            };
            Map<String, String> adcdMap = selectWzCounty();
            for (MultiWaterRainResVo temp : data) {
//                所属区域、站名、最高水位（米）、出现时间、警戒水位（米）、超过警戒（米）、保证水位（米）、超过保证（米）
                JSONObject e = new JSONObject();
                e.put("areaName", adcdMap.get(temp.getAreaCode()));
                e.put("stName", temp.getStName());
                Double highestLevel = null;
                Date highestLevelTm = null;
                Double warningLevel = temp.getWarningLevel();
                Double overWarning = null;
                Double guaranteeLevel = temp.getGuaranteeLevel();
                Double overGuarantee = null;
                List<StPrrwtResVo> waterRains = temp.getWaterRains();
                if (CollectionUtils.isNotBlank(waterRains)) {
                    waterRains = waterRains.stream().sorted(Comparator.comparing(StPrrwtResVo::getTm)).collect(Collectors.toList());
                    Optional<StPrrwtResVo> highestLevelOpt = waterRains.stream().max(waterLevelComparator);
                    if (highestLevelOpt.isPresent()) {
                        highestLevel = highestLevelOpt.get().getZ();
                        highestLevelTm = highestLevelOpt.get().getTm();
                        if (warningLevel != null && highestLevel > warningLevel) {
                            overWarning = highestLevel - warningLevel;
                        }
                        if (guaranteeLevel != null && highestLevel > guaranteeLevel) {
                            overGuarantee = highestLevel - guaranteeLevel;
                        }
                    }
                }
                e.put("highestLevel", highestLevel);
                e.put("highestLevelTm", highestLevelTm);
                e.put("warningLevel", warningLevel);
                e.put("overWarning", overWarning);
                e.put("guaranteeLevel", guaranteeLevel);
                e.put("overGuarantee", overGuarantee);
                result.add(e);
            }
            result = result.stream().sorted(Comparator.comparing(o -> String.valueOf(o.get("areaName")))).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 逐时潮位增水
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public Map<String, JSONObject> tideSubPerHour(Date start, Date end, String name) {
        if (start == null || end == null) {
            return Collections.emptyMap();
        }
        if ((end.getTime() - start.getTime()) > millis1d) {
            return Collections.emptyMap();
        }
        Map<String, String> tempMap = new HashMap<>();

        tempMap.put("SWZ_06896", "温州");
        tempMap.put("SWZ_10303", "灵昆");
        tempMap.put("SWZ_01252", "瑞安");
        tempMap.put("SWZ_01664", "鳌江");
        List<AttStBase> stationList = this.stationMapper.selectStationByCondition(Builder.of(StationRequestVo::new)
                .with(StationRequestVo::setStationCodes, String.join(StringUtils.JOIN_DELIMITER, tempMap.keySet()))
                .with(StationRequestVo::setStationType, StationType.TIDE.type())
                .with(StationRequestVo::setStationName, name)
                .build());
        if (org.springframework.util.CollectionUtils.isEmpty(stationList)) {
            return Collections.emptyMap();
        }
        Map<String, AttStBase> stationMap = stationList.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
        List<String> tables = echoWaterRainTableByDateInternal(com.ygkj.enums.StationType.TIDE.table(), start, end);
        List<StPrrwtResVo> waterLevelTempList = Collections.emptyList();
        List<String> stcds = stationList.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        if (!org.springframework.util.CollectionUtils.isEmpty(tables)) {
            waterLevelTempList = rainAndWaterMapper.selectTideByTimeInterval(tables, start, end, stcds);
        }
        waterLevelTempList = waterLevelTempList == null ? Collections.emptyList() : waterLevelTempList;

        List<StPrrwtResVo> astroTideTempList = rainAndWaterMapper.selectAstrotdByTimeInterval(DateUtils.clearDateTimePrecision(start, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND), end, stcds);
        astroTideTempList = astroTideTempList == null ? Collections.emptyList() : astroTideTempList;

        Map<String, List<StPrrwtResVo>> waterLevelMap = waterLevelTempList.stream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
        Map<String, List<StPrrwtResVo>> astroTideMap = astroTideTempList.stream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
//        List<JSONObject> tempList = new ArrayList<>(stationList.size());
        Map<String, JSONObject> resultMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH时");
        tempMap.forEach((stCode, value) -> {
            AttStBase stBase = stationMap.get(stCode);
            if (stBase != null) {
                JSONObject temp = JSONObject.parseObject(JSONObject.toJSONString(stBase, SerializerFeature.WriteMapNullValue));
                List<StPrrwtResVo> realTempList = waterLevelMap.get(stBase.getStCode());
                Map<String, List<StPrrwtResVo>> realMap = realTempList.stream().filter(o -> o.getTm() != null).collect(Collectors.groupingBy(o -> sdf.format(o.getTm())));
                List<StPrrwtResVo> astroTempList = astroTideMap.get(stBase.getStCode());
                Map<String, List<StPrrwtResVo>> astroMap = astroTempList.stream().collect(Collectors.groupingBy(o -> sdf.format(o.getTm())));
                Date tempDate = start;
                List<JSONObject> list = new ArrayList<>(24);
                while (!tempDate.after(end)) {
                    JSONObject e = new JSONObject();
                    String key = sdf.format(tempDate);
                    e.put("time", key);
                    List<StPrrwtResVo> realList = realMap.getOrDefault(key, Collections.emptyList());
                    List<StPrrwtResVo> astroList = astroMap.getOrDefault(key, Collections.emptyList());
                    realList = realList.stream().sorted(Comparator.comparing(StPrrwtResVo::getTm)).collect(Collectors.toList());
                    astroList = astroList.stream().sorted(Comparator.comparing(StPrrwtResVo::getTm)).collect(Collectors.toList());
                    StPrrwtResVo real = null;
                    StPrrwtResVo astro = null;
                    if (CollectionUtils.isNotBlank(realList)) {
                        real = realList.get(0);
                        e.put("realTdz", real.getTdz());
                        e.put("realTm", real.getTm());
                    } else {
                        e.put("realTdz", null);
                        e.put("realTm", null);
                    }
                    if (CollectionUtils.isNotBlank(astroList)) {
                        astro = astroList.get(0);
                        e.put("astroTdz", astro.getTdz());
                        e.put("astroTm", astro.getTm());
                    } else {
                        e.put("astroTdz", null);
                        e.put("astroTm", null);
                    }
                    if (real != null && astro != null) {
                        e.put("subTide", Double.valueOf(String.format("%.2f", real.getTdz() - astro.getTdz())));
                    } else {
                        e.put("subTide", null);
                    }
                    list.add(e);
                    tempDate = new Date(tempDate.getTime() + millis1h);
                }
                temp.put("list", list);
                resultMap.put(value, temp);
            }
        });
        return resultMap;
    }

    /**
     * 高潮位增水
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<JSONObject> highTideSub(Date start, Date end, String name) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }
        if ((end.getTime() - start.getTime()) > 10*millis1d) {
            return Collections.emptyList();
        }
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("SWZ_06896", "温州");
        tempMap.put("SWZ_10303", "灵昆");
        tempMap.put("SWZ_01252", "瑞安");
        tempMap.put("SWZ_01664", "鳌江");
        List<AttStBase> stationList = this.stationMapper.selectStationByCondition(Builder.of(StationRequestVo::new)
                .with(StationRequestVo::setStationCodes, String.join(StringUtils.JOIN_DELIMITER, tempMap.keySet()))
                .with(StationRequestVo::setStationType, StationType.TIDE.type())
                .build());
        if (org.springframework.util.CollectionUtils.isEmpty(stationList)) {
            return Collections.emptyList();
        }
        List<String> tables = echoWaterRainTableByDateInternal(com.ygkj.enums.StationType.TIDE.table(), start, end);
        List<StPrrwtResVo> waterLevelTempList = Collections.emptyList();
        List<String> stcds = stationList.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        if (!org.springframework.util.CollectionUtils.isEmpty(tables)) {
            waterLevelTempList = rainAndWaterMapper.selectTideByTimeInterval(tables, start, end, stcds);
        }
        waterLevelTempList = waterLevelTempList == null ? Collections.emptyList() : waterLevelTempList;

        Map<String, List<StPrrwtResVo>> waterLevelMap = waterLevelTempList.stream().filter(o -> o.getTdz() > 0).collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
        List<JSONObject> tempList = new ArrayList<>(stationList.size() * 10 * 2);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-a");
        Map<String, String> adcdMap = selectWzCounty();
        for (AttStBase stBase : stationList) {
            List<StPrrwtResVo> realTempList = waterLevelMap.get(stBase.getStCode());
            if (CollectionUtils.isBlank(realTempList)) {
                continue;
            }
            Map<String, Optional<StPrrwtResVo>> realMap = realTempList.stream().collect(Collectors.groupingBy(o -> sdf.format(o.getTm()), Collectors.maxBy(Comparator.comparing(StPrrwtResVo::getTdz))));
            Date tempDate = start;
            String areaName = adcdMap.get(stBase.getAreaCode());
            while (!tempDate.after(end)) {
                String key = sdf.format(tempDate);
                Optional<StPrrwtResVo> tideOpt = realMap.getOrDefault(key, Optional.empty());
                if (!tideOpt.isPresent()) {
                    tempDate = new Date(tempDate.getTime() + millisHalfDay);
                    continue;
                }
                JSONObject e = new JSONObject();
                e.put("stName", stBase.getStName());
                e.put("areaName", areaName);
                e.put("highestLevel", stBase.getHighestLevel());
                e.put("appearTime", stBase.getAppearTime());
                StPrrwtResVo real = tideOpt.get();
                e.put("realTdz", real.getTdz());
                e.put("realTm", real.getTm());
                Date astroStartTm = DateUtils.clearDateTimePrecision(real.getTm(), Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);
                Date astroEndTm = new Date(astroStartTm.getTime() + millis1h);
                List<StPrrwtResVo> astroList = rainAndWaterMapper.selectAstrotdBy2Timestamp(astroStartTm, astroEndTm, stBase.getStCode());
                if (CollectionUtils.isNotBlank(astroList)) {
                    StPrrwtResVo astro = astroList.get(0);
                    if (astroList.size() > 1) {
                        astro = astroList.get(0);
                        StPrrwtResVo next = astroList.get(1);
                        double avg = (next.getTdz() - astro.getTdz()) / 12;
                        int j = (int) (real.getTm().getTime() - astro.getTm().getTime()) / 300000;
                        astro.setTm(new Date(astro.getTm().getTime() + (long) j *millis5m));
                        astro.setTdz(Double.parseDouble(String.format("%.2f", astro.getTdz() + j * avg)));
                    }
                    e.put("astroTdz", astro.getTdz());
                    e.put("astroTm", astro.getTm());
                    e.put("subTide", Double.valueOf(String.format("%.2f", real.getTdz() - astro.getTdz())));
                } else {
                    e.put("subTide", null);
                    e.put("astroTdz", null);
                    e.put("astroTm", null);
                }
                tempList.add(e);
                tempDate = new Date(tempDate.getTime() + millisHalfDay);
            }
        }
        return tempList;
    }

    /**
     * 水库蓄水统计表
     *
     * @param dataTime
     * @return
     */
    @Override
    public List<RsvrStationWaterResVo> rsvrStorageStats(Date dataTime) {
        if (dataTime == null) {
            dataTime = new Date();
        }
        List<RsvrStationWaterResVo> result = new ArrayList<>(30);
        CommonResult<PageVO<LinkedHashMap>> resp = projectManageClient.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
                .with(RsvrStationWaterReqVo::setEngScales, String.join(StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc()))
                .with(RsvrStationWaterReqVo::setLimitStorageAbility, true)
                .with(RsvrStationWaterReqVo::setNormStorageAbility, true)
                .with(RsvrStationWaterReqVo::setStorageRatio, true)
                .with(RsvrStationWaterReqVo::setCompareHisAvgCapStat, true)
                .with(RsvrStationWaterReqVo::setDataTime, dataTime)
                .build());
        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData().getList())) {
            List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData().getList()), RsvrStationWaterResVo.class);
            List<RsvrStationWaterResVo> midData = new ArrayList<>(data.size());
            List<RsvrStationWaterResVo> largeData = new ArrayList<>(data.size());
            Map<String, String> adcdMap = selectWzCounty();
            for (RsvrStationWaterResVo temp : data) {
                temp.setAreaName(adcdMap.get(temp.getAreaCode()));
                if (EngScalEnum.MID.getDesc().equals(temp.getEngScal())) {
                    midData.add(temp);
                } else if (EngScalEnum.L1.getDesc().equals(temp.getEngScal()) || EngScalEnum.L2.getDesc().equals(temp.getEngScal())) {
                    largeData.add(temp);
                }
            }
            RsvrStationWaterResVo totalInfo = new RsvrStationWaterResVo();
            double totalCurrentStorage = data.stream().filter(o -> o.getWaterLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity).sum();
            double totalLimitStorage = data.stream().filter(o -> o.getLimitLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getLimitLevelCapacity).sum();
            double totalNormStorage = data.stream().filter(o -> o.getWaterLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity).sum();
            double totalAvgStorageRatio = 0D;
            if (totalLimitStorage > 0) {
                totalAvgStorageRatio = totalCurrentStorage * 100 / totalLimitStorage;
            }
            totalInfo.setResName(data.size() + "座");
            totalInfo.setAreaName("合计");
            totalInfo.setWaterLevelCapacity(Double.valueOf(String.format("%.2f", totalCurrentStorage)));
            totalInfo.setLimitLevelCapacity(Double.valueOf(String.format("%.2f", totalLimitStorage)));
            totalInfo.setNormWatLevCapacity(Double.valueOf(String.format("%.2f", totalNormStorage)));
            totalInfo.setStorageRatio(Double.valueOf(String.format("%.2f", totalAvgStorageRatio)));
            totalInfo.setAreaName("合计");
            RsvrStationWaterResVo midInfo = new RsvrStationWaterResVo();
            midInfo.setResName("小计（" + midData.size() + "座）");
            totalCurrentStorage = midData.stream().filter(o -> o.getWaterLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity).sum();
            totalLimitStorage = midData.stream().filter(o -> o.getLimitLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getLimitLevelCapacity).sum();
            totalNormStorage = midData.stream().filter(o -> o.getWaterLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity).sum();
            totalAvgStorageRatio = 0D;
            if (totalLimitStorage > 0) {
                totalAvgStorageRatio = totalCurrentStorage * 100 / totalLimitStorage;
            }
            midInfo.setWaterLevelCapacity(Double.valueOf(String.format("%.2f", totalCurrentStorage)));
            midInfo.setLimitLevelCapacity(Double.valueOf(String.format("%.2f", totalLimitStorage)));
            midInfo.setNormWatLevCapacity(Double.valueOf(String.format("%.2f", totalNormStorage)));
            midInfo.setStorageRatio(Double.valueOf(String.format("%.2f", totalAvgStorageRatio)));
            result.addAll(midData.stream().sorted(Comparator.comparing(RsvrStationWaterResVo::getAreaName)).collect(Collectors.toList()));
            result.add(midInfo);
            RsvrStationWaterResVo largeInfo = new RsvrStationWaterResVo();
            largeInfo.setResName("小计（" + largeData.size() + "座）");
            totalCurrentStorage = largeData.stream().filter(o -> o.getWaterLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity).sum();
            totalLimitStorage = largeData.stream().filter(o -> o.getLimitLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getLimitLevelCapacity).sum();
            totalNormStorage = largeData.stream().filter(o -> o.getWaterLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity).sum();
            totalAvgStorageRatio = 0D;
            if (totalLimitStorage > 0) {
                totalAvgStorageRatio = totalCurrentStorage * 100 / totalLimitStorage;
            }
            largeInfo.setWaterLevelCapacity(Double.valueOf(String.format("%.2f", totalCurrentStorage)));
            largeInfo.setLimitLevelCapacity(Double.valueOf(String.format("%.2f", totalLimitStorage)));
            largeInfo.setNormWatLevCapacity(Double.valueOf(String.format("%.2f", totalNormStorage)));
            largeInfo.setStorageRatio(Double.valueOf(String.format("%.2f", totalAvgStorageRatio)));
            result.addAll(largeData.stream().sorted(Comparator.comparing(RsvrStationWaterResVo::getAreaName)).collect(Collectors.toList()));
            result.add(largeInfo);
            result.add(totalInfo);
        }
        return result;
    }

    /**
     * 水库水位统计表
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<RsvrReplayResVo> rsvrWaterStats(Date start, Date end) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<RsvrReplayResVo> result = new ArrayList<>(30);
        CommonResult<PageVO<LinkedHashMap>> startResp = projectManageClient.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
                .with(RsvrStationWaterReqVo::setEngScales, String.join(StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc()))
                .with(RsvrStationWaterReqVo::setLimitStorageAbility, true)
                .with(RsvrStationWaterReqVo::setNormStorageAbility, true)
                .with(RsvrStationWaterReqVo::setDataTime, start)
                .build());

        CommonResult<LinkedHashMap> replayResp = projectManageClient.rsvrReplayStats(Builder.of(RsvrReplayReqVo::new)
                .with(RsvrReplayReqVo::setEngScales, String.join(StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc()))
                .with(RsvrReplayReqVo::setStartWaterLevelCapacity, true)
                .with(RsvrReplayReqVo::setEndWaterLevelCapacity, true)
                .with(RsvrReplayReqVo::setHighestWaterLevelCapacity, true)
                .with(RsvrReplayReqVo::setStart, sdf1.format(start))
                .with(RsvrReplayReqVo::setEnd, sdf1.format(end))
                .build());
        Map<String, RsvrStationWaterResVo> map = Collections.emptyMap();
        if (startResp != null && startResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(startResp.getData().getList())) {
            List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(startResp.getData().getList()), RsvrStationWaterResVo.class);
            map = data.stream().collect(Collectors.toMap(RsvrStationWaterResVo::getResCode, Function.identity()));
        }
        if (replayResp != null && replayResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank((Collection) replayResp.getData().get("list"))) {
            List<RsvrReplayResVo> data = JSONArray.parseArray(JSONArray.toJSONString(replayResp.getData().get("list")), RsvrReplayResVo.class);
            Map<String, String> adcdMap = selectWzCounty();
            List<RsvrReplayResVo> midData = new ArrayList<>(data.size());
            List<RsvrReplayResVo> largeData = new ArrayList<>(data.size());
            for (RsvrReplayResVo temp : data) {
                temp.setAreaName(adcdMap.get(temp.getAreaCode()));
                RsvrStationWaterResVo t1 = map.get(temp.getResCode());
                if (t1 != null) {
                    temp.setLimitLevelCapacity(t1.getLimitLevelCapacity());
                    temp.setCurrentLimitLevel(t1.getCurrentLimitLevel());
                    temp.setNormWatLevCapacity(t1.getNormWatLevCapacity());
                }
                if (EngScalEnum.MID.getDesc().equals(temp.getEngScal())) {
                    midData.add(temp);
                } else if (EngScalEnum.L1.getDesc().equals(temp.getEngScal()) || EngScalEnum.L2.getDesc().equals(temp.getEngScal())) {
                    largeData.add(temp);
                }
            }
            RsvrReplayResVo totalInfo = new RsvrReplayResVo();
            double totalStartStorage = data.stream().filter(o -> o.getStartCapacity() != null).mapToDouble(RsvrReplayResVo::getStartCapacity).sum();
            double totalEndStorage = data.stream().filter(o -> o.getEndCapacity() != null).mapToDouble(RsvrReplayResVo::getEndCapacity).sum();
            double totalHighestStorage = data.stream().filter(o -> o.getHighestCapacity() != null).mapToDouble(RsvrReplayResVo::getHighestCapacity).sum();
            double totalStartEndSubStorage = data.stream().filter(o -> o.getStartEndCapacitySub() != null).mapToDouble(RsvrReplayResVo::getStartEndCapacitySub).sum();
            double totalLimitStorage = data.stream().filter(o -> o.getLimitLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getLimitLevelCapacity).sum();
            double totalNormStorage = data.stream().filter(o -> o.getNormWatLevCapacity() != null).mapToDouble(RsvrStationWaterResVo::getNormWatLevCapacity).sum();

            totalInfo.setResName(data.size() + "座");
            totalInfo.setAreaName("合计");
            totalInfo.setStartCapacity(Double.valueOf(String.format("%.2f", totalStartStorage)));
            totalInfo.setEndCapacity(Double.valueOf(String.format("%.2f", totalEndStorage)));
            totalInfo.setHighestCapacity(Double.valueOf(String.format("%.2f", totalHighestStorage)));
            totalInfo.setStartEndCapacitySub(Double.valueOf(String.format("%.2f", totalStartEndSubStorage)));
            totalInfo.setNormWatLevCapacity(Double.valueOf(String.format("%.2f", totalNormStorage)));
            totalInfo.setLimitLevelCapacity(Double.valueOf(String.format("%.2f", totalLimitStorage)));

            RsvrReplayResVo midInfo = new RsvrReplayResVo();
            totalStartStorage = midData.stream().filter(o -> o.getStartCapacity() != null).mapToDouble(RsvrReplayResVo::getStartCapacity).sum();
            totalEndStorage = midData.stream().filter(o -> o.getEndCapacity() != null).mapToDouble(RsvrReplayResVo::getEndCapacity).sum();
            totalHighestStorage = midData.stream().filter(o -> o.getHighestCapacity() != null).mapToDouble(RsvrReplayResVo::getHighestCapacity).sum();
            totalStartEndSubStorage = midData.stream().filter(o -> o.getStartEndCapacitySub() != null).mapToDouble(RsvrReplayResVo::getStartEndCapacitySub).sum();
            totalLimitStorage = midData.stream().filter(o -> o.getLimitLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getLimitLevelCapacity).sum();
            totalNormStorage = midData.stream().filter(o -> o.getNormWatLevCapacity() != null).mapToDouble(RsvrStationWaterResVo::getNormWatLevCapacity).sum();

            midInfo.setResName("小计（" + midData.size() + "座）");
            midInfo.setStartCapacity(Double.valueOf(String.format("%.2f", totalStartStorage)));
            midInfo.setEndCapacity(Double.valueOf(String.format("%.2f", totalEndStorage)));
            midInfo.setHighestCapacity(Double.valueOf(String.format("%.2f", totalHighestStorage)));
            midInfo.setStartEndCapacitySub(Double.valueOf(String.format("%.2f", totalStartEndSubStorage)));
            midInfo.setNormWatLevCapacity(Double.valueOf(String.format("%.2f", totalNormStorage)));
            midInfo.setLimitLevelCapacity(Double.valueOf(String.format("%.2f", totalLimitStorage)));
            Comparator<RsvrReplayResVo> comparator = (o1, o2) -> {
                if (o1.getAreaName() != null && o2.getAreaName() != null) {
                    return o1.getAreaName().compareTo(o2.getAreaName());
                } else if (o1.getAreaName() == null & o2.getAreaName() != null) {
                    return -1;
                } else if (o1.getAreaName() != null & o2.getAreaName() == null) {
                    return 1;
                } else {
                    return 0;
                }
            };
            result.addAll(midData.stream().sorted(comparator).collect(Collectors.toList()));
            result.add(midInfo);

            RsvrReplayResVo largeInfo = new RsvrReplayResVo();
            totalStartStorage = largeData.stream().filter(o -> o.getStartCapacity() != null).mapToDouble(RsvrReplayResVo::getStartCapacity).sum();
            totalEndStorage = largeData.stream().filter(o -> o.getEndCapacity() != null).mapToDouble(RsvrReplayResVo::getEndCapacity).sum();
            totalHighestStorage = largeData.stream().filter(o -> o.getHighestCapacity() != null).mapToDouble(RsvrReplayResVo::getHighestCapacity).sum();
            totalStartEndSubStorage = largeData.stream().filter(o -> o.getStartEndCapacitySub() != null).mapToDouble(RsvrReplayResVo::getStartEndCapacitySub).sum();
            totalLimitStorage = largeData.stream().filter(o -> o.getLimitLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getLimitLevelCapacity).sum();
            totalNormStorage = largeData.stream().filter(o -> o.getNormWatLevCapacity() != null).mapToDouble(RsvrStationWaterResVo::getNormWatLevCapacity).sum();

            largeInfo.setResName("小计（" + largeData.size() + "座）");
            largeInfo.setStartCapacity(Double.valueOf(String.format("%.2f", totalStartStorage)));
            largeInfo.setEndCapacity(Double.valueOf(String.format("%.2f", totalEndStorage)));
            largeInfo.setHighestCapacity(Double.valueOf(String.format("%.2f", totalHighestStorage)));
            largeInfo.setStartEndCapacitySub(Double.valueOf(String.format("%.2f", totalStartEndSubStorage)));
            largeInfo.setNormWatLevCapacity(Double.valueOf(String.format("%.2f", totalNormStorage)));
            largeInfo.setLimitLevelCapacity(Double.valueOf(String.format("%.2f", totalLimitStorage)));
            result.addAll(largeData.stream().sorted(comparator).collect(Collectors.toList()));
            result.add(largeInfo);
            result.add(totalInfo);
        }
        return result;
    }

    /**
     * 水库水位时段表
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<LinkedHashMap> rsvrWaterTimeInterval(Date start, Date end, String name) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<LinkedHashMap> result = new ArrayList<>(30);
        CommonResult<LinkedHashMap> replayResp = projectManageClient.rsvrReplayStats(Builder.of(RsvrReplayReqVo::new)
                .with(RsvrReplayReqVo::setEngScales, String.join(StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc()))
                .with(RsvrReplayReqVo::setResName, name)
                .with(RsvrReplayReqVo::setStart, sdf1.format(start))
                .with(RsvrReplayReqVo::setEnd, sdf1.format(end))
                .with(RsvrReplayReqVo::setWaterLevelCurve, true)
                .build());
        SimpleDateFormat sdf = new SimpleDateFormat("dd日HH时");
        if (replayResp != null && replayResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank((Collection) replayResp.getData().get("list"))) {
            List<RsvrReplayResVo> data = JSONArray.parseArray(JSONArray.toJSONString(replayResp.getData().get("list")), RsvrReplayResVo.class);
            Map<String, String> adcdMap = selectWzCounty();
            for (RsvrReplayResVo temp : data) {
                LinkedHashMap e = new LinkedHashMap();
                e.put("resName", temp.getResName());
                e.put("resCode", temp.getResCode());
                e.put("areaName", adcdMap.get(temp.getAreaCode()));

                List<StRsvrR> waterLevelList = temp.getWaterLevelList();
                Map<String, List<StRsvrR>> map = waterLevelList.stream().collect(Collectors.groupingBy(o -> sdf.format(o.getTm())));
                Date tempDate = start;
                while (!tempDate.after(end)) {
                    String key = sdf.format(tempDate);
                    List<StRsvrR> realList = map.getOrDefault(key, Collections.emptyList());
                    realList = realList.stream().sorted(Comparator.comparing(StRsvrR::getTm)).collect(Collectors.toList());
                    if (CollectionUtils.isNotBlank(realList)) {
                        e.put(key, realList.get(0).getRz());
                    } else {
                        e.put(key, null);
                    }
                    tempDate = new Date(tempDate.getTime() + millis1h);
                }
                result.add(e);
            }
        }
        return result;
    }

    /**
     * 水库水位超讯表
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<JSONObject> rsvrWaterOverLimit(Date start, Date end, String name) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<JSONObject> result = new ArrayList<>(30);
        CommonResult<LinkedHashMap> replayResp = projectManageClient.rsvrReplayStats(Builder.of(RsvrReplayReqVo::new)
                .with(RsvrReplayReqVo::setEngScales, String.join(StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc()))
                .with(RsvrReplayReqVo::setResName, name)
                .with(RsvrReplayReqVo::setStart, sdf1.format(start))
                .with(RsvrReplayReqVo::setEnd, sdf1.format(end))
                .build());
        if (replayResp != null && replayResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank((Collection) replayResp.getData().get("list"))) {
            List<RsvrReplayResVo> data = JSONArray.parseArray(JSONArray.toJSONString(replayResp.getData().get("list")), RsvrReplayResVo.class);
            Map<String, String> adcdMap = selectWzCounty();
            for (RsvrReplayResVo temp : data) {
                Double highestWaterLevel = temp.getHighestWaterLevel();
                if (highestWaterLevel == null) {
                    continue;
                }
                JSONObject e = new JSONObject();
                e.put("resName", temp.getResName());
                e.put("resCode", temp.getResCode());
                e.put("highestWaterLevel", highestWaterLevel);
                e.put("highestWaterLevelTm", temp.getHighestWaterLevelTm());
                BigDecimal limitWaterLevel = temp.getCurrentLimitLevel();
                e.put("limitWaterLevel", limitWaterLevel);
                if (limitWaterLevel != null && highestWaterLevel > limitWaterLevel.doubleValue()) {
                    e.put("overLimitVal", highestWaterLevel - limitWaterLevel.doubleValue());

                } else {
                    e.put("overLimitVal", null);
                }
                BigDecimal normWaterLevel = temp.getNormWatLev();
                e.put("normWaterLevel", normWaterLevel);
                if (normWaterLevel != null && highestWaterLevel > normWaterLevel.doubleValue()) {
                    e.put("overNormVal", highestWaterLevel - normWaterLevel.doubleValue());
                } else {
                    e.put("overNormVal", null);
                }
                e.put("areaName", adcdMap.get(temp.getAreaCode()));
                result.add(e);
            }
        }
        return result;
    }


    List<String> echoWaterRainTableByDateInternal(String tablePrefix, Date start, Date end) {
        if (org.apache.commons.lang3.StringUtils.isBlank(tablePrefix) || start == null || end == null) {
            return Collections.emptyList();
        }
        LocalDate startTemp = DateUtils.date2LocalDate(start);
        LocalDate endTemp = DateUtils.date2LocalDate(end);
        LinkedHashSet<String> sequenceSet = new LinkedHashSet<>();
        do {
            String table = tablePrefix + startTemp.format(yyyyMM);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(this.rainAndWaterMapper.checkTableExist(table))) {
                sequenceSet.add(table);
            }
            startTemp = startTemp.plusDays(1);
        } while (startTemp.isBefore(endTemp));
        return new ArrayList<>(sequenceSet);
    }


    private Map<String, String> selectWzCounty() {
        List<AttAdcdBase> records = this.adcdBaseMapper.selectWZCounty();
        if (org.springframework.util.CollectionUtils.isEmpty(records)) {
            return Collections.emptyMap();
        }
        return records.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
    }

    /**
     * 水库水雨情过程表
     *
     * @param start   起始时刻，默认为前24小时
     * @param end     截止时刻，默认为当前时刻
     * @param resCode 水库编码
     * @return
     */
    @Override
    public List<JSONObject> rsvrWaterRainInfo(Date start, Date end, String resCode) {
        if (StringUtils.isBlank(resCode)) {
            return Collections.emptyList();
        }
        if (end == null) {
            end = new Date();
        }
        if (start == null) {
            start = new Date(end.getTime() - millis1d);
        }
        if (end.getTime() - start.getTime() > millis7d) {
            start = new Date(end.getTime() - millis7d);
        }
        // 1. 根据resCode 找到mainStcd
        String mainStcd = projectMapper.selectMainStcdOfRsvr(resCode);
        Map<Date, Double> levelMap = Collections.emptyMap();
        Map<Date, Double> capacityMap = Collections.emptyMap();
        if (StringUtils.isNotBlank(mainStcd)) {
            // 2. 根据mainStcd查询每小时整点水位
            List<StRsvrR> levelList = this.rainAndWaterMapper.selectHourRsvr(this.echoWaterRainTableByDateInternal(StationType.RESERVOIR.table(), start, end), start, end, mainStcd);
            // 3. 根据mainStcd查询库容曲线
            List<ReservoirCapacityCurve> curveList = this.rainAndWaterMapper.selectRsvrCapcityCurveAsc(mainStcd);
            // 4. 根据库容曲线和整点水位计算实时库容
            levelMap = new HashMap<>(levelList.size());
            capacityMap = new HashMap<>(levelList.size());
            for (StRsvrR rsvr : levelList) {
                Date tempDate = DateUtils.clearDateTimePrecision(rsvr.getTm(), Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);
                BigDecimal capacity = this.computeCapacity(curveList, BigDecimal.valueOf(rsvr.getRz()));
                levelMap.put(tempDate, rsvr.getRz());
                capacityMap.put(tempDate, capacity == null ? null : capacity.doubleValue());
            }
        }
        // 5. 根据resCode,查询关联的雨量站proj_code=resCode and is_rain_station=1 and is_reservoir_surface_rain_station=1
        List<AttStBase> stationList = this.stationMapper.selectStationByCondition(Builder.of(StationRequestVo::new).with(StationRequestVo::setProjectCodes, resCode).with(StationRequestVo::setIsRainStation, true).with(StationRequestVo::setIsReservoirSurfaceRainStation, true).build());

        List<StPptnR> rainfallList = new ArrayList<>();
        Map<Date, String> rainfallMap = new HashMap<>(72);
        if (CollectionUtils.isNotBlank(stationList)) {
            // 6. 根据雨量站st_code 查询降雨
            List<String> codes = stationList.stream().filter(attStBase -> attStBase.getStCode().startsWith("SWZ_"))
                    .map(AttStBase::getStCode).collect(Collectors.toList());
            List<String> yjCodes = stationList.stream().filter(attStBase -> attStBase.getStCode().startsWith("YJ_"))
                    .map(attStBase -> attStBase.getStCode().replaceAll("YJ_", "")).collect(Collectors.toList());
            if (CollectionUtils.isNotBlank(codes)) {
                List<StPptnR> rainfalls = this.rainAndWaterMapper.hourRainfall(this.echoWaterRainTableByDateInternal(StationType.RAINFALL.table(), start, end),
                        start, end,
                        codes);
                rainfallList.addAll(rainfalls);
            }
            if (CollectionUtils.isNotBlank(yjCodes)) {
                List<StPptnR> yjRainfalls = this.rainAndWaterMapper.hourYjRainfall(
                        start, end,
                        yjCodes);
                rainfallList.addAll(yjRainfalls);
            }
            // 7. 计算每小时的面雨量
            int size = stationList.size();
            rainfallList.stream().collect(Collectors.groupingBy(StPptnR::getTm)).forEach((date, groupRainfalls) -> {
                rainfallMap.put(date, String.format("%.2f", groupRainfalls.stream().mapToDouble(StPptnR::getDrp).sum() / size));
            });
        }

        Date tempDate = DateUtils.clearDateTimePrecision(start, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);
        if (tempDate.before(start)) {
            tempDate = new Date(tempDate.getTime() + millis1h);
        }
        List<JSONObject> result = new ArrayList<>(72);
        while (!tempDate.after(end)) {
            JSONObject tempObj = new JSONObject();
            tempObj.put("tm", tempDate);
            tempObj.put("surfaceRainfall", rainfallMap.get(tempDate));
            tempObj.put("waterLevel", levelMap.get(tempDate));
            tempObj.put("capacity", capacityMap.get(tempDate));
            result.add(tempObj);
            tempDate = new Date(tempDate.getTime() + millis1h);
        }
        return result;
    }

    /**
     * 计算根据单站的库容曲线和水位求库容
     *
     * @param curveList         单站库容曲线记录
     * @param currentWaterLevel 水位
     * @return 库容
     */
    private BigDecimal computeCapacity(List<ReservoirCapacityCurve> curveList, BigDecimal currentWaterLevel) {
        if (org.springframework.util.CollectionUtils.isEmpty(curveList) || currentWaterLevel == null) {
            return null;
        }
        // 将曲线按水位升序
        curveList = curveList.stream().sorted(Comparator.comparing(ReservoirCapacityCurve::getWaterLevel)).collect(Collectors.toList());

        ReservoirCapacityCurve before = null, after = null;
        for (ReservoirCapacityCurve reservoirCapacityCurve : curveList) {
            //水位直接找到相等的值，直接返回
            if (reservoirCapacityCurve.getWaterLevel().doubleValue() == currentWaterLevel.doubleValue()) {
                return reservoirCapacityCurve.getCapacity();
            } else if (reservoirCapacityCurve.getWaterLevel().doubleValue() < currentWaterLevel.doubleValue()) {
                before = reservoirCapacityCurve;
            } else if (reservoirCapacityCurve.getWaterLevel().doubleValue() > currentWaterLevel.doubleValue() && after == null) {
                after = reservoirCapacityCurve;
            }
        }
        //当传入水位小于最小水位返回最小库容
        if (before == null) {
            return after.getCapacity();
        }
        //当传入水位大于最大水位返回最大库容
        if (after == null) {
            return before.getCapacity();
        }

        // a = (x1-x2)/(y1-y2)
        BigDecimal a = (before.getCapacity().subtract(after.getCapacity()))
                .divide(before.getWaterLevel().subtract(after.getWaterLevel()), 20, BigDecimal.ROUND_DOWN);
        // b =   y1 -a*x1
        BigDecimal b = before.getCapacity().subtract(a.multiply(before.getWaterLevel()));

        BigDecimal capacity = a.multiply(currentWaterLevel).add(b);
        //返回结果保留四位小数
        return capacity.setScale(4, BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * 河网水位-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void rivernetWaterLevelExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }

    /**
     * 河道水位统计表-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void riverWaterStatsExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }

    /**
     * 河道日平均水位-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void riverDayAvgWaterExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }

    /**
     * 河道水位超警表-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void riverOverWarnExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }

    /**
     * 逐时潮位增水-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void tideSubPerHourExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }

    /**
     * 高潮位增水-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void highTideSubExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }

    /**
     * 水库蓄水统计表-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void rsvrStorageStatsExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }

    /**
     * 水库水位统计表-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void rsvrWaterStatsExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }

    /**
     * 水库水位时段表-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void rsvrWaterTimeIntervalExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }

    /**
     * 水库水位超讯表-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void rsvrWaterOverLimitExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }

    /**
     * 水库水雨情过程表-导出
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void rsvrWaterRainInfoExport(WaterStatusReportReqVo reqVo, HttpServletResponse response) {

    }
}
