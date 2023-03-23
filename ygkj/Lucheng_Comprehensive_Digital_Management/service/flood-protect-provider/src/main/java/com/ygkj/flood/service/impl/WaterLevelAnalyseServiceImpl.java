package com.ygkj.flood.service.impl;

import com.ygkj.enums.StationType;
import com.ygkj.enums.WaterStationType;
import com.ygkj.flood.enums.FloatWindowTypeEnum;
import com.ygkj.flood.feign.WaterLevelClient;
import com.ygkj.flood.mapper.AttAdcdBaseMapper;
import com.ygkj.flood.mapper.ExtremWaterMapper;
import com.ygkj.flood.mapper.StationMapper;
import com.ygkj.flood.mapper.WaterLevelAnalyseMapper;
import com.ygkj.flood.model.*;
import com.ygkj.flood.service.WaterLevelAnalyseService;
import com.ygkj.flood.vo.request.WaterLevelReqVo;
import com.ygkj.flood.vo.response.*;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WaterLevelAnalyseServiceImpl implements WaterLevelAnalyseService {

    @Resource
    private WaterLevelAnalyseMapper waterLevelAnalyseMapper;

    @Autowired
    private WaterLevelClient waterLevelClient;

    @Resource
    StationMapper stationMapper;

    @Resource
    ExtremWaterMapper waterMapper;

    @Resource
    AttAdcdBaseMapper adcdBaseMapper;

    @Override
    public CommonResult<List<WlaHisHighestWaterLevelRespVo>> hisHighestWaterLevel(String type, String stName, String stCode) {

        String stType = "";
        String scale = "";
        // 水位历史极值列表（RR、ZZ、DD）
        if (StringUtils.isNotBlank(type)) {
            stType = FloatWindowTypeEnum.getValue(type);
            scale = FloatWindowTypeEnum.getScale(type);
        }
        List<WlaHisHighestWaterLevelRespVo> hisHighestWaterLevels = waterLevelAnalyseMapper.getHisHighestWaterLevels(stType, scale, stName, stCode);
        return CommonResult.success(hisHighestWaterLevels);
    }

    @Override
    public CommonResult<List<WlaWarnNumStatisticsRespVo>> warnNumStatistics() {

        List<WlaWarnNumStatisticsRespVo> respVos = waterLevelAnalyseMapper.warnNumStatistics();
        return CommonResult.success(respVos);
    }

    @Override
    public CommonResult<WlaOverWarnNumRespVo> overWarnStatistics() {

        WlaOverWarnNumRespVo respVo = new WlaOverWarnNumRespVo();

        WaterLevelReqVo requestVo = new WaterLevelReqVo();
        requestVo.setStationType("RR,ZZ");
        // 获取测站信息（状态）
        List<StBase> stBases = getStBases();
        if (!CollectionUtils.isEmpty(stBases)) {
            // 超汛限数量
            int limitNum = (int) stBases.stream().filter(stBase -> WaterStationType.SUPER_LIMIT.type().equals(stBase.getStatus())).count();
            respVo.setLimitNum(limitNum);

            // 超警戒数量
            int warnNum = (int) stBases.stream().filter(stBase -> WaterStationType.SUPER_WARNING.type().equals(stBase.getStatus())).count();
            respVo.setWarnNum(warnNum);

            // 超保证数量
            int guaranteeNum = (int) stBases.stream().filter(stBase -> WaterStationType.SUPER_GUARANTEE.type().equals(stBase.getStatus())).count();
            respVo.setGuaranteeNum(guaranteeNum);
        }
        return CommonResult.success(respVo);
    }

    @Override
    public CommonResult<List<WlaFloatWinWaterLevelRespVo>> floatWinWaterLevel(String type, String name, String adcd, String stCode) {

        // 大中型水库（实时水位、汛限水位、防洪高水位）、河道/闸坝（实时水位、警戒水位、保证水位）、潮位（实时潮位、蓝色警戒潮位）
        // 先做水库,查询大中型水库测站相关的汛限水位、防洪高水位
        List<WlaFloatWinWaterLevelRespVo> respVos = waterLevelAnalyseMapper
                .floatWinWaterLevel(FloatWindowTypeEnum.getValue(type), FloatWindowTypeEnum.getScale(type), name, adcd, stCode);
        if (CollectionUtils.isEmpty(respVos)) {
            return CommonResult.success(respVos);
        }

        // 查询水库实时水位
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String tableName = StationType.RESERVOIR.table() + yyyyMM;
        List<StRiverRYyyymm> realWaterLevels = waterLevelAnalyseMapper.getRealWaterLevel(tableName);

        if (!CollectionUtils.isEmpty(realWaterLevels)) {
            Map<String, Double> stRealWl = realWaterLevels.stream().filter(rwl -> StringUtils.isNotBlank(rwl.getStcd()))
                    .collect(Collectors.toMap(rwl -> rwl.getStcd(), rwl -> rwl.getZ()));

            for (WlaFloatWinWaterLevelRespVo respVo : respVos) {
                String staCode = respVo.getStCode();
                if (stRealWl.containsKey(staCode)) {
                    Double z = stRealWl.get(staCode);
                    Double limitLevel = respVo.getLimitLevel();
                    // 判断超汛限
                    if (null != z && null != limitLevel) {
                        if (z >= limitLevel) {
                            respVo.setWarnStatus("1");
                        }
                    }
                    respVo.setZ(z);
                }
            }
        }
        return CommonResult.success(respVos);
    }

    @Override
    public List<WarningStationResVo> selectTodayWarningStation() {
        return waterLevelAnalyseMapper.selectTodayWarningStation();
    }

    @Override
    public List<Map<String, Object>> getAllStWatLev(String stName) {
        List<Map<String, Object>> result = new ArrayList<>();
        CommonResult<List<StBase>> res = waterLevelClient.waterStations(null, stName, StationType.RESERVOIR.type());
        List<StBase> resList = res.getData();
        if (resList == null) {
            resList = Collections.emptyList();
        }
        CommonResult<List<StBase>> was = waterLevelClient.waterStations(null, stName, StationType.GATE.type());
        List<StBase> wasList = was.getData();
        if (wasList == null) {
            wasList = Collections.emptyList();
        }
        CommonResult<List<StBase>> river = waterLevelClient.waterStations(null, stName, StationType.RIVER.type());
        List<StBase> riverList = river.getData();
        if (riverList == null) {
            riverList = Collections.emptyList();
        }
        for (StBase stBase : resList) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("stName", stBase.getStName());
            temp.put("waterLevel", stBase.getWaterLevel());
            result.add(temp);
        }
        for (StBase stBase : wasList) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("stName", stBase.getStName());
            temp.put("waterLevel", stBase.getUpz());
            result.add(temp);
        }
        for (StBase stBase : riverList) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("stName", stBase.getStName());
            temp.put("waterLevel", stBase.getWaterLevel());
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getHistoricWaterLevelExtremum(String stName) {
        return waterMapper.selectExtremWater(stName);
    }

