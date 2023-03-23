package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.controller.FloodDroughtDefenseController;
import com.ygkj.big.screen.feign.*;
import com.ygkj.big.screen.mapper.*;
import com.ygkj.big.screen.service.DrillService;
import com.ygkj.big.screen.service.ResultsWarnIndicatorService;
import com.ygkj.big.screen.service.WaterDisasterPreventionService;
import com.ygkj.big.screen.unit.*;
import com.ygkj.big.screen.feign.*;
import com.ygkj.big.screen.mapper.*;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.unit.*;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.enums.StationType;
import com.ygkj.flood.vo.request.AttTeamBasePageQueryVo;
import com.ygkj.flood.vo.request.AttWarehousePageQueryVo;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.request.SeawallWarnReqVo;
import com.ygkj.utils.*;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ygkj.com.util.DateUtil;
import ygkj.com.util.MydateUtil;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service("waterDisasterPreventionService")
@Slf4j
public class WaterDisasterPreventionServiceImpl implements WaterDisasterPreventionService {

    private GraphClient graphClient;
    private ProjectManageClient projectManageClient;
    private MeteorologicVisualizationClient mvClient;
    private FloodProtectClient floodProtectClient;
    private AttStBaseMapper stBaseMapper;
    private AttAdcdBaseMapper adcdBaseMapper;
    private ProjectMapper projectMapper;
    private WaterAndRainMapper waterAndRainMapper;
    private MetaDbMapper metaDbMapper;
    private WarningMessageManager warningMessageManager;
    private MessageRecordMapper messageRecordMapper;

    @Autowired
    private WeatherManager weatherManager;

    @Autowired
    private ShortForecastManager shortForecastManager;

    @Autowired
    private MountainTorrentManager mountainTorrentManager;

    @Autowired
    private RealRainfallManager realRainfallManager;

    @Autowired
    private WaterLoggingManager waterLoggingManager;

    @Autowired
    private WaterFlowManager waterFlowManager;

    @Autowired
    private TyphoonManager typhoonManager;

    @Autowired
    private TideManager tideManager;

    @Autowired
    private RiverNetManager riverNetManager;

    @Autowired
    private WagaManager wagaManager;

    @Autowired
    private RsvrManager rsvrManager;

    @Autowired
    private SeawallManager seawallManager;

    @Autowired
    private CameraManager cameraManager;

    @Autowired
    private DilapidatedProjectManager dilapProjManager;

    @Autowired
    private RescueSupportManager rescueSupportManager;

    @Autowired
    private BsDrillStdataMapper drillStdataMapper;


    @Autowired
    private AttPrevTfBaseMapper attPrevTfBaseMapper;
    @Autowired
    private MountainFloodWarnDataMapper mountainFloodWarnDataMapper;
    @Autowired
    private ResultsWarnIndicatorService resultsWarnIndicatorService;
    @Autowired
    private ResultsWarnIndicatorMapper resultsWarnIndicatorMapper;
    @Autowired
    private FloodDroughtDefenseController floodDroughtDefenseController;
    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private DrillService drillService;


    @Value("#{${dict.astrotd}}")
    private Map<String, String> astrotdMap;

    RestTemplate restTemplate;

    Map<String, String> wzCountyMap;

    public WaterDisasterPreventionServiceImpl(@Autowired GraphClient graphClient,
                                              @Autowired ProjectManageClient projectManageClient,
                                              @Autowired MeteorologicVisualizationClient mvClient,
                                              @Autowired FloodProtectClient floodProtectClient,
                                              @Autowired AttStBaseMapper stBaseMapper,
                                              @Autowired AttAdcdBaseMapper adcdBaseMapper,
                                              @Autowired ProjectMapper projectMapper,
                                              @Autowired WaterAndRainMapper waterAndRainMapper,
                                              @Autowired MetaDbMapper metaDbMapper,
                                              @Autowired WarningMessageManager warningMessageManager,
                                              @Autowired MessageRecordMapper messageRecordMapper
    ) {
        this.graphClient = graphClient;
        this.projectManageClient = projectManageClient;
        this.mvClient = mvClient;
        this.floodProtectClient = floodProtectClient;
        this.stBaseMapper = stBaseMapper;
        this.adcdBaseMapper = adcdBaseMapper;
        this.projectMapper = projectMapper;
        this.waterAndRainMapper = waterAndRainMapper;
        this.metaDbMapper = metaDbMapper;
        this.warningMessageManager = warningMessageManager;
        this.messageRecordMapper = messageRecordMapper;
        restTemplate = new RestTemplate();
    }

    @Override
    public JSONArray weatherWarning() {
//        return JSON.parseArray(HttpClientUtil.httpGet("http://www.wz121.com/map/getWeatherWarn"));
        return (JSONArray) weatherManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "weatherWarning")
                .build());
    }

    /**
     * 水库工程统计，统计（总数、超汛限、正常）*（全部、大中型、小（Ⅰ）型、小（Ⅱ）型）
     *
     * @param dataTime
     */
    @Override
    public JSONObject rsvrProjStats1(Date dataTime, String drillAffairId) {
        if (StringUtils.isNotBlank(drillAffairId)) {
            return rsvrProjStats1ForDrill(dataTime, drillAffairId);
        }

        return (JSONObject) rsvrManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "rsvrProjStats1")
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .build());
    }

    private JSONObject rsvrProjStats1ForDrill(Date dataTime, String drillAffairId) {
        String caseId = null;
        if (StringUtils.isNotBlank(drillAffairId)
                && StringUtils.isNotBlank((caseId = drillService.getDrillCaseIdByAffairId(drillAffairId)))) {
            String result = null;

            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                result = drillService.getDrillData(caseId, "水库", "waterDisasterPrevention/rsvrProjStats1", null, dataTime, 0);
                if (result != null) {
                    return JSONObject.parseObject(result);
                }
            } catch (Exception ignore) {

            }
        }
        return null;
    }

    /**
     * 水库工程统计，统计大中型、小（Ⅰ）型、小（Ⅱ）型，平均蓄水率、最小纳蓄能力的水库
     *
     * @param scaleType 大中型/小型
     * @param dataTime  数据时间
     * @return
     */
    @Override
    public JSONObject rsvrProjStats2(String scaleType, Date dataTime, String drillAffairId) {
        if (StringUtils.isNotBlank(drillAffairId)) {
            return rsvrProjStats2ForDrill(dataTime, drillAffairId);
        } else {
            return (JSONObject) rsvrManager.getData(Builder.of(UnitDataReqVo::new)
                    .with(UnitDataReqVo::setStrategy, "rsvrProjStats2")
                    .with(UnitDataReqVo::putParam, "scaleType", scaleType)
                    .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                    .build());
        }
    }

    private JSONObject rsvrProjStats2ForDrill(Date dataTime, String drillAffairId) {
        String caseId = null;
        if (StringUtils.isNotBlank(drillAffairId)
                && StringUtils.isNotBlank((caseId = drillService.getDrillCaseIdByAffairId(drillAffairId)))) {
            String result = null;

            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                result = drillService.getDrillData(caseId, "水库", "waterDisasterPrevention/rsvrProjStats2", null, dataTime, 0);
                if (result != null) {
                    return JSONObject.parseObject(result);
                }
            } catch (Exception ignore) {

            }
        }
        return null;
    }

    private List<RsvrStationWaterResVo> getLargeMidRsvrDrillData(Date dataTime) {
        List<RsvrStationWaterResVo> result = metaDbMapper.selectRsvrDrillDataByTime(dataTime);
        result = result == null ? Collections.emptyList() : result;
        if (CollectionUtils.isNotBlank(result)) {
            List<AttResBase> rsvrTempList = projectMapper.selectLargeMidRsvrByCode();
            rsvrTempList = rsvrTempList == null ? Collections.emptyList() : rsvrTempList;
            Map<String, AttResBase> rsvrMap = rsvrTempList.stream().collect(Collectors.toMap(AttResBase::getResCode, Function.identity()));
            for (RsvrStationWaterResVo temp : result) {
                AttResBase rsvr = rsvrMap.get(temp.getResCode());
                if (rsvr == null) {
                    continue;
                }
                BeanUtils.copyProperties(rsvr, temp);
                if (StringUtils.isNotBlank(temp.getAreaCode())) {
                    temp.setAreaName(wzCountyMap.getOrDefault(temp.getAreaCode(), StringUtils.EMPTY));
                }
            }
        }
        return result;
    }

    /**
     * 大中型水库演练水位曲线
     *
     * @param resCode 水库编码
     * @param start   演练起始时刻：格式是"yyyy-MM-dd HH:00:00"
     * @param end     演练截止时刻：格式是"yyyy-MM-dd HH:00:00"
     * @return
     */
    @Override
    public List<RsvrStationWaterResVo> lmRsvrDrillWlCurve(String resCode, Date start, Date end, Boolean timeForward, String drillAffairId) {
        if (start == null || end == null || StringUtils.isBlank(resCode)) {
            return Collections.emptyList();
        }
        String caseId = null;
        BsDrillCase drillCase = null;
        if (StringUtils.isNotBlank(drillAffairId)
                && (drillCase = drillService.getDrillCaseByAffairId(drillAffairId)) != null) {
            caseId = drillCase.getId();
        } else {
            return Collections.emptyList();
        }
        // 前端总是传错，估计是不好处理
        start = drillCase.getStartTime();
//        if (start==null){
//            start=drillCase.getStartTime();
//        }
        // 如果不为空，则需要将当前时间往后推backTimeSub毫秒
        start = DateUtils.clearDateTimePrecision(start, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);
        end = DateUtils.clearDateTimePrecision(end, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);

        List<BsDrillStdata> stdataList = this.drillStdataMapper.selectDataByTimeInterval(resCode, caseId, start, end);
        stdataList = stdataList == null ? Collections.emptyList() : stdataList;
        stdataList = stdataList.stream().sorted(Comparator.comparing(BsDrillStdata::getTimeStamp)).collect(Collectors.toList());
//        List<RsvrStationWaterResVo> result = this.metaDbMapper.selectSingleRsvrDrillDataByTimeInterval(resCode, start, end);
        List<RsvrStationWaterResVo> result = new ArrayList<>(stdataList.size());
        Date rightNow = DateUtils.localDateTime2Date(LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).plusHours(1));
        long forwardTimeSub = rightNow.getTime() - start.getTime();
        for (BsDrillStdata stdata : stdataList) {
            RsvrStationWaterResVo resVo = JSONObject.parseObject(stdata.getStData(), RsvrStationWaterResVo.class);
            if (timeForward != null && timeForward) {
                resVo.setDataTime(new Date(stdata.getTimeStamp().getTime() + forwardTimeSub));
            } else {
                resVo.setDataTime(stdata.getTimeStamp());
            }
            result.add(resVo);
        }
        return result;
    }

    /**
     * 水库工程统计，统计大中型水库24小时的泄流量
     *
     * @param dataTime 复盘时刻：格式是"yyyy-MM-dd HH:mm:ss"，不传则为最新值
     * @return
     */
    @Override
    public JSONObject rsvrProjDrainageStats(Date dataTime) {
        return (JSONObject) rsvrManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "rsvrProjDrainageStats")
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .build());
    }

    /**
     * 水库工程统计列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject rsvrProjList(RsvrStationWaterReqVo reqVo) {
        return (JSONObject) rsvrManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "rsvrProjList")
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .build());
    }

    /**
     * 水闸测站统计:水闸测站总数/温瑞平(水闸数目/开闸数目/泄流总量
     *
     * @param dataTime 复盘时刻
     * @param hours    过去多少小时
     * @return
     */
    @Override
    public JSONObject wagaStaStats(Date dataTime, Integer hours) {
        return (JSONObject) wagaManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "wagaStaStats")
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .with(UnitDataReqVo::putParam, "hours", hours)
                .build());
    }

    @Override
    public JSONObject wagaProjStats(Date dataTime, Integer hours) {
        return (JSONObject) wagaManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "wagaProjStats")
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .with(UnitDataReqVo::putParam, "hours", hours)
                .build());
    }

    /**
     * 水闸工程列表 同时统计水闸数量、小型水闸数量、闸门开启数量、24h累计排洪量（所有水闸的过闸流量*24小时）
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject wagaProjList(WrpWagaQueryReqVo reqVo) {
        return (JSONObject) wagaManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "wagaProjList")
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .build());
    }

    /**
     * 多个测站的水站总泄流量
     *
     * @param data 带着水闸历史监测数据的水闸测站
     * @param end  结束时刻
     * @return
     */
    private double wagaTotalDrainage(List<MultiWaterRainResVo> data, Date end) {
        double drainage = 0D;
        if (CollectionUtils.isBlank(data)) {
            return drainage;
        }
        if (CollectionUtils.isNotBlank(data)) {
            for (MultiWaterRainResVo temp : data) {
                List<StPrrwtResVo> waterRains = temp.getWaterRains();
                if (CollectionUtils.isBlank(waterRains)) {
                    continue;
                }
                // 过滤掉最大过闸流量po无法转换数字的值。
                waterRains = waterRains.stream().filter(o ->
                        NumberUtils.isCreatable(o.getLl())
                ).collect(Collectors.toList());
                if (CollectionUtils.isBlank(waterRains)) {
                    continue;
                }
                int size = waterRains.size();
                double singleDrainage = 0D;
                for (int index = 0; index < size; index++) {
                    StPrrwtResVo c = waterRains.get(index);
                    Date tempEnd;
                    if (index + 1 < size) {
                        StPrrwtResVo n = waterRains.get(index + 1);
                        tempEnd = n.getTm();
                    } else {
                        tempEnd = end;
                    }
                    singleDrainage += (new BigDecimal(c.getLl()).doubleValue() * (tempEnd.getTime() - c.getTm().getTime()) / 1000) / 10000;
                }
                drainage += singleDrainage;
            }
        }
        return drainage;
    }

    /**
     * 根据潮位名称，获取实时潮位和天文潮位的曲线
     *
     * @param tideStaName 潮位测站名称
     * @param dataTime    复盘时刻
     * @return
     */
    @Override
    public JSONObject tideCurve(String tideStaName, Date dataTime) {
        return (JSONObject) tideManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "tideCurve")
                .with(UnitDataReqVo::putParam, "tideStaName", tideStaName)
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .build());
    }

    /**
     * 潮位列表
     *
     * @return
     */
    @Override
    public JSONObject tideList(TideListQueryReqVo reqVo) {
        return (JSONObject) tideManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "tideList")
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .build());
    }

    /**
     * 根据行政区划编码查询行政区划名称并组成以编码为key的map
     *
     * @param adcdList
     * @return
     */
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

    /**
     * 查询温州各个区县
     *
     * @return
     */
    @PostConstruct
    public Map<String, String> selectWzCounty() {
        List<AttAdcdBase> records = this.adcdBaseMapper.queryAllAdOfWz();
        if (org.springframework.util.CollectionUtils.isEmpty(records)) {
            return Collections.emptyMap();
        }
        this.wzCountyMap = records.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        return wzCountyMap;
    }


    /**
     * astroTideTable
     *
     * @param tideStaName
     * @param dataTime    复盘时刻
     * @return
     */
    @Override
    public List<JSONObject> astroTideTable(String tideStaName, Date dataTime) {
        return (List<JSONObject>) tideManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "astroTideTable")
                .with(UnitDataReqVo::putParam, "tideStaName", tideStaName)
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .build());
    }

    /**
     * 统计海塘长度，海塘段数
     *
     * @return
     */
    @Override
    public CommonResult seawallProjStats() {
//        return projectManageClient.seawallStatsForBigScreen();
        return (CommonResult) seawallManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "seawallProjStats")
                .build());
    }

    @Override
    public CommonResult seawallProjList(SeawallListQueryReqVo reqVo) {
        return (CommonResult) seawallManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "seawallProjList")
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .build());
    }

    /**
     * 海塘组件C位
     *
     * @param reqVo
     * @return
     */
    @Override
    public CommonResult seawallUnitCWindow(SeawallWarnReqVo reqVo) {
        return (CommonResult) seawallManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "seawallUnitCWindow")
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .build());
    }

    /**
     * 海塘组件报警卡片
     *
     * @param reqVo
     * @return
     */
    @Override
    public CommonResult seawallUnitWarnCard(SeawallWarnReqVo reqVo) {
        return (CommonResult) seawallManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "seawallUnitWarnCard")
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .build());
    }

    @Override
    public CommonResult weatherPredict(String areaName) {
//        return mvClient.weatherPredict(areaName);
        return (CommonResult) weatherManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "weatherPredict")
                .build());
    }

    @Override
    public CommonResult predictRainfallByAreaName(String areaName) {
//        return mvClient.predictRainfallByAreaName(StringUtils.isEmpty(areaName) ? "" : areaName);
        return (CommonResult) shortForecastManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "predictRainfallByAreaName")
                .with(UnitDataReqVo::putParam, "areaName", areaName)
                .build());
    }

    @Override
    public CommonResult rainfallMaxValuePredict(Integer hour) {
//        return mvClient.rainfallMaxValuePredict(hour);
        return (CommonResult) shortForecastManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "rainfallMaxValuePredict")
                .with(UnitDataReqVo::putParam, "hour", hour)
                .build());
    }

    @Override
    public CommonResult rainfallPredictGrid(String type) {
//        return mvClient.rainfallPredictGrid(type);
        return (CommonResult) shortForecastManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "rainfallPredictGrid")
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }

    @Override
    public CommonResult grandTotalMaxRainfallStatistic(String endTime, Integer hour, String drillAffairId) {
        hour = hour == null ? 0 : hour;
        // 演练数据
        String caseId = null;
        if (StringUtils.isNotBlank(drillAffairId)
                && StringUtils.isNotBlank((caseId = drillService.getDrillCaseIdByAffairId(drillAffairId)))) {
            String result = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                JSONObject params = new JSONObject(true);
                params.put("hour", hour);
                params.put("endTime", endTime);
                result = drillService.getDrillData(caseId, "实时降雨", "/waterDisasterPrevention/grandTotalMaxRainfallStatistic", params.toJSONString(), sdf.parse(endTime), 0);
                if (result != null) {
                    return JSON.toJavaObject(JSON.parseObject(result), CommonResult.class);
                }
            } catch (Exception ignore) {

            }
        }
        //
