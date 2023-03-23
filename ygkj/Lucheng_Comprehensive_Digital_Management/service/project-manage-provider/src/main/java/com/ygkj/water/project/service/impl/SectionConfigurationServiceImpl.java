package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.gragh.rpc.GraphRpcService;
import com.ygkj.gragh.vo.request.SelectTideByTimeCodeVo;
import com.ygkj.project.model.SectionConfiguration;
import com.ygkj.project.model.SectionStorm;
import com.ygkj.project.model.WaveHeight;
import com.ygkj.project.model.WindSpeedRecord;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.feign.GraphClient;
import com.ygkj.water.project.mapper.SectionConfigurationMapper;
import com.ygkj.water.project.mapper.SectionStormMapper;
import com.ygkj.water.project.mapper.WindSpeedMapper;
import com.ygkj.water.project.service.SectionConfigurationService;
import com.ygkj.water.project.utils.WaveHeightUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2022/5/10 17:40
 * @Description:
 */
@Service
public class SectionConfigurationServiceImpl extends ServiceImpl<SectionConfigurationMapper, SectionConfiguration> implements SectionConfigurationService {

    @Autowired
    private SectionConfigurationMapper sectionConfigurationMapper;
    @Autowired
    private GraphClient graphClient;
    @Resource
    private WindSpeedMapper windSpeedMapper;
    @Resource
    private SectionStormMapper sectionStormMapper;

