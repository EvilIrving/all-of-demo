package com.ygkj.digitization.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.mapper.SynRainAndWaterMapper;
import com.ygkj.digitization.service.RainAndWaterService;
import com.ygkj.digitization.enums.MonthDaysType;
import com.ygkj.digitization.enums.StationType;
import com.ygkj.digitization.enums.StatisticType;
import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.mapper.ProjectMapper;
import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.*;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("rainAndWaterService")
public class RainAndWaterServiceImpl implements RainAndWaterService {

    @Autowired
    private AttStBaseMapper stBaseMapper;
    @Autowired
    private SynRainAndWaterMapper rainAndWaterMapper;
    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public JSONObject rainStationSynchronizedStatistic(StationQueryVo queryVo) {
        JSONObject result = new JSONObject();
        List<RainfallStationResVo> list = stBaseMapper.selectByCondition(queryVo);
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String table = StationType.RAINFALL.table() + year + (month < 10 ? "0" + month : "" + month);
        calendar.add(Calendar.HOUR, -12);
        Date start = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<HourRainfallResVo> rainfalls = rainAndWaterMapper.pastTwelveHourRainfall(table,
                format.format(start), format.format(now),
                list.stream().map(RainfallStationResVo::getStCode).collect(Collectors.toList()));
        Map<String, List<HourRainfallResVo>> map = rainfalls.stream().collect(Collectors.groupingBy(HourRainfallResVo::getMgstcd));
        for (RainfallStationResVo station : list) {
            String stationCode = station.getStCode();
            List<JSONObject> rainfall = new ArrayList<>();
            if (map.containsKey(stationCode)) {
                List<HourRainfallResVo> hourRainfall = map.get(stationCode);
                for (HourRainfallResVo vo : hourRainfall) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("hour", vo.getTm().split(" ")[1]);
                    jsonObject.put("total", vo.getTotal());
                    rainfall.add(jsonObject);
                }
            }
            station.setRainfalls(rainfall);
        }
        result.put("rainfall", list);
        calendar.setTime(now);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int i = 0;
        int[] ints = new int[12];
        while (i < 12) {
            ints[i] = hour - i >= 0 ? hour - i : 24 - (12 - i);
            i++;
        }
        result.put("hours", ints);
        return result;
    }

    @Override
    public List<WaterLevelStationResVo> waterLevelStationSynchronizedStatistic(StationQueryVo queryVo) {
        List<WaterLevelStationResVo> list = stBaseMapper.selectWaterLevelStationByCondition(queryVo);
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        int month = calendar.get(Calendar.MONTH) + 1;
        String table = "";
        List<WaterLevelResVo> waterLevels = new ArrayList<>();
        if (queryVo.getStationType().equals(StationType.RESERVOIR.type())) {
            //水库水位站
            table = StationType.RESERVOIR.table() + calendar.get(Calendar.YEAR) + (month < 10 ? "0" + month : "" + month);
            waterLevels = rainAndWaterMapper.selectNewestReserviorWaterLevelFromTable(table,Collections.emptyList());
        } else if (queryVo.getStationType().equals(StationType.RIVER.type())) {
            //河道水位站
            table = StationType.RIVER.table() + calendar.get(Calendar.YEAR) + (month < 10 ? "0" + month : "" + month);
            waterLevels = rainAndWaterMapper.selectNewestRiverLevelFromTable(table);
        } else {
            //堰闸，暂时没有堰闸的水位信息
        }
        if (!waterLevels.isEmpty()) {
            Map<String, List<WaterLevelResVo>> map = waterLevels.stream().collect(Collectors.groupingBy(WaterLevelResVo::getStcd));
            for (WaterLevelStationResVo station : list) {
                String stationCode = station.getStCode();
                if (map.containsKey(stationCode)) {
                    WaterLevelResVo waterLevel = map.get(stationCode).get(0);
                    station.setWaterLevels(waterLevel.getRz() == null ? 0d : Double.parseDouble(waterLevel.getRz()));
                    station.setTm(waterLevel.getTm());
                }
            }
        }
        if (queryVo.getStationType().equals(StationType.RESERVOIR.type())) {
            //水库关联其他信息
            List<ReservoirResVo> reservoirs = projectMapper.selectReservoirs(StringUtils.EMPTY,StringUtils.EMPTY);
            Map<String, List<ReservoirResVo>> map = reservoirs.stream().collect(Collectors.groupingBy(ReservoirResVo::getResCode));
            for (WaterLevelStationResVo station : list) {
                String projectCode = station.getProjectCode();
                if (map.containsKey(projectCode)) {
                    ReservoirResVo reservoir = map.get(projectCode).get(0);
                    station.setMeiFloodLimitWaterLevel(reservoir.getMeiFloodLimitWaterLevel());
                    station.setYphoonFloodLimitWaterLevel(reservoir.getYphoonFloodLimitWaterLevel());
                    station.setNormWatLev(reservoir.getNormWatLev());
                    station.setUppLevFlco(reservoir.getUppLevFlco());
                }
            }
        }
        list=list.stream().sorted((o1, o2) -> {
            BigDecimal a1=o1.getNormWatLev();
            BigDecimal a2=o2.getNormWatLev();
            if (a1!=null&&a2!=null){
                return -a1.compareTo(a2);
            }else if (a1==null&a2!=null){
                return 1;
            }else if(a1!=null&a2==null){
                return -1;
            }else {
                return 0;
            }
        }).collect(Collectors.toList());
//        list = list.stream().filter(vo -> StringUtils.isNotBlank(vo.getProjectCode())).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<GateStationResVo> gateStationSynchronizedStatistic(StationQueryVo queryVo) {
        if (StringUtils.isEmpty(queryVo.getStationType())){
            queryVo.setStationType("泵站,水闸");
        }
        List<GateStationResVo> list = stBaseMapper.gateStations(queryVo);
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        int month = calendar.get(Calendar.MONTH) + 1;
        String table = StationType.GATE.table() + calendar.get(Calendar.YEAR) + (month < 10 ? "0" + month : "" + month);
        if (!list.isEmpty()) {
            List<GateOpenResVo> gates = rainAndWaterMapper.selectNewestWas(table);
            Map<String, List<GateOpenResVo>> map = gates.stream().collect(Collectors.groupingBy(GateOpenResVo::getStcd));
            for (GateStationResVo vo : list) {
                String code = vo.getCode();
                if (map.containsKey(code)) {
                    GateOpenResVo gateOpenResVo = map.get(code).get(0);
                    vo.setUpWater(gateOpenResVo.getUpz());
                    vo.setDownWater(gateOpenResVo.getDwz());
                    vo.setTm(gateOpenResVo.getTm());
                    vo.setTgtq(gateOpenResVo.getMxgtq());
                    vo.setOpenGate(gateOpenResVo.getOvs());
                }
            }
        }
        return list;
    }

    @Override
    public JSONArray historyStatistic(StationHistoryQueryVo queryVo) {
        StationQueryVo stationQueryVo = new StationQueryVo();
        stationQueryVo.setStationCode(queryVo.getStationCode());
        RainfallStationResVo station = stBaseMapper.selectByCondition(stationQueryVo).get(0);
        JSONArray array = new JSONArray();
        String stationType = queryVo.getStationType();
        String table = "";
        List<String> tables = new ArrayList<>();
        String end = queryVo.getEnd();
        String endym = end.substring(0, 7).replace("-", "");
        String index = "";
        if (stationType.equals(StationType.RESERVOIR.type())) {
            index = StationType.RESERVOIR.table();
            table = index + endym;
        } else if (stationType.equals(StationType.RIVER.type())) {
            index = StationType.RIVER.table();
            table = index + endym;
        } else if (stationType.equals(StationType.GATE.type())) {
            index = StationType.GATE.table();
            table = index + endym;
        }

        if (StringUtils.isNotBlank(table)) {
            tables.add(table);
        }
        String startym = queryVo.getStart().substring(0, 7).replace("-", "");
        if (!startym.equals(endym)) {
            tables.add(index + startym);
        }

        List<Map<String, Object>> list = new ArrayList<>();
        if (stationType.equals(StationType.RESERVOIR.type())) {
            list = rainAndWaterMapper.selectHistoryReservoirWaterLevel(tables, queryVo);
        } else if (stationType.equals(StationType.RIVER.type())) {
            list = rainAndWaterMapper.selectHistoryRiverWaterLevel(tables, queryVo);
        } else if (stationType.equals(StationType.GATE.type())) {
            list = rainAndWaterMapper.selectHistoryGate(tables, queryVo);
        }
        for (Map<String, Object> map : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.putAll(map);
            jsonObject.put("adcd", station.getAreaName());
            jsonObject.put("name", station.getStName());
            array.add(jsonObject);
        }
        return array;
    }

    @Override
    public List<Map<String, Object>> stationMaxWaterLevel(MaxWaterLevelQueryVo queryVo) {
        if ("2020".equals(queryVo.getYear()) && Integer.valueOf(queryVo.getMonth()) < 12) {
            //不允许建库日期之前的查询，避免ORM层报错
            return new ArrayList<>();
        }
        StationQueryVo stationQueryVo = new StationQueryVo();
        stationQueryVo.setStationType(queryVo.getStationType());
        stationQueryVo.setAreaCode(queryVo.getAreaCode());
        List<RainfallStationResVo> stations = stBaseMapper.selectByCondition(stationQueryVo);
        Map<String, List<RainfallStationResVo>> map = stations.stream().collect(Collectors.groupingBy(RainfallStationResVo::getStCode));
        String start = null, end = null;
        String days = queryVo.getDays();
        if (MonthDaysType.FIRST_TEN_DAYS.type().equals(days)) {
            start = queryVo.getYear().concat("-").concat(queryVo.getMonth()).concat("-").concat("01 00:00:00");
            end = queryVo.getYear().concat("-").concat(queryVo.getMonth()).concat("-").concat("10 23:59:59");
        } else if (MonthDaysType.SECOND_TEN_DAYS.type().equals(days)) {
            start = queryVo.getYear().concat("-").concat(queryVo.getMonth()).concat("-").concat("11 00:00:00");
            end = queryVo.getYear().concat("-").concat(queryVo.getMonth()).concat("-").concat("20 23:59:59");
        } else if (MonthDaysType.LAST_TEN_DAYS.type().equals(days)) {
            start = queryVo.getYear().concat("-").concat(queryVo.getMonth()).concat("-").concat("21 00:00:00");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int maxday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            end = queryVo.getYear().concat("-").concat(queryVo.getMonth()).concat("-").concat(maxday + " 23:59:59");
        } else {
            //查整月，不做任何处理
        }

        List<Map<String, Object>> list = new ArrayList<>();
        String stationType = queryVo.getStationType();
        String table;
        if (StationType.RESERVOIR.type().equals(stationType)) {
            table = StationType.RESERVOIR.table().concat(queryVo.getYear()).concat(queryVo.getMonth());
            list = rainAndWaterMapper.selectReserVoirMaxWaterLevel(table, start, end, new ArrayList<>(map.keySet()));
        } else if (StationType.RIVER.type().equals(stationType)) {
            table = StationType.RIVER.table().concat(queryVo.getYear()).concat(queryVo.getMonth());
            list = rainAndWaterMapper.selectRiverMaxWaterLevel(table, start, end, new ArrayList<>(map.keySet()));
        }
        for (Map<String, Object> record : list) {
            String stationCode = String.valueOf(record.get("stcd"));
            if (map.containsKey(stationCode)) {
                RainfallStationResVo station = map.get(stationCode).get(0);
                record.put("adcd", station.getAreaName());
                record.put("name", station.getStName());
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> stationAverageRecords(AverageWaterAndRainQueryVo queryVo) {
        StationQueryVo vo = new StationQueryVo();
        vo.setAreaCode(queryVo.getAreaCode());
        vo.setStationType(queryVo.getStationType());
        vo.setStationName(queryVo.getStationName());
        List<RainfallStationResVo> stations = stBaseMapper.selectByCondition(vo);
        Map<String, List<RainfallStationResVo>> map = stations.stream().collect(Collectors.groupingBy(RainfallStationResVo::getStCode));
        List<Map<String, Object>> list = new ArrayList<>();
        String stationType = queryVo.getStationType();
        String days = queryVo.getDays();
        String start = null, end = null;
        if (MonthDaysType.FIRST_TEN_DAYS.type().equals(days)) {
            start = queryVo.getYm().concat("-").concat("01 00:00:00");
            end = queryVo.getYm().concat("-").concat("10 23:59:59");
        } else if (MonthDaysType.SECOND_TEN_DAYS.type().equals(days)) {
            start = queryVo.getYm().concat("-").concat("11 00:00:00");
            end = queryVo.getYm().concat("-").concat("20 23:59:59");
        } else if (MonthDaysType.LAST_TEN_DAYS.type().equals(days)) {
            start = queryVo.getYm().concat("-").concat("21 00:00:00");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int maxday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            end = queryVo.getYm().concat("-").concat(maxday + " 23:59:59");
        } else {
            //查整月，不做任何处理
        }
        String table;
        if (StationType.RAINFALL.type().equals(queryVo.getStationType())) {
            table = StationType.RAINFALL.table().concat(queryVo.getYm().replace("-", ""));
            list = rainAndWaterMapper.selectSumRainfall(table, start, end, new ArrayList<>(map.keySet()));
        } else {
            //水位
            table = StationType.RESERVOIR.table().concat(queryVo.getYm().replace("-", ""));
            list.addAll(rainAndWaterMapper.selectReservoirAverageWaterLevel(table, start, end, new ArrayList<>(map.keySet())));
            table = StationType.RIVER.table().concat(queryVo.getYm().replace("-", ""));
            list.addAll(rainAndWaterMapper.selectRiverAverageWaterLevel(table, start, end, new ArrayList<>(map.keySet())));
        }
        for (Map<String, Object> record : list) {
            String stationCode = String.valueOf(record.get("stcd"));
            if (map.containsKey(stationCode)) {
                RainfallStationResVo station = map.get(stationCode).get(0);
                record.put("adcd", station.getAreaName());
                record.put("name", station.getStName());
            }
        }
        return list;
    }

    @Override
    public JSONArray relationStatistic(RelationStatisticQueryVo queryVo) {
        JSONArray result = new JSONArray();
        if (StringUtils.isNotBlank(queryVo.getStationCodes())) {
            String statisticType = queryVo.getStatisticType();
            List<String> tables = new ArrayList<>();
            StationQueryVo stationQueryVo = new StationQueryVo();
            stationQueryVo.setStationCode(queryVo.getStationCodes());
            List<RainfallStationResVo> stations = stBaseMapper.selectByCondition(stationQueryVo);
            Map<String, List<RainfallStationResVo>> stationMap = stations.stream().collect(Collectors.groupingBy(RainfallStationResVo::getStCode));
            Map<String, List<Map<String, Object>>> map = null;
            if (StatisticType.RAINFALL.type().equals(statisticType)) {
                RainfallOrWaterLevelQueryVo rainfallOrWaterLevelQueryVo = new RainfallOrWaterLevelQueryVo();
                rainfallOrWaterLevelQueryVo.setStart(queryVo.getStart().concat(" 08:00:00"));
                rainfallOrWaterLevelQueryVo.setEnd(queryVo.getEnd().concat(" 08:00:00"));
                rainfallOrWaterLevelQueryVo.setStationCodes(Arrays.asList(queryVo.getStationCodes().split(",")));
                String startYm = queryVo.getStart().substring(0, 7).replace("-", "");
                tables.add(StationType.RAINFALL.table().concat(startYm));
                String endYm = queryVo.getEnd().substring(0, 7).replace("-", "");
                if (!endYm.equals(startYm)) {
                    tables.add(StationType.RAINFALL.table().concat(endYm));
                }
                List<Map<String, Object>> list = rainAndWaterMapper.rainfallRelationStatistic(tables, rainfallOrWaterLevelQueryVo);
                map = list.stream().collect(Collectors.groupingBy(m -> String.valueOf(m.get("stcd"))));
                map.forEach((stationCode, record) -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rainfall", record);
                    if (stationMap.containsKey(stationCode)) {
                        jsonObject.put("stationName", stationMap.get(stationCode).get(0).getStName());
                    }
                    result.add(jsonObject);
                });
            } else if (StatisticType.WATER_LEVEL.type().equals(statisticType)) {
                //水位关联统计
                RainfallOrWaterLevelQueryVo rainfallOrWaterLevelQueryVo = new RainfallOrWaterLevelQueryVo();
                rainfallOrWaterLevelQueryVo.setStart(queryVo.getStart().concat(" 00:00:00"));
                rainfallOrWaterLevelQueryVo.setEnd(queryVo.getEnd().concat(" 23:59:59"));
                rainfallOrWaterLevelQueryVo.setStationCodes(Arrays.asList(queryVo.getStationCodes().split(",")));
                String stationType = queryVo.getStationType();
                String tableIndex = "";
                if (StationType.RIVER.type().equals(stationType)) {
                    tableIndex = StationType.RIVER.table();
                } else if (StationType.RESERVOIR.type().equals(stationType)) {
                    tableIndex = StationType.RESERVOIR.table();
                }
                String startYm = queryVo.getStart().substring(0, 7).replace("-", "");
                tables.add(tableIndex.concat(startYm));
                String endYm = queryVo.getEnd().substring(0, 7).replace("-", "");
                if (!endYm.equals(startYm)) {
                    tables.add(tableIndex.concat(endYm));
                }
                List<Map<String, Object>> list = new ArrayList<>();
                if (StationType.RIVER.type().equals(stationType)) {
                    tableIndex = StationType.RIVER.table();
                    list = rainAndWaterMapper.riverWaterLevelRelationStatistic(tables, rainfallOrWaterLevelQueryVo);
                } else if (StationType.RESERVOIR.type().equals(stationType)) {
                    tableIndex = StationType.RESERVOIR.table();
                    list = rainAndWaterMapper.reservoirWaterLevelRelationStatistic(tables, rainfallOrWaterLevelQueryVo);
                }
                map = list.stream().collect(Collectors.groupingBy(m -> String.valueOf(m.get("stcd"))));
                map.forEach((stationCode, record) -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("rainfall", record);
                    if (stationMap.containsKey(stationCode)) {
                        jsonObject.put("stationName", stationMap.get(stationCode).get(0).getStName());
                    }
                    result.add(jsonObject);
                });
            } else if (StatisticType.WATER_QUALITY.type().equals(statisticType)) {
                //水质，保留入口
            } else if (StatisticType.FLOW.type().equals(statisticType)) {
                //流量，保留入口
            }
            Set<String> stationCodes = stationMap.keySet();
            stationCodes.removeAll(map.keySet());
            if (!stationCodes.isEmpty()) {
                for (String stationCode : stationCodes) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("stationName", stationMap.get(stationCode).get(0).getStName());
                    jsonObject.put("rainfall", new ArrayList<>());
                    result.add(jsonObject);
                }
            }
        }
        return result;
    }

    /**
     * 实时工情-农村供水工程列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<CwsResVo> listCws(ProjectListReqVo reqVo) {
        //按筛选条件获取相应的供水工程
        List<CwsResVo> cwsResVos = projectMapper.listCws(reqVo);
        if (CollectionUtils.isNotBlank(cwsResVos)) {
            Set<String> set = cwsResVos.stream().filter(cwsResVo -> StringUtils.isNotBlank(cwsResVo.getCwsCode()))
                    .map(cwsResVo -> cwsResVo.getStationCode()).collect(Collectors.toSet());
            //根据测站编码获取实时水质情况
            if (CollectionUtils.isNotBlank(set)) {
                List<CwsResVo> waterQualityList = rainAndWaterMapper.listWaterQuality(set);
                if (CollectionUtils.isNotBlank(waterQualityList)) {
                    Map<String, CwsResVo> map = waterQualityList.stream().collect(Collectors.toMap(CwsResVo::getStationCode, Function.identity()));
                    cwsResVos.forEach(cwsResVo -> {
                        String stCode = cwsResVo.getStationCode();
                        if (map.containsKey(stCode)) {
                            CwsResVo waterQuality = map.get(stCode);
                            String turbidity = waterQuality.getTurbidity();
                            String ph = waterQuality.getPH();
                            String freeChlorine = waterQuality.getFreeChlorine();
                            cwsResVo.setFreeChlorine(freeChlorine);
                            cwsResVo.setPH(ph);
                            cwsResVo.setDt(waterQuality.getDt());
                            cwsResVo.setTurbidity(turbidity);
                            cwsResVo.setWaterquantity(waterQuality.getWaterquantity());
                            //给水质加上状态
                            if (StringUtils.isNotBlank(turbidity) && StringUtils.isNotBlank(ph) && StringUtils.isNotBlank(freeChlorine)) {
                                String stationCode = cwsResVo.getStationCode();
                                Double freeChlorineD = Double.valueOf(freeChlorine);
                                Double phD = Double.valueOf(ph);
                                boolean flag = "3253009032".equals(stationCode) ? freeChlorineD >= 0.1 && freeChlorineD <= 0.8 : freeChlorineD >= 0.3;
                                flag = flag && Double.valueOf(turbidity) < 1 && phD >= 6.5 && phD <= 8.5;
                                cwsResVo.setStatus(flag ? "正常" : "不正常");
                            }
                        }
                    });
                }
            }
            //去除不符合状态的数据
            String status = reqVo.getStatus();
            if (StringUtils.isNotBlank(status)) {
                cwsResVos = cwsResVos.stream().filter(cwsResVo -> status.equals(cwsResVo.getStatus())).collect(Collectors.toList());
            }
        }
        return cwsResVos;
    }

    @Override
    public List<CwsResVo> listCwsDetail(CwsReqVo reqVo) {
        //通过工程编码查出测站编码
        String stationCode = projectMapper.selectCwsStationCode(reqVo.getStationCode());
        if (StringUtils.isNotBlank(stationCode)) {
            reqVo.setStationCode(stationCode);
            return rainAndWaterMapper.listCwsDetail(reqVo);
        }
        return null;
    }


    @Override
    public List<RainfallStationExportVo> exportRainfallForList(StationQueryVo vo) throws Exception {
        JSONObject result = new JSONObject();
        List<RainfallStationResVo> list = stBaseMapper.selectByCondition(vo);
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String table = StationType.RAINFALL.table() + year + (month < 10 ? "0" + month : "" + month);
        calendar.add(Calendar.HOUR, -12);
        Date start = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<HourRainfallResVo> rainfalls = rainAndWaterMapper.pastTwelveHourRainfall(table,
                format.format(start), format.format(now),
                list.stream().map(RainfallStationResVo::getStCode).collect(Collectors.toList()));
        Map<String, List<HourRainfallResVo>> map = rainfalls.stream().collect(Collectors.groupingBy(HourRainfallResVo::getMgstcd));
        for (RainfallStationResVo station : list) {
            String stationCode = station.getStCode();
            List<JSONObject> rainfall = new ArrayList<>();

            if (map.containsKey(stationCode)) {
                List<HourRainfallResVo> hourRainfall = map.get(stationCode);
                for (HourRainfallResVo vo1 : hourRainfall) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("hour", vo1.getTm().split(" ")[1]);
                    jsonObject.put("total", vo1.getTotal());
                    rainfall.add(jsonObject);
                }
            }
            station.setRainfalls(rainfall);
        }

        List<RainfallStationExportVo> exportVoList = new ArrayList<>();
        for (RainfallStationResVo resVo:list){
            List<JSONObject> jsonList = resVo.getRainfalls();
            Class<RainfallStationExportVo> voClass = RainfallStationExportVo.class;
            RainfallStationExportVo obj=voClass.newInstance();
            for (JSONObject jl:jsonList){
                String hour = "hour" + jl.get("hour");
                String total = (String) jl.get("total");
                //将对应时间字段调换设置值
                Field hourField = voClass.getDeclaredField(hour);
                hourField.set(obj,total);
            }
            //将值对应过去
            obj.setAreaCode(resVo.getAreaCode());
            obj.setStCode(resVo.getStCode());
            obj.setStName(resVo.getStName());
            obj.setAreaName(resVo.getAreaName());
            exportVoList.add(obj);
        }

        return exportVoList;
    }

}
