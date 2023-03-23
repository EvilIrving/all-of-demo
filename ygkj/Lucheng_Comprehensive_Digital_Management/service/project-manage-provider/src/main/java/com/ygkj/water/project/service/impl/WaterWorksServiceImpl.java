package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.project.model.AttCwsBaseWcp;
import com.ygkj.project.model.CwsWaterQuality;
import com.ygkj.project.model.CwsWaterVolume;
import com.ygkj.project.vo.request.WaterWorksReqVo;
import com.ygkj.project.vo.response.CwsWcpResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.mapper.AttAdcdBaseMapper;
import com.ygkj.water.project.mapper.AttCwsBaseWcpMapper;
import com.ygkj.water.project.service.WaterWorksService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 水厂服务
 * @Date 2021/8/16
 */
@Service
public class WaterWorksServiceImpl implements WaterWorksService {

    @Resource
    AttCwsBaseWcpMapper cwsBaseWcpMapper;

    @Resource
    AttAdcdBaseMapper adcdBaseMapper;


    /**
     * 查询水厂列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<CwsWcpResVo> listWaterWorks(WaterWorksReqVo reqVo) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("查询水厂");
        List<AttCwsBaseWcp> records = cwsBaseWcpMapper.selectByCondition(reqVo);
        if (CollectionUtils.isBlank(records)) {
            return Collections.emptyList();
        }
        Map<String, CwsWaterQuality> qualityMap = Collections.emptyMap();
        Map<String, CwsWaterVolume> volumeMap = Collections.emptyMap();
        stopWatch.stop();
        stopWatch.start("处理实时出局");
        if (reqVo.isWithRuntimeInfo()) {
            List<String> cwsCodeList = records.stream().map(AttCwsBaseWcp::getCwsCode).collect(Collectors.toList());

            List<CwsWaterQuality> qualityList = cwsBaseWcpMapper.selectNewestQuality(cwsCodeList);
            qualityList = qualityList == null ? Collections.emptyList() : qualityList;
            qualityMap = qualityList.stream().collect(Collectors.toMap(CwsWaterQuality::getWaterPlantCode, Function.identity()));

            List<CwsWaterVolume> volumeList = cwsBaseWcpMapper.selectNewestVolume(cwsCodeList);
            volumeList = volumeList == null ? Collections.emptyList() : volumeList;
            volumeMap = volumeList.stream().collect(Collectors.toMap(CwsWaterVolume::getWaterPlantCode, Function.identity()));
        }
        Predicate<CwsWcpResVo> runtimeStateFilter = null;
        if (reqVo.getRuntimeState() != null) {
            runtimeStateFilter = (o) -> reqVo.getRuntimeState().equals(o.getRuntimeState());
        } else {
            runtimeStateFilter = (o) -> true;
        }
        Map<String, String> adcdMap = adcdMap();
        Map<String, CwsWaterQuality> finalQualityMap = qualityMap;
        Map<String, CwsWaterVolume> finalVolumeMap = volumeMap;

        stopWatch.stop();
        stopWatch.start("最后处理");
        List<CwsWcpResVo> result = records.stream()
                .map(o -> {
                    CwsWcpResVo target = new CwsWcpResVo();
                    BeanUtils.copyProperties(o, target);
                    target.setQuality(finalQualityMap.get(target.getCwsCode()));
                    target.setVolume(finalVolumeMap.get(target.getCwsCode()));
                    target.computeRuntimeState();
                    if (StringUtils.isNotBlank(target.getAdcd()) && target.getAdcd().length() >= 6) {
                        target.setAdcd(target.getAdcd().substring(0, 6).concat("000000"));
                        target.setAdnm(adcdMap.get(target.getAdcd()));
                    }
                    return target;
                }).filter(runtimeStateFilter).collect(Collectors.toList());
        stopWatch.stop();
        return result;
    }

    /**
     * 查询水厂列表表头统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public Map<String, Object> stats4listWaterWorks(WaterWorksReqVo reqVo) {
        reqVo.setColumnNames("cws_code,wasu_rang");
        List<AttCwsBaseWcp> records = cwsBaseWcpMapper.selectByCondition(reqVo);
        if (CollectionUtils.isBlank(records)) {
            return Collections.emptyMap();
        }
        Map<String, List<AttCwsBaseWcp>> recordMap = records.stream().filter(o -> StringUtils.isNotBlank(o.getWasuRang())).collect(Collectors.groupingBy(AttCwsBaseWcp::getWasuRang));
        JSONObject result = new JSONObject();
        recordMap.forEach((wasuRang, tempList) -> {
            result.put(wasuRang, tempList.size());
        });
        result.put("totalNum", records.size());
        return result;
    }

    /**
     * 查询水厂运行状态列表表头统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public Map<String, Object> stats4listWaterWorksState(WaterWorksReqVo reqVo) {
        reqVo.setColumnNames("cws_code");
        List<CwsWcpResVo> records = listWaterWorks(reqVo);
        if (CollectionUtils.isBlank(records)) {
            return Collections.emptyMap();
        }
        Map<Integer, List<CwsWcpResVo>> recordMap = records.stream().collect(Collectors.groupingBy(CwsWcpResVo::getRuntimeState));
        JSONObject result = new JSONObject();
        recordMap.forEach((runtimeState, tempList) -> {
            String key = runtimeState2Key(runtimeState);
            if (StringUtils.isNotBlank(key)) {
                result.put(key, tempList.size());
            }
        });
        result.put("totalNum", records.size());
        return result;
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
     *             7. 根据行政区划统计：城市、乡镇、联村、单村;但是分成4个数组
     */
    @Override
    public Object waterWorksStats(int type) {
        switch (type) {
            case 1: {
                QueryWrapper<AttCwsBaseWcp> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("cws_code", "eng_type", "adcd", "des_wasu_pop");
                List<AttCwsBaseWcp> records = this.cwsBaseWcpMapper.selectList(queryWrapper);
                if (CollectionUtils.isBlank(records)) {
                    return Collections.emptyList();
                }
                Map<String, List<AttCwsBaseWcp>> recordMap = records.stream()
                        .filter(o -> StringUtils.isNotBlank(o.getAdcd()) && o.getAdcd().length() >= 6)
                        .peek(o -> o.setAdcd(o.getAdcd().substring(0, 6).concat("000000"))).collect(Collectors.groupingBy(AttCwsBaseWcp::getAdcd));
                List<AttAdcdBase> adcdList = adcdBaseMapper.selectWZCounty();
                List<JSONObject> result = new ArrayList<>(adcdList.size());
                for (AttAdcdBase adcd : adcdList) {
                    List<AttCwsBaseWcp> tempList = recordMap.getOrDefault(adcd.getAdcd(), Collections.emptyList());
                    result.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "totalNum", tempList.size())
                            .with(JSONObject::put, "singleNum", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("单村")).count())
                            .with(JSONObject::put, "unionNum", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("联村")).count())
                            .with(JSONObject::put, "townNum", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("乡镇")).count())
                            .with(JSONObject::put, "cityNum", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("城市")).count())
                            .with(JSONObject::put, "totalPop", tempList.stream().map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .with(JSONObject::put, "singlePop", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("单村")).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .with(JSONObject::put, "unionPop", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("联村")).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .with(JSONObject::put, "townPop", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("乡镇")).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .with(JSONObject::put, "cityPop", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("城市")).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                }
                return result.stream().sorted(Comparator.comparingInt(o -> ((JSONObject) o).getIntValue("total")).reversed()).collect(Collectors.toList());
            }
            case 2: {
                QueryWrapper<AttCwsBaseWcp> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("cws_code", "wasu_rang", "des_wasu_pop");
                List<AttCwsBaseWcp> records = this.cwsBaseWcpMapper.selectList(queryWrapper);
                if (CollectionUtils.isBlank(records)) {
                    return Collections.emptyList();
                }
                Map<String, List<AttCwsBaseWcp>> recordMap = records.stream().filter(o -> StringUtils.isNotBlank(o.getWasuRang())).collect(Collectors.groupingBy(AttCwsBaseWcp::getWasuRang));
                List<JSONObject> result = new ArrayList<>(recordMap.keySet().size());
                String[] wasuRangList = {"两百吨千人以下", "两百吨千人以上", "千吨万人", "城市水厂"};
                for (String wasuRang : wasuRangList) {
                    List<AttCwsBaseWcp> tempList = recordMap.getOrDefault(wasuRang, Collections.emptyList());
                    result.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "wasuRang", wasuRang)
                            .with(JSONObject::put, "pop", tempList.stream()
                                    .map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .with(JSONObject::put, "num", tempList.size())
                            .build());
                }
                return result;
            }
            case 3: {
                QueryWrapper<AttCwsBaseWcp> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("cws_code", "eng_type", "des_wasu_pop");
                List<AttCwsBaseWcp> records = this.cwsBaseWcpMapper.selectList(queryWrapper);
                if (CollectionUtils.isBlank(records)) {
                    return Collections.emptyList();
                }
                List<JSONObject> result = new ArrayList<>(4);
                Map<String, List<AttCwsBaseWcp>> recordMap = records.stream().filter(o -> StringUtils.isNotBlank(o.getEngType())).collect(Collectors.groupingBy(AttCwsBaseWcp::getEngType));
                String[] engTypeList = {"城市", "乡镇", "联村", "单村"};
                for (String engType : engTypeList) {
                    List<AttCwsBaseWcp> tempList = recordMap.getOrDefault(engType, Collections.emptyList());
                    result.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "engType", engType)
                            .with(JSONObject::put, "pop", tempList.stream()
                                    .map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .with(JSONObject::put, "num", tempList.size())
                            .build());
                }
                return result;
            }
            case 4: {
                QueryWrapper<AttCwsBaseWcp> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("cws_code", "eng_type", "adcd", "des_wasu_pop");
                List<AttCwsBaseWcp> records = this.cwsBaseWcpMapper.selectList(queryWrapper);
                if (CollectionUtils.isBlank(records)) {
                    return Collections.emptyList();
                }
                List<String> cwsCodeList = records.stream().map(AttCwsBaseWcp::getCwsCode).collect(Collectors.toList());

                List<CwsWaterQuality> qualityList = cwsBaseWcpMapper.selectNewestQuality(cwsCodeList);
                qualityList = qualityList == null ? Collections.emptyList() : qualityList;
                Map<String, CwsWaterQuality> qualityMap = qualityList.stream().collect(Collectors.toMap(CwsWaterQuality::getWaterPlantCode, Function.identity()));

                List<CwsWaterVolume> volumeList = cwsBaseWcpMapper.selectNewestVolume(cwsCodeList);
                volumeList = volumeList == null ? Collections.emptyList() : volumeList;
                Map<String, CwsWaterVolume> volumeMap = volumeList.stream().collect(Collectors.toMap(CwsWaterVolume::getWaterPlantCode, Function.identity()));

                Map<String, List<CwsWcpResVo>> recordMap = records.stream()
                        .filter(o -> StringUtils.isNotBlank(o.getAdcd()) && o.getAdcd().length() >= 6)
                        .peek(o -> o.setAdcd(o.getAdcd().substring(0, 6).concat("000000"))).map(o -> {
                            CwsWcpResVo target = new CwsWcpResVo();
                            BeanUtils.copyProperties(o, target);
                            target.setQuality(qualityMap.get(target.getCwsCode()));
                            target.setVolume(volumeMap.get(target.getCwsCode()));
                            target.computeRuntimeState();
                            return target;
                        }).collect(Collectors.groupingBy(CwsWcpResVo::getAdcd));
                List<AttAdcdBase> adcdList = adcdBaseMapper.selectWZCounty();
                List<JSONObject> total = new ArrayList<>(adcdList.size());
                List<JSONObject> norm = new ArrayList<>(adcdList.size());
                List<JSONObject> volumeWarn = new ArrayList<>(adcdList.size());
                List<JSONObject> qualityWarn = new ArrayList<>(adcdList.size());
                List<JSONObject> volQuaWarn = new ArrayList<>(adcdList.size());
                for (AttAdcdBase adcd : adcdList) {
                    List<CwsWcpResVo> tempList = recordMap.getOrDefault(adcd.getAdcd(), Collections.emptyList());
                    total.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "num", tempList.size())

                            .with(JSONObject::put, "pop", tempList.stream().map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                    norm.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "num", tempList.stream().filter(o -> o.getRuntimeState() == 0).count())

                            .with(JSONObject::put, "pop", tempList.stream().filter(o -> o.getRuntimeState() == 0).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                    volumeWarn.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "num", tempList.stream().filter(o -> o.getRuntimeState() == 1).count())

                            .with(JSONObject::put, "pop", tempList.stream().filter(o -> o.getRuntimeState() == 1).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                    qualityWarn.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "num", tempList.stream().filter(o -> o.getRuntimeState() == 2).count())

                            .with(JSONObject::put, "pop", tempList.stream().filter(o -> o.getRuntimeState() == 2).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                    volQuaWarn.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "num", tempList.stream().filter(o -> o.getRuntimeState() == 3).count())

                            .with(JSONObject::put, "pop", tempList.stream().filter(o -> o.getRuntimeState() == 3).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                }
                JSONObject result = new JSONObject();
                result.put("total", total);
                result.put("norm", norm);
                result.put("volumeWarn", volumeWarn);
                result.put("qualityWarn", qualityWarn);
                result.put("volQuaWarn", volQuaWarn);
                return result;
            }
            case 5: {
                QueryWrapper<AttCwsBaseWcp> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("cws_code", "eng_type", "adcd", "des_wasu_pop");
                List<AttCwsBaseWcp> records = this.cwsBaseWcpMapper.selectList(queryWrapper);
                if (CollectionUtils.isBlank(records)) {
                    return Collections.emptyList();
                }
                List<String> cwsCodeList = records.stream().map(AttCwsBaseWcp::getCwsCode).collect(Collectors.toList());

                List<CwsWaterQuality> qualityList = cwsBaseWcpMapper.selectNewestQuality(cwsCodeList);
                qualityList = qualityList == null ? Collections.emptyList() : qualityList;
                Map<String, CwsWaterQuality> qualityMap = qualityList.stream().collect(Collectors.toMap(CwsWaterQuality::getWaterPlantCode, Function.identity()));

                List<CwsWaterVolume> volumeList = cwsBaseWcpMapper.selectNewestVolume(cwsCodeList);
                volumeList = volumeList == null ? Collections.emptyList() : volumeList;
                Map<String, CwsWaterVolume> volumeMap = volumeList.stream().collect(Collectors.toMap(CwsWaterVolume::getWaterPlantCode, Function.identity()));

                Map<Integer, List<CwsWcpResVo>> recordMap = records.stream()
                        .filter(o -> StringUtils.isNotBlank(o.getAdcd()) && o.getAdcd().length() >= 6)
                        .peek(o -> o.setAdcd(o.getAdcd().substring(0, 6).concat("000000"))).map(o -> {
                            CwsWcpResVo target = new CwsWcpResVo();
                            BeanUtils.copyProperties(o, target);
                            target.setQuality(qualityMap.get(target.getCwsCode()));
                            target.setVolume(volumeMap.get(target.getCwsCode()));
                            target.computeRuntimeState();
                            return target;
                        }).collect(Collectors.groupingBy(CwsWcpResVo::getRuntimeState));
                Integer[] engTypeList = {0, 1, 2, 3};
                JSONObject result = new JSONObject();
                for (Integer runtimeState : engTypeList) {
                    List<CwsWcpResVo> tempList = recordMap.getOrDefault(runtimeState, Collections.emptyList());
                    String key = runtimeState2Key(runtimeState);
                    if (StringUtils.isNotBlank(key)) {
                        result.put(key, Builder.of(JSONObject::new)
                                .with(JSONObject::put, "pop", tempList.stream()
                                        .map(AttCwsBaseWcp::getDesWasuPop)
                                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                                .with(JSONObject::put, "num", tempList.size())
                                .build());
                    }
                }
                result.put("total", Builder.of(JSONObject::new)
                        .with(JSONObject::put, "pop", records.stream()
                                .map(AttCwsBaseWcp::getDesWasuPop)
                                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                        .with(JSONObject::put, "num", records.size())
                        .build());
                return result;
            }
            case 6: {
                QueryWrapper<AttCwsBaseWcp> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("cws_code");
                List<AttCwsBaseWcp> records = this.cwsBaseWcpMapper.selectList(queryWrapper);
                if (CollectionUtils.isBlank(records)) {
                    return Collections.emptyList();
                }
                List<String> cwsCodeList = records.stream().map(AttCwsBaseWcp::getCwsCode).collect(Collectors.toList());

                List<CwsWaterQuality> qualityList = cwsBaseWcpMapper.selectNewestQuality(cwsCodeList);
                qualityList = qualityList == null ? Collections.emptyList() : qualityList;
                Map<String, CwsWaterQuality> qualityMap = qualityList.stream().collect(Collectors.toMap(CwsWaterQuality::getWaterPlantCode, Function.identity()));

                List<CwsWaterVolume> volumeList = cwsBaseWcpMapper.selectNewestVolume(cwsCodeList);
                volumeList = volumeList == null ? Collections.emptyList() : volumeList;
                Map<String, CwsWaterVolume> volumeMap = volumeList.stream().collect(Collectors.toMap(CwsWaterVolume::getWaterPlantCode, Function.identity()));

                List<CwsWcpResVo> recordList = records.stream()
                        .map(o -> {
                            CwsWcpResVo target = new CwsWcpResVo();
                            BeanUtils.copyProperties(o, target);
                            target.setQuality(qualityMap.get(target.getCwsCode()));
                            target.setVolume(volumeMap.get(target.getCwsCode()));
                            target.computeRuntimeState();
                            return target;
                        }).collect(Collectors.toList());

                BigDecimal totalSupplyQuantity = recordList.stream()
                        .filter(o -> o.getVolume() != null)
                        .map(o -> {
                            BigDecimal sum = null;
                            try {
                                sum = new BigDecimal(o.getVolume().getTotalSupplyQuantity());
                            } catch (Exception e) {

                            }
                            return sum;
                        }).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                Long warnNum = recordList.stream()
                        .filter(o -> o.getRuntimeState() != 0)
                        .count();
                JSONObject result = new JSONObject();
                result.put("warnNum", warnNum);
                result.put("totalSupplyQuantity", totalSupplyQuantity);
                return result;
            }
            case 7: {
                QueryWrapper<AttCwsBaseWcp> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("cws_code", "eng_type", "adcd", "des_wasu_pop");
                List<AttCwsBaseWcp> records = this.cwsBaseWcpMapper.selectList(queryWrapper);
                if (CollectionUtils.isBlank(records)) {
                    return Collections.emptyList();
                }
                Map<String, List<AttCwsBaseWcp>> recordMap = records.stream()
                        .filter(o -> StringUtils.isNotBlank(o.getAdcd()) && o.getAdcd().length() >= 6)
                        .peek(o -> o.setAdcd(o.getAdcd().substring(0, 6).concat("000000"))).collect(Collectors.groupingBy(AttCwsBaseWcp::getAdcd));
                List<AttAdcdBase> adcdList = adcdBaseMapper.selectWZCounty();
                List<JSONObject> total = new ArrayList<>(adcdList.size());
                List<JSONObject> single = new ArrayList<>(adcdList.size());
                List<JSONObject> union = new ArrayList<>(adcdList.size());
                List<JSONObject> town = new ArrayList<>(adcdList.size());
                List<JSONObject> city = new ArrayList<>(adcdList.size());
                for (AttAdcdBase adcd : adcdList) {
                    List<AttCwsBaseWcp> tempList = recordMap.getOrDefault(adcd.getAdcd(), Collections.emptyList());
                    total.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "num", tempList.size())
                            .with(JSONObject::put, "pop", tempList.stream().map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                    single.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "num", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("单村")).count())
                            .with(JSONObject::put, "pop", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("单村")).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                    union.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "num", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("联村")).count())
                            .with(JSONObject::put, "pop", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("联村")).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                    town.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "num", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("乡镇")).count())
                            .with(JSONObject::put, "pop", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("乡镇")).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                    city.add(Builder.of(JSONObject::new)
                            .with(JSONObject::put, "areaName", adcd.getAdnm())
                            .with(JSONObject::put, "num", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("城市")).count())
                            .with(JSONObject::put, "pop", tempList.stream().filter(o -> StringUtils.isNotBlank(o.getEngType()) && o.getEngType().equals("城市")).map(AttCwsBaseWcp::getDesWasuPop)
                                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .build());
                }
                JSONObject result = new JSONObject();
                result.put("total", total);
                result.put("single", single);
                result.put("union", union);
                result.put("town", town);
                result.put("city", city);
                return result;
            }
        }
        return null;
    }

    /**
     * 水厂水量水质曲线
     *
     * @param type    1. 水量，2水质
     * @param days    时间区间类型：过去24小时=1，近7天=7；近1个月=30
     * @param cwsCode 水厂编码
     * @return
     */
    @Override
    public JSONObject waterWorksStateCurve(int type, int days, String cwsCode) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);
        List<?> list = Collections.emptyList();
        JSONObject info = new JSONObject();
        switch (type) {
            case 1:
                list = this.cwsBaseWcpMapper.selectVolumeByTimeInterval(cwsCode, DateUtils.localDateTime2Date(start), DateUtils.localDateTime2Date(end));
                list = list == null ? Collections.emptyList() : list;
                Collections.sort(list, Comparator.comparing(o -> ((CwsWaterVolume) o).getUpdateTime()));
                if (CollectionUtils.isNotBlank(list)) {
                    info.put("realInfo", list.get(list.size() - 1));
                } else {
                    info.put("realInfo", null);
                }
                break;
            case 2:
                list = this.cwsBaseWcpMapper.selectQualityByTimeInterval(cwsCode, DateUtils.localDateTime2Date(start), DateUtils.localDateTime2Date(end));
                list = list == null ? Collections.emptyList() : list;
                Collections.sort(list, Comparator.comparing(o -> ((CwsWaterQuality) o).getTimeRecord()));
                if (CollectionUtils.isNotBlank(list)) {
                    info.put("realInfo", list.get(list.size() - 1));
                } else {
                    info.put("realInfo", null);
                }
                info.put("clUpperLimit", 4);
                info.put("clLowerLimit", 0.3);
                info.put("ntuUpperLimit", 3);
                info.put("ntuLowerLimit", 0);
                info.put("phUpperLimit", 9.5);
                info.put("phLowerLimit", 6.5);
                break;
        }
        info.put("list", list);
        return info;
    }

    private String runtimeState2Key(int runtimeState) {
        String key = null;
        switch (runtimeState) {
            case 0:
                key = "norm";
                break;
            case 1:
                key = "volumeWarn";
                break;
            case 2:
                key = "qualityWarn";
                break;
            case 3:
                key = "volQuaWarn";
                break;
            default:
                break;
        }
        return key;
    }

    Map<String, String> adcdMap() {
        List<AttAdcdBase> adcdList = adcdBaseMapper.selectWZCounty();
        adcdList = adcdList == null ? Collections.emptyList() : adcdList;
        return adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
    }
}
