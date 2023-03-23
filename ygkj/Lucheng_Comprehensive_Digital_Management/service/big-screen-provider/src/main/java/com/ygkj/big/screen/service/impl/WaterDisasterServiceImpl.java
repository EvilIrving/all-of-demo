package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.enums.StationTypes;
import com.ygkj.big.screen.mapper.WaterAffairsMapper;
import com.ygkj.big.screen.service.WaterDisasterService;
import com.ygkj.big.screen.utils.CurveUtil;
import com.ygkj.big.screen.utils.DoubleUtils;
import com.ygkj.big.screen.mapper.WaterDisasterMapper;
import com.ygkj.big.screen.model.RealTyphoonData;
import com.ygkj.big.screen.model.ReservoirCapacityCurve;
import com.ygkj.big.screen.model.StPptnR;
import com.ygkj.big.screen.vo.response.RainfallStaInfo;
import com.ygkj.big.screen.vo.response.WaResStaWaterLevelInfo;
import com.ygkj.big.screen.vo.response.WuWiuStatsRespVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fml
 * @date 2021-03-31 11:40
 * @description 水灾害防御专题
 */
@Service("waterDisasterService")
public class WaterDisasterServiceImpl implements WaterDisasterService {

    @Resource
    private WaterDisasterMapper waterDisasterMapper;

    @Resource
    private WaterAffairsMapper waterAffairsMapper;

