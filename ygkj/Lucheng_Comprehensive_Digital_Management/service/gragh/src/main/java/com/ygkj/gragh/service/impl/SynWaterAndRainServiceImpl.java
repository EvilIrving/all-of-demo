package com.ygkj.gragh.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.gragh.enums.RainfallGridStatisticType;
import com.ygkj.gragh.enums.RainfallSurfaceType;
import com.ygkj.gragh.enums.StationType;
import com.ygkj.gragh.enums.WaterStationType;
import com.ygkj.gragh.feign.DataManagementClient;
import com.ygkj.gragh.feign.HydrologyClient;
import com.ygkj.gragh.feign.ProjectManageClient;
import com.ygkj.gragh.mapper.*;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.service.SynWaterAndRainService;
import com.ygkj.gragh.utils.RainSurfaceUtil;
import com.ygkj.gragh.utils.TableCandidateUtil;
import com.ygkj.gragh.vo.request.*;
import com.ygkj.gragh.vo.response.*;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.SectionStorm;
import com.ygkj.service.RsvrComputeService;
import com.ygkj.utils.*;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SynWaterAndRainServiceImpl implements SynWaterAndRainService {

    @Autowired
    private AttStBaseMapper stationMapper;
    @Autowired
    private AttProjBaseMapper projectMapper;
    @Autowired
    private SynRainAndWaterMapper rainAndWaterMapper;
    @Autowired
    private UserCollectionMapper userCollectionMapper;
    @Autowired
    private AttAdcdBaseMapper adcdBaseMapper;
    @Autowired
    private HydrologyClient hydrologyClient;
    @Autowired
    private DataManagementClient dataManagementClient;
    @Autowired
    private PrecedeAffectRainfallMapper precedeAffectRainfallMapper;
    @Autowired
    private ProjectManageClient projectManageClient;

    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");

    private DateTimeFormatter standardDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static long FOURTY_DAYS_MILLIS = 14 * 24 * 3600 * 1000;

    private static boolean isClip = true;

    private int[] size = new int[]{100, 100};

    private static URL fileUrl;

    /**
     * 永嘉测站前缀
     */
    private final static String yjStcdPrefix = "YJ_";
    private final static String yjRsvrTable = "st_rsvr_r_yongjia";

    private ExecutorService executor = new ThreadPoolExecutor(8, 512, 60, TimeUnit.SECONDS
            , new ArrayBlockingQueue<>(256));

    private ExecutorService rainfallStatisticExecutor = new ThreadPoolExecutor(16, 1024, 60, TimeUnit.SECONDS
            , new ArrayBlockingQueue<>(64));

    @Override
    public List<AttStBase> stations(SynWaterRequestVo requestVo) {
        requestVo.checkEngScal();
//        List<AttProjBase> projects = projectMapper.selectByCondition(requestVo);
//        String projectCodes = String.join(",", projects.stream().map(AttProjBase::getProjCode)
//                .collect(Collectors.toList()));
        List<AttStBase> stations = new ArrayList<>();
        StationRequestVo stationRequestVo = new StationRequestVo();
        stationRequestVo.setStationName(requestVo.getStationName());
        if (requestVo.getStationType().equals(StationType.RAINFALL.type())) {
            stationRequestVo.setIsRainStation(Boolean.TRUE);
        } else {
            stationRequestVo.setStationType(requestVo.getStationType());
        }
        if (StringUtils.isNotBlank(requestVo.getAreaCode())) {
            stationRequestVo.setAreaCode(requestVo.getAreaCode().startsWith("330") ? requestVo.getAreaCode() :
                    adcdBaseMapper.getWenzhouAreaCodeByAdnm(requestVo.getAreaCode()));
        }
        stationRequestVo.setResGrade(requestVo.getResGrade());
        stationRequestVo.setBas(requestVo.getBasinCode());
        stationRequestVo.setStationCodes(requestVo.getStCodes());
        stationRequestVo.setIsFocus(requestVo.getIsFocus());
        // 2021.06.06 新增是否只查询温瑞平水闸的逻辑
        stationRequestVo.setIsWrpWaga(requestVo.getIsWrp());
        stationRequestVo.setDisplay(Boolean.TRUE);
        if (StringUtils.isNotBlank(requestVo.getEngScal())) {
            stationRequestVo.setEngScal(EngScalEnum.getByDesc(requestVo.getEngScal()).getEngScal());
        }
        stationRequestVo.setRiverStationType(requestVo.getRiverStationType());
        // 2021.08.02 新增水库筛选项，设置是否绑定工程
        stationRequestVo.setBindProj(requestVo.getBindProj());
        stations = stationMapper.selectByCondition(stationRequestVo);
        Future<Set<String>> future = executor.submit(new StationOfflineStatus());
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH) + 1;
        String table;
        String stationType = requestVo.getStationType();
        String type = "";
        switch (stationType) {
            case "R3":
            case "R2":
            case "RR":
                type = "水位";
                table = StationType.RESERVOIR.table();
                break;
            case "ZZ":
                type = "水位";
                table = StationType.RIVER.table();
                break;
            case "PP":
                type = "雨量";
                table = StationType.RAINFALL.table();
                break;
            case "DD":
                table = StationType.GATE.table();
                break;
            case "TT":
                table = StationType.TIDE.table();
                break;
//            case "PT":
//                table = StationType.PUST.table();
//                break;
            default:
                table = StationType.RIVER.table();
                break;
        }
//        String zero = month < 10 ? "0" : "";
//        table = table.concat("" + year + zero + month);
        table = table.concat(requestVo.getDataTime() == null ? LocalDate.now().format(yyyyMM) : DateUtils.date2LocalDate(requestVo.getDataTime()).format(yyyyMM));
        if (stationType.equals(StationType.RESERVOIR.type())) {
            //水库水位站
//            List<StRsvrR> resLevel = rainAndWaterMapper.selectNewestReserviorWaterLevelFromTable(table);
            List<String> codes;
            List<AttResBase> rsvrList = null;
            if (requestVo.getMain() == null || !requestVo.getMain()) {
                codes = stations.stream()
                        .map(AttStBase::getStCode).collect(Collectors.toList());
            } else {
                rsvrList = projectMapper.selectReservoirs(stationRequestVo.getEngScal(), null, null);
                rsvrList = rsvrList.stream().filter(o -> StringUtils.isNotBlank(o.getMainStcd())).collect(Collectors.toList());
                codes = rsvrList.stream().map(AttResBase::getMainStcd).collect(Collectors.toList());
            }
            // 2021.05.13 添加查询某个时间点数据逻辑
//            List<StRsvrR> resLevel = rainAndWaterMapper.selectRsvrWaterLevelByTimeCode(table,
//                    codes.stream().collect(Collectors.toList()), requestVo.getDataTime());
            // 2021.07.28 添加永嘉数据查询逻辑
            List<String> disYjstcds = codes.stream().collect(Collectors.toList());
//            List<String> yjstcds = codes.stream().filter(o -> o.startsWith(yjStcdPrefix)).collect(Collectors.toList());
            List<StRsvrR> resLevel = Collections.emptyList();
            if (CollectionUtils.isNotBlank(codes)) {
                resLevel = rainAndWaterMapper.selectRsvrWaterLevelByTimeCode(table,
                        codes.stream().collect(Collectors.toList()), requestVo.getDataTime());
            }
//            if (CollectionUtils.isNotBlank(yjstcds)) {
//                yjwaterLevel = rainAndWaterMapper.selectYjRsvrWaterLevelByTime(requestVo.getDataTime());
//            }
//            List<StRsvrR> resLevel = new ArrayList<>(disYjwaterLevel.size() + yjwaterLevel.size());
//            resLevel.addAll(disYjwaterLevel);
            Map<String, List<StRsvrR>> map = resLevel.stream().collect(Collectors.groupingBy(StRsvrR::getMgstcd));
            Iterator<AttStBase> iterator = stations.iterator();
            Map<String, List<AttResBase>> stcdRsvrMap = Collections.emptyMap();
            Map<String, List<AttResBase>> rscdRsvrMap = Collections.emptyMap();
            // 2021.08.02 新增 将工程特征特征水位赋值给测站
            if (requestVo.isProjFeatureLevel()) {
                if (rsvrList == null) {
                    // 根据主测站，查询水库数据
                    if (CollectionUtils.isNotBlank(codes)) {
                        List<AttResBase> tempList1 = projectMapper.selectReservoirs(null, null, String.join(",", codes));
                        tempList1 = tempList1 == null ? Collections.emptyList() : tempList1;
                        stcdRsvrMap = tempList1.stream().collect(Collectors.groupingBy(AttResBase::getMainStcd));
                        List<String> leftCodes = CollectionUtils.getExcept(codes, new ArrayList<>(stcdRsvrMap.keySet()));
                        List<String> mainStcds = tempList1.stream().map(AttResBase::getMainStcd).collect(Collectors.toList());
                        if (CollectionUtils.isNotBlank(leftCodes)) {
                            List<AttResBase> tempList2 = projectMapper.selectReservoirs(null, stations.stream()
                                    .filter(o -> StringUtils.isNotBlank(o.getProjCode()))
                                    .filter(o -> leftCodes.contains(o.getStCode()))
                                    .map(AttStBase::getProjCode)
                                    .collect(Collectors.joining(",")), null);
                            tempList2 = tempList2 == null ? Collections.emptyList() : tempList2;
                            rscdRsvrMap = tempList2.stream().collect(Collectors.groupingBy(AttResBase::getResCode));
                        }
                    }
                } else {
                    stcdRsvrMap = rsvrList.stream().collect(Collectors.groupingBy(AttResBase::getMainStcd));
                }
            }
            LocalDate now = LocalDate.now();
            while (iterator.hasNext()) {
                AttStBase station = iterator.next();
                // 2021.08.02 新增 将工程特征特征水位赋值给测站
                if (requestVo.isProjFeatureLevel()) {
                    List<AttResBase> tempRsvrList = null;
                    if (CollectionUtils.isBlank(tempRsvrList = stcdRsvrMap.get(station.getStCode()))) {
                        tempRsvrList = rscdRsvrMap.get(station.getProjCode());
                    }
//                    station.setLimitLevel(null);
//                    station.setFloodLevel(null);
                    if (CollectionUtils.isNotBlank(tempRsvrList)) {
                        for (AttResBase tempRsvr : tempRsvrList) {
                            station.setLimitLevel(getRsvrLimitLevel(tempRsvr, now));
                            if (tempRsvr.getUppLevFlco() != null) {
                                station.setFloodLevel(tempRsvr.getUppLevFlco().doubleValue());
                            }
                            if (station.getLimitLevel() != null && station.getFloodLevel() != null &&
                                    station.getLimitLevel() > 0 && station.getFloodLevel() > 0) {
                                break;
                            }
                        }
                    }
                }
                String stationCode = station.getStCode();
                List<StRsvrR> list = map.get(stationCode);
                if (list != null) {
                    double waterLevel = list.get(0).getRz().doubleValue();
                    station.setWaterLevel(waterLevel);
                    station.setTime(list.get(0).getTm());
                    String status = "";
                    if (null == station.getLimitLevel() || 0d == station.getLimitLevel()) {
                        status = WaterStationType.NORMAL_RIVER.type();
                    } else {
                        if (waterLevel > station.getLimitLevel()) {
                            station.setWarning(true);
                            station.setOverWarnLevel(station.getWaterLevel() - station.getLimitLevel());
                            status = WaterStationType.SUPER_LIMIT.type();
                        } else {
                            status = WaterStationType.NORMAL_RIVER.type();
                        }
                    }
                    station.setStatus(status);
                } else {
                    iterator.remove();
                }
            }
            stations.sort(Comparator.comparingDouble(AttStBase::getWaterLevel).reversed());
        } else if (stationType.equals(StationType.RIVER.type())) {
            //河道水位站
//            List<StRiverR> riverLevel = rainAndWaterMapper.selectNewestRiverLevelFromTable(table);
            // 2021.05.13 添加查询某个时间点数据逻辑
            List<StRiverR> riverLevel = rainAndWaterMapper.selectRiverByTimeCode(table, stations.stream().map(AttStBase::getStCode).collect(Collectors.toList()), requestVo.getDataTime());
            Map<String, List<StRiverR>> map = riverLevel.stream().collect(Collectors.groupingBy(StRiverR::getStcd));
            for (AttStBase station : stations) {
                String stationCode = station.getStCode();
                List<StRiverR> list = map.get(stationCode);
                if (list != null) {
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
                }
            }
            stations.sort(Comparator.comparingDouble(AttStBase::getWaterLevel).reversed());
        } else if (stationType.equals(StationType.RAINFALL.type())) {
            //雨量站
            List<StPptnR> list = rainAndWaterMapper.selectNewestStationRainfall(table);
            Map<String, List<StPptnR>> map = list.stream().collect(Collectors.groupingBy(StPptnR::getStcd));
            for (AttStBase station : stations) {
                String stationCode = station.getStCode();
                List<StPptnR> rain = map.get(stationCode);
                if (rain != null) {
                    double rainfall = rain.get(0).getDrp().doubleValue();
                    station.setTime(list.get(0).getTm());
                    station.setRainfall(rainfall);
                }
            }
        } else if (stationType.equals(StationType.GATE.type())) {
            //不要状元水闸
            stations = stations.stream().filter(o->!"9".equals(o.getStCode())).collect(Collectors.toList());
            //堰闸
//            List<StWasR> wasList = rainAndWaterMapper.selectNewestWas(table);
            // 2021.05.13 添加查询某个时间点数据逻辑
            List<StWasR> wasList = rainAndWaterMapper.selectWasByTimeCode(table, stations.stream().map(AttStBase::getStCode).collect(Collectors.toList()), requestVo.getDataTime());
            //将部分河道水位、潮位测站数据作为某些堰闸水闸的闸上水位、闸下水位
//            String riverTable=StationType.RIVER.table().concat(requestVo.getDataTime() == null ? LocalDate.now().format(yyyyMM) : DateUtils.date2LocalDate(requestVo.getDataTime()).format(yyyyMM));
//            List<StRiverR> riverLevel = rainAndWaterMapper.selectRiverByTimeCode(riverTable,stations.stream().map(AttStBase::getStCode).collect(Collectors.toList()), requestVo.getDataTime());
//            String tideTable=StationType.TIDE.table().concat(requestVo.getDataTime() == null ? LocalDate.now().format(yyyyMM) : DateUtils.date2LocalDate(requestVo.getDataTime()).format(yyyyMM));
//            List<StTideR> tideList = rainAndWaterMapper.selectTideByTimeCode(tideTable, stations.stream().map(AttStBase::getStCode).collect(Collectors.toList()), requestVo.getDataTime());
//            List<StUpzDwz> list=stationMapper.selectAll();
            Map<String, List<StWasR>> map = wasList.stream().collect(Collectors.groupingBy(StWasR::getStcd));
//            Map<String, List<StRiverR>> map1 = riverLevel.stream().collect(Collectors.groupingBy(StRiverR::getStcd));
//            Map<String, List<StTideR>> map2 = tideList.stream().collect(Collectors.groupingBy(StTideR::getStcd));
            List<String> sixstCodes=Arrays.asList("2021101101","2021101102","2021101103","2021101104","2021101105","2021101106");
            for (AttStBase station : stations) {
                String stationCode = station.getStCode();
                List<StWasR> was = map.get(stationCode);
                if (was != null) {
                    StWasR stWasR = was.get(0);
                    station.setUpz(stWasR.getUpz() == null ? 0d : stWasR.getUpz().doubleValue());
                    station.setDwz(stWasR.getDwz() == null ? 0d : stWasR.getDwz().doubleValue());
                    //卧旗水闸数据有问题，暂时先设置成-
                    if("1".equals(stWasR.getStcd())){
                        stWasR.setOvs("-");
                    }
                    if(sixstCodes.contains(stWasR.getStcd())&&stWasR.getOvs() != null){
                        stWasR.setOvs(String.valueOf(new BigDecimal(Double.valueOf(stWasR.getOvs())/100).setScale(2,BigDecimal.ROUND_HALF_UP)));
                    }
                    station.setOpenGate(stWasR.getOvs() == null ? "0" : stWasR.getOvs());
                    station.setFlow(stWasR.getMxgtq() == null ? "0" : stWasR.getMxgtq());
                    station.setTime(stWasR.getTm());
                    station.setLl(stWasR.getLl());
                    // 水闸是闸上水位超警
                    String status = StringUtils.EMPTY;
                    if (station.getWarningLevel() != null
                            && station.getWarningLevel() > 0d
                            && station.getUpz() > station.getWarningLevel()) {
                        station.setOverWarnLevel(station.getUpz() - station.getWarningLevel());
                        station.setWarning(true);
                    }
                    station.setStatus(status);
                }
            }
//            for(AttStBase station : stations){
//                for (StUpzDwz stUpzDwz : list) {
//                    if(stUpzDwz.getStCode()!=null&&station.getStCode().equals(stUpzDwz.getStCode())){
//                       if(map1.size()>0&&map1.containsKey(stUpzDwz.getUpzCode())){
//                           station.setUpz(map1.get(stUpzDwz.getUpzCode()).get(0).getZ());
//                       }
//                       if(map1.size()>0&&map1.containsKey(stUpzDwz.getDwzCode())){
//                           station.setDwz(map1.get(stUpzDwz.getDwzCode()).get(0).getZ());
//                       }
//                       if(map2.size()>0&&map2.containsKey(stUpzDwz.getUpzCode())){
//                           station.setUpz(map2.get(stUpzDwz.getUpzCode()).get(0).getTdz());
//                       }
//                       if(map2.size()>0&&map2.containsKey(stUpzDwz.getDwzCode())){
//                           station.setDwz(map2.get(stUpzDwz.getDwzCode()).get(0).getTdz());
//                       }
//                    }
//
//                }
//
//            }
            if (requestVo.getOpenGate() != null && requestVo.getOpenGate()) {
                //只查询已开启的闸门
                stations = stations.stream().filter(station -> HydrologyUtils.isWagaOpenGate(station.getOpenGate(), station.getGateOpenThreshold()))
                        .collect(Collectors.toList());
            }
        } else if (stationType.equals(StationType.TIDE.type())) {
            //2021.04.14 新增潮位站分支逻辑

            if (requestVo.isAstrotd()) {
                // 2021.05.18 新增天文潮位逻辑
                List<StAstrotdR> wasList = rainAndWaterMapper.selectAstrotdByTimeCode(stations.stream().map(AttStBase::getStCode).collect(Collectors.toList()), requestVo.getDataTime());
                Map<String, List<StAstrotdR>> map = wasList.stream().collect(Collectors.groupingBy(StAstrotdR::getMgstcd));
                for (AttStBase station : stations) {
                    String stationCode = station.getStCode();
                    List<StAstrotdR> stTideRList = map.get(stationCode);
                    if (stTideRList != null) {
                        StAstrotdR stTideR = stTideRList.get(0);
                        station.setTdz(stTideR.getTdz().doubleValue());
                        station.setTime(stTideR.getYmdh());
                    }
                }
                stations.sort(Comparator.comparing(AttStBase::getTdz));
            } else {
//            List<StTideR> list = rainAndWaterMapper.selectNewestTide(table);
                // 2021.05.13 添加查询某个时间点数据逻辑
                List<StTideR> wasList = rainAndWaterMapper.selectTideByTimeCode(table, stations.stream().map(AttStBase::getStCode).collect(Collectors.toList()), requestVo.getDataTime());
                Map<String, List<StTideR>> map = wasList.stream().collect(Collectors.groupingBy(StTideR::getMgstcd));
                for (AttStBase station : stations) {
                    String stationCode = station.getStCode();
                    List<StTideR> stTideRList = map.get(stationCode);
                    if (stTideRList != null) {
                        StTideR stTideR = stTideRList.get(0);
                        station.setTdz(stTideR.getTdz().doubleValue());
                        station.setTime(stTideR.getTm());
                        String status = StringUtils.EMPTY;
                        if (station.getWarningLevel() != null && station.getWarningLevel() > 0d && station.getTdz() > station.getWarningLevel()) {
                            station.setOverWarnLevel(station.getTdz() - station.getWarningLevel());
                            station.setWarning(true);
                        }
                        station.setStatus(status);
                    }
                }
                stations.sort(Comparator.comparing(AttStBase::getTdz));
            }
        } else if (stationType.equals(StationType.PUST.type())) {
            List<StPustR> wasList = rainAndWaterMapper.selectPustByTimeCode(stations.stream().map(AttStBase::getStCode).collect(Collectors.toList()), requestVo.getDataTime());
            Map<String, List<StPustR>> map = wasList.stream().collect(Collectors.groupingBy(StPustR::getStcd));
            for (AttStBase station : stations) {
                String stationCode = station.getStCode();
                List<StPustR> pustList = map.get(stationCode);
                if (pustList != null) {
                    StPustR stPustR = pustList.get(0);
                    station.setNum1(stPustR.getNum1());
                    station.setNum2(stPustR.getNum2());
                    station.setNum3(stPustR.getNum3());
                    station.setNum4(stPustR.getNum4());
                    station.setNum5(stPustR.getNum5());
                    station.setJskZ(stPustR.getJskZ());
                    station.setJscZ(stPustR.getJscZ());
                    station.setCscZ(stPustR.getCscZ());
                    station.setLl(stPustR.getSum());
                    station.setTime(stPustR.getTm());
                }
            }
        }
        if (requestVo.isWarning()) {
            stations = stations.stream().filter(AttStBase::isWarning).collect(Collectors.toList());
        }
        //2021.02.23 新增用户收藏点位状态判断
//        UserCollectionQueryVo userCollectionQueryVo = new UserCollectionQueryVo();
//        userCollectionQueryVo.setUserId(JwtTokenUtil.getCurrentUserId().orElse(StringUtils.EMPTY));
//        userCollectionQueryVo.setType(type);
//        Set<String> collections = userCollectionMapper.selectByCondition(userCollectionQueryVo);
//        for (AttStBase station : stations) {
//            if (collections.contains(station.getStCode())) {
//                station.setCollected(true);
//            }
//        }
        //测站在线离线判断 2021.09.29
        try {
            Set<String> offStationCodes = future.get();
            stations.forEach(station -> station.setOnline(!offStationCodes.contains(station.getStCode())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (requestVo.getOnline() != null) {
            stations = stations.stream()
                    .filter(station -> station.getOnline().booleanValue() == requestVo.getOnline().booleanValue())
                    .collect(Collectors.toList());
        }
        return stations;
    }

    private class StationOfflineStatus implements Callable<Set<String>> {

        @Override
        public Set<String> call() throws Exception {
            try {
                Calendar calendar = Calendar.getInstance();
                Date now = new Date();
                calendar.setTime(now);
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String start = format.format(calendar.getTime());
                String end = format.format(now);
                List<Map<String, Object>> list = dataManagementClient.findStationOffline(start, end).getData();
                return list.stream().map(map -> map.get("station_code").toString()).collect(Collectors.toSet());
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptySet();
            }
        }
    }


    @Override
    public JSONObject waterLevel(WaterLevelQueryVo queryVo) {
        JSONObject result = new JSONObject();
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        if(queryVo.getEndTime() != null){
            now = queryVo.getEndTime();
        }
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        calendar.add(Calendar.DATE, queryVo.getDays() * -1);
        Date start = calendar.getTime();
        if(queryVo.getStartTime() != null){
            start = queryVo.getStartTime();
            calendar.setTime(start);
        }
        AttStBase station = stationMapper.load(queryVo.getStationCode());
        if (station == null) {
            return result;
        }
        result.put("limitLevel", station.getLimitLevel() == null ? 0d : station.getLimitLevel().doubleValue());
        result.put("warningLevel", station.getWarningLevel() == null ? 0d : station.getWarningLevel().doubleValue());
        result.put("guaranteeLevel", station.getGuaranteeLevel() == null ? 0d : station.getGuaranteeLevel().doubleValue());
        result.put("floodLevel", station.getFloodLevel() == null ? 0d : station.getFloodLevel().doubleValue());
        result.put("highestLevel", station.getHighestLevel());
        List<String> tables = new ArrayList<>();
        String table = "";
        String stationType = queryVo.getStationType();
        switch (stationType) {
            case "R3":
            case "R2":
            case "RR":
                table = StationType.RESERVOIR.table();
                break;
            case "ZZ":
                table = StationType.RIVER.table();
                break;
            case "DD":
                table = StationType.GATE.table();
                break;
            case "TT":
                table = StationType.TIDE.table();
                break;
            default:
                table = StationType.RIVER.table();
                break;
        }
        String zero = month < 10 ? "0" : "";
        tables.add(table.concat("" + year + zero + month));
        if (year != calendar.get(Calendar.YEAR) || month != calendar.get(Calendar.MONTH) + 1) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            zero = month < 10 ? "0" : "";
            tables.add(table.concat("" + year + zero + month));
        }
        if (stationType.equals(StationType.RESERVOIR.type())) {
            // 2021.07.28 永嘉水库水位数据接入逻辑
            List<StRsvrR> data = Collections.emptyList();
            if (station.getStCode().startsWith(yjStcdPrefix)) {
                data = rainAndWaterMapper.selectResStationWaterLevel(Arrays.asList(yjRsvrTable), start, now, station.getStCode());
            } else {
                data = rainAndWaterMapper.selectResStationWaterLevel(tables, start, now, station.getStCode());
            }

            // 5.10 跨月查询数据混乱，重新排序
            if (CollectionUtils.isNotBlank(data)) {
                data = data.stream().sorted(Comparator.comparing(StRsvrR::getTm)).collect(Collectors.toList());
            }
            result.put("data", data);
        } else if (stationType.equals(StationType.RIVER.type())) {
            // 5.10 跨月查询数据混乱，重新排序
            List<StRiverR> data = rainAndWaterMapper.selectRiverStationWaterLevel(tables, start, now, station.getStCode());
            if (CollectionUtils.isNotBlank(data)) {
                data = data.stream().sorted(Comparator.comparing(StRiverR::getTm)).collect(Collectors.toList());
            }
            result.put("data", data);

        } else if (stationType.equals(StationType.GATE.type())) {
            List<String> sixstCodes=Arrays.asList("2021101101","2021101102","2021101103","2021101104","2021101105","2021101106");
            //堰闸
            // 5.10 跨月查询数据混乱，重新排序
            List<StWasR> data = rainAndWaterMapper.selectHourWas(tables, start, now, station.getStCode());
            int days = queryVo.getDays();
            //抽稀
            if(days > 2){
                int total = days * 24;
                int size = data.size();
                int interval = size / total;
                Iterator<StWasR> it = data.iterator();
                int index = 0;
                while (it.hasNext()){
                    it.next();
                    if(index < size - 1 && interval != 0 && index % interval != 0){
                        it.remove();
                    }
                    index++;
                }
            }
            for (StWasR stWasR:data){
                if(stWasR.getOvs()!=null){
                    String[] strings=stWasR.getOvs().split(",");
                        stWasR.setOvs(strings[0]);
                }else
                    stWasR.setOvs("0.00");
                if(sixstCodes.contains(stWasR.getStcd())&&stWasR.getOvs()!=null){
                    stWasR.setOvs(String.valueOf(new BigDecimal(Double.valueOf(stWasR.getOvs())/100).setScale(3,BigDecimal.ROUND_HALF_UP)));
                }
                if("1".equals(stWasR.getStcd())){
                    stWasR.setOvs("-");
                }
            }
            if (CollectionUtils.isNotBlank(data)) {
                data = data.stream().sorted(Comparator.comparing(StWasR::getTm)).collect(Collectors.toList());
            }
            result.put("data", data);
            result.put("total",data.size());
        } else if (stationType.equals(StationType.TIDE.type())) {
            // 4月22日新增
            // 5.10 跨月查询数据混乱，重新排序
            // 2021.08.03 将实时潮位变为5分钟一个点
            List<StPrrwtResVo> realTdzList;
            if(StringUtils.isNotBlank(queryVo.getFcstId())){
                realTdzList = rainAndWaterMapper.listFcstVal(queryVo);
            }else {
                realTdzList = rainAndWaterMapper.selectTideByTimeInterval(tables, start, now, Collections.singletonList(station.getStCode()));
            }
            if (CollectionUtils.isNotBlank(realTdzList)) {
                realTdzList = realTdzList.stream().sorted(Comparator.comparing(StPrrwtResVo::getTm)).collect(Collectors.toList());
            }
            // 2021.08.03 新增天文潮位逻辑
            List<StPrrwtResVo> astroTdzList = rainAndWaterMapper.selectAstrotdByTimeInterval(DateUtils.clearDateTimePrecision(start, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND),// 如果起始时刻是 16：15 ，那么需要取16:00 的天文潮位，然后才能生成 16：20 16：25 等时刻的天文潮位
                    now,
                    Collections.singletonList(station.getStCode()));
            if (CollectionUtils.isBlank(astroTdzList)) {
                result.put("data", realTdzList);
                result.put("astroTdz", Collections.emptyList());
            } else {

                Map<Date, StPrrwtResVo> realMap = realTdzList.stream()
                        .collect(Collectors.groupingBy(StPrrwtResVo::getTm))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey,
                                o -> o.getValue().get(0)));

                // 将天文潮变成每5分钟一个点
                List<StPrrwtResVo> finalAstroList = new ArrayList<>(astroTdzList.size() * 12);
                int astroLength = astroTdzList.size();
                int nextIndex = 0;
                for (int i = 0; i < astroLength; i++) {
                    StPrrwtResVo current = astroTdzList.get(i);
                    nextIndex = i + 1;
                    if (nextIndex < astroLength) {
                        StPrrwtResVo next = astroTdzList.get(nextIndex);
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

                // emmmm 假设start 是16:12 需要把16:12 变成16：15 此处代码就是此作用
                // 就是创造时刻索引，5分钟一个点，天文潮，真实潮位均按此来
                LocalDateTime tempStartLd = DateUtils.date2LocalDateTime(start);
                int startMinutesTemp = tempStartLd.getMinute();
                LocalDateTime tempEndLd = DateUtils.date2LocalDateTime(now);
                int endMinutesTemp = tempEndLd.getMinute();
                // 取余
                int startLeft = startMinutesTemp % 5;
                int startMinutes = 0;
                if (startLeft == 0) {
                    startMinutes = startMinutesTemp;
                } else {
                    // 取整
                    int temp = startMinutesTemp / 5;
                    // 如果整数为11 如16：58 那么取16：55
                    if (temp == 11) {
                        startMinutes = 0;
                    } else {
                        startMinutes = (temp + 1) * 5;
                    }
                }
                // 取余
                int endLeft = endMinutesTemp % 5;
                int endMinutes = 0;
                if (endLeft == 0) {
                    endMinutes = endMinutesTemp;
                } else {
                    // 取整
                    int temp = startMinutesTemp / 5;
                    // 如果整数为0 如16：03 那么取16：00
                    if (temp == 0) {
                        endMinutes = 0;
                    } else {
                        endMinutes = (temp - 1) * 5;
                    }
                }
                Date tempStart = DateUtils.localDateTime2Date(tempStartLd.withNano(0).withMinute(startMinutes).withSecond(0));
                Date tempEnd = DateUtils.localDateTime2Date(tempEndLd.withNano(0).withMinute(endMinutes).withSecond(0));
                int size = (int) ((tempEnd.getTime() - tempStart.getTime()) / (5 * 60 * 1000));

                // 将实时潮位和天文潮位按时间一个个匹配
                List<StPrrwtResVo> resultRealList = new ArrayList<>(size);
                List<StPrrwtResVo> resultAstroList = new ArrayList<>(size);
//                List<StPrrwtResVo> subTideList = new ArrayList<>(size);
                Date timeIndex = tempStart;
                while (!timeIndex.after(tempEnd)) {
                    StPrrwtResVo realTemp = realMap.get(timeIndex);
                    StPrrwtResVo astroTemp = astroMap.get(timeIndex);
                    if (realTemp == null) {
                        realTemp = Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, timeIndex).build();
                    }
                    if (astroTemp == null) {
                        astroTemp = Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, timeIndex).build();
                    }
                    resultRealList.add(realTemp);
                    resultAstroList.add(astroTemp);
                    timeIndex = new Date(timeIndex.getTime() + 5 * 60 * 1000);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //如果断面id不为空 查询预测潮位
                if (StringUtils.isNotBlank(queryVo.getDmId())){
                    List<SectionStorm> list = projectManageClient.predictTideLevel(queryVo.getDmId());
//                    List<SectionStorm> list = (List<SectionStorm>) commonResult.getData();
                    for (SectionStorm jsonObject : list) {
                        for (StPrrwtResVo datum : resultRealList) {
                            if (sdf.format(datum.getTm()).equals(sdf.format(jsonObject.getTm()))){
                                datum.setH2(jsonObject.getH2());
                            }
                        }
                    }
                }

                result.put("data", resultRealList);
                result.put("astroTdz", resultAstroList);
            }
        } else {
            List<StPrrwtResVo> data = rainAndWaterMapper.selectPustByTimeInterval(start, now, Collections.singletonList(station.getStCode()));
            if (CollectionUtils.isNotBlank(data)) {
                data = data.stream().sorted(Comparator.comparing(StPrrwtResVo::getTm)).collect(Collectors.toList());
            }
            result.put("data", data);
        }
        result.put("highestLevel", station.getHighestLevel() == null ? "-" : station.getHighestLevel().doubleValue());
        result.put("appearTime", station.getAppearTime());
        return result;
    }

    /**
     * 导出水位
     *
     * @param waterLevelQueryVo
     * @param response
     */
    @Override
    public void exportWaterLevels(WaterLevelQueryVo waterLevelQueryVo, HttpServletResponse response) {
        JSONObject jsonObject = waterLevel(waterLevelQueryVo);
        if(StationType.TIDE.type().equals(waterLevelQueryVo.getStationType())){
            List<StPrrwtResVo> realTdzList = (List<StPrrwtResVo>)jsonObject.get("data");
           // if(CollectionUtils.isNotBlank(realTdzList)){
                List<TideExportResVo> tideExportResVos = JSONArray.parseArray(JSON.toJSONString(realTdzList), TideExportResVo.class);
                export("实时潮位",response,tideExportResVos,TideExportResVo.class,waterLevelQueryVo.getStationName());
          //  }
        }
    }

    private <T>void export(String fileName,HttpServletResponse response,List<T> list,Class<T> clazz,String sheetName){
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.addHeader("Access-Control-Expose-Headers","Content-Disposition");
            EasyExcel.write(response.getOutputStream(),clazz).sheet(sheetName).doWrite(list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<MultiWaterRainResVo> multiWaterRains(MultiWaterRainReqVo reqVo) {
        if (reqVo.getEnd() == null || reqVo.getStart() == null) {
            return Collections.emptyList();
        }
        List<AttStBase> stations = stationMapper.selectByCondition(Builder.of(StationRequestVo::new)
                .with(StationRequestVo::setStationCodes, reqVo.getStcds())
                .with(StationRequestVo::setStationType, reqVo.getStType())
                .with(StationRequestVo::setDisplay, reqVo.getDisplay())
                .build());
        if (CollectionUtils.isBlank(stations)) {
            return Collections.emptyList();
        }

        Date start = reqVo.getStart();
        Date end = reqVo.getEnd();
        Map<String, List<AttStBase>> typeGroupMap = stations.stream().collect(Collectors.groupingBy(AttStBase::getStType));
        List<MultiWaterRainResVo> result = new ArrayList<>(stations.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        typeGroupMap.forEach((key, values) -> {
            Map<String, List<StPrrwtResVo>> valueMap = null;
            List<String> stcds = values.stream().map(AttStBase::getStCode).collect(Collectors.toList());
            switch (key) {
                case "RR": {
                    List<String> tables = echoWaterRainTableByDateInternal(StationType.RESERVOIR.table(), start, end);
                    List<StPrrwtResVo> data = Collections.emptyList();
                    if (CollectionUtils.isNotBlank(tables)) {
                        data = rainAndWaterMapper.selectRsvrByTimeInterval(tables, start, end, stcds);
                    }
                    valueMap = data.parallelStream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
                }
                break;
                case "ZZ": {
                    List<String> tables = echoWaterRainTableByDateInternal(StationType.RIVER.table(), start, end);
                    List<StPrrwtResVo> data = Collections.emptyList();
                    if (CollectionUtils.isNotBlank(tables)) {
                        data = rainAndWaterMapper.selectRiverByTimeInterval(tables, start, end, stcds);
                    }
                    valueMap = data.parallelStream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
                }
                break;
                case "DD": {
                    List<String> tables = echoWaterRainTableByDateInternal(StationType.GATE.table(), start, end);
                    List<StPrrwtResVo> data = Collections.emptyList();
                    if (CollectionUtils.isNotBlank(tables)) {
                        data = rainAndWaterMapper.selectWasByTimeInterval(tables, start, end, stcds);
                    }
                    valueMap = data.parallelStream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
                }
                break;
                case "TT": {
                    if (reqVo.isAstrotd()) {
                        List<StPrrwtResVo> data = rainAndWaterMapper.selectAstrotdByTimeInterval(start, end, stcds);
                        valueMap = data.parallelStream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
                    } else {
                        List<String> tables = echoWaterRainTableByDateInternal(StationType.TIDE.table(), start, end);
                        List<StPrrwtResVo> data = Collections.emptyList();
                        if (CollectionUtils.isNotBlank(tables)) {
                            data = rainAndWaterMapper.selectTideByTimeInterval(tables, start, end, stcds);
                        }
                        valueMap = data.parallelStream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
                    }
                }
                break;
                case "PP": {
                    List<String> tables = echoWaterRainTableByDateInternal(StationType.RAINFALL.table(), start, end);
                    List<StPrrwtResVo> data = Collections.emptyList();
                    if (CollectionUtils.isNotBlank(tables)) {
                        data = rainAndWaterMapper.selectRainfallByTimeInterval(tables, start, end, stcds);
                    }
                    valueMap = data.parallelStream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
                }
                break;
                default:
                    break;
            }
            for (AttStBase st : values) {
                MultiWaterRainResVo temp = new MultiWaterRainResVo();
                BeanUtils.copyProperties(st, temp);
                List<StPrrwtResVo> waterRains = valueMap.getOrDefault(st.getStCode(), Collections.emptyList());
                if (CollectionUtils.isNotBlank(waterRains)) {
                    waterRains = waterRains.stream().sorted(Comparator.comparing(StPrrwtResVo::getTm)).collect(Collectors.toList());
                }
                temp.setWaterRains(waterRains);
                result.add(temp);
            }
        });
        return result;
    }


    @Override
    public List<StPptnR> rainfall(SynRainRequestVo synRainRequestVo) {
        StationRequestVo requestVo = new StationRequestVo();
        requestVo.setStationName(synRainRequestVo.getStationName());
        requestVo.setIsRainStation(Boolean.TRUE);
        //添加是否重点关注过滤
        requestVo.setIsFocus(synRainRequestVo.getIsFocus());
        requestVo.setDisplay(synRainRequestVo.getIsDisplay());
        if (ObjectUtils.isNotEmpty(synRainRequestVo.getDataSource())) {
            //判断是否永嘉测站
            requestVo.setIsYongJiaStation(synRainRequestVo.getDataSource().equals(2) ? Boolean.TRUE : Boolean.FALSE);
            //判断是否水文站测站
            requestVo.setIsShuiWenStation(synRainRequestVo.getDataSource().equals(1) ? Boolean.TRUE : Boolean.FALSE);
        }

        if (StringUtils.isNotBlank(synRainRequestVo.getAreaCode())) {
            synRainRequestVo.setAreaCode(requestVo.getAreaCode().startsWith("330") ? requestVo.getAreaCode() :
                    adcdBaseMapper.getWenzhouAreaCodeByAdnm(requestVo.getAreaCode()));
        }
        requestVo.setBas(synRainRequestVo.getBas());

        List<AttStBase> stations = stationMapper.selectByCondition(requestVo);
//        Map<String, List<AttStBase>> map = stations.stream().collect(Collectors.groupingBy(AttStBase::getStCode));
        //根据条件查询雨量数据
        RainfallQueryVo queryVo = new RainfallQueryVo();
        if (null != synRainRequestVo.getMin() && null != synRainRequestVo.getMax()) {
            queryVo.setMin(synRainRequestVo.getMin());
            queryVo.setMax(synRainRequestVo.getMax());
        }
        Integer minutes = synRainRequestVo.getMinutes();
        List<StPptnR> rainfall = new ArrayList<>();
        List<StPptnR> result = new ArrayList<>();
        String start = synRainRequestVo.getStart();
        String end = synRainRequestVo.getEnd();
        if ((minutes != null || (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end))) && !stations.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 2021.08.22 前端传参有误，在此修正
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                queryVo.setEnd(StringUtils.isBlank(end) ? new Date() : format.parse(end));
            } catch (ParseException e) {
                try {
                    queryVo.setEnd(StringUtils.isBlank(end) ? new Date() : format2.parse(end));
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
//                e.printStackTrace();
            }
            Calendar instance = Calendar.getInstance();
            instance.setTime(queryVo.getEnd());
            int year = instance.get(Calendar.YEAR);
            int month = instance.get(Calendar.MONTH) + 1;
            if (minutes != null) {
                instance.add(Calendar.MINUTE, -1 * minutes.intValue());
                queryVo.setStart(instance.getTime());
            } else if (StringUtils.isNotBlank(start)) {
                try {
                    queryVo.setStart(format.parse(start));
                    instance.setTime(queryVo.getStart());
                } catch (ParseException e) {
                    try {
                        queryVo.setStart(format2.parse(start));
                        instance.setTime(queryVo.getStart());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
//                    e.printStackTrace();
                }
            }
//            queryVo.setMin(synRainRequestVo.getMin());
//            queryVo.setMax(synRainRequestVo.getMax());
//            queryVo.setStationCode(stations.get(0).getStCode());
            List<String> tables = new ArrayList<>();
            String table = StationType.RAINFALL.table();
            String zero = month < 10 ? "0" : "";
            tables.add(table.concat("" + year + zero + month));
            if (year != instance.get(Calendar.YEAR) || month != instance.get(Calendar.MONTH) + 1) {
                year = instance.get(Calendar.YEAR);
                month = instance.get(Calendar.MONTH) + 1;
                zero = month < 10 ? "0" : "";
                tables.add(table.concat("" + year + zero + month));
            }
            queryVo.setCodes(stations.stream().map(AttStBase::getStCode).collect(Collectors.toList()));
            rainfall = rainAndWaterMapper.selectRainfallByCondition(tables, queryVo);
            Map<String, List<StPptnR>> map = rainfall.stream().collect(Collectors.groupingBy(StPptnR::getMgstcd));

            for (AttStBase station : stations) {
                List<StPptnR> rainfalls = map.get(station.getStCode());
                StPptnR stPptnR = new StPptnR();
                stPptnR.setStationName(station.getStName());
                stPptnR.setMgstcd(station.getStCode());
                stPptnR.setStcd(station.getStCode());
//                stPptnR.setTm(stPptnR.getTm());
                stPptnR.setLatitude(station.getStLat() == null ? 0d : station.getStLat().doubleValue());
                stPptnR.setLongitude(station.getStLong() == null ? 0d : station.getStLong().doubleValue());
                stPptnR.setStType(StationType.RAINFALL.type());
                if (rainfalls != null) {
                    stPptnR.setDrp(rainfalls.stream().mapToDouble(StPptnR::getDrp).sum());
                } else {
                    stPptnR.setDrp(-1d);//负数表示暂时无该测站的雨量数据，与前端协定显示`
                }
                result.add(stPptnR);
            }
            result.sort(Comparator.comparingDouble(StPptnR::getDrp).reversed());
            if (synRainRequestVo.isWarning()) {
                result = result.stream().filter(StPptnR::isWarning).collect(Collectors.toList());
            }
            if (null != synRainRequestVo.getMin() && null != synRainRequestVo.getMax()) {
                result = result.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() >= synRainRequestVo.getMin().doubleValue() &&
                        stPptnR.getDrp().doubleValue() <= synRainRequestVo.getMax().doubleValue())
                        .collect(Collectors.toList());
            }
            //2021.02.23 新增用户收藏状态判断逻辑
            UserCollectionQueryVo userCollectionQueryVo = new UserCollectionQueryVo();
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("authorization");
            if (StringUtils.isNotBlank(token)) {
                userCollectionQueryVo.setUserId(JwtTokenUtil.getUserId(token));
            }
            userCollectionQueryVo.setType("雨量");
            Set<String> collections = userCollectionMapper.selectByCondition(userCollectionQueryVo);
            for (StPptnR stPptnR : result) {
                if (collections.contains(stPptnR.getMgstcd())) {
                    stPptnR.setCollected(true);
                }
            }
            return result;
        }
        return result;
    }


    @Override
    public CommonResult rainfallForOne(SynRainRequestVo synRainRequestVo) {

        if (synRainRequestVo.isWarning()) {
            com.ygkj.digitization.vo.request.StationRequestVo vo = new com.ygkj.digitization.vo.request.StationRequestVo();
            vo.setAreaCode(synRainRequestVo.getAreaCode());
            vo.setBas(synRainRequestVo.getBas());
            vo.setStartTime(synRainRequestVo.getStart());
            vo.setEndTime(synRainRequestVo.getEnd());
            vo.setStationName(synRainRequestVo.getStationName());
            return hydrologyClient.rainstormWarning(vo);
        } else {
            return CommonResult.success(rainfall(synRainRequestVo));
        }
    }


    @Override
    public CommonResult rainfallForOneNum(SynRainRequestVo synRainRequestVo) {

        Map<String, Object> numMap = new HashMap<>(2);
        //调用服务接口获取对应的超警数量
        com.ygkj.digitization.vo.request.StationRequestVo vo = new com.ygkj.digitization.vo.request.StationRequestVo();
        vo.setAreaCode(synRainRequestVo.getAreaCode());
        vo.setBas(synRainRequestVo.getBas());
        vo.setStartTime(synRainRequestVo.getStart());
        vo.setEndTime(synRainRequestVo.getEnd());
        vo.setStationName(synRainRequestVo.getStationName());
        vo.setRainstormType("1");
        CommonResult commonResult = hydrologyClient.rainstormWarnNumListForOne(vo);

        //直接设定有降雨值 为传输开始结束时间段内有降雨的站点
        synRainRequestVo.setMin(0.01);
        synRainRequestVo.setMax(10000.00);

        int size = rainfall(synRainRequestVo).size();

        numMap.put("rainfallNum", size);
        numMap.put("warnNum", commonResult.getData());

        return CommonResult.success(numMap);
    }

    @Override
    public List<StPptnR> hourRainfall(HourRainfallQueryVo hourRainfallQueryVo) {
        String stationCode = hourRainfallQueryVo.getStationCode();
        List<StPptnR> rainfall = candidateStationHourRainfall(hourRainfallQueryVo);
//        if (stationCode.startsWith("SWZ_")) {
//        } else if (stationCode.startsWith("YJ_")) {
//            rainfall = candidateYJStationHourRainfall(hourRainfallQueryVo);
//        }
        // 跨越排序混乱，重新排序
        if(CollectionUtils.isNotBlank(rainfall)){
            rainfall = rainfall.stream().sorted(Comparator.comparing(StPptnR::getTm).reversed()).collect(Collectors.toList());
        }
        return rainfall;
    }

    /**
     * 导出雨量
     *
     * @param hourRainfallQueryVo
     * @param response
     */
    @Override
    public void exportHourRainfall(HourRainfallQueryVo hourRainfallQueryVo, HttpServletResponse response) {
        List<StPptnR> list = hourRainfall(hourRainfallQueryVo);
        export("雨量监测",response,list,StPptnR.class,hourRainfallQueryVo.getStationName());
    }

    @Override
    public List<StTideR> selectTideByTimeCode(String table, List<String> stcds, Date dataTime) {
        return rainAndWaterMapper.selectTideByTimeCode(table,stcds,dataTime);
    }

    @Override
    public JSONObject waterRainHours(WaterLevelVo queryVo) {

        JSONObject result = new JSONObject();
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        if(queryVo.getEndTime() != null){
            now = queryVo.getEndTime();
        }
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        calendar.add(Calendar.DATE, queryVo.getDays() * -1);
        Date start = calendar.getTime();
        if(queryVo.getStartTime() != null){
            start = queryVo.getStartTime();
            calendar.setTime(start);
        }
        String stationType = queryVo.getStationType();
        String prcd = queryVo.getPrcd();
        //获取绑定主水位测站
        if(com.ygkj.utils.StringUtils.isNotBlank(prcd)){
            String stcd = rainAndWaterMapper.ProjStRel(prcd, stationType);
            queryVo.setStationCode(stcd);
        }
        AttStBase station = stationMapper.load(queryVo.getStationCode());
        //获取绑定雨量测站
        String stcdPP;
        //如果是查询雨量测站，只需要雨量数据
        if ("PP".equals(stationType)){
            stcdPP = queryVo.getStationCode();
        }else {
            stcdPP = rainAndWaterMapper.ProjStRel(prcd, "PP");
        }
        List<String> dateSting = getDateSting(queryVo.getDays());
        List<StPptnR> stPptnRS = null;
        //查询实时雨量数据
        if (stcdPP != null){
            List<String> tables = getTablesName(calendar, year, month, "PP");
            HourRainfallQueryVo hourRainfallQueryVo = new HourRainfallQueryVo();
            hourRainfallQueryVo.setStart(start);
            hourRainfallQueryVo.setEnd(now);
            hourRainfallQueryVo.setStationCode(stcdPP);
            stPptnRS = rainAndWaterMapper.selectHourRainfall(tables, hourRainfallQueryVo);
        }
        List<StRsvrR> stRsvrRS = null;
        List<StRiverR> stRiverRS = null;
        List<StWasR> stWasRS = null;
        List<StTideR> stTideRS = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        //查询实时水位数据
        result.put("limitLevel", null);
        result.put("warningLevel", null);
        result.put("guaranteeLevel", null);
        result.put("floodLevel", null);
        result.put("hasRain", false);
        result.put("hasWater", false);
        if (station != null) {
            result.put("limitLevel", station.getLimitLevel() == null ? 0d : station.getLimitLevel().doubleValue());
            result.put("warningLevel", station.getWarningLevel() == null ? 0d : station.getWarningLevel().doubleValue());
            result.put("guaranteeLevel", station.getGuaranteeLevel() == null ? 0d : station.getGuaranteeLevel().doubleValue());
            result.put("floodLevel", station.getFloodLevel() == null ? 0d : station.getFloodLevel().doubleValue());
            //获取表名
            List<String> tables = getTablesName(calendar, year, month, stationType);

            if (stationType.equals(StationType.RESERVOIR.type())) {
                stRsvrRS = rainAndWaterMapper.selectResStationWaterLevel(tables, start, now, station.getStCode());

            } else if (stationType.equals(StationType.RIVER.type())) {
                stRiverRS = rainAndWaterMapper.selectRiverStationWaterLevel(tables, start, now, station.getStCode());

            } else if (stationType.equals(StationType.GATE.type())) {
                stWasRS = rainAndWaterMapper.selectHourWasLevel(tables, start, now, station.getStCode());

            }else if (stationType.equals(StationType.TIDE.type())){
                stTideRS = rainAndWaterMapper.selectHourTide(tables, start, now,station.getStCode());
            }
        }
        List<JSONObject> results = new ArrayList<>();
        Map<String, List<StPptnR>> listMap = new HashMap<>();
        if (stPptnRS != null){
            listMap = stPptnRS.stream().collect(Collectors.groupingBy(s -> sdf.format(s.getTm())));
        }
        Double maxLevel = null;
        Date maxLevelTime = null;
        Double minLevel = null;
        Double rainAll = 0d;
        for (String date : dateSting) {
            //拼装数据
            JSONObject json = new JSONObject();
            json.put("date",date + ":00:00");
            if (StringUtils.isNotBlank(stcdPP)){
                List<StPptnR> pptnRS = listMap.get(date);
                if (CollectionUtils.isNotBlank(pptnRS)){
                    json.put("rainValue",pptnRS.get(0).getDrp());
                    result.put("hasRain", true);
                } else {
                    json.put("rainValue",null);
                }
                if (CollectionUtils.isNotBlank(pptnRS)){
                    rainAll += pptnRS.get(0).getDrp();
                }
            }
            if (CollectionUtils.isNotBlank(stRsvrRS)){
                List<StRsvrR> stRsvrR = stRsvrRS.stream().filter(s -> date.equals(sdf.format(s.getTm()))).collect(Collectors.toList());
                json.put("waterLevel", stRsvrR.size()>0 ? stRsvrR.get(0).getRz() : null);
                if (stRsvrR.size()>0){
                    result.put("hasWater", true);
                    Double rz = stRsvrR.get(0).getRz();
                    Date tm = stRsvrR.get(0).getTm();
                    if (maxLevel == null){
                        maxLevel = rz;
                        maxLevelTime = tm;
                    } else {
                        if (maxLevel < rz){
                            maxLevel = rz;
                            maxLevelTime = tm;
                        }

                    }
                    if (minLevel == null){
                        minLevel = rz;
                    } else {
                        if (minLevel > rz){
                            minLevel = rz;
                        }

                    }
                }
            }
            if (CollectionUtils.isNotBlank(stRiverRS)){
                List<StRiverR> stRiverR = stRiverRS.stream().filter(s -> date.equals(sdf.format(s.getTm()))).collect(Collectors.toList());
                json.put("waterLevel", stRiverR.size()>0 ? stRiverR.get(0).getZ() : null);
                if (stRiverRS.size()>0){
                    result.put("hasWater", true);
                    Double rz = stRiverRS.get(0).getZ();
                    Date tm = stRiverRS.get(0).getTm();
                    if (maxLevel == null){
                        maxLevel = rz;
                        maxLevelTime = tm;
                    } else {
                        if (maxLevel < rz){
                            maxLevel = rz;
                            maxLevelTime = tm;
                        }

                    }
                    if (minLevel == null){
                        minLevel = rz;
                    } else {
                        if (minLevel > rz){
                            minLevel = rz;
                        }

                    }
                }
            }
            if (CollectionUtils.isNotBlank(stWasRS)){
                List<StWasR> stWasR = stWasRS.stream().filter(s -> date.equals(sdf.format(s.getTm()))).collect(Collectors.toList());
                json.put("waterLevel", stWasR.size()>0 ? stWasR.get(0).getUpz() : null);
                json.put("upz", stWasR.size()>0 ? stWasR.get(0).getUpz() : null);
                json.put("dwz", stWasR.size()>0 ? stWasR.get(0).getDwz() : null);
                json.put("ovs", stWasR.size()>0 ? stWasR.get(0).getOvs() : null);
                json.put("ll", stWasR.size()>0 ? stWasR.get(0).getLl() : null);
                if (stWasRS.size()>0){
                    result.put("hasWater", true);
                    Double rz = stWasRS.get(0).getUpz();
                    Date tm = stWasRS.get(0).getTm();
                    if (maxLevel == null){
                        maxLevel = rz;
                        maxLevelTime = tm;
                    } else {
                        if (maxLevel < rz){
                            maxLevel = rz;
                            maxLevelTime = tm;
                        }

                    }
                    if (minLevel == null){
                        minLevel = rz;
                    } else {
                        if (minLevel > rz){
                            minLevel = rz;
                        }

                    }
                }
            }
            if (CollectionUtils.isNotBlank(stTideRS)){
                List<StTideR> stTideR = stTideRS.stream().filter(s -> date.equals(sdf.format(s.getTm()))).collect(Collectors.toList());
                json.put("waterLevel", stTideR.size()>0 ? stTideR.get(0).getTdz() : null);
                if (stTideRS.size()>0){
                    result.put("hasWater", true);
                    Double rz = stTideRS.get(0).getTdz();
                    Date tm = stTideRS.get(0).getTm();
                    if (maxLevel == null){
                        maxLevel = rz;
                        maxLevelTime = tm;
                    } else {
                        if (maxLevel < rz){
                            maxLevel = rz;
                            maxLevelTime = tm;
                        }

                    }
                    if (minLevel == null){
                        minLevel = rz;
                    } else {
                        if (minLevel > rz){
                            minLevel = rz;
                        }
                    }
                }
            }
            results.add(json);
        }
        result.put("list",results);
        result.put("maxLevel",maxLevel);
        result.put("maxLevelTime",maxLevelTime);
        result.put("minLevel",minLevel);
        result.put("rainAll",rainAll);

        return result;
    }

    @Override
    public RainSurfaceResVo calculateRainfallSurface(String dataInterval, String startTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        StationRequestVo stationRequestVo = new StationRequestVo();
        stationRequestVo.setIsRainStation(true);
        List<AttStBase> stations = stationMapper.selectByCondition(stationRequestVo);
        List<String> stcds = stations.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        RainfallQueryVo queryVo = new RainfallQueryVo();
        queryVo.setCodes(stcds);
        try {
            queryVo.setStart(format.parse(startTime));
            queryVo.setEnd(format.parse(endTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        queryVo.setNeedSum(Boolean.TRUE);
        //查询指定时间的雨量数据
        List<StPptnR> rainfalls = rainAndWaterMapper.selectRainfallByCondition(TableCandidateUtil.candidateMonthTable(StationType.RAINFALL.table(), LocalDate.parse(startTime.substring(0,10)),LocalDate.parse(endTime.substring(0,10))),
                queryVo);
        Map<String, AttStBase> data = stations.stream()
                .filter(attStBase -> attStBase.getStLong() != null && attStBase.getStLat() != null)
                .collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
        Map<String, DoubleSummaryStatistics> map = rainfalls.stream().collect(Collectors.groupingBy(StPptnR::getMgstcd, Collectors.summarizingDouble(StPptnR::getDrp)));

        double[][] features = new double[data.size()][3];
        AtomicInteger index = new AtomicInteger();
        map.forEach((code, value) -> {
            int i = index.get();
            features[i][0] = data.get(code).getStLong().doubleValue();
            features[i][1] = data.get(code).getStLat().doubleValue();
            features[i][2] = value.getSum();
            index.getAndIncrement();
        });
        double[] transferInterval = RainSurfaceUtil.transferInterval(dataInterval);
        return RainSurfaceUtil.calEquiSurface(features, transferInterval, size, fileUrl, isClip);
    }

    @PostConstruct
    public void init() {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:lc_polygon.shp");
            Resource resource = resources[0];
            fileUrl = resource.getURL();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<String> getTablesName(Calendar calendar, int year, int month, String stationType) {
        List<String> tables = new ArrayList<>();
        String table = "";
        switch (stationType) {
            case "RR":
                table = StationType.RESERVOIR.table();
                break;
            case "ZZ":
                table = StationType.RIVER.table();
                break;
            case "DD":
                table = StationType.GATE.table();
                break;
            case "TT":
                table = StationType.TIDE.table();
                break;
            case "PP":
                table = StationType.RAINFALL.table();
                break;
            default:
                table = StationType.RESERVOIR.table();
                break;
        }
        String zero = month < 10 ? "0" : "";
        tables.add(table.concat("" + year + zero + month));
        if (year != calendar.get(Calendar.YEAR) || month != calendar.get(Calendar.MONTH) + 1) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            zero = month < 10 ? "0" : "";
            tables.add(table.concat("" + year + zero + month));
        }
        return tables;
    }

    private List<StPptnR> candidateStationHourRainfall(HourRainfallQueryVo hourRainfallQueryVo) {
        List<StPptnR> rainfall = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        List<String> tables = new ArrayList<>();
        String table = StationType.RAINFALL.table();
        if (hourRainfallQueryVo.getStart() != null && hourRainfallQueryVo.getEnd() != null) {
            calendar.setTime(hourRainfallQueryVo.getEnd());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            String zero = month < 10 ? "0" : "";
            tables.add(table.concat("" + year + zero + month));
            calendar.setTime(hourRainfallQueryVo.getStart());
            if (year != calendar.get(Calendar.YEAR) || month != calendar.get(Calendar.MONTH) + 1) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                zero = month < 10 ? "0" : "";
                tables.add(table.concat("" + year + zero + month));
            }
            rainfall = rainAndWaterMapper.selectHourRainfall(tables, hourRainfallQueryVo);
            // 20210625 新增逻辑，然后将时间往未来推
            if (hourRainfallQueryVo.getTimeForward() != null && hourRainfallQueryVo.getTimeForward()) {
                Date rightNow = DateUtils.localDateTime2Date(LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).plusHours(1));
                long forwardTimeSub = rightNow.getTime() - hourRainfallQueryVo.getStart().getTime();
                for (StPptnR temp : rainfall) {
                    temp.setTm(new Date(temp.getTm().getTime() + forwardTimeSub));
                }
            }
            return rainfall;
        }else if (hourRainfallQueryVo.getDays() != null) {
            if (null != hourRainfallQueryVo.getYear()) {
                //查往年
                calendar.setTime(new Date());
                calendar.set(Calendar.YEAR, hourRainfallQueryVo.getYear());
                hourRainfallQueryVo.setEnd(calendar.getTime());
            } else {
                Date now = hourRainfallQueryVo.getTime() == null ? new Date() : hourRainfallQueryVo.getTime();
                hourRainfallQueryVo.setEnd(now);
                calendar.setTime(now);
            }
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            String zero = month < 10 ? "0" : "";
            tables.add(table.concat("" + year + zero + month));
            calendar.add(Calendar.DATE, -1 * hourRainfallQueryVo.getDays());
            hourRainfallQueryVo.setStart(calendar.getTime());
            if (year != calendar.get(Calendar.YEAR) || month != calendar.get(Calendar.MONTH) + 1) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                zero = month < 10 ? "0" : "";
                tables.add(table.concat("" + year + zero + month));
            }
        }
        rainfall = rainAndWaterMapper.selectHourRainfall(tables, hourRainfallQueryVo);
        return rainfall;
    }

    private List<StPptnR> candidateYJStationHourRainfall(HourRainfallQueryVo hourRainfallQueryVo) {
        hourRainfallQueryVo.setStationCode(hourRainfallQueryVo.getStationCode().replaceAll("YJ_", ""));
        List<StPptnR> rainfall = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        if (hourRainfallQueryVo.getDays() != null) {
            Calendar instance = Calendar.getInstance();
            Date now = new Date();
            instance.setTime(now);
            if (null != hourRainfallQueryVo.getYear()) {
                instance.set(Calendar.YEAR, hourRainfallQueryVo.getYear().intValue());
                hourRainfallQueryVo.setEnd(instance.getTime());
            } else {
                hourRainfallQueryVo.setEnd(now);
//                instance.setTime(now);
            }
            instance.add(Calendar.DAY_OF_MONTH, -1 * hourRainfallQueryVo.getDays().intValue());
            hourRainfallQueryVo.setStart(instance.getTime());
        }
        List<StPptnR> list = rainAndWaterMapper.selectYJStationRainfall(hourRainfallQueryVo);
        if (!list.isEmpty()) {
            String time = format.format(list.get(0).getTm());
            double candidateRainfall = 0d;
            for (StPptnR stPptnR : list) {
                String candidateTime = format.format(stPptnR.getTm());
                if (!time.equals(candidateTime)) {
                    if (candidateTime.endsWith("00:00")) {
                        candidateRainfall += stPptnR.getDrp().doubleValue();
                        try {
                            stPptnR.setTm(format.parse(time));
                            stPptnR.setDrp(new Double(candidateRainfall));
                            rainfall.add((StPptnR) stPptnR.clone());
                            candidateRainfall = 0d;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            stPptnR.setTm(format.parse(time));
                            stPptnR.setDrp(new Double(candidateRainfall));
                            rainfall.add((StPptnR) stPptnR.clone());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        candidateRainfall = stPptnR.getDrp().doubleValue();
                    }
                    time = candidateTime;
                } else {
                    candidateRainfall += stPptnR.getDrp().doubleValue();
                }
            }
            try {
                StPptnR s = new StPptnR();
                s.setDrp(candidateRainfall);
                s.setTm(format.parse(time));
                s.setMgstcd("YJ_".concat(hourRainfallQueryVo.getStationCode()));
                rainfall.add((StPptnR) s.clone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        rainfall.sort(Comparator.comparing(StPptnR::getTm).reversed());
        return rainfall;
    }

    @Override
    public List<StPptnR> rainfallCurve(HourRainfallQueryVo queryVo) {
        int days = queryVo.getDays();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        List<String> tables = new ArrayList<>();
        String table = StationType.RAINFALL.table();
        tables.add(table.concat("" + year + (month < 10 ? "0" + month : month)));
        calendar.add(Calendar.DATE, -1 * days);
        if (year != calendar.get(Calendar.YEAR) || month != calendar.get(Calendar.MONTH) + 1) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            tables.add(table.concat("" + year + (month < 10 ? "0" + month : month)));
        }
        RainfallQueryVo vo = new RainfallQueryVo();
        vo.setStationCode(queryVo.getStationCode());
        vo.setStart(calendar.getTime());
        vo.setEnd(now);
        List<StPptnR> list = rainAndWaterMapper.selectRainfallByCondition(tables, vo);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        Map<String, List<StPptnR>> map = list.stream().collect(Collectors.groupingBy(stPptnR -> format.format(stPptnR.getTm())));
        List<StPptnR> result = new ArrayList<>(map.size());
        list = null;//2021.04.27 help gc  huangzh
        map.forEach((time, rainfalls) -> {
            double sum = rainfalls.stream().mapToDouble(StPptnR::getDrp).sum();
            StPptnR stPptnR = new StPptnR();
            try {
                stPptnR.setTm(format.parse(time));
                stPptnR.setDrp(sum);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            result.add(stPptnR);
        });
        result.sort(new Comparator<StPptnR>() {
            @Override
            public int compare(StPptnR o1, StPptnR o2) {
                return o2.getTm().compareTo(o1.getTm());
            }
        });
        return result;
    }


    @Override
    public JSONArray futureSevenDayRainfallPredict(String longitude, String latitude) {
        //直接去四创拿数据，以后数据断了直接凉凉，赵克华和宋亚路的意思
        String result = HttpClientUtil.httpGet("https://api.caiyunapp.com/v2/KsOyzsi07D09tw80/" + longitude + "," + latitude + "/forecast?dailysteps=7");
        JSONObject jsonObject = JSON.parseObject(result);
        return jsonObject.getJSONObject("result").getJSONObject("daily").getJSONArray("precipitation");
    }

    @Override
    public JSONObject rainfallStatistic(String areaName, String endTime) {
        boolean drill = StringUtils.isBlank(endTime) ? false : true;
        endTime = StringUtils.isBlank(endTime) ? new SimpleDateFormat("yyyy-MM-dd HH:00:00").format(new Date()) : endTime;
        JSONObject result = new JSONObject();
        StationRequestVo requestVo = new StationRequestVo();
        List<AttAdcdBase> adcdList = adcdBaseMapper.selectWenZhouCounty();
        Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdnm, AttAdcdBase::getAdcd));
        Map<String, String> adnmMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        requestVo.setAreaCode(adcdMap.getOrDefault(areaName, ""));
        requestVo.setIsSurfaceRainStation(Boolean.TRUE);
//        requestVo.setStationType(com.ygkj.enums.StationType.RAINFALL.type());
        List<AttStBase> attStBases = stationMapper.selectByCondition(requestVo);
        Map<String, String> basinMap = attStBases.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getBas()))
                .collect(Collectors.toMap(AttStBase::getStCode, AttStBase::getBas));

        String adcdSuffixNum = "000000";
        Map<String, Optional<String>> stcdAdnmMap = attStBases.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getAreaCode()))
                .collect(Collectors.toMap(AttStBase::getStCode, o -> Optional.ofNullable(adnmMap.get(o.getAreaCode().substring(0, 6).concat(adcdSuffixNum)))));

        List<String> codes = attStBases.stream().map(AttStBase::getStCode).collect(Collectors.toList());
//        List<String> yjCodes = attStBases.stream().filter(attStBase -> attStBase.getStCode().startsWith("YJ_"))
//                .map(attStBase -> attStBase.getStCode().replaceAll("YJ_", "")).collect(Collectors.toList());
        int size = attStBases.size();
        String table = judgeRainfallTable(endTime);
        int[] hours = new int[]{72, 24, 6, 3, 1, 240};
        Future<List<StPptnR>>[] futures = new Future[hours.length];
        for (int i = 0; i < hours.length; i++) {
            futures[i] = rainfallStatisticExecutor
                    .submit(new RainfallStatisticTask(table, endTime, hours[i], codes, codes, basinMap, stcdAdnmMap));
        }
        List<StPptnR> newestOneHourRainfall;
        if (!drill) {
            newestOneHourRainfall = rainAndWaterMapper.selectNewestOneHourRainfall(table, endTime, codes);
//            newestOneHourRainfall.addAll(rainAndWaterMapper.selectYJNewestOneHourRainfall(endTime));
        } else {
            newestOneHourRainfall = rainAndWaterMapper.selectNewestOneHourRainfallByPastTime(table, endTime, codes);
        }
        newestOneHourRainfall.forEach(stPptnR -> stPptnR.setBas(basinMap.getOrDefault(stPptnR.getMgstcd(), StringUtils.EMPTY)));

        List<StPptnR> stPptnR72 = null, stPptnR24 = null, stPptnR6 = null, stPptnR3 = null, stPptnR1 = null, stPptnR_ten_day = null;
        try {
            stPptnR72 = futures[0].get();
            stPptnR24 = futures[1].get();
            stPptnR6 = futures[2].get();
            stPptnR3 = futures[3].get();
            stPptnR1 = futures[4].get();
            stPptnR_ten_day = futures[5].get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, List<AttStBase>> map = attStBases.stream().collect(Collectors.groupingBy(AttStBase::getStCode));

        //按照12个区县取平均值
        result.put("rainfall_newsest_1", String.format("%.2f", newestOneHourRainfall.stream().mapToDouble(StPptnR::getDrp).sum() / size));
        result.put("rainfall_1", String.format("%.2f", stPptnR1.stream().mapToDouble(StPptnR::getDrp).sum() / size));
        result.put("rainfall_3", String.format("%.2f", stPptnR3.stream().mapToDouble(StPptnR::getDrp).sum() / size));
        result.put("rainfall_6", String.format("%.2f", stPptnR6.stream().mapToDouble(StPptnR::getDrp).sum() / size));
//        result.put("rainfall_12",stPptnR12.stream().mapToDouble(StPptnR::getDrp).sum()/size);
        result.put("rainfall_24", String.format("%.2f", stPptnR24.stream().mapToDouble(StPptnR::getDrp).sum() / size));
        result.put("rainfall_72", String.format("%.2f", stPptnR72.stream().mapToDouble(StPptnR::getDrp).sum() / size));
        result.put("rainfall_ten_day", String.format("%.2f", stPptnR_ten_day.stream().mapToDouble(StPptnR::getDrp).sum() / size));
        List<StPptnR> beyond30List = stPptnR1.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 30d).collect(Collectors.toList());
        result.put("1hourBeyond30", beyond30List.size());
        result.put("1hourBeyond30List", beyond30List);
        List<StPptnR> beyond60List = stPptnR3.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 60d).collect(Collectors.toList());
        result.put("3hourBeyond60", beyond60List.size());
        result.put("3hourBeyond60List", beyond60List);
        // 05.30 新增显示降雨的点位数目
        result.put("rainfall_newsest_1_num", newestOneHourRainfall.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getMgstcd).distinct().count());
        result.put("rainfall_1_num", stPptnR1.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getMgstcd).distinct().count());
        result.put("rainfall_3_num", stPptnR3.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getMgstcd).distinct().count());
        result.put("rainfall_6_num", stPptnR6.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getMgstcd).distinct().count());
