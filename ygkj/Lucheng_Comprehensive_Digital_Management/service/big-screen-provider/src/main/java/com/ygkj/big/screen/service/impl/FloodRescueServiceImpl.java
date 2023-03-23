package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.enums.StationTypes;
import com.ygkj.big.screen.mapper.FloodRescueMapper;
import com.ygkj.big.screen.service.FloodRescueService;
import com.ygkj.big.screen.mapper.RiverProtectionMapper;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.response.FrWaterLevelStaRespVo;
import com.ygkj.big.screen.vo.response.WzAdcdInfo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fml
 * @date 2021-03-29 14:21
 * @description 防汛抢险专题
 */
@Service("floodRescueService")
public class FloodRescueServiceImpl implements FloodRescueService {

    @Resource
    private FloodRescueMapper floodRescueMapper;

    @Resource
    private RiverProtectionMapper riverProtectionMapper;

    /**
     * 十大工程点位（水库、山塘、提防、海塘、水电站、泵站、水闸、闸站、灌区、农饮水）积水点、内河水位点、仓库、队伍以及摄像机
     *
     * @param type 1水库，2山塘，3提防，4海棠，5水电站...
     * @param id   id单查
     * @return CommonResult
     */
    @Override
    public CommonResult mapPoints(String type, String id) {

        switch (type) {
            case "1":
                // 水库
                List<AttResBase> res = floodRescueMapper.getResBases(id);
                return CommonResult.success(res);
            case "2":
                // 山塘 att_mnpd_base
                List<AttMnpdBase> mnpdBases = floodRescueMapper.getMnpdBases(id);
                return CommonResult.success(mnpdBases);
            case "3":
                // 提防
                List<AttDikeBase> dikeBases = floodRescueMapper.getDikeBases(id);
                return CommonResult.success(dikeBases);
            case "4":
                // 海塘
                List<AttSeawallBase> seawallBases = floodRescueMapper.getSeawallBases(id);
                return CommonResult.success(seawallBases);
            case "5":
                // 水电站
                List<AttHystBase> hystBases = floodRescueMapper.getHystsBases(id);
                return CommonResult.success(hystBases);
            case "6":
                // 泵站
                List<AttPustBase> pustBases = floodRescueMapper.getPustBases(id);
                return CommonResult.success(pustBases);
            case "7":
                // 水闸
                List<AttWagaBase> wagaBases = floodRescueMapper.getWagaBases(id);
                return CommonResult.success(wagaBases);
            case "8":
                // 农饮水
                List<WaterSupplyProject> supplyProjects = floodRescueMapper.getWsProjs(id);
                return CommonResult.success(supplyProjects);
            case "9":
                // 内河水位点
                List<AttStBase> riverStBases = floodRescueMapper.getRiverStBases(id);
                return CommonResult.success(riverStBases);
            case "10":
                // 仓库
                List<AttWarehouseBase> warehouseBases = floodRescueMapper.getWarehouseBases(id);
                return CommonResult.success(warehouseBases);
            case "11":
                // 队伍
                List<AttTeamBase> teamBases = floodRescueMapper.getTeamBases(id);
                return CommonResult.success(teamBases);
            case "12":
                // 摄像机
                List<HikvisionCameraRecord> cameraRecords = floodRescueMapper.getCameraRecords(id);
                return CommonResult.success(cameraRecords);
            default:
                return CommonResult.success(new ArrayList<>());
        }
    }

