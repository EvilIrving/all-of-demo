package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.mapper.WaterUserMapper;
import com.ygkj.big.screen.service.WaterUserService;
import com.ygkj.big.screen.mapper.RiverProtectionMapper;
import com.ygkj.big.screen.model.WiuUseMonth;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("waterUserService")
public class WaterUserServiceImpl implements WaterUserService {

    @Resource
    private WaterUserMapper waterUserMapper;

    @Resource
    private RiverProtectionMapper riverProtectionMapper;

    /**
     * 取水户点位
     *
     * @param type 1工业取水、2水利发电、3公共供、4农业取水、5其他
     * @return CommonResult
     */
    @Override
    public CommonResult<List<WuWaterUserMapRespVo>> mapPoints(String type, String status) {

        List<WuWaterUserMapRespVo> respVos = wiuList(type);
        if (CollectionUtils.isBlank(respVos)) {
            return CommonResult.success(respVos);
        }

        if (StringUtils.isNotBlank(status)) {
            return CommonResult.success(respVos.stream().filter(v -> status.equals(String.valueOf(v.getStatus()))).collect(Collectors.toList()));
        }

        return CommonResult.success(respVos);
    }

    /**
     * 取水户月取水量弹框
     *
     * @param wiuCode 取水户编码
     * @return CommonResult
     */
    @Override
    public CommonResult<WuWaterUseRespVo> waterUserUseMonth(String wiuCode) {

        WuWaterUseRespVo respVo = new WuWaterUseRespVo();

        // 当前年月取水量
        List<WiuUseMonth> wiuUseMonths = waterUserMapper.getWiuUseMonths(String.valueOf(LocalDate.now().getYear()), wiuCode);
        respVo.setWiuUseMonths(wiuUseMonths);

        if (CollectionUtils.isNotBlank(wiuUseMonths)) {
            // 计算当年总取水量
            Stream<WiuUseMonth> wiuUseMonthStream = wiuUseMonths.stream().filter(w -> w.getWatUse() != null);
            if (wiuUseMonthStream.count() > 0) {
                double sum = wiuUseMonths.stream().filter(w -> w.getWatUse() != null).mapToDouble(WiuUseMonth::getWatUse).sum();
                respVo.setYearUse(sum);
            }
        }
        return CommonResult.success(respVo);
    }

    /**
     * 取水许可总量统计（按行政区划区分）
     *
     * @return CommonResult
     */
    @Override
    public CommonResult<List<WzAdcdInfo>> adcdWptStatistics() {

        // 获取温州行政区划信息
        List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();
        if (CollectionUtils.isBlank(wzAdcds)) {
            return CommonResult.success(wzAdcds);
        }

        // 获取行政区划取水许可量
        List<Map<String, Object>> adcdWptStats = waterUserMapper.getAdcdWptStats();
        if (CollectionUtils.isNotBlank(adcdWptStats)) {
            Map<String, BigDecimal> collect = adcdWptStats.stream().filter(map -> StringUtils.isNotBlank((String) map.get("adcd")))
                    .collect(Collectors.toMap(map -> (String) map.get("adcd"), map -> (BigDecimal) map.get("num")));
            for (WzAdcdInfo wzAdcd : wzAdcds) {
                String adcd = wzAdcd.getAdcd();
                if (collect.containsKey(adcd)) {
                    wzAdcd.setValue(collect.get(adcd).doubleValue());
                }
            }
        }

        return CommonResult.success(wzAdcds);
    }

    /**
     * 取水许可类型统计（行政区划下取水类型）
     *
     * @param adcd 行政区划
     * @return CommonResult
     */
    @Override
    public CommonResult<List<WuWptQslxStatsRespVo>> wptQslxStatistics(String adcd) {

        List<WuWptQslxStatsRespVo> qslxStatistics = waterUserMapper.wptQslxStatistics(adcd);
        return CommonResult.success(qslxStatistics);
    }

    /**
     * 取水户统计（按行政区划区分）
     *
     * @return CommonResult
     */
    @Override
    public CommonResult<List<WzAdcdInfo>> adcdWiuStatistics() {

        // 获取温州行政区划信息
        List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();
        if (CollectionUtils.isBlank(wzAdcds)) {
            return CommonResult.success(wzAdcds);
        }

        // 获取行政区划取水户
        List<Map<String, Object>> adcdWiuStats = waterUserMapper.adcdWiuStats();
        if (CollectionUtils.isNotBlank(adcdWiuStats)) {
            Map<String, Long> collect = adcdWiuStats.stream().filter(map -> StringUtils.isNotBlank((String) map.get("adcd")))
                    .collect(Collectors.toMap(map -> (String) map.get("adcd"), map -> (Long) map.get("num")));
            for (WzAdcdInfo wzAdcd : wzAdcds) {
                String adcd = wzAdcd.getAdcd();
                if (collect.containsKey(adcd)) {
                    wzAdcd.setNum(collect.get(adcd).intValue());
                }
            }
        }
        return CommonResult.success(wzAdcds);
    }

