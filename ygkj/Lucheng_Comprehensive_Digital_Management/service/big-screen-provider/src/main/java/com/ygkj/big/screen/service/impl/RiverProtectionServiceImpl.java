package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.enums.StationTypes;
import com.ygkj.big.screen.service.RiverProtectionService;
import com.ygkj.big.screen.mapper.RiverProtectionMapper;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.request.RpProblemStatsReqVo;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author fml
 * @date 2021-03-22 17:16
 * @description 河湖保护专题
 */
@Service("riverProtectionService")
public class RiverProtectionServiceImpl implements RiverProtectionService {

    @Resource
    private RiverProtectionMapper riverProtectionMapper;

    /**
     * 温州行政区划信息
     *
     * @return List<WzAdcdInfo>
     */
    @Override
    public List<WzAdcdInfo> getWzAdcds() {
        List<WzAdcdInfo> adcdInfos = riverProtectionMapper.getWzAdcds();
        return adcdInfos;
    }

    @Override
    public CommonResult<List<RpRiverNumStatitstcsRespVo>> riverNumStatistics(String adcd) {

        List<RpRiverNumStatitstcsRespVo> riverNumStats = riverProtectionMapper.riverNumStatistics(adcd);
        return CommonResult.success(riverNumStats);
    }

    @Override
    public CommonResult<List<RpWaterLevelsRespVo>> waterLevels(String adcd) {

        return CommonResult.success(getWaterLevels(adcd));
    }

    @Override
    public CommonResult<List<RpProblemStatsRespVo>> problemStatistics(String type) {

        List<RpProblemStatsRespVo> proStats = riverProtectionMapper.problemStatistics(type);
        return CommonResult.success(proStats);
    }