    /**
     * 仓库数量统计
     *
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WzAdcdInfo>> wareHouseStats() {

        // 获取行政区划信息
        List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();
        if (CollectionUtils.isBlank(wzAdcds)) {
            return CommonResult.success(wzAdcds);
        }

        // 获取仓库数量统计信息
        List<Map<String, Object>> warehoseStats = floodRescueMapper.wareHouseAdcdStats();
        if (CollectionUtils.isNotBlank(warehoseStats)) {
            Map<String, Long> collect = warehoseStats.stream().filter(ws -> StringUtils.isNotBlank((String) ws.get("adcd")))
                    .collect(Collectors.toMap(ws -> (String) ws.get("adcd"), ws -> (Long) ws.get("num")));

            for (WzAdcdInfo wzAdcd : wzAdcds) {
                String adcd = wzAdcd.getAdcd();
                if (collect.containsKey(adcd)) {
                    wzAdcd.setNum(collect.get(adcd).intValue());
                }
            }
        }
        return CommonResult.success(wzAdcds);
    }

    @Override
    public CommonResult<List<WzAdcdInfo>> teamStats() {

        // 获取行政区划信息
        List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();
        if (CollectionUtils.isBlank(wzAdcds)) {
            return CommonResult.success(wzAdcds);
        }

        // 获取抢险队伍数量统计信息
        List<Map<String, Object>> teamStats = floodRescueMapper.teamAdcdStats();
        if (CollectionUtils.isNotBlank(teamStats)) {
            Map<String, Integer> collect = teamStats.stream().filter(ts -> StringUtils.isNotBlank((String) ts.get("adcd")))
                    .collect(Collectors.toMap(ts -> (String) ts.get("adcd"), ts -> (Integer) ts.get("num")));

            for (WzAdcdInfo wzAdcd : wzAdcds) {
                String adcd = wzAdcd.getAdcd();
                if (collect.containsKey(adcd)) {
                    wzAdcd.setNum(collect.get(adcd));
                }
            }
        }
        return CommonResult.success(wzAdcds);
    }

    @Override
    public CommonResult<List<WzAdcdInfo>> teamPersonStats() {
        // 获取行政区划信息
        List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();
        if (CollectionUtils.isBlank(wzAdcds)) {
            return CommonResult.success(wzAdcds);
        }

        // 获取抢险队伍人员数量统计信息
        List<Map<String, Object>> teamPersonStats = floodRescueMapper.teamPersonAdcdStats();
        if (CollectionUtils.isNotBlank(teamPersonStats)) {
            Map<String, Integer> collect = teamPersonStats.stream().filter(ts -> StringUtils.isNotBlank((String) ts.get("adcd")))
                    .collect(Collectors.toMap(ts -> (String) ts.get("adcd"), ts -> (Integer) ts.get("num")));

            for (WzAdcdInfo wzAdcd : wzAdcds) {
                String adcd = wzAdcd.getAdcd();
                if (collect.containsKey(adcd)) {
                    wzAdcd.setNum(collect.get(adcd));
                }
            }
        }
        return CommonResult.success(wzAdcds);
    }

    /**
     * 水利工程统计（按区划、按工程类型） 水库、山塘、提防、海塘、水电站、泵站、水闸、闸站、灌区、农饮水
     *
     * @param type 1按区划、2按工程类型
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WzAdcdInfo>> projStats(String type) {

        List<WzAdcdInfo> wzAdcds = new ArrayList<>();

        if ("1".equals(type)) {
            // 获取行政区划信息
            wzAdcds = riverProtectionMapper.getWzAdcds();
            if (CollectionUtils.isBlank(wzAdcds)) {
                return CommonResult.success(wzAdcds);
            }

            // 获取工程行政区划内数量统计
            List<Map<String, Object>> teamPersonStats = floodRescueMapper.projAdcdStats();
            if (CollectionUtils.isNotBlank(teamPersonStats)) {
                Map<String, Integer> collect = teamPersonStats.stream().filter(ts -> StringUtils.isNotBlank((String) ts.get("adcd")))
                        .collect(Collectors.toMap(ts -> (String) ts.get("adcd"), ts -> (Integer) ts.get("num")));

                for (WzAdcdInfo wzAdcd : wzAdcds) {
                    String adcd = wzAdcd.getAdcd();
                    if (collect.containsKey(adcd)) {
                        wzAdcd.setNum(collect.get(adcd));
                    }
                }
            }
            return CommonResult.success(wzAdcds);
        }
        if ("2".equals(type)) {
            wzAdcds = floodRescueMapper.projTypeStats();
        }
        return CommonResult.success(wzAdcds);
    }

    @Override
    public CommonResult<List<FrWaterLevelStaRespVo>> waterLevels() {

        List<FrWaterLevelStaRespVo> respVos = new ArrayList<>();

        // 获取水库、河道水位测站信息
        List<FrWaterLevelStaRespVo> waterLevelsRespVos = floodRescueMapper.getWatertLevelStas();
        if (CollectionUtils.isBlank(waterLevelsRespVos)) {
            return CommonResult.success(respVos);
        }

        // 获取水库水位测站实时水位
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        List<Map<String, Object>> resRealWaterLevels = floodRescueMapper.getResRealWaterLevels(StationTypes.RESERVOIR.table() + yyyyMM);
        if (CollectionUtils.isNotBlank(resRealWaterLevels)) {
            List<FrWaterLevelStaRespVo> rrList = waterLevelsRespVos.stream().filter(vo -> "RR".equals(vo.getStType())).collect(Collectors.toList());
            if (CollectionUtils.isNotBlank(rrList)) {
                Map<String, BigDecimal> collect = resRealWaterLevels.stream().filter(rl -> StringUtils.isNotBlank((String) rl.get("stcd")))
                        .collect(Collectors.toMap(rl -> (String) rl.get("stcd"), rl -> (BigDecimal) rl.get("z")));
                for (FrWaterLevelStaRespVo respVo : rrList) {
                    String stCode = respVo.getStCode();
                    if (collect.containsKey(stCode)) {
                        respVo.setZ(collect.get(stCode).doubleValue());
                    }
                }

                respVos.addAll(rrList);
            }
        }

        // 获取河道水位测站实时水位
        List<Map<String, Object>> riverRealWaterLevels = floodRescueMapper.getRiverRealWaterLevels(StationTypes.RIVER.table() + yyyyMM);
        if (CollectionUtils.isNotBlank(riverRealWaterLevels)) {
            List<FrWaterLevelStaRespVo> zzList = waterLevelsRespVos.stream().filter(vo -> "ZZ".equals(vo.getStType())).collect(Collectors.toList());
            if (CollectionUtils.isNotBlank(zzList)) {
                Map<String, BigDecimal> collect = riverRealWaterLevels.stream().filter(rl -> StringUtils.isNotBlank((String) rl.get("stcd")))
                        .collect(Collectors.toMap(rl -> (String) rl.get("stcd"), rl -> (BigDecimal) rl.get("z")));
                for (FrWaterLevelStaRespVo respVo : zzList) {
                    String stCode = respVo.getStCode();
                    if (collect.containsKey(stCode)) {
                        respVo.setZ(collect.get(stCode).doubleValue());
                    }
                }

                respVos.addAll(zzList);
            }
        }

        return CommonResult.success(respVos);
    }

    /**
     * 工情轮播
     *
     * @param type 1水闸，2水泵，3水库
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult projConditions(String type) {

        List<FrWaterLevelStaRespVo> waterLevelsRespVos = new ArrayList<>();

        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

        // 水闸
        if ("1".equals(type)) {
            // 获取水闸测站信息
            waterLevelsRespVos = floodRescueMapper.getStBases("DD");
            if (CollectionUtils.isBlank(waterLevelsRespVos)) {
                return CommonResult.success(waterLevelsRespVos);
            }

            // 获取水闸水位信息
            List<Map<String, Object>> szWaterLevels = floodRescueMapper.getSzWaterLevels(StationTypes.GATE.table() + yyyyMM);
            if (CollectionUtils.isNotBlank(szWaterLevels)) {
                Map<String, Map<String, Object>> collect = szWaterLevels.stream().filter((sz -> StringUtils.isNotBlank((String) sz.get("stcd"))))
                        .collect(Collectors.toMap(sz -> (String) sz.get("stcd"), sz -> sz));
                for (FrWaterLevelStaRespVo respVo : waterLevelsRespVos) {
                    String stCode = respVo.getStCode();
                    if (collect.containsKey(stCode)) {
                        Map<String, Object> szMap = collect.get(stCode);
                        respVo.setUpz(szMap.get("upz") == null ? null : ((BigDecimal) szMap.get("upz")).doubleValue());
                        respVo.setDwz(szMap.get("dwz") == null ? null : ((BigDecimal) szMap.get("dwz")).doubleValue());
                        respVo.setOvs(szMap.get("ovs") == null ? null : (String) szMap.get("ovs"));
                    }
                }
            }
        }

        // 水库
        if ("3".equals(type)) {
            // 获取水库水位测站
            waterLevelsRespVos = floodRescueMapper.getStBases("RR");
            if (CollectionUtils.isBlank(waterLevelsRespVos)) {
                return CommonResult.success(waterLevelsRespVos);
            }

            // 获取水库水位
            List<Map<String, Object>> resRealWaterLevels = floodRescueMapper.getResRealWaterLevels(StationTypes.RESERVOIR.table() + yyyyMM);
            if (CollectionUtils.isNotBlank(resRealWaterLevels)) {
                Map<String, BigDecimal> collect = resRealWaterLevels.stream()
                        .filter(rl -> StringUtils.isNotBlank((String) rl.get("stcd")) && rl.get("z") != null)
                        .collect(Collectors.toMap(rl -> (String) rl.get("stcd"), rl -> (BigDecimal) rl.get("z")));
                for (FrWaterLevelStaRespVo respVo : waterLevelsRespVos) {
                    String stCode = respVo.getStCode();
                    if (collect.containsKey(stCode)) {
                        respVo.setZ(collect.get(stCode).doubleValue());
                    }
                }
            }
        }
        return CommonResult.success(waterLevelsRespVos);
    }

}