//        result.put("rainfall_12_data",combineLocation(map,stPptnR12));
        result.put("rainfall_24_num", stPptnR24.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getMgstcd).distinct().count());
        result.put("rainfall_72_num", stPptnR72.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getMgstcd).distinct().count());

        result.put("rainfall_newsest_1_data", combineLocation(map, newestOneHourRainfall));
        result.put("rainfall_1_data", combineLocation(map, stPptnR1));
        result.put("rainfall_3_data", combineLocation(map, stPptnR3));
        result.put("rainfall_6_data", combineLocation(map, stPptnR6));
//        result.put("rainfall_12_data",combineLocation(map,stPptnR12));
        result.put("rainfall_24_data", combineLocation(map, stPptnR24));
        result.put("rainfall_72_data", combineLocation(map, stPptnR72));

        int totalStationNumber = attStBases.size();
        result.put("totalStationNumber", totalStationNumber);
        long count = attStBases.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getResGrade()) &&
                attStBase.getResGrade().equals("大中型水库雨量")).count();
        result.put("specialStationNumber", count);
        result.put("basicStationNumber", totalStationNumber - count);
        result.put("hasRainfallStationNumber", newestOneHourRainfall.stream()
                .filter(stPptnR -> stPptnR.getDrp().doubleValue() > 0).count());
        result.put("excutionTime", new Date());
        //获取历史上前1、3、12、24、72小时累计雨量最大的站点 2021.10.04 --huang.zh
        result.put("rainfallHistoryMax", rainfallHistoryMax(StringUtils.EMPTY));
        return result;
    }

    private class RainfallStatisticTask implements Callable<List<StPptnR>> {
        private String table;

        private String endTime;

        private int hour;

        /**
         * 普通雨量站，SWZ_开头
         */
        private List<String> codes;
        /**
         * 永嘉雨量站，YJ_开头
         */
        private List<String> yjStationCodes;

        Map<String, String> basinMap;

        Map<String, Optional<String>> stcdAdnmMap;

        public RainfallStatisticTask(String table, String endTime,
                                     int hour, List<String> codes, List<String> yjStationCodes,
                                     Map<String, String> basinMap, Map<String, Optional<String>> stcdAdnmMap) {
            this.table = table;
            this.endTime = endTime;
            this.hour = hour;
            this.codes = codes;
            this.yjStationCodes = yjStationCodes;
            this.basinMap = basinMap;
            this.stcdAdnmMap = stcdAdnmMap;
        }

        @Override
        public List<StPptnR> call() throws Exception {
            long millis = System.currentTimeMillis();
            List<StPptnR> rainfalls = rainAndWaterMapper.selectPastHoursRainfall(table, endTime, hour, codes, Boolean.TRUE);
//            rainfalls.addAll(rainAndWaterMapper.selectYJPastHoursRainfall(endTime, hour, yjStationCodes));
            rainfalls.forEach(stPptnR -> stPptnR.setBas(basinMap.getOrDefault(stPptnR.getMgstcd(), StringUtils.EMPTY)));
            rainfalls.forEach(stPptnR -> stPptnR.setAreaName(stcdAdnmMap.getOrDefault(stPptnR.getMgstcd(), Optional.empty()).orElse(StringUtils.EMPTY)));
            log.info(Thread.currentThread().getName().concat("----" + hour).concat(":" + (System.currentTimeMillis() - millis)));
            return rainfalls;
        }
    }

    /**
     * 获取历史上前1、3、12、24、72小时累计雨量最大的站点
     *
     * @return
     */
    public JSONArray rainfallHistoryMax(String stationCode) {
        List<Map<String, Object>> histories = stationMapper.rainfallHistoryMax(stationCode);
        Map<String, Map<String, Object>> historyMap = histories.stream().collect(Collectors.toMap(map -> map.get("during_unit").toString(), Function.identity()));
        String[] units = new String[]{"1小时", "3小时", "6小时", "24小时", "72小时"};
        Set<String> set = Arrays.asList(units).stream().map(s -> s).collect(Collectors.toSet());
        List<Map<String, Object>> list = historyMap.entrySet().stream().filter(entry -> set.contains(entry.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
        JSONArray array = new JSONArray();
        list.forEach(map -> array.add(new JSONObject(map)));
        return array;
    }

    @Override
    public RainfallSumResVo rainfall24(String stCode) {
        RainfallSumResVo rainfallSumResVo = new RainfallSumResVo();
        RainfallQueryVo queryVo = new RainfallQueryVo();
        queryVo.setStationCode(stCode);
        Date now = new Date();
        queryVo.setStart(new Date(now.getTime() - 24 * 60 * 60 * 1000));
        queryVo.setEnd(now);
        BigDecimal end = new BigDecimal(now.getTime());
        BigDecimal unit = new BigDecimal(60 * 60 * 1000);
        List<String> tables = new ArrayList<>();
        String table = StationType.RAINFALL.table();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String startStr = sdf.format(queryVo.getStart());
        String endStr = sdf.format(queryVo.getEnd());
        tables.add(table.concat(startStr));
        if (!startStr.equals(endStr)) {
            tables.add(table.concat(endStr));
        }
        List<StPptnR> list = rainAndWaterMapper.selectRainfallByCondition(tables, queryVo);
        for (StPptnR stPptnR : list) {
            Double drp = stPptnR.getDrp();
            if(drp == 0){
                continue;
            }
            Date tm = stPptnR.getTm();
            int hour = getDif(tm, end, unit);
            if (hour <= 3) {
                rainfallSumResVo.setR3(rainfallSumResVo.getR3() + drp);
            } else if (hour <= 6) {
                rainfallSumResVo.setR6(rainfallSumResVo.getR6() + drp);
            } else if (hour <= 12) {
                rainfallSumResVo.setR12(rainfallSumResVo.getR12() + drp);
            } else {
                rainfallSumResVo.setR24(rainfallSumResVo.getR24() + drp);
            }
        }
        rainfallSumResVo.setR6(rainfallSumResVo.getR6() + rainfallSumResVo.getR3());
        rainfallSumResVo.setR12(rainfallSumResVo.getR12() + rainfallSumResVo.getR6());
        rainfallSumResVo.setR24(rainfallSumResVo.getR24() + rainfallSumResVo.getR12());
        return rainfallSumResVo;
    }

    private static int getDif(Date tm, BigDecimal end,BigDecimal unit) {
        BigDecimal start = new BigDecimal(tm.getTime());
        return end.subtract(start).divide(unit,0,BigDecimal.ROUND_CEILING).intValue();
    }

    /**
     * 大屏上降雨统计，用于演练
     *
     * @param stcds
     * @param startTime
     * @param endTime
     * @return
     */
    public JSONObject rainfallStatistic4Drill(String stcds, String startTime, String endTime) {
        boolean drill = StringUtils.isBlank(endTime) ? false : true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        endTime = StringUtils.isBlank(endTime) ? sdf.format(new Date()) : endTime;
        JSONObject result = new JSONObject();
        StationRequestVo requestVo = new StationRequestVo();
        List<AttAdcdBase> adcdList = adcdBaseMapper.selectWenZhouCounty();
        Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdnm, AttAdcdBase::getAdcd));
//        requestVo.setAreaCode(adcdMap.getOrDefault(areaName, ""));
        requestVo.setIsSurfaceRainStation(Boolean.TRUE);
//        requestVo.setStationType(com.ygkj.enums.StationType.RAINFALL.type());
        requestVo.setStationCodes(stcds);
        List<AttStBase> attStBases = stationMapper.selectByCondition(requestVo);
        List<String> codes = attStBases.stream().filter(attStBase -> attStBase.getStCode().startsWith("SWZ_"))
                .map(AttStBase::getStCode).collect(Collectors.toList());
        List<String> yjCodes = attStBases.stream().filter(attStBase -> attStBase.getStCode().startsWith("YJ_"))
                .map(attStBase -> attStBase.getStCode().replaceAll("YJ_", "")).collect(Collectors.toList());
        int size = attStBases.size();
        String table = judgeRainfallTable(endTime);
        List<String> tables = Arrays.asList(DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(), startTime, endTime));
        Date startTimeGate = null;
        Date endTimeDate = null;
        String start72 = null;
        String start24 = null;
        String start6 = null;
        String start3 = null;
        String start1 = null;
        try {
            startTimeGate = sdf.parse(startTime);
            endTimeDate = sdf.parse(endTime);
            Date temp = new Date(endTimeDate.getTime() - 72 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start72 = startTime;
            } else {
                start72 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 24 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start24 = startTime;
            } else {
                start24 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 6 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start6 = startTime;
            } else {
                start6 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 3 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start3 = startTime;
            } else {
                start3 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 1 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start1 = startTime;
            } else {
                start1 = sdf.format(temp);
            }
        } catch (Exception e) {

        }
        List<StPptnR> stPptnR72 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, start72, endTime, codes);
        stPptnR72.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(start72, endTime, yjCodes));
        List<StPptnR> stPptnR24 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, start24, endTime, codes);
        stPptnR24.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(start24, endTime, yjCodes));
        List<StPptnR> stPptnR6 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, start6, endTime, codes);
        stPptnR6.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(start6, endTime, yjCodes));
        List<StPptnR> stPptnR3 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, start3, endTime, codes);
        stPptnR3.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(start3, endTime, yjCodes));
        List<StPptnR> stPptnR1 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, start1, endTime, codes);
        stPptnR1.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(start1, endTime, yjCodes));
        Date finalEndTimeDate = endTimeDate;
        List<StPptnR> newestOneHourRainfall = attStBases.stream()
                .map(o -> Builder.of(StPptnR::new)
                        .with(StPptnR::setMgstcd, o.getStCode())
                        .with(StPptnR::setStcd, o.getStCode())
                        .with(StPptnR::setTm, finalEndTimeDate)
                        .with(StPptnR::setDrp, 0D)
                        .build())
                .collect(Collectors.toList());
        Map<String, List<AttStBase>> map = attStBases.stream().collect(Collectors.groupingBy(AttStBase::getStCode));

        //按照12个区县取平均值
        result.put("rainfall_newsest_1", 0);
        result.put("rainfall_1", String.format("%.2f", stPptnR1.stream().mapToDouble(StPptnR::getDrp).sum() / size));
        result.put("rainfall_3", String.format("%.2f", stPptnR3.stream().mapToDouble(StPptnR::getDrp).sum() / size));
        result.put("rainfall_6", String.format("%.2f", stPptnR6.stream().mapToDouble(StPptnR::getDrp).sum() / size));
