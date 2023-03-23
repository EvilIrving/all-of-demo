package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.mapper.FloodControlDispatchingMapper;
import com.ygkj.big.screen.mapper.GiveAnAlarmMapper;
import com.ygkj.big.screen.mapper.SectionInformationMapper;
import com.ygkj.big.screen.mapper.SectionModelDataMapper;
import com.ygkj.big.screen.model.SectionInformation;
import com.ygkj.big.screen.model.SectionModelData;
import com.ygkj.big.screen.service.FloodControlDispatchingService;
import com.ygkj.big.screen.service.GiveAnAlarmService;
import com.ygkj.big.screen.service.SectionInformationService;
import com.ygkj.big.screen.vo.response.XpjProject;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.rpc.GraphRpcService;
import com.ygkj.gragh.vo.request.WaterLevelQueryVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ygkj.com.util.DateUtil;
import ygkj.com.util.MydateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2022/5/6 10:23
 * @Description:
 */
@Service
public class FloodControlDispatchingServiceImpl implements FloodControlDispatchingService {
    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
    @Reference
    private GraphRpcService graphRpcService;
    @Autowired
    private FloodControlDispatchingMapper dispatchingMapper;
    @Autowired
    private GiveAnAlarmService giveAnAlarmService;
    @Autowired
    private GraphClient graphClient;
    @Autowired
    private ProjectManageClient projectManageClient;
    @Autowired
    private SectionInformationService sectionInformationService;
    @Autowired
    private SectionModelDataMapper sectionModelDataMapper;
    @Autowired
    private SectionInformationMapper sectionInformationMapper;
    @Autowired
    private GiveAnAlarmMapper giveAnAlarmMapper;
    @Override
    public CommonResult mapPoints() {
        List<AttStBase> result = new ArrayList<>();
        List<AttStBase> stBases = dispatchingMapper.getStByIds("01589,101,01421");
        //泽雅水库
        String rsvrTable = StationType.RESERVOIR.table().concat(LocalDate.now().format(yyyyMM));
        List<StRsvrR> stRsvrRS = graphRpcService.selectRsvrWaterLevelByTimeCode(rsvrTable, new ArrayList<String>() {{
            add("01589");
        }}, null);
        //戍浦江大闸
        String gateTable = StationType.GATE.table().concat(LocalDate.now().format(yyyyMM));
        List<StWasR> stWasRS = graphRpcService.selectWasByTimeCode(gateTable, new ArrayList<String>() {{
            add("101");
        }}, null);
        //江心屿
        String tideTable = StationType.TIDE.table().concat(LocalDate.now().format(yyyyMM));
        List<StTideR> stTideRS = graphRpcService.selectTideByTimeCode(tideTable, new ArrayList<String>() {{
            add("01421");
        }}, null);
        //todo 若干断面
        for (AttStBase stBase : stBases) {
            if (stRsvrRS != null && stRsvrRS.size() > 0) {
                for (StRsvrR stRsvrR : stRsvrRS) {
                    if (stBase.getStCode().equals(stRsvrR.getMgstcd())) {
                        stBase.setWaterLevel(stRsvrR.getRz());
                        stBase.setTime(stRsvrR.getTm());
                    }
                }
            }
            if (stWasRS != null && stWasRS.size() > 0) {
                for (StWasR stWasR : stWasRS) {
                    if (stBase.getStCode().equals(stWasR.getMgstcd())) {
                        Double upz = stWasR.getUpz() != null ? stWasR.getUpz() : 0.0;
                        stBase.setUpz(upz);
                        Double dwz = stWasR.getDwz() != null ? stWasR.getDwz() : 0.0;
                        stBase.setDwz(dwz);
                        String ovs = stWasR.getOvs() != null ? stWasR.getOvs() : "0.0";
                        stBase.setOpenGate(ovs);
                        stBase.setTime(stWasR.getTm());
                    }
                }
            }
            if (stTideRS != null && stTideRS.size() > 0) {
                for (StTideR stTideR : stTideRS) {
                    if (stBase.getStCode().equals(stTideR.getMgstcd())) {
                        stBase.setTdz(stTideR.getTdz());
                        stBase.setTime(stTideR.getTm());
                    }
                }
            }
        }
        result.addAll(stBases);
        return CommonResult.success(result);
    }

