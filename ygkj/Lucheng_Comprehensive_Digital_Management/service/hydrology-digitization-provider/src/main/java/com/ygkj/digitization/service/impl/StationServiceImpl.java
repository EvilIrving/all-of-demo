package com.ygkj.digitization.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.mapper.SynRainAndWaterMapper;
import com.ygkj.digitization.mapper.WaterRuleConfigurationMapper;
import com.ygkj.digitization.service.StationService;
import com.ygkj.digitization.enums.StationQueryCollectionType;
import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.mapper.ProjectMapper;
import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.vo.request.AssoStationReservoirReqVo;
import com.ygkj.digitization.vo.request.StationDivideQueryVo;
import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.digitization.vo.response.AssoStationReservoirRespVo;
import com.ygkj.digitization.vo.response.RainfallStationResVo;
import com.ygkj.digitization.vo.response.ReservoirResVo;
import com.ygkj.digitization.vo.response.WaterLevelResVo;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.*;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ygkj.com.util.MydateUtil;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service("stationService")
public class StationServiceImpl implements StationService {

    @Autowired
    private AttStBaseMapper stBaseMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private SynRainAndWaterMapper rainAndWaterMapper;

    @Resource
    private WaterRuleConfigurationMapper waterRuleConfigurationMapper;

    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
    @Override
    public JSONArray stationDivide(StationDivideQueryVo queryVo) {
        JSONArray result = new JSONArray();
        if (StationQueryCollectionType.AREA_CODE.type().equals(queryVo.getCollectionType())){
            StationQueryVo vo = new StationQueryVo();
            BeanUtils.copyProperties(queryVo,vo);
            List<RainfallStationResVo> list = stBaseMapper.selectByCondition(vo);
            if (!list.isEmpty()){
                //根据行政区划分组
                Map<String, List<RainfallStationResVo>> map = list.stream().
                        filter(station -> StringUtils.isNotBlank(station.getAreaName())).
                        collect(Collectors.groupingBy(RainfallStationResVo::getAreaName));
                map.forEach((areaName,stations)->{
                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("areaCode",areaCode);
                    jsonObject.put("areaName",areaName);
                    jsonObject.put("stationList",stations);
                    result.add(jsonObject);
                });
            }
        } else {
            //留空，用于流域类型筛选
        }
        return result;
    }

    public JSONObject reservoirStationTree(String name){
        List<ReservoirResVo> reservoirs = projectMapper.selectReservoirs(name,StringUtils.EMPTY);
        StationQueryVo queryVo = new StationQueryVo();
        queryVo.setStationType(StationType.RESERVOIR.type());
        List<RainfallStationResVo> stations = stBaseMapper.selectByCondition(queryVo);
        Map<String, List<RainfallStationResVo>> projectUnionStationMap = stations.stream()
                .filter(rainfallStationResVo -> StringUtils.isNotBlank(rainfallStationResVo.getProjCode()))
                .collect(Collectors.groupingBy(RainfallStationResVo::getProjCode));
        Map<String, List<ReservoirResVo>> map = reservoirs.stream().collect(Collectors.groupingBy(reservoirResVo -> {
            String engScal = reservoirResVo.getEngScal();
            String scale = "其他";
            switch (engScal) {
                case "1":
                case "3":
                    scale = "大中型";
                    break;
                case "4":
                    scale = "小（1）型";
                    break;
                case "5":
                    scale = "小（2）型";
                    break;
            }
            return scale;
        }));
        JSONArray array = new JSONArray(map.size());
        AtomicInteger total = new AtomicInteger(0);
        map.forEach((scale,list) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key",scale);
            Integer index = 0;
            list.sort(Comparator.comparing(ReservoirResVo::getEngScal));
            JSONArray reservoirsArray = new JSONArray();
            for (ReservoirResVo reservoir : list) {
                String code = reservoir.getResCode();
                JSONObject object = new JSONObject();
                object.put("key",reservoir.getResName());
                if (projectUnionStationMap.containsKey(code)){
                    List<RainfallStationResVo> rainfallStationResVos = projectUnionStationMap.get(code);
                    Set<String> set = rainfallStationResVos.stream().map(RainfallStationResVo::getStCode).collect(Collectors.toSet());
                    String mainStcd = reservoir.getMainStcd();
                    if (!set.contains(mainStcd)){
                        set.add(mainStcd);
                    }
                    object.put("number",set.size());
                    index += set.size();
                } else {
                    object.put("number",StringUtils.isNotBlank(reservoir.getMainStcd())?1:0);
                    index += object.getIntValue("number");
                }
                object.put("reservoirCode",code);
                object.put("children",null);
                reservoirsArray.add(object);
            }
            jsonObject.put("number",index);
            jsonObject.put("children",reservoirsArray);
            array.add(jsonObject);
            total.getAndAdd(index);
        });
        //组装未绑定水库的水位站
        Set<String> mainStationCodes = reservoirs.stream().filter(reservoirResVo -> StringUtils.isNotBlank(reservoirResVo.getMainStcd())).map(ReservoirResVo::getMainStcd).collect(Collectors.toSet());
        List<RainfallStationResVo> unBindStations = stations.stream().filter(rainfallStationResVo -> StringUtils.isBlank(rainfallStationResVo.getProjCode()) && !mainStationCodes.contains(rainfallStationResVo.getStCode())).collect(Collectors.toList());
        JSONObject unBindObject = new JSONObject();
        unBindObject.put("key","未绑定");
        unBindObject.put("number",unBindStations.size());
        unBindObject.put("children",null);
        Collections.reverse(array);
        array.add(unBindObject);
        total.getAndAdd(unBindStations.size());

