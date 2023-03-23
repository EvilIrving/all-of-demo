package com.ygkj.water.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.model.AttSeawallBase;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.SeawallData;
import com.ygkj.project.model.SectionStorm;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.GiveAnAlarmMapper;
import com.ygkj.water.project.mapper.SectionStormMapper;
import com.ygkj.water.project.mapper.StationMapper;
import com.ygkj.water.project.service.SeawallService;
import com.ygkj.water.project.utils.DESUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2021/10/27 10:34
 * @Description: 信息总览
 */
@Api(tags = "信息总览")
@RestController
@RequestMapping("/overview")
public class OverviewController {

    @Autowired
    private SeawallService seawallService;
    @Autowired
    private StationMapper stationMapper;
    @Autowired
    private GiveAnAlarmMapper giveAnAlarmMapper;
    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private SectionStormMapper sectionStormMapper;

    @Value("${access.key}")
    private String key;

    @Value("${access.secret}")
    private String secret;

    @ApiOperation(value = "信息总览剖面图信息数据")
    @PostMapping("/profileMap")
    private CommonResult profileMap(String seawallCode,String dmId){

        AttSeawallBase seawallBase = seawallService.getSeawallInfo(seawallCode);
        if (seawallBase != null){
            BigDecimal blueLevel = seawallBase.getTideStationBlueLevel();
            BigDecimal yellowLevel = seawallBase.getTideStationYellowLevel();
            BigDecimal orangeLevel = seawallBase.getTideStationOrangeLevel();
            BigDecimal redLevel = seawallBase.getTideStationRedLevel();
            //查询测站信息
            String stationCode = seawallBase.getTideStationCode();
            BigDecimal highestLevel = null;
            BigDecimal recordLow = null;
            BigDecimal tdz = null;
            BigDecimal waterLevel = null;
            List<AttStBase> attStBases = stationMapper.candidateStation(stationCode);
            if (attStBases != null && attStBases.size() > 0){
                AttStBase attStBase = attStBases.get(0);
                highestLevel = attStBase.getHighestLevel();
                recordLow = attStBase.getRecordLow();
                //查询天文潮预报数据
                HashSet<String> objects = new HashSet<>();
                objects.add(attStBase.getStCode());
                List<Map<String, Object>> ybTideLevels = giveAnAlarmMapper.getYbTideLevels(objects);
                if (ybTideLevels != null && ybTideLevels.size()>0){
                    String tdz1 = String.valueOf(ybTideLevels.get(0).get("tdz"));
                    if (tdz1 != null){
                        tdz = new BigDecimal(tdz1);
                    }
                }
                //查询当前最新一条水位数据
                String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
                List<Map<String, Object>> tideLevels = giveAnAlarmMapper.getTideLevels("st_tide_r_" + yyyyMM, objects);
                if (tideLevels != null && tideLevels.size()>0){
                    String waterLevel1 = String.valueOf(tideLevels.get(0).get("tdz"));
                    if (waterLevel1 != null){
                        waterLevel = new BigDecimal(waterLevel1);
                    }
                }
            }
            HashMap<String, Object> map = new HashMap<>();
            if (StringUtils.isNotBlank(dmId)){
                List<SectionStorm> sectionStorms = sectionStormMapper.select24HourData(dmId);
                if (CollectionUtils.isNotBlank(sectionStorms)){
                    map.put("24HourMax",sectionStorms.get(0).getH2());
                    SectionStorm sectionStorm = sectionStorms.stream().max(Comparator.comparing(SectionStorm::getH2).thenComparing(SectionStorm::getH2)).orElse(null);
                    map.put("H2",sectionStorm.getH2());
//                    valueList.stream().peek(MountainTorrentMonitorWarnValue::buildWarnGradeIdAsInt).filter(o -> o.getWarnGradeIdAsInt() != null).max(Comparator.comparing(MountainTorrentMonitorWarnValue::getWarnGradeIdAsInt).thenComparing(MountainTorrentMonitorWarnValue::getOverThresholdVal)).orElse(null);
                }
            }
            map.put("blueLevel",blueLevel);
            map.put("yellowLevel",yellowLevel);
            map.put("orangeLevel",orangeLevel);
            map.put("redLevel",redLevel);
            map.put("stationCode",stationCode);
            map.put("seawallName",seawallBase.getSeawallName());
            map.put("highestLevel",highestLevel);
            map.put("recordLow",recordLow);
            map.put("twcLevel",tdz);
            map.put("waterLevel",waterLevel);
            map.put("finalCrestAltitude",seawallBase.getFinalCrestAltitude());
            return CommonResult.success(map);
        } else {
            return CommonResult.failed("海塘编码不正确，没有查询到海塘的数据");
        }
    }