//        result.put("rainfall_12",stPptnR12.stream().mapToDouble(StPptnR::getDrp).sum()/size);
        result.put("rainfall_24", String.format("%.2f", stPptnR24.stream().mapToDouble(StPptnR::getDrp).sum() / size));
        result.put("rainfall_72", String.format("%.2f", stPptnR72.stream().mapToDouble(StPptnR::getDrp).sum() / size));
        result.put("1hourBeyond30", stPptnR1.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 30d).count());
        result.put("3hourBeyond60", stPptnR3.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 60d).count());
        result.put("rainfall_newsest_1_data", combineLocation(map, newestOneHourRainfall));
        result.put("rainfall_1_data", combineLocation(map, stPptnR1));
        result.put("rainfall_3_data", combineLocation(map, stPptnR3));
        result.put("rainfall_6_data", combineLocation(map, stPptnR6));
//        result.put("rainfall_12_data",combineLocation(map,stPptnR12));
        result.put("rainfall_24_data", combineLocation(map, stPptnR24));
        result.put("rainfall_72_data", combineLocation(map, stPptnR72));

        // 05.30 新增显示降雨的点位数目
        result.put("rainfall_newsest_1_num", 0);
        result.put("rainfall_1_num", stPptnR1.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getStcd).distinct().count());
        result.put("rainfall_3_num", stPptnR3.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getStcd).distinct().count());
        result.put("rainfall_6_num", stPptnR6.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getStcd).distinct().count());