    private RedisTemplate redisTemplate;
    private static final String username = "jhq.slqy";
    private static final String password = "b263539a-c5cf-11ec-a019-00163e010604";
    private static final String GET_TOKEN_URL = "https://api-dshare.wenzhou.gov.cn/webapi2/get_token";

    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public JSONObject automaticAcquisition() {
        //查询天文潮下一小时整点数据
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1);
        Date time = calendar.getTime();
        String nextTime = DateUtils.format(time, "yyyy-MM-dd HH:00:00");
        Double astrotd = sectionConfigurationMapper.getTwcNextHours(nextTime);
        //因为新一年的刚开始，温州天文潮不一定有新一年的数据，拿去年的顶顶
        if (astrotd == null){
            calendar.add(Calendar.YEAR, -1);
            Date lastYearTime = calendar.getTime();
            String lastYearNextTime = DateUtils.format(lastYearTime, "yyyy-MM-dd HH:00:00");
            astrotd = sectionConfigurationMapper.getTwcNextHours(lastYearNextTime);
        }
        //查询风速
        LambdaQueryWrapper<WindSpeedRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WindSpeedRecord::getTime, nextTime);
        WindSpeedRecord windSpeedRecord = windSpeedMapper.selectOne(queryWrapper);
        if (windSpeedRecord == null) {
            windSpeedRecord = new WindSpeedRecord();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("speed", windSpeedRecord.getWindSpeed());
        jsonObject.put("astrotd", astrotd);
        return jsonObject;
    }

    @Override
    public JSONObject windows(String id) {
        SectionConfiguration configuration = sectionConfigurationMapper.selectSrcdById(id);
        //查询工程绑定的主潮位测站
        List<String> list = new ArrayList<>();
        list.add(configuration.getStcd());
        List<StTideR> lastTdz = getLastTdz(list);
        Double tdz = null;
        if (lastTdz != null && lastTdz.size() > 0) {
            tdz = lastTdz.get(0).getTdz();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1);
        Date time = calendar.getTime();
        LambdaQueryWrapper<SectionStorm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SectionStorm::getSectionId, configuration.getId()).eq(SectionStorm::getTm, DateUtils.format(time, "yyyy-MM-dd HH:00:00"));
        //h1等列表
        SectionStorm sectionStorm = sectionStormMapper.selectOne(queryWrapper);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", configuration.getId());
        jsonObject.put("name", configuration.getName());
        jsonObject.put("crestElevatio", configuration.getCrestElevatio());
        jsonObject.put("tdz", tdz);
        jsonObject.put("H2", sectionStorm.getH2());
        return jsonObject;
    }

    /**
     * 江心屿最新潮位
     *
     * @return
     */
    private List<StTideR> getLastTdz(List<String> codes) {
        //查询实时潮位数据。
        String table = StationType.TIDE.table().concat(LocalDate.now().format(yyyyMM));
        SelectTideByTimeCodeVo selectTideByTimeCodeVo = new SelectTideByTimeCodeVo();
        selectTideByTimeCodeVo.setDataTime(null);
        selectTideByTimeCodeVo.setStcds(codes);
        selectTideByTimeCodeVo.setTable(table);
        List<StTideR> stTideRS = null;
        if (codes.size() > 0) {
            stTideRS = graphClient.selectTideByTimeCode(selectTideByTimeCodeVo);
        }
        return stTideRS;
    }

    @Override
    public List<SectionConfiguration> forecastSectionPage(List<SectionConfiguration> list) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1);
        Date time = calendar.getTime();
        if (list != null && list.size() > 0) {
            List<String> stcds = list.stream().map(SectionConfiguration::getStcd).collect(Collectors.toList());
            List<StTideR> lastTdz = getLastTdz(stcds);
            for (SectionConfiguration sectionConfiguration : list) {
                LambdaQueryWrapper<SectionStorm> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SectionStorm::getSectionId, sectionConfiguration.getId()).eq(SectionStorm::getTm, DateUtils.format(time, "yyyy-MM-dd HH:00:00"));
                //h1等列表
                SectionStorm sectionStorm = sectionStormMapper.selectOne(queryWrapper);
                if (sectionStorm != null) {
                    sectionConfiguration.setH1(sectionStorm.getH1());
                    sectionConfiguration.setH2(sectionStorm.getH2());
                    sectionConfiguration.setH13(sectionStorm.getH13());
                }
                sectionConfiguration.setTdz(null);
                if (lastTdz != null && lastTdz.size() > 0){
                    List<StTideR> collect = lastTdz.stream().filter(s -> s.getMgstcd().equals(sectionConfiguration.getStcd())).collect(Collectors.toList());
                    if (collect.size() > 0){
                        sectionConfiguration.setTdz(collect.get(0).getTdz());
                    }
                }
            }
        }
        return list;
    }


    @Override
    public List<SectionStorm> forecastSectionInfo(String sectionId) {
        LambdaQueryWrapper<SectionStorm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SectionStorm::getSectionId, sectionId).ge(SectionStorm::getTm, new Date());
        //h1等列表
        List<SectionStorm> list = sectionStormMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            //塘顶高程
            SectionConfiguration sectionConfiguration = sectionConfigurationMapper.selectSrcdById(sectionId);
            Double crestElevatio = sectionConfiguration.getCrestElevatio();
            //最新潮位数据
            List<String> stcds = new ArrayList<>();
            stcds.add(sectionConfiguration.getStcd());
            List<StTideR> lastTdz = getLastTdz(stcds);
            Double tdz = null;
            if (lastTdz != null && lastTdz.size() > 0) {
                tdz = lastTdz.get(0).getTdz();
            }
            Double finalTdz = tdz;
            list.forEach(sectionStorm -> {
                sectionStorm.setCrestElevatio(crestElevatio);
                sectionStorm.setTdz(finalTdz);
            });
        }

        return list;
    }

    @Override
    public String getToken() {
        Boolean getToken = redisTemplate.hasKey("getToken");
        if (getToken) {
            return (String) redisTemplate.opsForValue().get("getToken");
        }
        //查询数据
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        try {
            String https = HttpClientUtil.httpPostHttps(GET_TOKEN_URL, map, null);
            JSONObject jsonObject = JSONObject.parseObject(https);
            if (jsonObject.containsKey("code") && "0".equals(jsonObject.getString("code"))) {
                //认证的时候是要在获取的token前面加上jwt和一个空格
                String token = "jwt " + jsonObject.getString("token");
                //缓存token
                redisTemplate.opsForValue().set("getToken", token, 25, TimeUnit.MINUTES);
                return token;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<JSONObject> forecastMaxHour(SectionConfiguration section) {
        List<SectionConfiguration> configurationList = sectionConfigurationMapper.selectSrcdAll(section);
        List<JSONObject> jsonObjects = sectionStormMapper.getMaxH2ByTime(new Date());
        List<JSONObject> list = new ArrayList<>();
        for (SectionConfiguration sectionConfiguration : configurationList) {
            JSONObject jsonObject = new JSONObject();
            if (jsonObjects.size() > 0){
                List<JSONObject> collect = jsonObjects.stream().filter(m -> m.getString("section_id").equals(sectionConfiguration.getId())).collect(Collectors.toList());
                if (collect.size() > 0){
                    jsonObject = collect.get(0);
                    Double blueLevel = sectionConfiguration.getBlueLevel();
                    Double h2 = jsonObject.getDouble("h2");
                    if (blueLevel < h2){
                        jsonObject.put("warning", true);
                    } else {
                        jsonObject.put("warning", false);
                    }
                } else {
                    jsonObject.put("sectionId", sectionConfiguration.getId());
                    jsonObject.put("tm", "");
                    jsonObject.put("h2", "");
                    jsonObject.put("warning", false);
                }
            }
            jsonObject.put("name", sectionConfiguration.getName());
            jsonObject.put("seawallName", sectionConfiguration.getSeawallName());
            jsonObject.put("blueLevel", sectionConfiguration.getBlueLevel());
            jsonObject.put("latitude", sectionConfiguration.getLatitude());
            jsonObject.put("longitude", sectionConfiguration.getLongitude());
            list.add(jsonObject);
        }
        return list;
    }

    @Override
    public JSONObject forecastSectionHour(String sectionId) {
        SectionConfiguration sectionConfiguration = sectionConfigurationMapper.selectSrcdById(sectionId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",sectionConfiguration.getName());
        jsonObject.put("blueLevel",sectionConfiguration.getBlueLevel());

        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        calendar.add(Calendar.DATE, -1);
        Date start = calendar.getTime();
        List<String> tables = getTablesName(calendar, year, month, "TT");
        List<JSONObject> list = new ArrayList<>();
        Double maxLevel = null, minLevel = null;
        Date maxTime = null;
        if (StringUtils.isNotBlank(sectionConfiguration.getStcd())){
            //24小时整点潮位数据
            List<StTideR> stTideRS = sectionStormMapper.selectHourTide(tables, start, now, sectionConfiguration.getStcd());
            //预报风暴潮位
            QueryWrapper<SectionStorm> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("section_id", sectionId);
            queryWrapper.ge("tm", start);
            List<SectionStorm> sectionStorms = sectionStormMapper.selectList(queryWrapper);
            if (stTideRS.size() > 0 || sectionStorms.size() > 0){
                List<String> dateSting = getDateSting();
                for (int i = 0; i < dateSting.size(); i++) {
                    String tm = dateSting.get(i);
                    JSONObject object = new JSONObject();
                    object.put("tm", tm);
                    object.put("nowTdz", null);
                    object.put("futureTdz", null);
                    if (stTideRS.size() > 0){
                        List<StTideR> collect = stTideRS.stream().filter(m -> DateUtils.format(m.getTm(), "yyyy-MM-dd HH:00:00").equals(tm)).collect(Collectors.toList());
                        if (collect.size() > 0){
                            StTideR stTideR = collect.get(0);
                            object.put("nowTdz", stTideR.getTdz());
                        }
                    }
                    if (sectionStorms.size() > 0){
                        List<SectionStorm> collect = sectionStorms.stream().filter(m -> tm.equals(DateUtils.format(m.getTm(), "yyyy-MM-dd HH:00:00"))).collect(Collectors.toList());
                        if (collect.size() > 0){
                            SectionStorm sectionStorm = collect.get(0);
                            Double h2 = sectionStorm.getH2();
                            Date date = sectionStorm.getTm();
                            if (maxLevel == null){
                                maxLevel = h2;
                                maxTime = date;
                            } else {
                                if (maxLevel < h2){
                                    maxLevel = h2;
                                    maxTime = date;
                                }
                            }
                            if (minLevel == null){
                                minLevel = h2;
                            } else {
                                if (minLevel > h2){
                                    minLevel = h2;
                                }
                            }
                            object.put("futureTdz", sectionStorm.getH2());
                        }
                    }
                    list.add(object);
                }
            }
        }
        jsonObject.put("list", list);
        jsonObject.put("maxLevel", maxLevel);
        jsonObject.put("minLevel", minLevel);
        jsonObject.put("maxTime", maxTime);
        return jsonObject;
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

    public List<String> getDateSting(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date endTime = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH,-2);
        List<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        calendar.add(Calendar.HOUR_OF_DAY,1);
        while (calendar.getTime().compareTo(endTime) <= 0){
            String format = sdf.format(calendar.getTime());
            result.add(format);
            calendar.add(Calendar.HOUR_OF_DAY,1);
        }
        return result;
    }
}
