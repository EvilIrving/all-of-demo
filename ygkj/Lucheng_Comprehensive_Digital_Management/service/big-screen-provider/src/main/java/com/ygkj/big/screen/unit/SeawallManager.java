package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.mapper.AttAdcdBaseMapper;
import com.ygkj.big.screen.model.AttAdcdBase;
import com.ygkj.big.screen.model.AttStBase;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.SeawallDrillResVo;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttSeawallBase;
import com.ygkj.project.vo.request.SeawallWarnReqVo;
import com.ygkj.utils.BeanComparator;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.MapComparator;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 海塘组件
 * @Date 2021/7/14
 */
@Component
@Slf4j
public class SeawallManager extends AbstractUnitManager {

    @Resource
    private AttAdcdBaseMapper adcdBaseMapper;

    @Autowired
    private ProjectManageClient projectManageClient;

    @Autowired
    private GraphClient graphClient;

    private ConcurrentHashMap<String, Object> cache;

    public SeawallManager(@Autowired ApplicationContext context) {
        super(context);
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        cache.clear();
    }

    @Override
    public void doSubscribe() {

    }

    @Override
    public void doPublish() {

    }

    @Override
    public Object getData(UnitDataReqVo reqVo) {
        switch (reqVo.getStrategy()) {
            case "seawallWarnStatistic": {
                SeawallWarnStatisticReqVo reqVoParam = reqVo.getParam("reqVo", SeawallWarnStatisticReqVo.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("seawallWarnStatistic", reqVoParam))) == null) {
                    result = seawallWarnStatistic(reqVoParam);
                }
                return result;
            }
            case "seawallProjStats": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("seawallProjStats"))) == null) {
                    result = seawallProjStats();
                }
                return result;
            }
            case "seawallProjList": {
                SeawallListQueryReqVo reqVoParam = reqVo.getParam("reqVo", SeawallListQueryReqVo.class);
                return seawallProjList(reqVoParam);
            }
            case "seawallUnitWarnCard": {
                SeawallWarnReqVo reqVoParam = reqVo.getParam("reqVo", SeawallWarnReqVo.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("seawallUnitWarnCard", reqVoParam))) == null) {
                    result = seawallUnitWarnCard(reqVoParam);
                }
                return result;
            }
            case "seawallUnitCWindow": {
                SeawallWarnReqVo reqVoParam = reqVo.getParam("reqVo", SeawallWarnReqVo.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("seawallUnitCWindow", reqVoParam))) == null) {
                    result = seawallUnitCWindow(reqVoParam);
                }
                return result;
            }
        }
        return null;
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项海塘缓存任务");
        SeawallWarnStatisticReqVo reqVoParam = new SeawallWarnStatisticReqVo();
        cache.put(buildDefaultCacheKey("seawallProjStats"), seawallProjStats());
        cache.put(buildDefaultCacheKey("seawallWarnStatistic", reqVoParam), seawallWarnStatistic(reqVoParam));
        SeawallWarnReqVo reqVoParam2 = new SeawallWarnReqVo();
        cache.put(buildDefaultCacheKey("seawallUnitWarnCard", reqVoParam2), seawallUnitWarnCard(reqVoParam2));
        cache.put(buildDefaultCacheKey("seawallUnitCWindow", reqVoParam2), seawallUnitCWindow(reqVoParam2));
    }

    @Override
    public ExecutorService getExecutor() {
        return getDefaultConfigExecutor();
    }

    /**
     * 每5分钟提交一次任务
     */
    @Scheduled(cron = "0 1/5 * * * ? ")
    public void updateCacheTask() {
        doBuild(null);
    }

    public CommonResult seawallWarnStatistic(SeawallWarnStatisticReqVo reqVo) {
        boolean drill = reqVo.getDrill() != null && reqVo.getDrill();
        List<SeawallDrillFormReqVo> formList = reqVo.getFormList();
        /**
         * 1. 查询所有的海塘
         * 2. 根据海塘的代表测站，查询实时实时潮位
         * 3. 统计正常，蓝色，黄色，橙色，红色等物种潮位预警的长度
         */
        Double normLength = 0D;
        Double blueLength = 0D;
        Double yellowLength = 0D;
        Double orangeLength = 0D;
        Double redLength = 0D;
        Integer normNum = 0;
        Integer blueNum = 0;
        Integer yellowNum = 0;
        Integer orangeNum = 0;
        Integer redNum = 0;
        List<SeawallDrillResVo> points = Collections.emptyList();
        JSONObject result = new JSONObject();
        CommonResult<List<JSONObject>> projResp = projectManageClient.projects(Builder.of(ProjectManageQueryVo::new).with(ProjectManageQueryVo::setType, "海塘").build());
        if (CollectionUtils.isBlank(projResp.getData())) {
            result.put("normLength", normLength);
            result.put("blueLength", blueLength);
            result.put("yellowLength", yellowLength);
            result.put("orangeLength", orangeLength);
            result.put("redLength", redLength);

            result.put("normNum", normNum);
            result.put("blueNum", blueNum);
            result.put("yellowNum", yellowNum);
            result.put("orangeNum", orangeNum);
            result.put("redNum", redNum);
            result.put("points", points);
            return CommonResult.success(result);
        }
        List<com.ygkj.big.screen.model.AttSeawallBase> projData = JSONArray.parseArray(JSONArray.toJSONString(projResp.getData(), SerializerFeature.WriteMapNullValue), com.ygkj.big.screen.model.AttSeawallBase.class);
        List<AttStBase> stData = Collections.emptyList();
        if (!drill) {
            CommonResult<List<AttStBase>> stResp = graphClient.waterStationsBody(Builder.of(SynWaterRequestVo::new)
                    .with(SynWaterRequestVo::setStationType, StationType.TIDE.type())
                    .with(SynWaterRequestVo::setStCodes, projData.stream()
                            .map(com.ygkj.big.screen.model.AttSeawallBase::getTideStationCode)
                            .filter(StringUtils::isNotBlank)
                            .collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER)))
                    .build());
            stData = stResp.getData();
        } else {
            formList = formList == null ? Collections.emptyList() : formList;
            stData = formList.stream().map(o -> Builder.of(AttStBase::new)
                    .with(AttStBase::setStCode, o.getStCode())
                    .with(AttStBase::setStName, o.getStName())
                    .with(AttStBase::setTdz, o.getDrillTdz())
                    .with(AttStBase::setTime, o.getDataTime())
                    .build()).collect(Collectors.toList());
        }

        Map<String, AttStBase> stMap = stData.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity(),(k1,k2)->k2));
        points = new ArrayList<>(projData.size());
        for (com.ygkj.big.screen.model.AttSeawallBase temp : projData) {
            SeawallDrillResVo target = new SeawallDrillResVo();
            BeanUtils.copyProperties(temp, target);
            points.add(target);
            AttStBase st = stMap.get(target.getTideStationCode());
            if (st == null) {
                target.setWarnState("norm");
                continue;
            }
            Double tdzTemp = st.getTdz();
            target.setTdz(tdzTemp);
            target.setDataTime(st.getTime());
            if (target.getTideStationRedLevel() != null && tdzTemp > target.getTideStationRedLevel()) {
                target.setWarnState("red");
            } else if (target.getTideStationOrangeLevel() != null && tdzTemp > target.getTideStationOrangeLevel()) {
                target.setWarnState("orange");
            } else if (target.getTideStationYellowLevel() != null && tdzTemp > target.getTideStationYellowLevel()) {
                target.setWarnState("yellow");
            } else if (target.getTideStationBlueLevel() != null && tdzTemp > target.getTideStationBlueLevel()) {
                target.setWarnState("blue");
            } else {
                target.setWarnState("norm");
            }
        }
        Map<String, List<SeawallDrillResVo>> projGroupMap = points.stream().collect(Collectors.groupingBy(SeawallDrillResVo::getWarnState));
        Iterator<Map.Entry<String, List<SeawallDrillResVo>>> iterator = projGroupMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<SeawallDrillResVo>> entry = iterator.next();
            String key = entry.getKey();
            List<SeawallDrillResVo> value = entry.getValue();
            switch (key) {
                case "norm":
                    normLength = value.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(com.ygkj.big.screen.model.AttSeawallBase::getSeawallLength).sum();
                    normNum = value.size();
                    break;
                case "blue":
                    blueLength = value.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(com.ygkj.big.screen.model.AttSeawallBase::getSeawallLength).sum();
                    blueNum = value.size();
                    break;
                case "yellow":
                    yellowLength = value.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(com.ygkj.big.screen.model.AttSeawallBase::getSeawallLength).sum();
                    yellowNum = value.size();
                    break;
                case "orange":
                    orangeLength = value.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(com.ygkj.big.screen.model.AttSeawallBase::getSeawallLength).sum();
                    orangeNum = value.size();
                    break;
                case "red":
                    redLength = value.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(com.ygkj.big.screen.model.AttSeawallBase::getSeawallLength).sum();
                    redNum = value.size();
                    break;
                default:
                    break;
            }
        }
        result.put("normLength", Double.parseDouble(String.format("%.1f", normLength / 1000)));
        result.put("blueLength", Double.parseDouble(String.format("%.1f", blueLength / 1000)));
        result.put("yellowLength", Double.parseDouble(String.format("%.1f", yellowLength / 1000)));
        result.put("orangeLength", Double.parseDouble(String.format("%.1f", orangeLength / 1000)));
        result.put("redLength", Double.parseDouble(String.format("%.1f", redLength / 1000)));

        result.put("normNum", normNum);
        result.put("blueNum", blueNum);
        result.put("yellowNum", yellowNum);
        result.put("orangeNum", orangeNum);
        result.put("redNum", redNum);
        result.put("points", points);
        return CommonResult.success(result);
    }

    public CommonResult seawallProjStats() {
        return projectManageClient.seawallStatsForBigScreen();
    }

    public CommonResult seawallProjList(SeawallListQueryReqVo reqVo) {
        ProjectManageQueryVo queryVo = Builder.of(ProjectManageQueryVo::new)
                .with(ProjectManageQueryVo::setType, "海塘")
                .with(ProjectManageQueryVo::setAreaCode, reqVo.getAdcd())
                .with(ProjectManageQueryVo::setGrade, reqVo.getGrade())
                .with(ProjectManageQueryVo::setProjectName, reqVo.getSeawallName())
                .build();
        if (StringUtils.isNotBlank(reqVo.getDesignTideStandard())) {
            if ("100年以上".equals(reqVo.getDesignTideStandard())) {
                queryVo.setDesignTideStandardStart(100);
            } else if ("10年以下".equals(reqVo.getDesignTideStandard())) {
                queryVo.setDesignTideStandardEnd(10);
            } else {
                try {
                    Integer temp = Integer.valueOf(reqVo.getDesignTideStandard().replace("年一遇", StringUtils.EMPTY));
                    queryVo.setDesignTideStandardStart(temp);
                    queryVo.setDesignTideStandardEnd(temp);
                } catch (Exception e) {
                    return CommonResult.failed("非法入参");
                }
            }
        }
        List<AttSeawallBase> list = Collections.emptyList();
        Map<String, Long> map = Collections.emptyMap();
        CommonResult<List<JSONObject>> resp = projectManageClient.projects(queryVo);
        String[] keys = {"lt10Year", "eq10Year", "eq20Year", "eq50Year", "eq100Year", "gt100Year", "notStandard"};
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            list = JSONArray.parseArray(JSONObject.toJSONString(resp.getData(), SerializerFeature.WriteMapNullValue), AttSeawallBase.class);
            Map<String, String> adcdMap = selectAdnmByAdcd(list.stream()
                    .map(AttSeawallBase::getAreaCode)
                    .filter(StringUtils::isNotBlank).collect(Collectors.toList()));
            map = list.stream().collect(Collectors.groupingBy(o -> {
                if (o.getDesignTideStandard() == null) {
                    return keys[6];
                } else if (o.getDesignTideStandard() < 10) {
                    return keys[0];
                } else if (o.getDesignTideStandard() > 100) {
                    return keys[5];
                } else {
                    switch (o.getDesignTideStandard().intValue()) {
                        case 10:
                            return keys[1];
                        case 20:
                            return keys[2];
                        case 50:
                            return keys[3];
                        case 100:
                            return keys[4];
                        default:
                            return StringUtils.EMPTY;
                    }
                }
            }, Collectors.counting()));
            for (AttSeawallBase temp : list) {
                temp.setAreaName(adcdMap.get(temp.getAreaCode()));
            }
            if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
                Comparator<AttSeawallBase> comparator = new BeanComparator<>(reqVo.getOrderColumn());
                if (reqVo.getAscOrDesc() != null && reqVo.getAscOrDesc()) {
                    comparator = comparator.reversed();
                }
                list = list.stream().sorted(comparator).collect(Collectors.toList());
            }

        }
        JSONObject result = new JSONObject();
        for (String key : keys) {
            result.put(key, map.getOrDefault(key, 0L));
        }
        result.put("total", list.size());
        result.put("list", list);
        return CommonResult.success(result);
    }

    private Map<String, String> selectAdnmByAdcd(List<String> adcdList) {
        if (org.springframework.util.CollectionUtils.isEmpty(adcdList)) {
            return Collections.emptyMap();
        }
        List<AttAdcdBase> records = this.adcdBaseMapper.selectAdnmByAdcdList(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, adcdList));
        if (org.springframework.util.CollectionUtils.isEmpty(records)) {
            return Collections.emptyMap();
        }
        return records.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
    }

    public Object rightMapPoints(String type) {
        return projectManageClient.projects(Builder.of(ProjectManageQueryVo::new).with(ProjectManageQueryVo::setType, type).build()).getData();
    }


    /**
     * 海塘组件C位
     *
     * @param reqVo
     * @return
     */
    public CommonResult seawallUnitCWindow(SeawallWarnReqVo reqVo) {
        return projectManageClient.seawallUnitWarnCard(reqVo);
    }

    /**
     * 海塘组件报警卡片
     *
     * @param reqVo
     * @return
     */
    public CommonResult seawallUnitWarnCard(SeawallWarnReqVo reqVo) {
        return projectManageClient.seawallUnitWarnCard(reqVo);
    }

}