    @Override
    public Object forecast(String index) {

        JSONObject object = new JSONObject();
        switch (index) {
            case "1":
                //泽雅水库站点水位曲线数据，汛限水位，防洪高水位
                AttStBase zyBase = dispatchingMapper.getStByIds("01589").get(0);
                LinkedHashMap data = getWaterLevelData(1, "RR", zyBase.getStCode());
                List<StRiverR> stRiverRS = (List<StRiverR>) data.get("data");
                object.put("base", zyBase);
                object.put("date", stRiverRS);
                break;
            case "2":
                //查询江心屿海塘绑定的潮位站实时潮位数据，预报潮位曲线
                AttStBase jxyBase = dispatchingMapper.getStByIds("01421").get(0);
                LinkedHashMap data2 = getWaterLevelData(1, "TT", jxyBase.getStCode());
                List<StTideR> stTideRS = (List<StTideR>) data2.get("data");
                //todo 缺少预报48h的潮位数据
                object.put("base", jxyBase);
                object.put("date", stTideRS);
                object.put("yb", "");
                break;
            case "3":
                //查询戌浦江闸站绑定的闸站测点数据，闸上水位，闸下水位，控制水位，死水位
                object = dispatchingMapper.getszinfo("3350ee98-1c7b-11ea-8760-6c92bf66b51e");
                LinkedHashMap data1 = getWaterLevelData(1, "DD", object.getString("stcd"));
                List<StWasR> stWasRS = (List<StWasR>) data1.get("data");
                object.put("date", stWasRS);
                break;
            default:
                //根据断面的数据查询对应断面的测点数据
                //todo 暂时不知道断面数据是怎么样的
        }
        return object;
    }

    @Override
    public Object plan() {
        JSONObject result = new JSONObject();
        //得到每个类型最新的预案
        List<FloodControlDispatchingPlan> plan = dispatchingMapper.getPlan();
        result.put("planList", plan);
        //预案各个类型历史数据
        List<FloodControlDispatchingPlan> historyPlan = dispatchingMapper.getHistoryPlan();
        result.put("historyPlanList", historyPlan);
        //对于1、2、3的类型要判断戍浦江内河水位是否大于死水位
        //死水位
        JSONObject object = dispatchingMapper.getszinfo("3350ee98-1c7b-11ea-8760-6c92bf66b51e");
        Double dealLevel = object.getDouble("deal_level");
        //内河水位
        String riverTable = StationType.RIVER.table().concat(LocalDate.now().format(yyyyMM));
        List<StRiverR> stRiverRS = graphRpcService.selectRiverByTimeCode(riverTable, new ArrayList<String>() {{
            add("010111");
        }}, null);
        if (stRiverRS.size() > 0) {
            StRiverR stRiverR = stRiverRS.get(0);
            if (dealLevel != null && stRiverR.getZ() < dealLevel) {
                result.put("compareToDealLevel", false);
            } else {
                result.put("compareToDealLevel", true);
            }
        } else {
            result.put("compareToDealLevel", true);
        }
        return result;
    }

    @Override
    public Object planSupervision() {
        JSONObject result = new JSONObject();
        //戍浦江监控视频
        CommonResult jksp = projectManageClient.projectCameras("3350ee98-1c7b-11ea-8760-6c92bf66b51e", null);
        result.put("jksp", jksp.getData());
        //内河（戍浦江河口站）实时水位
        LinkedHashMap spjZZ = getWaterLevelData(1, "ZZ", "010111");
        List<StRiverR> stRiverRS = (List<StRiverR>) spjZZ.get("data");
        result.put("riverData", stRiverRS);
        //todo 内河（戍浦江河口站）预报水位
        result.put("forecastRiverData", new ArrayList<>());
        //江心屿实时潮位
        LinkedHashMap jxyTT = getWaterLevelData(1, "TT", "01421");
        List<StTideR> stTideRS = (List<StTideR>) jxyTT.get("data");
        result.put("tideData", stTideRS);
        //todo 江心屿预报潮位
        result.put("forecastTideData", new ArrayList<>());
        //戍浦江闸上水位、闸下水位、开度值
        LinkedHashMap spjDD = getWaterLevelData(1, "DD", "101");
        List<StWasR> stWasRS = (List<StWasR>) spjDD.get("data");
        result.put("gateData", stWasRS);
        return result;
    }