    /**
     * 雨量预警（1小时降雨量超警数量、3小时降雨量超警数量）
     *
     * @param hour 小时，1h、3h
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WuWiuStatsRespVo>> rainfallWarn(Integer hour) {

        List<WuWiuStatsRespVo> respVos = new ArrayList<>();

        List<RainfallStaInfo> staInfos = getRainfallStas(hour);
        if (CollectionUtils.isNotBlank(staInfos)) {
            // 统计预警数
            int count = (int) staInfos.stream().filter(sta -> "0".equals(sta.getStatus())).count();
            WuWiuStatsRespVo respVo = new WuWiuStatsRespVo();
            respVo.setText("预警数");
            respVo.setNum(count);
            respVos.add(respVo);
        }

        return CommonResult.success(respVos);
    }

    /**
     * 雨量预警（1小时、3小时量）统计不同降雨量区间，雨量站的数量 ＜10、10-25、25-50、50-100、100-250、＞250
     *
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WuWiuStatsRespVo>> rainfallInteravalStats(Integer hour) {

        List<WuWiuStatsRespVo> respVos = new ArrayList<>();

        List<RainfallStaInfo> rainfallStas = getRainfallStas(hour);
        if (CollectionUtils.isNotBlank(rainfallStas)) {
            // ＜10、10-25、25-50、50-100、100-250、＞250
            int num = (int) rainfallStas.stream().filter(sta -> sta.getDrp().compareTo(10d) <= 0).count();
            int num1 = (int) rainfallStas.stream().filter(sta -> sta.getDrp().compareTo(10d) > 0 && sta.getDrp().compareTo(25d) <= 0).count();
            int num2 = (int) rainfallStas.stream().filter(sta -> sta.getDrp().compareTo(25d) > 0 && sta.getDrp().compareTo(50d) <= 0).count();
            int num3 = (int) rainfallStas.stream().filter(sta -> sta.getDrp().compareTo(50d) > 0 && sta.getDrp().compareTo(100d) <= 0).count();
            int num4 = (int) rainfallStas.stream().filter(sta -> sta.getDrp().compareTo(100d) > 0 && sta.getDrp().compareTo(250d) <= 0).count();
            int num5 = (int) rainfallStas.stream().filter(sta -> sta.getDrp().compareTo(250d) > 0).count();

            respVos.add(new WuWiuStatsRespVo("0-10", num, null));
            respVos.add(new WuWiuStatsRespVo("10-25", num1, null));
            respVos.add(new WuWiuStatsRespVo("25-50", num2, null));
            respVos.add(new WuWiuStatsRespVo("50-100", num3, null));
            respVos.add(new WuWiuStatsRespVo("100-250", num4, null));
            respVos.add(new WuWiuStatsRespVo("＞250", num5, null));
        }

        return CommonResult.success(respVos);
    }

    /**
     * 水库纳蓄量（大中型水库累加总库容-大中型水库累加实时库容）
     *
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WuWiuStatsRespVo>> largeResCap() {

        List<WuWiuStatsRespVo> respVos = new ArrayList<>();

        // 获取大中型水库测站信息
        List<WaResStaWaterLevelInfo> resStas = waterDisasterMapper.getLargeMediResStas();
        if (CollectionUtils.isBlank(resStas)) {
            return CommonResult.success(respVos);
        }

        // 大中型水库测站编码列表
        List<String> stcdList = resStas.stream().filter(res -> StringUtils.isNotBlank(res.getStCode()))
                .map(WaResStaWaterLevelInfo::getStCode).collect(Collectors.toList());
        if (CollectionUtils.isBlank(stcdList)) {
            return CommonResult.success(respVos);
        }

        // 获取大中型水库测站最新水位
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String tableName = StationTypes.RAINFALL.table() + yyyyMM;
        List<Map<String, Object>> resStaWaterLevels = waterDisasterMapper.getLargeMediResStaWaterLevels(tableName, stcdList);

        if (CollectionUtils.isNotBlank(resStaWaterLevels)) {
            Map<String, BigDecimal> collect = resStaWaterLevels.stream()
                    .collect(Collectors.toMap(res -> (String) res.get("stcd"), res -> (BigDecimal) res.get("z")));
            // 遍历计算实时库容
            for (WaResStaWaterLevelInfo resSta : resStas) {
                String stCode = resSta.getStCode();
                if (collect.containsKey(stCode)) {
                    resSta.setZ(collect.get(stCode) == null ? 0d : collect.get(stCode).doubleValue());
                    List<ReservoirCapacityCurve> capacityCurves = waterAffairsMapper.getCapacityCurveByStCode(stCode);
                    if (CollectionUtils.isNotBlank(capacityCurves)) {
                        Double capacity = CurveUtil.getYByRz(resSta.getZ(), capacityCurves);
                        resSta.setRealCapacity(capacity);
                    }
                }
            }
        }

        // 大中型水库总库容
        double totCap = resStas.stream().filter(res -> res.getTotCap() != null).mapToDouble(WaResStaWaterLevelInfo::getTotCap).sum();
        // 大中型水库总实时库容
        double realCap = resStas.stream().filter(res -> res.getRealCapacity() != null).mapToDouble(WaResStaWaterLevelInfo::getRealCapacity).sum();

        Double sub = DoubleUtils.sub(totCap, realCap);

        respVos.add(new WuWiuStatsRespVo("总纳蓄量", null, sub));

        return CommonResult.success(respVos);
    }

    /**
     * 水库纳蓄能力（纳蓄能力前五，纳蓄能力=（汛限水库对应库容-实时水位对应库容）/集水面积）
     *
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WaResStaWaterLevelInfo>> resCapAbility() {

        // 获取水库关联水库测站(包括集水面积)
        List<WaResStaWaterLevelInfo> resStaWaterLevelInfos = waterDisasterMapper.getResStaAndAreas();
        if (CollectionUtils.isBlank(resStaWaterLevelInfos)) {
            return CommonResult.success(resStaWaterLevelInfos);
        }

        // 过滤掉未绑定测站、总库容为空、集水面积为空的水库
        List<WaResStaWaterLevelInfo> existStInfos = resStaWaterLevelInfos.stream()
                .filter(res -> res.getTotCap() != null && StringUtils.isNotBlank(res.getStCode()) && res.getWatShedArea() != null)
                .collect(Collectors.toList());
        if (CollectionUtils.isBlank(existStInfos)) {
            return CommonResult.success(existStInfos);
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
        if (CollectionUtils.isBlank(collect)) {
            CommonResult.success(collect);
        }
        // 遍历 获取库容曲线（按水位排序），计算库容
        for (WaResStaWaterLevelInfo waResStaWaterLevelInfo : collect) {
            String stCode = waResStaWaterLevelInfo.getStCode();
            List<ReservoirCapacityCurve> capacityCurves = waterAffairsMapper.getCapacityCurveByStCode(stCode);
            if (CollectionUtils.isNotBlank(capacityCurves)) {
                Double capacity = CurveUtil.getYByRz(waResStaWaterLevelInfo.getZ(), capacityCurves);
                waResStaWaterLevelInfo.setRealCapacity(capacity);

                // 计算纳蓄能力 =（汛限水库对应库容-实时水位对应库容）/集水面积）
                Double totCap = waResStaWaterLevelInfo.getTotCap();
                Double watShedArea = waResStaWaterLevelInfo.getWatShedArea();
                if (watShedArea != null && watShedArea > 0d) {
                    Double sub = DoubleUtils.sub(totCap, capacity);
                    Double div = DoubleUtils.div(sub, watShedArea);
                    waResStaWaterLevelInfo.setRate(div);
                }
            }
        }

        // 获取纳蓄能力前5的数据
        List<WaResStaWaterLevelInfo> resStaInfos = collect.stream().filter(st -> st.getRate() != null)
                .sorted(Comparator.comparing(WaResStaWaterLevelInfo::getRate).reversed()).limit(5).collect(Collectors.toList());

        return CommonResult.success(resStaInfos);
    }

    /**
     * 台风等级统计(≤8级、9~10级、11~12级、13~14级、＞14级) real_typhoon_data
     *
     * @param year 年份区间
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WuWiuStatsRespVo>> typhoonPowerStats(Integer year) {

        year = year == null || year <= 0 ? 10 : year;

        int yearPeriod = LocalDate.now().getYear() - year;

        List<WuWiuStatsRespVo> respVos = waterDisasterMapper.typhoonPowerStats(yearPeriod);

        return CommonResult.success(respVos);
    }

    /**
     * 历史台风数量、历史最大台风等级
     *
     * @param year 年份区间
     * @param type 1历史台风数量、2历史最大台风等级
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WuWiuStatsRespVo>> hisTyphoonStats(Integer year, String type) {

        year = year == null || year <= 0 ? 10 : year;
        int yearPeriod = LocalDate.now().getYear() - year;

        List<WuWiuStatsRespVo> respVos = waterDisasterMapper.hisTyphoonStats(yearPeriod, type);

        return CommonResult.success(respVos);
    }

    /**
     * 统计不同年份台风数量
     *
     * @param year 年份区间
     * @return com.ygkj.water.model.CommonResult
     */
    @Override
    public CommonResult<List<WuWiuStatsRespVo>> yearTyphoonStats(Integer year) {

        year = year == null || year <= 0 ? 10 : year;
        int yearPeriod = LocalDate.now().getYear() - year;

        List<WuWiuStatsRespVo> respVos = waterDisasterMapper.yearTyphoonStats(yearPeriod);

        return CommonResult.success(respVos);
    }

