package com.ygkj.visualization.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.enums.StationQueryCollectionType;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.*;
import com.ygkj.visualization.enums.AdcdMeteorologicalStationCode;
import com.ygkj.visualization.enums.WeatherQueryApi;
import com.ygkj.visualization.enums.WeatherStatisticType;
import com.ygkj.visualization.enums.WzApiEnum;
import com.ygkj.visualization.mapper.AdcdBaseDataMapper;
import com.ygkj.visualization.mapper.CollectPointMapper;
import com.ygkj.visualization.mapper.FloodPredictMapper;
import com.ygkj.visualization.mapper.WaterAndRainMapper;
import com.ygkj.visualization.model.AttRainGrid;
import com.ygkj.visualization.model.CollectPoint;
import com.ygkj.visualization.service.WeatherService;
import com.ygkj.visualization.vo.request.CollectPointQueryVo;
import com.ygkj.visualization.vo.response.RainfallPredictGridResVo;
import com.ygkj.water.model.CommonCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service("weatherService")
@Slf4j
public class WeatherServiceImpl implements WeatherService, InitializingBean {

    private static final String CITY_CODE = "58659";

    private static final String STATE = "city";

    private static final String PREFIX = "collect_point_rainfall_detail_";

    private static final String WEATHER_PREDICT_KEY = "wenzhou_whole_weather";

    private static final Map<String, String> map;

    private CollectPointMapper collectPointMapper;

    private WaterAndRainMapper waterAndRainMapper;

    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redis;

    private static Map<String, float[][]> rainFallMap = new ConcurrentHashMap<>();

    // 1，3，6 起报时间
    private volatile static LocalDateTime rainfallPredictStartTime;
    // 2022年05月09日13:00时
    private static DateTimeFormatter rainfallPredictStartTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日HH:mm:00");


    @Autowired
    private AdcdBaseDataMapper adcdBaseDataMapper;

    @Value("${weather.syn-rain-path}")
    private String filePath;

    private static DelayQueue delayQueue;

    private static DelayQueue weatherTaskDelayQueue;

    private static String path;

    private ReentrantLock lock = new ReentrantLock();

    private volatile boolean updating = false;

    private volatile JSONArray weatherData;

    private CountDownLatch latch;

    private static ThreadPoolExecutor weatherExecutor;

    private static List<String> adcdSequenceList = new ArrayList<>();

    private volatile List<JSONObject> forestWeatherData;