//        return graphClient.grandTotalMaxRainfallStatistic(endTime, hour);
        return (CommonResult) realRainfallManager.getData(Builder
                .of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "grandTotalMaxRainfallStatistic")
                .with(UnitDataReqVo::putParam, "endTime", endTime)
                .with(UnitDataReqVo::putParam, "hour", hour)
                .build());
    }


    @Override
    public synchronized CommonResult grandTotalRainfallGrid(SynRainRequestVo requestVo, String endTime, String type, String surfaceType, Integer hour) {
        return graphClient.grandTotalRainfallGrid(requestVo, endTime, type, surfaceType, hour);
    }

    @Override
    public CommonResult grandTotalReservoirRainfall(String endTime, Integer hour, String drillAffairId) {
        // 演练数据
        String caseId = null;
        if (StringUtils.isNotBlank(drillAffairId)
                && StringUtils.isNotBlank((caseId = drillService.getDrillCaseIdByAffairId(drillAffairId)))) {
            String result = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                JSONObject params = new JSONObject(true);
                params.put("hour", hour);
                params.put("endTime", endTime);
                result = drillService.getDrillData(caseId, "实时降雨", "/waterDisasterPrevention/grandTotalReservoirRainfall", params.toJSONString(), sdf.parse(endTime), 0);
                if (result != null) {
                    return JSON.toJavaObject(JSON.parseObject(result), CommonResult.class);
                }
            } catch (Exception ignore) {
                return CommonResult.success();
            }
        }
