package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.enums.StationTypes;
import com.ygkj.big.screen.mapper.WaterAffairsMapper;
import com.ygkj.big.screen.service.WaterAffairsService;
import com.ygkj.big.screen.unit.CwsWaterManager;
import com.ygkj.big.screen.utils.CurveUtil;
import com.ygkj.big.screen.utils.DoubleUtils;
import com.ygkj.big.screen.mapper.RiverProtectionMapper;
import com.ygkj.big.screen.model.HikvisionCameraRecord;
import com.ygkj.big.screen.model.ReservoirCapacityCurve;
import com.ygkj.big.screen.model.WtSoilErosionProjBase;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.utils.Builder;
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
 * @date 2021-03-30 11:28
 * @description 水事务监管专题
 */
@Service("waterAffairsService")
public class WaterAffairsServiceImpl implements WaterAffairsService {

    @Resource
    private WaterAffairsMapper waterAffairsMapper;

    @Resource
    private RiverProtectionMapper riverProtectionMapper;

    @Resource
    private CwsWaterManager cwsWaterManager;
//    @Resource
//    private WaterOperateManager waterOperateManager;


    /**
     * 水土流失图斑
     *
     * @return CommonResult
     */
    @Override
    public CommonResult<List<WaSoilErosionRespVo>> soilEroMapPoints() {

        // 获取当前年水土流失图斑信息 wt_soil_erosion_rs（遥感影像），shp文件未作解析
        List<WaSoilErosionRespVo> soilEros = waterAffairsMapper.getSoilEros(String.valueOf(LocalDate.now().getYear()));
        return CommonResult.success(soilEros);
    }

