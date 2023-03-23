package com.ygkj.gragh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.enums.WaterStationType;
import com.ygkj.gragh.feign.CommonServiceClient;
import com.ygkj.gragh.mapper.AttAdcdBaseMapper;
import com.ygkj.gragh.mapper.AttRnBaseMapper;
import com.ygkj.gragh.mapper.AttStBaseMapper;
import com.ygkj.gragh.mapper.SynRainAndWaterMapper;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.service.RivernetService;
import com.ygkj.gragh.service.SynWaterAndRainService;
import com.ygkj.gragh.vo.request.RivernetReplayReqVo;
import com.ygkj.gragh.vo.request.RivernetWaterLevelReqVo;
import com.ygkj.gragh.vo.request.StationRequestVo;
import com.ygkj.gragh.vo.response.RivernetReplayResVo;
import com.ygkj.gragh.vo.response.RivernetWaterLevelResVo;
import com.ygkj.gragh.vo.response.StPrrwtResVo;
import com.ygkj.utils.*;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 河网服务
 * @Date 2021/5/27
 */
@Service("rivernetService")
public class RivernetServiceImpl implements RivernetService {

    @Resource
    AttRnBaseMapper rnBaseMapper;

    @Resource
    SynWaterAndRainService waterAndRainService;

    @Resource
    SynRainAndWaterMapper rainAndWaterMapper;

    @Resource
    AttStBaseMapper stationMapper;

    @Resource
    CommonServiceClient commonServiceClient;

    private final DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");

    String[] rnsort = new String[]{"温瑞水系", "瑞平水系", "乐成水系", "永强水系", "江南塘河", "鳌江内河"};

    final static String orderSql = "ORDER BY FIELD(CONCAT(SUBSTR( `area_code` from 1 for 6),'000000'),'330383000000','330327000000','330329000000','330326000000','330328000000','330324000000','330381000000','330382000000','330322000000','330304000000','330303000000','330302000000','330301000000') desc,`rn_name` ASC";

    @Override
    public List<RivernetWaterLevelResVo> listRivernet(RivernetWaterLevelReqVo reqVo) {
        reqVo.setOrderSql(orderSql);
        // 1. 先查所有的河网
        List<AttRnBase> tempList = this.rnBaseMapper.selectByCondition(reqVo);
        tempList = tempList == null ? Collections.emptyList() : tempList;

        // 用于保证顺序
//        Map<String, AttRnBase> tempMap = tempList.stream().collect(Collectors.toMap(AttRnBase::getRnName, Function.identity()));
//        List<AttRnBase> rnList = new ArrayList<>(tempList.size());
//        for (String s : rnsort) {
//            rnList.add(tempMap.get(s));
//        }
        Date dataTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dataTime = sdf.parse(reqVo.getDataTime());
        } catch (Exception ignore) {

        }
        // 2. 查询测站实时水位
        List<String> stcdList = tempList.stream().map(AttRnBase::getMainStCode).collect(Collectors.toList());
        List<AttStBase> waterList = stations(dataTime, stcdList);
        Map<String, String> adcdMap = selectAdnmByAdcd(tempList.stream().filter(o -> org.apache.commons.lang3.StringUtils.isNotBlank(o.getAreaCode())).map(AttRnBase::getAreaCode).collect(Collectors.toList()));
        waterList = waterList == null ? Collections.emptyList() : waterList;
        Map<String, AttStBase> waterLevelMap = waterList.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
        List<RivernetWaterLevelResVo> result = new ArrayList<>(tempList.size());
        for (AttRnBase temp : tempList) {
            RivernetWaterLevelResVo tempRes = new RivernetWaterLevelResVo();
            BeanUtils.copyProperties(temp, tempRes);
            tempRes.setAreaName(adcdMap.get(temp.getAreaCode()));
            AttStBase station = waterLevelMap.get(temp.getMainStCode());
            if (station != null) {
                tempRes.setWaterLevel(station.getWaterLevel());
                tempRes.setStLat(station.getStLat());
                tempRes.setStLong(station.getStLong());
                tempRes.setStType(station.getStType());
                tempRes.setDataTime(station.getTime());
                Double waterLevel = tempRes.getWaterLevel();
                BigDecimal warningLevel = temp.getWarningLevel();
                BigDecimal guaranteeLevel = temp.getGuranteeLevel();
                Double overWarn = null;
                Double overGuarantee = null;
                if (warningLevel != null && waterLevel != null && warningLevel.doubleValue() < waterLevel) {
                    overWarn = Double.valueOf(String.format("%.2f", waterLevel - warningLevel.doubleValue()));
                }
                if (guaranteeLevel != null && waterLevel != null && guaranteeLevel.doubleValue() < waterLevel) {
                    overGuarantee = Double.valueOf(String.format("%.2f", waterLevel - guaranteeLevel.doubleValue()));
                }
                tempRes.setOverWarn(overWarn);
                tempRes.setOverGuarantee(overGuarantee);
                tempRes.setLatitude(station.getStLat());
                tempRes.setLongitude(station.getStLong());
                tempRes.setCompareLast(station.getCompareLast());
            } else {
                tempRes.setDataTime(dataTime);
            }
            tempRes.computeOverLimitLevel();
            result.add(tempRes);
        }