//        result.put("rainfall_12_data",combineLocation(map,stPptnR12));
        result.put("rainfall_24_num", stPptnR24.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getStcd).distinct().count());
        result.put("rainfall_72_num", stPptnR72.stream().filter(o -> o.getDrp() != null && o.getDrp() > 0).map(StPptnR::getStcd).distinct().count());
        int totalStationNumber = attStBases.size();
        result.put("totalStationNumber", totalStationNumber);
        long count = attStBases.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getResGrade()) &&
                attStBase.getResGrade().equals("大中型水库雨量")).count();
        result.put("specialStationNumber", count);
        result.put("basicStationNumber", totalStationNumber - count);
        result.put("hasRainfallStationNumber", newestOneHourRainfall.stream()
                .filter(stPptnR -> stPptnR.getDrp().doubleValue() > 0).count());
        result.put("excutionTime", new Date());
        return result;
    }

    private List<StPptnR> combineLocation(Map<String, List<AttStBase>> map, List<StPptnR> list) {
//        for (StPptnR stPptnR : list) {
//            String stationCode = stPptnR.getMgstcd();
//            if (!stationCode.startsWith("SWZ_")){
//                stationCode = "YJ_".concat(stationCode);
//            }
//            if (map.containsKey(stationCode)) {
//                AttStBase attStBase = map.get(stationCode).get(0);
//                stPptnR.setLongitude(Optional.ofNullable(attStBase.getStLong()).orElse(new BigDecimal(0)).doubleValue());
//                stPptnR.setLatitude(Optional.ofNullable(attStBase.getStLat()).orElse(new BigDecimal(0)).doubleValue());
//                stPptnR.setStationName(attStBase.getStName());
//                stPptnR.setStLoc(attStBase.getStLoc());
//            }
//        }
        Map<String, StPptnR> rainfallMap = list.stream().collect(Collectors.toMap(StPptnR::getMgstcd, stPptnR -> stPptnR));
        map.entrySet().forEach((entry -> {
            String stationCode = entry.getKey();
            AttStBase attStBase = map.get(stationCode).get(0);
            if (rainfallMap.containsKey(stationCode)) {
                StPptnR stPptnR = rainfallMap.get(stationCode);
                stPptnR.setLongitude(Optional.ofNullable(attStBase.getStLong()).orElse(new BigDecimal(0)).doubleValue());
                stPptnR.setLatitude(Optional.ofNullable(attStBase.getStLat()).orElse(new BigDecimal(0)).doubleValue());
                stPptnR.setStationName(attStBase.getStName());
                stPptnR.setStLoc(attStBase.getStLoc());
                stPptnR.setAreaCode(attStBase.getAreaCode());
            } else {
                StPptnR rainfall = new StPptnR();
                rainfall.setMgstcd(attStBase.getStCode());
                rainfall.setStcd(attStBase.getStCode());
                rainfall.setLatitude(attStBase.getStLat() != null ? attStBase.getStLat().doubleValue() : new BigDecimal(0).doubleValue());
                rainfall.setLongitude(attStBase.getStLong() != null ? attStBase.getStLong().doubleValue() : new BigDecimal(0).doubleValue());
                rainfall.setDrp(0d);
                rainfall.setStLoc(attStBase.getStLoc());
                rainfall.setStationName(attStBase.getStName());
                rainfall.setAreaCode(attStBase.getAreaCode());
                list.add(rainfall);
            }
        }));
        return list;
    }

    @Override
    public JSONArray pastHoursRainfall() {
        String table = judgeRainfallTable();
        List<AttAdcdBase> adcdList = adcdBaseMapper.selectWenZhouCounty();
        Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        List<AttStBase> attStBases = stationMapper.selectByCondition(new StationRequestVo());
        List<StPptnR> stPptnRs = rainAndWaterMapper.selectStationPastHoursTotalRainfall(new Date(), table);
        Map<String, List<AttStBase>> map = attStBases.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getAreaCode())).collect(Collectors.groupingBy(AttStBase::getAreaCode));
        return concatSurfaceData(stPptnRs, map, adcdMap);
    }

    @Override
    public JSONObject grandTotalRainfallSurface(String areaName, String endTime) {
        JSONObject result = new JSONObject();
        List<AttAdcdBase> adcdList = adcdBaseMapper.selectWenZhouCounty();
        Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        StationRequestVo requestVo = Builder.of(StationRequestVo::new)
                .with(StationRequestVo::setAreaCode, adcdMap.getOrDefault(areaName, "")).build();
        List<AttStBase> attStBases = stationMapper.selectByCondition(requestVo);
        String table = judgeRainfallTable();
        return result;
    }

    @Override
    public JSONObject grandTotalMaxRainfallStatistic(String endTime, Integer hour) {
        Boolean drill = StringUtils.isNotBlank(endTime);
        endTime = StringUtils.isBlank(endTime) ? new SimpleDateFormat("yyyy-MM-dd HH:00:00").format(new Date()) : endTime;
        String table = judgeRainfallTable(endTime);
        //累计最大降雨的行政区划
        StationRequestVo requestVo = new StationRequestVo();
        requestVo.setIsRainStation(Boolean.TRUE);
//        requestVo.setStationType(com.ygkj.enums.StationType.RAINFALL.type());
        List<AttStBase> stations = stationMapper.selectByCondition(requestVo);
        List<String> codes = stations.stream().map(AttStBase::getStCode).collect(Collectors.toList());
//        List<String> yjCodes = stations.stream().filter(attStBase -> attStBase.getStCode().startsWith("YJ_"))
//                .map(attStBase -> attStBase.getStCode().replaceAll("YJ_", "")).collect(Collectors.toList());
        StPptnR stPptnR = hour == 0 ? rainAndWaterMapper.selectNewestOneHourMaxRainfall(table, endTime, codes, drill)
                : rainAndWaterMapper.selectMaxRainfallBetweenPastHours(table, endTime, hour, codes);
        JSONObject result = new JSONObject();
        if (stPptnR != null) {
            AttStBase station = stationMapper.load(stPptnR.getMgstcd());
            result.put("maxPointName", station.getStName());
            result.put("maxPointStaInfo", station);
            result.put("maxPointRainfall", stPptnR.getDrp().doubleValue());
        } else {
            result.put("maxPointName", "-");
            result.put("maxPointStaInfo", "-");
            result.put("maxPointRainfall", "-");
        }

        List<StPptnR> rainfalls = hour == 0 ? rainAndWaterMapper.selectNewestOneHourRainfallByPastTime(table, drill ? endTime : new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(new Date()), codes)
                : rainAndWaterMapper.selectPastHoursRainfall(table, endTime, hour, codes, Boolean.TRUE);
//        List<StPptnR> rainfallYJ = hour == 0 ? rainAndWaterMapper.selectYJNewestOneHourRainfall(endTime) : rainAndWaterMapper.selectYJPastHoursRainfall(endTime, hour, yjCodes);
        Map<String, Double> rainfallMap = rainfalls.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
//        Map<String, Double> rainfallMapYJ = rainfallYJ.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        for (AttStBase attStBase : stations) {
            attStBase.setRainfall(rainfallMap.getOrDefault(attStBase.getStCode(), 0d).doubleValue());
//            if (attStBase.getStCode().startsWith("SWZ_")) {
//            }
//            else {
//                //组装永嘉的数据
//                attStBase.setRainfall(rainfallMapYJ.getOrDefault(attStBase.getStCode(), 0d).doubleValue());
//            }
        }

        List<AttAdcdBase> adcdList = adcdBaseMapper.selectWenZhouCounty();
        Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        Map<String, DoubleSummaryStatistics> map = stations.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getAreaCode()))
                .collect(Collectors.groupingBy(AttStBase::getAreaCode, Collectors.summarizingDouble(AttStBase::getRainfall)));