    /**
     * 治理工程图斑（工程边界）
     *
     * @param id id
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WtSoilErosionProjBase>> projMapPoints(String id) {

        // 获取当前年工程图斑（工程边界）
        List<WtSoilErosionProjBase> soilErosionProjBases = waterAffairsMapper.getSoilEroProjs(String.valueOf(LocalDate.now().getYear()), id);
        return CommonResult.success(soilErosionProjBases);
    }

    /**
     * 摄像头统计
     *
     * @param warnStatus 1正常，2异常
     * @param stats      1区划，2工程类型
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WzAdcdInfo>> cameraStats(String warnStatus, String stats) {

        if ("1".equals(stats)) {
            // 获取温州行政区划信息
            List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();
            if (CollectionUtils.isBlank(wzAdcds)) {
                return CommonResult.success(wzAdcds);
            }
            List<Map<String, Object>> cameraAdcdStats = waterAffairsMapper.getCameraAdcdStats(warnStatus);
            if (CollectionUtils.isNotBlank(cameraAdcdStats)) {
                Map<String, Long> collect = cameraAdcdStats.stream().filter(c -> c.get("adcd") != null)
                        .collect(Collectors.toMap(c -> (String) c.get("adcd"), c -> (Long) c.get("num")));
                for (WzAdcdInfo wzAdcd : wzAdcds) {
                    String adcd = wzAdcd.getAdcd();
                    if (collect.containsKey(adcd)) {
                        wzAdcd.setNum(collect.get(adcd).intValue());
                    }
                }
            }
            return CommonResult.success(wzAdcds);
        }
        if ("2".equals(stats)) {
            List<WzAdcdInfo> projTypeCameraStats = waterAffairsMapper.getProjTypeCameraStats(warnStatus);
            return CommonResult.success(projTypeCameraStats);
        }

        return CommonResult.success(new ArrayList<>());
    }

    @Override
    public CommonResult<List<HikvisionCameraRecord>> cameraPoints(String status) {

        List<HikvisionCameraRecord> cameraRecords = waterAffairsMapper.getCameras(status);
        return CommonResult.success(cameraRecords);
    }

    /**
     * 水库超汛限占比
     *
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WuWiuStatsRespVo>> resOverLimitStats() {

        List<WuWiuStatsRespVo> statsRespVos = new ArrayList<>();

        List<RpWaterLevelsRespVo> resWaterLevels = getResWaterLevels();
        if (CollectionUtils.isBlank(resWaterLevels)) {
            return CommonResult.success(statsRespVos);
        }

        WuWiuStatsRespVo respVo = new WuWiuStatsRespVo();
        respVo.setText("超汛限");
        respVo.setNum((int) resWaterLevels.stream().filter(res -> "0".equals(res.getStatus())).count());

        WuWiuStatsRespVo respVo1 = new WuWiuStatsRespVo();
        respVo1.setText("正常");
        respVo1.setNum((int) resWaterLevels.stream().filter(res -> "1".equals(res.getStatus())).count());

        statsRespVos.add(respVo);
        statsRespVos.add(respVo1);
        return CommonResult.success(statsRespVos);
    }

    /**
     * 各区县水库平均蓄水率=实时库容/总库容
     *
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WzAdcdInfo>> waterStorageStats() {

        // 获取温州行政区划信息
        List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();
        if (CollectionUtils.isBlank(wzAdcds)) {
            return CommonResult.success(wzAdcds);
        }

        // 获取水库关联水库测站
        List<WaResStaWaterLevelInfo> resStaWaterLevelInfos = waterAffairsMapper.getResStas();
        if (CollectionUtils.isBlank(resStaWaterLevelInfos)) {
            return CommonResult.success(wzAdcds);
        }

        // 过滤掉未绑定测站和总库容为空的水库
        List<WaResStaWaterLevelInfo> existStInfos = resStaWaterLevelInfos.stream()
                .filter(res -> res.getTotCap() != null && StringUtils.isNotBlank(res.getStCode())).collect(Collectors.toList());
        if (CollectionUtils.isBlank(existStInfos)) {
            return CommonResult.success(wzAdcds);
        }

        // 获取水位测站水位信息
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        List<Map<String, Object>> resRealWaterLevels = waterAffairsMapper.getResWaterLevels(StationTypes.RESERVOIR.table() + yyyyMM);
        if (CollectionUtils.isNotBlank(resRealWaterLevels)) {
            Map<String, BigDecimal> collect = resRealWaterLevels.stream()
                    .filter(rl -> StringUtils.isNotBlank((String) rl.get("stcd")) && rl.get("z") != null)
                    .collect(Collectors.toMap(rl -> (String) rl.get("stcd"), rl -> (BigDecimal) rl.get("z")));
            // 绑定实时水位
            for (WaResStaWaterLevelInfo existStInfo : existStInfos) {
                String stCode = existStInfo.getStCode();
                if (collect.containsKey(stCode)) {
                    existStInfo.setZ(collect.get(stCode).doubleValue());
                }
            }
        }

        // 过滤掉实时水位为空的测站数据
        List<WaResStaWaterLevelInfo> collect = existStInfos.stream().filter(st -> st.getZ() != null).collect(Collectors.toList());
        if (CollectionUtils.isNotBlank(collect)) {
            // 遍历 获取库容曲线（按水位排序），计算库容
            for (WaResStaWaterLevelInfo waResStaWaterLevelInfo : collect) {
                String stCode = waResStaWaterLevelInfo.getStCode();
                List<ReservoirCapacityCurve> capacityCurves = waterAffairsMapper.getCapacityCurveByStCode(stCode);
                if (CollectionUtils.isNotBlank(capacityCurves)) {
                    Double capacity = CurveUtil.getYByRz(waResStaWaterLevelInfo.getZ(), capacityCurves);
                    waResStaWaterLevelInfo.setRealCapacity(capacity);
                }
            }

            // 按行政区划分组，遍历计算蓄水率
            Map<String, List<WaResStaWaterLevelInfo>> resMap = collect.stream()
                    .filter(res -> StringUtils.isNotBlank(res.getAdcd()) && res.getRealCapacity() != null)
                    .collect(Collectors.groupingBy(WaResStaWaterLevelInfo::getAdcd));
            for (WzAdcdInfo wzAdcd : wzAdcds) {
                String adcd = wzAdcd.getAdcd();
                if (resMap.containsKey(adcd)) {
                    List<WaResStaWaterLevelInfo> resInfos = resMap.get(adcd);
                    double totCap = resInfos.stream().mapToDouble(WaResStaWaterLevelInfo::getTotCap).sum();
                    double realCap = resInfos.stream().mapToDouble(WaResStaWaterLevelInfo::getRealCapacity).sum();
                    Double div = DoubleUtils.div(realCap, totCap);
                    wzAdcd.setValue(div);
                }
            }
        }

        return CommonResult.success(wzAdcds);
    }

    /**
     * 水土流失面积统计
     *
     * @param type 1按年份,2按行政区划
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult soilErosionStats(String type) {
        List<WzAdcdInfo> adcdInfos = new ArrayList<>();
        if ("1".equals(type)) {
            adcdInfos = waterAffairsMapper.yearSoilErosionStats();
        }
        if ("2".equals(type)) {
            // 获取温州行政区划信息
            adcdInfos = riverProtectionMapper.getWzAdcds();

            if (CollectionUtils.isNotBlank(adcdInfos)) {
                // 获取当前年水土流失信息
                List<WzAdcdInfo> soilEroStats = waterAffairsMapper.adcdSoilErosionStats(String.valueOf(LocalDate.now().getYear()));
                if (CollectionUtils.isNotBlank(soilEroStats)) {
                    Map<String, Double> collect = soilEroStats.stream().collect(Collectors.toMap(WzAdcdInfo::getAdcd, WzAdcdInfo::getValue));
                    for (WzAdcdInfo wzAdcd : adcdInfos) {
                        String adcd = wzAdcd.getAdcd();
                        if (collect.containsKey(adcd)) {
                            wzAdcd.setValue(collect.get(adcd));
                        }
                    }
                }
            }
        }
        return CommonResult.success(adcdInfos);
    }

    /**
     * 水土流失-治理图斑统计-治理面积
     *
     * @param type （1按年份,2按行政区划）
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult soilErosionGovernStats(String type) {
        List<WzAdcdInfo> adcdInfos = new ArrayList<>();
        if ("1".equals(type)) {
            adcdInfos = waterAffairsMapper.yearSoilErosionGovernStats();
        }
        if ("2".equals(type)) {
            // 获取温州行政区划信息
            adcdInfos = riverProtectionMapper.getWzAdcds();

            if (CollectionUtils.isNotBlank(adcdInfos)) {
                // 获取当前年水土流失治理面积信息
                List<WzAdcdInfo> soilEroStats = waterAffairsMapper.adcdSoilErosionGovernStats(String.valueOf(LocalDate.now().getYear()));
                if (CollectionUtils.isNotBlank(soilEroStats)) {
                    Map<String, Double> collect = soilEroStats.stream().collect(Collectors.toMap(WzAdcdInfo::getAdcd, WzAdcdInfo::getValue));
                    for (WzAdcdInfo wzAdcd : adcdInfos) {
                        String adcd = wzAdcd.getAdcd();
                        if (collect.containsKey(adcd)) {
                            wzAdcd.setValue(collect.get(adcd));
                        }
                    }
                }
            }
        }
        return CommonResult.success(adcdInfos);
    }

    /**
     * 水土流失-治理项目统计
     *
     * @param type （1按年份,2按行政区划）
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult soilErosionProjStats(String type) {
        List<WzAdcdInfo> adcdInfos = new ArrayList<>();
        if ("1".equals(type)) {
            adcdInfos = waterAffairsMapper.yearSoilErosionProjStats();
        }
        if ("2".equals(type)) {
            // 获取温州行政区划信息
            adcdInfos = riverProtectionMapper.getWzAdcds();

            if (CollectionUtils.isNotBlank(adcdInfos)) {
                // 获取当前年水土流失项目信息
                List<WzAdcdInfo> soilEroStats = waterAffairsMapper.adcdSoilErosionProjStats(String.valueOf(LocalDate.now().getYear()));
                if (CollectionUtils.isNotBlank(soilEroStats)) {
                    Map<String, Integer> collect = soilEroStats.stream().collect(Collectors.toMap(WzAdcdInfo::getAdcd, WzAdcdInfo::getNum));
                    for (WzAdcdInfo wzAdcd : adcdInfos) {
                        String adcd = wzAdcd.getAdcd();
                        if (collect.containsKey(adcd)) {
                            wzAdcd.setNum(collect.get(adcd));
                        }
                    }
                }
            }
        }
        return CommonResult.success(adcdInfos);
    }

    /**
     * 水厂统计
     *
     * @param type 1. 根据行政区划统计：城市、乡镇、联村、单村;
     *             2. 根据水厂规模统计：两百吨千人以下、两百吨千人以上、千吨万人、城市水厂；
     *             3. 根据水厂类型统计：城市、乡镇、联村、单村；
     *             4. 按行政区划统计: 正常供水，供水预警，水质预警，水质供水预警
     *             5. 按水厂的运行状态统计:正常供水，供水预警，水质预警，水质供水预警
     *             6. 统计日累计供水量，异常供水次数
     */
    @Override
    public CommonResult waterSupplyStats(int type) {
        return (CommonResult) cwsWaterManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "waterSupplyStats")
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }

