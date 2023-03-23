package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.*;
import com.ygkj.project.model.RsvrReportFlood;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.utils.*;
import com.ygkj.water.project.mapper.AttHydrDiagBaseMapper;
import com.ygkj.water.project.mapper.StationMapper;
import com.ygkj.water.project.mapper.WaterAndRainMapper;
import com.ygkj.water.project.service.ProjectStatusService;
import com.ygkj.water.project.service.RsvrService;
import com.ygkj.water.project.service.WagaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 工程工情服务
 * @Date 2021/5/27
 */
@Service("projectStatusServiceImpl")
public class ProjectStatusServiceImpl implements ProjectStatusService, InitializingBean {

    private DateTimeFormatter yyyyMM;

    @Resource
    private StationMapper stationMapper;

    @Resource
    private WaterAndRainMapper waterAndRainMapper;

    @Resource
    private AttHydrDiagBaseMapper hydrDiagMapper;

    @Resource
    private RsvrService rsvrService;

    @Resource
    private WagaService wagaService;

    private Set<String> riverFlowStCodes;


    @Override
    public void afterPropertiesSet() {
        yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
        riverFlowStCodes = new HashSet<>(Arrays.asList("01236",
                "01248",
                "01535",
                "01616",
                "02452",
                "05626")
        );
    }


    List<String> echoWaterRainTableByDateInternal(String tablePrefix, Date start, Date end) {
        if (StringUtils.isBlank(tablePrefix) || start == null || end == null) {
            return Collections.emptyList();
        }
        LocalDate startTemp = DateUtils.date2LocalDate(start);
        LocalDate endTemp = DateUtils.date2LocalDate(end);
        LinkedHashSet<String> sequenceSet = new LinkedHashSet<>();
        do {
            String table = tablePrefix + startTemp.format(yyyyMM);
            if (StringUtils.isNotBlank(this.waterAndRainMapper.checkTableExist(table))) {
                sequenceSet.add(table);
            }
            startTemp = startTemp.plusDays(1);
        } while (startTemp.isBefore(endTemp));
        return new ArrayList<>(sequenceSet);
    }

