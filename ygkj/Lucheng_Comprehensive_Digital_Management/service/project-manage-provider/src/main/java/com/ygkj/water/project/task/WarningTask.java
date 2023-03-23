package com.ygkj.water.project.task;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.gragh.model.*;
import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.WarningWaterRainReqVo;
import com.ygkj.project.vo.response.AttSeawallBaseVo;
import com.ygkj.project.vo.response.RuntimeWarnResVo;
import com.ygkj.project.vo.response.WarningWaterRainResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.BigScreenClient;
import com.ygkj.water.project.feign.SendFeignClient;
import com.ygkj.water.project.mapper.*;
import com.ygkj.water.project.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2021/10/25 15:46
 * @Description: 预警记录生成
 */
//@SpringBootTest(classes = ProjectManageApplication.class)
//@RunWith(SpringRunner.class)
//@ActiveProfiles("dev")
@Component
@Slf4j
public class WarningTask {

    @Autowired
    private WarningRuleService warningRuleService;
    @Autowired
    private ProjectRelService projectRelService;
    @Autowired
    private WarningInfoService warningInfoService;
    @Autowired
    private BigScreenClient bigScreenClient;
    @Autowired
    private SendFeignClient sendFeignClient;
    @Autowired
    private WarningUserService warningUserService;
    @Resource
    private AttSixchargeBaseMapper attSixchargeBaseMapper;
    @Resource
    private WarningWaterRainMapper warningWaterRainMapper;
    @Resource
    private ProjectRelMapper projectRelMapper;
    @Resource
    private WarningRuleMapper warningRuleMapper;
    @Autowired
    private EmergencyPlansManagementService emergencyPlansManagementService;
    @Resource
    private WarningMsgMapper warningMsgMapper;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 根据预警规则配置里面的信息，生成对应数据到库中，并发送短信给对应预警责任人
     * 预警规则（1.安全鉴定超期，2.汛前检查超期，3.年度总结超期，4.巡查信息异常，
     * 5.工程隐患，6.海塘实时报警，7.海塘预报预警，8.时段雨量报警，9.河道水情报警，
     * 10.水库水情报警，11.潮位报警）
     * 分开方法写。主要是可以配置不同的时间间隔运行
     */

    @Scheduled(cron = "0 1 0 * * ?")
    @Async
    public void giveAnAlarmTask1() {
        log.info("开始安全鉴定预警任务");
        //获取是否有安全鉴定预警规则
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 1).eq("isopen", 1));
        if (warningRule != null) {
            List<RuntimeWarnResVo> runtimeWarnResVos = projectRelService.selectRuntimeWarn(null);
            List<RuntimeWarnResVo> collect = runtimeWarnResVos.stream().filter(m -> m.getSafety() != null && m.getSafety() > 0).collect(Collectors.toList());
            if (collect.size() > 0) {
                saveWarning(warningRule, collect, 1, "安全鉴定已超期:");
            }
        }
        log.info("结束安全鉴定预警任务");
    }

    @Scheduled(cron = "0 2 0 * * ?")
    @Async
    public void giveAnAlarmTask2() {
        log.info("开始汛前检查超期任务");
        //获取是否有汛前检查超期
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 2).eq("isopen", 1));
        if (warningRule != null) {
            List<RuntimeWarnResVo> runtimeWarnResVos = projectRelService.selectRuntimeWarn(null);
            List<RuntimeWarnResVo> collect = runtimeWarnResVos.stream().filter(m -> m.getFlood() != null && m.getFlood() > 0).collect(Collectors.toList());
            if (collect.size() > 0) {
                saveWarning(warningRule, collect, 2, "汛前检查超期:");
            }
        }
        log.info("结束汛前检查超期任务");
    }


    @Scheduled(cron = "0 3 0 * * ?")
    @Async
    public void giveAnAlarmTask3() {
        log.info("开始年度总结超期任务");
        //获取是否有年度总结超期
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 3).eq("isopen", 1));
        if (warningRule != null) {
            List<RuntimeWarnResVo> runtimeWarnResVos = projectRelService.selectRuntimeWarn(null);
            List<RuntimeWarnResVo> collect = runtimeWarnResVos.stream().filter(m -> m.getYearReport() != null && m.getYearReport() > 0).collect(Collectors.toList());
            if (collect.size() > 0) {
                saveWarning(warningRule, collect, 3, "年度总结超期:");
            }
        }
        log.info("结束年度总结超期任务");
    }


    @Scheduled(cron = "0 4 0 * * ?")
    @Async
    public void giveAnAlarmTask4() {
        log.info("开始巡查信息异常任务");
        //获取是否有巡查信息异常
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 4).eq("isopen", 1));
        if (warningRule != null) {
            List<RuntimeWarnResVo> runtimeWarnResVos = projectRelService.selectRuntimeWarn(null);
            List<RuntimeWarnResVo> collect = runtimeWarnResVos.stream().filter(m -> m.getPatrol() != null && m.getPatrol() > 0).collect(Collectors.toList());
            if (collect.size() > 0) {
                saveWarning(warningRule, collect, 4, "巡查信息异常:");
            }
        }
        log.info("结束巡查信息异常任务");
    }


    @Scheduled(cron = "0 5 0 * * ?")
    @Async
    public void giveAnAlarmTask5() {
        log.info("开始工程隐患任务");
        //获取是否有工程隐患
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 5).eq("isopen", 1));
        if (warningRule != null) {
            List<RuntimeWarnResVo> runtimeWarnResVos = projectRelService.selectRuntimeWarn(null);
            List<RuntimeWarnResVo> collect = runtimeWarnResVos.stream().filter(m -> m.getDanger() != null && m.getDanger() > 0).collect(Collectors.toList());
            if (collect.size() > 0) {
                saveWarning(warningRule, collect, 5, "存在工程隐患:");
            }
        }
        log.info("结束工程隐患任务");
    }

    @Scheduled(cron = "0 0 0/2 * * ?")
    @Async
    public void giveAnAlarmTask6() {
        log.info("开始海塘实时报警任务");
        //海塘实时报警
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 6).eq("isopen", 1));
        if (warningRule != null) {
            CommonResult seawallWarning = bigScreenClient.seawallGiveAnAlarm(null, null);
            HashMap<String, Object> data = (HashMap) seawallWarning.getData();
            List<AttSeawallBaseVo> baseVo = JSON.parseArray(JSON.toJSONString(data.get("list")), AttSeawallBaseVo.class);
            if (baseVo != null && baseVo.size() > 0) {
                String bindcode = warningRule.getBindcode();
                String[] split = bindcode.split(",");
                List<String> stringList = Arrays.asList(split);
                String htzrn = warningRule.getHtzrn();
                List<AttSeawallBaseVo> collect = baseVo.stream().filter(m -> m.getStatus() != 1 && stringList.contains(m.getSeawallCode())).collect(Collectors.toList());
                if (collect.size() > 0) {
                    for (AttSeawallBaseVo attSeawallBaseVo : collect) {
                        Double tdz = attSeawallBaseVo.getTdz();
                        String tm = attSeawallBaseVo.getTm();
                        String seawallName = attSeawallBaseVo.getSeawallName();
                        String name = seawallName.replace("海塘", "");
                        String context = "【鹿城区水利局】" + name + "海塘，" + tm.substring(0, 16) + "实时潮位" + tdz + "米，请水利工程、旱闸和码头相关责任人做好防御措施，严禁行人进入亲水平台。";
                        //责任人发短信
                        if (StringUtils.isNotBlank(htzrn)) {
                            String[] ph = htzrn.split(",");
                            for (String s : ph) {
                                String[] phone = getPhone(s, attSeawallBaseVo.getSeawallCode());
                                if (phone != null && phone.length == 2) {
                                    //发短信
                                    sendMsg(phone, context);
                                }
                            }
                        }
                        //其他人员发短信
                        WarningInfo warningInfo = warningInfo(warningRule, context, 6);
                        //保存数据 一个海塘一条数据
                        warningInfoService.save(warningInfo);
                    }
                }
            }
        }
        log.info("结束海塘实时报警任务");
    }


    @Scheduled(cron = "0 0 0/2 * * ?")
    @Async
    public void giveAnAlarmTask7() {
        log.info("开始海塘预报预警任务");
        //海塘预报预警
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 7).eq("isopen", 1));
        if (warningRule != null) {
            CommonResult seawallWarning = bigScreenClient.seawallWarning(null, null);
            HashMap<String, Object> data = (HashMap) seawallWarning.getData();
            List<AttSeawallBaseVo> baseVo = JSON.parseArray(JSON.toJSONString(data.get("list")), AttSeawallBaseVo.class);
            if (baseVo != null && baseVo.size() > 0) {
                List<AttSeawallBaseVo> collect = baseVo.stream().filter(m -> m.getStatus() != 1).collect(Collectors.toList());
                if (collect.size() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (AttSeawallBaseVo attSeawallBaseVo : collect) {
                        Double tdz = attSeawallBaseVo.getTdz();
                        String tm = attSeawallBaseVo.getTm();
                        String seawallName = attSeawallBaseVo.getSeawallName();
                        stringBuffer.append(seawallName).append("预报时间").append(tm).append("预报潮位").append(tdz).append("、");
                    }
                    WarningInfo warningInfo = getWarningInfo(warningRule, stringBuffer, 7, "海塘预报预警:");
                    //保存数据
                    warningInfoService.save(warningInfo);

                }
            }
        }
        log.info("结束海塘预报预警任务");
    }