//        List<Map.Entry<String, DoubleSummaryStatistics>> entriesByAreaCode = map.entrySet().stream().sorted(new Comparator<Map.Entry<String, DoubleSummaryStatistics>>() {
//            @Override
//            public int compare(Map.Entry<String, DoubleSummaryStatistics> o1, Map.Entry<String, DoubleSummaryStatistics> o2) {
//                return (int) (o1.getValue().getAverage() - o2.getValue().getAverage());
//            }
//        }).collect(Collectors.toList());
//        result.put("maxRainfallAreaName",adcdMap.getOrDefault(entriesByAreaCode.get(entriesByAreaCode.size()-1).getKey(),""));
//        result.put("areaMaxRainfall",entriesByAreaCode.get(entriesByAreaCode.size()-1).getValue().getAverage());
        JSONArray areaRainfallChart = new JSONArray();
        map.forEach((areaCode, statistic) -> {
            JSONObject object = new JSONObject();
            object.put("key", adcdMap.getOrDefault(areaCode, ""));
            object.put("value", statistic.getAverage());
            areaRainfallChart.add(object);
        });
        List<Object> list = areaRainfallChart.stream()
                .sorted(Comparator.comparing(o -> ((JSONObject) o).getDoubleValue("value")).reversed())
                .collect(Collectors.toList());
        result.put("areaRainfallChart", list);
        JSONObject jsonObject = (JSONObject) list.get(0);
        result.put("maxRainfallAreaName", jsonObject.get("key"));
        result.put("areaMaxRainfall", jsonObject.get("value"));
        //累计最大降雨的流域
        map = stations.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getBas()))
                .collect(Collectors.groupingBy(AttStBase::getBas, Collectors.summarizingDouble(AttStBase::getRainfall)));
