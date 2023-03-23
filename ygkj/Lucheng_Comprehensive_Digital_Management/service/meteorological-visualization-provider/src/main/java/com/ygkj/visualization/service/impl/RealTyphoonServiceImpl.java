package com.ygkj.visualization.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.SingletonMap;
import com.ygkj.utils.*;
import com.ygkj.visualization.mapper.AdcdBaseDataMapper;
import com.ygkj.visualization.mapper.RealTyphoonDataMapper;
import com.ygkj.visualization.model.RealTyphoonData;
import com.ygkj.visualization.service.RealTyphoonService;
import com.ygkj.visualization.vo.request.RealTyphoonDataRequestVo;
import com.ygkj.visualization.vo.response.RealTyphoonDataResVo;
import com.ygkj.visualization.vo.response.TyphoonStatisticResVo;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.geom.Point2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("realTyphoonService")
public class RealTyphoonServiceImpl implements RealTyphoonService {

    final static double maxDis = 80;
    final static double minDis = 0;
//    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    final static List<Date> filteredTimePoint =new ArrayList<Date>();
//    static{
//        try {
//            filteredTimePoint.add(sdf.parse("2019-08-06 02:00"));
//            filteredTimePoint.add(sdf.parse("2019-08-06 05:00"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    private RealTyphoonDataMapper realTyphoonDataMapper;

    @Resource
    AdcdBaseDataMapper adcdBaseDataMapper;

    public RealTyphoonServiceImpl(@Autowired RealTyphoonDataMapper realTyphoonDataMapper) {
        this.realTyphoonDataMapper = realTyphoonDataMapper;
    }

