package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.mapper.GiveAnAlarmMapper;
import com.ygkj.big.screen.mapper.SectionInformationMapper;
import com.ygkj.big.screen.model.AttStBase;
import com.ygkj.big.screen.model.SectionInformation;
import com.ygkj.big.screen.service.SectionInformationService;
import com.ygkj.big.screen.vo.request.SynWaterRequestVo;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ygkj.com.util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2022/5/19 16:45
 * @Description:
 */
@Service
public class SectionInformationServiceImpl extends ServiceImpl<SectionInformationMapper, SectionInformation> implements SectionInformationService {

    @Autowired
    private SectionInformationMapper sectionInformationMapper;
    @Autowired
    private GiveAnAlarmMapper giveAnAlarmMapper;
    @Autowired
    private GraphClient graphClient;

    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public List<JSONObject> hydrograph(String time,String startTime,String endTime) {
        Date start = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss",startTime);
        Date end = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss",endTime);
        //查询所有断面已经测站相关数据
        List<JSONObject> list =  sectionInformationMapper.getSectionInformationStcd();

        Map<String, List<JSONObject>> typeStcd = list.stream().collect(Collectors.groupingBy(m -> m.getString("st_type")));
        List<JSONObject> arrayList = new ArrayList<>();
        Date parseDate = DateUtils.parseDate("yyyy-MM-dd HH:mm:ss", time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate);
        calendar.add(Calendar.HOUR_OF_DAY,  -1);
        Date calendarTime = calendar.getTime();
        typeStcd.forEach((k,v)->{
            List<String> stcd = v.stream().map(m -> m.getString("stcd")).collect(Collectors.toList());
            String table;
            //获取时间点最近的一条数据
            switch (k) {
                case "RR":
                    table = StationType.RESERVOIR.table().concat(LocalDate.now().format(yyyyMM));
                    List<StRsvrR> stRsvrRS = sectionInformationMapper.selectRsvrWaterLevelByTimeCode(table, stcd, parseDate);
                    List<StRsvrR> stRsvrRS2 = sectionInformationMapper.selectRsvrWaterLevelByTimeCode(table, stcd, calendarTime);
                    //获取各测站的历史最高水位
                    List<String> reservoirTables = echoWaterRainTableByDateInternal(StationType.RESERVOIR.table(), start, end);
                    List<StRsvrR> rsvrHighestLevel = sectionInformationMapper.selectRsvrHighestLevel(reservoirTables, stcd,startTime,endTime);
                    Map<String, StRsvrR> stRsvrRMap = null;
                    if (CollectionUtils.isNotBlank(rsvrHighestLevel)){
                        stRsvrRMap = rsvrHighestLevel.stream().collect(Collectors.toMap(StRsvrR::getMgstcd, Function.identity()));
                    }
                    for (JSONObject jsonObject : v) {
                        String stcd1 = jsonObject.getString("stcd");
                        for (StRsvrR stRsvrR : stRsvrRS) {
                            String mgstcd = stRsvrR.getMgstcd();
                            if (stcd1.equals(mgstcd)){
                                jsonObject.put("waterLevel", stRsvrR.getRz());
                            }
                        }
                        for (StRsvrR stRsvrR : stRsvrRS2) {
                            String mgstcd = stRsvrR.getMgstcd();
                            if (stcd1.equals(mgstcd)){
                                jsonObject.put("waterLevel2", stRsvrR.getRz());
                            }
                        }
                        if (stRsvrRMap != null && stRsvrRMap.containsKey(stcd1)){
                            StRsvrR stRsvrR = stRsvrRMap.get(stcd1);
                            jsonObject.put("appear_time", stRsvrR.getTm());
                            jsonObject.put("highest_level", stRsvrR.getRz());

                        }
                    }
                    arrayList.addAll(v);
                    break;
                case "ZZ":
                    table = StationType.RIVER.table().concat(LocalDate.now().format(yyyyMM));
                    List<StRiverR> stRiverRS = sectionInformationMapper.selectRiverByTimeCode(table, stcd, parseDate);
                    List<StRiverR> stRiverRS2 = sectionInformationMapper.selectRiverByTimeCode(table, stcd, calendarTime);
                    //获取各测站的历史最高水位
                    List<String> riverTables = echoWaterRainTableByDateInternal(StationType.RIVER.table(), start, end);
                    List<StRiverR> riverHighestLevel = sectionInformationMapper.selectRiverHighestLevel(riverTables, stcd,startTime,endTime);
                    Map<String, StRiverR> stRiverRMap = null;
                    if (CollectionUtils.isNotBlank(riverHighestLevel)){
                        stRiverRMap = riverHighestLevel.stream().collect(Collectors.toMap(StRiverR::getMgstcd, Function.identity()));
                    }
                    for (JSONObject jsonObject : v) {
                        String stcd1 = jsonObject.getString("stcd");
                        for (StRiverR stRiverR : stRiverRS) {
                            String mgstcd = stRiverR.getMgstcd();
                            if (stcd1.equals(mgstcd)){
                                jsonObject.put("waterLevel", stRiverR.getZ());
                            }
                        }
                        for (StRiverR stRiverR : stRiverRS2) {
                            String mgstcd = stRiverR.getMgstcd();
                            if (stcd1.equals(mgstcd)){
                                jsonObject.put("waterLevel2", stRiverR.getZ());
                            }
                        }
                        if (stRiverRMap != null && stRiverRMap.containsKey(stcd1)){
                            StRiverR stRiverR = stRiverRMap.get(stcd1);
                            jsonObject.put("appear_time", stRiverR.getTm());
                            jsonObject.put("highest_level", stRiverR.getZ());

                        }
                    }
                    arrayList.addAll(v);
                    break;
                case "TT":
                    table = StationType.TIDE.table().concat(LocalDate.now().format(yyyyMM));
                    List<StTideR> stTideRS = sectionInformationMapper.selectTideByTimeCode(table, stcd, parseDate);
                    List<StTideR> stTideRS2 = sectionInformationMapper.selectTideByTimeCode(table, stcd, calendarTime);
                    //获取各测站的历史最高水位
                    List<String> tideTables = echoWaterRainTableByDateInternal(StationType.TIDE.table(), start, end);
                    List<StTideR> tideHighestLevel = sectionInformationMapper.selectTideHighestLevel(tideTables, stcd,startTime,endTime);
                    Map<String, StTideR> stTideRMap = null;
                    if (CollectionUtils.isNotBlank(tideHighestLevel)){
                        stTideRMap = tideHighestLevel.stream().collect(Collectors.toMap(StTideR::getMgstcd, Function.identity()));
                    }
                    for (JSONObject jsonObject : v) {
                        String stcd1 = jsonObject.getString("stcd");
                        for (StTideR stTideR : stTideRS) {
                            String mgstcd = stTideR.getMgstcd();
                            if (stcd1.equals(mgstcd)){
                                jsonObject.put("waterLevel", stTideR.getTdz());
                            }
                        }
                        for (StTideR stTideR : stTideRS2) {
                            String mgstcd = stTideR.getMgstcd();
                            if (stcd1.equals(mgstcd)){
                                jsonObject.put("waterLevel2", stTideR.getTdz());
                            }
                        }
                        if (stTideRMap != null && stTideRMap.containsKey(stcd1)){
                            StTideR stTideR = stTideRMap.get(stcd1);
                            jsonObject.put("appear_time", stTideR.getTm());
                            jsonObject.put("highest_level", stTideR.getTdz());

                        }
                    }
                    arrayList.addAll(v);
                    break;
            }
        });
        List<JSONObject> collect = arrayList.stream().sorted(Comparator.comparing(m -> m.getInteger("sort"))).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<SectionInformation> fillData(List<SectionInformation> list) {
        //大屏戍浦江调度塞入实时水位，之后再塞入预报水位
        List<String> stcds = list.stream().filter(o -> StringUtils.isNotBlank(o.getStcd())).map(SectionInformation::getStcd).collect(Collectors.toList());
        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.TIDE.type()).with(SynWaterRequestVo::setStCodes,stcds.stream().collect(Collectors.joining(","))).build());
        CommonResult<List<AttStBase>> riverRes = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.RIVER.type()).with(SynWaterRequestVo::setStCodes,stcds.stream().collect(Collectors.joining(","))).build());
        List<AttStBase> riData = riverRes.getData();
        List<AttStBase> data = response.getData();
        data.addAll(riData);
        for (SectionInformation sectionInformation : list) {
            List<AttStBase> collect = data.stream().filter(d -> d.getStCode().equals(sectionInformation.getStcd())).collect(Collectors.toList());
            if (CollectionUtils.isNotBlank(collect)){
                sectionInformation.setWaterLevel(collect.get(0).getWaterLevel());
                sectionInformation.setUpdateTime(collect.get(0).getTime());
                sectionInformation.setLimitLevel(collect.get(0).getLimitLevel());
                sectionInformation.setWarningLevel(collect.get(0).getWarningLevel());
                sectionInformation.setGuaranteeLevel(collect.get(0).getGuaranteeLevel());
            }
        }
        return list;
    }

    public List<String> echoWaterRainTableByDateInternal(String tablePrefix, Date start, Date end) {
        if (org.apache.commons.lang3.StringUtils.isBlank(tablePrefix) || start == null || end == null) {
            return Collections.emptyList();
        }
        LocalDate startTemp = DateUtils.date2LocalDate(start);
        LocalDate endTemp = DateUtils.date2LocalDate(end);
        LinkedHashSet<String> sequenceSet = new LinkedHashSet<>();
        do {
            String table = tablePrefix + startTemp.format(yyyyMM);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(this.giveAnAlarmMapper.checkTableExist(table))) {
                sequenceSet.add(table);
            }
            startTemp = startTemp.plusDays(1);
        } while (startTemp.isBefore(endTemp));
        return new ArrayList<>(sequenceSet);
    }
}