//        List<Map.Entry<String, DoubleSummaryStatistics>> entriesByBasin = map.entrySet().stream().sorted(new Comparator<Map.Entry<String, DoubleSummaryStatistics>>() {
//            @Override
//            public int compare(Map.Entry<String, DoubleSummaryStatistics> o1, Map.Entry<String, DoubleSummaryStatistics> o2) {
//                return (int) (o1.getValue().getAverage() - o2.getValue().getAverage());
//            }
//        }).collect(Collectors.toList());
//        result.put("maxRainfallBasinName",entriesByBasin.get(entriesByBasin.size()-1).getKey());
//        result.put("basinMaxRainfall",entriesByBasin.get(entriesByBasin.size()-1).getValue().getAverage());
        JSONArray basinRainfallChart = new JSONArray();
        map.forEach((basin, statistic) -> {
            JSONObject object = new JSONObject();
            object.put("key", basin);
            object.put("value", statistic.getAverage());
            basinRainfallChart.add(object);
        });
        List<Object> basinList = basinRainfallChart.stream()
                .sorted(Comparator.comparing(o -> ((JSONObject) o).getDoubleValue("value")).reversed())
                .collect(Collectors.toList());
        jsonObject = (JSONObject) basinList.get(0);
        result.put("basinRainfallChart", basinList);
        result.put("maxRainfallBasinName", jsonObject.get("key"));
        result.put("basinMaxRainfall", jsonObject.get("value"));
        result.put("excutionTime", new Date());
        return result;
    }


    @Override
    public JSONObject grandTotalMaxRainfallStatistic4Drill(String stcds, String startTime, String endTime, Integer hour) {
        Boolean drill = StringUtils.isNoneBlank(endTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        endTime = StringUtils.isBlank(endTime) ? sdf.format(new Date()) : endTime;
        String table = judgeRainfallTable(endTime);
        List<String> tables = Arrays.asList(DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(), startTime, endTime));
        Date startTimeGate = null;
        Date endTimeDate = null;
        String start72 = null;
        String start24 = null;
        String start6 = null;
        String start3 = null;
        String start1 = null;
        try {
            startTimeGate = sdf.parse(startTime);
            endTimeDate = sdf.parse(endTime);
            Date temp = new Date(endTimeDate.getTime() - 72 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start72 = startTime;
            } else {
                start72 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 24 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start24 = startTime;
            } else {
                start24 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 6 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start6 = startTime;
            } else {
                start6 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 3 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start3 = startTime;
            } else {
                start3 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 1 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start1 = startTime;
            } else {
                start1 = sdf.format(temp);
            }
        } catch (Exception e) {

        }
        //累计最大降雨的行政区划
        StationRequestVo requestVo = new StationRequestVo();
        requestVo.setIsRainStation(Boolean.TRUE);
//        requestVo.setStationType(com.ygkj.enums.StationType.RAINFALL.type());
        requestVo.setStationCodes(stcds);
        List<AttStBase> stations = stationMapper.selectByCondition(requestVo);
        List<String> codes = stations.stream().filter(attStBase -> attStBase.getStCode().startsWith("SWZ_"))
                .map(AttStBase::getStCode).collect(Collectors.toList());
        List<String> yjCodes = stations.stream().filter(attStBase -> attStBase.getStCode().startsWith("YJ_"))
                .map(attStBase -> attStBase.getStCode().replaceAll("YJ_", "")).collect(Collectors.toList());
        StPptnR stPptnR = hour == 0 ? rainAndWaterMapper.selectNewestOneHourMaxRainfall(table, endTime, codes, drill)
                : rainAndWaterMapper.selectMaxRainfallBetweenPastHours(table, endTime, hour, codes);
        if (hour == 0) {
            stPptnR.setDrp(0d);
        }
        JSONObject result = new JSONObject();
        if (stPptnR != null) {
            AttStBase station = stationMapper.load(stPptnR.getMgstcd());
            result.put("maxPointName", station.getStName());
            result.put("maxPointStaInfo", station);
            result.put("maxPointRainfall", stPptnR.getDrp().doubleValue());
        } else {
            result.put("maxPointName", "-");
            result.put("maxPointStaInfo", "-");
            result.put("maxPointRainfall", "-");
        }

        Date finalEndTimeDate = endTimeDate;
        List<StPptnR> rainfalls = hour == 0 ? codes.stream()
                .map(o -> Builder.of(StPptnR::new)
                        .with(StPptnR::setMgstcd, o)
                        .with(StPptnR::setStcd, o)
                        .with(StPptnR::setTm, finalEndTimeDate)
                        .with(StPptnR::setDrp, 0D)
                        .build())
                .collect(Collectors.toList())
                : rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, startTime, endTime, codes);
        List<StPptnR> rainfallYJ = hour == 0 ? yjCodes.stream()
                .map(o -> Builder.of(StPptnR::new)
                        .with(StPptnR::setMgstcd, o)
                        .with(StPptnR::setStcd, o)
                        .with(StPptnR::setTm, finalEndTimeDate)
                        .with(StPptnR::setDrp, 0D)
                        .build())
                .collect(Collectors.toList()) : rainAndWaterMapper.selectYjSumRainfallByTimeInterval(startTime, endTime, yjCodes);
        Map<String, Double> rainfallMap = rainfalls.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        Map<String, Double> rainfallMapYJ = rainfallYJ.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        for (AttStBase attStBase : stations) {
            if (attStBase.getStCode().startsWith("SWZ_")) {
                attStBase.setRainfall(rainfallMap.getOrDefault(attStBase.getStCode(), 0d).doubleValue());
            } else {
                //组装永嘉的数据
                attStBase.setRainfall(rainfallMapYJ.getOrDefault(attStBase.getStCode(), 0d).doubleValue());
            }
        }
        List<AttAdcdBase> adcdList = adcdBaseMapper.selectWenZhouCounty();
        Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        Map<String, DoubleSummaryStatistics> map = stations.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getAreaCode()))
                .collect(Collectors.groupingBy(AttStBase::getAreaCode, Collectors.summarizingDouble(AttStBase::getRainfall)));
//        List<Map.Entry<String, DoubleSummaryStatistics>> entriesByAreaCode = map.entrySet().stream().sorted(new Comparator<Map.Entry<String, DoubleSummaryStatistics>>() {
//            @Override
//            public int compare(Map.Entry<String, DoubleSummaryStatistics> o1, Map.Entry<String, DoubleSummaryStatistics> o2) {
//                return (int) (o1.getValue().getAverage() - o2.getValue().getAverage());
//            }
//        }).collect(Collectors.toList());
//        result.put("maxRainfallAreaName",adcdMap.getOrDefault(entriesByAreaCode.get(entriesByAreaCode.size()-1).getKey(),""));
//        result.put("areaMaxRainfall",entriesByAreaCode.get(entriesByAreaCode.size()-1).getValue().getAverage());
        JSONArray areaRainfallChart = new JSONArray();
        map.forEach((areaCode, statistic) -> {
            JSONObject object = new JSONObject();
            object.put("key", adcdMap.getOrDefault(areaCode, ""));
            object.put("value", statistic.getAverage());
            areaRainfallChart.add(object);
        });
        List<Object> list = areaRainfallChart.stream()
                .sorted(Comparator.comparing(o -> ((JSONObject) o).getDoubleValue("value")).reversed())
                .collect(Collectors.toList());
        result.put("areaRainfallChart", list);
        JSONObject jsonObject = (JSONObject) list.get(0);
        result.put("maxRainfallAreaName", jsonObject.get("key"));
        result.put("areaMaxRainfall", jsonObject.get("value"));
        //累计最大降雨的流域
        map = stations.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getBas()))
                .collect(Collectors.groupingBy(AttStBase::getBas, Collectors.summarizingDouble(AttStBase::getRainfall)));