    @Override
    public List<RealTyphoonDataResVo> selectByCondition(RealTyphoonDataRequestVo realTyphoonDataRequestVo) {
        // 判断是否影响温州
        realTyphoonDataRequestVo.judgeAffectedWz();
        List<RealTyphoonDataResVo> list = realTyphoonDataMapper.selectByCondition(realTyphoonDataRequestVo);

        // 将当前台风放到列表最前面
//        Map<String,List<RealTyphoonDataResVo>> groupMap=tempList.stream().collect(Collectors.groupingBy(o->o.getEndTime()==null?"c":"h"));
//        List<RealTyphoonDataResVo> list=new ArrayList<>(tempList.size());
//        list.addAll(groupMap.getOrDefault("c",Collections.emptyList()));
//        list.addAll(groupMap.getOrDefault("h",Collections.emptyList()));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        for (RealTyphoonDataResVo voList : list) {
            Date now = null;
            try {
                now = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Integer hours = DateUtils.hourBetween(voList.getBeginTime(), voList.getEndTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            voList.setFutureBegin(calendar.getTime());
            calendar.add(Calendar.HOUR_OF_DAY, hours);
            voList.setFutureEnd(calendar.getTime());
            voList.setYearsAndMonth(format.format(voList.getBeginTime()));
            if (StringUtils.isNotEmpty(voList.getAffectedArea())) {
                String[] strArray = voList.getAffectedArea().split("，");
                voList.setAffectedArea(strArray[0]);
            }
            voList.setHours(DateUtils.hourBetween(voList.getBeginTime(), voList.getEndTime()));
        }
        return list;
    }

    /**
     * 大屏台风组件查询接口接口
     *
     * @param realTyphoonDataRequestVo
     * @return
     */
    @Override
    public List<RealTyphoonDataResVo> historyAndCurrent(RealTyphoonDataRequestVo realTyphoonDataRequestVo) {
        List<RealTyphoonDataResVo> historyList = selectByCondition(realTyphoonDataRequestVo);
        historyList = historyList == null ? Collections.emptyList() : historyList;
        Map<Integer, RealTyphoonDataResVo> allMap = historyList.stream().collect(Collectors.toMap(RealTyphoonDataResVo::getId, Function.identity()));
        List<Integer> allIdList = historyList.stream().map(RealTyphoonDataResVo::getId).collect(Collectors.toList());

        JSONArray array = fuckingCurrentTyphoon();
        if (CollectionUtils.isNotBlank(array)) {
            List<String> codes = array.stream().map(o -> ((JSONObject) o).getString("tfbh")).collect(Collectors.toList());
            if (CollectionUtils.isNotBlank(codes)) {
                List<RealTyphoonDataResVo> currentList = this.realTyphoonDataMapper.selectByCondition(Builder.of(RealTyphoonDataRequestVo::new)
                        .with(RealTyphoonDataRequestVo::setCodes, String.join(",", codes))
                        .with(RealTyphoonDataRequestVo::setAffectedArea, null)
                        .build());
                currentList = currentList == null ? Collections.emptyList() : currentList.stream().peek(o -> o.setCurrent(true)).collect(Collectors.toList());
                allMap.putAll(currentList.stream().collect(Collectors.toMap(RealTyphoonDataResVo::getId, Function.identity())));
                List<Integer> currentIdList = currentList.stream().map(RealTyphoonDataResVo::getId).collect(Collectors.toList());
                allIdList.removeAll(currentIdList);
                allIdList.addAll(0, currentIdList);
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        List<RealTyphoonDataResVo> resultList = new ArrayList<>(allIdList.size());
        for (Integer id : allIdList) {
            RealTyphoonDataResVo tempResult = allMap.get(id);
            if (tempResult != null) {
                Date now = null;
                try {
                    now = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Integer hours = DateUtils.hourBetween(tempResult.getBeginTime(), tempResult.getEndTime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                tempResult.setFutureBegin(calendar.getTime());
                calendar.add(Calendar.HOUR_OF_DAY, hours);
                tempResult.setFutureEnd(calendar.getTime());
                tempResult.setYearsAndMonth(format.format(tempResult.getBeginTime()));
                if (StringUtils.isNotEmpty(tempResult.getAffectedArea())) {
                    String[] strArray = tempResult.getAffectedArea().split("，");
                    tempResult.setAffectedArea(strArray[0]);
                }
                tempResult.setHours(DateUtils.hourBetween(tempResult.getBeginTime(), tempResult.getEndTime()));
                resultList.add(tempResult);
            }
        }
        return resultList;
    }

    @Override
    public RealTyphoonData newestTyphoon() {
        // 优先当前台风，没有当前台风就最新的历史台风。
        return realTyphoonDataMapper.selectNewestTyphoon();
    }

    @Override
    public RealTyphoonData selectDetail(String code) {
        RealTyphoonData realTyphoonData = realTyphoonDataMapper.selectDetail(code);
        if (realTyphoonData == null) {
            return null;
        }
        RealTyphoonDataResVo typhoonInfo = new RealTyphoonDataResVo();
        BeanUtils.copyProperties(realTyphoonData, typhoonInfo);
        String points = realTyphoonData.getPoints();
        JSONArray array = JSONArray.parseArray(points);
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            object.put("distance", distanceFarFromWenZhou(object.getString("lng"), object.getString("lat")));
            // 7.13 将台风信息，加入点位，方便前端交互
            object.put("typhoonInfo", JSONObject.toJSON(typhoonInfo));
        }
        realTyphoonData.setPoints(array.toJSONString());
        return realTyphoonData;
    }

    @Override
    public RealTyphoonData selectDetailById(Integer id) {
        RealTyphoonData realTyphoonData = realTyphoonDataMapper.load(id);
        if (realTyphoonData == null) {
            return null;
        }
        RealTyphoonDataResVo typhoonInfo = new RealTyphoonDataResVo();
        BeanUtils.copyProperties(realTyphoonData, typhoonInfo);
        String points = realTyphoonData.getPoints();
        JSONArray array = JSONArray.parseArray(points);
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            object.put("distance", distanceFarFromWenZhou(object.getString("lng"), object.getString("lat")));
            // 7.13 将台风信息，加入点位，方便前端交互
            object.put("typhoonInfo", JSONObject.toJSON(typhoonInfo));
        }
        realTyphoonData.setPoints(array.toJSONString());
        return realTyphoonData;
    }

    @Override
    public JSONObject analysis(String code, String type) {
        JSONObject result = new JSONObject();
        try {
            String url = "http://ecapi.wztf121.com/ecapi/api/typhoon/analysis?" +
                    "v=" + System.currentTimeMillis() + "&type=similar" +
                    "&top=30&tfbh=" + code +
                    "&analyse_type=" + type;
            String jsonString = HttpClientUtil.httpGet(url);
            result = JSONObject.parseObject(jsonString);
            JSONArray jsonArray = result.getJSONArray("data");
            String codes = jsonArray.stream().map(o -> ((JSONObject) o).getString("tfbh")).collect(Collectors.joining(","));
            List<Map<String, String>> list = realTyphoonDataMapper.selectCandidateTyphoonAffectedArea(codes);
            Map<String, String> collect = list.stream().collect(Collectors.toMap(map -> map.get("code"), map -> map.get("affected_area") == null ? "" : map.get("affected_area")));
            Map<String, String> landMap = list.stream().collect(Collectors.toMap(map -> map.get("code"), map -> StringUtils.isNotBlank(map.get("land")) ? "" : map.get("land")));
            Map<String, Object> primaryMap = list.stream().collect(Collectors.toMap(map -> map.get("code"), map -> map.get("id") == null ? "" : map.get("id")));
            Map<String, List<Map<String, String>>> timeMap = list.stream().collect(Collectors.groupingBy(map -> map.get("code")));
            for (Object o : jsonArray) {
                JSONObject object = (JSONObject) o;
                Object typhoonCode = object.getOrDefault("tfbh", "");
                object.put("affectedArea", collect.get(typhoonCode));
                object.put("id", primaryMap.get(typhoonCode));
                if (landMap.containsKey(typhoonCode)) {
                    //2021.05.27 组装登录数据
                    JSONArray array = JSONArray.parseArray(landMap.get(typhoonCode));
                    if (array != null && !array.isEmpty()) {
                        object.putAll(array.getJSONObject(0));
                    } else {
                        object.put("land_time", "-");
                        object.put("position", "-");
                    }
                    Map<String, String> map = timeMap.get(typhoonCode).get(0);
                    object.put("hours", DateUtils.hourBetween(map.get("begin_time"), map.get("end_time")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONObject();
        }

        return result;
    }

    @Override
    public double distanceFarFromWenZhou(String longitude, String latitude) {

        double lng = 120.707312d, lat = 28.001819d;
        return DistanceUtil.getDistance(lat, lng, Double.parseDouble(latitude), Double.parseDouble(longitude)).doubleValue();
    }

    final static Map<String, Map<String, Double>> wzAdLgLttdMap = new HashMap<>(17);

    static {
        Map<String, Double> map = new HashMap();
        map.put("longitude", 120.66704712295733D);
        map.put("latitude", 28.33284957790325D);
        wzAdLgLttdMap.put("永嘉县", map);
        map = new HashMap();
        map.put("longitude", 120.9952636756917);
        map.put("latitude", 28.196893767356375);
        wzAdLgLttdMap.put("乐清市", map);
        map = new HashMap();
        map.put("longitude", 120.57645463189935);
        map.put("latitude", 28.03827120238947);
        wzAdLgLttdMap.put("鹿城区", map);
        map = new HashMap();
        map.put("longitude", 120.929712074297);
        map.put("latitude", 27.970906374737538);
        wzAdLgLttdMap.put("洞头区", map);
        map = new HashMap();
        map.put("longitude", 120.5622791965776);
        map.put("latitude", 27.96116875430328);
        wzAdLgLttdMap.put("瓯海区", map);
        map = new HashMap();
        map.put("longitude", 120.80896558091213);
        map.put("latitude", 27.903392206393352);
        wzAdLgLttdMap.put("龙湾区", map);
        map.put("longitude", 119.88437565755092);
        map.put("latitude", 27.546872024506133);
        wzAdLgLttdMap.put("泰顺县", map);
        map = new HashMap();
        map.put("longitude", 120.45118650034945);
        map.put("latitude", 27.441361781041465);
        wzAdLgLttdMap.put("苍南县", map);

        map = new HashMap();
        map.put("longitude", 120.56896537677517);
        map.put("latitude", 27.548507842234244);
        wzAdLgLttdMap.put("龙港市", map);

        map = new HashMap();
        map.put("longitude", 120.39728315045245);
        map.put("latitude", 27.632134167756785);
        wzAdLgLttdMap.put("平阳县", map);
        map = new HashMap();
        map.put("longitude", 120.02308939387257);
        map.put("latitude", 27.80382238959982);
        wzAdLgLttdMap.put("文成县", map);
        map = new HashMap();
        map.put("longitude", 120.46755625299411);
        map.put("latitude", 27.828059660713564);
        wzAdLgLttdMap.put("瑞安市", map);
    }

    @Override
    public List<Map<String, Object>> getAdDistanceFromTyphoon(Double longitude, Double latitude) {
        List<Map<String, Object>> adcdList = adcdBaseDataMapper.selectWzChildAd();
        for (Map<String, Object> map : adcdList) {
            String adnm = (String) map.get("adnm");
            Map<String, Double> lgltMap = wzAdLgLttdMap.get(adnm);
            if (lgltMap != null) {
                map.putAll(lgltMap);
                Double adLgtd = lgltMap.get("longitude");
                Double adLttd = lgltMap.get("latitude");
                map.put("distance", DistanceUtil.getDistance(adLttd, adLgtd, latitude, longitude));
            } else {
                map.put("longitude", null);
                map.put("latitude", null);
                map.put("distance", null);
            }
        }
        return adcdList;
    }


    @Override
    public Object typhoon70PerArea(String code) {
        RealTyphoonData realTyphoonData = realTyphoonDataMapper.selectDetail(code);
        if (realTyphoonData == null) {
            return null;
        }
        String pointStr = realTyphoonData.getPoints();
        if (StringUtils.isBlank(pointStr)) {
            return null;
        }
        JSONArray jsonArray = JSONObject.parseArray(pointStr);
        int size = jsonArray.size();

        double average = (maxDis - minDis) / size;
        int i = 0;
        List<Map<String, Object>> result = new ArrayList<>(size);
        jsonArray.stream().sorted((o1, o2) -> {
            JSONObject point1 = (JSONObject) o1;
            JSONObject point2 = (JSONObject) o2;
            return point1.getDate("time").compareTo(point2.getDate("time"));
        });
        for (Object o : jsonArray) {
            JSONObject point = (JSONObject) o;
            Date time = point.getDate("time");
//            if (filteredTimePoint.contains(time)){
//                continue;
//            }
            Double lng = point.getDouble("lng");
            Double lat = point.getDouble("lat");
            Map<String, Object> tempResult = new HashMap<>();
            if (i >= 10) {
                double[] west = DistanceUtil.calLocationByDistanceAndLocationAndDirection(270D, lng, lat, minDis + ((i - 10) * average));
                double[] east = DistanceUtil.calLocationByDistanceAndLocationAndDirection(90D, lng, lat, minDis + ((i - 10) * average));
                tempResult.put("westLng", west[0]);
                tempResult.put("westLat", west[1]);
                tempResult.put("eastLng", east[0]);
                tempResult.put("eastLat", east[1]);
            } else {
                tempResult.put("westLng", lng);
                tempResult.put("westLat", lat);
                tempResult.put("eastLng", lng);
                tempResult.put("eastLat", lat);
            }
            tempResult.put("time", time);
            result.add(tempResult);
            i++;
        }
        return result;
    }

    @Override
    public JSONArray fuckingCurrentTyphoon() {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String url = "http://data.istrongcloud.com/v2/data/complex/" + calendar.get(Calendar.YEAR) + ".json?v=" + currentTimeMillis;
        String typhoons = HttpClientUtil.httpGet(url);
        if(StringUtils.isEmpty(typhoons)){
            return new JSONArray();
        }
        List<Object> list = JSONArray.parseArray(typhoons).stream()
                .filter(obj -> ((JSONObject) obj).getInteger("is_current").intValue() == 1)
                .collect(Collectors.toList());
        JSONArray result = new JSONArray();
        for (Object o : list) {
            JSONObject typhoon = (JSONObject) o;
            String code = typhoon.getString("tfbh");
            RealTyphoonData data = realTyphoonDataMapper.selectDetail(code);
            if(data == null){
                continue;
            }
//            typhoon.put("id",data.getId().intValue());
            String detailUrl = "http://data.istrongcloud.com/v2/data/complex/" + code + ".json?v=" + currentTimeMillis;
            String detail = HttpClientUtil.httpGet(detailUrl);
            JSONObject jsonObject = JSONArray.parseArray(detail).getJSONObject(0);
            //距离温州的距离
            JSONArray points = jsonObject.getJSONArray("points");
            for (Object point : points) {
                JSONObject pointObj = (JSONObject) point;
                pointObj.put("distance", distanceFarFromWenZhou(pointObj.getString("lng"), pointObj.getString("lat")));
                JSONArray forecast = pointObj.getJSONArray("forecast");
                if (null != forecast && !forecast.isEmpty()) {
                    for (Object object : forecast) {
                        JSONArray array = ((JSONObject) object).getJSONArray("points");
                        for (Object pointStr : array) {
                            JSONObject pointJson = (JSONObject) pointStr;
                            pointJson.put("distance", distanceFarFromWenZhou(pointJson.getString("lng"), pointJson.getString("lat")));
                        }
                    }
                }
            }
            jsonObject.put("id", data.getId().intValue());
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public TyphoonStatisticResVo typhoonStatistic() {
        TyphoonStatisticResVo result = new TyphoonStatisticResVo();
        List<RealTyphoonData> typhoonData = realTyphoonDataMapper.selectAllTyphoonWithoutPoint();
        result.setTyphoonNum(typhoonData.size());
        RealTyphoonData maxTyphoon = typhoonData.stream().max((o1, o2) -> {
            Integer i1 = 0;
            Integer i2 = 0;
            if (StringUtils.isNotBlank(o1.getLandPower())) {
                i1 = Integer.valueOf(o1.getLandPower());
            }
            if (StringUtils.isNotBlank(o2.getLandPower())) {
                i2 = Integer.valueOf(o2.getLandPower());
            }
            return i1.compareTo(i2);
        }).get();
        result.setMaxTyphoonInfo(maxTyphoon);
        Map<String, List<RealTyphoonData>> typhoonNumGroupByYearMap = typhoonData.stream().collect(Collectors.groupingBy(RealTyphoonData::getYear));
        List<Map<String, Object>> typhoonNumGroupByYearTemp = new ArrayList<>(typhoonNumGroupByYearMap.size());
        typhoonNumGroupByYearMap.forEach((s, realTyphoonData) -> {
            Map<String, Object> temp = new HashMap<>();
            temp.put("year", s);
            temp.put("num", realTyphoonData.size());
            typhoonNumGroupByYearTemp.add(temp);
        });
        List<Map<String, Object>> typhoonNumGroupByYear = typhoonNumGroupByYearTemp.stream().sorted(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return Integer.valueOf(o1.get("year").toString()).compareTo(Integer.valueOf(o2.get("year").toString()));
            }
        }).collect(Collectors.toList());
        result.setTyphoonNumGroupByYear(typhoonNumGroupByYear);
        // lowerEight,lowerTen,lowerTwelve,lowerFourteen,biggerFourteen
        Map<String, Integer> typhoonNumGroupByPowerMap = new HashMap<>();
        typhoonNumGroupByPowerMap.put(TyphoonStatisticResVo.lowerEight, 0);
        typhoonNumGroupByPowerMap.put(TyphoonStatisticResVo.lowerTen, 0);
        typhoonNumGroupByPowerMap.put(TyphoonStatisticResVo.lowerTwelve, 0);
        typhoonNumGroupByPowerMap.put(TyphoonStatisticResVo.lowerFourteen, 0);
        typhoonNumGroupByPowerMap.put(TyphoonStatisticResVo.biggerFourteen, 0);
        for (RealTyphoonData typhoon : typhoonData) {
            int landPower = 0;
            if (StringUtils.isNotBlank(typhoon.getLandPower())) {
                landPower = Integer.valueOf(typhoon.getLandPower());
            }
            if (landPower <= 8) {
                int typhoonNum = typhoonNumGroupByPowerMap.getOrDefault(TyphoonStatisticResVo.lowerEight, 0);
                typhoonNum++;
                typhoonNumGroupByPowerMap.put(TyphoonStatisticResVo.lowerEight, typhoonNum);
            } else if (landPower <= 10) {
                int typhoonNum = typhoonNumGroupByPowerMap.getOrDefault(TyphoonStatisticResVo.lowerTen, 0);
                typhoonNum++;
                typhoonNumGroupByPowerMap.put(TyphoonStatisticResVo.lowerTen, typhoonNum);
            } else if (landPower <= 12) {
                int typhoonNum = typhoonNumGroupByPowerMap.getOrDefault(TyphoonStatisticResVo.lowerTwelve, 0);
                typhoonNum++;
                typhoonNumGroupByPowerMap.put(TyphoonStatisticResVo.lowerTwelve, typhoonNum);
            } else if (landPower <= 14) {
                int typhoonNum = typhoonNumGroupByPowerMap.getOrDefault(TyphoonStatisticResVo.lowerFourteen, 0);
                typhoonNum++;
                typhoonNumGroupByPowerMap.put(TyphoonStatisticResVo.lowerFourteen, typhoonNum);
            } else if (landPower > 14) {
                int typhoonNum = typhoonNumGroupByPowerMap.getOrDefault(TyphoonStatisticResVo.biggerFourteen, 0);
                typhoonNum++;
                typhoonNumGroupByPowerMap.put(TyphoonStatisticResVo.biggerFourteen, typhoonNum);
            }
        }
        List<Map<String, Object>> typhoonNumGroupByPower = new ArrayList<>(typhoonNumGroupByPowerMap.size());
        typhoonNumGroupByPowerMap.forEach((s, num) -> {
            Map<String, Object> temp = new HashMap<>();
            temp.put("level", s);
            temp.put("num", num);
            typhoonNumGroupByPower.add(temp);
        });
        result.setTyphoonNumGroupByPower(typhoonNumGroupByPower);
        return result;
    }

    @Override
    public Object typhoonStatisticRadarMap(Integer year) {

        List<Map<String, Object>> result = realTyphoonDataMapper.selectTyphoonDataByYearGroupByMonth(year);
        if (result == null) {
            result = Collections.emptyList();
        }
        result.forEach(map -> map.put("monthVal", Integer.valueOf((String) map.get("monthVal"))));
        if (result.size() == 12) {
            return result;
        }
        List<Integer> monthList = result.stream().map(o -> (Integer) o.get("monthVal")).collect(Collectors.toList());
        List<Integer> fullMonthList = new ArrayList<>(12);
        for (int i = 1; i <= 12; i++) {
            fullMonthList.add(i);
        }
        List<Integer> noneMonthList = CollectionUtils.getExcept(fullMonthList, monthList);
        if (CollectionUtils.isNotBlank(monthList)) {
            for (Integer s : noneMonthList) {
                Map<String, Object> temp = new HashMap<>();
                temp.put("monthVal", s);
                temp.put("num", 0);
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * 台风静态风险-统计各个行政区划台风登录次数
     *
     * @return
     */
    @Override
    public List<SingletonMap<String, Integer>> landTimesEachAdcd() {
        List<SingletonMap<String, String>> landInfoList = this.realTyphoonDataMapper.selectAllLandInfo();
        String[] adnmKeyArray={"苍南","龙港","平阳","瑞安","龙湾","乐清","洞头"};
        String[] adnmArray={"苍南县","龙港市","平阳县","瑞安市","龙湾区","乐清市","洞头区"};
        List<SingletonMap<String, List<JSONObject>>> landList = landInfoList.stream().map(o -> {
            try {
                return new SingletonMap<>(o.getKey(), JSONArray.parseArray(o.getValue(), JSONObject.class));
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).filter(o->CollectionUtils.isNotBlank(o.getValue())).collect(Collectors.toList());
        Map<String,Set<String>> landOfAdcdMap=new HashMap<>();
        for (SingletonMap<String, List<JSONObject>> tempLandInfo : landList) {
            List<JSONObject> tempLandList= tempLandInfo.getValue();
            for (int i = 0; i < adnmKeyArray.length; i++) {
                String adnmKey=adnmKeyArray[i];
                String adnm=adnmArray[i];
                if (checkLand(tempLandList,adnmKey)){
                    Set<String> sets= landOfAdcdMap.get(adnm);
                    if(sets==null){
                        sets=new HashSet<>();
                    }
                    sets.add(tempLandInfo.getKey());
                    landOfAdcdMap.put(adnm,sets);
                    break;
                }
            }
        }
        List<SingletonMap<String, Integer>> result=new ArrayList<>(adnmArray.length);
        for (String adnm : adnmArray) {
            Set<String> sets=landOfAdcdMap.getOrDefault(adnm,Collections.emptySet());
            result.add(Builder.of(SingletonMap<String, Integer>::new)
                    .with(SingletonMap::setKey,adnm)
                    .with(SingletonMap::setValue,sets.size())
                    .with(SingletonMap::setRemark,String.join(",",sets)).build());
        }
        return result;
    }

    boolean checkLand(List<JSONObject> landInfoList,String adnmKey){
        if (CollectionUtils.isBlank(landInfoList)||StringUtils.isBlank(adnmKey)){
            return false;
        }
        for (JSONObject jsonObject : landInfoList) {
            String position=jsonObject.getString("position");
            if (StringUtils.isNotBlank(position)&&position.contains(adnmKey)){
                return true;
            }
        }
        return false;
    }
}