    static {
        map = new HashMap<>();
        Arrays.stream(AdcdMeteorologicalStationCode.values()).forEach(code -> map.put(code.type(), code.getStationCode()));
        weatherExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 256, 60, TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(128));
        adcdSequenceList.add("330302000000");
//        adcdSequenceList.add("330303000000");
//        adcdSequenceList.add("330304000000");
//        adcdSequenceList.add("330322000000");
//        adcdSequenceList.add("330382000000");
//        adcdSequenceList.add("330381000000");
//        adcdSequenceList.add("330324000000");
//        adcdSequenceList.add("330328000000");
//        adcdSequenceList.add("330326000000");
//        adcdSequenceList.add("330329000000");
//        adcdSequenceList.add("330327000000");
//        adcdSequenceList.add("330383000000");
    }

    public WeatherServiceImpl(@Autowired CollectPointMapper collectPointMapper,
                              @Autowired StringRedisTemplate redisTemplate,
                              @Autowired WaterAndRainMapper waterAndRainMapper) {
        this.collectPointMapper = collectPointMapper;
        this.redisTemplate = redisTemplate;
        this.waterAndRainMapper = waterAndRainMapper;
    }

    @Override
    public JSONObject currentWeather(String areaName) throws Exception {
        JSONObject result;
        String code = map.get(areaName);
        JSONArray array = getTwentyFourHoursWeather(code);
        JSONArray threeDayWeather = getThreeDayWeather(code);
        JSONObject currentWeather = array.getJSONObject(0);
        result = concatWeatherData(currentWeather);
        //当前温度
        JSONObject jsonObject = threeDayWeather.getJSONObject(0);
        result.put("currentTemperature", jsonObject.getDoubleValue("temp"));
        //当前风速
        result.put("currentWindSpeed", jsonObject.getDoubleValue("windSpeed"));
        //风力
        result.put("windPower", jsonObject.getString("windPower"));
        //日出
        result.put("sunrise", jsonObject.getString("sunrise"));
        //日落
        result.put("sunset", jsonObject.getString("sunset"));
        //气压
        result.put("airPressure", jsonObject.getString("airPressure"));
        //降雨
        result.put("rain", jsonObject.getString("rain"));
        //能见度
        result.put("visibility", jsonObject.getString("visibility"));
        //湿度
        result.put("humidity", jsonObject.getString("humidity"));
        result.put("winds", jsonObject.getString("winds"));
        //6小时降雨
        result.put("sixHourRainfall", array.getJSONObject(5).getDoubleValue("rainfall"));
        //24小时降雨
        result.put("twentyFourHourRainfall", array.getJSONObject(array.size() - 1).getDoubleValue("rainfall"));
        int i = 0;
        List<JSONObject> futureHoursWeather = new LinkedList<>();
        while (i < 24) {
            JSONObject data = array.getJSONObject(i++);
            futureHoursWeather.add(concatWeatherData(data));
        }
        result.put("futureHoursWeather", futureHoursWeather);
        return result;
    }

    @Override
    public JSONArray weatherPredict(String areaName) {
//        String code = map.getOrDefault(areaName, "58659");
        if (weatherData != null) {
            return weatherData;
        }
        String code = CITY_CODE;
        String url = WeatherQueryApi.SEVEN_DAY_WEATHER_PREDICT.getUrl();
        Object weatherData = null;
        try {
//            weatherData = requestWeatherData(StringUtils.isBlank(code) ? url : url.replace(CITY_CODE, code), new HashMap<>());
            weatherData = requestWeatherData(url, new HashMap<>());
            return this.weatherData = (JSONArray) weatherData;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    @Override
    public synchronized JSONArray wholeWeather() {
//        if (!redisTemplate.hasKey(WEATHER_PREDICT_KEY)){
            updateWeatherPredict();
//        }
        return JSONArray.parseArray(redisTemplate.opsForValue().get(WEATHER_PREDICT_KEY));
    }

    @Override
    public List<JSONObject> weatherStatistic(String areaName, String type) throws Exception {
        List<JSONObject> list = new LinkedList<>();
        String code = map.get(areaName);
        JSONArray array = getTwentyFourHoursWeather(code);
        for (Object data : array) {
            JSONObject weather = (JSONObject) data;
            JSONObject object = new JSONObject();
            object.put("hour", weather.getString("xTime"));
            double value = 0d;
            if (WeatherStatisticType.TEMPERATURE.type().equals(type)) {
                value = weather.getDoubleValue(WeatherStatisticType.TEMPERATURE.getColumn());
            } else if (WeatherStatisticType.RAINFALL.type().equals(type)) {
                value = weather.getDoubleValue(WeatherStatisticType.RAINFALL.getColumn());
            } else if (WeatherStatisticType.WIND.type().equals(type)) {
                value = weather.getDoubleValue(WeatherStatisticType.WIND.getColumn());
            }
            object.put("value", value);
            list.add(object);
        }
        return list;
    }

    private JSONArray getTwentyFourHoursWeather(String code) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("stationId", StringUtils.isBlank(code) ? CITY_CODE : code);
        params.put("state", STATE);
        Object weatherData = requestWeatherData(WeatherQueryApi.TWENTY_FOUR_WEATHER_PREDICT.getUrl(), params);
        JSONObject object = (JSONObject) weatherData;
        JSONArray array = object.getJSONArray("future24HourList");
        return array;
    }

    private JSONArray getThreeDayWeather(String code) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("stationId", StringUtils.isBlank(code) ? CITY_CODE : code);
        params.put("state", STATE);
        Object weatherData = requestWeatherData(WeatherQueryApi.TWENTY_FOUR_WEATHER_PREDICT.getUrl(), params);
        JSONObject object = (JSONObject) weatherData;
        JSONArray array = object.getJSONArray("future3DayList");
        return array;
    }


    private JSONObject concatWeatherData(JSONObject object) {
        JSONObject result = new JSONObject();
        //当前温度
//        result.put("currentTemperature", object.getDoubleValue("temp"));
        //当前风速
//        result.put("currentWindSpeed", object.getDoubleValue("windSpeed"));
        //当前天气
        result.put("currentWeather", object.getString("wpName"));
        //当前降雨量
        result.put("currentRainfall", object.getString("rainfall"));
        //小时整点
        result.put("hour", object.getString("xTime"));
        //风力
//        result.put("windPower",object.getString("windPower"));
        return result;
    }

    private static Object requestWeatherData(String url, Map<String, String> params) throws Exception {
        Map<String, String> headers = new HashMap<>();
        String result = HttpClientUtil.httpPost(url, params, headers);
        if (result.startsWith("[")) {
            return JSON.parseArray(result);
        } else {
            return JSON.parseObject(result);
        }
    }


    @Override
    public JSONObject rainfallWaterFromSiChuang(String longitude, String latitude) {
        List<CollectPoint> points = collectPointMapper.selectByCondition(new CollectPointQueryVo(longitude, latitude));
        if (!points.isEmpty()) {
            //如果缓存中有就去缓存中取数据
            CollectPoint point = points.get(0);
            String key = PREFIX.concat(point.getId());
            if (redisTemplate.hasKey(key)) {
                return JSON.parseObject(redisTemplate.opsForValue().get(key));
            }
        }
        //2021.02.26 又去四创拿雨量的详细数据，赵克华的意思
        String result = HttpClientUtil.httpGet("https://api.caiyunapp.com/v2/KsOyzsi07D09tw80/" + longitude + "," + latitude + "/forecast?dailysteps=7");
        JSONObject jsonObject = JSON.parseObject(result);
        String address = HttpClientUtil.httpGet("https://caiyunapp.com/fcgi-bin/v1/coord2text.py?lang=zh_CN&latlng=" + latitude + "," + longitude);
        JSONObject addressJson = JSON.parseObject(address);
        JSONObject object = jsonObject.getJSONObject("result");
        JSONObject json = new JSONObject();
        json.put("futureSevenDayRainfall", object
                .getJSONObject("daily").getJSONArray("precipitation"));
        JSONArray rainHourArray = object.getJSONObject("hourly").getJSONArray("precipitation");
        List<JSONObject> list = rainHourArray.toJavaList(JSONObject.class);
        if (list.size() > 6) {
            list = list.subList(0, 6);
        }
        json.put("sixHourTotalRainfall", list.stream().mapToDouble(jb -> jb.getDouble("value")).sum());
        //未来6小时，每个小时累计降雨量
        json.put("hourlyRainfallList",list);
        json.put("address", addressJson.getString("address"));
//        //2021.03.09 根据大数据局的数据，计算雨量，李金宵的主意
//        json.put("countRainfall", caculateRainfall(longitude, latitude));
        return json;
    }

    @Override
    public JSONArray caculateAllRainStationRainfall(Integer hour) {
        JSONArray result = new JSONArray();
        List<AttStBase> stations = waterAndRainMapper.findRainStation("");
        for (AttStBase station : stations) {
            JSONObject jsonObject = caculateRainfall(station.getStLong().toString(), station.getStLat().toString());
            StPptnR stPptnR = new StPptnR();
            stPptnR.setMgstcd(station.getStCode());
            stPptnR.setStcd(station.getStCode());
            String string = jsonObject.getString("rainfall_" + hour.intValue());
            if (string.equals("-")) {
                stPptnR.setDrp(-1d);
            } else {
                stPptnR.setDrp(Double.parseDouble(string));
            }
            stPptnR.setLongitude(station.getStLong().doubleValue());
            stPptnR.setLatitude(station.getStLat().doubleValue());
            result.add(stPptnR);
        }
        return result;
    }


    @Override
    public JSONObject caculateRainfall(String longitude, String latitude) {
        JSONObject predict = new JSONObject();
        List<AttRainGrid> grids = waterAndRainMapper.loadByCoordinate(longitude.length() > 5 ? longitude.substring(0, 5) : longitude,
                latitude.length() > 5 ? latitude.substring(0, 5) : latitude);
        double distance = Integer.MAX_VALUE;
        AttRainGrid candidateGrid = null;
        double lng = Double.parseDouble(longitude);
        double lat = Double.parseDouble(latitude);
        for (AttRainGrid grid : grids) {
            double trueDistance = DistanceUtil.getDistance(
                    lat, lng, Double.parseDouble(grid.getLatitude()), Double.parseDouble(grid.getLongitude()))
                    .doubleValue();
            if (trueDistance <= distance) {
                candidateGrid = grid;
                distance = trueDistance;
            }
        }
        AttRainGrid finalCandidateGrid = candidateGrid;
        rainFallMap.forEach((key, rainfalls) -> {
            predict.put(key, finalCandidateGrid == null ? "-" : rainfalls[finalCandidateGrid.getRowIndex()][finalCandidateGrid.getColumnIndex()]);
        });
        return predict;
    }

    @Override
    public JSONObject rainAnalysis(String year, String month) {
        JSONObject result = new JSONObject();
        result.put("currentMonth", waterAndRainMapper.findStationTotalRainfallByYearMonth(year, month));
        result.put("past", waterAndRainMapper.findPastStationTotalRainfall(year, month));
        return result;
    }

    @Override
    public JSONArray rainfallPredict() {
        JSONArray result = new JSONArray();
        List<AttStBase> rainStations = waterAndRainMapper.findRainStation("").stream()
                .filter(attStBase -> StringUtils.isNotBlank(attStBase.getAreaCode())).collect(Collectors.toList());
        List<AttAdcdBase> adcdList = adcdBaseDataMapper.selectWenZhouCounty();
        Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        adcdMap.forEach((areaCode, areaName) -> {
            List<AttStBase> list = rainStations.stream().filter(attStBase -> attStBase.getAreaCode().equals(areaCode)).collect(Collectors.toList());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("areaName", areaName);
            for (AttStBase attStBase : list) {
                JSONObject caculate = caculateRainfall(attStBase.getStLong().toString(), attStBase.getStLat().toString());
                caculate.forEach((key, rainfall) -> {
                    if (String.valueOf(rainfall).equals("-")) {
                        jsonObject.put(key, "无雨");
                    } else {
                        jsonObject.put(key, Double.parseDouble(String.valueOf(rainfall)) > 0 ? "有雨" : "无雨");
                    }
                });
            }
            result.add(jsonObject);
        });
        return result;
    }

    @Override
    public List<JSONObject> surfaceRainfall() {
        List<JSONObject> result = new ArrayList<>();
        List<AttStBase> rainStations = waterAndRainMapper.findRainStation("").stream()
                .filter(attStBase -> StringUtils.isNotBlank(attStBase.getAreaCode())).collect(Collectors.toList());
        List<AttAdcdBase> adcdList = adcdBaseDataMapper.selectWenZhouCounty();
        Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        adcdMap.forEach((areaCode, areaName) -> {
            List<AttStBase> list = rainStations.stream().filter(attStBase -> attStBase.getAreaCode().equals(areaCode)).collect(Collectors.toList());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("areaName", areaName);
            JSONArray array = new JSONArray();
            for (AttStBase attStBase : list) {
                JSONObject caculate = caculateRainfall(attStBase.getStLong().toString(), attStBase.getStLat().toString());
                caculate.put("longitude", attStBase.getStLong().doubleValue());
                caculate.put("latitude", attStBase.getStLat().doubleValue());
                array.add(caculate);
            }
            array.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString("rainfall_1").equals("-") ? -1 : ((JSONObject) obj).getDoubleValue("rainfall_1")).reversed());
            jsonObject.put("rainData", array);
            result.add(jsonObject);
        });
        return result;
    }

    @Override
    public JSONArray situationAnalysis() {
        JSONArray result = new JSONArray();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, -12);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        List<StPptnR> rainfall = waterAndRainMapper.pastHoursRainfall(judgeRainfallTable(), format.format(calendar.getTime()));
        Map<String, List<AttStBase>> stationMap = waterAndRainMapper.findRainStation("").stream().collect(Collectors.groupingBy(AttStBase::getStCode));
        Iterator<StPptnR> iterator = rainfall.iterator();
        while (iterator.hasNext()) {
            StPptnR stPptnR = iterator.next();
            String stationCode = stPptnR.getStcd();
            if (!stationMap.containsKey(stationCode)) {
                iterator.remove();
            } else {
                stPptnR.setLongitude(stationMap.get(stationCode).get(0).getStLong().doubleValue());
                stPptnR.setLatitude(stationMap.get(stationCode).get(0).getStLat().doubleValue());
            }
        }
        Map<String, List<StPptnR>> map = rainfall.stream().collect(Collectors.groupingBy(StPptnR::getHourTime));
        map.forEach((hourTime, list) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("hourTime", hourTime);
            jsonObject.put("rainfall", list);
            result.add(jsonObject);
        });
        result.sort(Comparator.comparing(o -> ((JSONObject) o).getDate("hourTime")));
        return result;
    }

    @Override
    public JSONObject predictRainfallStatistic() {
        JSONObject result = new JSONObject();
        rainFallMap.forEach((key, array) -> {
            float sum = 0f;
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    sum += array[i][j];
                }
            }
            result.put(key, sum / (1066 * 700));
        });

        return result;
    }

    @Override
    public JSONObject predictRainfallByAreaName(String areaName) {
        JSONObject result = new JSONObject();
        areaName = StringUtils.isBlank(areaName) || areaName.equals("\"\"") ? null : areaName;
        List<AttRainGrid> grids = waterAndRainMapper.grids(areaName);
        rainFallMap.forEach((key, floats) -> {
            JSONArray array = new JSONArray();
            for (AttRainGrid grid : grids) {
                float rainfall = floats[grid.getRowIndex()][grid.getColumnIndex()];
                grid.setRainfall(rainfall);
                JSONObject object = new JSONObject();
                object.put("longitude", grid.getLongitude());
                object.put("latitude", grid.getLatitude());
                object.put("rainfall", grid.getRainfall());
                array.add(object);
            }
            result.put(key, grids.stream().mapToDouble(AttRainGrid::getRainfall).average().orElse(0d));
            result.put(key.concat("_data"), array);
        });
        return result;
    }

    @Override
    public JSONObject rainfallMaxValuePredict(Integer hour) {
        JSONObject result = new JSONObject();
        float[][] floats = rainFallMap.get("rainfall_" + hour);
        if (floats == null) {
            return new JSONObject();
        }
        List<AttRainGrid> grids = waterAndRainMapper.grids(StringUtils.EMPTY);
        for (AttRainGrid grid : grids) {
            grid.setRainfall(floats[grid.getRowIndex()][grid.getColumnIndex()]);
        }
        grids.sort(Comparator.comparing(AttRainGrid::getRainfall));
        AttRainGrid grid = grids.get(grids.size() - 1);
        result.put("maxRainfall", grid.getRainfall());
        result.put("maxRainfallLongitude", grid.getLongitude());
        result.put("maxRainfallLatitude", grid.getLatitude());
        result.put("maxRainfallAddress", GeomUtil.locate(grid.getLongitude(), grid.getLatitude()));
        Map<String, DoubleSummaryStatistics> areaMap = grids.stream().
                filter(attRainGrid -> StringUtils.isNotBlank(attRainGrid.getAreaName()))
                .collect(Collectors.groupingBy(AttRainGrid::getAreaName, Collectors.summarizingDouble(AttRainGrid::getRainfall)));
        List<Map.Entry<String, DoubleSummaryStatistics>> entries = areaMap.entrySet().stream().sorted(new Comparator<Map.Entry<String, DoubleSummaryStatistics>>() {
            @Override
            public int compare(Map.Entry<String, DoubleSummaryStatistics> o1, Map.Entry<String, DoubleSummaryStatistics> o2) {
                return (o1.getValue().getAverage() >= o2.getValue().getAverage() ? 1 : -1);
            }
        }).collect(Collectors.toList());
        result.put("maxRainfallAreaName", entries.get(entries.size() - 1).getKey());
        result.put("areaMaxRainfall", entries.get(entries.size() - 1).getValue().getAverage());
        result.put("areaMinRainfall", entries.get(0).getValue().getMin());
        Map<String, DoubleSummaryStatistics> basMap = grids.stream().filter(attRainGrid -> StringUtils.isNotBlank(attRainGrid.getBasin()))
                .collect(Collectors.groupingBy(AttRainGrid::getBasin, Collectors.summarizingDouble(AttRainGrid::getRainfall)));
        List<Map.Entry<String, DoubleSummaryStatistics>> basinEntires = basMap.entrySet().stream().sorted(new Comparator<Map.Entry<String, DoubleSummaryStatistics>>() {
            @Override
            public int compare(Map.Entry<String, DoubleSummaryStatistics> o1, Map.Entry<String, DoubleSummaryStatistics> o2) {
                return (o1.getValue().getAverage() >= o2.getValue().getAverage() ? 1 : -1);
            }
        }).collect(Collectors.toList());
        result.put("maxRainfallBasinName", basinEntires.get(basinEntires.size() - 1).getKey());
        result.put("maxBasinRainfall", basinEntires.get(basinEntires.size() - 1).getValue().getAverage());
        result.put("minBasinRainfall", basinEntires.get(0).getValue().getMin());
        return result;
    }

    @Override
    public JSONArray rainfallPredictGrid(String type) {
        if (StringUtils.isNotBlank(type) &&
                Arrays.stream(StationQueryCollectionType.values()).noneMatch(stationQueryCollectionType -> stationQueryCollectionType.type().equals(type))) {
            throw new IllegalArgumentException("统计类型不正确！");
        }
        List<AttRainGrid> grids = waterAndRainMapper.grids("鹿城区");
        Map<String, List<AttRainGrid>> gridMap;
//        if (StringUtils.isNotBlank(type) && type.equals(StationQueryCollectionType.AREA_CODE.type())) {
//            gridMap = grids.stream().filter(attRainGrid -> StringUtils.isNotBlank(attRainGrid.getAreaCode()))
//                    .collect(Collectors.groupingBy(AttRainGrid::getAreaCode));
//        } else if (StringUtils.isNotBlank(type) && type.equals(StationQueryCollectionType.BASIN.type())) {
//            gridMap = grids.stream().filter(attRainGrid -> StringUtils.isNotBlank(attRainGrid.getBasin()))
//                    .collect(Collectors.groupingBy(AttRainGrid::getBasin));
//        } else {
//
//        }
        //更细粒度的乡镇街道级别统计
        gridMap = grids.stream().filter(attRainGrid -> StringUtils.isNotBlank(attRainGrid.getTownName()))
                .collect(Collectors.groupingBy(AttRainGrid::getTownName));
        JSONArray result = new JSONArray();
        gridMap.forEach((key, list) -> {
            JSONObject object = new JSONObject();
            object.put("key", key);
            rainFallMap.forEach((rainfallKey, floats) -> {
                for (AttRainGrid grid : list) {
                    grid.setRainfall(floats[grid.getRowIndex()][grid.getColumnIndex()]);
                }
                double max = list.stream().mapToDouble(AttRainGrid::getRainfall).max().orElse(0d);
                double min = list.stream().mapToDouble(AttRainGrid::getRainfall).min().orElse(0d);
                double average = list.stream().mapToDouble(AttRainGrid::getRainfall).average().orElse(0d);
                object.put(rainfallKey, average);
                object.put(rainfallKey.replace("rainfall_", "").concat("hourMaxRainfall"), max);
                object.put(rainfallKey.replace("rainfall_", "").concat("hourMinRainfall"), min);
            });
            result.add(object);
        });
        if (StringUtils.isNotBlank(type) && type.equals(StationQueryCollectionType.AREA_CODE.type())) {
//            result.sort(Comparator.comparing(o -> ((JSONObject) o).getString("key")));
            Map<String, String> adcdMap = adcdBaseDataMapper
                    .selectWenZhouCounty().stream()
                    .collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
            // 先转行政区划
            for (Object o : result) {
                String key = ((JSONObject) o).getString("key");
                // 转换成我们洞头区的行政区划编码
                if ("330305000000".equals(key)) {
                    key = "330322000000";
                }
                ((JSONObject) o).put("key", key);
            }
            // 按行政区划排序
            result.sort(Comparator.comparing(o -> adcdSequenceList.indexOf(((JSONObject) o).getString("key"))));
            // 将行政区划编码转成名称
//            for (Object o : result) {
//                String key = ((JSONObject) o).getString("key");
//                // 转换成我们洞头区的行政区划编码
//                if ("330305000000".equals(key)) {
//                    key = "330322000000";
//                }
//                ((JSONObject) o).put("key", adcdMap.getOrDefault(key, ""));
//            }
        }
        return result;
    }

    /**
     * 降雨预报详细表格导出
     *
     * @param type     统计类型：行政区划/流域
     * @param response
     * @return
     */
    @Override
    public void exportRainfallPredictGrid(String type, HttpServletResponse response) {
        if (!type.equals("流域") && !type.equals("行政区划")) {
            throw new IllegalArgumentException("统计类型不正确！");
        }
        JSONArray jsonArray = rainfallPredictGrid(type);
        List<RainfallPredictGridResVo> list = jsonArray.toJavaList(RainfallPredictGridResVo.class);
        ExportUtils.exportExcel(list, "短临预报",
                new String[]{"名称", "1小时(mm)", "3小时(mm)", "6小时(mm)"},
                new String[]{"key", "rainfall_1", "rainfall_3", "rainfall_6"},
                response, null, Collections.emptyMap());
    }

    private class delayTask implements Delayed {

        private long now;

        private long endTimestamp;

        public delayTask(long time) {
            this.now = System.currentTimeMillis();
            this.endTimestamp = now + time;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(endTimestamp - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }
    }

    private class updateMapTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    //阻塞，每个小时往rainfallMap更新数据
                    delayQueue.take();
                    updateRainfallMap();
                    delayQueue.add(new delayTask(TimeUnit.HOURS.toMillis(1)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class UpdateWeatherPredictTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    //阻塞，每个小时更新一次天气预报数据
                    weatherTaskDelayQueue.take();
                    updateWeatherPredict();
                    //顺带更新温州市区的天气预报
                    weatherData = (JSONArray) requestWeatherData(WeatherQueryApi.SEVEN_DAY_WEATHER_PREDICT.getUrl(), new HashMap<>());
                    weatherTaskDelayQueue.add(new delayTask(TimeUnit.HOURS.toMillis(1)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        candidatePathBySystem();
        updateRainfallMap();
        updateWeatherPredict();
        delayQueue = new DelayQueue();
        weatherTaskDelayQueue = new DelayQueue();
        delayQueue.add(new delayTask(TimeUnit.SECONDS.toMillis(1)));
        weatherTaskDelayQueue.add(new delayTask(TimeUnit.SECONDS.toMillis(10)));
        //主线程中开启一个新的线程，用于更新雨量map
        new Thread(new updateMapTask()).start();
        //主线程中开启一个新的线程，用于更新天气预报数据
        new Thread(new UpdateWeatherPredictTask()).start();
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
        String table = StationType.RAINFALL.table().concat(year.concat(month));
        return table;
    }

    private boolean updateRainfallMap() {
        try {
            lock.lock();
            updating = true;
            rainFallMap.clear();
            String currentFileUrl = filePath.concat(path);
            File currentFilePath = new File(currentFileUrl);
            if (currentFilePath.isDirectory()) {
                File[] files = currentFilePath.listFiles();
                for (File file : files) {
                    if (file.isFile()) {
                        float[][] floats = handleFile(file);
                        String fileName = file.getName();
                        int hour = (int) Double.parseDouble(fileName.split("_")[1].replaceAll(".dat", "")) / 60;
                        String key = "rainfall_" + hour;
                        rainFallMap.put(key, floats);
                    }
                }
                try {
                    insertPredictHourRain4FloodPredict();
                    log.info("成功持久化降雨预报到洪水预报中");
                } catch (Exception e) {
                    log.error("持久化降雨预报到洪水预报中错误", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return updating;
        } finally {
//            try {
//                insertPredictHourRain4FloodPredict();
//                log.info("成功持久化降雨预报到洪水预报中");
//            } catch (Exception e) {
//                log.error("持久化降雨预报到洪水预报中错误", e);
//            }
            updating = false;
            lock.unlock();
            return true;
        }
    }

    private boolean updateWeatherPredict() {
        while (latch != null) {

        }
        latch = new CountDownLatch(map.size());
        JSONArray array = new JSONArray();
        map.forEach((areaName, areaWeatherCode) -> {
            weatherExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = currentWeather(areaName);
                        jsonObject.put("areaName", areaName);
                        array.add(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                }
            });
        });
        try {
            latch.await();
            redisTemplate.opsForValue().set(WEATHER_PREDICT_KEY, array.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            latch = null;
        }
        return true;
    }

    private void candidatePathBySystem() {
        String system = System.getProperty("os.name").toLowerCase();
        if (system.indexOf("windows") >= 0) {
            path = "\\currentData";
        } else if (system.indexOf("linux") >= 0) {
            path = "/currentData";
        } else {
            throw new RuntimeException("当前系统类型监测异常！请确认系统类型是否正确");
        }
    }

    private float[][] handleFile(File file) {
        float[][] rainfall = new float[700][1066];
        int i = 0, j = 0;
        try (LineIterator it = FileUtils.lineIterator(file, "gb2312");) {// 2022.05.09 改成gb2312 读取
            int index = 0;
            while (it.hasNext()) {
                String line = it.nextLine();
                if (index == 0) {
                    deduceRainfallPredictStartTime(line);
                }
                if (index++ >= 3) {
                    String[] s = line.replaceFirst("     ", "").split("\\s+");
                    for (String s1 : s) {
                        float v = Float.parseFloat(s1);
                        rainfall[i][j++] = v;
                        if (j == 1066) {
                            j = 0;
                            i++;
                        }
                    }
                }
            }
//            ArrayUtils.reverse(rainfall);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rainfall;
    }


    public void deduceRainfallPredictStartTime(String line) {
        try {
            rainfallPredictStartTime = LocalDateTime.parse(line.substring(11, 27).concat(":00"), rainfallPredictStartTimeFormatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void terminate() {
        map.clear();
        delayQueue.clear();
        log.info("已清除当前雨量缓存");
    }

    public boolean updateRainfallPredictMap() {
        if (updating) {
            log.info("-----当前已有线程抢占更新-----");
            return true;
        }
        return updateRainfallMap();
    }

    @Override
    public JSONObject rainfallPredictByIndex(int rowIndex, int columnIndex) {
        JSONObject result = new JSONObject();
        rainFallMap.forEach((rainfallKey, floats) -> {
            try {
                result.put(rainfallKey, floats[rowIndex][columnIndex]);
            } catch (Exception e) {
                result.put(rainfallKey, 0D);
            }
        });
        return result;
    }

    public static Map<String, float[][]> rainfallPredictData() {
        return rainFallMap;
    }

    @Override
    public List<JSONObject> listJsonForWeather(String type) {
        List<JSONObject> list = new ArrayList<>();
        if ("futureWeather".equals(type)) {
            try {
                if (forestWeatherData != null) {
                    list = forestWeatherData;
                } else {
                    String httpGet = HttpClientUtil.httpGet("http://t.weather.sojson.com/api/weather/city/101210701");

                    JSONObject jsonObject = JSON.parseObject(httpGet);
                    Integer status = jsonObject.getInteger("status");
                    if (200 != status) {
                        return null;
                    }
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray array = data.getJSONArray("forecast");
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        String sj = jsonObject1.getString("ymd");
                        String high = jsonObject1.getString("high");
                        String low = jsonObject1.getString("low");
                        String type1 = jsonObject1.getString("type");
                        JSONObject object = new JSONObject();
                        object.put("tm", sj);
                        object.put("wd", low.substring(3) + "-" + high.substring(3));
                        object.put("type", type1);
                        list.add(object);
                    }
                    //            redis.opsForValue().set("futureDayWeather15", JSON.toJSONString(list), 8, TimeUnit.HOURS);
                    forestWeatherData = list;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }
            boolean flag = false;
            for (JSONObject jsonObject : list) {
                String type1 = jsonObject.getString("type");
                if (type1.contains("雨")) {
                    flag = true;
                }
            }
            return flag ? list : new ArrayList<>();
        } else {
            String url = "https://mp.wztf121.com/data/wzweather/v2/townforecast/330302.json?r=" + System.currentTimeMillis();
            String httpGet = httpGet = HttpClientUtil.httpGet(url);
            JSONObject jsonObject = JSON.parseObject(httpGet);
            List<JSONObject> objectList = JSON.parseArray(jsonObject.getString("daily_forecast"), JSONObject.class);
            for (JSONObject object : objectList) {
                String sj = object.getString("date");
                String high = object.getString("tmp_max");
                String low = object.getString("tmp_min");
                String type1 = object.getString("cond_txt_n");
                JSONObject object1 = new JSONObject();
                object1.put("tm", sj);
                object1.put("wd", low + "-" + high + "℃");
                object1.put("type", type1);
                list.add(object1);
            }
            return list;
        }
    }

    //大屏降雨预报十五天接口，因为接口时好时坏所以做缓存
    @Scheduled(cron = "0 0/20 * * * ?")
    public void synWeather() {
        for (int i = 0; i < 30; i++) {
            String httpGet = HttpClientUtil.httpGet("http://t.weather.sojson.com/api/weather/city/101210701");
            JSONObject jsonObject = JSON.parseObject(httpGet);
            Integer status = jsonObject.getInteger("status");
            if (200 == status) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray array = data.getJSONArray("forecast");
//                redis.opsForValue().set("futureDayWeather15", JSON.toJSONString(array), 8, TimeUnit.HOURS);
                List<JSONObject> list = new ArrayList<>();
                JSONArray array1 = data.getJSONArray("forecast");
                for (int j = 0; j < array1.size(); j++) {
                    JSONObject jsonObject1 = array1.getJSONObject(j);
                    String sj = jsonObject1.getString("ymd");
                    String high = jsonObject1.getString("high");
                    String low = jsonObject1.getString("low");
                    String type1 = jsonObject1.getString("type");
                    JSONObject object = new JSONObject();
                    object.put("tm", sj);
                    object.put("wd", low.substring(3) + "-" + high.substring(3));
                    object.put("type", type1);
                    list.add(object);
                }
                forestWeatherData = list;
                log.info("------天气同步成功-------");
                break;
            }
            log.info("------天气同步失败-------");
            try {
                Thread.sleep(30000);
            } catch (Exception e) {

            }
        }
    }

    @Resource(type = FloodPredictMapper.class)
    FloodPredictMapper floodPredictMapper;

    public void insertPredictHourRain4FloodPredict() {
        float[][] oneHour = rainFallMap.get("rainfall_1");
        float[][] threeHour = rainFallMap.get("rainfall_3");
        float[][] sixHour = rainFallMap.get("rainfall_6");
        List<FloodPredictMapper.BusStationGrid> attStBases = this.floodPredictMapper.selectRRStation();
        LocalDateTime start = rainfallPredictStartTime;
        LocalDateTime thisHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        Date now = new Date();
        if (rainfallPredictStartTime == null) {
            log.info("降雨预报过期{} ", rainfallPredictStartTime);
            return;
        }

        for (FloodPredictMapper.BusStationGrid station : attStBases) {
            Map<Integer, Float> perHourRain = perHourRainInFutureSixHour(
                    oneHour[station.getRowIndex()][station.getColumnIndex()],
                    threeHour[station.getRowIndex()][station.getColumnIndex()],
                    sixHour[station.getRowIndex()][station.getColumnIndex()]
            );
            perHourRain.forEach((hour, rain) -> {
                LocalDateTime upTime = start.plusHours(hour - 1);
                if (upTime.isAfter(thisHour)) {
                    this.floodPredictMapper.insertPredictHourRain4FloodPredict(station.getStationCode(), DateUtils.localDateTime2Date(upTime), String.format("%.3f", rain), now, "1");
                }
            });
        }
    }

    public Map<Integer, Float> perHourRainInFutureSixHour(float oneSum, float threeSum, float sixSum) {
        Map<Integer, Float> result = new HashMap<>(6);
        result.put(1, oneSum);
        float twoAndThree = (threeSum - oneSum) / 2;
        result.put(2, twoAndThree);
        result.put(3, twoAndThree);
        float fourFiveSix = (sixSum - threeSum) / 3;
        result.put(4, fourFiveSix);
        result.put(5, fourFiveSix);
        result.put(6, fourFiveSix);
        return result;
    }

    @Override
    public void initStGrid() {
        List<FloodPredictMapper.BusStationGrid> attStBases = this.floodPredictMapper.selectStation4FloodPredict();
        for (FloodPredictMapper.BusStationGrid station : attStBases) {
            if (station.getLat() == null
                    || station.getLng() == null) {
                continue;
            }
            AttRainGrid grid = deduceStationGrid(station, 8);
            if (grid == null) {
                log.info("测站:{}无法推断格点", station.getStationName());
            } else {
                this.floodPredictMapper.updateStGrid(station.getStationCode(), grid.getRowIndex(), grid.getColumnIndex());
            }
        }
    }

    public AttRainGrid deduceStationGrid(FloodPredictMapper.BusStationGrid station, int initAccuracy) {
        int accuracy = initAccuracy;
        while (accuracy > 0) {
            List<AttRainGrid> attRainGrids = this.waterAndRainMapper.loadByCoordinate(station.getLng().setScale(accuracy, BigDecimal.ROUND_DOWN).toString(),
                    station.getLat().setScale(accuracy, BigDecimal.ROUND_DOWN).toString());
            if (CollectionUtils.isNotBlank(attRainGrids)) {
                if (attRainGrids.size() == 1) {
                    return attRainGrids.get(0);
                } else {
                    return mostClosedGrid(station, attRainGrids);
                }
            }
            accuracy--;
        }
        return null;
    }

    public AttRainGrid mostClosedGrid(FloodPredictMapper.BusStationGrid station, List<AttRainGrid> attRainGrids) {
        AttRainGrid minGrid = null;
        BigDecimal minDis = null;
        for (AttRainGrid attRainGrid : attRainGrids) {
            BigDecimal distance = DistanceUtil.getDistance(station.getLat().doubleValue(),
                    station.getLng().doubleValue(),
                    Double.parseDouble(attRainGrid.getLatitude()),
                    Double.parseDouble(attRainGrid.getLongitude())
            );
            if (minDis == null || minGrid == null) {
                minGrid = attRainGrid;
                minDis = distance;
            } else {
                if (minDis.doubleValue() > distance.doubleValue()) {
                    minGrid = attRainGrid;
                    minDis = distance;
                }
            }
        }
        return minGrid;
    }


    @Override
    public Object rainFallPredictForWz() {

        JSONObject jsonObject = predictRainfallByAreaName("鹿城区");
        List<JSONObject> jsonObjects = new ArrayList<>();

        if (jsonObject != null) {
            JSONArray rainfall_1_data = jsonObject.getJSONArray("rainfall_1_data");
            JSONArray rainfall_3_data = jsonObject.getJSONArray("rainfall_3_data");
            jsonObject.remove("rainfall_6_data");
            if (CollectionUtils.isNotBlank(rainfall_1_data)) {
                jsonObjects = JSONArray.parseArray(rainfall_1_data.toJSONString(), JSONObject.class);
                JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(jsonObjects));
                jsonObject.put("rainfall_1_data", jsonArray);
            }
            if (CollectionUtils.isNotBlank(rainfall_3_data)) {
                jsonObjects = JSONArray.parseArray(rainfall_1_data.toJSONString(), JSONObject.class);
                JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(jsonObjects));
                jsonObject.put("rainfall_3_data", jsonArray);
            }
            return jsonObject;
        } else {
            return null;
        }

//        String token = getToken(WzApiEnum.PREDIT_RAINFALL_BY_AREANAME);
//        //从温州请求数据
//        JSONObject param = new JSONObject();
//        param.put("areaName","鹿城区");
//        Map<String,String> header = new HashMap<>();
//        header.put("Content-Type", "application/json");
//        header.put("authorization",token);
//        String rs = "";
//        try {
//            rs = HttpClientUtil.httpPostHttps(WzApiEnum.PREDIT_RAINFALL_BY_AREANAME.getUrl(), param.toJSONString() , header);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        List<AttRainGrid> grids = waterAndRainMapper.rainGridList();
//        JSONObject jsonObject = JSONObject.parseObject(rs);
//        List<JSONObject> jsonObjects = new ArrayList<>();
//        if (jsonObject.getInteger("code") == 0) {
//            JSONObject data = jsonObject.getJSONObject("data");
//            if (data != null) {
//                jsonObject = data.getJSONObject("data");
//                JSONArray rainfall_1_data = jsonObject.getJSONArray("rainfall_1_data");
//                JSONArray rainfall_3_data = jsonObject.getJSONArray("rainfall_3_data");
//                jsonObject.remove("rainfall_6_data");
//                if (CollectionUtils.isNotBlank(rainfall_1_data)) {
//                    jsonObjects = JSONArray.parseArray(rainfall_1_data.toJSONString(), JSONObject.class);
//                    jsonObjects = filterLc(jsonObjects,grids);
//                    JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(jsonObjects));
//                    jsonObject.put("rainfall_1_data",jsonArray);
//                }
//                if (CollectionUtils.isNotBlank(rainfall_3_data)) {
//                    jsonObjects = JSONArray.parseArray(rainfall_1_data.toJSONString(), JSONObject.class);
//                    jsonObjects = filterLc(jsonObjects,grids);
//                    JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(jsonObjects));
//                    jsonObject.put("rainfall_3_data",jsonArray);
//                }
//                return jsonObject;
//            }else {
//                return null;
//            }
//        }

    }


    public List<JSONObject> filterLc(List<JSONObject> jsonObjects, List<AttRainGrid> grids) {
        List<JSONObject> result = new ArrayList<>();
        for (JSONObject jsonObject : jsonObjects) {
            String lat = jsonObject.getString("latitude");
            String log = jsonObject.getString("longitude");
            for (AttRainGrid grid : grids) {
                if (lat.equals(grid.getLatitude()) && log.equals(grid.getLongitude())) {
                    result.add(jsonObject);
                }
            }
        }
        return result;
    }


    @Override
    public Object rainFallGridForWz(Integer hour) {
        List<JSONObject> list = new ArrayList<>();
        JSONArray dataList = rainfallPredictGrid("行政区划");
        if (CollectionUtils.isNotBlank(dataList)) {
            list = JSONArray.parseArray(dataList.toJSONString(), JSONObject.class);
            List<String> strings = waterAndRainMapper.townNames();
            String judges = strings.stream().collect(Collectors.joining(","));
            list = list.stream().filter(j -> judges.contains(j.getString("key")))
                    .collect(Collectors.toList());
        }
        if (ObjectUtils.isNotEmpty(hour)) {
            switch (hour) {
                case 1:
                    list.forEach(l -> l.put("value", l.getDouble("rainfall_1")));
                    break;
                case 3:
                    list.forEach(l -> l.put("value", l.getDouble("rainfall_3")));
                    break;
                case 6:
                    list.forEach(l -> l.put("value", l.getDouble("rainfall_6")));
                    break;
            }
        }

//        List<JSONObject> list = new ArrayList<>();
//        String token = getToken(WzApiEnum.RAIN_FALL_GRID);
//        //从温州请求数据
//        JSONObject param = new JSONObject();
//        param.put("areaName","鹿城区");
//        Map<String,String> header = new HashMap<>();
//        header.put("Content-Type", "application/json");
//        header.put("authorization",token);
//        String rs = "";
//        try {
//            rs = HttpClientUtil.httpPostHttps(WzApiEnum.RAIN_FALL_GRID.getUrl(), param.toJSONString() , header);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        JSONObject jsonObject = JSONObject.parseObject(rs);
//        if (jsonObject.getInteger("code") == 0) {
//            JSONObject data = jsonObject.getJSONObject("data");
//            if (data != null){
//                JSONArray dataList = data.getJSONArray("data");
//                if (CollectionUtils.isNotBlank(dataList)) {
//                    list = JSONArray.parseArray(dataList.toJSONString(), JSONObject.class);
//                    List<String> strings = waterAndRainMapper.townNames();
//                    String judges = strings.stream().collect(Collectors.joining(","));
//                    list = list.stream().filter( j -> judges.contains(j.getString("key")))
//                            .collect(Collectors.toList());
//                }
//                if (ObjectUtils.isNotEmpty(hour)){
//                    switch (hour){
//                        case 1 :
//                            list.forEach( l -> l.put("value",l.getDouble("rainfall_1")));
//                            break;
//                        case 3:
//                            list.forEach( l -> l.put("value",l.getDouble("rainfall_3")));
//                            break;
//                        case 6:
//                            list.forEach( l -> l.put("value",l.getDouble("rainfall_6")));
//                            break;
//                    }
//                }
//            }
//        }
        return list;
    }

    public String getToken(WzApiEnum apiEnum) {
        if (redisTemplate.hasKey(apiEnum.getTokenKey())) {
            return redisTemplate.opsForValue().get(apiEnum.getTokenKey());
        } else {
            JSONObject param = new JSONObject();
            param.put("apiKey", apiEnum.getKey());
            param.put("apiSecret", apiEnum.getSecret());
            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            String rs = "";
            try {
                rs = HttpClientUtil.httpPostHttps("https://sgpt.wzsl.com:8081/api/data-management/busApiApply/getToken", param.toJSONString(), header);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = JSONObject.parseObject(rs);
            String token = jsonObject.getString("data");
            redisTemplate.opsForValue().set(apiEnum.getTokenKey(), token, 30, TimeUnit.MINUTES);
            return token;
        }
    }


}