//        List<Map.Entry<String, DoubleSummaryStatistics>> entriesByBasin = map.entrySet().stream().sorted(new Comparator<Map.Entry<String, DoubleSummaryStatistics>>() {
//            @Override
//            public int compare(Map.Entry<String, DoubleSummaryStatistics> o1, Map.Entry<String, DoubleSummaryStatistics> o2) {
//                return (int) (o1.getValue().getAverage() - o2.getValue().getAverage());
//            }
//        }).collect(Collectors.toList());
//        result.put("maxRainfallBasinName",entriesByBasin.get(entriesByBasin.size()-1).getKey());
//        result.put("basinMaxRainfall",entriesByBasin.get(entriesByBasin.size()-1).getValue().getAverage());
        JSONArray basinRainfallChart = new JSONArray();
        map.forEach((basin, statistic) -> {
            JSONObject object = new JSONObject();
            object.put("key", basin);
            object.put("value", statistic.getAverage());
            basinRainfallChart.add(object);
        });
        List<Object> basinList = basinRainfallChart.stream()
                .sorted(Comparator.comparing(o -> ((JSONObject) o).getDoubleValue("value")).reversed())
                .collect(Collectors.toList());
        jsonObject = (JSONObject) basinList.get(0);
        result.put("basinRainfallChart", basinList);
        result.put("maxRainfallBasinName", jsonObject.get("key"));
        result.put("basinMaxRainfall", jsonObject.get("value"));
        return result;
    }

    @Override
    public JSONArray grandTotalRainfallGrid(SynRainRequestVo requestVo, String endTime, String type, String surfaceType, Integer hour) {
        if (Arrays.stream(RainfallSurfaceType.values()).noneMatch(rainfallSurfaceType -> rainfallSurfaceType.type().equals(type))) {
            throw new IllegalArgumentException("统计类型不正确！");
        }
        if (hour == null) {
            //默认近一小时
            hour = new Integer(0);
        }
        if (StringUtils.isNotBlank(surfaceType) &&
                Arrays.stream(RainfallGridStatisticType.values())
                        .noneMatch(rainfallGridStatisticType -> rainfallGridStatisticType.type().equals(surfaceType))) {
            throw new IllegalArgumentException("面雨量统计类型不正确！");
        }
        endTime = StringUtils.isBlank(endTime) ? new SimpleDateFormat("yyyy-MM-dd HH:00:00").format(new Date()) : endTime;
        JSONArray result = new JSONArray();
        StationRequestVo stationRequestVo = new StationRequestVo();
        stationRequestVo.setAreaCode(requestVo.getAreaCode());
        stationRequestVo.setBas(requestVo.getBas());
        if (requestVo.getIsSurfaceRainStation()) {
            //查770个
            stationRequestVo.setIsSurfaceRainStation(requestVo.getIsSurfaceRainStation());
        } else {
            //查1211个
            stationRequestVo.setIsRainStation(Boolean.TRUE);
        }
        stationRequestVo.setStationName(requestVo.getStationName());
//        stationRequestVo.setStationType(com.ygkj.enums.StationType.RAINFALL.type());
        //2021.06.07 新增分页逻辑，解决数据加载太慢的问题
        if (requestVo.getPageNum() != null && requestVo.getPageNum() != 0 &&
                requestVo.getPageSize() != null && requestVo.getPageSize() != 0) {
            PageHelper.startPage(requestVo.getPageNum(), requestVo.getPageSize());
        }
        List<AttStBase> stations = stationMapper.selectByCondition(stationRequestVo);
        String table = judgeRainfallTable();
        List<String> codes = stations.stream().map(AttStBase::getStCode).collect(Collectors.toList());
//        List<String> swzCodes = codes.stream().collect(Collectors.toList());
//        List<String> yjCodes = codes.stream().filter(stationCode -> stationCode.startsWith("YJ_"))
//                .map(code -> code.replaceAll("YJ_", StringUtils.EMPTY)).collect(Collectors.toList());
        Map<String, Double> dataMap;
        if (hour.intValue() == 0) {
            //近一小时
            List<StPptnR> rainfall = rainAndWaterMapper.selectNewestOneHourRainfall(table, endTime, codes);
//            rainfall.addAll(rainAndWaterMapper.selectYJNewestOneHourRainfall(endTime));
            dataMap = rainfall
                    .stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        } else {
            //前N小时
            List<StPptnR> rainfall = rainAndWaterMapper.selectPastHoursRainfall(table, endTime, hour.intValue(), codes, Boolean.FALSE);
//            rainfall.addAll(rainAndWaterMapper.selectYJPastHoursRainfall(endTime, hour, yjCodes));
            dataMap = rainfall.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        }
        String key;
        if (hour.intValue() == 0) {
            //近一小时
            key = "newest1hour";
        } else {
            key = String.valueOf(hour.intValue()).concat("hour");
        }
        if (type.equals(RainfallSurfaceType.POINT.type())) {
            for (AttStBase station : stations) {
                JSONObject object = new JSONObject();
                String code = station.getStCode();
                object.put("name", station.getStName());
                object.put(key, dataMap.getOrDefault(code, 0d));
                result.add(object);
            }
        } else if (type.equals(RainfallSurfaceType.SURFACE.type()) &&
                surfaceType.equals(RainfallGridStatisticType.ADMINISTRATIVE_DiVISIONS.type())) {
            //统计面雨量
            List<AttAdcdBase> adcdList = adcdBaseMapper.selectWenZhouCounty();
            Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
            Map<String, List<AttStBase>> areaCodeMap = stations.stream()
                    .filter(attStBase -> StringUtils.isNotBlank(attStBase.getAreaCode()))
                    .collect(Collectors.groupingBy(AttStBase::getAreaCode));
            List<Map.Entry<String, List<AttStBase>>> entries = areaCodeMap.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).collect(Collectors.toList());
            for (Map.Entry<String, List<AttStBase>> e : entries) {
                String areaCode = e.getKey();
                List<AttStBase> list = e.getValue();
                JSONObject object = new JSONObject();
                object.put("name", adcdMap.getOrDefault(areaCode, StringUtils.EMPTY));
                List<String> stationCodes = list.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                int size = stationCodes.size();
                object.put(key, dataMap.entrySet().stream()
                        .filter(entry -> stationCodes.contains(entry.getKey())).mapToDouble(entry -> entry.getValue().doubleValue()).sum() / size);
                result.add(object);
            }
        } else {
            Map<String, List<AttStBase>> basinMap = stations.stream()
                    .filter(attStBase -> StringUtils.isNotBlank(attStBase.getBas()))
                    .collect(Collectors.groupingBy(AttStBase::getBas));
            basinMap.forEach((basin, list) -> {
                JSONObject object = new JSONObject();
                object.put("name", basin);
                List<String> stationCodes = list.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                int size = stationCodes.size();
                object.put(key, dataMap.entrySet().stream()
                        .filter(entry -> stationCodes.contains(entry.getKey())).mapToDouble(entry -> entry.getValue().doubleValue()).sum() / size);
                result.add(object);
            });
        }
        return result;
    }

    @Override
    public JSONArray grandTotalReservoirRainfall(String endTime, Integer hour) {
        endTime = StringUtils.isBlank(endTime) ? new SimpleDateFormat("yyyy-MM-dd HH:00:00").format(new Date()) : endTime;
        JSONArray result = new JSONArray();
        List<AttResBase> reservoirs = projectMapper.selectBigAndMiddleReservoir();
        Map<String, String> reservoirMap = reservoirs.stream().collect(Collectors.toMap(AttResBase::getResCode, AttResBase::getResName));
        StationRequestVo vo = new StationRequestVo();
        vo.setIsReservoirSurfaceRainStation(Boolean.TRUE);
//        vo.setResGrade("大中型水库雨量");
//        vo.setIsRainStation(Boolean.TRUE);
//        vo.setStationType(com.ygkj.enums.StationType.RAINFALL.type());
        List<AttStBase> stations = stationMapper.selectByCondition(vo);
        List<String> codes = stations.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        String table = judgeRainfallTable(endTime);
        List<StPptnR> rainfalls = hour == 0 ? rainAndWaterMapper.selectNewestOneHourRainfall(table, endTime, codes)
                : rainAndWaterMapper.selectPastHoursRainfall(table, endTime, hour, codes, Boolean.TRUE);
        Map<String, Double> rainfallMap = rainfalls.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        Map<String, List<AttStBase>> stationMap = stations.stream()
                .filter(attStBase -> StringUtils.isNotBlank(attStBase.getProjCode())).
                        collect(Collectors.groupingBy(AttStBase::getProjCode));
        stationMap.forEach((projectCode, stationList) -> {
            if (reservoirMap.containsKey(projectCode)) {
                JSONObject object = new JSONObject();
                object.put("key", reservoirMap.get(projectCode));
                List<String> codeList = stationList.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                double rainfall = rainfallMap.entrySet().stream().filter(entry -> codeList.contains(entry.getKey()))
                        .mapToDouble(entry -> entry.getValue().doubleValue())
                        .sum();
                object.put("excutionTime", new Date());
                object.put("value", rainfall / stationList.size());
                result.add(object);
            }
        });

        return result;
    }

    public JSONArray grandTotalReservoirRainfall4Drill(String stcds, String startTime, String endTime, Integer hour) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        endTime = StringUtils.isBlank(endTime) ? sdf.format(new Date()) : endTime;
        JSONArray result = new JSONArray();
        List<AttResBase> reservoirs = projectMapper.selectBigAndMiddleReservoir();
        Map<String, String> reservoirMap = reservoirs.stream().collect(Collectors.toMap(AttResBase::getResCode, AttResBase::getResName));
        StationRequestVo vo = new StationRequestVo();
        vo.setIsReservoirSurfaceRainStation(Boolean.TRUE);