        if (reqVo.isWaterCapacity() || reqVo.isWarnStorage()) {
            setCapacity4Rsvr(result, o -> BigDecimal.valueOf(o.getWaterLevel()), RivernetWaterLevelResVo::setWaterLevelCapacity);
        }
        // 计算至警戒纳蓄水能力
        if (reqVo.isWarnStorage()) {
            computeLimitStorageAbility(result);
        }
        return result;
    }

    /**
     * 查询河道测站实时水位以及和上个时刻的水位对比
     *
     * @param dataTime
     * @param stcds
     * @return
     */
    private List<AttStBase> stations(Date dataTime, List<String> stcds) {
        if (com.ygkj.utils.CollectionUtils.isBlank(stcds)) {
            return Collections.emptyList();
        }
        dataTime = dataTime == null ? new Date() : dataTime;
        Date start = new Date(dataTime.getTime() - 3600000);
        List<StPrrwtResVo> stPrrwtResVos = rainAndWaterMapper.selectRiverByTimeInterval(echoWaterRainTableByDateInternal(StationType.RIVER.table(), start, dataTime), start, dataTime, stcds);
        List<AttStBase> stBaseList = stationMapper.selectByCondition(Builder.of(StationRequestVo::new)
                .with(StationRequestVo::setStationCodes, String.join(StringUtils.JOIN_DELIMITER, stcds))
                .build());
        Map<String, List<StPrrwtResVo>> groupMap = stPrrwtResVos.stream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
        for (AttStBase station : stBaseList) {
            List<StPrrwtResVo> list = groupMap.get(station.getStCode());
            if (com.ygkj.utils.CollectionUtils.isBlank(list)) {
                continue;
            }
            list = list.stream().sorted(Comparator.comparing(StPrrwtResVo::getTm).reversed()).limit(2).collect(Collectors.toList());
            double waterLevel = list.get(0).getZ().doubleValue();
            station.setTime(list.get(0).getTm());
            station.setWaterLevel(waterLevel);
            String status = "";
            if (null == station.getLimitLevel() || 0d == station.getLimitLevel()) {
                status = WaterStationType.NORMAL_RIVER.type();
            } else {
                if (waterLevel > station.getLimitLevel()) {
                    station.setWarning(true);
                    station.setOverWarnLevel(station.getWaterLevel() - station.getWarningLevel());
                    status = WaterStationType.SUPER_LIMIT.type();
                }
//                    else if (waterLevel >= station.getGuaranteeLevel()) {
//                        status = WaterStationType.SUPER_GUARANTEE.type();
//                    } else if (waterLevel >= station.getWarningLevel()) {
//                        status = WaterStationType.SUPER_WARNING.type();
//                    }
                else {
                    status = WaterStationType.NORMAL_RIVER.type();
                }
            }
            station.setStatus(status);
            if (list.size() == 2) {
                station.setCompareLast(list.get(0).getZ().compareTo(list.get(1).getZ()));
            } else {
                station.setCompareLast(0);
            }
        }
        return stBaseList;
    }

    @Resource
    private AttAdcdBaseMapper attAdcdBaseMapper;

    /**
     * 根据行政区划编码查询行政区划名称并组成以编码为key的map
     *
     * @param adcdList
     * @return
     */
    public Map<String, String> selectAdnmByAdcd(List<String> adcdList) {
        if (CollectionUtils.isEmpty(adcdList)) {
            return Collections.emptyMap();
        }
        List<AttAdcdBase> records = this.attAdcdBaseMapper.selectAdnmByAdcdList(adcdList);
        if (CollectionUtils.isEmpty(records)) {
            return Collections.emptyMap();
        }
        return records.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
    }

    /**
     * 将水位代入库容曲线获取对应的库容
     *
     * @param rsvrList 水库列表
     * @param origin   源数据字段
     * @param consumer 目标数据字段
     */
    private void setCapacity4Rsvr(List<? extends RivernetWaterLevelResVo> rsvrList,
                                  Function<RivernetWaterLevelResVo, BigDecimal> origin,
                                  BiConsumer<RivernetWaterLevelResVo, Double> consumer) {
        if (!CollectionUtils.isEmpty(rsvrList)) {
            List<RivernetCapacityCurve> orgList = rsvrList.stream().filter(o -> {
                try {
                    return origin.apply(o) != null
                            && org.apache.commons.lang3.StringUtils.isNotBlank(o.getMainStCode());
                } catch (Exception ignore) {
                    return false;
                }
            }).map(o -> Builder.of(RivernetCapacityCurve::new)
                    .with(RivernetCapacityCurve::setCode, o.getMainStCode())
                    .with(RivernetCapacityCurve::setWaterLevel, origin.apply(o))
                    .build()).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orgList)) {
                CommonResult<Map<String, BigDecimal>> resp = commonServiceClient.calculationRivernetCapacity(orgList);
                if (resp.getCode() == CommonCode.SUCCESS.code() && resp.getData() != null) {
                    Map<String, BigDecimal> map = resp.getData();
                    rsvrList.forEach(o -> {
                        BigDecimal capacity = map.get(o.getMainStCode());
                        if (capacity != null) {
                            capacity = capacity.setScale(1, BigDecimal.ROUND_HALF_UP);
                            consumer.accept(o, capacity.doubleValue());
                        } else {
                            consumer.accept(o, null);
                        }
                    });
                }
            }
        }
    }

    /**
     * 计算至警戒纳蓄能力与警戒与实时库容的插值
     *
     * @param result 将要计算的河网列表
     */
    public void computeLimitStorageAbility(List<? extends RivernetWaterLevelResVo> result) {
        setCapacity4Rsvr(result, RivernetWaterLevelResVo::getWarningLevel, RivernetWaterLevelResVo::setWarnLevelCapacity);
        result.forEach(o -> {
            if (o.getWaterLevelCapacity() != null
                    && o.getWarnLevelCapacity() != null//警戒水位对应库容
                    && o.getWaterArea() != null//集雨面积
            ) {
                o.setWarnStorageAbility(HydrologyUtils.calculateStorageAbility(BigDecimal.valueOf(o.getWaterLevelCapacity()), BigDecimal.valueOf(o.getWarnLevelCapacity()), o.getWaterArea(), BigDecimal.valueOf(0.5D)).intValue());
                o.setWarnStorageSub(o.getWarnLevelCapacity() - o.getWaterLevel());
            }
        });
    }

    @Override
    public JSONObject rivernetReplayStats(RivernetReplayReqVo reqVo) {
        String rnName = reqVo.getRnName();
        Date start = reqVo.getStart();
        Date end = reqVo.getEnd();
        JSONObject result = new JSONObject();
        if (start == null || end == null) {
            return result;
        }
        List<AttRnBase> rnList = this.rnBaseMapper.selectByCondition(Builder.of(RivernetWaterLevelReqVo::new)
                .with(RivernetWaterLevelReqVo::setRnName, rnName).build());
        if (CollectionUtils.isEmpty(rnList)) {
            return result;
        }
        List<String> tables = echoWaterRainTableByDateInternal(StationType.RIVER.table(), start, end);
        List<StPrrwtResVo> waterLevelTempList = Collections.emptyList();
        if (!CollectionUtils.isEmpty(tables)) {
            waterLevelTempList = rainAndWaterMapper.selectRiverByTimeInterval(tables, start, end, rnList.stream().map(AttRnBase::getMainStCode).collect(Collectors.toList()));
        }
        waterLevelTempList = waterLevelTempList == null ? Collections.emptyList() : waterLevelTempList;
        Map<String, List<StPrrwtResVo>> waterLevelMap = waterLevelTempList.stream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));

        List<RivernetReplayResVo> tempList = new ArrayList<>(rnList.size());
        int overWarnNum = 0;
        RivernetReplayResVo maxOverWarnTime = null;

        Comparator<StPrrwtResVo> waterLevelComparator = (o1, o2) -> {
            if (o1.getRz() != null && o2.getRz() != null) {
                return o1.getRz().compareTo(o2.getRz());
            } else if (o1.getRz() == null & o2.getRz() != null) {
                return -1;
            } else if (o1.getRz() != null & o2.getRz() == null) {
                return 1;
            } else {
                return 0;
            }
        };
        for (AttRnBase attRnBase : rnList) {
            RivernetReplayResVo temp = new RivernetReplayResVo();
            BeanUtils.copyProperties(attRnBase, temp);
            tempList.add(temp);
            List<StPrrwtResVo> waterLevelList = waterLevelMap.get(temp.getMainStCode());
            if (CollectionUtils.isEmpty(waterLevelList)) {
                temp.setWaterLevelList(Collections.emptyList());
                continue;
            }
            waterLevelList = waterLevelList.stream().sorted(Comparator.comparing(StPrrwtResVo::getTm)).collect(Collectors.toList());
            temp.setWaterLevelList(waterLevelList);
            waterLevelList.parallelStream().max(waterLevelComparator).ifPresent(o -> {
                temp.setHighestWaterLevel(o.getZ());
                temp.setHighestWaterLevelTm(o.getTm());
            });
            waterLevelList.parallelStream().min(waterLevelComparator).ifPresent(o -> {
                temp.setLowestWaterLevel(o.getZ());
                temp.setLowestWaterLevelTm(o.getTm());
            });
            if (temp.getWarningLevel() != null) {
                double wl = temp.getWarningLevel().doubleValue();
                long overWarnTimes = waterLevelList.stream().filter(o -> o.getZ() > wl).count();
                if (overWarnTimes > 0) {
                    overWarnNum++;
                    temp.setOverWarn(true);
                    temp.setOverWarnMinutes((int) overWarnTimes * 5);
                    if (maxOverWarnTime == null) {
                        maxOverWarnTime = temp;
                    } else if (temp.getOverWarnMinutes() > maxOverWarnTime.getOverWarnMinutes()) {
                        maxOverWarnTime = temp;
                    }
                }
            }
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<RivernetReplayResVo> comparator = new BeanComparator<>(reqVo.getOrderColumn());
            if (reqVo.isAscOrDesc()) {
                comparator = comparator.reversed();
            }
            tempList = tempList.stream().sorted(comparator).collect(Collectors.toList());
        }
        result.put("overWarnNum", overWarnNum);
        result.put("maxOverWarnTime", maxOverWarnTime == null ? new RivernetReplayResVo() : maxOverWarnTime);
        if (!reqVo.isWaterLevelCurve()) {
            tempList.forEach(o -> {
                o.setWaterLevelList(Collections.emptyList());
            });
        }
        result.put("list", tempList);
        return result;
    }

    /**
     * 导出河网
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void exportRivernet(RivernetWaterLevelReqVo reqVo, HttpServletResponse response) {
        if (reqVo.getExportStrategy() == null) {
            reqVo.setExportStrategy(0);
        }
        // 禁止分页
        reqVo.noPage();
        List<RivernetWaterLevelResVo> list = listRivernet(reqVo);
        switch (reqVo.getExportStrategy()) {
            case 0: {
                ExportUtils.exportExcel(list, "河网",
                        new String[]{"名称", "流域", "行政区划", "水位(m)", "水位站名称", "警戒水位（m）", "超警戒水位（m）", "保证水位（m）", "超保证水位（m）"},
                        new String[]{"rnName", "bas", "areaName", "waterLevel", "mainStName", "warningLevel", "overLimitLevel", "highestLevel", ""},
                        response, null, Collections.emptyMap());

            }
            break;
        }
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

}