    @ApiOperation(value = "信息总览俯视图工程数据")
    @GetMapping("planView")
    private CommonResult planView(@ApiParam("海塘编码")String seawallCode, HttpServletRequest request) throws UnsupportedEncodingException {
        //从工程运管获取海塘附属工程建筑绑定关系
        //获取token
        String userId = JwtTokenUtil.getUserId(request.getHeader("authorization"));
        SysUser sysUser = userFeignClient.detailInfo(userId);
        if ("3".equals(sysUser.getUserType()) || "admin".equals(sysUser.getUsername())){
            sysUser.setPhone("13858860396");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", sysUser.getPhone());
        jsonObject.put("prcd", seawallCode);
        jsonObject.put("accesskey", key);
        jsonObject.put("accesssecret", secret);
        String data = JSON.toJSONString(jsonObject);
        String encrypt = DESUtil.encrypt(DESUtil.KEY, data);
        if (encrypt == null){
            return CommonResult.failed("获取工程运管token失败");
        }
        String url = "https://sk.zjwater.com/mgt/bm/prcdaccprcd/listJson?outsideToken="+encrypt+"&fetchAll=true";
        String json = HttpClientUtil.httpGet(url);
        JSONObject object = JSON.parseObject(json);
        boolean o = (boolean) object.get("success");
        if (o){
            List<JSONObject> rows = (List<JSONObject>) object.get("rows");
            if (rows != null && rows.size() > 0){
                HashMap<String, List<JSONObject>> map = new HashMap<>();
                Map<String, List<JSONObject>> projectCategory = rows.stream().collect(Collectors.groupingBy(m -> m.getString("projectCategory")));
                //查询实时数据。
                projectCategory.forEach((k,v)->{
                    //查询水闸和泵站的最新实时数据
                    List<String> prcd = v.stream().map(m -> m.getString("accPrcd")).collect(Collectors.toList());
                    if ("水闸".equals(k)){
                        //查询绑定水闸的主测站
                        List<JSONObject> objects = giveAnAlarmMapper.getDdStation(prcd);
                        List<JSONObject> realDd = null;
                        if (objects != null && objects.size() >0){
                            List<String> stcd = objects.stream().map(m -> m.getString("stcd")).collect(Collectors.toList());
                            if (stcd.size()>0){
                                //查询测站最新的一条数据
                                String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
                                realDd = giveAnAlarmMapper.getDdStationReal(stcd,"st_was_r_"+yyyyMM);
                            }
                            for (JSONObject object1 : objects) {
                                String stcd1 = object1.getString("stcd");
                                object1.put("tm",null);
                                object1.put("upz",null);
                                object1.put("dwz",null);
                                object1.put("mxgtq",null);
                                object1.put("ovs",null);
                                if (realDd != null && realDd.size()>0){
                                    for (JSONObject object2 : realDd) {
                                        String mgstcd = object2.getString("MGSTCD");
                                        if (mgstcd.equals(stcd1)){
                                            object1.put("tm",object2.get("TM"));
                                            object1.put("upz",object2.get("UPZ"));
                                            object1.put("dwz",object2.get("DWZ"));
                                            object1.put("mxgtq",object2.get("MXGTQ"));
                                            object1.put("ovs",object2.get("OVS"));
                                        }
                                    }
                                }
                            }
                            map.put("sz", objects);
                        }
                    } else if ("泵站".equals(k)){
                        List<JSONObject> pust = giveAnAlarmMapper.getPust(prcd);
                        map.put("bz", pust);
                        //查询绑定泵站的主测站
                        //List<JSONObject> objects = giveAnAlarmMapper.getPtStation(prcd);
                    } else if ("倒吸虹".equals(k)){
                        map.put("dxh", v);
                    } else if ("输供水隧洞".equals(k)){
                        //输供水隧洞 就是 管涵
                        map.put("gh", v);
                    }
                });
                return CommonResult.success(map);
            }
        }
        return CommonResult.success(new HashMap<>());
    }


    @ApiOperation(value = "信息总览的运行监测")
    @GetMapping("operation")
    private CommonResult operation(@ApiParam("海塘编码")String seawallCode){
        HashMap<String, Object> map = new HashMap<>();
        //查询防汛值班（安排值班、未安排值班）、险情处置（无险情、已处置、未处置）、防汛预案（已上传、未上传）、防汛演练（已演练、未演练）
        int count = seawallService.isScheduling(seawallCode);
        //防汛值班
        if (count > 0){
            map.put("fxzb", "安排值班");
        } else {
            map.put("fxzb", "未安排值班");
        }
        //险情处置
        // TODO  不知道哪里查询，暂时没有数据来源
        map.put("xqcz", "无险情");
        //防汛预案
        // TODO 不知道哪里查询，暂时没有数据来源
        map.put("fxya", "已上传");
        //防汛演练
        // TODO 不知道哪里查询，暂时没有数据来源
        map.put("fxyl", "已演练");
        //维修养护
        // TODO 不知道哪里查询，暂时没有数据来源
        map.put("wxyh", "-");
        AttSeawallBase seawallBase = seawallService.getSeawallInfo(seawallCode);
        if (seawallBase != null){
            //安全鉴定
            map.put("aqjd", seawallBase.getSafetyConclusion() == null?"-":seawallBase.getSafetyConclusion());
            //除险加固
            map.put("cxjg", seawallBase.getReinforce()== null?"-":seawallBase.getReinforce());
        } else {
            map.put("aqjd", "-");
            map.put("cxjg", "-");
        }
        //站点绑定
        int bindSt = seawallService.isBindSt(seawallCode);
        if (bindSt > 0){
            map.put("zdbd", "已绑定");
        } else {
            map.put("zdbd", "未绑定");
        }
        SeawallData seawallData = seawallService.getSeawallGcyg(seawallCode);
        //安全责任人
        String havesafeuser = seawallData.getHavesafeuser();
        if ("有".equals(havesafeuser)){
            map.put("aqzrr", "已设置");
        }else {
            map.put("aqzrr", "未设置");
        }
        //划界权限
        String delimitation = seawallData.getDelimitation();
        if ("是".equals(delimitation)){
            map.put("hjqx", "已上传");
        }else {
            map.put("hjqx", "未上传");
        }
        return CommonResult.success(map);
    }


    @ApiOperation(value = "江心屿预测潮位数据")
    @PostMapping("/predictTideLevel")
    private List<SectionStorm> predictTideLevel(@RequestParam String dmId){

        List<SectionStorm> sectionStorms = sectionStormMapper.select3DaysData(dmId);
        return sectionStorms;
    }



}