/**
    @Scheduled(cron = "0 0 0/2 * * ?")
    @Async
    public void giveAnAlarmTask8() {
        log.info("开始时段雨量报警任务");
        //时段雨量报警
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 8).eq("isopen", 1));
        if (warningRule != null) {
            CommonResult commonResult = bigScreenClient.rainGiveAnAlarm2(warningRule);
            HashMap<String, Object> data = (HashMap) commonResult.getData();
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");
            if (list != null && list.size() > 0) {
                List<Map<String, Object>> collect = list.stream().filter(m -> "true".equals(String.valueOf(m.get("status")))).collect(Collectors.toList());
                if (collect.size() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Map<String, Object> map : collect) {
                        String stName = String.valueOf(map.get("stName"));
                        String drp = String.valueOf(map.get("drp"));
                        String tm = String.valueOf(map.get("tm"));

                        stringBuffer.append(stName).append(warningRule.getRainhour()).append("小时累计降雨量超过").append(warningRule.getCumulativerain()).append("mm").append(",监测时间,").append(tm).append(",降雨量").append(drp).append("、");

                    }
                    WarningInfo warningInfo = getWarningInfo(warningRule, stringBuffer, 8, "时段雨量报警:");
                    //保存数据
                    warningInfoService.save(warningInfo);
                }
            }
        }
        log.info("结束时段雨量报警任务");
    }
**/
    @Scheduled(cron = "0 0 0/2 * * ?")
    @Async
    public void giveAnAlarmTask8() {
        log.info("开始时段雨量报警任务");
        //时段雨量报警
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 8).eq("isopen", 1));
        if (warningRule != null) {
            CommonResult commonResult = bigScreenClient.rainGiveAnAlarm3(warningRule);
            HashMap<String, Object> data = (HashMap) commonResult.getData();
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");
            Double aveRainfall = (Double) data.get("aveRainfall");
            if (list != null && list.size() > 0) {
                List<Map<String, Object>> collect = list.stream().filter(m -> "true".equals(String.valueOf(m.get("status")))).collect(Collectors.toList());
                if (collect.size() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    Calendar calendar = DateUtils.getCalendar();
                    calendar.setTime(new Date());
                    calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-1);
                    stringBuffer.append(calendar.get(Calendar.MONTH)+1).append("月").append(calendar.get(Calendar.DAY_OF_MONTH)).append("日").append(calendar.get(Calendar.HOUR_OF_DAY)).append("时至");
                    calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1);
                    stringBuffer.append(calendar.get(Calendar.MONTH)+1).append("月").append(calendar.get(Calendar.DAY_OF_MONTH)).append("日").append(calendar.get(Calendar.HOUR_OF_DAY)).append("时，");
                    stringBuffer.append("全区平均雨量").append(aveRainfall).append("mm,").append(warningRule.getRainhour()).append("小时雨量超过").append(warningRule.getCumulativerain()).append("mm的站点为");
                    for (Map<String, Object> map : collect) {
                        String stName = String.valueOf(map.get("stName"));
                        String drp = String.valueOf(map.get("drp"));

                        stringBuffer.append(stName).append("站").append(drp).append("mm,");

                    }
                    stringBuffer.append("注意加强防范强降水引发的洪涝和小流域山洪泥石流灾害。");
                    WarningInfo warningInfo = getWarningInfo(warningRule, stringBuffer, 8, "时段雨量报警:");
                    //保存数据
                    warningInfoService.save(warningInfo);
                }
            }
        }
        log.info("结束时段雨量报警任务");
    }

    private WarningInfo getWarningInfo(WarningRule warningRule, StringBuffer stringBuffer, int i, String s) {
        WarningInfo warningInfo = new WarningInfo();
        String warningId = IdWorker.get32UUID();
        warningInfo.setId(warningId);
        warningInfo.setStatus(2);
        warningInfo.setWarningrule(i);
        warningInfo.setWarningtime(new Date());
        warningInfo.setSendcode(warningRule.getSendcode());
        warningInfo.setSendcount(warningRule.getSendcount());
        warningInfo.setRuletype(warningRule.getRuletype());
        warningInfo.setWarningcontent(s + stringBuffer.toString().substring(0, stringBuffer.length() - 1));

        //查询所有人
        String sendcode = warningRule.getSendcode();
        String[] split = sendcode.split(",");
        if (split.length > 0) {
            List<WarningUser> warningUsers = warningUserService.listByIds(Arrays.asList(split));
            for (WarningUser warningUser : warningUsers) {
                String userPhone = warningUser.getPhone();
                warningMsgMapper.insert(Builder.of(WarningMsg::new)
                        .with(WarningMsg::setId,IdWorker.get32UUID())
                        .with(WarningMsg::setWarningId,warningId)
                        .with(WarningMsg::setPhone,warningUser.getPhone())
                        .build());
                MsgSendRecord msgSendRecord = new MsgSendRecord();
                msgSendRecord.setReceivePhone(userPhone);
                msgSendRecord.setReceiveName(warningUser.getName());
                msgSendRecord.setSendType(1);
                msgSendRecord.setBusinessType(4);
                //todo  发送短信 需要配置模板
                if (i == 8){
                    String warningcontent = warningInfo.getWarningcontent().substring(7);
                    msgSendRecord.setContext("【鹿城水文】时间"+warningcontent+"。");
                }else {
                    msgSendRecord.setContext("【鹿城区水利局】时间"+ LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"，"+warningInfo.getWarningcontent()+"。");
                }
                sendFeignClient.send(msgSendRecord);
                log.info("给" + userPhone + "号码发送短信了");
            }
        }

        return warningInfo;
    }


    private WarningInfo warningInfo(WarningRule warningRule, String context, int i) {
        WarningInfo warningInfo = new WarningInfo();
        String warningId = IdWorker.get32UUID();
        warningInfo.setId(warningId);
        warningInfo.setStatus(2);
        warningInfo.setWarningrule(i);
        warningInfo.setWarningtime(new Date());
        warningInfo.setSendcode(warningRule.getSendcode());
        warningInfo.setSendcount(warningRule.getSendcount());
        warningInfo.setRuletype(warningRule.getRuletype());
        warningInfo.setWarningcontent(context);
        //查询所有人
        String sendcode = warningRule.getSendcode();
        String[] split = sendcode.split(",");
        if (split.length > 0) {
            List<WarningUser> warningUsers = warningUserService.listByIds(Arrays.asList(split));
            for (WarningUser warningUser : warningUsers) {
                warningMsgMapper.insert(Builder.of(WarningMsg::new)
                        .with(WarningMsg::setId,IdWorker.get32UUID())
                        .with(WarningMsg::setWarningId,warningId)
                        .with(WarningMsg::setPhone,warningUser.getPhone())
                        .build());
                String userPhone = warningUser.getPhone();
                MsgSendRecord msgSendRecord = new MsgSendRecord();
                msgSendRecord.setReceivePhone(userPhone);
                msgSendRecord.setReceiveName(warningUser.getName());
                msgSendRecord.setSendType(1);
                msgSendRecord.setBusinessType(4);
                msgSendRecord.setContext(warningInfo.getWarningcontent());
                sendFeignClient.send(msgSendRecord);
                log.info("给" + userPhone + "号码发送短信了");
            }
        }

        return warningInfo;
    }

    @Scheduled(cron = "0 0 0/2 * * ?")
    @Async
    public void giveAnAlarmTask9() {
        log.info("开始河道水情报警任务");
        //河道水情报警
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 9).eq("isopen", 1));
        if (warningRule != null) {
            CommonResult commonResult = bigScreenClient.waterGiveAnAlarm2(warningRule);
            HashMap<String, Object> data = (HashMap) commonResult.getData();
            List<AttStBase> baseVo = JSON.parseArray(JSON.toJSONString(data.get("list")), AttStBase.class);
            if (baseVo != null && baseVo.size() > 0) {
                List<AttStBase> collect = baseVo.stream().filter(m -> "ZZ".equals(m.getStType()) && m.isWarning()).collect(Collectors.toList());
                if (collect.size() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (AttStBase attStBase : collect) {
                        Double warningLevel = attStBase.getWarningLevel();
                        String stName = attStBase.getStName();
                        String time = DateUtils.format(attStBase.getTime(),"yyyy-MM-dd HH:mm:ss");

                        stringBuffer.append(stName).append(",监测时间,").append(time).append(",监测水位").append(warningLevel).append("、");
                    }
                    WarningInfo warningInfo = getWarningInfo(warningRule, stringBuffer, 9, "河道水情报警:");
                    //保存数据
                    warningInfoService.save(warningInfo);
                }
            }
        }
        log.info("结束河道水情报警任务");
    }


    @Scheduled(cron = "0 0 0/2 * * ?")
    @Async
    public void giveAnAlarmTask10() {
        log.info("开始水库水情报警任务");
        //水库水情报警
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 10).eq("isopen", 1));
        if (warningRule != null) {
            CommonResult commonResult = bigScreenClient.waterGiveAnAlarm2(warningRule);
            HashMap<String, Object> data = (HashMap) commonResult.getData();
            List<AttStBase> baseVo = JSON.parseArray(JSON.toJSONString(data.get("list")), AttStBase.class);
            if (baseVo != null && baseVo.size() > 0) {
                List<AttStBase> collect = baseVo.stream().filter(m -> "RR".equals(m.getStType()) && m.isWarning()).collect(Collectors.toList());
                if (collect.size() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (AttStBase attStBase : collect) {
                        Double warningLevel = attStBase.getWarningLevel();
                        String stName = attStBase.getStName();
                        String time = DateUtils.format(attStBase.getTime(),"yyyy-MM-dd HH:mm:ss");
                        stringBuffer.append(stName).append(",监测时间,").append(time).append(",监测水位").append(warningLevel).append("、");
                    }
                    WarningInfo warningInfo = getWarningInfo(warningRule, stringBuffer, 10, "水库水情报警:");
                    //保存数据
                    warningInfoService.save(warningInfo);
                }
            }
        }
        log.info("结束水库水情报警任务");
    }


    @Scheduled(cron = "0 0 0/2 * * ?")
    @Async
    public void giveAnAlarmTask11() {
        log.info("开始潮位报警任务");
        //潮位报警
        WarningRule warningRule = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", 11).eq("isopen", 1));
        if (warningRule != null) {
            CommonResult commonResult = bigScreenClient.waterGiveAnAlarm2(warningRule);
            HashMap<String, Object> data = (HashMap) commonResult.getData();
            List<AttStBase> baseVo = JSON.parseArray(JSON.toJSONString(data.get("list")), AttStBase.class);
            if (baseVo != null && baseVo.size() > 0) {
                List<AttStBase> collect = baseVo.stream().filter(m -> "TT".equals(m.getStType()) && m.isWarning()).collect(Collectors.toList());
                if (collect.size() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (AttStBase attStBase : collect) {
                        Double tdz = attStBase.getTdz();
                        String stName = attStBase.getStName();
                        String time = DateUtils.format(attStBase.getTime(),"yyyy-MM-dd HH:mm:ss");
                        stringBuffer.append(stName).append(",监测时间,").append(time).append(",监测潮位").append(tdz).append("、");
                    }
                    WarningInfo warningInfo = getWarningInfo(warningRule, stringBuffer, 11, "潮位报警:");
                    //保存数据
                    warningInfoService.save(warningInfo);
                }
            }
        }
        log.info("结束潮位报警任务");
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void stRelWarnTask() {
        log.info("开始测站关联报警");
        //查询所有开启的测站关联报警
        List<WarningRule> list = warningRuleService.list(new LambdaQueryWrapper<WarningRule>()
                .eq(WarningRule::getWarningrule, 12).eq(WarningRule::getIsopen, 1));
        if (!CollectionUtils.isEmpty(list)) {
            WarningWaterRainReqVo reqVo = initWarningWaterRainReqVo();
            for (WarningRule warningRule : list) {
                List<WarningWaterRainResVo> valueList = new ArrayList<>();
                Map<String, String> stNameMap = listValue(reqVo, valueList, warningRule);
                if (!CollectionUtils.isEmpty(valueList)) {
                    //差值限额
                    Double maximum = warningRule.getMaximum();
                    //两两比对
                    out:
                    for (int i = 0; i < valueList.size() - 1; i++) {
                        for (int j = i + 1; j < valueList.size(); j++) {
                            WarningWaterRainResVo iVo = valueList.get(i);
                            WarningWaterRainResVo jVo = valueList.get(j);
                            Double d = iVo.getValue() - jVo.getValue();
                            Double dif = Math.abs(d);
                            //任意两个的差值大于限额就报警
                            if (dif > maximum) {
                                StringBuffer sb = new StringBuffer();
                                sb.append("【鹿城区水利局】")
                                        .append("站点：").append(stNameMap.get(iVo.getStCode())).append(",时间：").append(sdf.format(iVo.getTm())).append(",测值：").append(iVo.getValue())
                                        .append(";")
                                        .append("站点：").append(stNameMap.get(jVo.getStCode())).append(",时间：").append(sdf.format(jVo.getTm())).append(",测值：").append(jVo.getValue())
                                        .append("。两者差值超过限额。。");
                                WarningInfo warningInfo = getWarningInfo(warningRule, sb, 12, "测站关联报警:");
                                //保存数据
                                warningInfoService.save(warningInfo);
                                break out;
                            }
                        }
                    }
                }

            }
        }
        log.info("结束测站关联报警");
    }

    private Map<String, String> listValue(WarningWaterRainReqVo reqVo, List<WarningWaterRainResVo> valueList, WarningRule warningRule) {
        Integer warnStType = warningRule.getStType();
        //查询关联规则所有的测站
        List<AttStBase> stList = warningWaterRainMapper.listSt(warningRule.getBindcode());
        //测站按类型分类
        Map<String, List<String>> stTypeMap = stList.stream()
                .collect(Collectors.groupingBy(AttStBase::getStType,
                        Collectors.mapping(AttStBase::getStCode, Collectors.toList())));
        Map<String, String> stNameMap = stList.stream().collect(Collectors.toMap(AttStBase::getStCode, AttStBase::getStName));
        //遍历所有类型的测站
        Iterator<Map.Entry<String, List<String>>> it = stTypeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            //获取测站类型
            String stType = entry.getKey();
            reqVo.setStList(entry.getValue());
            //根据类型查询最近一小时最新的数据
            switch (stType) {
                case "ZZ":
                    zz(reqVo, valueList);
                    break;
                case "RR":
                    rr(reqVo, valueList);
                    break;
                case "TT":
                    tt(reqVo, valueList);
                    break;
                case "DD":
                    dd(reqVo, valueList, warnStType);
                    break;
                case "PP":
                    pp(reqVo, valueList);
                    break;
            }
        }
        return stNameMap;
    }


    private WarningWaterRainReqVo initWarningWaterRainReqVo() {
        LocalDate now = LocalDate.now();
        String yyyyMM = dtf.format(now);
        Date date = new Date();
        date.setTime(date.getTime() - 60 * 60 * 1000);
        WarningWaterRainReqVo reqVo = new WarningWaterRainReqVo();
        reqVo.setDate(date);
        reqVo.setYm(yyyyMM);
        return reqVo;
    }

    private void pp(WarningWaterRainReqVo reqVo, List<WarningWaterRainResVo> valueList) {
        List<StPptnR> list = warningWaterRainMapper.listPp(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            for (StPptnR stPptnR : list) {
                WarningWaterRainResVo warningWaterRainResVo = new WarningWaterRainResVo();
                warningWaterRainResVo.setTm(stPptnR.getTm());
                warningWaterRainResVo.setStCode(stPptnR.getStcd());
                warningWaterRainResVo.setValue(stPptnR.getDrp());
                if (warningWaterRainResVo.getValue() != null) {
                    valueList.add(warningWaterRainResVo);
                }
            }
        }
    }

    private void dd(WarningWaterRainReqVo reqVo, List<WarningWaterRainResVo> valueList, Integer warnStType) {
        List<StWasR> list = warningWaterRainMapper.listDd(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            for (StWasR stWasR : list) {
                WarningWaterRainResVo warningWaterRainResVo = new WarningWaterRainResVo();
                warningWaterRainResVo.setTm(stWasR.getTm());
                warningWaterRainResVo.setStCode(stWasR.getStcd());
                if (warnStType == 1) {
                    warningWaterRainResVo.setValue(stWasR.getUpz());
                } else {
                    warningWaterRainResVo.setValue(Double.valueOf(stWasR.getLl()));
                }
                if (warningWaterRainResVo.getValue() != null) {
                    valueList.add(warningWaterRainResVo);
                }
            }
        }
    }

    private void tt(WarningWaterRainReqVo reqVo, List<WarningWaterRainResVo> valueList) {
        List<StTideR> list = warningWaterRainMapper.listTt(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            for (StTideR stTideR : list) {
                WarningWaterRainResVo warningWaterRainResVo = new WarningWaterRainResVo();
                warningWaterRainResVo.setTm(stTideR.getTm());
                warningWaterRainResVo.setStCode(stTideR.getStcd());
                warningWaterRainResVo.setValue(stTideR.getTdz());
                if (warningWaterRainResVo.getValue() != null) {
                    valueList.add(warningWaterRainResVo);
                }
            }
        }
    }

    private void rr(WarningWaterRainReqVo reqVo, List<WarningWaterRainResVo> valueList) {
        List<StRsvrR> list = warningWaterRainMapper.listRr(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            for (StRsvrR stRsvrR : list) {
                WarningWaterRainResVo warningWaterRainResVo = new WarningWaterRainResVo();
                warningWaterRainResVo.setTm(stRsvrR.getTm());
                warningWaterRainResVo.setStCode(stRsvrR.getStcd());
                warningWaterRainResVo.setValue(stRsvrR.getRz());
                if (warningWaterRainResVo.getValue() != null) {
                    valueList.add(warningWaterRainResVo);
                }
            }
        }
    }

    private void zz(WarningWaterRainReqVo reqVo, List<WarningWaterRainResVo> valueList) {
        List<StRiverR> list = warningWaterRainMapper.listZz(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            for (StRiverR stRiverR : list) {
                WarningWaterRainResVo warningWaterRainResVo = new WarningWaterRainResVo();
                warningWaterRainResVo.setTm(stRiverR.getTm());
                warningWaterRainResVo.setStCode(stRiverR.getStcd());
                warningWaterRainResVo.setValue(stRiverR.getZ());
                if (warningWaterRainResVo.getValue() != null) {
                    valueList.add(warningWaterRainResVo);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void stLimitWarnTask() {
        log.info("开始测站限值报警");
        //查询所有开启的测站关联报警
        List<WarningRule> list = warningRuleService.list(new LambdaQueryWrapper<WarningRule>()
                .eq(WarningRule::getWarningrule, 13).eq(WarningRule::getIsopen, 1));
        if (!CollectionUtils.isEmpty(list)) {
            WarningWaterRainReqVo reqVo = initWarningWaterRainReqVo();
            //遍历每个规则
            for (WarningRule warningRule : list) {
                //获取测站最新测值
                List<WarningWaterRainResVo> valueList = new ArrayList<>();
                Map<String, String> stNameMap = listValue(reqVo, valueList, warningRule);
                if (!CollectionUtils.isEmpty(valueList)) {
                    for (WarningWaterRainResVo warningWaterRainResVo : valueList) {
                        Double value = warningWaterRainResVo.getValue();
                        //超过上下限报警
                        if (value > warningRule.getMaximum() || value < warningRule.getMinimum()) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("【鹿城区水利局】")
                                    .append("站点：").append(stNameMap.get(warningWaterRainResVo.getStCode())).append(",时间：")
                                    .append(sdf.format(warningWaterRainResVo.getTm())).append(",测值：").append(warningWaterRainResVo.getValue())
                                    .append("。超过限额。。");
                            WarningInfo warningInfo = getWarningInfo(warningRule, sb, 13, "测站限值报警:");
                            //保存数据
                            warningInfoService.save(warningInfo);
                        }
                    }
                }
            }
        }
        log.info("结束测站限值报警");
    }

    @Scheduled(cron = "0 0 8 * * ?")
    @Async
    @Test
    public void concernedCheck() {
        log.info("开始汛前检查提醒");
        //查询所有开启的测站关联报警
        List<WarningRule> list = warningRuleService.list(new LambdaQueryWrapper<WarningRule>()
                .eq(WarningRule::getWarningrule, 18).eq(WarningRule::getIsopen, 1));
        if (!CollectionUtils.isEmpty(list)) {
            //遍历每个规则 当年 3月15号 到 第二年 3月14号 期间进行提醒，提醒的是（当年未上传汛前检查文件的工程）
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            int monthValue = now.getMonthValue();
            int day = now.getDayOfMonth();
            if (monthValue < 3  || (monthValue == 3 && day < 15)){
                year = year - 1;
            }
            for (WarningRule warningRule : list) {
                String bindcode = warningRule.getBindcode();
                String id = warningRule.getId();
                Integer frequency = warningRule.getFrequency();
                //查询上次提醒的时间根据频次计算这次是否提醒
                boolean tx = isTx(id, frequency);
                if (!tx){
                    continue;
                }
                List<String> codeList = Arrays.asList(bindcode.split(","));
                List<JSONObject> projList = projectRelMapper.getProjList(codeList);
                List<JSONObject> jsonObjects = projectRelMapper.listYearBhFc(codeList,year);
                StringBuffer sb = new StringBuffer();
                for (JSONObject jsonObject : projList) {
                    String name = jsonObject.getString("project_name");
                    String projectCode = jsonObject.getString("project_code");
                    if (jsonObjects != null && jsonObjects.size() > 0){
                        long count = jsonObjects.stream().filter(jsonObject1 -> jsonObject1.getString("prcd").equals(projectCode)).count();
                        if (count == 0){
                            sb.append(name).append("、");
                        }
                    } else {
                        sb.append(name).append("、");
                    }
                }
                if (sb.length() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer append = stringBuffer.append(year).append("未上传汛前检查文件:").append(sb);
                    WarningInfo warningInfo = getWarningInfo(warningRule, append, 18, "汛前检查提醒:");
                    //保存数据
                    warningInfoService.save(warningInfo);
                    //更新或者新增这一次提醒的时间对应的规则数据
                    ReminderTimeRecord record = warningRuleMapper.getById(id);
                    if (record == null){
                        warningRuleMapper.saveReminderTimeRecord(id,new Date());
                    } else {
                        warningRuleMapper.updateReminderTimeRecord(id,new Date());
                    }
                }
            }
        }
        log.info("结束汛前检查提醒");
    }

    @Scheduled(cron = "0 0 8 * * ?")
    @Async
    public void yearCheck() {
        log.info("开始年度检查提醒");
        //查询所有开启的测站关联报警
        List<WarningRule> list = warningRuleService.list(new LambdaQueryWrapper<WarningRule>()
                .eq(WarningRule::getWarningrule, 17).eq(WarningRule::getIsopen, 1));
        if (!CollectionUtils.isEmpty(list)) {
            //遍历每个规则 当年 12月15号 到 第二年 3月14号 期间进行提醒，提醒的是（当年未上传年度检查文件的工程）
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            LocalDate date = LocalDate.of(year, 3, 14);
            LocalDate date2 = LocalDate.of(year, 12, 15);
            int monthValue = now.getMonthValue();
            if (now.compareTo(date) > 0 && now.compareTo(date2) < 0){
                //3.14 < x <12.15 的范围不做提醒
                return;
            }
            if (monthValue <= 3){
                year = year - 1;
            }
            for (WarningRule warningRule : list) {
                String bindcode = warningRule.getBindcode();
                String id = warningRule.getId();
                Integer frequency = warningRule.getFrequency();
                //查询上次提醒的时间根据频次计算这次是否提醒
                boolean tx = isTx(id, frequency);
                if (!tx){
                    continue;
                }
                List<String> codeList = Arrays.asList(bindcode.split(","));
                List<JSONObject> projList = projectRelMapper.getProjList(codeList);
                List<JSONObject> jsonObjects = projectRelMapper.listYearCheck(codeList,year);
                StringBuffer sb = new StringBuffer();
                for (JSONObject jsonObject : projList) {
                    String name = jsonObject.getString("project_name");
                    String projectCode = jsonObject.getString("project_code");
                    if (jsonObjects != null && jsonObjects.size() > 0){
                        long count = jsonObjects.stream().filter(jsonObject1 -> jsonObject1.getString("prcd").equals(projectCode)).count();
                        if (count == 0){
                            sb.append(name).append("、");
                        }
                    } else {
                        sb.append(name).append("、");
                    }
                }
                if (sb.length() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer append = stringBuffer.append(year).append("未上传年度检查文件:").append(sb);
                    WarningInfo warningInfo = getWarningInfo(warningRule, append, 17, "年度检查提醒:");
                    //保存数据
                    warningInfoService.save(warningInfo);
                    //更新或者新增这一次提醒的时间对应的规则数据
                    ReminderTimeRecord record = warningRuleMapper.getById(id);
                    if (record == null){
                        warningRuleMapper.saveReminderTimeRecord(id,new Date());
                    } else {
                        warningRuleMapper.updateReminderTimeRecord(id,new Date());
                    }
                }
            }
        }
        log.info("结束年度检查提醒");
    }

    @Scheduled(cron = "0 0 8 * * ?")
    @Async
    public void afterCheck() {
        log.info("开始汛后检查提醒");
        List<WarningRule> list = warningRuleService.list(new LambdaQueryWrapper<WarningRule>()
                .eq(WarningRule::getWarningrule, 16).eq(WarningRule::getIsopen, 1));
        if (!CollectionUtils.isEmpty(list)) {
            //遍历每个规则 当年 10月15号 到 第二年 3月14号 期间进行提醒，提醒的是（当年未上传年度检查文件的工程）
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            LocalDate date = LocalDate.of(year, 3, 14);
            LocalDate date2 = LocalDate.of(year, 10, 15);
            int monthValue = now.getMonthValue();
            if (now.compareTo(date) > 0 && now.compareTo(date2) < 0){
                //3.14 < x <10.15 的范围不做提醒
                return;
            }
            if (monthValue <= 3){
                year = year - 1;
            }
            for (WarningRule warningRule : list) {
                String bindcode = warningRule.getBindcode();
                String id = warningRule.getId();
                Integer frequency = warningRule.getFrequency();
                //查询上次提醒的时间根据频次计算这次是否提醒
                boolean tx = isTx(id, frequency);
                if (!tx){
                    continue;
                }
                List<String> codeList = Arrays.asList(bindcode.split(","));
                List<JSONObject> projList = projectRelMapper.getProjList(codeList);
                List<JSONObject> jsonObjects = projectRelMapper.listAfterCheck(codeList,year);
                StringBuffer sb = new StringBuffer();
                for (JSONObject jsonObject : projList) {
                    String name = jsonObject.getString("project_name");
                    String projectCode = jsonObject.getString("project_code");
                    if (jsonObjects != null && jsonObjects.size() > 0){
                        long count = jsonObjects.stream().filter(jsonObject1 -> jsonObject1.getString("prcd").equals(projectCode)).count();
                        if (count == 0){
                            sb.append(name).append("、");
                        }
                    } else {
                        sb.append(name).append("、");
                    }
                }
                if (sb.length() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer append = stringBuffer.append(year).append("未上传汛后检查文件:").append(sb);
                    WarningInfo warningInfo = getWarningInfo(warningRule, append, 16, "汛后检查提醒:");
                    //保存数据
                    warningInfoService.save(warningInfo);
                    //更新或者新增这一次提醒的时间对应的规则数据
                    ReminderTimeRecord record = warningRuleMapper.getById(id);
                    if (record == null){
                        warningRuleMapper.saveReminderTimeRecord(id,new Date());
                    } else {
                        warningRuleMapper.updateReminderTimeRecord(id,new Date());
                    }
                }
            }
        }
        log.info("结束汛后检查提醒");
    }

    @Scheduled(cron = "0 0 15-23 * * ? ")
    @Async
    @Test
    public void dailyCheck() {
        log.info("日常检查提醒");
        List<WarningRule> list = warningRuleService.list(new LambdaQueryWrapper<WarningRule>()
                .eq(WarningRule::getWarningrule, 15).eq(WarningRule::getIsopen, 1));
        if (!CollectionUtils.isEmpty(list)){
            for (WarningRule warningRule : list) {
                String bindcode = warningRule.getBindcode();
                String id = warningRule.getId();
                Integer frequency = warningRule.getFrequency();
                //查询上次提醒的时间根据频次计算这次是否提醒
                boolean tx = isTx(id, frequency);
                if (!tx){
                    continue;
                }
                List<String> codeList = Arrays.asList(bindcode.split(","));
                List<JSONObject> projList = projectRelMapper.getProjList(codeList);
                List<JSONObject> jsonObjects = projectRelMapper.listDailyCheck(codeList,DateUtils.format(new Date(), "yyyy-MM-dd HH"));
                StringBuffer sb = new StringBuffer();
                for (JSONObject jsonObject : projList) {
                    String name = jsonObject.getString("project_name");
                    String projectCode = jsonObject.getString("project_code");
                    if (jsonObjects != null && jsonObjects.size() > 0){
                        long count = jsonObjects.stream().filter(jsonObject1 -> jsonObject1.getString("prcd").equals(projectCode)).count();
                        if (count == 0){
                            sb.append(name).append("、");
                        }
                    } else {
                        sb.append(name).append("、");
                    }
                }
                if (sb.length() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer append = stringBuffer.append("今日未进行日常检查:").append(sb);
                    WarningInfo warningInfo = getWarningInfo(warningRule, append, 15, "日常检查提醒:");
                    //保存数据
                    warningInfoService.save(warningInfo);
                    //更新或者新增这一次提醒的时间对应的规则数据
                    ReminderTimeRecord record = warningRuleMapper.getById(id);
                    if (record == null){
                        warningRuleMapper.saveReminderTimeRecord(id,new Date());
                    } else {
                        warningRuleMapper.updateReminderTimeRecord(id,new Date());
                    }
                }
            }
        }
        log.info("结束日常检查提醒");
    }

    @Scheduled(cron = "0 0 8 * * ?")
    @Async
    public void planManagement() {
        log.info("预案资料提醒");
        List<WarningRule> list = warningRuleService.list(new LambdaQueryWrapper<WarningRule>()
                .eq(WarningRule::getWarningrule, 14).eq(WarningRule::getIsopen, 1));
        if (!CollectionUtils.isEmpty(list)){
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            int monthValue = now.getMonthValue();
            int day = now.getDayOfMonth();
            if (monthValue < 4  || (monthValue == 4 && day < 15)){
                year = year - 1;
            }
            for (WarningRule warningRule : list) {
                String bindcode = warningRule.getBindcode();
                String id = warningRule.getId();
                Integer frequency = warningRule.getFrequency();
                //查询上次提醒的时间根据频次计算这次是否提醒
                boolean tx = isTx(id, frequency);
                if (!tx){
                    continue;
                }
                List<String> codeList = Arrays.asList(bindcode.split(","));
                List<JSONObject> projList = projectRelMapper.getProjList(codeList);
                List<JSONObject> jsonObjects = emergencyPlansManagementService.planManagement(codeList,year);
                StringBuffer sb = new StringBuffer();
                for (JSONObject jsonObject : projList) {
                    String name = jsonObject.getString("project_name");
                    String projectCode = jsonObject.getString("project_code");
                    if (jsonObjects != null && jsonObjects.size() > 0){
                        List<JSONObject> collect = jsonObjects.stream().filter(jsonObject1 -> jsonObject1.getString("prcd").equals(projectCode)).collect(Collectors.toList());
                        if (collect.size() == 0){
                            sb.append(name).append("未上传应急预案、控运计划、调度方案、管理手册、放水预警方案").append("、");
                        } else {
                            JSONObject jsonObject1 = collect.get(0);
                            String types = jsonObject1.getString("types");
                            String[] split = types.split(",");
                            List<String> strings = Arrays.asList(split);
                            if (split.length < 5){
                                sb.append(name).append("未上传");
                                //1.应急预案2.控运计划3.调度方案4.管理手册5.放水预警方案
                                if (!strings.contains("1")){
                                    sb.append("应急预案、");
                                }
                                if (!strings.contains("2")){
                                    sb.append("控运计划、");
                                }
                                if (!strings.contains("3")){
                                    sb.append("调度方案、");
                                }
                                if (!strings.contains("4")){
                                    sb.append("管理手册、");
                                }
                                if (!strings.contains("5")){
                                    sb.append("放水预警方案、");
                                }
                            }
                        }
                    } else {
                        sb.append(name).append("未上传应急预案、控运计划、调度方案、管理手册、放水预警方案").append("、");
                    }
                }
                if (sb.length() > 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer append = stringBuffer.append(year).append("未上传预案资料:").append(sb);
                    WarningInfo warningInfo = getWarningInfo(warningRule, append, 14, "预案资料提醒:");
                    //保存数据
                    warningInfoService.save(warningInfo);
                    //更新或者新增这一次提醒的时间对应的规则数据
                    ReminderTimeRecord record = warningRuleMapper.getById(id);
                    if (record == null){
                        warningRuleMapper.saveReminderTimeRecord(id,new Date());
                    } else {
                        warningRuleMapper.updateReminderTimeRecord(id,new Date());
                    }
                }
            }
        }
        log.info("结束预案资料提醒");
    }

    /**
     * 是否需要进行提醒
     * @param id
     * @param frequency
     * @return
     */
    private boolean isTx(String id,Integer frequency) {
        //提醒频率（1.1天/次、2.1周/次、3.半月/次、4.1月/次、5.1小时/次、6.2小时/次、7.3小时/次）
        ReminderTimeRecord record = warningRuleMapper.getById(id);
        if (record != null){
            Date tm = record.getTm();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tm);
            switch (frequency){
                case 1:
                    calendar.add(Calendar.DATE, 1);
                    break;
                case 2:
                    calendar.add(Calendar.DATE, 7);
                    break;
                case 3:
                    calendar.add(Calendar.DATE, 15);
                    break;
                case 4:
                    calendar.add(Calendar.DATE, 30);
                    break;
                case 5:
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                    break;
                case 6:
                    calendar.add(Calendar.HOUR_OF_DAY, 2);
                    break;
                case 7:
                    calendar.add(Calendar.HOUR_OF_DAY, 3);
                    break;
            }
            Date time = calendar.getTime();
            if (frequency < 5){
                String format = DateUtils.format(time, "yyyy-MM-dd");
                String date = DateUtils.format(new Date(), "yyyy-MM-dd");
                if (format.compareTo(date) <= 0){
                    return true;
                } else {
                    return false;
                }
            } else {
                String format = DateUtils.format(time, "yyyy-MM-dd HH:00:00");
                String date = DateUtils.format(new Date(), "yyyy-MM-dd HH:00:00");
                if (format.compareTo(date) <= 0){
                    return true;
                }else {
                    return false;
                }
            }
        } else {
            return true;
        }
    }


    private void saveWarning(WarningRule warningRule, List<RuntimeWarnResVo> collect, int rule, String content) {
        WarningInfo warningInfo = new WarningInfo();
        String warningId = IdWorker.get32UUID();
        warningInfo.setId(warningId);
        warningInfo.setStatus(2);
        warningInfo.setWarningrule(rule);
        warningInfo.setWarningtime(new Date());
        warningInfo.setSendcode(warningRule.getSendcode());
        warningInfo.setRuletype(warningRule.getRuletype());
        warningInfo.setSendcount(warningRule.getSendcount());
        StringBuffer stringBuffer = new StringBuffer();
        for (RuntimeWarnResVo runtimeWarnResVo : collect) {
            String seawallName = runtimeWarnResVo.getSeawallName();
            stringBuffer.append(seawallName).append("、");
        }
        String substring = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
        warningInfo.setWarningcontent(content + substring);

        //查询所有人
        String sendcode = warningRule.getSendcode();
        String[] split = sendcode.split(",");
        if (split.length > 0) {
            List<WarningUser> warningUsers = warningUserService.listByIds(Arrays.asList(split));
            for (WarningUser warningUser : warningUsers) {
                String userPhone = warningUser.getPhone();
                warningMsgMapper.insert(Builder.of(WarningMsg::new)
                        .with(WarningMsg::setId,IdWorker.get32UUID())
                        .with(WarningMsg::setWarningId,warningId)
                        .with(WarningMsg::setPhone,warningUser.getPhone())
                        .build());
                MsgSendRecord msgSendRecord = new MsgSendRecord();
                msgSendRecord.setReceivePhone(userPhone);
                msgSendRecord.setReceiveName(warningUser.getName());
                msgSendRecord.setSendType(1);
                msgSendRecord.setBusinessType(4);
                //todo  发送短信 需要配置模板
                msgSendRecord.setContext("【鹿城区水利局】时间"+ LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+"，"+warningInfo.getWarningcontent()+"。");
                sendFeignClient.send(msgSendRecord);
                log.info("给" + userPhone + "号码发送短信了");
            }
        }
        //保存数据
        warningInfoService.save(warningInfo);
    }

    private String[] getPhone(String htzrn, String code) {
        List<AttSixchargeBase> list = attSixchargeBaseMapper.selectList(new QueryWrapper<AttSixchargeBase>()
                .eq("project_code", code).eq("del_flag", 0).orderByDesc("id"));
        if (list != null && list.size() > 0) {
            AttSixchargeBase attSixchargeBase = list.get(0);
            String[] strings = new String[2];
            switch (htzrn) {
                case "1":
                    strings[0] = attSixchargeBase.getGovermentChargeName();
                    strings[1] = attSixchargeBase.getGovermentChargePhone();
                    break;
                case "2":
                    strings[0] = attSixchargeBase.getWaterDepartmentChargeName();
                    strings[1] = attSixchargeBase.getWaterDepartmentChargePhone();
                    break;
                case "3":
                    strings[0] = attSixchargeBase.getCompetentDepartmentChargeName();
                    strings[1] = attSixchargeBase.getCompetentDepartmentChargePhone();
                    break;
                case "4":
                    strings[0] = attSixchargeBase.getManageDepartmentChargeName();
                    strings[1] = attSixchargeBase.getManageDepartmentChargePhone();
                    break;
                case "5":
                    strings[0] = attSixchargeBase.getTechnologyChargeName();
                    strings[1] = attSixchargeBase.getTechnologyChargePhone();
                    break;
                case "6":
                    strings[0] = attSixchargeBase.getPatrolChargeName();
                    strings[1] = attSixchargeBase.getPatrolChargePhone();
                    break;
                default:
                    return null;
            }
            return strings;
        }
        return null;
    }

    private void sendMsg(String[] phone, String context) {
        MsgSendRecord msgSendRecord = new MsgSendRecord();
        msgSendRecord.setReceivePhone(phone[1]);
        msgSendRecord.setReceiveName(phone[0]);
        msgSendRecord.setSendType(1);
        msgSendRecord.setBusinessType(4);
        msgSendRecord.setContext(context);
        sendFeignClient.send(msgSendRecord);
    }
}
