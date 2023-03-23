package com.ygkj.digitization.task;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.feign.NoticeClient;
import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.mapper.WarnUserMapper;
import com.ygkj.digitization.mapper.WaterWarnInformationMapper;
import com.ygkj.digitization.service.WaterRuleConfigurationService;
import com.ygkj.digitization.service.WaterWarnInformationService;
import com.ygkj.digitization.util.CommonUtil;
import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.model.TideWarnSendInfo;
import com.ygkj.digitization.vo.request.RuleDetailInfo;
import com.ygkj.digitization.vo.request.WaterRuleConfigurationReqVo;
import com.ygkj.digitization.vo.request.WaterWarnInformationReqVo;
import com.ygkj.digitization.vo.response.WaterRuleConfigurationRespVo;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import ygkj.com.util.MydateUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 水文预警系统task
 */

@Component
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class WaterWarnSystemTask {

    @Resource
    private WaterRuleConfigurationService waterRuleConfigurationService;
    @Resource
    private WaterWarnInformationService waterWarnInformationService;
    @Autowired
    private AttStBaseMapper stBaseMapper;
    @Resource
    private NoticeClient noticeClient;
    @Resource
    private WaterWarnInformationMapper waterWarnInformationMapper;
    @Resource
    private WarnUserMapper warnUserMapper;

    private static Map<String,String> adcdMap = new HashMap<>();

    static {
        adcdMap.put("330303","龙湾区农业农村局");
        adcdMap.put("330383","龙港市农业农村局");
        adcdMap.put("330302","鹿城区农业农村局");
        adcdMap.put("330305","洞头区农业农村局");
        adcdMap.put("330328","文成县水利局");
        adcdMap.put("330324","永嘉县水利局");
        adcdMap.put("330327","苍南县水利局");
        adcdMap.put("330304","瓯海区水利局");
        adcdMap.put("330381","瑞安市水利局");
        adcdMap.put("330382","乐清市水利局");
        adcdMap.put("330326","平阳县水利局");
        adcdMap.put("330329","泰顺县水利局");
    }
    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");


    @Scheduled(cron = "15 20 0/1 * * ? ")
//    @Scheduled(cron = "15 0/1 * * * ? ")
    @Test
    public void execute() throws Exception {
        log.info("水文预警系统task开始");
        long startTime = System.currentTimeMillis();
        List<WaterRuleConfigurationRespVo> list = waterRuleConfigurationService.loadPage(new WaterRuleConfigurationReqVo()).getList();
        Map<String, List<WaterRuleConfigurationRespVo>> listMap = list.stream().filter(e->!StringUtils.isEmpty(e.getType())).collect(Collectors.groupingBy(WaterRuleConfigurationRespVo::getType));
        Iterator<Map.Entry<String, List<WaterRuleConfigurationRespVo>>> iterator = listMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<WaterRuleConfigurationRespVo>> entry = iterator.next();
            String type = entry.getKey();//预警类型 1:时段雨量警报 2:日雨量警报 3:河道水情警报 4:水库水情警报 5:潮位警报
            List<WaterRuleConfigurationRespVo> configurationRespVos = entry.getValue();
            switch (type) {
                case "1":
                    handleTimePeriodRainfallAlert(configurationRespVos);
                    break;
                case "2":
                    handleDailyRainfallWarn(configurationRespVos);
                    break;
                case "3":
                    handleRiverWaterConditionWarn(configurationRespVos);
                    break;
                case "4":
                    handleReservoirWaterConditionWarn(configurationRespVos);
                    break;
                case "5":
                    handleTideAlarm(configurationRespVos);
                    break;
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("水文预警系统task结束，所用时间:{}",endTime - startTime);
    }

    /**
     * 处理时段雨量警报
     * @param configurationRespVos
     */
    private void handleTimePeriodRainfallAlert(List<WaterRuleConfigurationRespVo> configurationRespVos) throws Exception {
        Date currentDate = new Date();
        for (WaterRuleConfigurationRespVo configurationRespVo : configurationRespVos) {
            String stationIds = configurationRespVo.getStationIds();
            String ruleDetailInfoStr = configurationRespVo.getRuleDetailInfoStr();
            String sendType = configurationRespVo.getSendType();//发送方式
            String warnTemplteConent = configurationRespVo.getWarnTemplteConent();//预警内容模板
            if ( !StringUtils.isEmpty(stationIds) && !StringUtils.isEmpty(ruleDetailInfoStr)) {
                List<RuleDetailInfo> ruleDetailInfos = JSONObject.parseArray(ruleDetailInfoStr, RuleDetailInfo.class);

                for (RuleDetailInfo ruleDetailInfo : ruleDetailInfos) {
                    Integer hour = ruleDetailInfo.getHour();
                    Double overData = ruleDetailInfo.getOverData().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    Date priorDate = MydateUtil.getNextDay(currentDate, null,null,null,-hour,null,null);

                    String[] tableNames = DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(), DateUtils.format(priorDate, "yyyy-MM-dd HH:mm:ss"),
                            DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss"));

                    List<com.ygkj.gragh.model.StPptnR> stPptnRList = waterRuleConfigurationService.selectHourRainfall(Arrays.asList(tableNames),
                            DateUtils.format(priorDate, "yyyy-MM-dd HH:mm:ss"), DateUtils.format(currentDate, "yyyy-MM-dd HH:mm:ss"), stationIds);

//                    List<StPptnR> stPptnRList = waterRuleConfigurationService.selectHourRainfall(Arrays.asList(tableNames),
//                            "2021-07-10 15:51:54", "2021-07-14 16:51:54", stationIds);//测试时使用

                    List<String> stcds = stPptnRList.stream().filter(e -> e.getDrp() >= overData).map(k -> k.getMgstcd()).distinct().collect(Collectors.toList());
                    if (!stcds.isEmpty()) {
                        List<AttStBase> attStBases = stBaseMapper.selectByStCodeList(stcds).stream().filter(e -> "1".equals(e.getIsSurfaceRainStation())).collect(Collectors.toList());;
                        log.info("时段雨量警报超过预警的数据测站条数:{}",attStBases.size());
                        String sendInfoDeatilInfo = getSendInfoDeatilInfo(attStBases, stPptnRList);//预警信息
                        sendMessage(sendType,configurationRespVo,sendInfoDeatilInfo,attStBases,currentDate,
                                DateUtils.format(priorDate,"MM月dd日HH时") + "至" + DateUtils.format(currentDate,"MM月dd日HH时"),"时段雨量预警");
                    }
                }
            }
        }
    }

    /**
     * 处理日雨量警报
     * @param configurationRespVos
     */
    private void handleDailyRainfallWarn(List<WaterRuleConfigurationRespVo> configurationRespVos) throws Exception {
        Date currentDate = new Date();
        for (WaterRuleConfigurationRespVo configurationRespVo : configurationRespVos) {
            String stationIds = configurationRespVo.getStationIds();
            String ruleDetailInfoStr = configurationRespVo.getRuleDetailInfoStr();
            String sendType = configurationRespVo.getSendType();//发送方式
            String warnTemplteConent = configurationRespVo.getWarnTemplteConent();//预警内容模板
            if (!StringUtils.isEmpty(stationIds) && !StringUtils.isEmpty(ruleDetailInfoStr)) {
                List<RuleDetailInfo> ruleDetailInfos = JSONObject.parseArray(ruleDetailInfoStr, RuleDetailInfo.class);

                List<AttStBase> attStBases = stBaseMapper.selectByStCodeList(Arrays.asList(stationIds.split(",")))
                        .stream().filter(e -> "1".equals(e.getIsSurfaceRainStation())).collect(Collectors.toList());

                Map<String, String> map = judgeDailyRainSendOrPrintFlag(ruleDetailInfos, currentDate, attStBases, warnTemplteConent);
                String detailInfo = map.get("detailInfo");
                if (!StringUtils.isEmpty(detailInfo) && detailInfo.length() > 0) {
                    String time = map.get("time");
                    sendMessage(sendType,configurationRespVo,detailInfo,attStBases,currentDate,time,"日雨量警报");
                }
            }
        }
    }


    /**
     * 判断日雨量警报是否发送短信或打印pdf传真
     * @return
     */
    public Map<String,String> judgeDailyRainSendOrPrintFlag(List<RuleDetailInfo> ruleDetailInfos,
                                                Date currentDate,
                                                List<AttStBase> attStBases,
                                                String warnTemplteConent) {
        StringBuilder sBuilder = new StringBuilder();
        Map<String,String> map = new HashMap<>();
        StringBuilder timeBuilder = new StringBuilder();
        for (RuleDetailInfo ruleDetailInfo : ruleDetailInfos) {
            double overData = ruleDetailInfo.getOverData().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //2:日雨量警报-每天几时生成日雨量超过多少毫米 3:日雨量警报-每天几时全市面雨量大于等于多少毫米
            String type = ruleDetailInfo.getType();
            Integer hour = ruleDetailInfo.getHour();
            String endTime = DateUtils.format(currentDate,"yyyy-MM-dd") + " " + hour + ":00:00";
            Date priorDate = MydateUtil.getNextDay(DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",endTime), null,null,-1,null,null,null);

            String startTime = DateUtils.format(priorDate,"yyyy-MM-dd HH:mm:ss");

            if (timeBuilder.length() ==0) {
                timeBuilder.append(DateUtils.format(priorDate,"yyyy年MM月dd日") + hour + "时至" + DateUtils.format(currentDate,"yyyy年MM月dd日")+ hour + "时");
            }

            String[] tableNames = DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(),startTime ,endTime);

            if (attStBases.isEmpty()) {
                continue;
            }
            List<String> codes = attStBases.stream().map(e -> e.getStCode()).distinct().collect(Collectors.toList());
                            List<StPptnR> stPptnRList = waterRuleConfigurationService.selectHourRainfall(Arrays.asList(tableNames),
                    startTime, endTime, String.join(",",codes));
//            List<StPptnR> stPptnRList = waterRuleConfigurationService.selectHourRainfall(Arrays.asList(tableNames),
//                    "2021-07-10 15:51:54", "2021-07-14 16:51:54", String.join(",",codes));//测试时使用
            double sum = stPptnRList.stream().mapToDouble(k -> k.getDrp()).sum();
            if ("2".equals(type)) {
                if (sum > overData) {
                    sBuilder.append("具体为:" + getSendInfoDeatilInfo(attStBases,stPptnRList));
                }
            } else if ("3".equals(type)) {
                if (new BigDecimal(sum + "").divide(new BigDecimal(stPptnRList.size()),2,BigDecimal.ROUND_HALF_UP).doubleValue() >= overData) {
                    sBuilder.append("具体为:" + getSendInfoDeatilInfo(attStBases,stPptnRList));
                }
            }
        }
        map.put("detailInfo",sBuilder.toString());
        map.put("time",timeBuilder.toString());
        return map;
    }

    /**
     * 处理河道水情警报
     * @param configurationRespVos
     */
    private void handleRiverWaterConditionWarn(List<WaterRuleConfigurationRespVo> configurationRespVos) throws Exception {
        Date currentDate = new Date();
        for (WaterRuleConfigurationRespVo configurationRespVo : configurationRespVos) {
            String stationIds = configurationRespVo.getStationIds();
            String ruleDetailInfoStr = configurationRespVo.getRuleDetailInfoStr();
            String sendType = configurationRespVo.getSendType();//发送方式
            String warnTemplteConent = configurationRespVo.getWarnTemplteConent();//预警内容模板
            if (!StringUtils.isEmpty(stationIds) && !StringUtils.isEmpty(ruleDetailInfoStr)) {
                List<RuleDetailInfo> ruleDetailInfos = JSONObject.parseArray(ruleDetailInfoStr, RuleDetailInfo.class);

                List<AttStBase> attStBases = stBaseMapper.selectByStCodeList(Arrays.asList(stationIds.split(",")));
                ruleDetailInfos = ruleDetailInfos.stream().filter(e->e.getOverData() != null).collect(Collectors.toList());
                for (RuleDetailInfo ruleDetailInfo : ruleDetailInfos) {
                    Double overData = ruleDetailInfo.getOverData().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    String table = StationType.RIVER.table().concat(currentDate == null ? LocalDate.now().format(yyyyMM) : DateUtils.date2LocalDate(currentDate).format(yyyyMM));

                    List<StRiverR> riverLevel = waterRuleConfigurationService.selectRiverByTimeCode(table, Arrays.asList(stationIds.split(",")), currentDate);

                    //5:河道水情警报-当前水位超过警戒水位（）米生成警报 6:河道水情警报-当前水位超过保证水位（）米生成警报
                    String type = ruleDetailInfo.getType();
                    List<AttStBase> attStBasesNew = null;
                    JSONObject jo = null;
                    if ("5".equals(type)) {
                        jo = exceedWaterLevel(attStBases, riverLevel, "1", overData);
                    } else if ("6".equals(type)) {
                        jo = exceedWaterLevel(attStBases,riverLevel,"2",overData);
                    }
                    if (jo != null) {
                        attStBasesNew = (List<AttStBase>) jo.get("attStBaseNews");
                    }
                    if (!CollectionUtils.isEmpty(attStBasesNew)){
                        String sendWaterInfoDeatilInfo = jo.getString("sendWaterInfoDeatilInfo");
                        sendMessage(sendType,configurationRespVo,sendWaterInfoDeatilInfo,attStBasesNew,currentDate,
                                DateUtils.format(currentDate,"yyyy年MM月dd日HH时"),"水文预警");
                    }
                }
            }
        }
    }

    /**
     * 查找超过警戒水位的过滤方法
     * @param attStBases 测站list
     * @param riverLevel 最新水位数据
     * @param type 1:警戒水位 2:保证水位 3:汛限水位 4:防洪高水位
     * @return
     */
    public JSONObject exceedWaterLevel(List<AttStBase> attStBases,List<StRiverR> riverLevel,String type,Double overData) {
        JSONObject jo = null;
        if (!attStBases.isEmpty()) {
            jo = new JSONObject();
            List<AttStBase> attStBaseNews = new ArrayList<>();
            StringBuilder sBuilder = new StringBuilder();
            for (AttStBase attStBase : attStBases) {
                List<StRiverR> collect = riverLevel.stream().filter(e -> e.getStcd().equals(attStBase.getStCode())).collect(Collectors.toList());
                if (!collect.isEmpty()) {
                    Double z = collect.get(0).getZ();
                    switch (type) {
                        case "1":
                            Double warningLevel = attStBase.getWarningLevel();
                            if (warningLevel != null && z > (warningLevel + overData)) {
                                attStBaseNews.add(attStBase);
                                double v = new BigDecimal((z - warningLevel) + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                sBuilder.append(attStBase.getStName() + "水位" + z + "米，超过警戒水位"+ v +"米。");
                            }
                            break;
                        case "2":
                            Double guaranteeLevel = attStBase.getGuaranteeLevel();
                            if (guaranteeLevel != null && z > (guaranteeLevel + overData)) {
                                attStBaseNews.add(attStBase);
                                double v = new BigDecimal((z - guaranteeLevel) + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                sBuilder.append(attStBase.getStName() + "水位" + z + "米，超过保证水位"+v+"米。");
                            }
                            break;
                        case "3":
                            Double limitLevel = attStBase.getLimitLevel();
                            if (limitLevel != null && z > (limitLevel+ overData)) {
                                attStBaseNews.add(attStBase);
                                double v = new BigDecimal((z - limitLevel) + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                sBuilder.append(attStBase.getStName() + "水位" + z + "米，超过汛限水位"+v+"米。");
                            }
                            break;
                        case "4":
                            Double floodLevel = attStBase.getFloodLevel();
                            if (floodLevel != null && z > (floodLevel+ overData)) {
                                attStBaseNews.add(attStBase);
                                double v = new BigDecimal((z - floodLevel) + "").setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                sBuilder.append(attStBase.getStName() + "水位" + z + "米，超过防洪高水位"+ v +"米。");
                            }
                            break;
                    }
                }
            }
            jo.put("attStBaseNews",attStBaseNews);
            jo.put("sendWaterInfoDeatilInfo",sBuilder.toString());
        }
        return jo;
    }

    /**
     * 处理水库水情警报
     * @param configurationRespVos
     */
    private void handleReservoirWaterConditionWarn(List<WaterRuleConfigurationRespVo> configurationRespVos) throws Exception {
        Date currentDate = new Date();
        for (WaterRuleConfigurationRespVo configurationRespVo : configurationRespVos) {
            String stationIds = configurationRespVo.getStationIds();
            String ruleDetailInfoStr = configurationRespVo.getRuleDetailInfoStr();
            String sendType = configurationRespVo.getSendType();//发送方式
            String warnTemplteConent = configurationRespVo.getWarnTemplteConent();//预警内容模板
            if (!StringUtils.isEmpty(stationIds) && !StringUtils.isEmpty(ruleDetailInfoStr)) {
                List<RuleDetailInfo> ruleDetailInfos = JSONObject.parseArray(ruleDetailInfoStr, RuleDetailInfo.class);

                List<AttStBase> attStBases = stBaseMapper.selectByStCodeList(Arrays.asList(stationIds.split(",")));

                for (RuleDetailInfo ruleDetailInfo : ruleDetailInfos) {
                    Double overData = ruleDetailInfo.getOverData().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    String table = StationType.RESERVOIR.table().concat(currentDate == null ? LocalDate.now().format(yyyyMM) : DateUtils.date2LocalDate(currentDate).format(yyyyMM));

                    List<StRiverR> riverLevel = waterRuleConfigurationService.selectRsvrWaterLevelByTimeCode(table, Arrays.asList(stationIds.split(",")), currentDate);

                    //7:水库水情警报-当前水位超过汛限水位（）米生成警报 8:水库水情警报-超过防洪高水位（）米
                    String type = ruleDetailInfo.getType();
                    List<AttStBase> attStBasesNew = null;
                    JSONObject jo = null;
                    if ("7".equals(type)) {
                        jo = exceedWaterLevel(attStBases, riverLevel, "3",overData);
                    } else if ("8".equals(type)) {
                        jo = exceedWaterLevel(attStBases,riverLevel,"4",overData);
                    }
                    if (jo != null) {
                        attStBasesNew = (List<AttStBase>) jo.get("attStBaseNews");
                    }
                    if (!CollectionUtils.isEmpty(attStBasesNew)){
                        String sendWaterInfoDeatilInfo = jo.getString("sendWaterInfoDeatilInfo");
                        sendMessage(sendType,configurationRespVo,sendWaterInfoDeatilInfo,attStBasesNew,
                                currentDate,DateUtils.format(currentDate,"yyyy年MM月dd日HH时"),"水文预警");
                    }
                }
            }
        }
    }

    /**
     * 处理潮位警报
     * @param configurationRespVos
     */
    private void handleTideAlarm(List<WaterRuleConfigurationRespVo> configurationRespVos) throws Exception {
        Date currentDate = new Date();
        for (WaterRuleConfigurationRespVo configurationRespVo : configurationRespVos) {
            String stationIds = configurationRespVo.getStationIds();
            String ruleDetailInfoStr = configurationRespVo.getRuleDetailInfoStr();
            String sendType = configurationRespVo.getSendType();//发送方式
            String warnTemplteConent = configurationRespVo.getWarnTemplteConent();//预警内容模板
            if (!StringUtils.isEmpty(stationIds) && !StringUtils.isEmpty(ruleDetailInfoStr)) {
                List<RuleDetailInfo> ruleDetailInfos = JSONObject.parseArray(ruleDetailInfoStr, RuleDetailInfo.class);

                List<AttStBase> attStBases = stBaseMapper.selectByStCodeList(Arrays.asList(stationIds.split(",")));

                List<TideWarnSendInfo> tideWarnSendInfos = new ArrayList<>();
                List<AttStBase> attStBasesNew = new ArrayList<>();
                for (RuleDetailInfo ruleDetailInfo : ruleDetailInfos) {
                    Double overData = ruleDetailInfo.getOverData().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                    String table = StationType.TIDE.table().concat(currentDate == null ? LocalDate.now().format(yyyyMM) : DateUtils.date2LocalDate(currentDate).format(yyyyMM));

                    List<StTideR> stTideRList = waterRuleConfigurationService.selectTideByTimeCode(table, Arrays.asList(stationIds.split(",")), currentDate);

                    //9:潮位警报-当前潮位超过（）米显示为蓝色预警
                    //10:当前潮位超过（）米显示为黄色预警 11:潮位警报-当前潮位超过（）米显示为橙色预警 12:潮位警报-当前潮位超过（）米显示为红色预警
                    String type = ruleDetailInfo.getType();
                    exceedTideLevel(attStBases, stTideRList, overData, type,attStBasesNew,tideWarnSendInfos);

                }
                if (!CollectionUtils.isEmpty(attStBasesNew) && !CollectionUtils.isEmpty(tideWarnSendInfos)){
                    String sendWaterInfoDeatilInfo = assembTideInfo(tideWarnSendInfos);
                    if (sendWaterInfoDeatilInfo.length() > 0) {
                        sendWaterInfoDeatilInfo += "。";
                    }
                    sendMessage(sendType,configurationRespVo,sendWaterInfoDeatilInfo,
                            attStBasesNew,currentDate,DateUtils.format(currentDate,"yyyy年MM月dd日HH时"),"潮位警报");
                }
            }
        }
    }

    /**
     * 组装潮位发送信息数据str
     * @param tideWarnSendInfos
     * @return
     */
    public String assembTideInfo(List<TideWarnSendInfo> tideWarnSendInfos) {
        StringBuilder sBuilder = new StringBuilder();
        Map<String, List<TideWarnSendInfo>> typeMap = tideWarnSendInfos.stream().collect(Collectors.groupingBy(TideWarnSendInfo::getType));
        Iterator<Map.Entry<String, List<TideWarnSendInfo>>> typeIter = typeMap.entrySet().iterator();
        while (typeIter.hasNext()) {
            Map.Entry<String, List<TideWarnSendInfo>> typeMapEntity = typeIter.next();
            String type = typeMapEntity.getKey();
            List<TideWarnSendInfo> typrList = typeMapEntity.getValue();
            if (!typrList.isEmpty()) {
                String s =String.join("；", typrList.stream().map(e -> e.getAdNm() + "-" + e.getStName() + "潮位:" + e.getTdz() + "，警戒值为" + e.getOverData()).collect(Collectors.toList()));
                switch (type) {
                    case "12":
                        String deatilInfo1 = "";
                        if (sBuilder.length() ==0) {
                            deatilInfo1 = "当前潮位超过预警值显示为红色预警，测站为" + s ;
                        } else {
                            deatilInfo1 = "、当前潮位超过预警值显示为红色预警，测站为" + s ;
                        }
                        sBuilder.append(deatilInfo1);
                        break;
                    case "11":
                        String deatilInfo2 = "";
                        if (sBuilder.length() ==0) {
                            deatilInfo2 = "当前潮位超过预警值显示为橙色预警，测站为" + s ;
                        } else {
                            deatilInfo2 = "、当前潮位超过预警值显示为橙色预警，测站为" + s ;
                        }
                        sBuilder.append(deatilInfo2);
                        break;
                    case "10":
                        String deatilInfo3 = "";
                        if (sBuilder.length() ==0) {
                            deatilInfo3 = "当前潮位超过预警值显示为黄色预警，测站为" + s ;
                        } else {
                            deatilInfo3 = "、当前潮位超过预警值显示为黄色预警，测站为" + s ;
                        }
                        sBuilder.append(deatilInfo3);
                        break;
                    case "9":
                        String deatilInfo4 = "";
                        if (sBuilder.length() ==0) {
                            deatilInfo4 = "当前潮位超过预警值显示为蓝色预警，测站为" + s ;
                        } else {
                            deatilInfo4 = "、当前潮位超过预警值显示为蓝色预警，测站为" + s ;
                        }
                        sBuilder.append(deatilInfo4);
                        break;
                }
            }
        }
        return sBuilder.toString();
    }

    /**
     * 查找超过警戒潮位测站list
     * @param attStBases
     * @param stTideRList
     * @param overData
     * @param type 9:潮位警报-当前潮位超过（）米显示为蓝色预警10:当前潮位超过（）米显示为黄色预警
     *             11:潮位警报-当前潮位超过（）米显示为橙色预警 12:潮位警报-当前潮位超过（）米显示为红色预警
     * @return
     */
    private void exceedTideLevel(List<AttStBase> attStBases, List<StTideR> stTideRList,
                                            Double overData,String type,List<AttStBase> attStBasesNew,
                                            List<TideWarnSendInfo> tideWarnSendInfos) {
        if (!attStBases.isEmpty()) {
            TideWarnSendInfo tideWarnSendInfo = null;
            for (AttStBase attStBase : attStBases) {
                List<StTideR> collect = stTideRList.stream().filter(e -> e.getMgstcd().equals(attStBase.getStCode())).collect(Collectors.toList());
                if (!collect.isEmpty()) {
                    StTideR stTideR = collect.get(0);
                    if (stTideR.getTdz() > overData) {
                        attStBase.setTdz(stTideR.getTdz());
                        if (attStBasesNew.stream().filter(k->k.getStCode().equals(attStBase.getStCode())).count() == 0) {
                            attStBasesNew.add(attStBase);
                        }
                        List<TideWarnSendInfo> tideWarnSendInfoList = tideWarnSendInfos.stream().filter(l -> l.getStcd().equals(attStBase.getStCode())).collect(Collectors.toList());
                        if (tideWarnSendInfoList.isEmpty()) {
                            tideWarnSendInfo = new TideWarnSendInfo();
                            tideWarnSendInfo.setAdcd(attStBase.getAreaCode());
                            String adNm = adcdMap.get(attStBase.getAreaCode().substring(0, 6));
                            tideWarnSendInfo.setAdNm(StringUtils.isEmpty(adNm) ? "" : adNm);
                            tideWarnSendInfo.setOverData(overData);
                            tideWarnSendInfo.setTdz(stTideR.getTdz());
                            tideWarnSendInfo.setType(type);
                            tideWarnSendInfo.setStName(attStBase.getStName());
                            tideWarnSendInfo.setStcd(attStBase.getStCode());
                            tideWarnSendInfos.add(tideWarnSendInfo);
                        } else {
                            TideWarnSendInfo tideWarnSendInfo1 = tideWarnSendInfoList.get(0);
                            if (Integer.parseInt(tideWarnSendInfo1.getType()) < Integer.parseInt(type)) {
                                tideWarnSendInfo1.setType(type);
                                tideWarnSendInfo1.setOverData(overData);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 根据测站列表查找发送单位
     * @param attStBases
     * @return
     */
    public String getSendUnit(List<AttStBase> attStBases) {
        String sendUnit = "各县（市、区）水利局";
        Set<String> areaCodes = attStBases.stream().map(e -> e.getAreaCode()).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(areaCodes)) {
            if (areaCodes.size() == 1) {
                for (String areaCode : areaCodes) {
                    String strAdcd = adcdMap.get(areaCode.substring(0, 6));
                    return StringUtils.isEmpty(strAdcd) ? sendUnit : strAdcd;
                }
            } else if (areaCodes.size() > 1) {
                return sendUnit;
            }
        }
        return sendUnit;
    }

    /**
     * 获取超雨量详细信息
     * @param attStBases
     * @return
     */
    public String getSendInfoDeatilInfo(List<AttStBase> attStBases,List<com.ygkj.gragh.model.StPptnR> stPptnRList) {
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < attStBases.size(); i++) {
            AttStBase attStBase = attStBases.get(i);
            List<com.ygkj.gragh.model.StPptnR> stPptnRS = stPptnRList.stream().filter(e -> e.getMgstcd().equals(attStBase.getStCode())).collect(Collectors.toList());
            if (!stPptnRS.isEmpty()) {
                if (i == 0) {
                    sBuilder.append(attStBase.getStName() + stPptnRS.get(0).getDrp() +"mm");
                } else if (i == 1){
                    sBuilder.append("、" + attStBase.getStName() + stPptnRS.get(0).getDrp() +"mm等;");
                } else {
                    break;
                }
            }
        }
        return sBuilder.toString();
    }

    /**
     * 发送信息or上传pdf传真至七牛
     * @param sendType
     * @param configurationRespVo
     * @param sendInfoDeatilInfo 发送详细信息
     * @param attStBases 测站集合
     * @param currentDate 当前请求时间
     * @param time 详细时间
     * @param smsType 短信发送类型
     * @throws Exception
     */
    public void sendMessage(String sendType,WaterRuleConfigurationRespVo configurationRespVo,
                            String sendInfoDeatilInfo,List<AttStBase> attStBases, Date currentDate,String time,String smsType) throws Exception {
        WaterWarnInformationReqVo waterWarnInformation = new WaterWarnInformationReqVo();
        if (!StringUtils.isEmpty(sendType) && sendType.contains("1")) {//短信发送
            String peopleIds = configurationRespVo.getPeopleIds();
            CommonUtil.sendSms(peopleIds, sendInfoDeatilInfo,smsType,warnUserMapper,noticeClient);
        }
        if (!StringUtils.isEmpty(sendType) && sendType.contains("2")) {//传真发送
            String filePath = CommonUtil.uploadPdfToqiNiu(getSendUnit(attStBases),configurationRespVo.getLsPerson(),
                    DateUtils.format(configurationRespVo.getLsTime() == null ? currentDate : configurationRespVo.getLsTime(),"yyyy年MM月dd日HH时"),
                    DateUtils.format(currentDate,"yyyy"),time, sendInfoDeatilInfo,configurationRespVo.getType());
            waterWarnInformation.setPdfUrl(filePath);
        }
        waterWarnInformation.setType(configurationRespVo.getType());
        waterWarnInformation.setRuleContent(configurationRespVo.getRuleContent());
        waterWarnInformation.setWarnContent(sendInfoDeatilInfo);//预警内容
        waterWarnInformation.setSendStatus(1);
        waterWarnInformation.setSendPersonIds(configurationRespVo.getPeopleIds());
        waterWarnInformation.setSendType(sendType);
        waterWarnInformation.setRuleContentId(configurationRespVo.getId());
        waterWarnInformation.setPeopleNames(configurationRespVo.getPeopleNames());
        waterWarnInformation.setSendPersonIds(configurationRespVo.getPeopleIds());
        waterWarnInformation.setLsPerson(configurationRespVo.getLsPerson());
        waterWarnInformation.setLsTime(configurationRespVo.getLsTime());
        waterWarnInformation.setStationIds(configurationRespVo.getStationIds());
        waterWarnInformationService.insert(waterWarnInformation);
    }

}