        JSONObject object = new JSONObject();
        object.put("key","全部水库");
        object.put("number",stations.size());
        object.put("children",array);
        return object;
    }

    @Override
    public PageVO<RainfallStationResVo> stationList(StationQueryVo queryVo) {
        if (queryVo.getPageNum() != 0 && queryVo.getPageSize() != 0){
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        List<RainfallStationResVo> stations = stBaseMapper.selectByCondition(queryVo);
        Set<String> set = stBaseMapper.selectByCondition(queryVo).stream().map(RainfallStationResVo::getStCode).collect(Collectors.toSet());
        if (StringUtils.isNotBlank(queryVo.getProjectCode())){
            //水库主测站标记
            String mainStationCode = projectMapper.selectReservoirs(StringUtils.EMPTY, queryVo.getProjectCode()).get(0).getMainStcd();
            if (StringUtils.isNotBlank(mainStationCode)){
                if (!set.contains(mainStationCode)){
                    if (queryVo.getPageNum() != 0 && queryVo.getPageSize() != 0){
                        PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
                    }
                    StationQueryVo stationQueryVo = new StationQueryVo();
                    set.add(mainStationCode);
//                    BeanUtils.copyProperties(queryVo,stationQueryVo);
                    stationQueryVo.setStationCode(set.stream().collect(Collectors.joining(",")));
                    stations = stBaseMapper.selectByCondition(stationQueryVo);
                }
                for (RainfallStationResVo station : stations) {
                    String stationCode = station.getStCode();
                    if (mainStationCode.equals(stationCode)){
                        station.setMainStation(Boolean.TRUE);
                    }
                }
            }
        }
        findSynData(stations,set,queryVo.getStationType(),queryVo);
        return PageUtils.getPageVO(stations);
    }

    /**
     * 查找测站的数据
     * @param stations
     */
    private void findSynData(List<RainfallStationResVo> stations,
                             Set<String> codesSet, String stationType,StationQueryVo queryVo) {
        String table;
        switch (stationType) {
            case "R3":
            case "R2":
            case "RR":
                table = StationType.RESERVOIR.table();
                break;
            case "ZZ":
                table = StationType.RIVER.table();
                break;
            case "PP":
                table = StationType.RAINFALL.table();
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
        table = table.concat(LocalDate.now().format(yyyyMM));
        List<String> codeList = stations.stream().map(e -> e.getStCode()).filter(s -> s.startsWith("SWZ_")).collect(Collectors.toList());
        List<String> codeYJList = stations.stream().map(e -> e.getStCode()).filter(s -> s.startsWith("YJ_")).collect(Collectors.toList());
        if (stationType.equals(StationType.RESERVOIR.type())) {
            List<StRsvrR> resLevel = rainAndWaterMapper.selectRsvrWaterLevelByTimeCode(table, codeList, null);
            List<StRsvrR> resYJLevel = rainAndWaterMapper.selectRsvrWaterLevelByTimeCode("st_rsvr_r_yongjia", codeYJList, null);
            Map<String, List<StRsvrR>> map = resLevel.stream().collect(Collectors.groupingBy(StRsvrR::getMgstcd));
            Map<String, List<StRsvrR>> yjMap = resYJLevel.stream().collect(Collectors.groupingBy(StRsvrR::getMgstcd));
            for (RainfallStationResVo station : stations) {
                String stCode = station.getStCode();
                List<StRsvrR> list = null;
                if (stCode.startsWith("SWZ_")){
                    list = map.get(stCode);
                } else if (stCode.startsWith("YJ_")){
                    list = yjMap.get(stCode);
                }
                if (list != null) {
                    double waterLevel = list.get(0).getRz().doubleValue();
                    station.setWaterLevel(waterLevel);
                }
            }
        } else if (stationType.equals(StationType.RIVER.type())) {
            List<StRiverR> riverLevel = rainAndWaterMapper.selectRiverByTimeCode(table, codeList, null);
            Map<String, List<StRiverR>> map = riverLevel.stream().collect(Collectors.groupingBy(StRiverR::getStcd));

            for (RainfallStationResVo station : stations) {
                String stationCode = station.getStCode();
                List<StRiverR> list = map.get(stationCode);
                if (list != null) {
                    double waterLevel = list.get(0).getZ().doubleValue();
                    station.setWaterLevel(waterLevel);
                }
            }
        } else if (stationType.equals(StationType.RAINFALL.type())) {
            //雨量站 查询前3天的累计雨量
            Date currentDate = new Date();
            Date priorDate = MydateUtil.getNextDay(currentDate, null,null,-3,null,null,null);
            String[] tableNames = DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(), DateUtils.format(priorDate, "yyyy-MM-dd HH:mm:ss"),
                    DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
            Map<String, List<StPptnR>> yjMap;
            if (!codeYJList.isEmpty()){
                codeYJList = codeYJList.stream().map(code -> code.replaceAll("YJ_","")).collect(Collectors.toList());
                List<StPptnR> stPptnRYJList = rainAndWaterMapper.selectYongJiaRainfall(codeYJList, DateUtils.format(priorDate, "yyyy-MM-dd HH:mm:ss"), DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
                yjMap = stPptnRYJList.stream().collect(Collectors.groupingBy(StPptnR::getMgstcd));
            } else {
                yjMap = Collections.emptyMap();
            }
            List<StPptnR> stPptnRList = waterRuleConfigurationMapper.selectHourRainfall(Arrays.asList(tableNames),
                    DateUtils.format(priorDate, "yyyy-MM-dd HH:mm:ss"), DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss"), codeList.stream().collect(Collectors.joining(",")));

//            Map<String, List<com.ygkj.project.model.StPptnR>> map = stPptnRList.stream().filter(e->StringUtils.isNotBlank(e.getStcd())).collect(Collectors.groupingBy(com.ygkj.project.model.StPptnR::getStcd));
            Map<String, List<StPptnR>> map = stPptnRList.stream().collect(Collectors.groupingBy(StPptnR::getMgstcd));

            for (RainfallStationResVo station : stations) {
                String stationCode = station.getStCode();
                List<StPptnR> rain = null;
                if (stationCode.startsWith("SWZ_")){
                    rain = map.get(stationCode);
                } else if (stationCode.startsWith("YJ_")){
                    rain = yjMap.get(stationCode.replaceAll("YJ_",""));
                }
                if (rain != null) {
                    double rainfall = rain.get(0).getDrp().doubleValue();
                    station.setCumulativeRainfall(rainfall);
                }
            }
        } else if (stationType.equals(StationType.TIDE.type())) {
            List<StTideR> wasList = rainAndWaterMapper.selectTideByTimeCode(table, codeList, null);
            Map<String, List<StTideR>> map = wasList.stream().collect(Collectors.groupingBy(StTideR::getMgstcd));
            for (RainfallStationResVo station : stations) {
                String stationCode = station.getStCode();
                List<StTideR> stTideRList = map.get(stationCode);
                if (stTideRList != null) {
                    StTideR stTideR = stTideRList.get(0);
                    station.setTdz(stTideR.getTdz().doubleValue());
                }
            }
        } else if (stationType.equals(StationType.GATE.type())) {
            List<StWasR> wasList = rainAndWaterMapper.selectWasByTimeCode(table, codeList, null);
            Map<String, List<StWasR>> map = wasList.stream().collect(Collectors.groupingBy(StWasR::getStcd));
            for (RainfallStationResVo station : stations) {
                String stationCode = station.getStCode();
                List<StWasR> was = map.get(stationCode);
                if (was != null) {
                    StWasR stWasR = was.get(0);
                    station.setUpz(stWasR.getUpz().doubleValue());
                    station.setDwz(stWasR.getDwz() == null ? 0d : stWasR.getDwz().doubleValue());
                    station.setOpenGate(stWasR.getOvs() == null ? "0" : stWasR.getOvs());
                    station.setFlow(stWasR.getMxgtq() == null ? "0" : stWasR.getMxgtq());
                    station.setLl(stWasR.getLl());
                }
            }
        }
    }

    @Override
    public boolean focus(String stationCode, Boolean focus) {
        if (StringUtils.isEmpty(stationCode) || focus == null){
            throw new IllegalArgumentException("参数不正确");
        }
        AttStBase attStBase = new AttStBase();
        attStBase.setStCode(stationCode);
        attStBase.setIsFocus(focus);
        return stBaseMapper.update(attStBase) > 0;
    }

    @Override
    public boolean display(String stationCode, Boolean display) {
        if (StringUtils.isEmpty(stationCode) || display == null){
            throw new IllegalArgumentException("参数不正确");
        }
        String[] stationCodes = stationCode.split(",");
        return stBaseMapper.batchUpdate(stationCodes,display? 1:0) > 0;
    }

    @Override
    public boolean reservoirRainfallSurfaceCalculate(String stationCode, Boolean reservoirSurfaceRainStation) {
        AttStBase attStBase = new AttStBase();
        attStBase.setStCode(stationCode);
        attStBase.setIsReservoirSurfaceRainStation(reservoirSurfaceRainStation);
        return stBaseMapper.update(attStBase) > 0;
    }

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean bindReservoirStation(String reservoirCode, String stationCode) {
        if (StringUtils.isEmpty(reservoirCode)){
            throw new IllegalArgumentException("参数不正确");
        }
        stBaseMapper.cancelBind(reservoirCode);
        if (!StringUtils.isEmpty(stationCode)){
            AttStBase attStBase = new AttStBase();
            attStBase.setStCode(stationCode);
            attStBase.setProjCode(reservoirCode);
            return stBaseMapper.update(attStBase) > 0;
        }
        return true;
    }

    @Override
    public boolean updateReservoirMainStation(String reservoirCode, String stationCode) {
        if (StringUtils.isEmpty(stationCode) || StringUtils.isEmpty(reservoirCode)){
            throw new IllegalArgumentException("参数不正确");
        }
        return projectMapper.updateReservoirMainStation(reservoirCode, stationCode) > 0;
    }

    @Override
    public PageVO<AssoStationReservoirRespVo> assoStationReservoirList(AssoStationReservoirReqVo vo) {
        if (vo.getPageNum() != 0 && vo.getPageSize() != 0){
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<AssoStationReservoirRespVo> attStBases = stBaseMapper.assoStationReservoirList(vo);
        return PageUtils.getPageVO(attStBases);
    }

    @Override
    public void bindBatchReservoirStation(String reservoirCode, String stationCodes) {
        if (StringUtils.isEmpty(stationCodes) || StringUtils.isEmpty(reservoirCode)){
            throw new IllegalArgumentException("参数不正确");
        }
        String[] codes = stationCodes.split(",");
        for (String code : codes) {
            AttStBase attStBase = new AttStBase();
            attStBase.setStCode(code);
            attStBase.setProjCode(reservoirCode);
            stBaseMapper.update(attStBase);
        }
    }

    @Override
    public List<com.ygkj.gragh.model.AttStBase> unBindReservoirStationInfo() {
        List<ReservoirResVo> reservoirs = projectMapper.selectReservoirs(StringUtils.EMPTY,StringUtils.EMPTY);
        List<String> mainStationCodes = reservoirs.stream().filter(reservoirResVo -> StringUtils.isNotBlank(reservoirResVo.getMainStcd())).map(ReservoirResVo::getMainStcd).collect(Collectors.toList());
        List<com.ygkj.gragh.model.AttStBase> attStBases = stBaseMapper.unBindStationCodes(mainStationCodes);
        List<String> stationCodes = attStBases.stream().filter(attStBase -> attStBase.getStCode().startsWith("SWZ_"))
                .map(com.ygkj.gragh.model.AttStBase::getStCode).collect(Collectors.toList());
        List<String> stationYJCodes = attStBases.stream().filter(attStBase -> attStBase.getStCode().startsWith("YJ_"))
                .map(com.ygkj.gragh.model.AttStBase::getStCode).collect(Collectors.toList());
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        int month = calendar.get(Calendar.MONTH) + 1;
        String table = StationType.RESERVOIR.table() + calendar.get(Calendar.YEAR) + (month < 10 ? "0" + month : "" + month);
        List<WaterLevelResVo> waterLevels = rainAndWaterMapper.selectNewestReserviorWaterLevelFromTable(table, stationCodes);
        List<StRsvrR> yjWaterLevels = rainAndWaterMapper.selectRsvrWaterLevelByTimeCode("st_rsvr_r_yongjia", stationYJCodes, null);
        Map<String, Double> map = waterLevels.stream().collect(Collectors.toMap(WaterLevelResVo::getStcd, waterLevelResVo -> Double.parseDouble(waterLevelResVo.getRz())));
        Map<String, Double> yjMap = yjWaterLevels.stream().collect(Collectors.toMap(StRsvrR::getMgstcd, StRsvrR::getRz));
        for (com.ygkj.gragh.model.AttStBase station : attStBases) {
            String stationCode = station.getStCode();
            if (stationCode.startsWith("SWZ_")){
                station.setWaterLevel(map.getOrDefault(stationCode,0d));
            } else if (stationCode.startsWith("YJ_")){
                station.setWaterLevel(yjMap.getOrDefault(stationCode,0d));
            }
        }
        return attStBases;
    }
}