//        vo.setResGrade("大中型水库雨量");
//        vo.setIsRainStation(Boolean.TRUE);
//        vo.setStationType(com.ygkj.enums.StationType.RAINFALL.type());
        vo.setStationCodes(stcds);
        List<AttStBase> stations = stationMapper.selectByCondition(vo);
        List<String> codes = stations.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        String table = judgeRainfallTable(endTime);
        List<String> tables = Arrays.asList(DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(), startTime, endTime));
        Date startTimeGate = null;
        Date endTimeDate = null;
        String start72 = null;
        String start24 = null;
        String start6 = null;
        String start3 = null;
        String start1 = null;
        try {
            startTimeGate = sdf.parse(startTime);
            endTimeDate = sdf.parse(endTime);
            Date temp = new Date(endTimeDate.getTime() - 72 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start72 = startTime;
            } else {
                start72 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 24 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start24 = startTime;
            } else {
                start24 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 6 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start6 = startTime;
            } else {
                start6 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 3 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start3 = startTime;
            } else {
                start3 = sdf.format(temp);
            }
            temp = new Date(endTimeDate.getTime() - 3 * 3600 * 1000);
            if (temp.before(startTimeGate)) {
                start1 = startTime;
            } else {
                start1 = sdf.format(temp);
            }
        } catch (Exception e) {

        }
        Date finalEndTimeDate = endTimeDate;
        List<StPptnR> rainfalls = hour == 0 ? stations.stream()
                .map(o -> Builder.of(StPptnR::new)
                        .with(StPptnR::setMgstcd, o.getStCode())
                        .with(StPptnR::setStcd, o.getStCode())
                        .with(StPptnR::setTm, finalEndTimeDate)
                        .with(StPptnR::setDrp, 0D)
                        .build())
                .collect(Collectors.toList())
                : rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, startTime, endTime, codes);
        Map<String, Double> rainfallMap = rainfalls.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        Map<String, List<AttStBase>> stationMap = stations.stream()
                .filter(attStBase -> StringUtils.isNotBlank(attStBase.getProjCode())).
                        collect(Collectors.groupingBy(AttStBase::getProjCode));
        stationMap.forEach((projectCode, stationList) -> {
            if (reservoirMap.containsKey(projectCode)) {
                JSONObject object = new JSONObject();
                object.put("key", reservoirMap.get(projectCode));
                List<String> codeList = stationList.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                double rainfall = rainfallMap.entrySet().stream().filter(entry -> codeList.contains(entry.getKey()))
                        .mapToDouble(entry -> entry.getValue().doubleValue())
                        .sum();
                object.put("value", rainfall / stationList.size());
                result.add(object);
            }
        });
        return result;
    }


    @Override
    public JSONObject judgeRainfallStation(String beginTime, String endTime) {
        JSONObject object = new JSONObject();
        StationRequestVo stationRequestVo = new StationRequestVo();
        stationRequestVo.setIsRainStation(Boolean.TRUE);
//        stationRequestVo.setStationType(com.ygkj.enums.StationType.RAINFALL.type());
        List<AttStBase> stations = stationMapper.selectByCondition(stationRequestVo);
        List<String> codes = stations.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        List<StPptnR> rainfall = rainAndWaterMapper.selectPastHoursRainfall(judgeRainfallTable(), endTime, DateUtils.hourBetween(beginTime, endTime), codes, Boolean.FALSE);
        Map<String, Double> rainfallMap = rainfall.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        JSONArray array = new JSONArray(stations.size());
        stations = stations.stream().filter(attStBase -> null != attStBase.getStLong() && null != attStBase.getStLat()).collect(Collectors.toList());
        for (AttStBase station : stations) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("longitude", station.getStLong().doubleValue());
            jsonObject.put("latitude", station.getStLat().doubleValue());
            jsonObject.put("stationName", station.getStName());
            jsonObject.put("rainfall", rainfallMap.getOrDefault(station.getStCode(), 0d));
            array.add(jsonObject);
        }
        object.put("data", array);
        object.put("between0And10", rainfall.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 0d && stPptnR.getDrp().doubleValue() <= 10d).count());
        object.put("between10And25", rainfall.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 10d && stPptnR.getDrp().doubleValue() <= 25d).count());
        object.put("between25And50", rainfall.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 25d && stPptnR.getDrp().doubleValue() <= 50d).count());
        object.put("between50And100", rainfall.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 50d && stPptnR.getDrp().doubleValue() <= 100d).count());
        object.put("between100And250", rainfall.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 100d && stPptnR.getDrp().doubleValue() <= 250d).count());
        object.put("between250And500", rainfall.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 250d && stPptnR.getDrp().doubleValue() <= 500d).count());
        object.put("between500And800", rainfall.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 500d && stPptnR.getDrp().doubleValue() <= 800d).count());
        object.put("beyond800", rainfall.stream().filter(stPptnR -> stPptnR.getDrp().doubleValue() > 800d).count());
        return object;
    }

    private JSONArray concatSurfaceData(List<StPptnR> list, Map<String, List<AttStBase>> map, Map<String, String> adcdMap) {
        JSONArray array = new JSONArray();
        adcdMap.forEach((adcd, adnm) -> {
            if (map.containsKey(adcd)) {
                Map<String, List<AttStBase>> stationMap = map.get(adcd).stream()
                        .filter(attStBase -> attStBase.getStLat() != null && attStBase.getStLong() != null)
                        .collect(Collectors.groupingBy(AttStBase::getStCode));
                List<String> collect = map.get(adcd).stream().map(AttStBase::getStCode).collect(Collectors.toList());
                List<StPptnR> rains = list.stream().filter(stPptnR -> collect.contains(stPptnR.getMgstcd())).collect(Collectors.toList());
                for (StPptnR rain : rains) {
                    String stcd = rain.getStcd();
                    if (stationMap.containsKey(stcd)) {
                        AttStBase attStBase = stationMap.get(stcd).get(0);
                        rain.setLatitude(attStBase.getStLat().doubleValue());
                        rain.setLongitude(attStBase.getStLong().doubleValue());
                    }
                }
                rains.sort(Comparator.comparing(StPptnR::getMgstcd).thenComparing(Comparator.comparing(StPptnR::getTm).reversed()));
                JSONObject object = new JSONObject();
                object.put("areaName", adnm);
                object.put("rainData", rains);
                array.add(object);
            }
        });
        return array;
    }

    @Override
    public JSONObject customRainfallSurfaceStatistic(String beginTime, String endTime) {
        JSONObject result = new JSONObject();
        List<AttStBase> stations = getRainfallStations();
        List<String> codes = stations.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        List<StPptnR> rainfall = rainAndWaterMapper.selectPastHoursRainfall(judgeRainfallTable(), endTime, DateUtils.hourBetween(beginTime, endTime), codes, Boolean.FALSE);
        Map<String, Double> map = rainfall.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        Map<String, String> stationMap = stations.stream().collect(Collectors.toMap(AttStBase::getStCode, AttStBase::getStName));
        stations.forEach(attStBase -> attStBase.setRainfall(map.getOrDefault(attStBase.getStCode(), 0d)));
        //温州市平均面雨量
        result.put("totalSurfaceRainfall", rainfall.stream().mapToDouble(StPptnR::getDrp).average().orElse(0d));
//        rainfall.sort(Comparator.comparing(StPptnR::getDrp).reversed());
//        StPptnR maxPoint = rainfall.get(0);
        if (!rainfall.isEmpty()) {
            rainfall.sort(Comparator.comparing(StPptnR::getDrp).reversed());
            StPptnR maxPoint = rainfall.get(0);
            result.put("maxPointRainfall", maxPoint.getDrp().doubleValue());
            result.put("maxPointName", stationMap.getOrDefault(maxPoint.getStcd(), ""));
        } else {
            result.put("maxPointRainfall", 0);
            result.put("maxPointName", StringUtils.EMPTY);
        }
//        Optional<StPptnR> maxPointTemp = rainfall.stream().max(Comparator.comparing(StPptnR::getDrp).reversed());
//        if (maxPointTemp.isPresent()) {
//            StPptnR maxPoint = maxPointTemp.get();
//            //最大降雨点位
//            result.put("maxPointRainfall", maxPoint.getDrp().doubleValue());
//            result.put("maxPointName", stationMap.getOrDefault(maxPoint.getStcd(), ""));
//        } else {
//            result.put("maxPointRainfall", 0);
//            result.put("maxPointName", StringUtils.EMPTY);
//        }
        //行政区划最大面雨量
        List<AttAdcdBase> adcdList = adcdBaseMapper.selectWenZhouCounty();
        Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        Map<String, DoubleSummaryStatistics> dataMap = stations.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getAreaCode()))
                .collect(Collectors.groupingBy(AttStBase::getAreaCode, Collectors.summarizingDouble(AttStBase::getRainfall)));
        JSONArray areaRainfallChart = new JSONArray();
        dataMap.forEach((areaCode, statistic) -> {
            JSONObject object = new JSONObject();
            object.put("key", adcdMap.getOrDefault(areaCode, ""));
            object.put("value", statistic.getAverage());
            areaRainfallChart.add(object);
        });
        List<Object> list = areaRainfallChart.stream()
                .sorted(Comparator.comparing(o -> ((JSONObject) o).getDoubleValue("value")).reversed())
                .collect(Collectors.toList());
        result.put("areaRainfallChart", list);
        JSONObject jsonObject = (JSONObject) list.get(0);
        result.put("maxRainfallAreaName", jsonObject.get("key"));
        result.put("areaMaxRainfall", jsonObject.get("value"));
        //流域最大降雨
        dataMap = stations.stream().filter(attStBase -> StringUtils.isNotBlank(attStBase.getBas()))
                .collect(Collectors.groupingBy(AttStBase::getBas, Collectors.summarizingDouble(AttStBase::getRainfall)));
        JSONArray basinRainfallChart = new JSONArray();
        dataMap.forEach((basin, statistic) -> {
            JSONObject object = new JSONObject();
            object.put("key", basin);
            object.put("value", statistic.getAverage());
            basinRainfallChart.add(object);
        });
        List<Object> basinList = basinRainfallChart.stream()
                .sorted(Comparator.comparing(o -> ((JSONObject) o).getDoubleValue("value")).reversed())
                .collect(Collectors.toList());
        jsonObject = (JSONObject) basinList.get(0);
        result.put("basinRainfallChart", basinList);
        result.put("maxRainfallBasinName", jsonObject.get("key"));
        result.put("basinMaxRainfall", jsonObject.get("value"));
        return result;
    }

    @Override
    public List<AttStBase> customRainfallSurfaceData(String beginTime, String endTime) {
        List<AttStBase> stations = getRainfallStations();
        List<String> codes = stations.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        List<StPptnR> rainfall = rainAndWaterMapper.selectPastHoursRainfall(judgeRainfallTable(), endTime, DateUtils.hourBetween(beginTime, endTime), codes, Boolean.FALSE);
        Map<String, Double> map = rainfall.stream().collect(Collectors.toMap(StPptnR::getStcd, StPptnR::getDrp));
        stations.forEach(attStBase -> attStBase.setRainfall(map.getOrDefault(attStBase.getStCode(), 0d)));
        return stations;
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

    private static String judgeRainfallTable() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
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

    private List<AttStBase> getRainfallStations() {
        StationRequestVo stationRequestVo = new StationRequestVo();
        stationRequestVo.setIsRainStation(Boolean.TRUE);
        List<AttStBase> stations = stationMapper.selectByCondition(stationRequestVo);
        return stations;
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

    /**
     * 河网复盘统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject tideReplayStats(TideReplayReqVo reqVo) {
        Date start = reqVo.getStart();
        Date end = reqVo.getEnd();
        JSONObject result = new JSONObject();
        if (start == null || end == null) {
            return result;
        }
        List<AttStBase> stationList = this.stationMapper.selectByCondition(Builder.of(StationRequestVo::new)
                .with(StationRequestVo::setStationName, reqVo.getStName())
                .with(StationRequestVo::setStationCodes, reqVo.getStCode())
                .with(StationRequestVo::setStationType, StationType.TIDE.type())
                .build());
        if (org.springframework.util.CollectionUtils.isEmpty(stationList)) {
            return result;
        }
        List<String> tables = echoWaterRainTableByDateInternal(com.ygkj.enums.StationType.TIDE.table(), start, end);
        List<StPrrwtResVo> waterLevelTempList = Collections.emptyList();
        List<String> stcds = stationList.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        if (!org.springframework.util.CollectionUtils.isEmpty(tables)) {
            waterLevelTempList = rainAndWaterMapper.selectTideByTimeInterval(tables, start, end, stcds);
        }
        waterLevelTempList = waterLevelTempList == null ? Collections.emptyList() : waterLevelTempList;

        List<StPrrwtResVo> astroTideTempList = rainAndWaterMapper.selectAstrotdByTimeInterval(start, end, stcds);
        astroTideTempList = astroTideTempList == null ? Collections.emptyList() : astroTideTempList;

        Map<String, List<StPrrwtResVo>> waterLevelMap = waterLevelTempList.stream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));
        Map<String, List<StPrrwtResVo>> astroTideMap = astroTideTempList.stream().collect(Collectors.groupingBy(StPrrwtResVo::getMgstcd));

        List<TideReplayResVo> tempList = new ArrayList<>(stationList.size());
        for (AttStBase stBase : stationList) {
            List<StPrrwtResVo> realTideList = waterLevelMap.get(stBase.getStCode());
            List<StPrrwtResVo> astroTideList = astroTideMap.get(stBase.getStCode());
            TideReplayResVo temp = singleTideCurve(stBase, realTideList, astroTideList);
            tempList.add(temp);
        }
        long overWarnNum = tempList.stream().filter(TideReplayResVo::isOverWarn).count();
        Optional<TideReplayResVo> maxOverWarnTimeTemp = tempList.stream().filter(o -> o.getOverWarnMinutes() != null).max(Comparator.comparing(TideReplayResVo::getOverWarnMinutes));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<TideReplayResVo> comparator = new BeanComparator<>(reqVo.getOrderColumn());
            if (reqVo.isAscOrDesc()) {
                comparator = comparator.reversed();
            }
            tempList = tempList.stream().sorted(comparator).collect(Collectors.toList());
        }
        result.put("overWarnNum", overWarnNum);
        result.put("maxOverWarnTime", maxOverWarnTimeTemp.isPresent() ? maxOverWarnTimeTemp.get() : new RivernetReplayResVo());
        if (!reqVo.isTideCurve()) {
            tempList.forEach(o -> {
                o.setRealTide(Collections.emptyList());
                o.setAstroTide(Collections.emptyList());
                o.setSubTide(Collections.emptyList());
            });
        }
        result.put("list", tempList);
        return result;
    }


    private TideReplayResVo singleTideCurve(AttStBase stBase, List<StPrrwtResVo> realTempList, List<StPrrwtResVo> astroTempList) {
        if (stBase == null) {
            return null;
        }
        TideReplayResVo result = new TideReplayResVo();
        BeanUtils.copyProperties(stBase, result);
        realTempList = realTempList == null ? Collections.emptyList() : realTempList;
        astroTempList = astroTempList == null ? Collections.emptyList() : astroTempList;

        Map<Date, StPrrwtResVo> realMap = realTempList.stream().collect(Collectors.groupingBy(StPrrwtResVo::getTm)).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().get(0)));

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

        // 将实时潮位和天文潮位按时间一个个匹配
        List<StPrrwtResVo> finalRealList = null;
        List<StPrrwtResVo> subTideList = Collections.emptyList();
        if (CollectionUtils.isNotBlank(finalAstroList)) {
            finalRealList = new ArrayList<>(realTempList.size());
            subTideList = new ArrayList<>(realTempList.size());
            for (StPrrwtResVo astroTemp : finalAstroList) {
                StPrrwtResVo realTemp = realMap.get(astroTemp.getTm());
                if (realTemp != null) {
                    finalRealList.add(realTemp);
                    subTideList.add(Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, astroTemp.getTm()).with(StPrrwtResVo::setTdz, Double.parseDouble(String.format("%.2f", realTemp.getTdz() - astroTemp.getTdz()))).build());
                } else {
                    realTemp = Builder.of(StPrrwtResVo::new).with(StPrrwtResVo::setTm, astroTemp.getTm()).build();
                    subTideList.add(realTemp);
                }
            }
        } else {
            finalRealList = realTempList;
        }
        Comparator<StPrrwtResVo> tideComparator = (o1, o2) -> {
            if (o1.getTdz() != null && o2.getTdz() != null) {
                return o1.getTdz().compareTo(o2.getTdz());
            } else if (o1.getTdz() == null & o2.getTdz() != null) {
                return -1;
            } else if (o1.getTdz() != null & o2.getTdz() == null) {
                return 1;
            } else {
                return 0;
            }
        };
        finalRealList.parallelStream().max(tideComparator).ifPresent(o -> {
            result.setHighestRealTide(o.getTdz());
            result.setHighestRealTideTm(o.getTm());
        });
        finalRealList.parallelStream().min(tideComparator).ifPresent(o -> {
            result.setLowestRealTide(o.getTdz());
            result.setLowestRealTideTm(o.getTm());
        });

        subTideList.parallelStream().max(tideComparator).ifPresent(o -> {
            result.setHighestSubTide(o.getTdz());
            result.setHighestSubTideTm(o.getTm());
        });
        if (result.getWarningLevel() != null) {
            double wl = result.getWarningLevel().doubleValue();
            long overWarnTimes = finalRealList.stream().filter(o -> o.getTdz() > wl).count();
            if (overWarnTimes > 0) {
                result.setOverWarn(true);
                result.setOverWarnMinutes((int) overWarnTimes * 5);
            }
        }
        result.setRealTide(finalRealList);
        result.setAstroTide(finalAstroList);
        result.setSubTide(subTideList);
        return result;
    }

    public List<AttStBase> yongJiaRainStation(SynRainRequestVo vo) {
        StationRequestVo requestVo = new StationRequestVo();
        requestVo.setStationName(vo.getStationName());
        requestVo.setIsYongJiaStation(Boolean.TRUE);
        List<AttStBase> stations = stationMapper.selectByCondition(requestVo);
        List<String> codes = stations.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        List<StPptnR> rainfalls = rainAndWaterMapper.selectYongJiaRainfall(codes, vo.getMin(), vo.getMax());
        Map<String, Double> map = rainfalls.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        for (AttStBase station : stations) {
            if (map.containsKey(station.getStCode())) {
                station.setRainfall(map.get(station.getStCode()));
            }
        }
        stations = stations.stream().filter(station -> map.containsKey(station.getStCode())).collect(Collectors.toList());
        return stations;
    }

    @Override
    public List<AttStBase> soilMoistureMonitorStations(SynWaterRequestVo requestVo) {
        List<AttStBase> stations = new ArrayList<>();
        StationRequestVo stationRequestVo = new StationRequestVo();
        stationRequestVo.setStationName(requestVo.getStationName());
        stationRequestVo.setStationType(requestVo.getStationType());
        if (StringUtils.isNotBlank(requestVo.getAreaCode())) {
            stationRequestVo.setAreaCode(requestVo.getAreaCode().startsWith("330") ? requestVo.getAreaCode() :
                    adcdBaseMapper.getWenzhouAreaCodeByAdnm(requestVo.getAreaCode()));
        }
        stations = stationMapper.selectByCondition(stationRequestVo);

        List<String> stCodes = stations.stream().map(e -> e.getStCode()).collect(Collectors.toList());

        if (!stCodes.isEmpty()) {
            List<StSoliR> stSoliRS = rainAndWaterMapper.selectSoliRByTime(stCodes, requestVo.getDataTime());
            for (AttStBase station : stations) {
                List<StSoliR> collect = stSoliRS.stream().filter(e -> e.getMgstcd().equals(station.getStCode())).collect(Collectors.toList());
                if (!collect.isEmpty()) {
                    station.setStSoliR(collect.get(0));
                }
            }
        }
        return stations;
    }

    @Override
    public List<StSoliRRespVo> soilMoistureMonitorLevels(WaterLevelQueryVo queryVo) {
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH) + 1;
        calendar.add(Calendar.DATE, queryVo.getDays() * -1);
        Date start = calendar.getTime();

        String stationCode = queryVo.getStationCode();
        List<StSoliRRespVo> stSoliRS = rainAndWaterMapper.findSoliRListData(stationCode, DateUtils.format(start, "yyyy-MM-dd HH:mm:ss"),
                DateUtils.format(now, "yyyy-MM-dd HH:mm:ss"));

        return stSoliRS;
    }


    @Autowired
    RsvrComputeService limitLevelService;

    public Double getRsvrLimitLevel(AttResBase attResBase, LocalDate now) {
//        BigDecimal limitLevel = limitLevelService.getCurrentLimitLevel((JSONObject) JSON.toJSON(attResBase), now);
        BigDecimal limitLevel = limitLevelService.getCurrentLimitLevel(attResBase, now);
        if (limitLevel != null) {
            return limitLevel.doubleValue();
        }
        return null;
    }

    /**
     * 前期影响雨量曲线
     *
     * @param areaCode
     * @return
     */
    @Override
    public List<PrecedeAffectRainfall> precedeAffectRainfallCurve(String areaCode) {
        if (StringUtils.isBlank(areaCode)) {
            areaCode = "330300000000";
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDate start = today.minusDays(30);
        return precedeAffectRainfallMapper.selectByAreaCodeTimeInterval(areaCode, DateUtils.localDate2Date(start), DateUtils.localDate2Date(today));
    }

    // 基准计算日期
    LocalDate beginDate = LocalDate.of(2021, 8, 1);

    /**
     * 前期影响雨量列表
     *
     * @param theDay
     * @return
     */
    @Override
    public List<PrecedeAffectRainfall> todayPrecedeAffectRainfall(LocalDate theDay) {
        if (theDay == null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime stamp = now.withHour(8).withMinute(0).withSecond(0).withNano(0);
            if (now.isBefore(stamp)) {
                theDay = LocalDate.now().minusDays(1);
            } else {
                theDay = LocalDate.now();
            }
        }
        List<AttAdcdBase> adcdList = adcdBaseMapper.selectWenZhouCounty();

        List<PrecedeAffectRainfall> result = precedeAffectRainfallMapper.selectByAreaCodeListTime(adcdList.stream().map(AttAdcdBase::getAdcd).collect(Collectors.toList()), DateUtils.localDate2Date(theDay));
        if (CollectionUtils.isNotBlank(result)) {
            Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
            for (PrecedeAffectRainfall precedeAffectRainfall : result) {
                precedeAffectRainfall.setAreaName(adcdMap.get(precedeAffectRainfall.getAreaCode()));
            }
        } else {
            List<PrecedeAffectRainfall> list = precedeAffectRainfallMapper.selectNewest();
            list = list == null ? Collections.emptyList() : list;
            Map<String, PrecedeAffectRainfall> map = list.stream().collect(Collectors.toMap(PrecedeAffectRainfall::getAreaCode, Function.identity()));
//            AttAdcdBase adcd = new AttAdcdBase();
//            adcd.setAdcd("330300000000");
//            adcd.setAdnm("温州市");
//            adcdList.add(adcd);
            StationRequestVo reqVo = new StationRequestVo();
            reqVo.setIsRainStation(true);
            List<AttStBase> stBases = this.stationMapper.selectByCondition(reqVo);
            Map<String, List<AttStBase>> stBaseMap = stBases.stream()
                    .filter(o -> StringUtils.isNotBlank(o.getAreaCode()))
                    .peek(o -> o.setAreaCode(o.getAreaCode().substring(0, 6).concat("000000"))).collect(Collectors.groupingBy(AttStBase::getAreaCode));

            LocalDateTime now = LocalDateTime.now();

            LocalDateTime startStamp = theDay.atTime(LocalTime.of(8, 0, 0, 0));
            LocalDateTime endStamp = startStamp.plusDays(1);
            Date yesterday = DateUtils.localDate2Date(theDay.minusDays(1));
            String startTime = startStamp.format(standardDtf);
            String endTime = endStamp.format(standardDtf);
            List<String> tables = Arrays.asList(DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(), startTime, endTime));
            result = new ArrayList<>(adcdList.size());
            for (AttAdcdBase tempAdcd : adcdList) {
                List<AttStBase> baseList = stBaseMap.getOrDefault(tempAdcd.getAdcd(), Collections.emptyList());
                if (CollectionUtils.isBlank(baseList)) {
                    if (!tempAdcd.getAdcd().equals("330300000000")) {
                        continue;
                    }
                }
                PrecedeAffectRainfall start = null;
                if ((start = map.get(tempAdcd.getAdcd())) == null) {
                    start = new PrecedeAffectRainfall();
                    start.setDate(DateUtils.localDate2Date(beginDate));
                    start.setAreaCode(tempAdcd.getAdcd());
                    start.setDrp(BigDecimal.valueOf(100));
                }
                PrecedeAffectRainfall pre = start;
                Date tempDate = new Date(start.getDate().getTime() + 24 * 3600 * 1000);
                while (!tempDate.after(yesterday)) {
                    BigDecimal avg = this.precedeAffectRainfallMapper.selectAvgDayRainfall(baseList == null ? null : baseList.stream().map(AttStBase::getStCode).collect(Collectors.toList()), tempDate);
                    avg = avg == null ? BigDecimal.ZERO : avg;
                    BigDecimal drp = pre.getDrp().multiply(BigDecimal.valueOf(0.985)).add(avg);
                    if (drp.doubleValue() > 100) {
                        drp = BigDecimal.valueOf(100);
                    }
                    pre = new PrecedeAffectRainfall();
                    pre.setDate(tempDate);
                    pre.setAreaCode(tempAdcd.getAdcd());
                    pre.setDrp(drp);
                    tempDate = new Date(tempDate.getTime() + 24 * 3600 * 1000);
                }
                List<StPptnR> stPptnR1 = null;
                if (CollectionUtils.isNotBlank(baseList)) {
                    List<String> codes = baseList.stream().filter(attStBase -> attStBase.getStCode().startsWith("SWZ_"))
                            .map(AttStBase::getStCode).collect(Collectors.toList());
                    List<String> yjCodes = baseList.stream().filter(attStBase -> attStBase.getStCode().startsWith("YJ_"))
                            .map(attStBase -> attStBase.getStCode().replaceAll("YJ_", "")).collect(Collectors.toList());
                    stPptnR1 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, startTime, endTime, codes);
                    stPptnR1.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(startTime, endTime, yjCodes));
                } else {
                    stPptnR1 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, startTime, endTime, null);
                    stPptnR1.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(startTime, endTime, null));
                }
                BigDecimal avg = BigDecimal.valueOf(stPptnR1.stream().mapToDouble(StPptnR::getDrp).average().orElse(0d));
                avg = avg == null ? BigDecimal.ZERO : avg;
                BigDecimal drp = pre.getDrp().multiply(BigDecimal.valueOf(0.985)).add(avg);
                if (drp.doubleValue() > 100) {
                    drp = BigDecimal.valueOf(100);
                }
                pre = new PrecedeAffectRainfall();
                pre.setDate(tempDate);
                pre.setAreaCode(tempAdcd.getAdcd());
                pre.setAreaName(tempAdcd.getAdnm());
                pre.setDrp(drp);
                result.add(pre);
            }
        }
        result.stream().filter(o -> o.getDrp() != null).max(Comparator.comparing(PrecedeAffectRainfall::getDrp)).ifPresent(o -> o.setValueType(3));
        result.stream().filter(o -> o.getDrp() != null).min(Comparator.comparing(PrecedeAffectRainfall::getDrp)).ifPresent(o -> o.setValueType(1));
        return result;
    }

    /**
     * 前期影响雨量c位窗口
     *
     * @return
     */
    @Override
    public JSONObject precedeAffectRainfall4CWindow() {
        List<PrecedeAffectRainfall> list = todayPrecedeAffectRainfall(null);
        JSONObject result = new JSONObject();
        PrecedeAffectRainfall aridest = list.stream().filter(o -> o.getDrp() != null).min(Comparator.comparing(PrecedeAffectRainfall::getDrp)).orElse(null);
        PrecedeAffectRainfall moistest = list.stream().filter(o -> o.getDrp() != null).max(Comparator.comparing(PrecedeAffectRainfall::getDrp)).orElse(null);
        result.put("aridest", aridest);
        result.put("moistest", moistest);
        return result;
    }

    public List<String> getDateSting(int days){
        Calendar calendar = Calendar.getInstance();
        Date endTime = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH,days * -1);
        List<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        calendar.add(Calendar.HOUR_OF_DAY,1);
        while (calendar.getTime().compareTo(endTime) <= 0){
            String format = sdf.format(calendar.getTime());
            result.add(format);
            calendar.add(Calendar.HOUR_OF_DAY,1);
        }
        return result;
    }
}