    /**
     * 水文工程测站概化图
     *
     * @return
     */
    @Override
    public Map<String, Object> hydrProjStaDiagram(HydrDiagReqVo reqVo) {
        // 1. 根据流域查询整编好的水文工程测站数据。
        // 2. 然后根据分别去查询水库，水闸，潮位，河道的监测信息
        //JSONObject result = new JSONObject();
        HashMap<String, List<HydrDiagResVo>> listHashMap = new HashMap<>();
        HashMap<String, Object> results = new HashMap<>();
        AtomicInteger skbj = new AtomicInteger();
        AtomicInteger szbj = new AtomicInteger();
        AtomicInteger czbj = new AtomicInteger();
        AtomicInteger szkq = new AtomicInteger();
        ArrayList<HydrDiagResVo> result = new ArrayList<>();
        List<AttHydrDiagBase> tempList = hydrDiagMapper.selectByCondition(reqVo);
        if (CollectionUtils.isEmpty(tempList)) {
            return results;
        }
        reqVo.setDataTime(reqVo.getDataTime() == null ? new Date() : reqVo.getDataTime());
        Map<String, List<AttHydrDiagBase>> groupMap = tempList.stream().collect(Collectors.groupingBy(AttHydrDiagBase::getCodeType));
        groupMap.forEach((codeType, values) -> {
            if (StringUtils.isNotBlank(codeType) && !CollectionUtils.isEmpty(values)) {
                switch (codeType) {
                    case "水库工程": {
                        List<RsvrStationWaterResVo> list = rsvrService.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
                                .with(RsvrStationWaterReqVo::setDataTime, reqVo.getDataTime())
                                .with(RsvrStationWaterReqVo::setResCodes, values.stream().map(AttHydrDiagBase::getCode).filter(StringUtils::isNotBlank).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER))).build()).getList();
                        Map<String, RsvrReportFlood> reportMap = Collections.emptyMap();
                        Map<String, RsvrStationWaterResVo> map = Collections.emptyMap();
                        if (!CollectionUtils.isEmpty(list)) {
                            List<RsvrReportFlood> reportList = waterAndRainMapper.selectNewestRsvrReportFlood(list.stream().map(RsvrStationWaterResVo::getResCode).filter(StringUtils::isNotBlank).collect(Collectors.toList()));
                            reportList = reportList == null ? Collections.emptyList() : reportList;
                            reportMap = reportList.stream().collect(Collectors.toMap(RsvrReportFlood::getResCode, Function.identity()));
                            map = list.stream().collect(Collectors.toMap(RsvrStationWaterResVo::getResCode, Function.identity()));
                        }
                        for (AttHydrDiagBase temp : values) {
                            HydrDiagResVo target = new HydrDiagResVo();
                            BeanUtils.copyProperties(temp, target);
                            //result.put(temp.getName(), target);
                            RsvrReportFlood report = reportMap.get(temp.getCode());
                            // 水库出库出库入库流量
                            if (report != null) {
                                if (report.getOutflow() != null) {
                                    target.setOutFlow(report.getOutflow().doubleValue());
                                    target.setReportTime(report.getTm());
                                }
                                if (report.getInflow() != null) {
                                    target.setInFlow(report.getInflow().doubleValue());
                                    target.setReportTime(report.getTm());
                                }
                            }
                            // 实时水情
                            RsvrStationWaterResVo rsvr = map.get(temp.getCode());
                            if (rsvr != null) {
                                target.setExtraInfo(rsvr);
                                target.setCodeName(rsvr.getResName());
                                target.setWaterLevel(rsvr.getWaterLevel());
                                target.setWarnLimitLevel(rsvr.getCurrentLimitLevel() == null ? null : rsvr.getCurrentLimitLevel().doubleValue());
                                target.setOverWarnLimit(rsvr.getOverLimitVal() != null && rsvr.getOverLimitVal() > 0);
                                if (target.isOverWarnLimit()){
                                    skbj.getAndIncrement();
                                }
                            }

                            if (target.getWaterLevel() == null && target.getInFlow() == null && target.getOutFlow() == null) {
                                target.setNoData(true);
                            }
                            result.add(target);
                        }
                    }
                    break;
                    case "水闸工程": {
                        List<WagaStationWaterResVo> list = wagaService.wagaWaterLevel(Builder.of(WagaStationWaterReqVo::new)
                                .with(WagaStationWaterReqVo::setDataTime, reqVo.getDataTime())
                                .with(WagaStationWaterReqVo::setWagaCodes, values.stream().map(AttHydrDiagBase::getCode).filter(StringUtils::isNotBlank).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER))).build()).getList();
                        Map<String, WagaStationWaterResVo> map = Collections.emptyMap();
                        if (!CollectionUtils.isEmpty(list)) {
                            map = list.stream().collect(Collectors.toMap(WagaStationWaterResVo::getWagaCode, Function.identity()));
                        }
                        for (AttHydrDiagBase temp : values) {
                            HydrDiagResVo target = new HydrDiagResVo();
                            BeanUtils.copyProperties(temp, target);
                            //result.put(temp.getName(), target);
                            WagaStationWaterResVo waga = map.get(temp.getCode());
                            if (waga == null) {
                                target.setNoData(true);
                                result.add(target);
                                continue;
                            }
                            target.setExtraInfo(waga);
                            target.setCodeName(waga.getWagaName());
                            target.setUpz(waga.getUpz());
                            target.setDwz(waga.getDwz());
                            target.setOvs(waga.getOvs());
                            target.setFlow(waga.getLl());
                            target.setGateOpened(waga.isGateOpened());
                            target.setOverWarnLimit(waga.getReferenceWlStationWarningLevel() != null && target.getUpz() != null && target.getUpz() > waga.getReferenceWlStationWarningLevel().doubleValue());
                            if (target.getUpz() == null
                                    && target.getDwz() == null
                                    && StringUtils.isBlank(target.getOvs())
                                    && StringUtils.isBlank(target.getFlow())
                            ) {
                                target.setNoData(true);
                            }
                            if (target.isOverWarnLimit()){
                                szbj.getAndIncrement();
                            }
                            if (target.isGateOpened()){
                                szkq.getAndIncrement();
                            }
                            result.add(target);
                        }
                    }
                    break;
                    case "水闸测站": {
                        List<String> stcds = values.stream().map(AttHydrDiagBase::getCode).filter(StringUtils::isNotBlank).collect(Collectors.toList());
                        // 没有主测站编码的水闸，根据去测站表里根据关联的工程，查询测站和实时工情
                        // 根据工程编码查询所有的测站
                        if (!CollectionUtils.isEmpty(stcds)) {
                            List<AttStBase> stationList = this.stationMapper.candidateStation(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, stcds));
                            stationList = stationList == null ? Collections.emptyList() : stationList;
                            Map<String, AttStBase> stationMap = stationList.stream().collect(Collectors.toMap(AttStBase::getStCode, o -> o));
                            List<StWasR> waterLevel = Collections.emptyList();
                            if (reqVo.getDataTime() == null) {
                                // 查询最新的水位
                                LocalDateTime nowLdt = LocalDateTime.now();
                                waterLevel = waterAndRainMapper.candidateSluiceWaterLevelWithTime(StationType.GATE.table() + nowLdt.format(yyyyMM), stcds, null, 1);
                            } else {
                                // 查询某个时刻左右的水位
                                waterLevel = waterAndRainMapper.candidateSluiceWaterLevelWithTime(StationType.GATE.table() + DateUtils.date2LocalDate(reqVo.getDataTime()).format(yyyyMM), stcds, reqVo.getDataTime(), 1);
                            }
                            Map<String, StWasR> waterLevelMap = waterLevel.stream().collect(Collectors.toMap(StWasR::getStcd, o -> o));
                            for (AttHydrDiagBase temp : values) {
                                HydrDiagResVo target = new HydrDiagResVo();
                                BeanUtils.copyProperties(temp, target);
                                StWasR stWasR = waterLevelMap.get(temp.getCode());
                                if (stWasR == null) {
                                    target.setNoData(true);
                                    result.add(target);
                                    continue;
                                }
                                AttStBase attStBase = stationMap.get(temp.getCode());
                                StWasR stWasR1 = waterLevelMap.get(temp.getCode());
                                target.setExtraInfo(attStBase);
                                target.setCodeName(attStBase.getStName());
                                target.setUpz(stWasR1.getUpz());
                                target.setDwz(stWasR1.getDwz());
                                target.setOvs(stWasR1.getOvs());
                                target.setFlow(stWasR1.getLl());
                                if (target.getUpz() == null
                                        && target.getDwz() == null
                                        && StringUtils.isBlank(target.getOvs())
                                        && StringUtils.isBlank(target.getFlow())
                                ) {
                                    target.setNoData(true);
                                }
                                target.setOverWarnLimit(attStBase.getWarningLevel() != null && target.getWaterLevel() != null && target.getWaterLevel() > attStBase.getWarningLevel());
                                if (target.isOverWarnLimit()){
                                    czbj.getAndIncrement();
                                }
                                result.add(target);
                            }
                        }
                    }
                    break;
                    case "潮位测站": {
                        List<AttStBase> list = listTide(values.stream().map(AttHydrDiagBase::getCode).filter(StringUtils::isNotBlank).collect(Collectors.toList()), reqVo.getDataTime());
                        Map<String, AttStBase> map = Collections.emptyMap();
                        if (!CollectionUtils.isEmpty(list)) {
                            map = list.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
                        }
                        for (AttHydrDiagBase temp : values) {
                            HydrDiagResVo target = new HydrDiagResVo();
                            BeanUtils.copyProperties(temp, target);
                            //result.put(temp.getName(), target);
                            AttStBase station = map.get(temp.getCode());
                            if (station == null) {
                                target.setNoData(true);
                                result.add(target);
                                continue;
                            }
                            target.setExtraInfo(station);
                            target.setCodeName(station.getStName());
                            target.setWaterLevel(station.getWaterLevel() == Double.MIN_VALUE ? null : station.getWaterLevel());
                            target.setOverWarnLimit(station.getWarningLevel() != null && target.getWaterLevel() != null && target.getWaterLevel() > station.getWarningLevel());
                            if (target.isOverWarnLimit()){
                                czbj.getAndIncrement();
                            }
                            if (target.getWaterLevel() == null) {
                                target.setNoData(true);
                            }
                            result.add(target);
                        }
                    }
                    break;
                    case "河道测站": {
                        List<AttStBase> list = listRiver(values.stream().map(AttHydrDiagBase::getCode).filter(StringUtils::isNotBlank).collect(Collectors.toList()), reqVo.getDataTime());
                        Map<String, AttStBase> map = Collections.emptyMap();
                        if (!CollectionUtils.isEmpty(list)) {
                            map = list.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
                        }
                        for (AttHydrDiagBase temp : values) {
                            HydrDiagResVo target = new HydrDiagResVo();
                            BeanUtils.copyProperties(temp, target);
                            //result.put(temp.getName(), target);
                            AttStBase station = map.get(temp.getCode());
                            if (station == null) {
                                target.setNoData(true);
                                result.add(target);
                                continue;
                            }
                            target.setExtraInfo(station);
                            target.setCodeName(station.getStName());
                            target.setWaterLevel(station.getWaterLevel() == Double.MIN_VALUE ? null : station.getWaterLevel());
                            target.setOverWarnLimit(station.getWarningLevel() != null && target.getWaterLevel() != null && target.getWaterLevel() > station.getWarningLevel());
                            if (target.isOverWarnLimit()){
                                czbj.getAndIncrement();
                            }
                            if (this.riverFlowStCodes.contains(temp.getCode())) {
                                List<StZqrlB> zqrlList = this.stationMapper.selectNewestZqCurveByStcd(temp.getCode());
                                BigDecimal flow = computeFlow(zqrlList, new BigDecimal(target.getWaterLevel()));
                                target.setFlow(flow == null ? null : flow.toString());
                            }
                            if (target.getWaterLevel() == null && StringUtils.isBlank(target.getFlow())) {
                                target.setNoData(true);
                            }
                            result.add(target);
                        }
                    }
                    break;
                    case "水库测站":{
                        List<AttStBase> list = listRsvr(values.stream().map(AttHydrDiagBase::getCode).filter(StringUtils::isNotBlank).collect(Collectors.toList()), reqVo.getDataTime());
                        Map<String, AttStBase> map = Collections.emptyMap();
                        if (!CollectionUtils.isEmpty(list)) {
                            map = list.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
                        }
                        for (AttHydrDiagBase temp : values) {
                            HydrDiagResVo target = new HydrDiagResVo();
                            BeanUtils.copyProperties(temp, target);
                            //result.put(temp.getName(), target);
                            AttStBase station = map.get(temp.getCode());
                            if (station == null) {
                                target.setNoData(true);
                                result.add(target);
                                continue;
                            }
                            target.setExtraInfo(station);
                            target.setCodeName(station.getStName());
                            target.setWaterLevel(station.getWaterLevel() == Double.MIN_VALUE ? null : station.getWaterLevel());
                            target.setOverWarnLimit(station.getWarningLevel() != null && target.getWaterLevel() != null && target.getWaterLevel() > station.getWarningLevel());
                            if (target.isOverWarnLimit()){
                                czbj.getAndIncrement();
                            }
                            if (this.riverFlowStCodes.contains(temp.getCode())) {
                                List<StZqrlB> zqrlList = this.stationMapper.selectNewestZqCurveByStcd(temp.getCode());
                                BigDecimal flow = computeFlow(zqrlList, new BigDecimal(target.getWaterLevel()));
                                target.setFlow(flow == null ? null : flow.toString());
                            }
                            if (target.getWaterLevel() == null && StringUtils.isBlank(target.getFlow())) {
                                target.setNoData(true);
                            }
                            result.add(target);
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        });
        if (result.size() > 0){
            Map<String, List<HydrDiagResVo>> collect = result.stream().collect(Collectors.groupingBy(AttHydrDiagBase::getBas));
            for (String key : collect.keySet()) {
                List<HydrDiagResVo> hydrDiagResVos = collect.get(key);
                hydrDiagResVos.sort(Comparator.comparing(HydrDiagResVo::getPx));
                //前端需要改名字
                String keyName = "";
                switch (key){
                    case "荫溪小流域":
                        keyName = "yingXiBasins";
                        break;
                    case "戍浦江":
                        keyName = "ourPujiang";
                        break;
                    case "仰义河":
                        keyName = "angYiRiver";
                        break;
                    case "卧旗大河":
                        keyName = "woQiRiver";
                        break;
                    case "温瑞塘河":
                        keyName = "wenRuitangRiver";
                        break;
                    case "七都内河":
                        keyName = "qiDuRiver";
                        break;
                    case "丰门河":
                        keyName = "fengRiverDoor";
                        break;
                    default:
                        break;
                }
                //仰义水闸现在没有数据，所以手动塞入闸上：岩门上游21年建30032905，闸下：岩门下游21年建30032912
                //之后仰义有数据，并且闸上闸下测站确定后可以删去下面代码
                if ("angYiRiver".equals(keyName)){
                    String stcds = "30032905,30032912";
                    List<String> asList = Arrays.asList(stcds);
                    List<AttStBase> list = listRiver(asList, reqVo.getDataTime());
                    Map<String, AttStBase> map = Collections.emptyMap();
                    if (!CollectionUtils.isEmpty(list)) {
                        map = list.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
                    }
                    for (HydrDiagResVo hydrDiagResVo : hydrDiagResVos) {
                        if ("仰义水闸".equals(hydrDiagResVo.getName())){
                            if (map.containsKey("30032905")){
                                AttStBase station = map.get("30032905");
                                hydrDiagResVo.setUpz(station.getWaterLevel() == Double.MIN_VALUE ? null : station.getWaterLevel());
                            }
                            if (map.containsKey("30032912")){
                                AttStBase station = map.get("30032912");
                                hydrDiagResVo.setDwz(station.getWaterLevel() == Double.MIN_VALUE ? null : station.getWaterLevel());
                            }
                            if (hydrDiagResVo.getUpz() != null && hydrDiagResVo.getDwz() != null){
                                hydrDiagResVo.setNoData(false);
                            }
                        }
                    }
                }
                listHashMap.put(keyName, hydrDiagResVos);
            }
        }
        results.put("list", listHashMap);
        results.put("skbj", skbj.intValue());
        results.put("szbj", szbj.intValue());
        results.put("czbj", czbj.intValue());
        results.put("szkq", szkq.intValue());
        return results;
    }

    private List<AttStBase> listRsvr(List<String> codes, Date dataTime) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        List<AttStBase> stationList = stationMapper.candidateStation(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, codes));
        if (CollectionUtils.isEmpty(stationList)) {
            return Collections.emptyList();
        }
        List<String> tables = new ArrayList<>();
        String dateStr = DateUtils.date2LocalDate(dataTime).format(yyyyMM);
        tables.add(StationType.RESERVOIR.table() + dateStr);
        List<StRsvrR> stRsvrRList = waterAndRainMapper.candidateRsvrWaterLevelWithTimeInternal(tables, codes, dataTime,new Date());
        stRsvrRList = stRsvrRList == null ? Collections.emptyList() : stRsvrRList;
        Map<String, StRsvrR> map = stRsvrRList.stream().collect(Collectors.toMap(StRsvrR::getMgstcd, Function.identity()));
        for (AttStBase stBase : stationList) {
            StRsvrR temp = map.get(stBase.getStCode());
            if (temp == null) {
                continue;
            }
            stBase.setTdz(temp.getRz() == null ? Double.MIN_VALUE : temp.getRz());
        }
        return stationList;
    }

    /**
     * 水文工程测站概化图统计窗口
     *
     * @return
     */
    @Override
    public JSONObject hydrProjStaDiagramStatistic(Integer hours) {
        // 1. 根据流域查询整编好的水文工程测站数据。
        // 2. 然后根据分别去查询水库，水闸，潮位，河道的监测信息
        JSONObject result = new JSONObject();
        HydrDiagReqVo reqVo = new HydrDiagReqVo();
        List<AttHydrDiagBase> tempList = hydrDiagMapper.selectByCondition(reqVo);
        if (CollectionUtils.isEmpty(tempList)) {
            return result;
        }
        if (hours == null) {
            hours = 1;
        }
        reqVo.setDataTime(reqVo.getDataTime() == null ? new Date() : reqVo.getDataTime());
        Map<String, List<AttHydrDiagBase>> groupMap = tempList.stream().collect(Collectors.groupingBy(AttHydrDiagBase::getCodeType));
        // 水库报警数量、水闸开启水量、水文报警数据、潮位报警数据、水库出库流量、过去**小时水闸泄洪流量
        int rsvrWarnNum = 0;
        int wagaOpenNum = 0;
        int riverWarnNum = 0;
        int tideWarnNum = 0;
        double rsvrOutflow = 0D;
        double wagaDrainage = 0D;
        for (Map.Entry<String, List<AttHydrDiagBase>> entry : groupMap.entrySet()) {
            String codeType = entry.getKey();
            List<AttHydrDiagBase> values = entry.getValue();
            if (StringUtils.isNotBlank(codeType) && !CollectionUtils.isEmpty(values)) {
                switch (codeType) {
                    case "水库工程": {
                        List<RsvrStationWaterResVo> list = rsvrService.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
                                .with(RsvrStationWaterReqVo::setDataTime, reqVo.getDataTime())
                                .with(RsvrStationWaterReqVo::setResCodes, values.stream().map(AttHydrDiagBase::getCode).filter(StringUtils::isNotBlank).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER)))
                                .with(RsvrStationWaterReqVo::setDrainage24H, true)
                                .build())
                                .getList();
                        if (!CollectionUtils.isEmpty(list)) {
                            rsvrWarnNum = (int) list.stream().filter(rsvr -> rsvr.getOverLimitVal() != null && rsvr.getOverLimitVal() > 0).count();
                            rsvrOutflow = list.stream().filter(o -> o.getDrainage24H() != null).mapToDouble(RsvrStationWaterResVo::getDrainage24H).sum();
                        }
                    }
                    break;
                    case "水闸工程": {
                        List<WagaStationWaterResVo> list = wagaService.wagaWaterLevel(Builder.of(WagaStationWaterReqVo::new)
                                .with(WagaStationWaterReqVo::setDataTime, reqVo.getDataTime())
                                .with(WagaStationWaterReqVo::setWagaCodes, values.stream().map(AttHydrDiagBase::getCode).filter(StringUtils::isNotBlank).filter(StringUtils::isNotBlank).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER)))
                                .build()).getList();
                        if (!CollectionUtils.isEmpty(list)) {
                            wagaOpenNum = (int) list.stream().filter(waga -> waga.isGateOpened()).count();
                            // 查询 水库泄流量
                            Date end = reqVo.getDataTime();
                            Date start = new Date(end.getTime() - hours * 3600 * 1000);
                            List<StWasR> waterLevelTempList = Collections.emptyList();
                            List<String> tables = echoWaterRainTableByDateInternal(StationType.GATE.table(), start, end);
                            if (!CollectionUtils.isEmpty(tables)) {
                                waterLevelTempList = waterAndRainMapper.candidateSluiceWaterLevelWithTimeInternal(tables,
                                        list.stream().map(WagaStationWaterResVo::getStCode).filter(StringUtils::isNotBlank).collect(Collectors.toList()),
                                        start,
                                        end
                                );
                            }
                            waterLevelTempList = waterLevelTempList == null ? Collections.emptyList() : waterLevelTempList;
                            Map<String, List<StWasR>> waterLevelMap = waterLevelTempList.stream().collect(Collectors.groupingBy(StWasR::getMgstcd));
                            Iterator<Map.Entry<String, List<StWasR>>> wasIterator = waterLevelMap.entrySet().iterator();
                            while (wasIterator.hasNext()) {
                                Map.Entry<String, List<StWasR>> wasEntry = wasIterator.next();
                                List<StWasR> wasList = wasEntry.getValue();
//                                System.out.println(wasList.stream().map(StWasR::getLl).filter(StringUtils::isNotBlank).collect(Collectors.toList()));
                                if (!CollectionUtils.isEmpty(wasList)) {
                                    wagaDrainage += wagaService.wagaTotalDrainage(wasList, end);
                                }
                            }
                        }
                    }
                    break;
                    case "潮位测站": {
                        List<AttStBase> list = listTide(values.stream().map(AttHydrDiagBase::getCode).filter(StringUtils::isNotBlank).collect(Collectors.toList()), reqVo.getDataTime());
                        if (!CollectionUtils.isEmpty(list)) {
                            tideWarnNum = (int) list.stream().filter(station -> station.getWarningLevel() != null && station.getWaterLevel() != Double.MIN_VALUE && station.getWaterLevel() > station.getWarningLevel()).count();
                        }
                    }
                    break;
                    case "河道测站": {
                        List<AttStBase> list = listRiver(values.stream().map(AttHydrDiagBase::getCode).filter(StringUtils::isNotBlank).collect(Collectors.toList()), reqVo.getDataTime());
                        if (!CollectionUtils.isEmpty(list)) {
                            riverWarnNum = (int) list.stream().filter(station -> station.getWarningLevel() != null && station.getWaterLevel() != Double.MIN_VALUE && station.getWaterLevel() > station.getWarningLevel()).count();
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        }
        result.put("rsvrWarnNum", rsvrWarnNum);
        result.put("wagaOpenNum", wagaOpenNum);
        result.put("riverWarnNum", riverWarnNum);
        result.put("tideWarnNum", tideWarnNum);
        result.put("rsvrOutflow", rsvrOutflow);
        result.put("wagaDrainage", wagaDrainage);
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

    public List<AttStBase> listTide(List<String> codes, Date dataTime) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        List<AttStBase> stationList = stationMapper.candidateStation(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, codes));
        if (CollectionUtils.isEmpty(stationList)) {
            return Collections.emptyList();
        }
        List<StTideR> tideList = waterAndRainMapper.candidateTideWithTime(StationType.TIDE.table() + DateUtils.date2LocalDate(dataTime).format(yyyyMM), codes, dataTime);
        tideList = tideList == null ? Collections.emptyList() : tideList;
        Map<String, StTideR> map = tideList.stream().collect(Collectors.toMap(StTideR::getMgstcd, Function.identity()));
        for (AttStBase stBase : stationList) {
            StTideR temp = map.get(stBase.getStCode());
            if (temp == null) {
                continue;
            }
            stBase.setTdz(temp.getTdz() == null ? Double.MIN_VALUE : temp.getTdz());
        }
        return stationList;
    }


    public List<AttStBase> listRiver(List<String> codes, Date dataTime) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        List<AttStBase> stationList = stationMapper.candidateStation(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, codes));
        if (CollectionUtils.isEmpty(stationList)) {
            return Collections.emptyList();
        }
        List<StRiverR> tideList = waterAndRainMapper.candidateRiverWaterLevelWithTime(StationType.RIVER.table() + DateUtils.date2LocalDate(dataTime).format(yyyyMM), codes, dataTime);
        tideList = tideList == null ? Collections.emptyList() : tideList;
        Map<String, StRiverR> map = tideList.stream().collect(Collectors.toMap(StRiverR::getMgstcd, Function.identity()));
        for (AttStBase stBase : stationList) {
            StRiverR temp = map.get(stBase.getStCode());
            if (temp == null) {
                continue;
            }
            stBase.setWaterLevel(temp.getZ() == null ? Double.MIN_VALUE : temp.getZ());
        }
        return stationList;
    }
}