    @Override
    public Object projectList(String name) {

        //查询泽雅水库，江心屿站，戍浦江大闸
        CommonResult commonResult = mapPoints();
        List<AttStBase> stBases = (List<AttStBase>) commonResult.getData();
        //查询江心屿海塘对应的主断面预报最高水位和时间 332ac745-1c7b-11ea-8760-6c92bf66b51e
        String sectionId = dispatchingMapper.getDmMain("332ac745-1c7b-11ea-8760-6c92bf66b51e");
        JSONObject jsonObject = dispatchingMapper.getMaxH2(sectionId, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        //查询断面信息
        QueryWrapper<SectionInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.orderByDesc("creation_time");
        List<SectionInformation> list = sectionInformationService.list(queryWrapper);
        List<SectionInformation> informations = sectionInformationService.fillData(list);

        ArrayList<XpjProject> objects = new ArrayList<>();
        //组装数据
        for (AttStBase stBase : stBases) {
            if (StringUtils.isNotBlank(name)) {
                if (!stBase.getStName().contains(name)) {
                    continue;
                }
            }
            XpjProject xpjProject = new XpjProject();
            xpjProject.setName(stBase.getStName());
            xpjProject.setStCode(stBase.getStCode());
            xpjProject.setLgtd(stBase.getStLong().toString());
            xpjProject.setLttd(stBase.getStLat().toString());
            xpjProject.setLimitLevel(stBase.getLimitLevel());
            xpjProject.setGuaranteeLevel(stBase.getGuaranteeLevel());
            xpjProject.setWarningLevel(stBase.getWarningLevel());
            xpjProject.setWaterLevel(stBase.getWaterLevel());
            xpjProject.setUpdateTime(stBase.getTime());
            xpjProject.setUpz(stBase.getUpz());
            xpjProject.setDwz(stBase.getDwz());
            xpjProject.setOpenGate(stBase.getOpenGate());
            String stType = stBase.getStType();
            if (StringUtils.isNotBlank(stType)) {
                switch (stType) {
                    case "RR":
                        xpjProject.setType("水库测站");
                        break;
                    case "TT":
                        xpjProject.setType("潮位测站");
                        //潮位站的时候就是江心屿，需要插入预报高潮位数据，预报高潮位时间
                        xpjProject.setYbTdz(jsonObject != null ? jsonObject.getDoubleValue("h2") : 0d);
                        xpjProject.setYbTime(jsonObject != null ? jsonObject.getDate("tm") : null);
                        break;
                    case "DD":
                        xpjProject.setType("水闸测站");
                        break;
                }
            }
            objects.add(xpjProject);
        }
        //放入断面数据
        for (SectionInformation information : informations) {
            Double waterLevel = information.getWaterLevel();
            Double guaranteeLevel = information.getGuaranteeLevel();
            Double warningLevel = information.getWarningLevel();
            XpjProject xpjProject = new XpjProject();
            xpjProject.setDmId(information.getId());
            xpjProject.setName(information.getName());
            xpjProject.setStCode(information.getStcd());
            xpjProject.setLgtd(information.getLgtd());
            xpjProject.setLttd(information.getLttd());
            xpjProject.setCoordinates(information.getCoordinates());
            xpjProject.setLimitLevel(information.getLimitLevel());
            xpjProject.setGuaranteeLevel(information.getGuaranteeLevel());
            xpjProject.setWarningLevel(information.getWarningLevel());
            xpjProject.setWaterLevel(information.getWaterLevel());
            xpjProject.setUpdateTime(information.getUpdateTime());
            xpjProject.setType("断面测站");
            xpjProject.setStatus("0");
            if (waterLevel != null) {
                if (guaranteeLevel != null && guaranteeLevel > 0 && waterLevel > guaranteeLevel) {
                    xpjProject.setStatus("1");
                }
                if (warningLevel != null && warningLevel > 0 && waterLevel > warningLevel) {
                    xpjProject.setStatus("2");
                }
            }
            objects.add(xpjProject);
        }
        return objects;
    }

    @Override
    public Object dmInfo(String id, String startTime, String endTime) {
        HashMap<String, Object> result = new HashMap<>();
        //查询断面的信息
        SectionInformation information = sectionInformationService.getById(id);
        result.put("name", information.getName());
        result.put("leftElevation", information.getLeftElevation());
        result.put("rightElevation", information.getRightElevation());
        result.put("stcd", information.getStcd());
        //查询绑定测站信息
        String stcd = information.getStcd();
        if (StringUtils.isNotBlank(stcd)) {
            List<AttStBase> stBases = dispatchingMapper.getStByIds(stcd);
            AttStBase attStBase = stBases.get(0);
            result.put("warningLevel", attStBase.getWarningLevel());
            result.put("guaranteeLevel", attStBase.getGuaranteeLevel());
            //获取历史最高水位
            Double highestLevel = null;
            Date appearTime = null;
            String stType = attStBase.getStType();
            List<String> stcds = new ArrayList<>();
            if (StationType.RIVER.type().equals(stType)){
                List<String> riverTables = echoWaterRainTableByDateInternal(StationType.RIVER.table(), startTime, endTime);
                stcds.add(information.getStcd());
                List<StRiverR> stRiverRS = sectionInformationMapper.selectRiverHighestLevel(riverTables, stcds,startTime,endTime);
                StRiverR stRiverR = stRiverRS.get(0);
                highestLevel = stRiverR.getZ();
                appearTime = stRiverR.getTm();
            }else if (StationType.RESERVOIR.type().equals(stType)) {
                List<String> reservoirTables = echoWaterRainTableByDateInternal(StationType.RESERVOIR.table(), startTime, endTime);
                stcds.add(information.getStcd());
                List<StRsvrR> stRsvrRS = sectionInformationMapper.selectRsvrHighestLevel(reservoirTables, stcds,startTime,endTime);
                StRsvrR stRsvrR = stRsvrRS.get(0);
                highestLevel = stRsvrR.getRz();
                appearTime = stRsvrR.getTm();
            }else if (StationType.TIDE.type().equals(stType)){
                List<String> tideTables = echoWaterRainTableByDateInternal(StationType.TIDE.table(), startTime, endTime);
                stcds.add(information.getStcd());
                List<StTideR> stTideRS = sectionInformationMapper.selectTideHighestLevel(tideTables, stcds,startTime,endTime);
                StTideR stTideR = stTideRS.get(0);
                highestLevel = stTideR.getTdz();
                appearTime = stTideR.getTm();
            }
            result.put("highestLevel", highestLevel);
            result.put("appearTime", DateUtil.format(appearTime,"yyyy-MM-dd HH:mm:ss"));
            //查询实时水位曲线
            LinkedHashMap waterLevelData;
            if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
                waterLevelData = getWaterLevelData(1, attStBase.getStType(), stcd);
            } else {
                waterLevelData = getWaterLevelData2(attStBase.getStType(), stcd, DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", startTime), DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", endTime));
            }
            result.put("waterLevelData", waterLevelData);

            List<JSONObject> forecastModelData = new LinkedList<>();
            Object data = waterLevelData.get("data");
            String jsonString = JSON.toJSONString(data);
            List<JSONObject> objectList = JSONArray.parseArray(jsonString, JSONObject.class);
            for (JSONObject jsonObject : objectList) {
                JSONObject modelData = new JSONObject();
                modelData.put("tm",jsonObject.getString("tm"));
                modelData.put("type","实时水位");
                if (jsonObject.containsKey("z")){
                    modelData.put("waterLevel",jsonObject.getDouble("z"));
                }else if (jsonObject.containsKey("rz")){
                    modelData.put("waterLevel",jsonObject.getDouble("rz"));
                }else if (jsonObject.containsKey("tdz")){
                    modelData.put("waterLevel",jsonObject.getDouble("tdz"));
                }
                forecastModelData.add(modelData);
            }
            //查询预报水位
            Calendar calendar = Calendar.getInstance();
            Date now = new Date();
            if(endTime != null){
                now = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", endTime);
            }
            calendar.setTime(now);
            calendar.add(Calendar.DATE,  1);
            Date end = calendar.getTime();
            calendar.add(Calendar.DATE,  -2);
            Date start = calendar.getTime();
            if (startTime != null){
                start = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", startTime);
            }
            List<JSONObject> sectionModelData = sectionModelDataMapper.getForecastWaterLevelData(id, start, end);
            List<JSONObject> sectionModelData1 = sectionModelDataMapper.getForecastWaterLevelData(id, now, end);
            if (CollectionUtils.isNotBlank(sectionModelData)){
                for (JSONObject sectionModelDatum : sectionModelData) {
                    sectionModelDatum.put("type","预测水位");
                }
                forecastModelData.addAll(sectionModelData);
            }
            if (CollectionUtils.isNotBlank(sectionModelData1)){
                //获取预报最高水位数据
                JSONObject jsonObject = sectionModelData.stream().max(Comparator.comparing(o -> o.getDouble("waterLevel"))).get();
                result.put("forecastHighestLevel", jsonObject.getDouble("waterLevel"));
            }else {
                result.put("forecastHighestLevel", null);
            }
            LinkedHashMap<Date, List<JSONObject>> time = forecastModelData.stream().sorted(Comparator.comparing(o -> o.getDate("tm"))).collect(Collectors.groupingBy(o -> o.getDate("tm"), LinkedHashMap::new, Collectors.toList()));
            result.put("timeMap", time);
//            result.put("forecastWaterLevelData", forecastModelData);
        }else {
            result.put("highestLevel", null);
            result.put("appearTime", null);
            result.put("waterLevelData", new LinkedHashMap<>());
            result.put("forecastHighestLevel", null);
//            result.put("forecastWaterLevelData", new ArrayList<>());
            result.put("timeMap", new LinkedHashMap<>());
        }
        return result;
    }

    @Override
    public Object chooseSection() {
        QueryWrapper<SectionInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("stcd");
        List<SectionInformation> list = sectionInformationService.list(queryWrapper);
        return list;
    }

    @Override
    public Object forecastSection(String sectionIds) {
        //断面信息
        QueryWrapper<SectionInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(StringUtils.isNotBlank(sectionIds), "id in (" + sectionIds + ")");
        List<SectionInformation> sectionList = sectionInformationService.list(queryWrapper);

        //断面模型数据
        Date startTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        QueryWrapper<SectionModelData> modleWrapper = new QueryWrapper<>();
        modleWrapper.ge("hour", startTime);
        modleWrapper.le("hour", calendar.getTime());
        modleWrapper.apply(StringUtils.isNotBlank(sectionIds), "sectionId in (" + sectionIds + ")");
        List<SectionModelData> modelData = sectionModelDataMapper.selectList(modleWrapper);

        Map<String, List<SectionModelData>> map = modelData.stream().collect(Collectors.groupingBy(SectionModelData::getSectionid));

        List<JSONObject> objectList = new ArrayList<>();
        if (CollectionUtils.isNotBlank(sectionList)) {
            for (SectionInformation sectionInformation : sectionList) {
                if (map.containsKey(sectionInformation.getId())) {
                    List<SectionModelData> sectionModelData = map.get(sectionInformation.getId());
//                    Map<String, List<SectionModelData>> modleDataMap = sectionModelData.stream().collect(Collectors.groupingBy(SectionModelData::getHour));
//                    String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:00:00");
//                    Date time = DateUtil.parse(format);
//                    Boolean isTomorrow = false;
//                    for (int i = 0; i < 24; i++) {
//                        String format1 = DateUtil.format(time, "yyyy-MM-dd HH:00:00");
//                        if (isTomorrow){
//                            JSONObject object = new JSONObject();
//                            object.put("time",format1);
//                            object.put("name",sectionInformation.getName());
//                            List<SectionModelData> modelData1 = modleDataMap.get("24:00:00");
//                            object.put("forestWaterLevel",modelData1.get(0).getDepth());
//                            objectList.add(object);
//                            isTomorrow = false;
//                        }else  if(modleDataMap.containsKey(format1.substring(11,19))){
//                            JSONObject object = new JSONObject();
//                            object.put("time",format1);
//                            object.put("name",sectionInformation.getName());
//                            List<SectionModelData> modelData1 = modleDataMap.get(format1.substring(11, 19));
//                            object.put("forestWaterLevel",modelData1.get(0).getDepth());
//                            objectList.add(object);
//                        }
//                        time = MydateUtil.getNextDay(time, null, null, null, 1, null, null);
//                        if (DateUtil.format(time, "yyyy-MM-dd HH:00:00").endsWith("00:00:00") && i > 0){
//                            isTomorrow = true;
//                        }
//                    }
                    for (SectionModelData sectionModelDatum : sectionModelData) {
                        JSONObject object = new JSONObject();
                        object.put("time", sectionModelDatum.getHour());
                        object.put("name", sectionInformation.getName());
                        object.put("forestWaterLevel", sectionModelDatum.getDepth());
                        object.put("forestFlow", sectionModelDatum.getFlow());
                        objectList.add(object);
                    }
                } else {
                    String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:00:00");
                    Date time = DateUtil.parse(format);
                    for (int i = 0; i < 24; i++) {
                        String format1 = DateUtil.format(time, "yyyy-MM-dd HH:00:00");
                        JSONObject object = new JSONObject();
                        object.put("time", format1);
                        object.put("name", sectionInformation.getName());
                        object.put("forestWaterLevel", 0);
                        object.put("forestFlow", 0);
                        objectList.add(object);
                        time = MydateUtil.getNextDay(time, null, null, null, 1, null, null);
                    }

                }
            }
            if (CollectionUtils.isNotBlank(objectList)) {
                LinkedHashMap<Date, List<JSONObject>> time = objectList.stream().sorted(Comparator.comparing(o -> o.getDate("time"))).collect(Collectors.groupingBy(o -> o.getDate("time"), LinkedHashMap::new, Collectors.toList()));
                return time;
            }
        }
        return new HashMap();
    }

    @Override
    public Object dmWaterLevel(String id) {
        HashMap<String, Object> result = new HashMap<>();
        //查询断面的信息
        SectionInformation information = sectionInformationService.getById(id);
        result.put("name", information.getName());
        String stcd = information.getStcd();
        if (StringUtils.isNotBlank(stcd)) {
            List<AttStBase> stBases = dispatchingMapper.getStByIds(stcd);
            AttStBase attStBase = stBases.get(0);
            //查询实时数据
            List<JSONObject> list = new LinkedList<>();
            LinkedHashMap waterLevelData = getWaterLevelData(1, attStBase.getStType(), stcd);
            Object data = waterLevelData.get("data");
            String jsonString = JSON.toJSONString(data);
            List<JSONObject> objectList = JSONArray.parseArray(jsonString, JSONObject.class);
            for (JSONObject jsonObject : objectList) {
                JSONObject modelData = new JSONObject();
                modelData.put("tm",jsonObject.getString("tm"));
                modelData.put("type","实时水位");
                if (jsonObject.containsKey("z")){
                    modelData.put("waterLevel",jsonObject.getDouble("z"));
                }else if (jsonObject.containsKey("rz")){
                    modelData.put("waterLevel",jsonObject.getDouble("rz"));
                }else if (jsonObject.containsKey("tdz")){
                    modelData.put("waterLevel",jsonObject.getDouble("tdz"));
                }
                list.add(modelData);
            }
            //查询预报水位
            Calendar calendar = Calendar.getInstance();
            Date now = new Date();
            calendar.setTime(now);
            calendar.add(Calendar.DATE,  1);
            Date end = calendar.getTime();
            calendar.add(Calendar.DATE,  -2);
            Date start = calendar.getTime();
            List<JSONObject> sectionModelData = sectionModelDataMapper.getForecastWaterLevelData(id, start,end );
            List<JSONObject> sectionModelData1 = sectionModelDataMapper.getForecastWaterLevelData(id, now,end );
            if (CollectionUtils.isNotBlank(sectionModelData)){
                for (JSONObject sectionModelDatum : sectionModelData) {
                    JSONObject modelData = new JSONObject();
                    modelData.put("tm",sectionModelDatum.getString("tm"));
                    modelData.put("type","预测水位");
                    modelData.put("waterLevel",sectionModelDatum.getDouble("waterLevel"));
                    list.add(modelData);
                }

            }
            if (CollectionUtils.isNotBlank(sectionModelData1)){
                JSONObject jsonObject = sectionModelData1.stream().max(Comparator.comparing(o -> o.getDouble("waterLevel"))).get();
                Double highestLevel = jsonObject.getDouble("waterLevel");
                result.put("forecastHighestLevel", highestLevel);
                result.put("appearTime", jsonObject.getDouble("tm"));
                JSONObject jsonObject1 = sectionModelData1.get(0);
                Double forecastStartLevel = jsonObject1.getDouble("waterLevel");
                result.put("description","本次预报最大涨水位约" + new BigDecimal(highestLevel-forecastStartLevel + "").setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() + "m");
            } else {
                result.put("forecastHighestLevel", null);
                result.put("appearTime", null);
                result.put("description","本次预报最大涨水位约0m");
            }
            LinkedHashMap<Date, List<JSONObject>> time = list.stream().sorted(Comparator.comparing(o -> o.getDate("tm"))).collect(Collectors.groupingBy(o -> o.getDate("tm"), LinkedHashMap::new, Collectors.toList()));
            result.put("timeMap", time);
        }else {
            result.put("forecastHighestLevel", null);
            result.put("appearTime", null);
            result.put("timeMap", new LinkedHashMap<>());
            result.put("description","本次预报最大涨水位约0m");
        }
        //查询预报数据
        return result;
    }

    private LinkedHashMap getWaterLevelData(int days, String stationType, String stationCode) {
        WaterLevelQueryVo waterLevelReqVo = new WaterLevelQueryVo();
        waterLevelReqVo.setDays(1);
        waterLevelReqVo.setStationType(stationType);
        waterLevelReqVo.setStationCode(stationCode);
        CommonResult result = graphClient.waterLevelsForFeign(waterLevelReqVo);
        return (LinkedHashMap) result.getData();
    }

    private LinkedHashMap getWaterLevelData2(String stationType, String stationCode, Date startTime, Date endTime) {
        WaterLevelQueryVo waterLevelReqVo = new WaterLevelQueryVo();
        waterLevelReqVo.setStartTime(startTime);
        waterLevelReqVo.setEndTime(endTime);
        waterLevelReqVo.setStationType(stationType);
        waterLevelReqVo.setStationCode(stationCode);
        CommonResult result = graphClient.waterLevelsForFeign(waterLevelReqVo);
        return (LinkedHashMap) result.getData();
    }

    /**
     * 获取时间范围内需要查询的表名称（分表）
     *
     * @param tablePrefix
     * @param start
     * @param end
     * @return
     */
    public List<String> echoWaterRainTableByDateInternal(String tablePrefix, String startTime, String endTime) {
        if (org.apache.commons.lang3.StringUtils.isBlank(tablePrefix) || startTime == null || endTime == null) {
            return Collections.emptyList();
        }
        Date start = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss",startTime);
        Date end = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss",endTime);
        LocalDate startTemp = DateUtils.date2LocalDate(start);
        LocalDate endTemp = DateUtils.date2LocalDate(end);
        LinkedHashSet<String> sequenceSet = new LinkedHashSet<>();
        do {
            String table = tablePrefix + startTemp.format(yyyyMM);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(this.giveAnAlarmMapper.checkTableExist(table))) {
                sequenceSet.add(table);
            }
            startTemp = startTemp.plusDays(1);
        } while (startTemp.isBefore(endTemp));
        return new ArrayList<>(sequenceSet);
    }
}
