package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.*;
import com.ygkj.big.screen.mapper.*;
import com.ygkj.big.screen.service.MeteorologicVisualizationService;
import com.ygkj.big.screen.feign.*;
import com.ygkj.big.screen.mapper.*;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.request.StQueryReqVo;
import com.ygkj.big.screen.vo.request.SynRainRequestVo;
import com.ygkj.big.screen.vo.request.SynWaterRequestVo;
import com.ygkj.enums.StationType;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/4/20
 */
@Service
public class MeteorologicVisualizationServiceImpl implements MeteorologicVisualizationService {

    @Resource
    MeteorologicVisualizationClient mvClient;

    @Resource
    GraphClient graphClient;

    @Resource
    FloodProtectClient floodClient;

    @Resource
    FloodRescueMapper rescueMapper;

    @Resource
    AttAdcdBaseMapper adcdMapper;

    @Resource
    ProjectMapper projectMapper;


    @Resource
    MeteorologicMapper meteorologicMapper;

    @Resource
    ProjectManageClient projectManageClient;

    @Resource
    AttStBaseMapper stMapper;

    @Resource
    WaterAndRainMapper waterAndRainMapper;

    @Resource
    CommonServiceClient commonServiceClient;


    @Override
    public List<JSONObject> forecast() {
        CommonResult<JSONArray> response = mvClient.weatherPredict();
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
            JSONArray respTemp = response.getData();
            List<JSONObject> dataList = JSONArray.parseArray(JSONObject.toJSONString(respTemp)).toJavaList(JSONObject.class);
            List<JSONObject> result = new ArrayList<>(7);
            LocalDate today = LocalDate.now();
            int i = 0;
            for (JSONObject data : dataList) {
                JSONObject temp = new JSONObject();
                temp.put("weekDay", DateUtils.getWeekday(today.plusDays(i++)));
                // todo 这里本来是需要放url的，但是目前没有url,待定
                temp.put("weather", data.getString("wpname"));
                Double max = data.getDouble("maxTemp");
                Double min = data.getDouble("minTemp");
                if (max != null && min != null) {
                    temp.put("temp", ((int) (max + min) / 2) + "℃");
                } else if (max != null && min == null) {
                    temp.put("temp", max.intValue() + "℃");
                } else if (max == null && min != null) {
                    temp.put("temp", min.intValue() + "℃");
                } else if (max == null && min == null) {
                    temp.put("temp", StringUtils.EMPTY);
                }
                result.add(temp);
            }
            while (result.size() < 7) {
                JSONObject temp = new JSONObject();
                temp.put("weekDay", DateUtils.getWeekday(today.plusDays(i++)));
                temp.put("weather", StringUtils.EMPTY);
                temp.put("temp", StringUtils.EMPTY);
                result.add(temp);
            }
            return result;
        }
        return Collections.emptyList();
    }

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Override
    public List<JSONObject> meteorologicStatistic(String type) {
        CommonResult<List<LinkedHashMap>> response = mvClient.weatherStatistic("温州市", type);
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
//            List<JSONObject> dataRes = response.getData();

            List<LinkedHashMap> respTemp = response.getData();
            List<JSONObject> dataList = JSONArray.parseArray(JSONObject.toJSONString(respTemp)).toJavaList(JSONObject.class);
            List<JSONObject> result = new ArrayList<>(dataList.size());
            String s = StringUtils.EMPTY;
            if (type.equals("温度")) {
                s = "温度(℃)";
            } else if (type.equals("降雨量")) {
                s = "降水量(mm)";
            } else if (type.equals("风速")) {
                s = "风速(级)";
            }
            LocalDateTime now = LocalDateTime.now();
            for (JSONObject data : dataList) {
                JSONObject temp = new JSONObject();
                temp.put("x", dtf.format(now.withHour(data.getIntValue("hour"))));
                temp.put("y", data.getDouble("value"));
                temp.put("s", s);
                temp.put("title", "");
                result.add(temp);
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<JSONObject> typhoonStatistic() {
        JSONObject result = new JSONObject();
        result.put("level", StringUtils.EMPTY);
        result.put("distance", StringUtils.EMPTY);
        result.put("name", StringUtils.EMPTY);
        CommonResult<List<LinkedHashMap>> response = mvClient.currentTyphoon();
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
            List<LinkedHashMap> respTemp = response.getData();
            List<JSONObject> dataList = JSONArray.parseArray(JSONObject.toJSONString(respTemp)).toJavaList(JSONObject.class);
            JSONObject temp = dataList.get(0);
            JSONArray points = temp.getJSONArray("points");
            result.put("name", temp.getString("name") + "(" + temp.getString("ename") + ")");
            if (CollectionUtils.isNotBlank(points)) {
                JSONObject prePoint = null;
                for (Object o : points) {
                    JSONObject point = (JSONObject) o;
                    if (prePoint == null) {
                        prePoint = point;
                    } else {
                        if (CollectionUtils.isNotBlank(point.getJSONArray("forecast"))) {
                            break;
                        } else {
                            prePoint = point;
                        }
                    }
                }
                if (prePoint != null) {
                    String power = prePoint.getString("power");
                    String distance = prePoint.getString("distance");
                    result.put("level", StringUtils.isBlank(power) ? StringUtils.EMPTY : power);
                    result.put("distance", StringUtils.isBlank(distance) ? StringUtils.EMPTY : (distance + "KM"));
                }
            }
        }
        return new ArrayList<>(Arrays.asList(result));
    }

    @Override
    public List<JSONObject> typhoonRecent() {
        Map<String, Object> record = meteorologicMapper.selectRecentTyphoonCode();
        if (record == null) {
            return Collections.emptyList();
        }
        JSONObject result = new JSONObject();
        Object ename = record.get("english_name");
        String name = record.get("name").toString();
        if (ename != null && StringUtils.isNotBlank(ename.toString())) {
            name += ("(" + ename.toString() + ")");
        }
        result.put("name", name);
        return new ArrayList<>(Arrays.asList(result));
    }

    @Override
    public List<JSONObject> similarTyphoon(String type) {
        List<JSONObject> result = new ArrayList<>();
        Map<String, Object> record = meteorologicMapper.selectRecentTyphoonCode();
        if (record == null) {
            return result;
        }
        String typhoonCode = record.get("code").toString();
        if (StringUtils.isNotBlank(typhoonCode)
                && StringUtils.isNotBlank(type)) {
            CommonResult<LinkedHashMap> temp = mvClient.analysis(typhoonCode, type);
            if (temp.getCode() == CommonResult.success().getCode()
                    && temp.getData() != null) {
                LinkedHashMap respTemp = temp.getData();
                JSONObject a = JSONObject.parseObject(JSONObject.toJSONString(respTemp));
                JSONArray finalData = a.getJSONArray("data");
                if (CollectionUtils.isNotBlank(finalData)) {
                    for (Object fo : finalData) {
                        JSONObject fd = (JSONObject) fo;
                        JSONObject tempResult = new JSONObject();
                        tempResult.put("name", fd.getString("name"));
                        tempResult.put("code", fd.getString("tfbh"));
                        Date begin = fd.getDate("begin_time");
                        Date end = fd.getDate("end_time");
                        if (begin != null && end != null) {
                            Duration duration = Duration.between(DateUtils.date2LocalDateTime(begin), DateUtils.date2LocalDateTime(end));
                            double days = duration.toHours() / 24;
                            tempResult.put("duration", days);
                        } else {
                            tempResult.put("duration", 0);
                        }
                        result.add(tempResult);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<JSONObject> realtimeRainfall() {
        List<AttStBase> stBases = rescueMapper.queryStByCondtion(Builder.of(StQueryReqVo::new).with(StQueryReqVo::setStType,
                StationType.RAINFALL.type()).build());
        if (CollectionUtils.isBlank(stBases)) {
            return Collections.emptyList();
        }
        SynRainRequestVo req = new SynRainRequestVo();
        req.setMin(-10);
        req.setMax(100000);
        req.setMinutes(60);
        CommonResult<List<StPptnR>> res1h = graphClient.rainfall(req);
        List<StPptnR> data1h = Collections.emptyList();
        if (res1h.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(res1h.getData())) {
            data1h = res1h.getData();
        }
        Map<String, StPptnR> map1h = data1h.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> o));
        req.setMinutes(180);
        CommonResult<List<StPptnR>> res3h = graphClient.rainfall(req);
        List<StPptnR> data3h = Collections.emptyList();
        if (res3h.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(res3h.getData())) {
            data3h = res3h.getData();
        }
        Map<String, StPptnR> map3h = data3h.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> o));
        req.setMinutes(360);
        CommonResult<List<StPptnR>> res6h = graphClient.rainfall(req);
        List<StPptnR> data6h = Collections.emptyList();
        if (res6h.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(res6h.getData())) {
            data6h = res1h.getData();
        }
        Map<String, StPptnR> map6h = data6h.stream().collect(Collectors.toMap(StPptnR::getStcd, o -> o));
        List<JSONObject> result = new ArrayList<>(stBases.size());
        for (AttStBase st : stBases) {
            JSONObject temp = new JSONObject();
            temp.put("name", st.getStName());
            StPptnR drp1h = map1h.get(st.getStCode());
            if (drp1h != null && drp1h.getDrp() >= 0) {
                temp.put("rain1h", drp1h.getDrp());
            } else {
                temp.put("rain1h", 0);
            }
            StPptnR drp3h = map3h.get(st.getStCode());
            if (drp3h != null && drp3h.getDrp() >= 0) {
                temp.put("rain3h", drp3h.getDrp());
            } else {
                temp.put("rain3h", 0);
            }
            StPptnR drp6h = map6h.get(st.getStCode());
            if (drp6h != null && drp6h.getDrp() >= 0) {
                temp.put("rain6h", drp6h.getDrp());
            } else {
                temp.put("rain6h", 0);
            }
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<JSONObject> forecastRainfall() {
        List<AttAdcdBase> adcdList = adcdMapper.queryAllAdOfWz();
        adcdList = adcdList == null ? Collections.emptyList() : adcdList;
        List<JSONObject> result = new ArrayList<>(adcdList.size());
        for (AttAdcdBase adcd : adcdList) {
            if (StringUtils.isBlank(adcd.getLgtd()) || StringUtils.isBlank(adcd.getLttd())) {
                continue;
            }
            CommonResult<LinkedHashMap> forest = mvClient.caculateRainfall(String.valueOf(adcd.getLgtd()), String.valueOf(adcd.getLttd()));
            JSONObject temp = new JSONObject();
            temp.put("name", adcd.getAdnm());
            if (CommonResult.success().getCode() == forest.getCode()
                    && forest.getData() != null) {
                JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(forest.getData()));
                try {
                    temp.put("rain1h", data.getDoubleValue("rainfall_1"));
                } catch (Exception e) {
                    temp.put("rain1h", 0D);
                }
                try {
                    temp.put("rain3h", data.getDoubleValue("rainfall_3"));
                } catch (Exception e) {
                    temp.put("rain3h", 0D);
                }
                try {
                    temp.put("rain6h", data.getDoubleValue("rainfall_6"));
                } catch (Exception e) {
                    temp.put("rain6h", 0D);
                }
            } else {
                temp.put("rain1h", 0);
                temp.put("rain3h", 0);
                temp.put("rain6h", 0);
            }
            result.add(temp);
        }
        return result;
    }

    final static String[] rainfallType = new String[]{"0-10mm", "10-25mm", "25-50mm", "50-100mm", "100-250mm"};

    @Override
    public List<JSONObject> rainfallStatistic(String type) {
        if (StringUtils.isBlank(type)) {
            return Collections.emptyList();
        }
        List<JSONObject> result = new ArrayList<>();
        SynRainRequestVo req = new SynRainRequestVo();
        req.setMin(-10);
        req.setMax(100000);
        switch (type) {
            case "1h":
                req.setMinutes(60);
                break;
            case "3h":
                req.setMinutes(180);
                break;
            case "24h":
                req.setMinutes(1440);
                break;
            default:
                return Collections.emptyList();
        }
        List<StPptnR> stationRainfall = Collections.emptyList();
        CommonResult<List<StPptnR>> response = graphClient.rainfall(req);
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
            stationRainfall = response.getData();
        }
        if (stationRainfall == null) {
            stationRainfall = Collections.emptyList();
        }
        Map<String, List<StPptnR>> tempMap = stationRainfall.stream().collect(Collectors.groupingBy(item -> {
            if (item.getDrp() == null) {
                return rainfallType[0];
            } else if (item.getDrp() < 10) {
                return rainfallType[0];
            } else if (item.getDrp() >= 10 && item.getDrp() < 25) {
                return rainfallType[1];
            } else if (item.getDrp() >= 25 && item.getDrp() < 50) {
                return rainfallType[2];
            } else if (item.getDrp() >= 50 && item.getDrp() < 100) {
                return rainfallType[3];
            } else {
                return rainfallType[4];
            }
        }));
        int total = stationRainfall.size();
        for (String s : rainfallType) {
            JSONObject temp = new JSONObject();
            temp.put("x", s);
            List<StPptnR> tempList = tempMap.getOrDefault(s, Collections.emptyList());
            if (total != 0) {
                temp.put("y", tempList.size());
            } else {
                temp.put("y", "0%");
            }
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<JSONObject> warnStatsNum(String type) {
        Integer warnNum = 0;
        if ("水位".equals(type)) {
            CommonResult<JSONObject> response = floodClient.overWarnStatistics();
            if (response.getCode() == CommonResult.success().getCode()
                    && response.getData() != null) {
                JSONObject temp = response.getData();
                warnNum += temp.getIntValue("limitNum");
                warnNum += temp.getIntValue("warnNum");
                warnNum += temp.getIntValue("guaranteeNum");
            }
        } else {
            SynRainRequestVo req = new SynRainRequestVo();
            req.setMin(-10);
            req.setMax(100000);
            int hours = 0;
            switch (type) {
                case "1h":
                    hours = 1;
                    break;
                case "3h":
                    hours = 3;
                    break;
                case "24h":
                    hours = 24;
                    break;
                default:
                    break;
            }

            CommonResult<Integer> response = floodClient.getRainWarningStationNum(hours);
            if (response.getCode() == CommonResult.success().getCode()
                    && response.getData() == null) {
                warnNum = response.getData();
            }
        }
        return Arrays.asList(Builder.of(JSONObject::new).with(JSONObject::put, "warnNum", warnNum).build());
    }

    /**
     * 水库纳蓄能力(type=1)/剩余库容(type=2)/已拦蓄水量(type=3)之和
     *
     * @param type
     * @return
     */
    @Override
    public List<JSONObject> rsvrStorageTotal(Integer type) {
        JSONObject result = new JSONObject();
        double total = 0D;
        switch (type) {
            case 1: {
                result.put("name", "纳蓄能力");
                CommonResult<List<LinkedHashMap>> response = floodClient.countStorage(2, 6);
                if (response.getCode() == CommonResult.success().getCode()
                        && CollectionUtils.isNotBlank(response.getData())) {
                    List<JSONObject> data = JSONArray.parseArray(JSONObject.toJSONString(response.getData())).toJavaList(JSONObject.class);
                    for (JSONObject o : data) {
                        total += o.getDoubleValue("amount");
                    }
                    result.put("value", total / data.size());
                } else {
                    result.put("value", 0
                    );
                }
            }
            break;
            case 2: {
                result.put("name", "剩余库容");
                CommonResult<List<LinkedHashMap>> response = floodClient.countStorage(1, 6);
                if (response.getCode() == CommonResult.success().getCode()
                        && CollectionUtils.isNotBlank(response.getData())) {
                    List<JSONObject> data = JSONArray.parseArray(JSONObject.toJSONString(response.getData())).toJavaList(JSONObject.class);
                    for (JSONObject o : data) {
                        total += (o.getDoubleValue("totCap") - o.getDoubleValue("amount"));
                    }
                    result.put("value", total);
                } else {
                    result.put("value", 0);
                }
            }
            break;
            case 3: {
                result.put("name", "已拦蓄水量");
                /**
                 * 已拦需水量=当前水位库容-台风生成时间的水位库容
                 * 1. 先拿当前台风生成时间
                 * 2. 查询当前大中型水库的水位库容
                 * 3. 查询当前大中型水库的水位库容
                 * 4. 求差
                 */
                mvClient.currentTyphoon();
                Date typhoonBeginTime = null;
                CommonResult<List<LinkedHashMap>> typhResp = mvClient.currentTyphoon();
                if (typhResp.getCode() == CommonResult.success().getCode()
                        && CollectionUtils.isNotBlank(typhResp.getData())) {
                    LinkedHashMap respTemp = typhResp.getData().get(0);
                    JSONObject temp = JSONObject.parseObject(JSONObject.toJSONString(respTemp));
                    typhoonBeginTime = temp.getDate("begin_time");
                }
//                typhoonBeginTime=DateUtils.localDateTime2Date(LocalDateTime.now().minusDays(3));
                if (typhoonBeginTime == null) {
                    result.put("value", BigDecimal.ZERO);
                    break;
                }
                List<AttResBase> rsvrList = this.projectMapper.selectLargeMidRsvrByCode();
                rsvrList = rsvrList == null ? Collections.emptyList() : rsvrList;

                String projCode = rsvrList.stream()
                        .filter(o -> o != null && StringUtils.isNotBlank(o.getResCode()))
                        .map(AttResBase::getResCode)
                        .collect(Collectors.joining(","));
                List<AttStBase> stList = this.stMapper.selectByCondition(Builder.of(HashMap<String, Object>::new)
                        .with(HashMap::put, "projCodes", projCode)
                        .build());
                stList = stList == null ? Collections.emptyList() : stList;
                Map<String, List<String>> projStMap = stList.stream()
                        .collect(Collectors.groupingBy(AttStBase::getProjCode,
                                Collectors.mapping(AttStBase::getStCode,
                                        Collectors.toList())));
                Map<String, BigDecimal> divideMap = new HashMap<>(stList.size());
                if (typhoonBeginTime != null) {
                    List<String> stcds = stList.stream()
                            .filter(o -> o != null && StringUtils.isNotBlank(o.getStCode()))
                            .map(AttStBase::getStCode)
                            .collect(Collectors.toList());
                    LocalDateTime nowLdt = LocalDateTime.now();
                    LocalDateTime typhLdt = DateUtils.date2LocalDateTime(typhoonBeginTime);
                    List<StRsvrR> beginLevel = waterAndRainMapper.selectRecentRsvrWaterLevelByDate(Arrays.asList(StationType.RESERVOIR.table() + typhLdt.format(yyyyMM),
                            StationType.RESERVOIR.table() + typhLdt.minusHours(1).format(yyyyMM))
                            .stream().distinct().collect(Collectors.toList()), stcds, typhoonBeginTime);
                    Map<String, Double> beginLevelMap = beginLevel.stream().collect(Collectors.toMap(StRsvrR::getStcd, StRsvrR::getRz));
                    Map<String, BigDecimal> beginCapacityMap = caculateCapactiy(beginLevelMap);
                    List<StRsvrR> nowCapcity = waterAndRainMapper.selectNewestRsvrWaterLevel(Arrays.asList(StationType.RESERVOIR.table() + nowLdt.format(yyyyMM),
                            StationType.RESERVOIR.table() + nowLdt.minusHours(1).format(yyyyMM))
                            .stream().distinct().collect(Collectors.toList()), stcds);
                    Map<String, Double> nowLevelMap = nowCapcity.stream().collect(Collectors.toMap(StRsvrR::getStcd, StRsvrR::getRz));
                    Map<String, BigDecimal> nowCapacityMap = caculateCapactiy(nowLevelMap);
                    for (String stcd : stcds) {
                        BigDecimal begin = beginCapacityMap.getOrDefault(stcd, BigDecimal.ZERO);
                        BigDecimal now = nowCapacityMap.getOrDefault(stcd, BigDecimal.ZERO);
                        divideMap.put(stcd, now.divide(begin));
                    }
                }
                BigDecimal totalValue = BigDecimal.ZERO;
                for (AttResBase rsvr : rsvrList) {
                    List<String> stcds = projStMap.getOrDefault(rsvr.getResCode(), Collections.emptyList());
                    if (CollectionUtils.isNotBlank(stcds)) {
                        totalValue = totalValue.add(divideMap.getOrDefault(stcds.get(0), BigDecimal.ZERO));
                    }
                }
                result.put("value", totalValue);
            }
            break;
            default:
                break;
        }
        return Arrays.asList(result);
    }

    DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");

    /**
     * 水库纳蓄能力(type=1)/剩余库容(type=2)/已拦蓄水量(type=3)/最快达到汛限水位水库(type=4)列表
     *
     * @param type
     * @return
     */
    @Override
    public List<JSONObject> rsvrStorageList(Integer type) {
        List<JSONObject> result = new ArrayList<>();
        switch (type) {
            case 1: {
                CommonResult<List<LinkedHashMap>> response = floodClient.countStorage(2, 6);
                if (response.getCode() == CommonResult.success().getCode()
                        && CollectionUtils.isNotBlank(response.getData())) {
                    List<JSONObject> data = JSONArray.parseArray(JSONObject.toJSONString(response.getData())).toJavaList(JSONObject.class);
                    for (JSONObject o : data) {
                        JSONObject temp = new JSONObject();
                        temp.put("name", o.getString("resName"));
                        temp.put("value", o.getDoubleValue("amount"));
                        temp.put("limit", o.getDoubleValue("totCap"));
                        result.add(temp);
                    }
                    result = result.stream().sorted((o1, o2) -> o2.getDouble("value").compareTo(o1.getDouble("value"))).collect(Collectors.toList());
                }
            }
            break;
            case 2: {
                CommonResult<List<LinkedHashMap>> response = floodClient.countStorage(1, 6);
                if (response.getCode() == CommonResult.success().getCode()
                        && CollectionUtils.isNotBlank(response.getData())) {
                    List<JSONObject> data = JSONArray.parseArray(JSONObject.toJSONString(response.getData())).toJavaList(JSONObject.class);
                    for (JSONObject o : data) {
                        JSONObject temp = new JSONObject();
                        temp.put("name", o.getString("resName"));
                        temp.put("value", o.getDoubleValue("amount"));
                        temp.put("limit", o.getDoubleValue("totCap"));
                        result.add(temp);
                    }
                    result = result.stream().sorted((o1, o2) -> o2.getDouble("value").compareTo(o1.getDouble("value"))).collect(Collectors.toList());
                }
            }
            break;
            case 3: {
                /**
                 * 已拦需水量=当前水位库容-台风生成时间的水位库容
                 * 1. 先拿当前台风生成时间
                 * 2. 查询当前大中型水库的水位库容
                 * 3. 查询当前大中型水库的水位库容
                 * 4. 求差
                 */
                mvClient.currentTyphoon();
                Date typhoonBeginTime = null;
                CommonResult<List<LinkedHashMap>> typhResp = mvClient.currentTyphoon();
                if (typhResp.getCode() == CommonResult.success().getCode()
                        && CollectionUtils.isNotBlank(typhResp.getData())) {
                    LinkedHashMap respTemp = typhResp.getData().get(0);
                    JSONObject temp = JSONObject.parseObject(JSONObject.toJSONString(respTemp));
                    typhoonBeginTime = temp.getDate("begin_time");
                }
//                typhoonBeginTime=DateUtils.localDateTime2Date(LocalDateTime.now().minusDays(3));
                List<AttResBase> rsvrList = this.projectMapper.selectLargeMidRsvrByCode();
                rsvrList = rsvrList == null ? Collections.emptyList() : rsvrList;

                String projCode = rsvrList.stream()
                        .filter(o -> o != null && StringUtils.isNotBlank(o.getResCode()))
                        .map(AttResBase::getResCode)
                        .collect(Collectors.joining(","));
                List<AttStBase> stList = this.stMapper.selectByCondition(Builder.of(HashMap<String, Object>::new)
                        .with(HashMap::put, "projCodes", projCode)
                        .build());
                stList = stList == null ? Collections.emptyList() : stList;
                Map<String, List<String>> projStMap = stList.stream()
                        .collect(Collectors.groupingBy(AttStBase::getProjCode,
                                Collectors.mapping(AttStBase::getStCode,
                                        Collectors.toList())));
                Map<String, BigDecimal> subtractMap = new HashMap<>(stList.size());
                if (typhoonBeginTime != null) {
                    List<String> stcds = stList.stream()
                            .filter(o -> o != null && StringUtils.isNotBlank(o.getStCode()))
                            .map(AttStBase::getStCode)
                            .collect(Collectors.toList());
                    LocalDateTime nowLdt = LocalDateTime.now();
                    LocalDateTime typhLdt = DateUtils.date2LocalDateTime(typhoonBeginTime);
                    List<StRsvrR> beginLevel = waterAndRainMapper.selectRecentRsvrWaterLevelByDate(Arrays.asList(StationType.RESERVOIR.table() + typhLdt.format(yyyyMM),
                            StationType.RESERVOIR.table() + typhLdt.minusHours(1).format(yyyyMM))
                            .stream().distinct().collect(Collectors.toList()), stcds, typhoonBeginTime);
                    Map<String, Double> beginLevelMap = beginLevel.stream().collect(Collectors.toMap(StRsvrR::getStcd, StRsvrR::getRz));
                    Map<String, BigDecimal> beginCapacityMap = caculateCapactiy(beginLevelMap);
                    List<StRsvrR> nowCapcity = waterAndRainMapper.selectNewestRsvrWaterLevel(Arrays.asList(StationType.RESERVOIR.table() + nowLdt.format(yyyyMM),
                            StationType.RESERVOIR.table() + nowLdt.minusHours(1).format(yyyyMM))
                            .stream().distinct().collect(Collectors.toList()), stcds);
                    Map<String, Double> nowLevelMap = nowCapcity.stream().collect(Collectors.toMap(StRsvrR::getStcd, StRsvrR::getRz));
                    Map<String, BigDecimal> nowCapacityMap = caculateCapactiy(nowLevelMap);
                    for (String stcd : stcds) {
                        BigDecimal begin = beginCapacityMap.getOrDefault(stcd, BigDecimal.ZERO);
                        begin = begin == null ? BigDecimal.ZERO : begin;
                        BigDecimal now = nowCapacityMap.getOrDefault(stcd, BigDecimal.ZERO);
                        now = now == null ? BigDecimal.ZERO : now;
                        subtractMap.put(stcd, now.subtract(begin));
                    }
                }
                for (AttResBase rsvr : rsvrList) {
                    JSONObject temp = new JSONObject();
                    temp.put("name", rsvr.getResName());
                    List<String> stcds = projStMap.getOrDefault(rsvr.getResCode(), Collections.emptyList());
                    if (CollectionUtils.isNotBlank(stcds)) {
                        temp.put("value", subtractMap.getOrDefault(stcds.get(0), BigDecimal.ZERO));
                    } else {
                        temp.put("value", BigDecimal.ZERO);
                    }
                    result.add(temp);
                }
            }
            break;
            case 4: {
                CommonResult<List<LinkedHashMap>> topNRsvr = floodClient.topNReservoir(18);
                if (topNRsvr.getCode() == CommonResult.success().getCode()
                        && CollectionUtils.isNotBlank(topNRsvr.getData())) {
                    List<LinkedHashMap> respTemp = topNRsvr.getData();
                    result = JSONArray.parseArray(JSONObject.toJSONString(respTemp)).toJavaList(JSONObject.class);
                }
            }
            default:
                break;
        }
        return result;
    }


    /**
     * 根据水位计算库容
     *
     * @param rsvrLevel
     * @return
     */
    private Map<String, BigDecimal> caculateCapactiy(Map<String, Double> rsvrLevel) {
        Map<String, BigDecimal> map = new HashMap<>();
        if (CollectionUtils.isNotBlank(rsvrLevel)) {
            List<ReservoirCapacityCurve> list = new ArrayList<>();
            rsvrLevel.forEach((key, value) -> {
                ReservoirCapacityCurve reservoirCapacityCurve = new ReservoirCapacityCurve();
                reservoirCapacityCurve.setCode(key);
                reservoirCapacityCurve.setWaterLevel(new BigDecimal(value));
                list.add(reservoirCapacityCurve);
            });
            map = commonServiceClient.calculationCapacity(list).getData();
        }
        return map;
    }

    @Override
    public List<JSONObject> realtimeRainSt() {
        List<JSONObject> result = new ArrayList<>();
        SynRainRequestVo reqVo = new SynRainRequestVo();
        reqVo.setMin(-10);
        reqVo.setMax(100000);
        reqVo.setMinutes(60);
        CommonResult<List<StPptnR>> response = graphClient.rainfall(reqVo);
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
            List<StPptnR> data = response.getData();
            for (StPptnR d : data) {
                JSONObject o = new JSONObject();
                o.put("name", d.getStationName());
                o.put("rainfall", d.getDrp() == null ? 0 : d.getDrp());
                o.put("longitude", d.getLongitude());
                o.put("latitude", d.getLatitude());
                result.add(o);
            }
        }
        return result;
    }

    @Override
    public List<JSONObject> realtimeWaterSt() {
        List<JSONObject> result = new ArrayList<>();
        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.RIVER.type())
                .build());
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
            List<AttStBase> data = response.getData();
            for (AttStBase d : data) {
                JSONObject o = new JSONObject();
                o.put("name", d.getStName());
                o.put("waterLevel", d.getWaterLevel() < 0 ? 0 : d.getWaterLevel());
                o.put("longitude", d.getStLong());
                o.put("latitude", d.getStLat());
                o.put("warningLevel", d.getWarningLevel() == null ? 0 : d.getWarningLevel());
                if (d.getWarningLevel() != null && d.getWaterLevel() >= d.getWarningLevel()) {
                    o.put("warning", true);
                } else {
                    o.put("warning", false);
                }
                result.add(o);
            }
        }
        return result;
    }

    @Override
    public List<JSONObject> rsvrPoint() {
        List<JSONObject> result = new ArrayList<>();
        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setResGrade, "大中型水库")
                .with(SynWaterRequestVo::setStationType, StationType.RESERVOIR.type())
                .build());
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
            List<AttStBase> data = response.getData();
            String resCode = data.stream().map(AttStBase::getProjCode).filter(StringUtils::isNotBlank).collect(Collectors.joining(","));
            if (StringUtils.isNotBlank(resCode)) {
                List<AttResBase> rsvrList = projectMapper.selectRsvrByCode(resCode);
                if (CollectionUtils.isNotBlank(rsvrList)) {
                    Map<String, List<AttStBase>> stMap = data.stream().filter(o -> StringUtils.isNotBlank(o.getProjCode())).collect(Collectors.groupingBy((AttStBase::getProjCode)));
                    for (AttResBase d : rsvrList) {
                        JSONObject o = new JSONObject();
                        o.put("name", d.getResName());
                        o.put("longitude", d.getLowLeftLong());
                        o.put("latitude", d.getLowLeftLat());
                        List<AttStBase> stList = stMap.get(d.getResCode());
                        if (CollectionUtils.isNotBlank(stList)) {
                            AttStBase st = stList.get(0);
                            o.put("waterLevel", st.getWaterLevel() < 0 ? 0 : st.getWaterLevel());
                            o.put("warningLevel", st.getLimitLevel() == null ? 0 : st.getLimitLevel());
                            if (st.getLimitLevel() != null && st.getWaterLevel() >= st.getLimitLevel()) {
                                o.put("warning", true);
                            } else {
                                o.put("warning", false);
                            }
                        } else {
                            o.put("waterLevel", 0);
                            o.put("warningLevel", 0);
                            o.put("warning", false);
                        }
                        result.add(o);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<JSONObject> wagaPoint() {
        List<JSONObject> result = new ArrayList<>();
        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.GATE.type())
                .build());
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
            List<AttStBase> data = response.getData();
            for (AttStBase d : data) {
                JSONObject o = new JSONObject();
                o.put("name", d.getStName());
                o.put("waterLevel", d.getWaterLevel() < 0 ? 0 : d.getWaterLevel());
                o.put("longitude", d.getStLong());
                o.put("latitude", d.getStLat());
                o.put("warningLevel", d.getWarningLevel() == null ? 0 : d.getWarningLevel());
                if (d.getWarningLevel() != null && d.getWaterLevel() >= d.getWarningLevel()) {
                    o.put("warning", true);
                } else {
                    o.put("warning", false);
                }
                o.put("maxFlow", d.getFlow());
                result.add(o);
            }
        }
        return result;
    }

    @Override
    public List<JSONObject> floodPoint() {
        List<JSONObject> result = new ArrayList<>();
        CommonResult<PageVO<LinkedHashMap>> response = floodClient.newestFloodPoint(1, 10000);
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData().getList())) {
//            List<LinkedHashMap> data = response.getData().getList();

            List<LinkedHashMap> respTemp = response.getData().getList();
            List<JSONObject> dataList = JSONArray.parseArray(JSONObject.toJSONString(respTemp)).toJavaList(JSONObject.class);
            for (JSONObject d : dataList) {
                JSONObject o = new JSONObject();
                o.put("name", d.getString("name"));
                o.put("longitude", d.getString("longitude"));
                o.put("latitude", d.getString("latitude"));
                o.put("waterLevel", d.getString("waterLevel") + d.getString("waterLevelUnit"));
                result.add(o);
            }
        }
        return result;
    }

    @Override
    public List<JSONObject> innerRiverLevel() {
        List<JSONObject> result = new ArrayList<>();
        CommonResult<List<LinkedHashMap>> response = floodClient.watLevOfPlainBasInnerRiv(null, 3);
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
//            List<JSONObject> data = response.getData();
            List<JSONObject> data = JSONArray.parseArray(JSONObject.toJSONString(response.getData())).toJavaList(JSONObject.class);
            ;
            for (JSONObject d : data) {
                JSONObject o = new JSONObject();
                o.put("name", d.getString("stName"));
                o.put("longitude", d.getString("longitude"));
                o.put("latitude", d.getString("latitude"));
                o.put("waterLevel", d.getString("realLevel"));
                result.add(o);
            }
        }
        return result;
    }

    @Override
    public List<JSONObject> typhoonPath() {
        List<JSONObject> result = new ArrayList<>();
        CommonResult<List<LinkedHashMap>> response = mvClient.currentTyphoon();
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
//            JSONObject temp = response.getData().get(0);
            List<LinkedHashMap> respTemp = response.getData();
            List<JSONObject> dataList = JSONArray.parseArray(JSONObject.toJSONString(respTemp)).toJavaList(JSONObject.class);
            JSONObject temp = dataList.get(0);
            JSONArray points = temp.getJSONArray("points");
            if (CollectionUtils.isNotBlank(points)) {
                for (Object o : points) {
                    JSONObject point = (JSONObject) o;
                    if (CollectionUtils.isNotBlank(point.getJSONArray("forecast"))) {
                        break;
                    } else {
                        result.add(point);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<JSONObject> rsvrRisk(String type) {
        JSONObject result = new JSONObject();
        int totalNum = projectMapper.countRsvrNum();
        result.put("name", type);
        result.put("totalNum", totalNum);
        int warnNum = 0;
        result.put("warnNum", warnNum);
        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.RESERVOIR.type())
                .build());
        List<AttStBase> data = null;
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
            data = response.getData();
        }
        if (CollectionUtils.isBlank(data)) {
            return Arrays.asList(result);
        }
        String resCode = data.stream().map(AttStBase::getProjCode).filter(StringUtils::isNotBlank).collect(Collectors.joining(","));
        if (StringUtils.isBlank(resCode)) {
            return Arrays.asList(result);
        }
        List<AttResBase> rsvrList = projectMapper.selectRsvrByCode(resCode);
        if (CollectionUtils.isBlank(rsvrList)) {
            return Arrays.asList(result);
        }
        Map<String, List<AttStBase>> stMap = data.stream().filter(o -> StringUtils.isNotBlank(o.getProjCode())).collect(Collectors.groupingBy((AttStBase::getProjCode)));

        if ("超防洪高".equals(type)) {
            for (AttResBase d : rsvrList) {
                List<AttStBase> stList = stMap.get(d.getResCode());
                if (CollectionUtils.isNotBlank(stList)) {
                    for (AttStBase st : data) {
                        if (d.getUppLevFlco() != null && d.getUppLevFlco() > st.getWaterLevel()) {
                            warnNum++;
                            break;
                        }
                    }
                }
            }
        } else if ("超汛限".equals(type)) {
            for (AttResBase d : rsvrList) {
                List<AttStBase> stList = stMap.get(d.getResCode());
                if (CollectionUtils.isNotBlank(stList)) {
                    for (AttStBase st : data) {
                        if (d.getMeiFloodLimitWaterLevel() != null && d.getMeiFloodLimitWaterLevel() > st.getWaterLevel()) {
                            warnNum++;
                            break;
                        }
                    }
                }
            }
        }
        result.put("warnNum", warnNum);
        return Arrays.asList(result);
    }

    @Override
    public List<JSONObject> wagaRisk(String type) {
        JSONObject result = new JSONObject();
        int totalNum = projectMapper.countWagaNum();
        result.put("name", type);
        result.put("totalNum", totalNum);
        int warnNum = 0;
        result.put("warnNum", warnNum);
        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.RESERVOIR.type())
                .build());
        List<AttStBase> data = null;
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData())) {
            data = response.getData();
        }
        if (CollectionUtils.isBlank(data)) {
            return Arrays.asList(result);
        }
        String resCode = data.stream().map(AttStBase::getProjCode).filter(StringUtils::isNotBlank).collect(Collectors.joining(","));
        if (StringUtils.isBlank(resCode)) {
            return Arrays.asList(result);
        }
        List<AttWagaBase> wagaList = projectMapper.selectWagaByCode(resCode);
        if (CollectionUtils.isBlank(wagaList)) {
            return Arrays.asList(result);
        }
        Map<String, List<AttStBase>> stMap = data.stream().filter(o -> StringUtils.isNotBlank(o.getProjCode())).collect(Collectors.groupingBy((AttStBase::getProjCode)));
        if ("超警戒".equals(type)) {
            for (AttWagaBase d : wagaList) {
                List<AttStBase> stList = stMap.get(d.getWagaCode());
                if (CollectionUtils.isNotBlank(stList)) {
                    for (AttStBase st : data) {
                        if (st.getWarningLevel() != null && st.getWaterLevel() >= st.getWarningLevel()) {
                            warnNum++;
                            break;
                        }
                    }
                }
            }
        } else if ("超保证".equals(type)) {
            for (AttWagaBase d : wagaList) {
                List<AttStBase> stList = stMap.get(d.getWagaCode());
                if (CollectionUtils.isNotBlank(stList)) {
                    for (AttStBase st : data) {
                        if (st.getWarningLevel() != null && st.getWaterLevel() >= st.getWarningLevel()) {
                            warnNum++;
                            break;
                        }
                    }
                }
            }
        }
        result.put("warnNum", warnNum);
        return Arrays.asList(result);
    }

    /**
     * 排水能力
     * 温瑞平所有水闸最大过闸流量之和
     * 温瑞平所有水闸编号：
     * 83,1,2,3,4,5,6,8,9,10,11,12,13,14,15,16,17,18,31,20,33,21,19,22,55,24,39,71,35,23,25,26,27,28,29,30,38,40,41,42,43,45,46,47,49,50,51,52,53,54,56,58,59,61,63,64,84
     *
     * @return
     */
    @Override
    public List<JSONObject> drainageAbility() {
        List<JSONObject> result = new ArrayList<>();
        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.GATE.type())
                .build());
        if (CollectionUtils.isNotBlank(response.getData())) {
            List<AttStBase> data = response.getData();
            Map<String, AttStBase> wagaStMap = data.stream().collect(Collectors.toMap(AttStBase::getStCode, o -> o));
            String[] stCodes = ("83,1,2,3,4,5,6,8,9,10,11,12,13,14,15," +
                    "16,17,18,31,20,33,21,19,22,55,24,39,71," +
                    "35,23,25,26,27,28,29,30,38,40,41,42,43,45,46,47," +
                    "49,50,51,52,53,54,56,58,59,61,63,64,84").split(",");
            for (String stCode : stCodes) {
                AttStBase stTemp = wagaStMap.get(stCode);
                if (stTemp != null) {
                    JSONObject temp = new JSONObject();
                    temp.put("name", stTemp.getStName());
                    try {
                        BigDecimal flow = new BigDecimal(stTemp.getFlow());
                        temp.put("maxFlow", flow.doubleValue());
                    } catch (Exception e) {
                        temp.put("maxFlow", 0);
                    }
                    result.add(temp);
                }
            }
        }
        return result.stream().sorted((o1, o2) -> o2.getDouble("maxFlow").compareTo(o1.getDouble("maxFlow"))).collect(Collectors.toList());
    }

    @Override
    public List<JSONObject> drainageAbilityTotal() {
        CommonResult<List<AttStBase>> response = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.GATE.type())
                .build());
        BigDecimal total = new BigDecimal(0);
        if (CollectionUtils.isNotBlank(response.getData())) {
            List<AttStBase> data = response.getData();
            Map<String, AttStBase> wagaStMap = data.stream().collect(Collectors.toMap(AttStBase::getStCode, o -> o));
            String[] stCodes = ("83,1,2,3,4,5,6,8,9,10,11,12,13,14,15," +
                    "16,17,18,31,20,33,21,19,22,55,24,39,71," +
                    "35,23,25,26,27,28,29,30,38,40,41,42,43,45,46,47," +
                    "49,50,51,52,53,54,56,58,59,61,63,64,84").split(",");
            for (String stCode : stCodes) {
                AttStBase stTemp = wagaStMap.get(stCode);
                if (stTemp != null) {
                    try {
                        BigDecimal flow = new BigDecimal(stTemp.getFlow());
                        total = total.add(flow);
                    } catch (Exception e) {
                    }
                }
            }
        }

        return Arrays.asList(Builder.of(JSONObject::new).with(JSONObject::put, "total", total.doubleValue()).build());
    }

    @Override
    public List<JSONObject> riverWarnStats() {
        JSONObject result = new JSONObject();
        result.put("name", "水位统计");
        AtomicInteger warnNum = new AtomicInteger();
        List<AttStBase> totalList = new ArrayList<>(3000);
        CommonResult<List<AttStBase>> riverResp = graphClient.waterStations(Builder.of(SynWaterRequestVo::new)
                .with(SynWaterRequestVo::setStationType, StationType.RIVER.type())
                .build());
        if (riverResp.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(riverResp.getData())) {
            totalList.addAll(riverResp.getData());
        }
        totalList.forEach(o -> {
            if (o.getWarningLevel() != null && o.getWaterLevel() >= o.getWaterLevel()) {
                warnNum.getAndIncrement();
            }
        });
        result.put("totalNum", totalList.size());
        result.put("warnNum", warnNum.get());
        return Arrays.asList(result);
    }

    @Override
    public List<JSONObject> floodPointWarnStats() {
        JSONObject result = new JSONObject();
        result.put("name", "积水统计");
        int total = 0;
        int warn = 0;
        CommonResult<PageVO<LinkedHashMap>> response = floodClient.newestFloodPoint(1, 10000);
        if (response.getCode() == CommonResult.success().getCode()
                && CollectionUtils.isNotBlank(response.getData().getList())) {
            List<LinkedHashMap> respTemp = response.getData().getList();
            List<JSONObject> dataList = JSONArray.parseArray(JSONObject.toJSONString(respTemp)).toJavaList(JSONObject.class);
            total = dataList.size();
            for (JSONObject d : dataList) {
                if (d.getDoubleValue("waterLevel") > 0) {
                    warn++;
                }
            }
        }
        result.put("totalNum", total);
        result.put("warnNum", warn);
        return Arrays.asList(result);
    }

    static Map<String, Double> rsvrDayWaterSupply = new HashMap<>();

    static {
        /**
         * 各个水库的日供水能力，单位万立方米
         */
        rsvrDayWaterSupply.put("珊溪水库", 190.00);
        rsvrDayWaterSupply.put("吴家园水库", 12.53);
        rsvrDayWaterSupply.put("桥墩水库", 12.00);
        rsvrDayWaterSupply.put("钟前水库", 2.50);
        rsvrDayWaterSupply.put("淡溪水库", 5.00);
        rsvrDayWaterSupply.put("顺溪水库", 7.00);
        rsvrDayWaterSupply.put("泽雅水库", 9.00);
    }

    /**
     * 抗旱统计
     *
     * @return
     */
    @Override
    public List<JSONObject> antiDroughtStats() {
        /**
         * 水库可用水量=（实时水位对应库容-死库容）/日供水量
         */
        List<JSONObject> result = new ArrayList<>();
        List<AttResBase> rsvrList = this.projectMapper.selectLargeMidRsvrByCode();
        rsvrList = rsvrList == null ? Collections.emptyList() : rsvrList;

        String projCode = rsvrList.stream()
                .filter(o -> o != null && StringUtils.isNotBlank(o.getResCode()))
                .map(AttResBase::getResCode)
                .collect(Collectors.joining(","));
        List<AttStBase> stList = this.stMapper.selectByCondition(Builder.of(HashMap<String, Object>::new)
                .with(HashMap::put, "projCodes", projCode)
                .build());
        stList = stList == null ? Collections.emptyList() : stList;
        Map<String, List<String>> projStMap = stList.stream()
                .collect(Collectors.groupingBy(AttStBase::getProjCode,
                        Collectors.mapping(AttStBase::getStCode,
                                Collectors.toList())));
        List<String> stcds = stList.stream()
                .filter(o -> o != null && StringUtils.isNotBlank(o.getStCode()))
                .map(AttStBase::getStCode)
                .collect(Collectors.toList());
        LocalDateTime nowLdt = LocalDateTime.now();
        List<StRsvrR> nowCapcity = waterAndRainMapper.selectNewestRsvrWaterLevel(Arrays.asList(StationType.RESERVOIR.table() + nowLdt.format(yyyyMM),
                StationType.RESERVOIR.table() + nowLdt.minusHours(1).format(yyyyMM))
                .stream().distinct().collect(Collectors.toList()), stcds);
        Map<String, Double> nowLevelMap = nowCapcity.stream().collect(Collectors.toMap(StRsvrR::getStcd, StRsvrR::getRz));
        Map<String, BigDecimal> nowCapacityMap = caculateCapactiy(nowLevelMap);
        for (AttResBase rsvr : rsvrList) {
            JSONObject temp = new JSONObject();
            temp.put("name", rsvr.getResName());
            List<String> stCodes = projStMap.getOrDefault(rsvr.getResCode(), Collections.emptyList());
            if (CollectionUtils.isNotBlank(stCodes)
                    && rsvr.getDeadCap() != null
                    && rsvrDayWaterSupply.containsKey(rsvr.getResCode())) {
                BigDecimal value = nowCapacityMap.getOrDefault(stCodes.get(0), null);
                if (value != null) {
                    temp.put("value", (value.doubleValue() - rsvr.getDeadCap()) / rsvrDayWaterSupply.getOrDefault(rsvr.getResCode(), 1D));
                }
            }
            temp.putIfAbsent("value", BigDecimal.ZERO);
            result.add(temp);
        }
        return result.stream().sorted(Comparator.comparing(o -> o.getDouble("value"))).collect(Collectors.toList());

    }
}