//        return graphClient.grandTotalReservoirRainfall(endTime, hour);
        return (CommonResult) realRainfallManager.getData(Builder
                .of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "grandTotalReservoirRainfall")
                .with(UnitDataReqVo::putParam, "endTime", endTime)
                .with(UnitDataReqVo::putParam, "hour", hour)
                .build());
    }

    /**
     * 河网测站统计，统计正常、超警戒、超保证数目
     *
     * @param dataTime 复盘时刻
     * @return
     */
    @Override
    public JSONObject riverNetStats(Date dataTime, Integer type) {
        return (JSONObject) riverNetManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "riverNetStats")
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }

    /**
     * 河网列表
     *
     * @return
     */
    @Override
    public JSONObject riverNetworkList(RivNetListQueryReqVo reqVo) {
        return (JSONObject) riverNetManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "riverNetworkList")
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .build());
    }

    /**
     * 病险工程统计
     *
     * @return
     */
    @Override
    public List<ChartResVo<String, Long>> dilapProjStats() {
        return (List<ChartResVo<String, Long>>) dilapProjManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "dilapProjStats")
                .build());
    }

    /**
     * 病险工程列表
     *
     * @return
     */
    @Override
    public CommonResult dilapProjList(DilapProjReqVo reqVo) {
//        if (reqVo == null) {
//            reqVo = new DilapProjReqVo();
//        }
//        reqVo.setResWaterLevel(true);
//        return projectManageClient.dilapProjList(reqVo);
        return (CommonResult) dilapProjManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "dilapProjList")
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .build());
    }

    /**
     * 工程风险统计
     *
     * @param type
     * @return
     */
    @Override
    public JSONObject projRiskStats(String type) {
        JSONObject result = new JSONObject();
        if (StringUtils.isBlank(type)) {
            return result;
        }
        switch (type) {
            case "风险类型": {
                result.put("hiddenTrouble", 96.65);
                result.put("highRisk", 0D);
                result.put("weakSpot", 3.35);
            }
            break;
            case "隐患点销号情况": {
                result.put("hiddenTrouble", 96.65);
                result.put("highRisk", 0D);
                result.put("weakSpot", 3.35);
            }
            break;
            default:
                break;
        }
        return result;
    }

    /**
     * 工程风险列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public CommonResult projRiskList(ProjRiskReqVo reqVo) {
        if (reqVo == null) {
            reqVo = new ProjRiskReqVo();
        }
        return projectManageClient.projRiskList(reqVo);
    }

    /**
     * 右侧地图落点
     *
     * @param type          : 水库/水闸/河网/潮位/海塘/病险工程
     * @param dilapProjType 病险工程类型：水库/水闸/山塘/海塘
     * @return
     */
    @Override
    public Object rightMapPoints(String type, String dilapProjType) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        switch (type) {
            case "水库": {
//                List<LinkedHashMap> list = projectManageClient.rsvrWaterLevel(new RsvrStationWaterReqVo()).getData().getList();
//                return list;
                return rsvrManager.getData(Builder.of(UnitDataReqVo::new)
                        .with(UnitDataReqVo::setStrategy, "allRsvrRealtimeLevel")
                        .build());
            }
            case "海塘": {
                return projectManageClient.projects(Builder.of(ProjectManageQueryVo::new).with(ProjectManageQueryVo::setType, type).build()).getData();
            }
            case "水闸": {
                return projectManageClient.wagaWaterLevel(Builder.of(WagaStationWaterReqVo::new).with(WagaStationWaterReqVo::setWrpWaga, true).build()).getData().getList();
            }
            case "河网": {
//                return graphClient.listRivernet(Builder.of(RivernetWaterLevelReqVo::new)
//                        .build()).getData();
                return riverNetManager.getData(Builder.of(UnitDataReqVo::new)
                        .with(UnitDataReqVo::setStrategy, "allRivernetRealtimeLevel")
                        .build());
            }
            case "潮位": {
                return graphClient.waterStationsBody(Builder.of(SynWaterRequestVo::new)
                        .with(SynWaterRequestVo::setStationType, StationType.TIDE.type())
                        .with(SynWaterRequestVo::setStCodes, String.join(",", this.astrotdMap.get("鳌江"), astrotdMap.get("瑞安"), astrotdMap.get("灵昆"), astrotdMap.get("温州")))
                        .build()).getData();
            }
            case "病险工程": {
                DilapProjReqVo reqVo = new DilapProjReqVo();
                if (StringUtils.isNotBlank(dilapProjType)) {
                    dilapProjType = dilapProjType.replace("病险", StringUtils.EMPTY);
                    reqVo.setProjType(dilapProjType);
                }
                reqVo.setWagaWaterLevel(true);
                reqVo.setResWaterLevel(true);
                reqVo.setSeawallInfo(true);
                reqVo.setMnpdInfo(true);
                try {
                    return projectManageClient.dilapProjList(reqVo).getData().getList();
                } catch (Exception e) {
                    return Collections.emptyList();
                }
            }
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public CommonResult wholeWeather() {
//        return mvClient.wholeWeather();
        return (CommonResult) weatherManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "wholeWeather")
                .build());
    }

    /**
     * 山洪预警模块
     * 旧的模块我实在是改不下去了
     *
     * @param endTime
     * @param currentFlag
     * @param drillAffairId
     * @return
     */
    @Override
    public JSONObject floodWarning4NewVersion(String endTime, Boolean currentFlag, String drillAffairId) {
        // 演练事务不为空,说明处于演练状态中,取演练的值
        if (StringUtils.isNotBlank(drillAffairId)) {
            String caseId = null;
            if (StringUtils.isNotBlank(drillAffairId)
                    && StringUtils.isNotBlank((caseId = drillService.getDrillCaseIdByAffairId(drillAffairId)))) {
                String result = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    result = drillService.getDrillData(caseId, "山洪预警", "waterDisasterPrevention/floodWarning", null, sdf.parse(endTime), 0);
                    if (result != null) {
                        return JSON.parseObject(result);
                    }
                } catch (Exception ignore) {

                }
            }
        } else if (currentFlag != null && currentFlag) {
            // 此时为实时监测值
            return (JSONObject) mountainTorrentManager.getData(Builder.of(UnitDataReqVo::new)
                    .with(UnitDataReqVo::setStrategy, "floodWarning4Monitor")
//                    .with(UnitDataReqVo::putParam, "endTime", endTime)
                    .build());
        } else {
            // 什么都不传就是大禹山洪预报数据
            return (JSONObject) mountainTorrentManager.getData(Builder.of(UnitDataReqVo::new)
                    .with(UnitDataReqVo::setStrategy, "floodWarning4Predict")
                    .build());
        }
        return null;
    }

    /**
     * 山洪基础数据落点
     *
     * @return
     */
    @Override
    public MountainTorrentBaseInfoStats mountainTorrentBaseStats() {
        return (MountainTorrentBaseInfoStats) mountainTorrentManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "mountainTorrentBaseStats")
                .build());
    }

    /**
     * 山洪C位统计
     *
     * @param dataTime
     * @return
     */
    @Override
    public JSONObject mountainTorrentCWindow(Date dataTime) {
        return (JSONObject) mountainTorrentManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "mountainTorrentCWindow")
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .build());
    }

    /**
     * 山洪C位详表
     *
     * @param dataTime
     * @param warnType
     * @return
     */
    @Override
    public Object mountainTorrentCWindowTable(Date dataTime, Integer warnType) {
        return mountainTorrentManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "mountainTorrentCWindowTable")
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .with(UnitDataReqVo::putParam, "warnType", warnType)
                .build());
    }

    /**
     * 山洪C位详表批量报警
     *
     * @param reqVo
     * @return
     */
    @Override
    public CommonResult batchSendWarnSms(MtSendWarnReqVo reqVo) {
        return mountainTorrentManager.batchSendWarnSms(reqVo.getWarnList(), reqVo.getWarnType());
    }

    /**
     * 山洪报警落点
     *
     * @param dataTime
     * @return
     */
    @Override
    public Object mountainTorrentWarnMapPoint(Date dataTime) {
        return mountainTorrentManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "mountainTorrentWarnMapPoint")
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .build());
    }

    /**
     * 山洪报警落点单个报警
     *
     * @param reqVo
     * @return
     */
    @Override
    public CommonResult singleSendWarnSms(MtSendWarnReqVo reqVo) {
        return mountainTorrentManager.singleSendWarnSms(reqVo.getWarnDetail(), reqVo.getWarnType());
    }

    Map<String, String> floodWarnStatus = new HashMap<>();//0:准备转移 1:立即转移
    List<AttPrevTfBase> attPrevTfBasesAll = new ArrayList<>();

    /**
     * @param endTime     为空则预报，不为空则
     * @param currentFlag true 监测 false 演练
     * @return
     */
    @Deprecated
    @Override
    public synchronized JSONObject floodWarning(String endTime, Boolean currentFlag, String drillAffairId) {
        if (StringUtils.isNotEmpty(endTime)) {

            String caseId = null;
            if (StringUtils.isNotBlank(drillAffairId)
                    && StringUtils.isNotBlank((caseId = drillService.getDrillCaseIdByAffairId(drillAffairId)))) {
                String result = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    result = drillService.getDrillData(caseId, "山洪预警", "waterDisasterPrevention/floodWarning", null, sdf.parse(endTime), 0);
                    if (result != null) {
                        return JSON.parseObject(result);
                    }
                } catch (Exception ignore) {

                }
            }

            long startSystemTime = System.currentTimeMillis();
            JSONObject result = new JSONObject();

            Integer grade4County = 0;   //准备转移区县
            Integer grade4villages = 0; //准备转移村落
            Integer grade4street = 0;   //准备转移乡镇
            Integer grade4Person = 0;   //准备转移人口

            Integer grade5County = 0;    //立即转移区县
            Integer grade5villages = 0;  //立即转移村落
            Integer grade5street = 0;    //立即转移乡镇
            Integer grade5Person = 0;    //立即转移人口

            List<AttPrevTfBase> attPrevTfBases = attPrevTfBaseMapper.selectList(null);//村落
            List<ResultsWarnIndicator> resultsWarnIndicatorList = resultsWarnIndicatorService.selectList(null);//指标
            List<AttPrevTfBase> attPrevTfBasesCp = new ArrayList<>();

            if (currentFlag) {

                String tableName = StationType.RAINFALL.table() + endTime.substring(0, 7).replaceAll("-", "");
                //查找每个测站的离endTime最近的水位数据
                List<StPptnR> stPptnRS = attPrevTfBaseMapper.recentWaterLevelData(tableName,
                        attPrevTfBases.stream().map(e -> e.getStationCode()).collect(Collectors.toList()), endTime);

                for (AttPrevTfBase attPrevTfBase : attPrevTfBases) {
                    // 村落名称
                    String nvi = attPrevTfBase.getNvi();
                    // 村落人口
                    Integer tpo = attPrevTfBase.getTpo();//村子人数
                    // 自然村
                    String avi = attPrevTfBase.getAvi();
                    String stationCode = attPrevTfBase.getStationCode();

                    // 筛选除当前防治对象的预警成果
                    List<ResultsWarnIndicator> collect = resultsWarnIndicatorList.stream()
                            .filter(e -> nvi.equals(e.getNvi()) && avi.equals(e.getAvi())).collect(Collectors.toList());

                    // 遍历预警成果
                    for (ResultsWarnIndicator resultsWarnIndicator : collect) {
                        BigDecimal readyMove = resultsWarnIndicator.getReadyMove();//准备转移
                        BigDecimal immediateTransfer = resultsWarnIndicator.getImmediateTransfer();//立即转移
                        // 筛选绑定测站
                        List<StPptnR> collect1 = stPptnRS.stream().filter(e -> e.getStcd().equals(stationCode)).collect(Collectors.toList());
                        if (!collect1.isEmpty()) {
                            BigDecimal period = resultsWarnIndicator.getPeriod();//时段
                            String tm = DateUtil.format(collect1.get(0).getTm(), "yyyy-MM-dd HH:mm:ss");
                            double v = period.setScale(1, BigDecimal.ROUND_UP).doubleValue();
                            Date nextDay = null;
                            nextDay = getDate(tm, nextDay, v);

                            Double data = mountainFloodWarnDataMapper.findRainDataByCondition(stationCode,
                                    DateUtil.format(nextDay, "yyyy-MM-dd HH:mm:ss"), tm, tableName);


                            if (immediateTransfer.doubleValue() < data) {
                                double thresholdData = new BigDecimal(data).subtract(readyMove).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                attPrevTfBase.setThresholdData(thresholdData);
                                attPrevTfBase.setThresholdTime(DateUtil.format(nextDay, "yyyy-MM-dd HH:mm:ss") + "至" + tm);
                                attPrevTfBase.setWarnGradeID("5");//5 立即转移 4 准备转移
                                grade5County++;
                                grade5villages++;
                                grade5street++;
                                grade5Person += tpo;
                                attPrevTfBasesCp.add(attPrevTfBase);
                            }
                            if (readyMove.doubleValue() < data) {
                                double thresholdData = new BigDecimal(data).subtract(readyMove).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                attPrevTfBase.setThresholdData(thresholdData);
                                attPrevTfBase.setThresholdTime(DateUtil.format(nextDay, "yyyy-MM-dd HH:mm:ss") + "至" + tm);
                                attPrevTfBase.setWarnGradeID("4");//5 立即转移 4 准备转移
                                grade4County++;
                                grade4villages++;
                                grade4street++;
                                grade4Person += tpo;
                                attPrevTfBasesCp.add(attPrevTfBase);
                            }
                        }
                    }
                }
            } else {
                attPrevTfBasesCp = getAttPrevTfBase(endTime);

            }

            JSONObject data = new JSONObject();
            attPrevTfBasesCp = attPrevTfBasesCp.stream().collect(
                    collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getLat() + ";" + o.getLon()))), ArrayList::new)
            );
            if (!attPrevTfBasesCp.isEmpty()) {
                List<ResultsWarnVo> resultsWarnVos = new ArrayList<>();
                ResultsWarnVo resultsWarnVo = null;
                Map<String, List<AttPrevTfBase>> stringListMap = attPrevTfBasesCp.stream().collect(Collectors.groupingBy(AttPrevTfBase::getTown));
                for (Map.Entry<String, List<AttPrevTfBase>> stringListEntry : stringListMap.entrySet()) {
                    resultsWarnVo = new ResultsWarnVo();
                    String key = stringListEntry.getKey();
                    List<AttPrevTfBase> value = stringListEntry.getValue();


                    List<Villages> villagesList = new ArrayList<>();
                    Villages villages = null;
                    for (AttPrevTfBase attPrevTfBase : value) {
                        List<AttPrevTfBase> collect = attPrevTfBases.stream().filter(e -> e.getLon()
                                .equals(attPrevTfBase.getLon()) && e.getLat().equals(attPrevTfBase.getLat())).collect(Collectors.toList());
                        if (!collect.isEmpty()) {
                            AttPrevTfBase attPrevTfBase1 = collect.get(0);
                            Integer tpo = attPrevTfBase1.getTpo();
                            if ("5".equals(attPrevTfBase.getWarnGradeID())) {
                                grade5County++;
                                grade5villages++;
                                grade5street++;
                                grade5Person += tpo;
                            } else if ("4".equals(attPrevTfBase.getWarnGradeID())) {
                                grade4County++;
                                grade4villages++;
                                grade4street++;
                                grade4Person += tpo;
                            }
                            villages = new Villages();
                            villages.setThresholdTime(attPrevTfBase.getThresholdTime());
                            villages.setThresholdData(attPrevTfBase.getThresholdData());
                            villages.setAdnm(attPrevTfBase.getAvi());
                            villages.setLat(attPrevTfBase.getLat() + "");
                            villages.setLng(attPrevTfBase.getLon() + "");
                            villages.setWarnGradeID(attPrevTfBase.getWarnGradeID());
                            villages.setEffectPersonNumber(attPrevTfBase.getTpo());
                            villagesList.add(villages);
                        }

                    }
                    resultsWarnVo.setVillages(villagesList);
                    resultsWarnVo.setAdnm(key);
                    resultsWarnVos.add(resultsWarnVo);
                }

                data.put("town", resultsWarnVos);
                data.put("promptlyMoveVillage", "");
                data.put("effectPeople", "");
                data.put("warningTown", "");
                data.put("warningMoveVillage", "");
                data.put("rows", null);

                result.put("data", data);
            } else {
                result.put("data", null);
            }
            data.put("grade5County", grade5County);
            data.put("grade5villages", grade5villages);
            data.put("grade5street", grade5street);
            data.put("grade5Person", grade5Person);

            data.put("grade4County", grade4County);
            data.put("grade4villages", grade4villages);
            data.put("grade4street", grade4street);
            data.put("grade4Person", grade4Person);

//            result.put("code",0);
//            result.put("message","操作成功");
            if ("2019-08-13 11:00:00".equals(endTime)) {
                floodWarnStatus.clear();
                attPrevTfBasesAll.clear();
            }
            long endSystemTime = System.currentTimeMillis();
            log.info("cal time {}", endSystemTime - startSystemTime);
            //2021.06.19 发送山洪预警短信 huang.zh
            JSONArray town = data.getJSONArray("town");
            if (town != null) {
//                List<MessageRecord> records = warningMessageManager.submit(MessageTypes.MOUNTAIN_FLOOD.type(), town);
//                messageRecordMapper.batchInsert(records);
            }