//    /**
//     * 平原
//     */
//    static List<String> plainStCodeList = new ArrayList<>();
//    /**
//     * 流域
//     */
//    static List<String> basStCodeList = new ArrayList<>();
//
//    static {
//
//        plainStCodeList.addAll(Arrays.asList("SWZ_05624",
//                "SWZ_02941",
//                "SWZ_01254",
//                "SWZ_02940",
//                "SWZ_02567",
//                "SWZ_01202",
//                "SWZ_13366"
//        ));
//
//        basStCodeList.addAll(Arrays.asList("SWZ_09614",
//                "SWZ_09616",
//                "SWZ_05815",
//                "SWZ_15032",
//                "SWZ_05818",
//                "SWZ_05813",
//                "SWZ_15069"));
//    }


    /**
     * type=1 为平原水位
     * type=2 为流域水位
     * type=3 为内河水位
     *
     * @param stName
     * @param type
     * @return
     */
    @Override
    public List<RealWaterLevel> watLevOfPlainBasInnerRiv(String stName, Integer type) {
        String mark = null;
        if (type == null) {
            return Collections.emptyList();
        }
        switch (type) {
            case 1:
                mark = "平原";
                break;
            case 2:
                mark = "流域";
                break;
            case 3:
                mark = "内河";
                break;
            default:
                break;
        }
        if (StringUtils.isBlank(mark)) {
            return Collections.emptyList();
        }
        List<StBase> stBases = this.stationMapper.loadStByMark(mark, stName);
        Map<String, List<StBase>> stMap = stBases.stream().filter(o -> !StationType.RAINFALL.type().equals(o.getStType())).collect(Collectors.groupingBy(StBase::getStType));
        Map<String, Double> wlMap = new HashMap<>(stBases.size());
        List<StBase> riverStBases = stMap.get(StationType.RIVER.type());
        List<StBase> gateStBases = stMap.get(StationType.GATE.type());
        List<StBase> tideStBases = stMap.get(StationType.TIDE.type());
        List<StBase> rsvrStBases = stMap.get(StationType.RESERVOIR.type());
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        if (com.ygkj.utils.CollectionUtils.isNotBlank(riverStBases)) {
            String tableName = StationType.RIVER.table() + yyyyMM;
            List<String> stCodeList = riverStBases.stream().map(StBase::getStCode).collect(Collectors.toList());
            List<StRiverRYyyymm> realWaterLevels = waterLevelAnalyseMapper.getRiverRealWaterLevelByStCodeList(tableName, stCodeList);
            Map<String, Double> tempMap = realWaterLevels.stream().filter(rwl -> StringUtils.isNotBlank(rwl.getStcd()))
                    .collect(Collectors.toMap(rwl -> rwl.getStcd(), rwl -> rwl.getZ()));
            wlMap.putAll(tempMap);
        }
        if (com.ygkj.utils.CollectionUtils.isNotBlank(gateStBases)) {
            String tableName = StationType.GATE.table() + yyyyMM;
            List<String> stCodeList = gateStBases.stream().map(StBase::getStCode).collect(Collectors.toList());
            List<StRiverRYyyymm> realWaterLevels = waterLevelAnalyseMapper.getGateRealWaterLevelByStCodeList(tableName, stCodeList);
            Map<String, Double> tempMap = realWaterLevels.stream().filter(rwl -> StringUtils.isNotBlank(rwl.getStcd()))
                    .collect(Collectors.toMap(rwl -> rwl.getStcd(), rwl -> rwl.getZ()));
            wlMap.putAll(tempMap);
        }
        if (com.ygkj.utils.CollectionUtils.isNotBlank(tideStBases)) {
            String tableName = StationType.TIDE.table() + yyyyMM;
            List<String> stCodeList = tideStBases.stream().map(StBase::getStCode).collect(Collectors.toList());
            List<StRiverRYyyymm> realWaterLevels = waterLevelAnalyseMapper.getTideRealWaterLevelByStCodeList(tableName, stCodeList);
            Map<String, Double> tempMap = realWaterLevels.stream().filter(rwl -> StringUtils.isNotBlank(rwl.getStcd()))
                    .collect(Collectors.toMap(rwl -> rwl.getStcd(), rwl -> rwl.getZ()));
            wlMap.putAll(tempMap);
        }

        if (com.ygkj.utils.CollectionUtils.isNotBlank(rsvrStBases)) {
            String tableName = StationType.RESERVOIR.table() + yyyyMM;
            List<String> stCodeList = rsvrStBases.stream().map(StBase::getStCode).collect(Collectors.toList());
            List<StRiverRYyyymm> realWaterLevels = waterLevelAnalyseMapper.getRsvrRealWaterLevelByStCodeList(tableName, stCodeList);
            Map<String, Double> tempMap = realWaterLevels.stream().filter(rwl -> StringUtils.isNotBlank(rwl.getStcd()))
                    .collect(Collectors.toMap(rwl -> rwl.getStcd(), rwl -> rwl.getZ()));
            wlMap.putAll(tempMap);
        }

        List<RealWaterLevel> result = new ArrayList<>();
        for (StBase stBase : stBases) {
            RealWaterLevel realWaterLevel = new RealWaterLevel();
            realWaterLevel.setStType(stBase.getStType());
            realWaterLevel.setGuaranteeLevel(stBase.getGuaranteeLevel());
            realWaterLevel.setWarningLevel(stBase.getWarningLevel());
            realWaterLevel.setStCode(stBase.getStCode());
            realWaterLevel.setStName(stBase.getStName());
            realWaterLevel.setLatitude(stBase.getStLat());
            realWaterLevel.setLongitude(stBase.getStLong());
            realWaterLevel.setRealLevel(wlMap.getOrDefault(stBase.getStCode(), 0D));
            result.add(realWaterLevel);
        }
        return result;
    }

    @Override
    public List<StBase> innerRiverWaterLevel(String stName) {
        List<StBase> stBases = this.stationMapper.loadStByMark("内河", stName);
        Map<String, List<StBase>> stMap = stBases.stream().filter(o -> StationType.RIVER.type().equals(o.getStType())).collect(Collectors.groupingBy(StBase::getStType));
        Map<String, Double> wlMap = new HashMap<>(stBases.size());
        List<StBase> riverStBases = stMap.get(StationType.RIVER.type());
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        Map<String, String> areaMap = Collections.EMPTY_MAP;
        if (com.ygkj.utils.CollectionUtils.isNotBlank(riverStBases)) {
            String tableName = StationType.RIVER.table() + yyyyMM;
            List<String> stCodeList = riverStBases.stream().map(StBase::getStCode).collect(Collectors.toList());
            List<StRiverRYyyymm> realWaterLevels = waterLevelAnalyseMapper.getRiverRealWaterLevelByStCodeList(tableName, stCodeList);
            Map<String, Double> tempMap = realWaterLevels.stream().filter(rwl -> StringUtils.isNotBlank(rwl.getStcd()))
                    .collect(Collectors.toMap(rwl -> rwl.getStcd(), rwl -> rwl.getZ()));
            wlMap.putAll(tempMap);
            String areaCodes = stBases.stream().map(StBase::getAreaCode).filter(StringUtils::isNotBlank).collect(Collectors.joining(","));
            if (StringUtils.isNotBlank(areaCodes)) {
                List<AttAdcdBase> temp = this.adcdBaseMapper.selectAdcdNameByAdcds(areaCodes);
                if (com.ygkj.utils.CollectionUtils.isNotBlank(tempMap)) {
                    areaMap = temp.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd,
                            AttAdcdBase::getAdnm));
                }
            }
        }

        for (StBase stBase : stBases) {
            stBase.setWaterLevel(wlMap.getOrDefault(stBase.getStCode(), 0D));
            stBase.setOverWarnLevel(stBase.getWaterLevel() - stBase.getWarningLevel());
            stBase.setWarning(stBase.getOverWarnLevel() >= 0);
            stBase.setOverGuarantee(stBase.getGuaranteeLevel() != null && stBase.getWaterLevel() >= stBase.getGuaranteeLevel());
            if (StringUtils.isNotBlank(stBase.getAreaCode())) {
                stBase.setAreaName(areaMap.getOrDefault(stBase.getAreaCode(), StringUtils.EMPTY));
            }
        }
        return stBases.stream()
                .sorted((o1, o2) -> o2.getOverWarnLevel().compareTo(o1.getOverWarnLevel()))
                .collect(Collectors.toList());
    }

    /**
     * 获取测站信息
     *
     * @return
     */
    public List<StBase> getStBases() {

        // 获取测站(当前只统计大中型水库)
        WaterLevelReqVo requestVo = new WaterLevelReqVo();
        requestVo.setStationType("RR");
        List<StBase> stBases = waterLevelAnalyseMapper.stBases(requestVo);

        // 获取水库水位、河道水位
        // 查询水库实时水位
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String tableName = StationType.RESERVOIR.table() + yyyyMM;
        List<StRiverRYyyymm> realWaterLevels = waterLevelAnalyseMapper.getRealWaterLevel(tableName);
        if (!CollectionUtils.isEmpty(realWaterLevels)) {
            Map<String, StRiverRYyyymm> collect = realWaterLevels.stream().collect(Collectors.toMap(StRiverRYyyymm::getStcd, r -> r));
            for (StBase stBase : stBases) {
                String stCode = stBase.getStCode();
                if (collect.containsKey(stCode)) {
                    StRiverRYyyymm stRiverRYyyymm = collect.get(stCode);
                    Double z = stRiverRYyyymm.getZ() == null ? 0d : stRiverRYyyymm.getZ();
                    stBase.setWaterLevel(z);
                    stBase.setTime(stRiverRYyyymm.getTm());
                    if (stBase.getLimitLevel() != null && z != null) {
                        if (z.compareTo(stBase.getLimitLevel()) > 0) {
                            stBase.setWarning(true);
                            stBase.setStatus(WaterStationType.SUPER_LIMIT.type());
                        }
                    }
                }
            }
        }

        // 查询河道实时水位
        String riverTableName = StationType.RIVER.table() + yyyyMM;
        List<StRiverRYyyymm> realRiverWaterLevels = waterLevelAnalyseMapper.getRealRiverWaterLevel(riverTableName);
        if (!CollectionUtils.isEmpty(realRiverWaterLevels)) {
            Map<String, StRiverRYyyymm> collect = realRiverWaterLevels.stream().collect(Collectors.toMap(StRiverRYyyymm::getStcd, r -> r));
            for (StBase stBase : stBases) {
                String stCode = stBase.getStCode();
                if (collect.containsKey(stCode)) {
                    StRiverRYyyymm stRiverRYyyymm = collect.get(stCode);
                    Double z = stRiverRYyyymm.getZ() == null ? 0d : stRiverRYyyymm.getZ();
                    stBase.setWaterLevel(z);
                    stBase.setTime(stRiverRYyyymm.getTm());
                    if (stBase.getWarningLevel() != null && z.compareTo(stBase.getWarningLevel()) > 0) {
                        stBase.setWarning(true);
                        stBase.setStatus(WaterStationType.SUPER_WARNING.type());
                    }
                    if (stBase.getGuaranteeLevel() != null && z.compareTo(stBase.getGuaranteeLevel()) > 0) {
                        stBase.setWarning(true);
                        String status = null;
                        if (StringUtils.isNotBlank(stBase.getStatus())) {
                            status = stBase.getStatus().join(WaterStationType.SUPER_GUARANTEE.type(), ",");
                        }
                        stBase.setStatus(status);
                    }
                }
            }
        }

        return stBases;
    }

}