    /**
     * 取水曲线（一年内月取水曲线）
     *
     * @param year 年份
     * @return CommonResult
     */
    @Override
    public CommonResult<List<WuWiuUseCurveRespVo>> wiuUseCurve(String year) {

        if (StringUtils.isEmpty(year)) {
            year = String.valueOf(LocalDate.now().getYear());
        }

        List<WuWiuUseCurveRespVo> wiuUseMonthStats = waterUserMapper.wiuUseCurve(year);

        return CommonResult.success(wiuUseMonthStats);
    }

    /**
     * 档案完整率 （取水户数量-档案必须项确实的数量）/取水户数量
     *
     * @return CommonResult
     */
    @Override
    public CommonResult wiuDocRate() {

        List<WuWiuStatsRespVo> respVos = new ArrayList<>();

        Integer wiuNum = waterUserMapper.getWiuNum();
        Integer docNum = waterUserMapper.getDocNum();

        if (wiuNum == 0) {
            return CommonResult.success(respVos);
        }

        double v = new BigDecimal((float) (wiuNum - docNum) / wiuNum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        WuWiuStatsRespVo respVo = new WuWiuStatsRespVo();
        respVo.setValue(v);
        respVos.add(respVo);

        return CommonResult.success(respVos);
    }

    /**
     * 取水预警统计（许可量80%预警）
     *
     * @return CommonResult
     */
    @Override
    public CommonResult<List<WuWiuStatsRespVo>> waterUseWarn() {

        List<WuWiuStatsRespVo> wiuStatsRespVos = new ArrayList<>();

        List<WuWaterUserMapRespVo> respVos = wiuList(null);
        if (CollectionUtils.isBlank(respVos)) {
            return CommonResult.success(wiuStatsRespVos);
        }

        // 状态 1正常、2许可证过期、3水量超许可、4掉线或未安装监控设施、5取水偏快提醒
        WuWiuStatsRespVo respVo = new WuWiuStatsRespVo();
        respVo.setText("正常");
        respVo.setNum((int) respVos.stream().filter(v -> 1 == v.getStatus()).count());

        WuWiuStatsRespVo respVo1 = new WuWiuStatsRespVo();
        respVo1.setText("接近取水许可");
        respVo1.setNum((int) respVos.stream().filter(v -> 5 == v.getStatus()).count());

        WuWiuStatsRespVo respVo2 = new WuWiuStatsRespVo();
        respVo2.setText("超过取水许可");
        respVo2.setNum((int) respVos.stream().filter(v -> 3 == v.getStatus()).count());

        wiuStatsRespVos.add(respVo);
        wiuStatsRespVos.add(respVo1);
        wiuStatsRespVos.add(respVo2);

        return CommonResult.success(wiuStatsRespVos);
    }

    /**
     * 取水户信息列表
     *
     * @param type 取水类型
     * @return List<WuWaterUserMapRespVo>
     */
    private List<WuWaterUserMapRespVo> wiuList(String type) {

        // 获取取水户信息
        List<WuWaterUserMapRespVo> respVos = waterUserMapper.getWiuInfos(type);
        if (CollectionUtils.isBlank(respVos)) {
            return respVos;
        }

        // 获取取水户用水量 (状态 1正常、2许可证过期、3水量超许可、4掉线或未安装监控设施、5取水偏快提醒)
        List<WiuUseMonth> wiuUseMonths = waterUserMapper.getWiuUseMonths(String.valueOf(LocalDate.now().getYear()), null);
        if (CollectionUtils.isNotBlank(wiuUseMonths)) {
            Map<String, List<WiuUseMonth>> collect = wiuUseMonths.stream()
                    .filter(w -> StringUtils.isNotBlank(w.getWiuCode()) && w.getWatUse() != null).collect(Collectors.groupingBy(WiuUseMonth::getWiuCode));
            for (WuWaterUserMapRespVo respVo : respVos) {
                if (2 == respVo.getStatus()) {
                    continue;
                }
                String wiuCode = respVo.getWiuCode();
                if (collect.containsKey(wiuCode)) {
                    List<WiuUseMonth> wiuUseMonths1 = collect.get(wiuCode);
                    double sum = wiuUseMonths1.stream().mapToDouble(WiuUseMonth::getWatUse).sum();
                    respVo.setWiuUse(sum);
                    Double wpermitsize = respVo.getWpermitsize() != null ? respVo.getWpermitsize() : 0d;
                    double warnValue = wpermitsize * 0.8d;
                    if (sum > wpermitsize) {
                        respVo.setStatus(3);
                        continue;
                    }
                    if (sum > warnValue && sum < wpermitsize) {
                        respVo.setStatus(5);
                    }

                } else {
                    respVo.setStatus(4);
                }
            }
        }
        return respVos;
    }
}