//    /**
//     * 水厂运行状态统计
//     * @param result  1按数量 。2按人口
//     * @param data 1统计 2柱状图
//     * @return
//     */
//    @Override
//    public Object waterOperateStats(String result, String data) {
//
//        return waterOperateManager.getData(Builder.of(UnitDataReqVo::new)
//                .with(UnitDataReqVo::setDataParam,result)
//                .with(UnitDataReqVo::setDataApi,data)
//                .build());
//    }

    /**
     * 获取水位测站水位信息
     *
     * @return java.util.List<com.ygkj.big.screen.vo.response.RpWaterLevelsRespVo>
     */
    private List<RpWaterLevelsRespVo> getResWaterLevels() {

        // 获取水库水位测站
        List<RpWaterLevelsRespVo> respVos = waterAffairsMapper.getStBases("RR");
        if (CollectionUtils.isBlank(respVos)) {
            return respVos;
        }

        // 获取实时水位
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        List<Map<String, Object>> resRealWaterLevels = waterAffairsMapper.getResWaterLevels(StationTypes.RESERVOIR.table() + yyyyMM);
        if (CollectionUtils.isNotBlank(resRealWaterLevels)) {
            Map<String, BigDecimal> collect = resRealWaterLevels.stream()
                    .filter(rl -> StringUtils.isNotBlank((String) rl.get("stcd")) && rl.get("z") != null)
                    .collect(Collectors.toMap(rl -> (String) rl.get("stcd"), rl -> (BigDecimal) rl.get("z")));
            for (RpWaterLevelsRespVo respVo : respVos) {
                String stCode = respVo.getStCode();
                Double limitLevel = respVo.getLimitLevel();
                if (collect.containsKey(stCode)) {
                    double z = collect.get(stCode).doubleValue();
                    respVo.setZ(z);
                    if (limitLevel != null) {
                        respVo.setStatus(z > limitLevel ? "0" : "1");
                    }
                }
            }
        }

        return respVos;
    }
}