//            result.put("data", data);
            return data;
        }
        try {
            String url = "http://122.224.94.107:8034/FloodWarning/WarnCount";
            Map<String, Object> params = new HashMap<>();
            params.put("autoCalc", "false");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            params.put("searchTm", format.format(date));
            params.put("searchTm2", format.format(date));
            params.put("regionCode", 3303);
            params.put("adcdCode", 3303);
            params.put("format", "json");
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            String s = HttpClientUtil.sendGet(url, headers, params, "UTF-8");
            JSONObject object = JSONObject.parseObject(s);
            JSONArray town = object.getJSONArray("town");
            JSONObject result = new JSONObject();
            Map<String, List<Object>> map = town.stream().collect(Collectors.groupingBy(json -> ((JSONObject) json).getString("countyName")));
            map.forEach((areaName, list) -> {
                Set<String> set4 = new HashSet<>();
                Set<String> set5 = new HashSet<>();
                list.stream().forEach(o -> {
                    JSONArray villages = ((JSONObject) o).getJSONArray("villages");
                    long count4 = villages.stream().map(village -> ((JSONObject) village).getIntValue("warnGradeID")).filter(integer -> integer == 4).count();
                    result.put("grade4villages", result.getIntValue("grade4villages") + count4);
                    long count5 = villages.stream().map(village -> ((JSONObject) village).getIntValue("warnGradeID")).filter(integer -> integer == 5).count();
                    result.put("grade5villages", result.getIntValue("grade5villages") + count5);
                    if (count4 > 0) {
                        result.put("grade4street", result.getIntValue("grade4street") + 1);
                        if (!set4.contains(areaName)) {
                            set4.add(areaName);
                            result.put("grade4County", result.getIntValue("grade4County") + 1);
                        }
                        result.put("grade4Person", result.getIntValue("grade4Person") + ((JSONObject) o).getIntValue("effectPeople"));
                    }
                    if (count5 > 0) {
                        result.put("grade5street", result.getIntValue("grade5street") + 1);
                        if (!set5.contains(areaName)) {
                            set5.add(areaName);
                            result.put("grade5County", result.getIntValue("grade5County") + 1);
                        }
                        result.put("grade4Person", result.getIntValue("grade4Person") + ((JSONObject) o).getIntValue("effectPeople"));
                    }

                });
            });
            result.put("data", object);
            return result;
        } catch (Exception ignore) {
            ignore.printStackTrace();
            return new JSONObject();
        }
    }

    public synchronized CommonResult clearFloodWarning() {
        floodWarnStatus.clear();
        attPrevTfBasesAll.clear();
        return CommonResult.success();
    }

    private Date getDate(String tm, Date nextDay, double v) {
        if (v == 0.5) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -30, null);
        }
        if (v == 1.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -60, null);
        }
        if (v == 1.5) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -90, null);
        }
        if (v == 2.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -120, null);
        }
        if (v == 2.5) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -150, null);
        }
        if (v == 3.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -180, null);
        }
        if (v == 3.5) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -210, null);
        }
        if (v == 4.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -240, null);
        }
        if (v == 4.5) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -270, null);
        }
        if (v == 6.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -360, null);
        }
        if (v == 9.0) {
            nextDay = MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", tm), null,
                    null, null, null, -540, null);
        }
        return nextDay;
    }

    @Override
    public CommonResult judgeRainfallIntervalStation(String beginTime, String endTime) {
        return graphClient.judgeRainfallIntervalStation(beginTime, endTime);
    }

    @Override
    public CommonResult customRainfallSurfaceStatistic(String beginTime, String endTime) {
        return graphClient.customRainfallSurfaceStatistic(beginTime, endTime);
    }

    @Override
    public CommonResult customRainfallSurfaceData(String beginTime, String endTime) {
        return graphClient.customRainfallSurfaceData(beginTime, endTime);
    }

    /**
     * 水库复盘统计
     *
     * @return
     */
    @Override
    public CommonResult rsvrReplayStats(RsvrReplayReqVo reqVo) {
        if (reqVo == null) {
            reqVo = new RsvrReplayReqVo();
        }
        reqVo.setHighestWaterLevelCapacity(true);
        reqVo.setLowestWaterLevelCapacity(true);
        return projectManageClient.rsvrReplayStats(reqVo);
    }

    /**
     * 温瑞平水闸复盘统计
     *
     * @return
     */
    @Override
    public CommonResult wrpWagaReplayStats(WagaReplayReqVo reqVo) {
        if (reqVo == null) {
            reqVo = new WagaReplayReqVo();
        }
        reqVo.setWrpWaga(true);
        return projectManageClient.wagaReplayStats(reqVo);
    }

    /**
     * 河网复盘统计
     *
     * @return
     */
    @Override
    public CommonResult rivernetReplayStats(RivernetReplayReqVo reqVo) {
//        return graphClient.rivernetReplayStats(reqVo);
        return (CommonResult) riverNetManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "rivernetReplayStats")
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .build());
    }

    /**
     * 潮位复盘曲线
     *
     * @param tideStaName 潮位站名称
     * @param start       复盘起始时刻
     * @param end         复盘结束时刻
     * @return
     */
    @Override
    public JSONObject tideReplayCurve(String tideStaName, Date start, Date end) {
        JSONObject result = new JSONObject();
        result.put("astroTide", Collections.emptyList());
        result.put("realTide", Collections.emptyList());
        result.put("subTide", Collections.emptyList());
        result.put("warnTide", 0D);

        if ("龙湾".equals(tideStaName)) {
            tideStaName = "灵昆";
        }
        String stcd = astrotdMap.get(tideStaName);
        if (StringUtils.isBlank(stcd)) {
            return null;
        }
        CommonResult<List<LinkedHashMap>> realTideResp = this.graphClient.multiWaterRains(Builder.of(MultiWaterRainReqVo::new)
                .with(MultiWaterRainReqVo::setStcds, stcd)
                .with(MultiWaterRainReqVo::setStart, start)
                .with(MultiWaterRainReqVo::setEnd, end)
                .build());
        CommonResult<List<LinkedHashMap>> astroTideResp = this.graphClient.multiWaterRains(Builder.of(MultiWaterRainReqVo::new)
                .with(MultiWaterRainReqVo::setStcds, stcd)
                .with(MultiWaterRainReqVo::setStart, start)
                .with(MultiWaterRainReqVo::setEnd, end)
                .with(MultiWaterRainReqVo::setAstrotd, true)
                .build());
        List<StPrrwtResVo> realTempList = null;
        if (realTideResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(realTideResp.getData())) {
            List<MultiWaterRainResVo> data = JSONArray.parseArray(JSONArray.toJSONString(realTideResp.getData()), MultiWaterRainResVo.class);
            MultiWaterRainResVo tide = data.get(0);
            result.put("warnTide", tide.getWarningLevel() == null ? 0D : tide.getWarningLevel());
            realTempList = tide.getWaterRains();
        }
        realTempList = realTempList == null ? Collections.emptyList() : realTempList;
        Map<Date, StPrrwtResVo> realMap = realTempList.stream().collect(Collectors.groupingBy(StPrrwtResVo::getTm)).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().get(0)));

        List<StPrrwtResVo> astroTempList = null;
        if (astroTideResp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(astroTideResp.getData())) {
            List<MultiWaterRainResVo> data = JSONArray.parseArray(JSONArray.toJSONString(astroTideResp.getData()), MultiWaterRainResVo.class);
            astroTempList = data.get(0).getWaterRains();
        }
        astroTempList = astroTempList == null ? Collections.emptyList() : astroTempList;

        // 将天文潮变成每5分钟一个点
        List<StPrrwtResVo> finalAstroList = new ArrayList<>(astroTempList.size() * 12);
        int astroLength = astroTempList.size();
        int nextIndex = 0;
        for (int i = 0; i < astroLength; i++) {
            StPrrwtResVo current = astroTempList.get(i);
            nextIndex = i + 1;
            if (nextIndex < astroLength) {
                StPrrwtResVo next = astroTempList.get(nextIndex);
                int j = 0;
                double avg = (next.getTdz() - current.getTdz()) / 12;
                while (j < 12) {
                    if (j == 0) {
                        finalAstroList.add(current);
                    } else {
                        StPrrwtResVo temp = new StPrrwtResVo();
                        BeanUtils.copyProperties(current, temp);
                        temp.setTm(new Date(current.getTm().getTime() + (long) j * 5 * 60 * 1000));
                        temp.setTdz(Double.parseDouble(String.format("%.2f", temp.getTdz() + j * avg)));
                        finalAstroList.add(temp);
                    }
                    j++;
                }
            } else {
                finalAstroList.add(current);
            }
        }
        Map<Date, StPrrwtResVo> astroMap = finalAstroList.stream().peek(o -> o.setTm(DateUtils.clearDateTimePrecision(o.getTm(), Calendar.SECOND, Calendar.MILLISECOND))).collect(Collectors.toMap(StPrrwtResVo::getTm, Function.identity()));

        LocalDateTime tempStartLd = DateUtils.date2LocalDateTime(start);
        int startMinutesTemp = tempStartLd.getMinute();
        LocalDateTime tempEndLd = DateUtils.date2LocalDateTime(end);
        int endMinutesTemp = tempEndLd.getMinute();
        int startLeft = startMinutesTemp % 5;
        int startMinutes = 0;
        if (startLeft == 0) {
            startMinutes = startMinutesTemp;
        } else {
            startMinutes = (startMinutesTemp / 5 + 1) * 5;
        }
        int endLeft = endMinutesTemp % 5;
        int endMinutes = 0;
        if (endLeft == 0) {
            endMinutes = endMinutesTemp;
        } else {
            endMinutes = (endMinutesTemp / 5 - 1) * 5;
        }
        Date tempStart = DateUtils.localDateTime2Date(tempStartLd.withNano(0).withMinute(startMinutes).withSecond(0));
        Date tempEnd = DateUtils.localDateTime2Date(tempEndLd.withNano(0).withMinute(endMinutes).withSecond(0));
        int size = (int) ((tempEnd.getTime() - tempStart.getTime()) / (5 * 60 * 1000));

        // 将实时潮位和天文潮位按时间一个个匹配
        List<StPrrwtResVo> resultRealList = new ArrayList<>(size);
        ;
        List<StPrrwtResVo> resultAstroList = new ArrayList<>(size);
        ;
        List<StPrrwtResVo> subTideList = new ArrayList<>(size);
        Date timeIndex = tempStart;
        while (!timeIndex.after(tempEnd)) {
            StPrrwtResVo realTemp = realMap.get(timeIndex);
            StPrrwtResVo astroTemp = astroMap.get(timeIndex);
            if (realTemp != null && astroTemp != null) {
                subTideList.add(Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, timeIndex).with(StPrrwtResVo::setTdz, Double.parseDouble(String.format("%.2f", realTemp.getTdz() - astroTemp.getTdz()))).build());
            } else {
                if (realTemp == null) {
                    realTemp = Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, timeIndex).build();
                }
                if (astroTemp == null) {
                    astroTemp = Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, timeIndex).build();
                }
                subTideList.add(Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, astroTemp.getTm()).build());
            }
            resultRealList.add(realTemp);
            resultAstroList.add(astroTemp);
            timeIndex = new Date(timeIndex.getTime() + 5 * 60 * 1000);
        }
        result.put("astroTide", resultAstroList);
        result.put("realTide", resultRealList);
        result.put("subTide", subTideList);
        return result;
    }

    @Override
    public CommonResult tideReplayStats(TideReplayReqVo reqVo) {
        return graphClient.tideReplayStats(reqVo);
    }

    AtomicInteger lastTimeTotalWarnNum = new AtomicInteger(0);

    /**
     * 报警地图落点
     *
     * @param type 暴雨/河网/水库/山洪/潮位  不传则查全部
     * @return
     */
    @Override
    public Object warnMapPoints(String type,
                                String endTime, String drillAffairId) {
        int totalWarn = 0;
        List<JSONObject> tempList = new ArrayList<>();
        // 暴雨
        if (StringUtils.isBlank(type) || "暴雨".equals(type)) {
            totalWarn += rainstormWarnMapPoint(endTime, tempList, drillAffairId);
        }
        // 山洪
        if (StringUtils.isBlank(type) || "山洪".equals(type)) {
            totalWarn += torrentWarnMapPoint(endTime, tempList, drillAffairId);
        }
        // 水库
        if (StringUtils.isBlank(type) || "水库".equals(type)) {
            totalWarn += rsvrWarnMapPoint(endTime, tempList, drillAffairId);
        }
        // 河网
        if (StringUtils.isBlank(type) || "河网".equals(type)) {
            totalWarn += rnWarnMapPoint(tempList);
        }
        // 潮位
        if (StringUtils.isBlank(type) || "潮位".equals(type)) {
            totalWarn += tideWarnMapPoint(tempList);
        }
        JSONObject result = new JSONObject();
        result.put("totalWarn", totalWarn);
        result.put("list", tempList);
        result.put("doWarn", totalWarn > lastTimeTotalWarnNum.getAndSet(totalWarn));
        return result;
    }

    public int testWarnMapPoint() {
        return lastTimeTotalWarnNum.getAndSet(0);
    }

    /**
     * 报警地图落点统计
     *
     * @return
     */
    @Override
    public JSONObject warnMapPointStats() {
        JSONObject result = new JSONObject();
        JSONObject list = new JSONObject();
        JSONObject stats = new JSONObject();
        // 暴雨报警
        {
            CommonResult rainfallCR = floodDroughtDefenseController.rainfall("温州市", null, null);//暴雨
            JSONObject jo2 = JSONObject.parseObject(JSONObject.toJSONString(rainfallCR.getData(), SerializerFeature.WriteMapNullValue));
            List<StPptnR> stPptnRS1 = jo2.getJSONArray("rainfall_1_data").toJavaList(StPptnR.class).stream().filter(e -> e.getDrp() > 30).peek(o -> {
                o.setIntv(60D);
                o.setStcd(o.getMgstcd());
            }).collect(Collectors.toList());
            list.put("rainfall1Warn", stPptnRS1);
            stats.put("rainfall1Warn", stPptnRS1.size());
            List<StPptnR> stPptnRS3 = jo2.getJSONArray("rainfall_3_data").toJavaList(StPptnR.class).stream().filter(e -> e.getDrp() > 60).peek(o -> {
                o.setIntv(180D);
                o.setStcd(o.getMgstcd());
            }).collect(Collectors.toList());
            list.put("rainfall3Warn", stPptnRS3);
            stats.put("rainfall3Warn", stPptnRS3.size());
        }
        // 山洪预报
        {
            List<Villages> grad4List = flashFloodWarnList(null, true, null, "4");
            list.put("mountainTorrentGrad4", grad4List);
            stats.put("mountainTorrentGrad4", grad4List.size());
            List<Villages> grad5List = flashFloodWarnList(null, true, null, "5");
            list.put("mountainTorrentGrad5", grad5List);
            stats.put("mountainTorrentGrad5", grad5List.size());
        }
        // 水库
        {
            List<JSONObject> tempList = new ArrayList<>();
            int warnNum = rsvrWarnMapPoint(null, tempList, null);
            list.put("rsvrWarn", tempList.get(0).getJSONArray("points"));
            stats.put("rsvrWarn", warnNum);
        }
        // 河网
        {
            List<JSONObject> tempList = new ArrayList<>();
            int warnNum = rnWarnMapPoint(tempList);
            list.put("riverNetWarn", tempList.get(0).getJSONArray("points"));
            stats.put("riverNetWarn", warnNum);
        }
        // 潮位
        {
            List<JSONObject> tempList = new ArrayList<>();
            int warnNum = tideWarnMapPoint(tempList);
            list.put("tideWarn", tempList.get(0).getJSONArray("points"));
            stats.put("tideWarn", warnNum);
        }

        result.put("stats", stats);
        result.put("list", list);
        return result;
    }

    /**
     * 地图报警落点导出
     *
     * @param response
     */
    @Override
    public void exportWarnMapPoint(HttpServletResponse response) {
        List<ExcelExportEntity> list = new ArrayList<>();
        JSONObject listObject = warnMapPointStats().getJSONObject("list");
        Function<Object, Object> drpMapping = (o) -> {
            try {
                return new BigDecimal(String.valueOf(o)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            } catch (Exception e) {
                return null;
            }
        };
        List<StPptnR> rainfall1WarnList = listObject.getJSONArray("rainfall1Warn").toJavaList(StPptnR.class);
        list.add(new ExcelExportEntity("1小时降雨(" + rainfall1WarnList.size() + ")", rainfall1WarnList,
                new String[]{"站点名称", "报警时间", "1小时累计降雨量（mm）"},
                new String[]{"stationName", "tm", "rainfallOneData"}, Collections.singletonMap("rainfallOneData", drpMapping))
        );
        List<StPptnR> rainfall3WarnList = listObject.getJSONArray("rainfall3Warn").toJavaList(StPptnR.class);
        list.add(new ExcelExportEntity("3小时降雨(" + rainfall3WarnList.size() + ")", rainfall3WarnList,
                new String[]{"站点名称", "报警时间", "3小时累计降雨量（mm）"},
                new String[]{"stationName", "tm", "rainfallThreeData"}, Collections.singletonMap("rainfallThreeData", drpMapping))
        );
        List<RsvrStationWaterResVo> rsvrWarnList = listObject.getJSONArray("rsvrWarn").toJavaList(RsvrStationWaterResVo.class);
        list.add(new ExcelExportEntity("水库(" + rsvrWarnList.size() + ")", rsvrWarnList,
                new String[]{"站点名称", "水位测量时间", "实时水位（m）", "汛限水位（m）", "超汛限水位（m）"},
                new String[]{"resName", "dataTime", "waterLevel", "currentLimitLevel", "overLimitVal"}, Collections.emptyMap())
        );
        List<RivernetWaterLevelResVo> riverNetWarnList = listObject.getJSONArray("riverNetWarn").toJavaList(RivernetWaterLevelResVo.class);
        list.add(new ExcelExportEntity("河网(" + riverNetWarnList.size() + ")", riverNetWarnList,
                new String[]{"站点名称", "报警时间", "实时水位（m）", "警戒水位（m）", "超警戒值（m）"},
                new String[]{"rnName", "dataTime", "waterLevel", "warningLevel", "overWarn"}, Collections.emptyMap())
        );
        List<AttStBase> tideWarnList = listObject.getJSONArray("tideWarn").toJavaList(AttStBase.class);
        list.add(new ExcelExportEntity("潮位(" + tideWarnList.size() + ")", tideWarnList,
                new String[]{"站点名称", "报警时间", "实时潮位（m）", "警戒水位（m）", "超警戒值（m）"},
                new String[]{"stName", "tm", "tdz", "warningLevel", "overWarnLevel"}, Collections.emptyMap())
        );
        List<Villages> mountainTorrentGrad4List = listObject.getJSONArray("mountainTorrentGrad4").toJavaList(Villages.class);
        list.add(new ExcelExportEntity("有可能发生山洪(" + mountainTorrentGrad4List.size() + ")", mountainTorrentGrad4List,
                new String[]{"村庄名称", "预警时间", "超预警指标", "预报降雨量", "预报时间"},
                new String[]{"adnm", "dataTime", "overWarnIndex", "forecastRainfall", "forecastTime"}, Collections.emptyMap())
        );
        List<Villages> mountainTorrentGrad5List = listObject.getJSONArray("mountainTorrentGrad5").toJavaList(Villages.class);
        list.add(new ExcelExportEntity("可能性很大(" + mountainTorrentGrad5List.size() + ")", mountainTorrentGrad5List,
                new String[]{"村庄名称", "预警时间", "超预警指标", "预报降雨量", "预报时间"},
                new String[]{"adnm", "dataTime", "overWarnIndex", "forecastRainfall", "forecastTime"}, Collections.emptyMap())
        );
        ExportUtils.exportExcel(list, "报警列表", response);
    }

    private static String judgeRainfallTable(String time) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:00").parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int monthIndex = calendar.get(Calendar.MONTH) + 1;
        String month;
        if (monthIndex < 10) {
            month = "0" + monthIndex;
        } else {
            month = String.valueOf(monthIndex);
        }
        String table = com.ygkj.enums.StationType.RAINFALL.table().concat(year.concat(month));
        return table;
    }

    /**
     * 暴雨报警落点
     *
     * @param endTime
     * @param tempList
     * @return
     */
    private int rainstormWarnMapPoint(String endTime, List<JSONObject> tempList, String drillAffairId) {
        if (StringUtils.isEmpty(endTime)) {
            List<StPptnR> points = Collections.emptyList();
            try {
                CommonResult rainfallCR = floodDroughtDefenseController.rainfall("温州市", null, null);//暴雨
                JSONObject jo2 = JSONObject.parseObject(JSONObject.toJSONString(rainfallCR.getData(), SerializerFeature.WriteMapNullValue));
                List<StPptnR> stPptnRS1 = jo2.getJSONArray("rainfall_1_data").toJavaList(StPptnR.class).stream().filter(e -> e.getDrp() > 30).peek(o -> {
                    o.setIntv(60D);
                    o.setStcd(o.getMgstcd());
                }).collect(Collectors.toList());
                List<StPptnR> stPptnRS3 = jo2.getJSONArray("rainfall_3_data").toJavaList(StPptnR.class).stream().filter(e -> e.getDrp() > 60).peek(o -> {
                    o.setIntv(180D);
                    o.setStcd(o.getMgstcd());
                }).collect(Collectors.toList());

                //                stPptnRS1.addAll(stPptnRS3);//合并数据组
                Map<String, StPptnR> rs1Map = stPptnRS1.stream().collect(Collectors.toMap(StPptnR::getMgstcd, Function.identity()));
                Map<String, StPptnR> rs3Map = stPptnRS3.stream().collect(Collectors.toMap(StPptnR::getMgstcd, Function.identity()));
                // 1h 和 3h 共同的，以1h 为准，1h没有，3h有的，则将3h点加入到1h中返回
                List<String> rs3Except = CollectionUtils.getExcept(new ArrayList<>(rs3Map.keySet()), new ArrayList<>(rs1Map.keySet()));
                String currTime = new SimpleDateFormat("yyyy-MM-dd HH:00:00").format(new Date());
                String tableName = judgeRainfallTable(currTime);
                List<StPptnR> stPptnROneList = waterAndRainMapper.selectPastHoursRainfall(tableName, currTime, 1, rs3Except);//1小时累计雨量
                List<StPptnR> stPptnRThreeList = waterAndRainMapper.selectPastHoursRainfall(tableName, currTime, 3, rs3Except);//3小时累计雨量
                if (CollectionUtils.isNotBlank(rs3Except)) {
                    for (String s : rs3Except) {
                        StPptnR temp = rs3Map.get(s);
                        if (temp != null) {
                            rs1Map.put(s, temp);
                        }
                    }
                }
                for (String stcd : rs1Map.keySet()) {
                    StPptnR stPptnR = rs1Map.get(stcd);
                    List<StPptnR> stPptnROne = stPptnROneList.stream().filter(e -> StringUtils.isNotBlank(stPptnR.getMgstcd()) &&
                            stPptnR.getMgstcd().equals(e.getMgstcd())).collect(Collectors.toList());
                    if (!stPptnROne.isEmpty()) {
                        stPptnR.setRainfallOneData(stPptnROne.get(0).getDrp());
                    }
                    List<StPptnR> stPptnRSThree = stPptnRThreeList.stream().filter(e -> StringUtils.isNotBlank(stPptnR.getMgstcd()) &&
                            stPptnR.getMgstcd().equals(e.getMgstcd())).collect(Collectors.toList());
                    if (!stPptnRSThree.isEmpty()) {
                        stPptnR.setRainfallThreeData(stPptnRSThree.get(0).getDrp());
                    }

                }
                points = new ArrayList<>(rs1Map.values());
            } catch (Exception e) {
                e.printStackTrace();
                points = Collections.emptyList();
            }
            JSONObject temp = new JSONObject();
            temp.put("points", points);
            temp.put("warnNum", points.size());
            temp.put("type", "暴雨");
            tempList.add(temp);
            return points.size();
        } else {
            List<StPptnR> points = Collections.emptyList();
            try {
                CommonResult rainfallCR = floodDroughtDefenseController.rainfall(null, endTime, drillAffairId);//暴雨
                JSONObject jo2 = (JSONObject) rainfallCR.getData();
                List<StPptnR> stPptnRS1 = jo2.getJSONArray("rainfall_1_data").toJavaList(StPptnR.class).stream().filter(e -> e.getDrp() > 30).peek(o -> {
                    o.setIntv(60D);
                    o.setStcd(o.getMgstcd());
                }).collect(Collectors.toList());
                List<StPptnR> stPptnRS3 = jo2.getJSONArray("rainfall_3_data").toJavaList(StPptnR.class).stream().filter(e -> e.getDrp() > 60).peek(o -> {
                    o.setIntv(180D);
                    o.setStcd(o.getMgstcd());
                }).collect(Collectors.toList());

                //                stPptnRS1.addAll(stPptnRS3);//合并数据组
                Map<String, StPptnR> rs1Map = stPptnRS1.stream().collect(Collectors.toMap(StPptnR::getMgstcd, Function.identity()));
                Map<String, StPptnR> rs3Map = stPptnRS3.stream().collect(Collectors.toMap(StPptnR::getMgstcd, Function.identity()));
                // 1h 和 3h 共同的，以1h 为准，1h没有，3h有的，则将3h点加入到1h中返回
                List<String> rs3Except = CollectionUtils.getExcept(new ArrayList<>(rs3Map.keySet()), new ArrayList<>(rs1Map.keySet()));
                if (CollectionUtils.isNotBlank(rs3Except)) {
                    for (String s : rs3Except) {
                        StPptnR temp = rs3Map.get(s);
                        if (temp != null) {
                            rs1Map.put(s, temp);
                        }
                    }
                }
                points = new ArrayList<>(rs1Map.values());
            } catch (Exception e) {
                points = Collections.emptyList();
            }
            JSONObject temp = new JSONObject();
            temp.put("points", points);
            temp.put("warnNum", points.size());
            temp.put("type", "暴雨");
            tempList.add(temp);
            return points.size();
        }
    }

    /**
     * 水库报警落点
     *
     * @param endTime
     * @param tempList
     * @return
     */
    private int rsvrWarnMapPoint(String endTime, List<JSONObject> tempList, String drillAffairId) {
        if (StringUtils.isEmpty(endTime)) {
            JSONObject temp = new JSONObject();
            List<LinkedHashMap> overWarnList = Collections.emptyList();
//            CommonResult<PageVO<LinkedHashMap>> resp = projectManageClient.rsvrWaterLevel(new RsvrStationWaterReqVo());
            List<LinkedHashMap> resp = (List<LinkedHashMap>) rsvrManager.getData(Builder.of(UnitDataReqVo::new)
                    .with(UnitDataReqVo::setStrategy, "allRsvrRealtimeLevel").build());
            if (CollectionUtils.isNotBlank(resp)) {
                List<LinkedHashMap> list = resp;
                overWarnList = list.stream().filter(o -> {
                    try {
                        return Double.parseDouble(String.valueOf(o.get("overLimitVal"))) > 0;
                    } catch (Exception e) {
                        return false;
                    }
                }).collect(Collectors.toList());
            }
            overWarnList = overWarnList == null ? Collections.emptyList() : overWarnList;
            temp.put("points", overWarnList);
            temp.put("warnNum", overWarnList.size());
            temp.put("type", "水库");
            tempList.add(temp);
            return overWarnList.size();
        } else {
            List<RsvrStationWaterResVo> rsvrStationWaterResVos = Collections.emptyList();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JSONObject jo1 = rsvrProjStats2("大中型", sdf.parse(endTime), drillAffairId);//水库

                JSONArray storageList = jo1.getJSONArray("storageList");//RsvrStationWaterResVo
                if (CollectionUtils.isNotBlank(storageList)) {
                    rsvrStationWaterResVos = storageList.toJavaList(RsvrStationWaterResVo.class);
                    rsvrStationWaterResVos = rsvrStationWaterResVos.stream().filter(o -> o.getOverLimitVal() != null && o.getOverLimitVal() > 0).collect(Collectors.toList());
                }
            } catch (Exception e) {

            }
            JSONObject temp = new JSONObject();
            temp.put("points", rsvrStationWaterResVos);
            temp.put("warnNum", rsvrStationWaterResVos.size());
            temp.put("type", "水库");
            tempList.add(temp);
            return rsvrStationWaterResVos.size();
        }
    }

    private int rnWarnMapPoint(List<JSONObject> tempList) {
        JSONObject temp = new JSONObject();
        List<LinkedHashMap> overWarnList = Collections.emptyList();

//        CommonResult<List<LinkedHashMap>> resp = graphClient.listRivernet(new RivernetWaterLevelReqVo());
//        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
//            List<LinkedHashMap> list = resp.getData();
        List<LinkedHashMap> resp = (List<LinkedHashMap>) riverNetManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "allRivernetRealtimeLevel")
                .build());
        if (CollectionUtils.isNotBlank(resp)) {
            List<LinkedHashMap> list = resp;
            overWarnList = list.stream().filter(o -> {
                try {
                    return Double.parseDouble(String.valueOf(o.get("waterLevel"))) > Double.parseDouble(String.valueOf(o.get("warningLevel")));
                } catch (Exception e) {
                    return false;
                }
            }).collect(Collectors.toList());
        }
        overWarnList = overWarnList == null ? Collections.emptyList() : overWarnList;
        temp.put("points", overWarnList);
        temp.put("warnNum", overWarnList.size());
        temp.put("type", "河网");
        tempList.add(temp);
        return overWarnList.size();
    }

    private int torrentWarnMapPoint(String endTime, List<JSONObject> tempList, String drillAffairId) {
        if (StringUtils.isEmpty(endTime)) {
            int predictWarnNum = 0;
            int monitorWarnNum = 0;
            {
                // 大禹山洪预报数据
                JSONObject result = (JSONObject) mountainTorrentManager.getData(Builder.of(UnitDataReqVo::new)
                        .with(UnitDataReqVo::setStrategy, "floodWarning4Predict")
                        .build());
                int warnNum = result.getIntValue("grade4villages") + result.getIntValue("grade5villages");
                JSONObject data = result.getJSONObject("data");
                List<JSONObject> points = new ArrayList<>(warnNum > 0 ? warnNum : 10);
                if (data != null) {
                    JSONArray town = data.getJSONArray("town");
                    if (town != null) {
                        for (Object o : town) {
                            JSONObject t = (JSONObject) o;
                            JSONArray villages = t.getJSONArray("villages");
                            if (villages != null) {
                                points.addAll(villages.toJavaList(JSONObject.class));
                            }
                        }
                    }
                }
                JSONObject temp = new JSONObject();
                temp.put("points", points);
                temp.put("warnNum", points.size());
                temp.put("type", "预报山洪");
                tempList.add(temp);
                predictWarnNum = points.size();
            }
            {
                List<MountainTorrentDetailResVo> allVillages = (List<MountainTorrentDetailResVo>) mountainTorrentManager.getData(Builder.of(UnitDataReqVo::new)
                        .with(UnitDataReqVo::setStrategy, "mountainTorrentWarningInfo")
                        .with(UnitDataReqVo::putParam, "reqParam", new MountainTorrentDrillReqVo())
                        .build());

                List<MountainTorrentDetailResVo> warnVillages = allVillages.stream().filter(village -> "5".equals(village.getWarnGradeID()) || "4".equals(village.getWarnGradeID())).collect(Collectors.toList());

                JSONObject temp = new JSONObject();
                temp.put("points", warnVillages);
                temp.put("warnNum", warnVillages.size());
                temp.put("type", "监测山洪");
                tempList.add(temp);
                monitorWarnNum = warnVillages.size();
            }
            return monitorWarnNum + predictWarnNum;
        } else {
            String caseId = null;
            if (StringUtils.isNotBlank(drillAffairId)
                    && StringUtils.isNotBlank((caseId = drillService.getDrillCaseIdByAffairId(drillAffairId)))) {
                String result = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    result = drillService.getDrillData(caseId, "山洪预警", "waterDisasterPrevention/warnMapPoints", null, sdf.parse(endTime), 0);
                    if (result != null) {
                        JSONObject temp = JSON.parseObject(result);
                        tempList.add(temp);
                        return temp.getInteger("warnNum");
                    }
                } catch (Exception ignore) {

                }
            }
            List<AttPrevTfBase> attPrevTfBaseList = getAttPrevTfBase(endTime);//山洪
            JSONObject temp = new JSONObject();
            temp.put("points", attPrevTfBaseList);
            temp.put("warnNum", attPrevTfBaseList.size());
            temp.put("type", "山洪");
            tempList.add(temp);
            return attPrevTfBaseList.size();

        }
    }

    /**
     * 潮位报警数量
     *
     * @param tempList
     * @return 报警数量
     */
    private int tideWarnMapPoint(List<JSONObject> tempList) {
        JSONObject temp = new JSONObject();
        List<AttStBase> overWarnList = Collections.emptyList();
        List<AttStBase> resp = (List<AttStBase>) tideManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "mainTideRealtimeLevel").build());
        if (CollectionUtils.isNotBlank(resp)) {
            List<AttStBase> list = resp;
            String adcdSuffixNum = "000000";
            Map<String, Optional<String>> stcdAdnmMap = list.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getAreaCode()))
                    .collect(Collectors.toMap(AttStBase::getStCode, o -> Optional.ofNullable(this.wzCountyMap.get(o.getAreaCode().substring(0, 6).concat(adcdSuffixNum)))));

            overWarnList = list.stream().peek(o -> o.setAreaName(stcdAdnmMap.getOrDefault(o.getStCode(), Optional.empty()).orElse(StringUtils.EMPTY))).filter(o -> {
                try {
                    if (o.getRedLevel() != null && o.getTdz() >= o.getRedLevel()) {
                        o.setTideWarnType("红色警戒");
                        return true;
                    } else if (o.getOrangeLevel() != null && o.getTdz() >= o.getOrangeLevel()) {
                        o.setTideWarnType("橙色警戒");
                        return true;
                    } else if (o.getYellowLevel() != null && o.getTdz() >= o.getYellowLevel()) {
                        o.setTideWarnType("黄色警戒");
                        return true;
                    } else if (o.getBlueLevel() != null && o.getTdz() >= o.getBlueLevel()) {
                        o.setTideWarnType("蓝色警戒");
                        return true;
                    }
                } catch (Exception e) {

                }
                return false;
            }).collect(Collectors.toList());
        }
        overWarnList = overWarnList == null ? Collections.emptyList() : overWarnList;

        temp.put("points", overWarnList);
        temp.put("warnNum", overWarnList.size());
        temp.put("type", "潮位");
        tempList.add(temp);
        return overWarnList.size();
    }


    /**
     * 水文工程测站概化图
     *
     * @param reqVo
     * @return
     */
    @Override
    public CommonResult hydrProjStaDiagram(HydrDiagReqVo reqVo) {
        return projectManageClient.hydrProjStaDiagram(reqVo);
    }

    /**
     * 水文工程概化图统计
     *
     * @return
     */
    @Override
    public CommonResult hydrProjStaDiagramStatistic(Integer hours) {
        return projectManageClient.hydrProjStaDiagramStatistic(hours);
    }

    /**
     * 降雨演练统计
     *
     * @param startTime
     * @param endTime
     * @param drillAffairId
     * @return
     * @throws ParseException
     */
    @Override
    public JSONObject rainstormDrillStatistics(String startTime, String endTime, String drillAffairId) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject jo1 = floodWarning4NewVersion(endTime, false, drillAffairId);