    @Override
    public CommonResult<List<RealTyphoonData>> typhoonInfos(String id, String year, String landPower) {
        if (StringUtils.isEmpty(year)) {
            year = String.valueOf(LocalDate.now().getYear());
        }

        List<RealTyphoonData> typhoonInfos = waterDisasterMapper.typhoonInfos(id, year, landPower);
        return CommonResult.success(typhoonInfos);
    }

    /**
     * 获取雨量测站小时雨量信息
     *
     * @param hour 小时数
     * @return java.util.List<com.ygkj.big.screen.vo.response.RainfallStaInfo>
     */
    private List<RainfallStaInfo> getRainfallStas(Integer hour) {

        if (hour == null) {
            hour = 1;
        }

        Double rainfall = hour <= 1 ? 30d : hour == 3 ? 50d : 50d;
        // 获取雨量测站信息
        List<RainfallStaInfo> staInfos = waterDisasterMapper.getRainfallStas();
        if (CollectionUtils.isBlank(staInfos)) {
            return staInfos;
        }

        // 获取当前实时雨量信息
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String tableName = StationTypes.RAINFALL.table() + yyyyMM;
        List<StPptnR> stPptnRS = waterDisasterMapper.getRealRainfalls(tableName, hour);

        if (CollectionUtils.isNotBlank(stPptnRS)) {
            Map<String, Double> collect = stPptnRS.stream().filter(st -> StringUtils.isNotBlank(st.getStcd()))
                    .collect(Collectors.toMap(StPptnR::getStcd, StPptnR::getDrp));
            for (RainfallStaInfo staInfo : staInfos) {
                String stCode = staInfo.getStCode();
                if (collect.containsKey(stCode)) {
                    Double drp = collect.get(stCode);
                    staInfo.setDrp(drp);
                    if (drp != null) {
                        staInfo.setStatus(drp.compareTo(rainfall) > 0 ? "0" : "1");
                    }
                }
            }
        }
        return staInfos;
    }
}