    @Override
    public CommonResult<List<RpRainfallRespVo>> railfallStatistics(String adcd, String type) {

        List<RpRainfallRespVo> respVos = new ArrayList<>();

        // 获取雨量测站信息
        List<RpRailfallInfo> rainStas = riverProtectionMapper.getRainStations(adcd);
        if (CollectionUtils.isBlank(rainStas)) {
            return CommonResult.success(respVos);
        }

        // 获取实时雨量
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        List<StPptnR> realRailfalls = riverProtectionMapper.getRealRainfall(StationTypes.RAINFALL.table() + yyyyMM);

        // 计算 1、3、6、24、72h雨量
        if (CollectionUtils.isNotBlank(realRailfalls)) {
            Map<String, List<StPptnR>> stcdMap = realRailfalls.stream().collect(Collectors.groupingBy(map -> map.getStcd()));
            for (RpRailfallInfo rainSta : rainStas) {
                String stCode = rainSta.getStCode();
                if (stcdMap.containsKey(stCode)) {
                    rainSta.setStPptnRS(stcdMap.get(stCode));
                }
            }
        }

        // 1流域，2行政区划
        if ("1".equals(type)) {
            Map<String, List<RpRailfallInfo>> collect = rainStas.stream().filter(sta -> StringUtils.isNotBlank(sta.getBas()))
                    .collect(Collectors.groupingBy(sta -> sta.getBas()));
            collect.forEach((k, v) -> {
                RpRainfallRespVo respVo = new RpRainfallRespVo();
                respVo.setBas(k);
                double oneHSum = 0d;
                double threeHSum = 0d;
                double sixHSum = 0d;
                double oneDSum = 0d;
                double threeDSum = 0d;
                for (RpRailfallInfo rpRailfallInfo : v) {
                    List<StPptnR> maps = rpRailfallInfo.getStPptnRS();
                    if (CollectionUtils.isNotBlank(maps)) {
                        Stream<StPptnR> stPptnRStream = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(1).getTime() >= 0);
                        if (stPptnRStream.count() > 0) {
                            oneHSum += maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(1).getTime() >= 0)
                                    .mapToDouble(map -> map.getDrp()).sum();
                        }
                        Stream<StPptnR> stPptnRStream1 = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(3).getTime() >= 0);
                        if (stPptnRStream1.count() > 0) {
                            threeHSum += maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(3).getTime() >= 0)
                                    .mapToDouble(map -> map.getDrp()).sum();
                        }
                        Stream<StPptnR> stPptnRStream2 = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(6).getTime() >= 0);
                        if (stPptnRStream2.count() > 0) {
                            sixHSum += maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(6).getTime() >= 0)
                                    .mapToDouble(map -> map.getDrp()).sum();
                        }
                        Stream<StPptnR> stPptnRStream3 = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(24).getTime() >= 0);
                        if (stPptnRStream3.count() > 0) {
                            oneDSum += maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(24).getTime() >= 0)
                                    .mapToDouble(map -> map.getDrp()).sum();
                        }
                        Stream<StPptnR> stPptnRStream4 = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(72).getTime() >= 0);
                        if (stPptnRStream4.count() > 0) {
                            threeDSum += maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(72).getTime() >= 0)
                                    .mapToDouble(map -> map.getDrp()).sum();
                        }
                    }
                }
                respVo.setOneHour(oneHSum);
                respVo.setThreeHour(threeHSum);
                respVo.setSixHour(sixHSum);
                respVo.setOneDay(oneDSum);
                respVo.setThreeDay(threeDSum);

                respVos.add(respVo);
            });
        }
        if ("2".equals(type)) {
            Map<String, List<RpRailfallInfo>> collect = rainStas.stream().filter(sta -> StringUtils.isNotBlank(sta.getAdnm()))
                    .collect(Collectors.groupingBy(sta -> sta.getAdnm()));
            collect.forEach((k, v) -> {
                RpRainfallRespVo respVo = new RpRainfallRespVo();
                respVo.setAdnm(k);
                double oneHSum = 0d;
                double threeHSum = 0d;
                double sixHSum = 0d;
                double oneDSum = 0d;
                double threeDSum = 0d;
                for (RpRailfallInfo rpRailfallInfo : v) {
                    List<StPptnR> maps = rpRailfallInfo.getStPptnRS();
                    if (CollectionUtils.isNotBlank(maps)) {
                        Stream<StPptnR> stPptnRStream = maps.stream().filter(map -> map.getDrp() != null && map.getTm() != null && map.getTm().getTime() - getDate(1).getTime() >= 0);
                        if (stPptnRStream.count() > 0) {
                            oneHSum += maps.stream().filter(map -> map.getDrp() != null && map.getTm() != null && map.getTm().getTime() - getDate(1).getTime() >= 0)
                                    .mapToDouble(map -> map.getDrp()).sum();
                        }
                        Stream<StPptnR> stPptnRStream1 = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(3).getTime() >= 0);
                        if (stPptnRStream1.count() > 0) {
                            threeHSum += maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(3).getTime() >= 0)
                                    .mapToDouble(map -> map.getDrp()).sum();
                        }
                        Stream<StPptnR> stPptnRStream2 = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(6).getTime() >= 0);
                        if (stPptnRStream2.count() > 0) {
                            sixHSum += maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(6).getTime() >= 0)
                                    .mapToDouble(map -> map.getDrp()).sum();
                        }
                        Stream<StPptnR> stPptnRStream3 = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(24).getTime() >= 0);
                        if (stPptnRStream3.count() > 0) {
                            oneDSum += maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(24).getTime() >= 0)
                                    .mapToDouble(map -> map.getDrp()).sum();
                        }
                        Stream<StPptnR> stPptnRStream4 = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(72).getTime() >= 0);
                        if (stPptnRStream4.count() > 0) {
                            threeDSum += maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(72).getTime() >= 0)
                                    .mapToDouble(map -> map.getDrp()).sum();
                        }
                    }
                }
                respVo.setOneHour(oneHSum);
                respVo.setThreeHour(threeHSum);
                respVo.setSixHour(sixHSum);
                respVo.setOneDay(oneDSum);
                respVo.setThreeDay(threeDSum);

                respVos.add(respVo);
            });
        }
        return CommonResult.success(respVos);
    }

    /**
     * 地图点位
     *
     * @param type 1水源地、2水库、 3水质监测、 4水位监测、5雨量监测、 6河湖全景图、 7摄像机、 8四乱点位
     * @return
     */
    @Override
    public CommonResult<RpMapPointsRespVo> mapPoint(String type, String id) {

        RpMapPointsRespVo respVo = new RpMapPointsRespVo();

        if ("1".equals(type)) {
            // att_swhs_base
            List<AttSwhsBase> swhsBases = riverProtectionMapper.getSwshs(id);
            respVo.setSwhsBases(swhsBases);
        }
        if ("2".equals(type)) {
            List<AttResBase> resBases = riverProtectionMapper.getResBases(id);
            respVo.setResBases(resBases);
        }
        if ("3".equals(type)) {
            List<AttCwsstationBase> cwsStas = riverProtectionMapper.getCwsStas(id);
            respVo.setCwsStas(cwsStas);
        }
        if ("4".equals(type)) {
            List<AttStBase> waterStas = riverProtectionMapper.getWaterStas(id);
            respVo.setWaterStas(waterStas);
        }
        if ("5".equals(type)) {
            List<AttStBase> rainStas = riverProtectionMapper.getRainStas(id);
            respVo.setRainStas(rainStas);
        }
        if ("6".equals(type)) {
            List<RiverUnionInfo> ruInfos = riverProtectionMapper.getRuInfos(id);
            respVo.setRuInfos(ruInfos);
        }
        if ("7".equals(type)) {
            List<HikvisionCameraRecord> cameraRecords = riverProtectionMapper.getCameraRecords(id);
            respVo.setCameraRecords(cameraRecords);
        }
        if ("8".equals(type)) {
            List<WtDynProblem> dynProblems = riverProtectionMapper.getDynProblems(id);
            respVo.setDynProblems(dynProblems);
        }
        return CommonResult.success(respVo);
    }

    /**
     * 水位统计
     *
     * @return
     */
    @Override
    public CommonResult<List<RpWarnNumStatsRespVo>> waterLevelStatistics() {

        List<RpWarnNumStatsRespVo> respVos = new ArrayList<>();

        List<RpWaterLevelsRespVo> waterLevels = getWaterLevels(null);

        RpWarnNumStatsRespVo respVo = new RpWarnNumStatsRespVo();
        respVo.setStatus("超警");

        RpWarnNumStatsRespVo respVo1 = new RpWarnNumStatsRespVo();
        respVo1.setStatus("正常");

        // 超警
        if (CollectionUtils.isNotBlank(waterLevels)) {
            int warnNum = (int) waterLevels.stream().filter(wt -> "0".equals(wt.getStatus())).count();
            respVo.setNum(warnNum);

            int normalNum = waterLevels.size() - warnNum;
            respVo1.setNum(normalNum);
        }
        respVos.add(respVo);
        respVos.add(respVo1);
        return CommonResult.success(respVos);
    }

    /**
     * 水质统计
     *
     * @return CommonResult
     */
    @Override
    public CommonResult<List<RpWarnNumStatsRespVo>> szStatistics() {

        List<RpWarnNumStatsRespVo> respVos = new ArrayList<>();

        RpWarnNumStatsRespVo respVo = new RpWarnNumStatsRespVo();
        respVo.setStatus("超警");

        RpWarnNumStatsRespVo respVo1 = new RpWarnNumStatsRespVo();
        respVo1.setStatus("正常");

        // 获取水质测站
        List<RpCwsStasRealDataInfo> stasRealDataInfos = riverProtectionMapper.getCwsStaCodes();
        if (CollectionUtils.isNotBlank(stasRealDataInfos)) {

            // 获取实时水质
            List<RpCwsStasRealDataInfo> stasRealDatas = riverProtectionMapper.getCwsStaRealDatas();

            if (CollectionUtils.isNotBlank(stasRealDatas)) {
                Map<String, RpCwsStasRealDataInfo> collect = stasRealDatas.stream().filter(srd -> StringUtils.isNotBlank(srd.getStationCode()))
                        .collect(Collectors.toMap(RpCwsStasRealDataInfo::getStationCode, srd -> srd));

                for (RpCwsStasRealDataInfo stasRealDataInfo : stasRealDataInfos) {
                    String stationCode = stasRealDataInfo.getStationCode();
                    if (collect.containsKey(stationCode)) {
                        RpCwsStasRealDataInfo rpCwsStasRealDataInfo = collect.get(stationCode);

                        Double ph = Double.valueOf(StringUtils.isBlank(rpCwsStasRealDataInfo.getPH()) ? rpCwsStasRealDataInfo.getPH() : "0");
                        Double freeChlorine = Double.valueOf(StringUtils.isBlank(rpCwsStasRealDataInfo.getFreeChlorine()) ? rpCwsStasRealDataInfo.getFreeChlorine() : "0");
                        Double turbidity = Double.valueOf(StringUtils.isBlank(rpCwsStasRealDataInfo.getTurbidity()) ? rpCwsStasRealDataInfo.getTurbidity() : "0");

                        boolean flag = "3253009032".equals(stationCode) ? freeChlorine >= 0.1d && freeChlorine <= 0.8d : freeChlorine >= 0.3d;
                        flag = flag && turbidity < 1d && ph >= 6.5d && ph <= 8.5d;

                        stasRealDataInfo.setStatus(flag ? "1" : "0");
                    }
                }
            }
            int normalNum = (int) stasRealDataInfos.stream().filter(srd -> "1".equals(srd.getStatus())).count();
            respVo1.setNum(normalNum);
            int warnNum = (int) stasRealDataInfos.stream().filter(srd -> "0".equals(srd.getStatus())).count();
            respVo.setNum(warnNum);
        }

        respVos.add(respVo);
        respVos.add(respVo1);

        return CommonResult.success(respVos);
    }

    /**
     * 雨量统计
     *
     * @return CommonResult
     */
    @Override
    public CommonResult<List<RpWarnNumStatsRespVo>> rainfallStatistics() {

        List<RpWarnNumStatsRespVo> respVos = new ArrayList<>();

        RpWarnNumStatsRespVo respVo = new RpWarnNumStatsRespVo();
        respVo.setStatus("超警");

        RpWarnNumStatsRespVo respVo1 = new RpWarnNumStatsRespVo();
        respVo1.setStatus("正常");

        // 获取雨量测站信息
        List<RpRailfallInfo> rainStas = riverProtectionMapper.getRainStations(null);
        if (CollectionUtils.isNotBlank(rainStas)) {

            // 获取实时雨量
            String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            List<StPptnR> realRailfalls = riverProtectionMapper.getRealRainfall(StationTypes.RAINFALL.table() + yyyyMM);

            // 计算 1、3、6、24、72h雨量
            if (CollectionUtils.isNotBlank(realRailfalls)) {
                Map<String, List<StPptnR>> stcdMap = realRailfalls.stream().collect(Collectors.groupingBy(map -> map.getStcd()));
                for (RpRailfallInfo rainSta : rainStas) {
                    String stCode = rainSta.getStCode();
                    double oneHSum = 0d;
                    double threeHSum = 0d;
                    double sixHSum = 0d;
                    if (stcdMap.containsKey(stCode)) {
                        List<StPptnR> maps = stcdMap.get(stCode);
                    /*double oneDSum = 0d;
                    double threeDSum = 0d;*/
                        if (CollectionUtils.isNotBlank(maps)) {
                            Stream<StPptnR> stPptnRStream = maps.stream()
                                    .filter(map -> map.getDrp() != null && map.getTm() != null && map.getTm().getTime() - getDate(1).getTime() >= 0);
                            if (stPptnRStream.count() > 0) {
                                oneHSum += maps.stream()
                                        .filter(map -> map.getDrp() != null && map.getTm() != null && map.getTm().getTime() - getDate(1).getTime() >= 0)
                                        .mapToDouble(map -> map.getDrp()).sum();
                            }
                            Stream<StPptnR> stPptnRStream1 = maps.stream()
                                    .filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(3).getTime() >= 0);
                            if (stPptnRStream1.count() > 0) {
                                threeHSum += maps.stream()
                                        .filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(3).getTime() >= 0)
                                        .mapToDouble(map -> map.getDrp()).sum();
                            }
                            Stream<StPptnR> stPptnRStream2 = maps.stream()
                                    .filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(6).getTime() >= 0);
                            if (stPptnRStream2.count() > 0) {
                                sixHSum += maps.stream()
                                        .filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(6).getTime() >= 0)
                                        .mapToDouble(map -> map.getDrp()).sum();
                            }
                        /*Stream<StPptnR> stPptnRStream3 = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(24).getTime() >= 0);
                        if (stPptnRStream3.count() > 0) {
                            oneDSum += stPptnRStream3.mapToDouble(map -> map.getDrp()).sum();
                        }
                        Stream<StPptnR> stPptnRStream4 = maps.stream().filter(map -> map.getDrp() != null && map.getTm().getTime() - getDate(72).getTime() >= 0);
                        if (stPptnRStream4.count() > 0) {
                            threeDSum += stPptnRStream4.mapToDouble(map -> map.getDrp()).sum();
                        }*/
                        }
                    }
                    rainSta.setStatus((oneHSum + threeHSum + sixHSum) > 100d ? "0" : "1");
                }
            }

            int normalNum = (int) rainStas.stream().filter(srd -> "1".equals(srd.getStatus())).count();
            respVo1.setNum(normalNum);
            int warnNum = (int) rainStas.stream().filter(srd -> "0".equals(srd.getStatus())).count();
            respVo.setNum(warnNum);
        }

        respVos.add(respVo);
        respVos.add(respVo1);
        return CommonResult.success(respVos);
    }

    /**
     * 视频统计
     *
     * @return CommonResult
     */
    @Override
    public CommonResult<List<RpWarnNumStatsRespVo>> cameraStatistics() {

        List<RpWarnNumStatsRespVo> respVos = new ArrayList<>();

        RpWarnNumStatsRespVo respVo = new RpWarnNumStatsRespVo();
        respVo.setStatus("离线");

        RpWarnNumStatsRespVo respVo1 = new RpWarnNumStatsRespVo();
        respVo1.setStatus("在线");

        // 获取视频信息
        List<HikvisionCameraRecord> cameraRecords = riverProtectionMapper.getCameraRecords(null);
        if (CollectionUtils.isNotBlank(cameraRecords)) {

            int onlineNum = (int) cameraRecords.stream().filter(camera -> camera.getStatus() == 1).count();
            respVo1.setNum(onlineNum);
            int unonlineNum = cameraRecords.size() - onlineNum;
            respVo.setNum(unonlineNum);
        }
        respVos.add(respVo);
        respVos.add(respVo1);
        return CommonResult.success(respVos);
    }

    /**
     * 行政区划内设备统计
     *
     * @param type 1水位/2水质/3雨量/4视频
     * @return CommonResult
     */
    @Override
    public CommonResult<List<WzAdcdInfo>> adcdEquipmentStats(String type) {

        // 行政区划信息
        List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();

        List<WzAdcdInfo> equips = null;

        if ("1".equals(type)) {
            //水位
            equips = riverProtectionMapper.getAdcdStStats("RR,ZZ");
        }
        if ("2".equals(type)) {
            // 水质
            equips = riverProtectionMapper.getAdcdCwsStats();
        }
        if ("3".equals(type)) {
            // 雨量
            equips = riverProtectionMapper.getAdcdStStats("PP");
        }
        if ("4".equals(type)) {
            // 视频
            equips = riverProtectionMapper.getAdcdCameraStats();
        }

        if (CollectionUtils.isNotBlank(equips)) {
            Map<String, Integer> collect = equips.stream().filter(e -> StringUtils.isNotBlank(e.getAdcd()))
                    .collect(Collectors.toMap(WzAdcdInfo::getAdcd, WzAdcdInfo::getNum));
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
     * 行政区划河湖库数量统计
     *
     * @param type 1河流/2湖泊/3水位
     * @return CommonResult
     */
    @Override
    public CommonResult<List<WzAdcdInfo>> adcdRLRStats(String type) {

        List<WzAdcdInfo> wzAdcds = riverProtectionMapper.getWzAdcds();

        List<WzAdcdInfo> equips = null;

        if ("1".equals(type)) {
            // 河流
            equips = riverProtectionMapper.getAdcdRiverNum();
        }
        if ("2".equals(type)) {
            // 湖泊
            equips = riverProtectionMapper.getAdcdLakeNum();
        }
        if ("3".equals(type)) {
            // 水库
            equips = riverProtectionMapper.getAdcdResNum();
        }
        if (CollectionUtils.isNotBlank(equips)) {
            Map<String, Integer> collect = equips.stream().filter(e -> StringUtils.isNotBlank(e.getAdcd()))
                    .collect(Collectors.toMap(WzAdcdInfo::getAdcd, WzAdcdInfo::getNum));
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
     * 水位测站信息
     *
     * @param adcd
     * @return
     */
    private List<RpWaterLevelsRespVo> getWaterLevels(String adcd) {
        // 获取河道水位测站信息
        List<RpWaterLevelsRespVo> zzStations = riverProtectionMapper.getZZStations(adcd);
        if (CollectionUtils.isBlank(zzStations)) {
            return zzStations;
        }

        // 获取测站最新水位（默认近一周）
        String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        List<Map<String, Object>> realWaterLevels = riverProtectionMapper.getRealWaterLevels(StationTypes.RIVER.table() + yyyyMM);
        if (CollectionUtils.isNotBlank(realWaterLevels)) {
            // 统计水位是否超警
            Map<String, Double> collect = realWaterLevels.stream().filter(map -> StringUtils.isNotBlank((String) map.get("stcd")) && map.get("z") != null)
                    .collect(Collectors.toMap(map -> (String) map.get("stcd"), map -> (Double) map.get("z")));
            for (RpWaterLevelsRespVo zzStation : zzStations) {
                String stCode = zzStation.getStCode();
                if (collect.containsKey(stCode)) {
                    zzStation.setZ(collect.get(stCode));
                }
                Double limitLevel = zzStation.getLimitLevel();
                Double warningLevel = zzStation.getWarningLevel();
                Double z = zzStation.getZ();
                if (null != z) {
                    if (warningLevel != null && z.compareTo(warningLevel) > 0) {
                        zzStation.setStatus("0");
                        continue;
                    }
                    if (limitLevel != null && z.compareTo(limitLevel) > 0) {
                        zzStation.setStatus("0");
                        continue;
                    }
                }
            }
        }
        return zzStations;
    }

    /**
     * 时间计算
     *
     * @param ihour
     * @return
     */
    private Date getDate(int ihour) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - ihour);
        return c.getTime();
    }
}