//        JSONObject jo1 = jsonObject.getJSONObject("data") == null ? new JSONObject() : jsonObject.getJSONObject("data");
        Integer gradevillages = 0;
        if (jo1.getInteger("grade5villages") != null) {
            gradevillages += jo1.getInteger("grade5villages");
        }
        if (jo1.getInteger("grade4villages") != null) {
            gradevillages += jo1.getInteger("grade4villages");
        }
        Integer grade5Person = jo1.getInteger("grade5Person");//立即转移人口数量
        Integer grade4Person = jo1.getInteger("grade4Person");//准备转移人口数量
        Integer overLimitNum = 0;//超汛限大中型水库
        try {
            JSONObject jo2 = rsvrProjStats2("大中型", sdf.parse(endTime), drillAffairId);
            JSONArray storageList = jo2.getJSONArray("storageList");
            for (int i = 0; i < storageList.size(); i++) {
                Double overLimitVal = storageList.getJSONObject(i).getDoubleValue("overLimitVal");
                if (overLimitVal != null && overLimitVal > 0) {
                    overLimitNum++;
                }
            }
        } catch (Exception e) {
        }
        JSONObject jo = new JSONObject();
        jo.put("gradevillages", gradevillages);
        List<JSONObject> tempList = new ArrayList<>(1);
        torrentWarnMapPoint(endTime, tempList, drillAffairId);
        jo.put("mountainWarnPoint", tempList.get(0).getJSONArray("points"));
        jo.put("grade5Person", grade5Person == null ? 0 : grade5Person);
        jo.put("grade4Person", grade4Person == null ? 0 : grade4Person);
        jo.put("overLimitNum", overLimitNum);
        tempList.clear();
        rsvrWarnMapPoint(endTime, tempList, drillAffairId);
        jo.put("rsvrWarnPoint", tempList.get(0).getJSONArray("points"));
        try {
            if (StringUtils.isNotBlank(startTime)) {
                Date startDateTime = sdf.parse(startTime);
                Date endDateTime = sdf.parse(endTime);
                Date rightNow = DateUtils.localDateTime2Date(LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).plusHours(1));
                long forwardTimeSub = rightNow.getTime() - startDateTime.getTime();
                jo.put("endTime", sdf.format(new Date(endDateTime.getTime() + forwardTimeSub)));
            } else {
                jo.put("endTime", endTime);
            }
        } catch (Exception e) {
            jo.put("endTime", endTime);
        }
        return jo;
    }


    @Override
    public JSONObject rainstormDrillWarn(String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<RsvrStationWaterResVo> rsvrStationWaterResVos = null;
        try {
            JSONObject jo1 = rsvrProjStats2("大中型", sdf.parse(endTime), null);//水库
            String storageList = jo1.getString("storageList");//RsvrStationWaterResVo
            rsvrStationWaterResVos = JSONObject.parseArray(storageList, RsvrStationWaterResVo.class);
        } catch (Exception e) {
            rsvrStationWaterResVos = new ArrayList<>();
        }


        List<RainstormDrillWarn> list = new ArrayList<>();
        RainstormDrillWarn rainstormDrillWarn = null;
        for (RsvrStationWaterResVo rsvrStationWaterResVo : rsvrStationWaterResVos) {
            rainstormDrillWarn = new RainstormDrillWarn();
            rainstormDrillWarn.setLat(rsvrStationWaterResVo.getStLat().doubleValue() + "");
            rainstormDrillWarn.setLng(rsvrStationWaterResVo.getStLong() + "");
            rainstormDrillWarn.setName(rsvrStationWaterResVo.getResName());
            list.add(rainstormDrillWarn);
        }

        Map<String, Object> rsvrMap = new HashMap<>();
        rsvrMap.put("num", list == null ? 0 : list.size());
        rsvrMap.put("list", list);

        List<StPptnR> rainStormList = Collections.emptyList();
        try {
            CommonResult rainfallCR = floodDroughtDefenseController.rainfall(null, endTime, null);//暴雨
            JSONObject jo2 = (JSONObject) rainfallCR.getData();
            List<StPptnR> stPptnRS1 = jo2.getJSONArray("rainfall_1_data").toJavaList(StPptnR.class).stream().filter(e -> e.getDrp() > 30).peek(o -> o.setIntv(60D)).collect(Collectors.toList());
            List<StPptnR> stPptnRS3 = jo2.getJSONArray("rainfall_3_data").toJavaList(StPptnR.class).stream().filter(e -> e.getDrp() > 60).peek(o -> o.setIntv(180D)).collect(Collectors.toList());
//                stPptnRS1.addAll(stPptnRS3);//合并数据组
            Map<String, StPptnR> rs1Map = stPptnRS1.stream().collect(Collectors.toMap(StPptnR::getStcd, Function.identity()));
            Map<String, StPptnR> rs3Map = stPptnRS3.stream().collect(Collectors.toMap(StPptnR::getStcd, Function.identity()));
            // 1h 和 3h 共同的，以1h 为准，1h没有，3h有的，则将3h点加入到1h中返回
            List<String> rs3Except = CollectionUtils.getExcept(new ArrayList<>(rs3Map.keySet()), new ArrayList<>(rs1Map.keySet()));
            if (CollectionUtils.isNotBlank(rs3Except)) {
                for (String s : rs3Except) {
                    StPptnR temp = rs3Map.get(s);
                    if (temp != null) {
                        rs1Map.put(s, temp);
                    }
                }
            }
            rainStormList = new ArrayList<>(rs1Map.values());
        } catch (Exception e) {
            rainStormList = Collections.emptyList();
        }
        Map<String, Object> stPptnMap = new HashMap<>();
        stPptnMap.put("num", rainStormList == null ? 0 : rainStormList.size());
        stPptnMap.put("list", rainStormList);

        List<AttPrevTfBase> attPrevTfBaseList = getAttPrevTfBase(endTime);//山洪

        list = new ArrayList<>();
        for (AttPrevTfBase attPrevTfBase : attPrevTfBaseList) {
            rainstormDrillWarn = new RainstormDrillWarn();
            rainstormDrillWarn.setLat(attPrevTfBase.getLat().doubleValue() + "");
            rainstormDrillWarn.setLng(attPrevTfBase.getLon().doubleValue() + "");
            rainstormDrillWarn.setName(attPrevTfBase.getNvi());
            list.add(rainstormDrillWarn);
        }

        Map<String, Object> attPrevMap = new HashMap<>();//山洪map
        attPrevMap.put("num", attPrevTfBaseList == null ? 0 : attPrevTfBaseList.size());
        attPrevMap.put("list", attPrevTfBaseList);

        JSONObject jo = new JSONObject();

        jo.put("rainstorm", stPptnMap);//暴雨
        jo.put("reservoir", rsvrMap);//水库
        jo.put("mountainTorrent", attPrevMap);//山洪
        return jo;
    }


    @Override
    public JSONObject flashFloodDetails(String nvi, String avi, Date endTime, String drillAffairId) {

        BsDrillCase drillCase = null;
        Date startTimeGate = null;
        if (StringUtils.isNotBlank(drillAffairId)
                && (drillCase = drillService.getDrillCaseByAffairId(drillAffairId)) != null) {
            startTimeGate = drillCase.getStartTime();
        }
        JSONObject result = new JSONObject();
        String tho = "";
        String tpo = "";
        String chargerPhone = "";
        String chargerName = "";
        String stationName = "";
        String stationCode = "";
        AttPrevTfBase attPrevTfBase = attPrevTfBaseMapper.flashFloodDetails(nvi, avi);
        if (attPrevTfBase != null) {
            avi = attPrevTfBase.getAvi();
            tho = attPrevTfBase.getTho() + "";
            tpo = attPrevTfBase.getTpo() + "";
            chargerPhone = attPrevTfBase.getChargerPhone();
            chargerName = attPrevTfBase.getChargerName();
            stationName = attPrevTfBase.getStationName();
            stationCode = attPrevTfBase.getStationCode();
        }
        List<FlashFloodDetailsResVo> flashFloodDetailsResVos = resultsWarnIndicatorService.findFlashFloodDetails(nvi, avi);
        if (!flashFloodDetailsResVos.isEmpty() && ygkj.com.util.StringUtils.isNotBlank(stationCode)) {
            endTime = endTime == null ? new Date() : endTime;
            String tm = DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss");
            for (FlashFloodDetailsResVo flashFloodDetailsResVo : flashFloodDetailsResVos) {
                BigDecimal period = flashFloodDetailsResVo.getPeriod();
                Date nextDay = null;
                double v = period.setScale(1, BigDecimal.ROUND_UP).doubleValue();
                nextDay = getDate(tm, nextDay, v);
                String startTime = null;
                if (startTimeGate != null && nextDay.before(startTimeGate)) {
                    startTime = DateUtil.format(startTimeGate, "yyyy-MM-dd HH:mm:ss");
                } else {
                    startTime = DateUtil.format(nextDay, "yyyy-MM-dd HH:mm:ss");
                }
                String[] tableNames = DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(), startTime, tm);

                List<StPptnR> stPptnRList = Collections.emptyList();
                if (tableNames.length > 0) {
                    stPptnRList = attPrevTfBaseMapper.selectHourRainfall(Arrays.asList(tableNames), startTime, tm, stationCode);
                }
                if (!stPptnRList.isEmpty()) {
                    Double drp = stPptnRList.stream().mapToDouble(e -> e.getDrp()).sum();
                    flashFloodDetailsResVo.setRealTimeRainfall(drp + "");
                }
            }

//            Double realTimeRainfall = resultsWarnIndicatorService.findDataRainNewData(stationCode, StationType.RAINFALL.table() + DateUtil.format(endTime, "yyyyMM"));
//            flashFloodDetailsResVos.forEach(e -> e.setRealTimeRainfall(realTimeRainfall + ""));
        }
        result.put("nvi", nvi);
        result.put("tho", tho);
        result.put("tpo", tpo);
        result.put("chargerPhone", chargerPhone);
        result.put("chargerName", chargerName);
        result.put("stationName", stationName);
        result.put("list", flashFloodDetailsResVos);
        return result;
    }

    /**
     * 根据时间算出山洪报警情况，返回村落list
     *
     * @param endTime
     * @return
     */
    public synchronized List<AttPrevTfBase> getAttPrevTfBase(String endTime) {

        List<AttPrevTfBase> attPrevTfBases = attPrevTfBaseMapper.selectList(null);//村落
        List<MountainFloodWarnData> mountainFloodWarnDataList = mountainFloodWarnDataMapper.selectPrevTfData(endTime);
        List<ResultsWarnIndicator> resultsWarnIndicatorList = resultsWarnIndicatorService.selectList(null);//指标
        List<AttPrevTfBase> attPrevTfBasesCp = new ArrayList<>();

        for (AttPrevTfBase attPrevTfBase : attPrevTfBases) {
            String nvi = attPrevTfBase.getNvi();
            String avi = attPrevTfBase.getAvi();

            String status = floodWarnStatus.get(attPrevTfBase.getLat() + "_" + attPrevTfBase.getLon());
            if ("1".equals(status)) {////0:准备转移 1:立即转移   立即转移直接返回
                List<AttPrevTfBase> collect = attPrevTfBasesAll.stream().filter(e -> e.getLon().equals(attPrevTfBase.getLon())
                        && e.getLat().equals(attPrevTfBase.getLat()) && "5".equals(e.getWarnGradeID())).collect(Collectors.toList());
                if (collect.size() > 0) {
                    attPrevTfBasesCp.add(collect.get(0));
                }
                continue;
            } else if ("0".equals(status)) {
                List<AttPrevTfBase> collect = attPrevTfBasesAll.stream().filter(e -> e.getLon().equals(attPrevTfBase.getLon()) &&
                        e.getLat().equals(attPrevTfBase.getLat()) && "4".equals(e.getWarnGradeID())).collect(Collectors.toList());
                if (collect.size() > 0) {
                    attPrevTfBasesCp.add(collect.get(0));
                }
            }

            String stationCode = attPrevTfBase.getStationCode();

            List<MountainFloodWarnData> collect1 = mountainFloodWarnDataList.stream().filter(k -> stationCode.equals(k.getStationCode())).collect(Collectors.toList());
            if (collect1.isEmpty()) {
                continue;
            }

            List<ResultsWarnIndicator> collect = resultsWarnIndicatorList.stream()
                    .filter(e -> nvi.equals(e.getNvi()) && avi.equals(e.getAvi())).collect(Collectors.toList());

            for (ResultsWarnIndicator resultsWarnIndicator : collect) {
                BigDecimal readyMove = resultsWarnIndicator.getReadyMove();//准备转移
                BigDecimal immediateTransfer = resultsWarnIndicator.getImmediateTransfer();//立即转移
                List<MountainFloodWarnData> collect2 = collect1.stream().filter(e ->
                        e.getPeriod().equals(resultsWarnIndicator.getPeriod())).collect(Collectors.toList());
                if (!collect2.isEmpty()) {
                    MountainFloodWarnData mountainFloodWarnData = collect2.get(0);
                    String tm = mountainFloodWarnData.getTm().substring(0, 19);
                    BigDecimal period = mountainFloodWarnData.getPeriod();

                    Date nextDay = null;
                    double v = period.setScale(1, BigDecimal.ROUND_UP).doubleValue();
                    nextDay = getDate(tm, nextDay, v);

                    BigDecimal data = mountainFloodWarnData.getData();

                    if (immediateTransfer.doubleValue() < data.doubleValue()) {
                        double thresholdData = data.subtract(readyMove).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        attPrevTfBase.setThresholdData(thresholdData);
                        attPrevTfBase.setThresholdTime(DateUtil.format(nextDay, "yyyy-MM-dd HH:mm:ss") + "至" + tm);
                        if (attPrevTfBasesCp.stream().filter(e -> e.getLon().equals(attPrevTfBase.getLon())
                                && e.getLat().equals(attPrevTfBase.getLat())).count() == 0) {
                            attPrevTfBase.setWarnGradeID("5");//5 立即转移 4 准备转移
                            attPrevTfBasesAll.add(attPrevTfBase);
                            attPrevTfBasesCp.add(attPrevTfBase);
                        } else {
                            AttPrevTfBase attPrevTfBase1 = attPrevTfBasesCp.stream().filter(e -> e.getLon().equals(attPrevTfBase.getLon())
                                    && e.getLat().equals(attPrevTfBase.getLat())).collect(Collectors.toList()).get(0);
                            attPrevTfBase1.setWarnGradeID("5");
                        }
                        floodWarnStatus.put(attPrevTfBase.getLat() + "_" + attPrevTfBase.getLon(), "1");//键值用经纬度保证唯一性
                        break;
                    }
                    if (readyMove.doubleValue() < data.doubleValue()) {
                        double thresholdData = data.subtract(readyMove).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        attPrevTfBase.setThresholdData(thresholdData);
                        attPrevTfBase.setThresholdTime(DateUtil.format(nextDay, "yyyy-MM-dd HH:mm:ss") + "至" + tm);
                        floodWarnStatus.put(attPrevTfBase.getLat() + "_" + attPrevTfBase.getLon(), "0");//键值用经纬度保证唯一性
                        if (attPrevTfBasesCp.stream().filter(e -> e.getLon().equals(attPrevTfBase.getLon()) && e.getLat().equals(attPrevTfBase.getLat())).count() == 0) {
                            attPrevTfBase.setWarnGradeID("4");//5 立即转移 4 准备转移
                            attPrevTfBasesAll.add(attPrevTfBase);
                            attPrevTfBasesCp.add(attPrevTfBase);
                        }
                        continue;
                    }
                }
            }
        }
        attPrevTfBasesCp = attPrevTfBasesCp.stream().collect(
                collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getLat() + ";" + o.getLon()))), ArrayList::new)
        );
        return attPrevTfBasesCp;
    }

    /**
     * 更新降雨
     *
     * @return
     */
    @Deprecated
    @Override
    public Object updateDrillRainContourGisImg(DrillRainContourGisImgReqVo reqVo) {
        /**
         * 1. 先根据入参找到，那条commonresult
         * 2. 将commonresult json 解析，并上传文件，到七牛云，获取到url
         * 3. 将url拼入commonresult 更新记录
         */
        return CommonResult.success();
    }

    /**
     * 海塘演练
     *
     * @param reqVo
     * @return
     */
    @Override
    public CommonResult seawallWarnStatistic(SeawallWarnStatisticReqVo reqVo) {
        return (CommonResult) seawallManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "seawallWarnStatistic")
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .build());
    }

    /**
     * 潮位报警统计
     *
     * @return
     */
    @Override
    public JSONObject tideWarnStatistic() {
        return (JSONObject) tideManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "tideWarnStatistic")
                .build());
    }

    /**
     * 山洪演练
     *
     * @return
     */
    @Override
    public MountainTorrentDrillResVo mountainTorrentDrill(MountainTorrentDrillReqVo reqVo) {

        /**
         * 1. 查出所有的村庄的报警情况
         * 2. 统计报警
         * 3. 将报警和落点分组。
         */
        MountainTorrentDrillResVo result = new MountainTorrentDrillResVo();
        List<MountainTorrentDetailResVo> allVillages = mountainTorrentManager.mountainTorrentWarningInfo(reqVo);
        Map<String, List<MountainTorrentDetailResVo>> groupMap = allVillages.stream().collect(Collectors.groupingBy(o -> StringUtils.isBlank(o.getWarnGradeID()) ? "norm" : o.getWarnGradeID()));
        List<MountainTorrentDetailResVo> normVillages = groupMap.getOrDefault("norm", Collections.emptyList());
        List<MountainTorrentDetailResVo> grad5Villages = groupMap.getOrDefault("5", Collections.emptyList());
        List<MountainTorrentDetailResVo> grad4Villages = groupMap.getOrDefault("4", Collections.emptyList());
        List<MountainTorrentDetailResVo> warnVillages = new ArrayList<>(grad4Villages.size() + grad5Villages.size());
        warnVillages.addAll(grad5Villages);
        warnVillages.addAll(grad4Villages);

        // 统计山洪报警数据
        MountainTorrentWarnStatsResVo warnStats = new MountainTorrentWarnStatsResVo();
        Integer grade4County = 0;   //准备转移区县
        Integer grade4villages = 0; //准备转移村落
        Integer grade4street = 0;   //准备转移乡镇
        Integer grade4Person = 0;   //准备转移人口

        Integer grade5County = 0;    //立即转移区县
        Integer grade5villages = 0;  //立即转移村落
        Integer grade5street = 0;    //立即转移乡镇
        Integer grade5Person = 0;    //立即转移人口
        if (!warnVillages.isEmpty()) {
            List<ResultsWarnVo> resultsWarnVos = new ArrayList<>();
            ResultsWarnVo resultsWarnVo = null;
            Map<String, List<AttPrevTfBase>> townGroupMap = warnVillages.stream().collect(Collectors.groupingBy(AttPrevTfBase::getTown));
            for (Map.Entry<String, List<AttPrevTfBase>> townGroupMapEntry : townGroupMap.entrySet()) {
                resultsWarnVo = new ResultsWarnVo();
                String key = townGroupMapEntry.getKey();
                List<AttPrevTfBase> value = townGroupMapEntry.getValue();
                List<Villages> villagesList = new ArrayList<>();
                Villages villages = null;
                for (AttPrevTfBase attPrevTfBase : value) {
                    List<AttPrevTfBase> collect = warnVillages.stream().filter(e -> e.getLon()
                            .equals(attPrevTfBase.getLon()) && e.getLat().equals(attPrevTfBase.getLat())).collect(Collectors.toList());
                    if (!collect.isEmpty()) {
                        AttPrevTfBase attPrevTfBase1 = collect.get(0);
                        Integer tpo = attPrevTfBase1.getTpo();
                        if ("5".equals(attPrevTfBase.getWarnGradeID())) {
                            grade5County++;
                            grade5villages++;
                            grade5street++;
                            grade5Person += tpo;
                        } else if ("4".equals(attPrevTfBase.getWarnGradeID())) {
                            grade4County++;
                            grade4villages++;
                            grade4street++;
                            grade4Person += tpo;
                        }
                        villages = new Villages();
                        villages.setThresholdTime(attPrevTfBase.getThresholdTime());
                        villages.setThresholdData(attPrevTfBase.getThresholdData());
                        villages.setAdnm(attPrevTfBase.getAvi());
                        villages.setLat(attPrevTfBase.getLat() + "");
                        villages.setLng(attPrevTfBase.getLon() + "");
                        villages.setWarnGradeID(attPrevTfBase.getWarnGradeID());
                        villages.setEffectPersonNumber(attPrevTfBase.getTpo());
                        villagesList.add(villages);
                    }
                }
                resultsWarnVo.setVillages(villagesList);
                resultsWarnVo.setAdnm(key);
                resultsWarnVos.add(resultsWarnVo);
            }
            warnStats.setTown(resultsWarnVos);
            warnStats.setPromptlyMoveVillage(StringUtils.EMPTY);
            warnStats.setEffectPeople(StringUtils.EMPTY);
            warnStats.setWarningTown(StringUtils.EMPTY);
            warnStats.setWarningMoveVillage(StringUtils.EMPTY);
            warnStats.setRows(null);
        }
        warnStats.setGrade5County(grade5County);
        warnStats.setGrade5villages(grade5villages);
        warnStats.setGrade5street(grade5street);
        warnStats.setGrade5Person(grade5Person);

        warnStats.setGrade4County(grade4County);
        warnStats.setGrade4villages(grade4villages);
        warnStats.setGrade4street(grade4street);
        warnStats.setGrade4Person(grade4Person);
        warnStats.setGradevillages(grade5villages + grade4villages);
        //2021.06.19 发送山洪预警短信 huang.zh
        if (CollectionUtils.isNotBlank(warnStats.getTown())) {
//            List<MessageRecord> records = warningMessageManager.submit(MessageTypes.MOUNTAIN_FLOOD.type(), JSONArray.parseArray(JSONArray.toJSONString(warnStats.getTown(), SerializerFeature.WriteMapNullValue)));
//            messageRecordMapper.batchInsert(records);
        }
        // 正常落点
        result.setNormList(normVillages);
        // 报警村庄
        result.setWarnList(warnVillages);
        result.setWarnNum(warnVillages.size());
        result.setWarnStats(warnStats);

        return result;
    }

    /**
     * 积水点组件C位统计
     *
     * @param dataTime
     * @return
     */
    @Override
    public CommonResult floodPointCWindows(Date dataTime) {
        return (CommonResult) waterLoggingManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "floodPointCWindows")
                .with(UnitDataReqVo::putParam, "dataTime", dataTime)
                .build());
    }

    /**
     * 积水统计
     *
     * @param statsType 1. 按行政区划统计，2. 按是否有积水统计，3 统计积水点数量
     * @return
     */
    @Override
    public CommonResult waterLoggingStats(Integer statsType) {
        return (CommonResult) waterLoggingManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "waterLoggingStats")
                .with(UnitDataReqVo::putParam, "statsType", statsType)
                .build());
    }

    /**
     * 积水列表和落点
     *
     * @param areaCode
     * @param ponding
     * @param orderColumn 排序column列名
     * @param ascOrDesc   升序降序,false asc/true desc
     * @return
     */
    @Override
    public CommonResult waterLoggingList(String areaCode, Boolean ponding, String name, String orderColumn, Boolean ascOrDesc) {
        return (CommonResult) waterLoggingManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "waterLoggingList")
                .with(UnitDataReqVo::putParam, "reqVo", Builder.of(FloodPointReqVo::new)
                        .with(FloodPointReqVo::setAreaCode, areaCode)
                        .with(FloodPointReqVo::setPonding, ponding)
                        .with(FloodPointReqVo::setName, name)
                        .with(FloodPointReqVo::setOrderColumn, orderColumn)
                        .with(FloodPointReqVo::setAscOrDesc, ascOrDesc)
                        .build())
                .build());
    }

    /**
     * 流量组件统计、列表、落点
     *
     * @return
     */
    @Override
    public CommonResult waterFlowList() {
        return (CommonResult) waterFlowManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "waterFlowList")
                .build());
    }

    /**
     * 流量组件统计
     *
     * @return
     */
    @Override
    public CommonResult waterFlowStats() {
        return (CommonResult) waterFlowManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "waterFlowStats")
                .build());
    }

    /**
     * 台风组件C位统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public CommonResult typhoonStats4C(CurTyphoonReqVo reqVo) {
        return (CommonResult) typhoonManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::putParam, "CurTyphoonReqVo", reqVo)
                .with(UnitDataReqVo::setStrategy, "typhoonStats4C")
                .build());
    }

    @Override
    public CommonResult listCamera(HikCameraQueryReqVo reqVo) {
        return (CommonResult) cameraManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .with(UnitDataReqVo::setStrategy, "listCamera")
                .build());
    }

    @Override
    public CommonResult listTeam(AttTeamBasePageQueryVo reqVo) {
        return (CommonResult) rescueSupportManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .with(UnitDataReqVo::setStrategy, "listTeam")
                .build());
    }

    @Override
    public CommonResult listWarehouses(AttWarehousePageQueryVo reqVo) {
        return (CommonResult) rescueSupportManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::putParam, "reqVo", reqVo)
                .with(UnitDataReqVo::setStrategy, "listWarehouses")
                .build());
    }

    @Override
    public List<Villages> flashFloodWarnList(String endTime, Boolean currentFlag, String drillAffairId, String floodType) {
        try {
            JSONObject jo = floodWarning4NewVersion(endTime, currentFlag, drillAffairId);
            List<ResultsWarnVo> resultsWarnVoList = JSONObject.parseArray(jo.getJSONObject("data").getString("town"), ResultsWarnVo.class);
            if (!resultsWarnVoList.isEmpty()) {
                return Collections.emptyList();
            }
            List<Villages> villages = new ArrayList<>();
            for (ResultsWarnVo resultsWarnVo : resultsWarnVoList) {
                List<Villages> villages1 = resultsWarnVo.getVillages();
                if (!villages1.isEmpty()) {
                    for (Villages villages2 : villages1) {
                        handleDes(villages2);
                    }
                    villages.addAll(villages1);
                }
            }
            return villages.stream().filter(e -> floodType.equals(e.getWarnGradeID())).collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public JSONObject digitalShanXiOverview(String reservoirName) {
        JSONObject joResult = new JSONObject();

        RsvrStationWaterReqVo reqVo = new RsvrStationWaterReqVo();
        reqVo.setEngScales("大中型");
        JSONObject rsvrJo = rsvrProjList(reqVo);

        //水位、库容数据查找
        List<RsvrStationWaterResVo> rsvrStationWaterResVoList = (List<RsvrStationWaterResVo>) rsvrJo.get("list");
        List<RsvrStationWaterResVo> stationWaterResVos = rsvrStationWaterResVoList.stream().filter(e ->
                reservoirName.equals(e.getResName())).collect(Collectors.toList());
        if (!stationWaterResVos.isEmpty()) {
            RsvrStationWaterResVo rsvrStationWaterResVo = stationWaterResVos.get(0);
            joResult.put("waterLevel", rsvrStationWaterResVo.getWaterLevel());
            joResult.put("waterLevelCapacity", rsvrStationWaterResVo.getWaterLevelCapacity());
        } else {
            joResult.put("waterLevel", "-");
            joResult.put("waterLevelCapacity", "-");
        }

        //日面雨量数据查找
        CommonResult commonResult = grandTotalReservoirRainfall(null, 24, null);
        long code = commonResult.getCode();
        if (code == CommonResult.success().getCode()) {
            ArrayList list = (ArrayList) commonResult.getData();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                String key = (String) map.get("key");
                Double value = (Double) map.get("value");
                if (reservoirName.equals(key)) {
                    joResult.put("surfaceWaterLevel", new BigDecimal(value + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    break;
                }
            }
        }
        if (joResult.get("surfaceWaterLevel") == null) {
            joResult.put("surfaceWaterLevel", "-");
        }

        // 查找实时入库流量  珊溪水库:330328028000154 赵山渡水库:330381021000005
        String resCode = "";
        if ("珊溪水库".equals(reservoirName)) {
            resCode = "330328028000154";
        } else if ("赵山渡水库".equals(reservoirName)) {
            resCode = "330381021000005";
        }
        Double synFlow = waterAndRainMapper.findLastRsvrFloodData(resCode);
        if (synFlow == null) {
            joResult.put("synFlow", "-");
        } else {
            joResult.put("synFlow", synFlow);
        }
        return joResult;
    }

    //

    /**
     * 预报时间、预报降雨量、超预警指标解析
     * 例子 未来3小时（22日10时-13时）降雨56mm,超预警指标4mm,发生山洪灾害的可能性较大。
     *
     * @param villages
     */
    private void handleDes(Villages villages) {
        //序号、村庄名称、预警时间、预报时间、预报降雨量、超预警指标
        try {
            String desc = villages.getDesc();
            String forecastTime = desc.substring(0, desc.indexOf("降雨"));//预报时间
            villages.setForecastTime(forecastTime);
            String forecastRainfall = desc.substring(desc.indexOf("降雨") + 2, desc.indexOf("mm,超预警指标"));//预报降雨量
            villages.setForecastRainfall(forecastRainfall);
            String overWarnIndex = desc.substring(desc.indexOf(",超预警指标") + 6, desc.indexOf("mm,发生山洪"));//超预警指标
            villages.setOverWarnIndex(overWarnIndex);
            villages.setWarnTime(DateUtils.format(new Date(), "yyyy年MM月dd日HH时"));
        } catch (Exception e) {

        }
    }
}
