package com.ygkj.big.screen.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.afterturn.easypoi.word.entity.MyXWPFDocument;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.ygkj.big.screen.controller.FloodDroughtDefenseController;
import com.ygkj.big.screen.enums.StationTypes;
import com.ygkj.big.screen.feign.AuthenticationClient;
import com.ygkj.big.screen.feign.GraphClient;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.mapper.GiveAnAlarmMapper;
import com.ygkj.big.screen.model.AttAdcdBase;
import com.ygkj.big.screen.model.AttSeawallBase;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.service.GiveAnAlarmService;
import com.ygkj.big.screen.service.HealthCodeService;
import com.ygkj.big.screen.service.ResponseLevelService;
import com.ygkj.big.screen.utils.PoiUtil;
import com.ygkj.big.screen.utils.QRCodeUtil;
import com.ygkj.big.screen.vo.request.ProjectManageQueryVo;
import com.ygkj.big.screen.vo.request.QrCodeReqVo;
import com.ygkj.big.screen.vo.request.SeawallAmbitusVo;
import com.ygkj.big.screen.vo.request.SynWaterRequestVo;
import com.ygkj.big.screen.vo.response.AttSeawallBaseVo;
import com.ygkj.big.screen.vo.response.SeawallWarnResVo;
import com.ygkj.entity.ByteArrayMultipartFile;
import com.ygkj.enums.StationType;
import com.ygkj.enums.WaterStationType;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.rpc.GraphRpcService;
import com.ygkj.gragh.vo.response.AqjcExportResVo;
import com.ygkj.project.model.WarningRule;
import com.ygkj.project.vo.request.BmCtroChcuVo;
import com.ygkj.project.vo.request.BmCtroChwlIndexVo;
import com.ygkj.project.vo.response.AttResBaseVo;
import com.ygkj.project.vo.response.RuntimeWarnResVo;
import com.ygkj.project.vo.response.SeawallPointResVo;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.DistanceUtil;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.utils.HydrologyUtils;
import com.ygkj.visualization.model.RealTyphoonData;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ygkj.com.assistant.utils.UUIDUtils;
import ygkj.com.util.DateUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2021/10/18 19:27
 * @Description:
 */
@Service("giveAnAlarmService")
@Slf4j
public class GiveAnAlarmServiceImpl implements GiveAnAlarmService {

    @Autowired
    private GiveAnAlarmMapper giveAnAlarmMapper;
    @Autowired
    private HealthCodeService healthCodeService;
    @Reference
    private GraphRpcService graphRpcService;
    @Autowired
    private ProjectManageClient projectManageClient;
    @Autowired
    private GraphClient graphClient;
    @Autowired
    private FloodDroughtDefenseController floodDroughtDefenseController;
    @Autowired
    private AuthenticationClient authenticationClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ResponseLevelService responseLevelService;

    @Value("${gcjc.aqjcList}")
    private String aqjcList;
    @Value("${gcjc.aqjcData}")
    private String aqjcData;
    @Value("${gcjc.token}")
    private String token;
    @Value("${qrcode.mobile.seawall}")
    private String seawallMobileUrl;

    //背景色
    private static final Color background = new Color(8, 38, 69);

    private static final Color gray = new Color(153, 153, 153);
    private static final Color green = new Color(103, 194, 58);
    private static final Color yellow = new Color(230, 162, 60);
    private static final Color red = new Color(245, 108, 108);

    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
    //计算两地经纬度使用
    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public CommonResult seawallWarning(String seawallCode, String time, Boolean warning) {

        if (ObjectUtils.isEmpty(warning)) {
            warning = false;
        }

        List<AttSeawallBase> list = giveAnAlarmMapper.getseawall(seawallCode);
        HashSet<String> set = new HashSet<>();
        for (AttSeawallBase attSeawallBase : list) {
            String tideStationCode = attSeawallBase.getTideStationCode();
            set.add(tideStationCode);
        }
        List<Map<String, Object>> ybTideLevels = null;
        List<JSONObject> ybTides = null;
        if (!set.isEmpty()) {
            //查询潮位站预报下一天0点的数据
//            ybTideLevels = giveAnAlarmMapper.getYbTideLevels(set, time);
            ybTides = giveAnAlarmMapper.getSectionStorm(time);
        }
//        HashMap<Object, Object> map = getHashMap(list, ybTideLevels);
        HashMap<Object, Object> map = getStormHashMap(list, ybTides);
        if (warning) {
            List<AttSeawallBaseVo> resList = (ArrayList<AttSeawallBaseVo>) map.get("list");
            resList = resList.stream().filter(r -> r.getStatus() != 1).collect(Collectors.toList());
            map.put("list", resList);
        }
        return CommonResult.success(map);
    }

    @Override
    public CommonResult seawallGiveAnAlarm(String seawallCode, String time, Boolean warning) {
        return seawallGiveAnAlarm(seawallCode, time, null, warning);
    }

    @Override
    public CommonResult seawallGiveAnAlarm(String seawallCode, String time, String fcstId, Boolean warning) {

        if (ObjectUtils.isEmpty(warning)) {
            warning = false;
        }
        //获取所有海塘所有的测站。
        List<AttSeawallBase> list = giveAnAlarmMapper.getseawall(seawallCode);
        Set<String> set = new HashSet<>();
        for (AttSeawallBase attSeawallBase : list) {
            String tideStationCode = attSeawallBase.getTideStationCode();
            set.add(tideStationCode);
        }
        List<Map<String, Object>> tideLevels = null;
        if (!set.isEmpty()) {
            //查询潮位站最新一条数据的数据（默认近一天）
            String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            if (StringUtils.isNotEmpty(fcstId)) {
                tideLevels = giveAnAlarmMapper.listFcstVal(fcstId, time, set);
            } else {
                tideLevels = giveAnAlarmMapper.getTideLevels(StationTypes.TIDE.table() + yyyyMM, set, time);
            }
        }
        HashMap<Object, Object> map = getHashMap(list, tideLevels);
        if (warning) {
            List<AttSeawallBaseVo> resList = (ArrayList<AttSeawallBaseVo>) map.get("list");
            resList = resList.stream().filter(r -> r.getStatus() != 1).collect(Collectors.toList());
            map.put("list", resList);
        }
        return CommonResult.success(map);
    }


    private HashMap<Object, Object> getStormHashMap(List<AttSeawallBase> list, List<JSONObject> tideLevels) {
        int zc = 0, blue = 0, yellow = 0, orange = 0, red = 0;
        Double zcLen = 0.00, blueLen = 0.00, yellowLen = 0.00, orangeLen = 0.00, redLen = 0.00;
        ArrayList<AttSeawallBaseVo> arrayList = new ArrayList<>();
        for (AttSeawallBase attSeawallBase : list) {
            AttSeawallBaseVo attSeawallBaseVo = new AttSeawallBaseVo();
            BeanUtils.copyProperties(attSeawallBase, attSeawallBaseVo);
            if (tideLevels != null && tideLevels.size() > 0) {
                JSONObject map = tideLevels.get(0);
                Double tdz = Double.valueOf(map.getString("tdz"));
                String tm = map.getString("tm");
                attSeawallBaseVo.setTm(tm);
                attSeawallBaseVo.setTdz(tdz);
                Double blueLevel = attSeawallBaseVo.getTideStationBlueLevel();
                Double yellowLevel = attSeawallBaseVo.getTideStationYellowLevel();
                Double orangeLevel = attSeawallBaseVo.getTideStationOrangeLevel();
                Double redLevel = attSeawallBaseVo.getTideStationRedLevel();
                if (blueLevel != null && tdz > blueLevel && yellowLevel != null && tdz <= yellowLevel) {
                    attSeawallBaseVo.setStatus(2);
                } else if (yellowLevel != null && tdz > yellowLevel && orangeLevel != null && tdz <= orangeLevel) {
                    attSeawallBaseVo.setStatus(3);
                } else if (orangeLevel != null && tdz > orangeLevel && redLevel != null && tdz <= redLevel) {
                    attSeawallBaseVo.setStatus(4);
                } else if (redLevel != null && tdz > redLevel) {
                    attSeawallBaseVo.setStatus(5);
                }
            }
            Integer status = attSeawallBaseVo.getStatus();
            if (status == 1) {
                zc++;
                zcLen += attSeawallBaseVo.getSeawallLength();
            } else if (status == 2) {
                blue++;
                blueLen += attSeawallBaseVo.getSeawallLength();
            } else if (status == 3) {
                yellow++;
                yellowLen += attSeawallBaseVo.getSeawallLength();
            } else if (status == 4) {
                orange++;
                orangeLen += attSeawallBaseVo.getSeawallLength();
            } else {
                red++;
                redLen += attSeawallBaseVo.getSeawallLength();
            }
            arrayList.add(attSeawallBaseVo);
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("list", arrayList);
        map.put("zc", zc);
        map.put("zcLen", BigDecimal.valueOf(zcLen).divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP));
        map.put("blue", blue);
        map.put("blueLen", BigDecimal.valueOf(blueLen).divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP));
        map.put("yellow", yellow);
        map.put("yellowLen", BigDecimal.valueOf(yellowLen).divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP));
        map.put("orange", orange);
        map.put("orangeLen", BigDecimal.valueOf(orangeLen).divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP));
        map.put("red", red);
        map.put("redLen", BigDecimal.valueOf(redLen).divide(BigDecimal.valueOf(1000), 2, BigDecimal.ROUND_HALF_UP));
        map.put("bj", red + blue + yellow + orange);


        return map;
    }


    private HashMap<Object, Object> getHashMap(List<AttSeawallBase> list, List<Map<String, Object>> tideLevels) {
        int zc = 0, blue = 0, yellow = 0, orange = 0, red = 0;
        ArrayList<AttSeawallBaseVo> arrayList = new ArrayList<>();
        for (AttSeawallBase attSeawallBase : list) {
            AttSeawallBaseVo attSeawallBaseVo = new AttSeawallBaseVo();
            BeanUtils.copyProperties(attSeawallBase, attSeawallBaseVo);
            if (tideLevels != null && tideLevels.size() > 0) {
                List<Map<String, Object>> stcd = tideLevels.stream().filter(m -> String.valueOf(m.get("stcd")).equals(attSeawallBase.getTideStationCode())).collect(Collectors.toList());
                if (stcd != null && stcd.size() > 0) {
                    Map<String, Object> map = stcd.get(0);
                    Double tdz = Double.valueOf(String.valueOf(map.get("tdz")));
                    String tm = String.valueOf(map.get("tm"));
                    attSeawallBaseVo.setTm(tm);
                    attSeawallBaseVo.setTdz(tdz);
                    Double blueLevel = attSeawallBaseVo.getTideStationBlueLevel();
                    Double yellowLevel = attSeawallBaseVo.getTideStationYellowLevel();
                    Double orangeLevel = attSeawallBaseVo.getTideStationOrangeLevel();
                    Double redLevel = attSeawallBaseVo.getTideStationRedLevel();
                    if (blueLevel != null && tdz > blueLevel && yellowLevel != null && tdz <= yellowLevel) {
                        attSeawallBaseVo.setStatus(2);
                    } else if (yellowLevel != null && tdz > yellowLevel && orangeLevel != null && tdz <= orangeLevel) {
                        attSeawallBaseVo.setStatus(3);
                    } else if (orangeLevel != null && tdz > orangeLevel && redLevel != null && tdz <= redLevel) {
                        attSeawallBaseVo.setStatus(4);
                    } else if (redLevel != null && tdz > redLevel) {
                        attSeawallBaseVo.setStatus(5);
                    }
                }
            }
            Integer status = attSeawallBaseVo.getStatus();
            if (status == 1) {
                zc++;
            } else if (status == 2) {
                blue++;
            } else if (status == 3) {
                yellow++;
            } else if (status == 4) {
                orange++;
            } else {
                red++;
            }
            arrayList.add(attSeawallBaseVo);
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("list", arrayList);
        map.put("zc", zc);
        map.put("blue", blue);
        map.put("yellow", yellow);
        map.put("orange", orange);
        map.put("red", red);
        map.put("bj", red + blue + yellow + orange);


        return map;
    }

    @Override
    public CommonResult waterGiveAnAlarm() {
        //获取鹿城区的所有水库、河道、堰闸、潮位测站
        List<AttStBase> rainLevels = giveAnAlarmMapper.getwaterLevels();
        ArrayList<AttStBase> attStBases = new ArrayList<>();
        if (rainLevels != null && rainLevels.size() > 0) {
            Map<String, List<AttStBase>> type = rainLevels.stream().collect(Collectors.groupingBy(AttStBase::getStType));
            type.forEach((k, v) -> {
                String table;
                List<String> codes = v.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                switch (k) {
                    //闸坝水位站
                    case "DD":
                        table = StationType.GATE.table().concat(LocalDate.now().format(yyyyMM));
                        List<StWasR> stWasRS = graphRpcService.selectWasByTimeCode(table, codes, null);
                        if (stWasRS != null && stWasRS.size() > 0) {
                            for (StWasR stWasR : stWasRS) {
                                double upz = stWasR.getUpz() == null ? 0d : stWasR.getUpz().doubleValue();
                                double dwz = stWasR.getDwz() == null ? 0d : stWasR.getDwz().doubleValue();
                                String ovs = stWasR.getOvs() == null ? "0" : stWasR.getOvs();
                                String mxgtq = stWasR.getMxgtq() == null ? "0" : stWasR.getMxgtq();
                                Date tm = stWasR.getTm();
                                String ll = stWasR.getLl();
                                String mgstcd = stWasR.getMgstcd();
                                for (AttStBase attStBase : v) {
                                    if (attStBase.getStCode().equals(mgstcd)) {
                                        attStBase.setUpz(upz);
                                        attStBase.setDwz(dwz);
                                        attStBase.setOpenGate(ovs);
                                        attStBase.setFlow(mxgtq);
                                        attStBase.setTime(tm);
                                        attStBase.setLl(ll);
                                        // 水闸是闸上水位超警
                                        String status = StringUtils.EMPTY;
                                        if (attStBase.getWarningLevel() != null
                                                && attStBase.getWarningLevel() > 0d
                                                && attStBase.getUpz() > attStBase.getWarningLevel()) {
                                            attStBase.setOverWarnLevel(attStBase.getUpz() - attStBase.getWarningLevel());
                                            attStBase.setWarning(true);
                                        }
                                        attStBase.setStatus(status);
                                    }
                                }
                            }
                        }
                        break;
                    //潮位站
                    case "TT":
                        table = StationType.TIDE.table().concat(LocalDate.now().format(yyyyMM));
                        List<StTideR> stTideRS = graphRpcService.selectTideByTimeCode(table, codes, null);
                        if (stTideRS != null && stTideRS.size() > 0) {
                            for (StTideR stTideR : stTideRS) {
                                double tdz = stTideR.getTdz().doubleValue();
                                String mgstcd = stTideR.getMgstcd();
                                Date tm = stTideR.getTm();
                                for (AttStBase attStBase : v) {
                                    if (attStBase.getStCode().equals(mgstcd)) {
                                        attStBase.setTdz(stTideR.getTdz().doubleValue());
                                        attStBase.setTime(stTideR.getTm());
                                        String status = StringUtils.EMPTY;
                                        if (attStBase.getWarningLevel() != null && attStBase.getWarningLevel() > 0d && attStBase.getTdz() > attStBase.getWarningLevel()) {
                                            attStBase.setOverWarnLevel(attStBase.getTdz() - attStBase.getWarningLevel());
                                            attStBase.setWarning(true);
                                        }
                                        attStBase.setStatus(status);
                                    }
                                }
                            }
                        }
                        break;
                    //水库水位站
                    case "RR":
                        table = StationType.RESERVOIR.table().concat(LocalDate.now().format(yyyyMM));
                        ;
                        List<StRsvrR> stRsvrRS = graphRpcService.selectRsvrWaterLevelByTimeCode(table, codes, null);
                        if (stRsvrRS != null && stRsvrRS.size() > 0) {
                            for (StRsvrR stRsvrR : stRsvrRS) {
                                double waterLevel = stRsvrR.getRz().doubleValue();
                                String mgstcd = stRsvrR.getMgstcd();
                                Date tm = stRsvrR.getTm();
                                for (AttStBase attStBase : v) {
                                    if (attStBase.getStCode().equals(mgstcd)) {
                                        attStBase.setWaterLevel(waterLevel);
                                        attStBase.setTime(tm);
                                        String status = "";
                                        if (null == attStBase.getLimitLevel() || 0d == attStBase.getLimitLevel()) {
                                            status = WaterStationType.NORMAL_RIVER.type();
                                        } else {
                                            if (waterLevel > attStBase.getLimitLevel()) {
                                                attStBase.setWarning(true);
                                                attStBase.setOverWarnLevel(attStBase.getWaterLevel() - attStBase.getLimitLevel());
                                                status = WaterStationType.SUPER_LIMIT.type();
                                            } else {
                                                status = WaterStationType.NORMAL_RIVER.type();
                                            }
                                        }
                                        attStBase.setStatus(status);
                                    }
                                }

                            }
                        }
                        break;
                    //河道水位站
                    case "ZZ":
                        table = StationType.RIVER.table().concat(LocalDate.now().format(yyyyMM));
                        ;
                        List<StRiverR> stRiverRS = graphRpcService.selectRiverByTimeCode(table, codes, null);
                        if (stRiverRS != null && stRiverRS.size() > 0) {
                            for (StRiverR stRiverR : stRiverRS) {
                                double waterLevel = stRiverR.getZ().doubleValue();
                                String mgstcd = stRiverR.getMgstcd();
                                Date tm = stRiverR.getTm();
                                for (AttStBase attStBase : v) {
                                    if (attStBase.getStCode().equals(mgstcd)) {
                                        attStBase.setWaterLevel(waterLevel);
                                        attStBase.setTime(tm);
                                        String status = "";
                                        if (null == attStBase.getLimitLevel() || 0d == attStBase.getLimitLevel()) {
                                            status = WaterStationType.NORMAL_RIVER.type();
                                        } else {
                                            if (waterLevel > attStBase.getLimitLevel()) {
                                                attStBase.setWarning(true);
                                                attStBase.setOverWarnLevel(attStBase.getWaterLevel() - attStBase.getLimitLevel());
                                                status = WaterStationType.SUPER_LIMIT.type();
                                            } else {
                                                status = WaterStationType.NORMAL_RIVER.type();
                                            }
                                        }
                                        attStBase.setStatus(status);
                                    }
                                }
                            }
                        }
                        break;
                }
                attStBases.addAll(v);
            });
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", attStBases);
        if (attStBases != null && attStBases.size() > 0) {
            long count = attStBases.stream().filter(m -> m.isWarning()).count();
            long tt = attStBases.stream().filter(m -> m.isWarning() && "TT".equals(m.getStType())).count();
            long dd = attStBases.stream().filter(m -> m.isWarning() && "DD".equals(m.getStType())).count();
            long zz = attStBases.stream().filter(m -> m.isWarning() && "ZZ".equals(m.getStType())).count();
            long rr = attStBases.stream().filter(m -> m.isWarning() && "RR".equals(m.getStType())).count();
            map.put("bj", count);
            map.put("ttCount", tt);
            map.put("ddCount", dd);
            map.put("zzCount", zz);
            map.put("rrCount", rr);

        }
        return CommonResult.success(map);
    }

    @Override
    public CommonResult rainGiveAnAlarm() {
        //获取鹿城的雨量站
        List<Map<String, Object>> rainLevels = giveAnAlarmMapper.getRainLevels();
        //查询每个测站1小时内最新的一条展示数据
        Set<String> set = new HashSet<>();
        if (rainLevels != null && rainLevels.size() > 0) {
            for (Map<String, Object> rainLevel : rainLevels) {
                String stcd = String.valueOf(rainLevel.get("stcd"));
                set.add(stcd);
            }
        }
        List<Map<String, Object>> rain = null;
        List<Map<String, Object>> hoursRain = null;
        if (!set.isEmpty()) {
            String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            rain = giveAnAlarmMapper.getRains(StationTypes.RAINFALL.table() + yyyyMM, set);
            //查询今天现在整点数据，1小时超30mm、3小时超60mm预警。
            String format = DateUtil.format(new Date(), "yyyy-MM-dd HH:00:00");
            hoursRain = giveAnAlarmMapper.getHoursRain(format, set);
        }
        int zc = 0, oneHour = 0, threeHours = 0, bj = 0;
        for (Map<String, Object> rainLevel : rainLevels) {
            String stcd = String.valueOf(rainLevel.get("stcd"));
            rainLevel.put("drp", null);
            rainLevel.put("tm", null);
            //status  0.正常  1.1小时超30mm  2.3小时超60mm
            rainLevel.put("status", 0);
            if (rain != null && rain.size() > 0) {
                for (Map<String, Object> map : rain) {
                    String stcd1 = String.valueOf(map.get("stcd"));
                    String drp = String.valueOf(map.get("drp"));
                    String tm = String.valueOf(map.get("tm"));
                    if (stcd1.equals(stcd)) {
                        rainLevel.put("drp", drp);
                        rainLevel.put("tm", tm);
                    }
                }
            }
            if (hoursRain != null && hoursRain.size() > 0) {
                for (Map<String, Object> map : hoursRain) {
                    String stcd2 = String.valueOf(map.get("stcd"));
                    double hour = Double.valueOf(String.valueOf(map.get("hour")));
                    double threehour = Double.valueOf(String.valueOf(map.get("threehour")));
                    if (threehour > 60) {
                        rainLevel.put("status", 2);
                    } else if (hour > 30) {
                        rainLevel.put("status", 1);
                    }
                }
            }
            String status = String.valueOf(rainLevel.get("status"));
            if ("0".equals(status)) {
                zc++;
            } else if ("1".equals(status)) {
                oneHour++;
            } else if ("2".equals(status)) {
                threeHours++;
            }
            if ("1".equals(status) || "2".equals(status)) {
                bj++;
            }
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("list", rainLevels);
        map.put("zc", zc);
        map.put("oneHour", oneHour);
        map.put("threeHours", threeHours);
        map.put("bj", bj);
        return CommonResult.success(map);
    }

    @Override
    public CommonResult typhoonGiveAnAlarm() {
        JSONArray jsonArray = fuckingCurrentTyphoon();
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", jsonArray);
        map.put("bj", jsonArray.size());
        return CommonResult.success(map);
    }

    @Override
    public CommonResult runGiveAnAlarm() {
        CommonResult commonResult = projectManageClient.selectRuntimeWarn("");
        List<RuntimeWarnResVo> data = JSON.parseArray(JSON.toJSONString(commonResult.getData()), RuntimeWarnResVo.class);
        int bj = 0;
        for (RuntimeWarnResVo datum : data) {
            //工程隐患 0无隐患 1有隐患
            Integer danger = datum.getDanger();
            //汛前检查 0未超期 1超期
            Integer flood = datum.getFlood();
            //巡查信息 0正常 1异常
            Integer patrol = datum.getPatrol();
            //安全鉴定 0未超期 1超期
            Integer safety = datum.getSafety();
            //年度检查 0未超期 1已超期
            Integer yearReport = datum.getYearReport();
            String context = null;
            StringBuffer stringBuffer = new StringBuffer();
            if (safety != null && safety > 0) {
                stringBuffer.append("、安全鉴定超期");
            }
            if (flood != null && flood > 0) {
                stringBuffer.append("、汛前检查超期");
            }
            if (yearReport != null && yearReport > 0) {
                stringBuffer.append("、年度检查超期");
            }
            if (patrol != null && patrol > 0) {
                stringBuffer.append("、巡查信息异常");
            }
            if (danger != null && danger > 0) {
                stringBuffer.append("、工程存在隐患");
            }
            if (stringBuffer != null && stringBuffer.length() > 0) {
                context = stringBuffer.toString().substring(1, stringBuffer.length());
                //context存在值说明存在报警，统计报警的数值
                bj++;
            }
            datum.setContext(context);
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("list", data);
        map.put("bj", bj);
        map.put("zc", data.size() - bj);
        return CommonResult.success(map);
    }

    @Override
    public CommonResult giveAnAlarmCount() {
        CommonResult seawallWarning = seawallWarning(null, null, null);
        CommonResult seawallGiveAnAlarm = seawallGiveAnAlarm(null, null, null);
        CommonResult waterGiveAnAlarm = waterGiveAnAlarm();
        CommonResult rainGiveAnAlarm = rainGiveAnAlarm();
        CommonResult typhoonGiveAnAlarm = typhoonGiveAnAlarm();
        CommonResult runGiveAnAlarm = runGiveAnAlarm();

        HashMap<String, Object> data1 = (HashMap) seawallWarning.getData();
        HashMap<String, Object> data2 = (HashMap) seawallGiveAnAlarm.getData();
        HashMap<String, Object> data3 = (HashMap) rainGiveAnAlarm.getData();
        HashMap<String, Object> data4 = (HashMap) typhoonGiveAnAlarm.getData();
        HashMap<String, Object> data5 = (HashMap) waterGiveAnAlarm.getData();
        HashMap<String, Object> data6 = (HashMap) runGiveAnAlarm.getData();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("seawallWarning", data1.get("bj"));
        hashMap.put("seawallGiveAnAlarm", data2.get("bj"));
        hashMap.put("rainGiveAnAlarm", data3.get("bj"));
        hashMap.put("typhoonGiveAnAlarm", data4.get("bj"));
        hashMap.put("waterGiveAnAlarm", data5.get("bj"));
        hashMap.put("runGiveAnAlarm", data6.get("bj"));
        return CommonResult.success(hashMap);
    }

    /**
     * 海塘预警
     *
     * @param seawallCode
     * @return
     */
    @Override
    public SeawallWarnResVo selectWarn(String seawallCode, String time, String fcstId) {
        SeawallWarnResVo seawallWarnResVo = new SeawallWarnResVo();
        //预警
        AttSeawallBaseVo earlyWarn = getWarn(seawallWarning(seawallCode, time, null));
        seawallWarnResVo.setEarlyWarn(earlyWarn.getStatus());

        //报警
        AttSeawallBaseVo warn = getWarn(seawallGiveAnAlarm(seawallCode, time, fcstId, null));
        seawallWarnResVo.setWarn(warn.getStatus());

        return seawallWarnResVo;
    }

    @Override
    public CommonResult typhoonInfluence(String date) throws ParseException {
        if (date == null) {
            JSONArray jsonArray = fuckingCurrentTyphoon();
            return CommonResult.success(jsonArray);
        }
        //获取该时间点存在的台风
        List<RealTyphoonData> typhoon = giveAnAlarmMapper.getTyphoonByDate(date);
        if (typhoon != null && typhoon.size() > 0) {
            for (RealTyphoonData realTyphoonData : typhoon) {
                String points = realTyphoonData.getPoints();
                JSONArray objects = JSON.parseArray(points);
                //距离鹿城
                JSONArray jsonArray = new JSONArray();
                for (Object point : objects) {
                    JSONObject pointObj = (JSONObject) point;
                    //如果是固定时间点的当时的数据，需要把这个时间点之后的（未来的）数据删除
                    String time = pointObj.getString("time");
                    String replace = time.replace("T", " ");//处理一下返回数据时间中多的一个T，
                    int compare = sdf.parse(date).compareTo(sdf.parse(replace));
                    if (compare < 0) {
                        continue;
                    } else {
                        double value = DistanceUtil.getDistance(28.03827120238947, 120.57645463189935, Double.parseDouble(pointObj.getString("lat")), Double.parseDouble(pointObj.getString("lng"))).doubleValue();
                        pointObj.put("distance", value);
                        JSONArray forecast = pointObj.getJSONArray("forecast");
                        if (null != forecast && !forecast.isEmpty()) {
                            for (Object object : forecast) {
                                JSONArray array = ((JSONObject) object).getJSONArray("points");
                                for (Object pointStr : array) {
                                    JSONObject pointJson = (JSONObject) pointStr;
                                    double doubleValue = DistanceUtil.getDistance(28.03827120238947, 120.57645463189935, Double.parseDouble(pointJson.getString("lat")), Double.parseDouble(pointJson.getString("lng"))).doubleValue();
                                    pointJson.put("distance", doubleValue);
                                }
                            }
                        }
                    }
                    jsonArray.add(point);
                }
                realTyphoonData.setPoints(jsonArray.toJSONString());
            }
        }
        return CommonResult.success(typhoon);
    }

    @Override
    public CommonResult seawallTyphoon(String date, String id) throws ParseException {
        //查询所有海塘
        List<AttSeawallBase> list = giveAnAlarmMapper.getseawall(null);
        //查询台风数据
        RealTyphoonData realTyphoonData = giveAnAlarmMapper.selectTyphoonById(id);
        String points = realTyphoonData.getPoints();
        JSONArray objects = JSON.parseArray(points);
        JSONObject pointObj;
        if (date == null) {
            pointObj = (JSONObject) objects.get(objects.size() - 1);
        } else {
            //对比传入时间，取time字段小于传入时间并且最接近的一条数据
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < objects.size(); i++) {
                JSONObject o = (JSONObject) objects.get(i);
                String time = o.getString("time");
                String replace = time.replace("T", " ");//处理一下返回数据时间中多的一个T，
                int compare = sdf.parse(date).compareTo(sdf.parse(replace));
                if (compare < 0) {
                    continue;
                }
                jsonArray.add(o);
            }
            pointObj = (JSONObject) jsonArray.get(jsonArray.size() - 1);
        }
        ArrayList<JSONObject> arrayList = new ArrayList<>();
        for (AttSeawallBase attSeawallBase : list) {
            JSONObject object = new JSONObject();
            object.put("seawallCode", attSeawallBase.getSeawallCode());
            object.put("seawallName", attSeawallBase.getSeawallName());
            //查询台风距离
            double value = DistanceUtil.getDistance(Double.valueOf(attSeawallBase.getLatitude()), Double.valueOf(attSeawallBase.getLongitude()), Double.parseDouble(pointObj.getString("lat")), Double.parseDouble(pointObj.getString("lng"))).doubleValue();
            object.put("distance", value);
            //对比10级风圈最大半径，小于则受台风影响
            JSONObject radius10_quad = pointObj.getJSONObject("radius10_quad");
            double se = radius10_quad.getDoubleValue("se");
            double sw = radius10_quad.getDoubleValue("sw");
            double ne = radius10_quad.getDoubleValue("ne");
            double nw = radius10_quad.getDoubleValue("nw");
            if (value > se && value > sw && value > ne && value > nw) {
                object.put("isInfluence", "否");
            } else {
                object.put("isInfluence", "是");
            }
            arrayList.add(object);
        }
        return CommonResult.success(arrayList);
    }

    @Override
    public CommonResult seawallAmbitus(SeawallAmbitusVo vo) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(vo.getType()) || StringUtils.isBlank(vo.getSeawallCode())) {
            return CommonResult.failed("缺少必传参数type,seawallCode");
        } else {
            if (!"5".equals(vo.getType())) {
                if (vo.getRadius() == null) {
                    return CommonResult.failed("type类型是(2,3,4)时缺少必传参数radius");
                }
            } else {
                if (StringUtils.isBlank(vo.getKeyWord())) {
                    return CommonResult.failed("type类型是5时缺少必传参数keyWord");
                }
            }
        }

        //查询海塘数据.

        List<AttSeawallBase> list = giveAnAlarmMapper.getseawall(vo.getSeawallCode());
        if (list == null) {
            return CommonResult.failed("海塘编码错误");
        }
        AttSeawallBase attSeawallBase = list.get(0);
        Double latitude = Double.valueOf(attSeawallBase.getLatitude());
        Double longitude = Double.valueOf(attSeawallBase.getLongitude());
        String type = vo.getType();
        Double radius = vo.getRadius();
        String keyWord = vo.getKeyWord();
        //1.保护范围，2.救援仓库,3救援队伍,4.避灾点，5.社会经济要素，查出所有数据然后筛选半径范围内的数据
        switch (type) {
            case "1":
                //todo 已取消这个类型
                return CommonResult.success();
            case "2":
                List<AttWarehouseBase> attWarehouseBase = giveAnAlarmMapper.getAttWarehouseBase();
                if (attWarehouseBase != null && attWarehouseBase.size() > 0) {
                    attWarehouseBase = attWarehouseBase.stream().filter(m ->
                            DistanceUtil.getDistance(Double.valueOf(m.getLatitude()), Double.valueOf(m.getLongitude()),
                                    latitude, longitude).intValue() < radius).collect(Collectors.toList());
                }
                return CommonResult.success(attWarehouseBase);
            case "3":
                List<AttTeamBase> attTeamBase = giveAnAlarmMapper.getAttTeamBase();
                if (attTeamBase != null && attTeamBase.size() > 0) {
                    attTeamBase = attTeamBase.stream().filter(m ->
                            DistanceUtil.getDistance(Double.valueOf(m.getLatitude()), Double.valueOf(m.getLongitude()),
                                    latitude, longitude).intValue() < radius).collect(Collectors.toList());
                }
                return CommonResult.success(attTeamBase);
            case "4":
                List<AvoidancePoint> avoidancePoint = giveAnAlarmMapper.getAvoidancePoint();
                if (avoidancePoint != null && avoidancePoint.size() > 0) {
                    avoidancePoint = avoidancePoint.stream().filter(m ->
                            DistanceUtil.getDistance(m.getLatitude().doubleValue(), m.getLongitude().doubleValue(),
                                    latitude, longitude).intValue() < radius).collect(Collectors.toList());
                }
                return CommonResult.success(avoidancePoint);
            case "5":
                if (StringUtils.isNotBlank(keyWord)) {
                    //查询海塘的管保区域
                    List<SeawallRange> seawallRanges = giveAnAlarmMapper.getSeawallRange(attSeawallBase.getSeawallCode(), 2);
                    if (seawallRanges != null && seawallRanges.size() > 0) {
                        SeawallRange seawallRange = seawallRanges.get(0);
                        String point = seawallRange.getPoint();
                        JSONArray objects = JSON.parseArray(point);
                        if (objects == null || objects.size() < 2) {
                            return CommonResult.failed("海塘管保区域未设置,请设置2个点位的数据");
                        }
                        JSONArray o1 = objects.getJSONArray(0);
                        JSONArray o2 = objects.getJSONArray(1);
                        String long1 = o1.getString(0);


                        String lat1 = o1.getString(1);
                        String long2 = o2.getString(0);
                        String lat2 = o2.getString(1);
                        //查询范围内数据
                        List<JSONObject> rangeData = getRangeData(Double.valueOf(long1), Double.valueOf(lat1), Double.valueOf(long2), Double.valueOf(lat2), keyWord);
                        return CommonResult.success(rangeData);
                    } else {
                        return CommonResult.failed("海塘管保区域未设置");
                    }

                } else {
                    CommonResult.failed("勾选查询的关键要素");
                }


            default:
                break;
        }
        return CommonResult.failed("勾选的类型错误");
    }

    @Override
    public CommonResult displacement(String date, Integer jclx) {
        //获取所有工程
        //查询工程时间点内检测报警数据
        HashMap<String, String> map = new HashMap<>();
        if (jclx == 1) {
            map.put("jclx", "4");
        }
        if (jclx == 2) {
            map.put("jclx", "16");
        }
        if (com.ygkj.utils.StringUtils.isBlank(date)) {
            date = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        }
        map.put("endTime", date);
        //一小时前的时间
        Calendar c = Calendar.getInstance();
        c.setTime(DateUtil.parseDate(null, date));
        c.add(Calendar.HOUR_OF_DAY, -1);
        Date time = c.getTime();
        map.put("startTime", DateUtil.format(time, "yyyy-MM-dd HH:mm:ss"));
        map.put("pageSize", "-1");
        map.put("pageIndex", "1");

        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("authorization", token);
        List<Object> list = new ArrayList<>();
        try {
            //拿到所有鹿城的对应测点数据
            String httpPost = HttpClientUtil.httpPost(aqjcList, map, tokenMap);
            JSONObject object = JSON.parseObject(httpPost);
            int code = object.getIntValue("code");
            if (code == 200) {
                JSONArray jsonArray = object.getJSONArray("rows");
                if (jsonArray != null && jsonArray.size() > 0) {
                    Map<String, List<Object>> gcbm = jsonArray.stream().collect(Collectors.groupingBy(m -> JSON.parseObject(m.toString()).getString("gcbm")));
                    gcbm.forEach((k, v) -> {
                        List<Object> gcsj = v.stream().sorted(Comparator.comparing(s -> JSON.parseObject(s.toString()).getDate("gcsj")).reversed()).collect(Collectors.toList());
                        Object o = gcsj.get(0);
                        list.add(o);
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(list);
    }

    @Override
    public CommonResult getAqjcData(Integer days, String stationCode, String startTime, String endTime) {
        HashMap<String, String> map = new HashMap<>();
        String[] split = stationCode.split("_");
        if (split == null && split.length < 2) {
            return CommonResult.failed("测点编码不正确");
        }
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(Integer.valueOf(split[1]));
        map.put("ids", split[1]);
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        } else {
            Calendar calendar = Calendar.getInstance();
            Date now = new Date();
            calendar.setTime(now);
            calendar.add(Calendar.DATE, days * -1);
            Date start = calendar.getTime();
            map.put("startTime", DateUtil.format(start, "yyyy-MM-dd HH:mm:ss"));
            map.put("endTime", DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"));
        }

        map.put("getLimit", "true");
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("authorization", token);
        try {
            //拿到测点数据
            String httpPost = HttpClientUtil.httpPost(aqjcData, map, tokenMap);
            JSONObject object = JSON.parseObject(httpPost);
            int code = object.getIntValue("code");
            if (code == 200) {
                JSONObject jsonObject = object.getJSONObject("data");
                if (jsonObject != null && jsonObject.size() > 0) {
                    Object o = jsonObject.get(split[1]);
                    return CommonResult.success(o);
                } else {
                    return CommonResult.success(new ArrayList<>());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed("未查到数据");
    }

    @Override
    public void exportGetAqjcData(Integer days, String stationCode, String startTime, String endTime, String stationName, HttpServletResponse response) {
        CommonResult aqjcData = getAqjcData(days, stationCode, startTime, endTime);
        Object data = aqjcData.getData();
        List<AqjcExportResVo> list = JSONArray.parseArray(JSON.toJSONString(data), AqjcExportResVo.class);
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("安全监测", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            EasyExcel.write(response.getOutputStream(), AqjcExportResVo.class).sheet(stationName).doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AttSeawallBaseVo getWarn(CommonResult result) {
        HashMap<Object, Object> warnMap = (HashMap<Object, Object>) result.getData();
        List<AttSeawallBaseVo> list = (List<AttSeawallBaseVo>) warnMap.get("list");
        return list.get(0);
    }

    public JSONArray fuckingCurrentTyphoon(Double lat, Double lng) {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String url = "http://data.istrongcloud.com/v2/data/complex/" + calendar.get(Calendar.YEAR) + ".json?v=" + currentTimeMillis;
        String typhoons = HttpClientUtil.httpGet(url);
        if (StringUtils.isEmpty(typhoons)) {
            return new JSONArray();
        }
        List<Object> list = JSONArray.parseArray(typhoons).stream()
                .filter(obj -> ((JSONObject) obj).getInteger("is_current").intValue() == 1)
                .collect(Collectors.toList());
        JSONArray result = new JSONArray();
        for (Object o : list) {
            JSONObject typhoon = (JSONObject) o;
            String code = typhoon.getString("tfbh");
            RealTyphoonData data = giveAnAlarmMapper.selectDetail(code);
            if (data == null) {
                continue;
            }
            String detailUrl = "http://data.istrongcloud.com/v2/data/complex/" + code + ".json?v=" + currentTimeMillis;
            String detail = HttpClientUtil.httpGet(detailUrl);
            JSONObject jsonObject = JSONArray.parseArray(detail).getJSONObject(0);
            JSONArray points = jsonObject.getJSONArray("points");
            for (Object point : points) {
                JSONObject pointObj = (JSONObject) point;
                double value = DistanceUtil.getDistance(lat, lng, Double.parseDouble(pointObj.getString("lat")), Double.parseDouble(pointObj.getString("lng"))).doubleValue();
                pointObj.put("distance", value);
                JSONArray forecast = pointObj.getJSONArray("forecast");
                if (null != forecast && !forecast.isEmpty()) {
                    for (Object object : forecast) {
                        JSONArray array = ((JSONObject) object).getJSONArray("points");
                        for (Object pointStr : array) {
                            JSONObject pointJson = (JSONObject) pointStr;
                            double doubleValue = DistanceUtil.getDistance(lat, lng, Double.parseDouble(pointJson.getString("lat")), Double.parseDouble(pointJson.getString("lng"))).doubleValue();
                            pointJson.put("distance", doubleValue);
                        }
                    }
                }
            }
            jsonObject.put("id", data.getId().intValue());
            result.add(jsonObject);
        }
        return result;
    }

    public JSONArray fuckingCurrentTyphoon() {
        //鹿城中心点
        return fuckingCurrentTyphoon(28.03827120238947, 120.57645463189935);
    }

    /**
     * 根据关键字和两个经纬度获取范围内的数据
     *
     * @param longitude1
     * @param latitude1
     * @param longitude2
     * @param latitude2
     * @param keyWord
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<JSONObject> getRangeData(double longitude1, double latitude1, double longitude2, double latitude2, String keyWord) throws UnsupportedEncodingException {
        String url = "http://api.tianditu.gov.cn/search";

        JSONObject object = new JSONObject();
        //搜索的关键字 不支持’与&符号
        object.put("keyWord", keyWord);
        //目前查询的级别1-20
        object.put("level", "15");
        //查询的地图范围("-x,-y,x,y")
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer append = stringBuffer.append(longitude1).append(",").append(latitude1).append(",").append(longitude2).append(",").append(latitude2);
        object.put("mapBound", append.toString());
        //搜索类型（1：普通搜索，2：视野内搜索，3：周边搜索，4：普通建议词搜索，5：公交规划建议词搜索，
        // 6：公交规划起止点搜索（只搜索公交站），7： 纯地名搜索（不搜公交线），10：拉框搜索
        //传递参数为1-10，分别对应不同的搜索类型。暂无8和9。
        object.put("queryType", "2");
        //返回的结果数量（用于分页和缓存）1-300
        object.put("count", "300");
        //返回结果起始位（用于分页和缓存）默认0  0-300
        object.put("start", "0");

        Map<String, String> map1 = new HashMap<>();
        map1.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
        Map<String, Object> map = new HashMap<>();
        map.put("postStr", object.toJSONString());
        map.put("type", "query");
        map.put("tk", "4bdb37c167c981c421594200336c816d");
        String detail = HttpClientUtil.sendGet(url, map1, map, "utf-8");
        JSONObject jsonObject = JSON.parseObject(detail);
        JSONArray pois = jsonObject.getJSONArray("pois");
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        if (pois != null && pois.size() > 0) {
            for (int i = 0; i < pois.size(); i++) {
                JSONObject object2 = new JSONObject();
                JSONObject object1 = pois.getJSONObject(i);
                String lonlat = object1.getString("lonlat");
                String[] s = lonlat.split(" ");
                object2.put("name", object1.getString("name"));
                object2.put("address", object1.getString("address"));
                object2.put("longitude", s[0]);
                object2.put("latitude", s[1]);
                jsonObjects.add(object2);
            }
        }
        return jsonObjects;
    }

    /**
     * 海塘二维码
     *
     * @param seawallCode
     * @return
     */
    @Override
    public String qrCode(String seawallCode) {
        // 设置响应流信息
        try {
            AttSeawallBaseVo warn = getWarn(seawallGiveAnAlarm(seawallCode, null, null));
            Color color;
            Integer status = warn.getStatus();
            if (StringUtils.isEmpty(warn.getTideStationCode())) {
                color = gray;
            } else if (status == 1) {
                color = green;
            } else if (status == 5) {
                color = red;
            } else {
                color = yellow;
            }
            MatrixToImageConfig config = new MatrixToImageConfig(color.getRGB(), background.getRGB());
            BitMatrix bitMatrix = QRCodeUtil.createCode(seawallMobileUrl + seawallCode);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            //以流的形式输出到前端
            return Base64Utils.encodeToString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 海塘二维码
     *
     * @param reqVo
     * @return
     */
    @Override
    public String qrCode(QrCodeReqVo reqVo) {
        // 计算得分
        int score = countScore(reqVo);

        //根据得分计算二维码颜色
        Color color = score2Color(score, reqVo.getSeawallCode());
        try {
//            AttSeawallBaseVo warn = getWarn(seawallGiveAnAlarm(seawallCode, null));
            MatrixToImageConfig config = new MatrixToImageConfig(color.getRGB(), background.getRGB());
            BitMatrix bitMatrix = QRCodeUtil.createCode(seawallMobileUrl + reqVo.getSeawallCode());
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            //以流的形式输出到前端
            return Base64Utils.encodeToString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Color score2Color(int score, String seawallCode) {
        //查询海塘各码的分数
        List<HealthCodeColor> list = healthCodeService.listColor(seawallCode);
        String category = "";
        for (HealthCodeColor healthCodeColor : list) {
            if (score >= healthCodeColor.getMinVal() && score <= healthCodeColor.getMaxVal()) {
                category = healthCodeColor.getCategory();
                break;
            }
        }
        switch (category) {
            case "黄码":
                return yellow;
            case "红码":
                return red;
            default:
                return green;
        }
    }

    /**
     * 计算海塘分数
     *
     * @param reqVo
     * @return
     */
    private int countScore(QrCodeReqVo reqVo) {
        String seawallCode = reqVo.getSeawallCode();
        int score = 0;
        //首先查询该海塘的权重
        List<HealthCodeWeight> list = healthCodeService.listHealthCodeWeight(seawallCode);
        Map<String, HealthCodeWeight> map = list.stream().collect(Collectors.toMap(HealthCodeWeight::getCategory, Function.identity()));
        CommonResult commonResult = projectManageClient.selectRuntimeWarn(seawallCode);
        List runtimeWarnResVoList = (List) commonResult.getData();
        Object o = runtimeWarnResVoList.get(0);
        RuntimeWarnResVo runtimeWarnResVo = JSONObject.parseObject(JSON.toJSONString(o), RuntimeWarnResVo.class);

        //安全鉴定超期
        Integer safety = runtimeWarnResVo.getSafety();
        if (safety == null || safety == 0) {
            score += map.get("安全鉴定超期").getScore();
        }

        //汛前检查超期
        if (runtimeWarnResVo.getFlood() == 0) {
            score += map.get("汛前检查超期").getScore();
        }

        //年度总结超期
        if (runtimeWarnResVo.getYearReport() == 0) {
            score += map.get("年度总结超期").getScore();
        }

        //巡查信息异常
        if (runtimeWarnResVo.getPatrol() == 0) {
            score += map.get("巡查信息异常").getScore();
        }

        //工程隐患
        Integer danger = runtimeWarnResVo.getDanger();
        List<HealthCodeWeight> dangerWeightList = map.get("工程隐患").getSubWeight();
        String dangerCategory = "";
        switch (danger) {
            case 1:
                dangerCategory = "有未解决";
                break;
            case 2:
                dangerCategory = "有已解决";
                break;
            default:
                dangerCategory = "无";
        }
        for (HealthCodeWeight healthCodeWeight : dangerWeightList) {
            if (healthCodeWeight.getCategory().equals(dangerCategory)) {
                score += healthCodeWeight.getScore();
                break;
            }
        }

        //安全鉴定
        String safetyConclusion = runtimeWarnResVo.getSafetyConclusion();
        List<HealthCodeWeight> safeWeightList = map.get("安全鉴定").getSubWeight();
        if (!"一类塘".equals(safetyConclusion) && !"二类塘".equals(safetyConclusion) && !"三类塘".equals(safetyConclusion)) {
            safetyConclusion = "一类塘";
        }
        for (HealthCodeWeight healthCodeWeight : safeWeightList) {
            if (healthCodeWeight.getCategory().equals(safetyConclusion)) {
                score += healthCodeWeight.getScore();
                break;
            }
        }

        SeawallWarnResVo seawallWarnResVo = selectWarn(seawallCode, reqVo.getTime(), reqVo.getFcstId());

        //海塘报警
        score += tideScore(map.get("海塘报警").getSubWeight(), seawallWarnResVo.getWarn());

        //海塘预警
        score += tideScore(map.get("海塘预警").getSubWeight(), seawallWarnResVo.getWarn());

        //台风影响
        score += typhoonScore(map.get("台风影响"), reqVo);

        return score;
    }

    private int typhoonScore(HealthCodeWeight healthCodeWeight, QrCodeReqVo reqVo) {
        //查询海塘的经纬度
        SeawallPointResVo seawallPointResVo = giveAnAlarmMapper.selectSeawallPoint(reqVo.getSeawallCode());
        Double lat = Double.valueOf(seawallPointResVo.getLatitude());
        Double lng = Double.valueOf(seawallPointResVo.getLongitude());
        if (reqVo.getTime() == null) {
            //查询当前台风
            long currentTimeMillis = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            String url = "http://data.istrongcloud.com/v2/data/complex/" + calendar.get(Calendar.YEAR) + ".json?v=" + currentTimeMillis;
            String typhoons = HttpClientUtil.httpGet(url);
            if (StringUtils.isEmpty(typhoons)) {
                return healthCodeWeight.getScore();
            }
            List<Object> list = JSONArray.parseArray(typhoons).stream()
                    .filter(obj -> ((JSONObject) obj).getInteger("is_current").intValue() == 1)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(list)) {
                return healthCodeWeight.getScore();
            }
            for (Object o : list) {
                JSONObject typhoon = (JSONObject) o;
                String code = typhoon.getString("tfbh");
                RealTyphoonData data = giveAnAlarmMapper.selectDetail(code);
                if (data == null) {
                    continue;
                }
                String detailUrl = "http://data.istrongcloud.com/v2/data/complex/" + code + ".json?v=" + currentTimeMillis;
                String detail = HttpClientUtil.httpGet(detailUrl);
                JSONObject jsonObject = JSONArray.parseArray(detail).getJSONObject(0);
                JSONArray points = jsonObject.getJSONArray("points");
                JSONObject lastPoint = points.getJSONObject(points.size() - 1);
                if (include(lat, lng, lastPoint)) {
                    break;
                }
            }
        } else {
            try {
                Date date = sdf.parse(reqVo.getTime());
                //查询在该时间内的台风
                List<RealTyphoonData> list = giveAnAlarmMapper.getTyphoonByDate(reqVo.getTime());
                if (CollectionUtils.isEmpty(list)) {
                    return 0;
                }
                for (RealTyphoonData realTyphoonData : list) {
                    String pointStr = realTyphoonData.getPoints();
                    JSONArray jsonArray = JSONArray.parseArray(pointStr);
                    JSONObject last = null;
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject point = jsonArray.getJSONObject(i);
                        Date pointDate = sdf.parse(point.getString("time").replace("T", " "));
                        if (pointDate.after(date)) {
                            break;
                        }
                        last = point;
                    }
                    if (include(lat, lng, last)) {
                        break;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return healthCodeWeight.getScore();
    }

    private boolean include(Double lat, Double lng, JSONObject point) {
        Double distance = DistanceUtil.getDistance(lat, lng, point.getDouble("lat"), point.getDouble("lng")).doubleValue();
        //对比10级风圈最大半径，小于则受台风影响
        JSONObject radius10_quad = point.getJSONObject("radius10_quad");
        double se = radius10_quad.getDoubleValue("se");
        double sw = radius10_quad.getDoubleValue("sw");
        double ne = radius10_quad.getDoubleValue("ne");
        double nw = radius10_quad.getDoubleValue("nw");
        if (se >= distance || sw >= distance || ne >= distance || nw >= distance) {
            return true;
        }
        return false;
    }

    /**
     * 潮位分数
     *
     * @param healthCodeWeightList
     * @param warn
     * @return
     */
    private int tideScore(List<HealthCodeWeight> healthCodeWeightList, Integer warn) {
        String category = "";
        switch (warn) {
            case 2:
                category = "超蓝色";
                break;
            case 3:
                category = "超黄色";
                break;
            case 4:
                category = "超橙色";
                break;
            case 5:
                category = "超红色";
                break;
            default:
                category = "正常";
        }

        for (HealthCodeWeight healthCodeWeight : healthCodeWeightList) {
            if (healthCodeWeight.getCategory().equals(category)) {
                return healthCodeWeight.getScore();
            }
        }
        return 0;
    }

    /**
     * 安全监测警报
     *
     * @param date
     * @param seawallCode
     * @return
     */
    @Override
    public Map<String, Integer> aqjcWarn(String date, String seawallCode) {
        Integer wy = aqjcStatus(date, seawallCode, 1);
        Integer zj = aqjcStatus(date, seawallCode, 2);
        Map<String, Integer> result = new HashMap<>();
        result.put("wy", wy);
        result.put("zj", zj);
        return result;
    }

    private Integer aqjcStatus(String date, String seawallCode, Integer type) {
        CommonResult displacement = displacement(date, type);
        List<JSONObject> data = (List<JSONObject>) displacement.getData();
        Integer status = 0;
        if (!CollectionUtils.isEmpty(data)) {
            for (JSONObject jsonObject : data) {
                String gcbm = jsonObject.getString("gcbm");
                if (seawallCode.equals(gcbm)) {
                    status = 1;
                    break;
                }
            }
        }

        return status;
    }

    @Override
    public CommonResult waterGiveAnAlarm2(WarningRule warningRule) {
        //获取鹿城区的所有水库、河道、堰闸、潮位测站
        List<AttStBase> rainLevels = giveAnAlarmMapper.getwaterLevels();
        ArrayList<AttStBase> attStBases = new ArrayList<>();
        if (rainLevels != null && rainLevels.size() > 0) {
            Map<String, List<AttStBase>> type = rainLevels.stream().collect(Collectors.groupingBy(AttStBase::getStType));
            type.forEach((k, v) -> {
                String table;
                List<String> codes = v.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                switch (k) {
                    //闸坝水位站
                    case "DD":
                        table = StationType.GATE.table().concat(LocalDate.now().format(yyyyMM));
                        List<StWasR> stWasRS = graphRpcService.selectWasByTimeCode(table, codes, null);
                        if (stWasRS != null && stWasRS.size() > 0) {
                            for (StWasR stWasR : stWasRS) {
                                double upz = stWasR.getUpz() == null ? 0d : stWasR.getUpz().doubleValue();
                                double dwz = stWasR.getDwz() == null ? 0d : stWasR.getDwz().doubleValue();
                                String ovs = stWasR.getOvs() == null ? "0" : stWasR.getOvs();
                                String mxgtq = stWasR.getMxgtq() == null ? "0" : stWasR.getMxgtq();
                                Date tm = stWasR.getTm();
                                String ll = stWasR.getLl();
                                String mgstcd = stWasR.getMgstcd();
                                for (AttStBase attStBase : v) {
                                    if (attStBase.getStCode().equals(mgstcd)) {
                                        attStBase.setUpz(upz);
                                        attStBase.setDwz(dwz);
                                        attStBase.setOpenGate(ovs);
                                        attStBase.setFlow(mxgtq);
                                        attStBase.setTime(tm);
                                        attStBase.setLl(ll);
                                        // 水闸是闸上水位超警
                                        String status = StringUtils.EMPTY;
                                        if (attStBase.getWarningLevel() != null
                                                && attStBase.getWarningLevel() > 0d
                                                && attStBase.getUpz() > attStBase.getWarningLevel()) {
                                            attStBase.setOverWarnLevel(attStBase.getUpz() - attStBase.getWarningLevel());
                                            attStBase.setWarning(true);
                                        }
                                        attStBase.setStatus(status);
                                    }
                                }
                            }
                        }
                        break;
                    //潮位站
                    case "TT":
                        table = StationType.TIDE.table().concat(LocalDate.now().format(yyyyMM));
                        List<StTideR> stTideRS = graphRpcService.selectTideByTimeCode(table, codes, null);
                        if (stTideRS != null && stTideRS.size() > 0) {
                            for (StTideR stTideR : stTideRS) {
                                double tdz = stTideR.getTdz().doubleValue();
                                String mgstcd = stTideR.getMgstcd();
                                Date tm = stTideR.getTm();
                                for (AttStBase attStBase : v) {
                                    if (attStBase.getStCode().equals(mgstcd)) {
                                        attStBase.setTdz(stTideR.getTdz().doubleValue());
                                        attStBase.setTime(stTideR.getTm());
                                        String status = StringUtils.EMPTY;
                                        if (warningRule.getOverbluelevel() != null && warningRule.getOverbluelevel() > 0d && attStBase.getTdz() > (warningRule.getOverbluelevel() / 1000)) {
                                            attStBase.setOverWarnLevel(attStBase.getTdz() - warningRule.getOverbluelevel());
                                            attStBase.setWarning(true);
                                            attStBase.setStatus("蓝色警报");
                                        }
                                        if (warningRule.getOveryellowlevel() != null && warningRule.getOveryellowlevel() > 0d && attStBase.getTdz() > (warningRule.getOveryellowlevel() / 1000)) {
                                            attStBase.setOverWarnLevel(attStBase.getTdz() - warningRule.getOveryellowlevel());
                                            attStBase.setWarning(true);
                                            attStBase.setStatus("黄色警报");
                                        }
                                        if (warningRule.getOverorangelevel() != null && warningRule.getOverorangelevel() > 0d && attStBase.getTdz() > (warningRule.getOverorangelevel() / 1000)) {
                                            attStBase.setOverWarnLevel(attStBase.getTdz() - warningRule.getOverorangelevel());
                                            attStBase.setWarning(true);
                                            attStBase.setStatus("橙色警报");
                                        }
                                        if (warningRule.getOverredlevel() != null && warningRule.getOverredlevel() > 0d && attStBase.getTdz() > (warningRule.getOverredlevel() / 1000)) {
                                            attStBase.setOverWarnLevel(attStBase.getTdz() - warningRule.getOverredlevel());
                                            attStBase.setWarning(true);
                                            attStBase.setStatus("红色警报");
                                        }
                                        attStBase.setStatus(status);
                                    }
                                }
                            }
                        }
                        break;
                    //水库水位站
                    case "RR":
                        table = StationType.RESERVOIR.table().concat(LocalDate.now().format(yyyyMM));
                        ;
                        List<StRsvrR> stRsvrRS = graphRpcService.selectRsvrWaterLevelByTimeCode(table, codes, null);
                        if (stRsvrRS != null && stRsvrRS.size() > 0) {
                            for (StRsvrR stRsvrR : stRsvrRS) {
                                double waterLevel = stRsvrR.getRz().doubleValue();
                                String mgstcd = stRsvrR.getMgstcd();
                                Date tm = stRsvrR.getTm();
                                for (AttStBase attStBase : v) {
                                    if (attStBase.getStCode().equals(mgstcd)) {
                                        attStBase.setWaterLevel(waterLevel);
                                        attStBase.setTime(tm);
                                        String status = "";
                                        List<String> statusList = new ArrayList<>();
                                        if (null == attStBase.getLimitLevel() || 0d == attStBase.getLimitLevel()) {
                                            status = WaterStationType.NORMAL_RIVER.type();
                                        } else {
                                            double limitLevel = attStBase.getLimitLevel() + warningRule.getOverlimitlevel() / 1000;
                                            if (waterLevel > limitLevel) {
                                                attStBase.setWarning(true);
                                                attStBase.setOverWarnLevel(attStBase.getWaterLevel() - attStBase.getLimitLevel());
                                                statusList.add(WaterStationType.SUPER_LIMIT.type());
                                            } else {
                                                status = WaterStationType.NORMAL_RIVER.type();
                                            }
                                        }
                                        if (null == attStBase.getFloodLevel() || 0d == attStBase.getFloodLevel()) {
                                            status = WaterStationType.NORMAL_RIVER.type();
                                        } else {
                                            double floodLevel = attStBase.getFloodLevel() + warningRule.getOverfloodlevel() / 1000;
                                            if (waterLevel > floodLevel) {
                                                attStBase.setWarning(true);
                                                attStBase.setOverWarnLevel(attStBase.getWaterLevel() - attStBase.getFloodLevel());
                                                statusList.add(WaterStationType.SUPER_FLOOD.type());
                                            } else {
                                                status = WaterStationType.NORMAL_RIVER.type();
                                            }
                                        }
                                        if (statusList.size() > 0) {
                                            status = String.join(",", statusList);
                                        }
                                        attStBase.setStatus(status);
                                    }
                                }

                            }
                        }
                        break;
                    //河道水位站
                    case "ZZ":
                        table = StationType.RIVER.table().concat(LocalDate.now().format(yyyyMM));
                        ;
                        List<StRiverR> stRiverRS = graphRpcService.selectRiverByTimeCode(table, codes, null);
                        if (stRiverRS != null && stRiverRS.size() > 0) {
                            for (StRiverR stRiverR : stRiverRS) {
                                double waterLevel = stRiverR.getZ().doubleValue();
                                String mgstcd = stRiverR.getMgstcd();
                                Date tm = stRiverR.getTm();
                                for (AttStBase attStBase : v) {
                                    if (attStBase.getStCode().equals(mgstcd)) {
                                        attStBase.setWaterLevel(waterLevel);
                                        attStBase.setTime(tm);
                                        String status = "";
                                        List<String> statusList = new ArrayList<>();
                                        if (null == attStBase.getWarningLevel() || 0d == attStBase.getWarningLevel()) {
                                            status = WaterStationType.NORMAL_RIVER.type();
                                        } else {
                                            double warningLevel = attStBase.getWarningLevel() + warningRule.getOverwarninglevel().doubleValue() / 1000;
                                            if (waterLevel > warningLevel) {
                                                attStBase.setWarning(true);
                                                attStBase.setOverWarnLevel(attStBase.getWaterLevel() - attStBase.getWarningLevel());
                                                statusList.add(WaterStationType.SUPER_WARNING.type());
                                            } else {
                                                status = WaterStationType.NORMAL_RIVER.type();
                                            }
                                        }
                                        if (null == attStBase.getGuaranteeLevel() || 0d == attStBase.getGuaranteeLevel()) {
                                            status = WaterStationType.NORMAL_RIVER.type();
                                        } else {
                                            double guaranteeLevel = attStBase.getGuaranteeLevel() + warningRule.getOverguaranteelevel().doubleValue() / 1000;
                                            if (waterLevel > guaranteeLevel) {
                                                attStBase.setWarning(true);
                                                attStBase.setOverWarnLevel(attStBase.getWaterLevel() - attStBase.getGuaranteeLevel());
                                                statusList.add(WaterStationType.SUPER_GUARANTEE.type());
                                            } else {
                                                status = WaterStationType.NORMAL_RIVER.type();
                                            }
                                        }
                                        if (statusList.size() > 0) {
                                            status = String.join(",", statusList);
                                        }
                                        attStBase.setStatus(status);
                                    }
                                }
                            }
                        }
                        break;
                }
                attStBases.addAll(v);
            });
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", attStBases);
        if (attStBases != null && attStBases.size() > 0) {
            long count = attStBases.stream().filter(m -> m.isWarning()).count();
            long tt = attStBases.stream().filter(m -> m.isWarning() && "TT".equals(m.getStType())).count();
            long dd = attStBases.stream().filter(m -> m.isWarning() && "DD".equals(m.getStType())).count();
            long zz = attStBases.stream().filter(m -> m.isWarning() && "ZZ".equals(m.getStType())).count();
            long rr = attStBases.stream().filter(m -> m.isWarning() && "RR".equals(m.getStType())).count();
            map.put("bj", count);
            map.put("ttCount", tt);
            map.put("ddCount", dd);
            map.put("zzCount", zz);
            map.put("rrCount", rr);

        }
        return CommonResult.success(map);
    }

    @Override
    public CommonResult rainGiveAnAlarm2(WarningRule warningRule) {
        //获取鹿城的雨量站
        List<Map<String, Object>> rainLevels = giveAnAlarmMapper.getRainLevels();
        //查询每个测站1小时内最新的一条展示数据
        Set<String> set = new HashSet<>();
        if (rainLevels != null && rainLevels.size() > 0) {
            for (Map<String, Object> rainLevel : rainLevels) {
                String stcd = String.valueOf(rainLevel.get("stcd"));
                set.add(stcd);
            }
        }

        List<Map<String, Object>> hoursRain = null;
        if (!set.isEmpty()) {
            String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            hoursRain = giveAnAlarmMapper.getHourRain(StationTypes.RAINFALL.table() + yyyyMM, warningRule.getRainhour(), set);
        }
        c:
        for (Map<String, Object> rainLevel : rainLevels) {
            String stcd = String.valueOf(rainLevel.get("stcd"));
            if (hoursRain != null && hoursRain.size() > 0) {
                for (Map<String, Object> map : hoursRain) {
                    String stcd1 = String.valueOf(map.get("stcd"));
                    if (stcd.equals(stcd1)) {
                        //累计降雨量
                        double rain = Double.valueOf(String.valueOf(map.get("rain")));
                        //最近时间作为监测时间
                        String tm = String.valueOf(map.get("tm"));
                        //累计雨量大于规定值就报警
                        if (rain > warningRule.getCumulativerain()) {
                            rainLevel.put("drp", String.valueOf(rain));
                            rainLevel.put("tm", tm);
                            rainLevel.put("status", "true");
                            continue c;
                        }
                    }
                }
            }
            rainLevel.put("status", "false");
        }
        HashMap<Object, Object> map = new HashMap<>();
        map.put("list", rainLevels);

        return CommonResult.success(map);
    }

    @Override
    public CommonResult rainGiveAnAlarm3(WarningRule warningRule) {
        //获取鹿城的雨量站
        List<Map<String, Object>> rainLevels = giveAnAlarmMapper.getRainLevels();
        String bindcode = warningRule.getBindcode();
        List<Map<String, Object>> bindStations = new ArrayList<>();
        //查询每个测站1小时内最新的一条展示数据
        Set<String> set = new HashSet<>();
        if (rainLevels != null && rainLevels.size() > 0) {
            for (Map<String, Object> rainLevel : rainLevels) {
                String stcd = String.valueOf(rainLevel.get("stcd"));
                if (bindcode.contains(stcd)){
                    set.add(stcd);
                    bindStations.add(rainLevel);
                }
            }
        }

        List<Map<String, Object>> hoursRain = null;
        if (!set.isEmpty()) {
            String yyyyMM = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            hoursRain = giveAnAlarmMapper.getHourRain(StationTypes.RAINFALL.table() + yyyyMM, warningRule.getRainhour(), set);
        }
        Double rainfall = 0.0;
        c:
        for (Map<String, Object> bindStation : bindStations) {
            String stcd = String.valueOf(bindStation.get("stcd"));
            if (hoursRain != null && hoursRain.size() > 0) {
                for (Map<String, Object> map : hoursRain) {
                    String stcd1 = String.valueOf(map.get("stcd"));
                    if (stcd.equals(stcd1)) {
                        //累计降雨量
                        double rain = Double.valueOf(String.valueOf(map.get("rain")));
                        rainfall += rain;
                        //最近时间作为监测时间
                        String tm = String.valueOf(map.get("tm"));
                        //累计雨量大于规定值就报警
                        if (rain > warningRule.getCumulativerain()) {
                            bindStation.put("drp", String.valueOf(rain));
                            bindStation.put("tm", tm);
                            bindStation.put("status", "true");
                            continue c;
                        }
                    }
                }
            }
            bindStation.put("status", "false");
        }
        Double aveRainfall = bindStations.size() > 0?rainfall/bindStations.size():rainfall;
        aveRainfall = new BigDecimal(aveRainfall+"").setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        HashMap<Object, Object> map = new HashMap<>();
        map.put("list", bindStations);
        map.put("aveRainfall",aveRainfall);
        return CommonResult.success(map);
    }
/**
    @Override
    public void floodPreventionBulletin() {
        try {
            //时间范围
            Date end = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.HOUR_OF_DAY, -24);
            Date start = c.getTime();
            String endTime = DateUtil.format(end, "yyyy-MM-dd HH:00:00");
            String startTime = DateUtil.format(start, "yyyy-MM-dd HH:00:00");
            //查询这个时间点数据是否存在
            int isFcp = giveAnAlarmMapper.isFloodControlPresentation(endTime);
            if (isFcp > 0) {
                log.info("isFcp > 0 ");
                return;
            } else {
                //获取模板文档
                // File rootFile = new File(ResourceUtils.getURL("classpath:").getPath());
                //  File templateFile = new File(rootFile, "/office/防汛防台简报.docx");
                //雨情
                //24小时面雨量表按照行政区划分
                List<Map<String, Object>> mylList = new ArrayList<>();
                //24小时暴雨报警表（前5）
                List<Map<String, Object>> byList = new ArrayList<>();
                CommonResult commonResult = floodDroughtDefenseController.rainfall(null, null, null);
                JSONObject data = (JSONObject) commonResult.getData();
                double rainfall24 = data.getDoubleValue("rainfall_24");
                double rainfall72 = data.getDoubleValue("rainfall_72");
                JSONArray dataJSONArray = data.getJSONArray("rainfall_24_data");
                List<StPptnR> rainfall24Data = dataJSONArray.toJavaList(StPptnR.class);
                List<StPptnR> drp = rainfall24Data.stream().sorted(Comparator.comparing(StPptnR::getDrp).reversed()).collect(Collectors.toList());
                //查詢行政区划
                List<AttAdcdBase> adcdBases = giveAnAlarmMapper.getAdcdList();
                Map<String, String> adnmMap = adcdBases.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));

                StPptnR rainfall = drp.get(0);
                for (int i = 0; i < drp.size(); i++) {
                    StPptnR jsonObject = drp.get(i);
                    drp.get(i).setAreaName(adnmMap.get(jsonObject.getAreaCode()));
                    if (i < 5) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("xh", i + 1);
                        map.put("name", jsonObject.getStationName());
                        map.put("jyl", jsonObject.getDrp());
                        byList.add(map);
                    }
                }
                Map<String, List<StPptnR>> areaName = drp.stream().filter(m -> StringUtils.isNotBlank(m.getAreaName())).collect(Collectors.groupingBy(StPptnR::getAreaName));

                AtomicInteger ylxh = new AtomicInteger(0);
                AtomicReference<HashMap<String, Object>> maxMyl = new AtomicReference<>(new HashMap<>());
                areaName.forEach((k, v) -> {
                    ylxh.getAndIncrement();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("xh", ylxh);
                    map.put("xzqh", k);
                    double myl = 0d;
                    for (StPptnR jsonObject : v) {
                        Double drp1 = jsonObject.getDrp();
                        myl += drp1;
                    }
                    BigDecimal divide = new BigDecimal(myl).divide(new BigDecimal(v.size()), 1, BigDecimal.ROUND_HALF_UP);
                    map.put("myl", divide);
                    if (maxMyl.get().size() > 0) {
                        BigDecimal o = (BigDecimal) maxMyl.get().get("myl");
                        if (divide.compareTo(o) > 0) {
                            maxMyl.set(map);
                        }
                    } else {
                        maxMyl.set(map);
                    }
                    mylList.add(map);
                });

                //0.1-10mm，10-25mm，25-50mm，50-100mm，100-250mm，大于250mm。
                int index2 = 0, index3 = 0, index4 = 0, index5 = 0, index6 = 0, index7 = 0, index8 = 0;
                for (int i = 0; i < rainfall24Data.size(); i++) {
                    StPptnR stPptnR = rainfall24Data.get(i);
                    Double aDouble = stPptnR.getDrp();
                    if (aDouble != null) {
                        if (aDouble > 30) {
                            index2++;
                        }
                        if (aDouble >= 0.1 && aDouble < 10) {
                            index3++;
                        }
                        if (aDouble >= 10 && aDouble < 25) {
                            index4++;
                        }
                        if (aDouble >= 25 && aDouble < 50) {
                            index5++;
                        }
                        if (aDouble >= 50 && aDouble < 100) {
                            index6++;
                        }
                        if (aDouble >= 100 && aDouble < 250) {
                            index7++;
                        }
                        if (aDouble >= 250) {
                            index8++;
                        }
                    }
                }

                //获取鹿城区的所有水库、河道、堰闸、潮位测站
                List<AttStBase> rainLevels = giveAnAlarmMapper.getwaterLevels();
                Map<String, List<AttStBase>> type = rainLevels.stream().collect(Collectors.groupingBy(AttStBase::getStType));

                //水库水情
                List<Map<String, Object>> skWaterList = new ArrayList<>();
                List<String> skTables = echoWaterRainTableByDateInternal(StationType.RESERVOIR.table(), start, end);
                //查询水库绑定的主测站，已及安全鉴定结论
                List<AttResBaseVo> skStBases = giveAnAlarmMapper.getSkListStcd();
                List<String> skCodes = skStBases.stream().map(AttResBaseVo::getStcd).collect(Collectors.toList());
                List<Map<String, Object>> sksqList = giveAnAlarmMapper.getsksqList(skTables, skCodes, startTime, endTime);

                int skXh = 1, skCjCount = 0, skSfCj = 0, notOneClass = 0;
                if (sksqList != null && sksqList.size() > 0) {
                    Map<Object, List<Map<String, Object>>> listMap = sksqList.stream().collect(Collectors.groupingBy(m -> m.get("STCD")));
                    for (AttResBaseVo skStBase : skStBases) {
                        Map<String, Object> stringObjectMap = new HashMap<>();
                        String resCode = skStBase.getResCode();
                        String resName = skStBase.getResName();
                        String stcd = skStBase.getStcd();
                        String safetyConclusion = skStBase.getSafetyConclusion();
                        Double flLowLimLevCap = skStBase.getFlLowLimLevCap();
                        Double level = skStBase.getWarningLevel();
                        List<Map<String, Object>> list = listMap.get(stcd);
                        //是否超警
                        boolean isWarning = false;
                        //最高水位的数据
                        Map<String, Object> objectMap = null;
                        if (list != null && list.size() > 0) {
                            for (Map<String, Object> map : list) {
                                Double o = Double.valueOf(String.valueOf(map.get("rz")));
                                if (level != null && level > 0 && level < o) {
                                    skCjCount++;
                                    isWarning = true;
                                }
                                if (objectMap == null || Double.valueOf(String.valueOf(objectMap.get("rz"))) > o) {
                                    objectMap = map;
                                }
                            }
                        }
                        if (isWarning) {
                            //超警的测站
                            skSfCj++;
                            //计算超警数据是多少m
                            stringObjectMap.put("cjMax", objectMap != null && level != null && level > 0 ? Double.valueOf(String.valueOf(objectMap.get("Z"))) - level : 0);
                        } else {
                            stringObjectMap.put("cjMax", 0);
                        }
                        stringObjectMap.put("xh", skXh);
                        stringObjectMap.put("name", resName);
                        stringObjectMap.put("max", objectMap != null ? objectMap.get("rz") : "/");
                        stringObjectMap.put("tm", objectMap != null ? objectMap.get("TM") : "/");
                        stringObjectMap.put("aqjd", safetyConclusion);
                        //计算纳蓄能力
                        Float rz = getXByY(Float.parseFloat(String.valueOf(objectMap.get("rz"))), resCode);
                        BigDecimal subtract = new BigDecimal(flLowLimLevCap).subtract(new BigDecimal(rz)).setScale(1, BigDecimal.ROUND_HALF_UP);
                        if (subtract.compareTo(new BigDecimal(0)) > 0) {
                            stringObjectMap.put("nxnl", subtract);
                        } else {
                            stringObjectMap.put("nxnl", 0);
                        }
                        //水位数据5分钟一条，超警时长(h)是 cjCount * 5 / 60
                        stringObjectMap.put("hours", new BigDecimal(skCjCount).divide(new BigDecimal(12), 1, BigDecimal.ROUND_HALF_UP));
                        skXh++;
                        if (!safetyConclusion.contains("一类")) {
                            //非一类坝
                            notOneClass++;
                        }
                        //查询
                        skWaterList.add(stringObjectMap);
                    }
                }

                //江河水情
                List<Map<String, Object>> jhWaterList = new ArrayList<>();
                List<String> hdTables = echoWaterRainTableByDateInternal(StationType.RIVER.table(), start, end);
                List<AttStBase> hdStBases = type.get(StationType.RIVER.type());
                List<String> hdCodes = hdStBases.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                List<Map<String, Object>> hdsqList = giveAnAlarmMapper.getHdsqList(hdTables, hdCodes, startTime, endTime);

                int hdXh = 1, hdSfCj = 0;
                if (hdsqList != null && hdsqList.size() > 0) {
                    Map<Object, List<Map<String, Object>>> listMap = hdsqList.stream().collect(Collectors.groupingBy(m -> m.get("STCD")));
                    for (AttStBase hdStBase : hdStBases) {
                        Map<String, Object> stringObjectMap = new HashMap<>();
                        String stName = hdStBase.getStName();
                        String stCode = hdStBase.getStCode();
                        Double level = hdStBase.getWarningLevel();
                        List<Map<String, Object>> list = listMap.get(stCode);
                        //是否超警
                        boolean isWarning = false;
                        //最高水位的数据
                        Map<String, Object> objectMap = null;
                        int hdCjCount = 0;
                        if (list != null && list.size() > 0) {
                            for (Map<String, Object> map : list) {
                                Double o = Double.valueOf(String.valueOf(map.get("Z")));
                                if (level != null && level > 0 && level < o) {
                                    hdCjCount++;
                                    isWarning = true;
                                }
                                if (objectMap == null || Double.valueOf(String.valueOf(objectMap.get("Z"))) > o) {
                                    objectMap = map;
                                }
                            }
                        }
                        if (isWarning) {
                            //超警的测站
                            hdSfCj++;
                            //计算超警数据是多少m
                            stringObjectMap.put("cjMax", objectMap != null && level != null && level > 0 ? Double.valueOf(String.valueOf(objectMap.get("Z"))) - level : 0);
                        } else {
                            stringObjectMap.put("cjMax", 0);
                        }
                        stringObjectMap.put("xh", hdXh);
                        stringObjectMap.put("name", stName);
                        stringObjectMap.put("max", objectMap != null ? objectMap.get("Z") : "/");
                        stringObjectMap.put("tm", objectMap != null ? objectMap.get("TM") : "/");
                        //水位数据5分钟一条，超警时长(h)是 cjCount * 5 / 60
                        stringObjectMap.put("hours", new BigDecimal(hdCjCount).divide(new BigDecimal(12), 1, BigDecimal.ROUND_HALF_UP));
                        hdXh++;
                        //查询
                        jhWaterList.add(stringObjectMap);
                    }
                }

                //潮位水情
                List<Map<String, Object>> cwWaterList = new ArrayList<>();
                List<String> cwTables = echoWaterRainTableByDateInternal(StationType.TIDE.table(), start, end);
                List<AttStBase> cwStBases = type.get(StationType.TIDE.type());
                List<String> cwCodes = cwStBases.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                List<Map<String, Object>> cwsqList = giveAnAlarmMapper.getCwsqList(cwTables, cwCodes, startTime, endTime);

                int cwXh = 1, cwSfCj = 0;
                if (cwsqList != null && cwsqList.size() > 0) {
                    Map<Object, List<Map<String, Object>>> listMap = cwsqList.stream().collect(Collectors.groupingBy(m -> m.get("STCD")));
                    for (AttStBase cwStBase : cwStBases) {
                        int cwCjCount = 0;
                        Map<String, Object> stringObjectMap = new HashMap<>();
                        String stName = cwStBase.getStName();
                        String stCode = cwStBase.getStCode();
                        Double level = cwStBase.getWarningLevel();
                        List<Map<String, Object>> list = listMap.get(stCode);
                        //是否超警
                        boolean isWarning = false;
                        //最高水位的数据
                        Map<String, Object> objectMap = null;
                        if (list != null && list.size() > 0) {
                            for (Map<String, Object> map : list) {
                                Double o = Double.valueOf(String.valueOf(map.get("TDZ")));
                                if (level != null && level > 0 && level < o) {
                                    cwCjCount++;
                                    isWarning = true;
                                }
                                if (objectMap == null || Double.valueOf(String.valueOf(objectMap.get("TDZ"))) > o) {
                                    objectMap = map;
                                }
                            }
                        }
                        if (isWarning) {
                            //超警的测站
                            cwSfCj++;
                            //计算超警数据是多少m
                            stringObjectMap.put("cjMax", objectMap != null && level != null && level > 0 ? Double.valueOf(String.valueOf(objectMap.get("TDZ"))) - level : 0);
                        } else {
                            stringObjectMap.put("cjMax", 0);
                        }
                        stringObjectMap.put("xh", cwXh);
                        stringObjectMap.put("name", stName);
                        stringObjectMap.put("max", objectMap != null ? objectMap.get("TDZ") : "/");
                        stringObjectMap.put("tm", objectMap != null ? objectMap.get("TM") : "/");
                        //水位数据5分钟一条，超警时长(h)是 cjCount * 5 / 60
                        stringObjectMap.put("hours", new BigDecimal(cwCjCount).divide(new BigDecimal(12), 1, BigDecimal.ROUND_HALF_UP));
                        cwXh++;
                        //查询
                        cwWaterList.add(stringObjectMap);
                    }
                }

                //水闸工情
                List<Map<String, Object>> szWaterList = new ArrayList<>();
                List<String> szTables = echoWaterRainTableByDateInternal(StationType.GATE.table(), start, end);
                List<AttStBase> szStBases = type.get(StationType.GATE.type());
                List<String> szCodes = szStBases.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                List<Map<String, Object>> szgqList = giveAnAlarmMapper.getSzsqList(szTables, szCodes, startTime, endTime);

                int szXh = 1, sfkzCount = 0;
                double ljphl = 0d;
                if (szgqList != null && szgqList.size() > 0) {
                    Map<Object, List<Map<String, Object>>> listMap = szgqList.stream().collect(Collectors.groupingBy(m -> m.get("STCD")));
                    for (AttStBase szStBase : szStBases) {
                        Map<String, Object> stringObjectMap = new HashMap<>();
                        String stName = szStBase.getStName();
                        String stCode = szStBase.getStCode();
                        //闸门开度阈值
                        Double gateOpenThreshold = szStBase.getGateOpenThreshold();
                        List<Map<String, Object>> list = listMap.get(stCode);
                        //最高水位的数据
                        Map<String, Object> objectMap = null;
                        //是否开闸
                        boolean isKz = false;
                        double ljphlStcd = 0d;
                        int szKqCount = 0;
                        if (list != null && list.size() > 0) {
                            for (Map<String, Object> map : list) {
                                Double upz = Double.valueOf(String.valueOf(map.get("UPZ")).equals("null") ? "0" : String.valueOf(map.get("UPZ")));
                                //开度
                                String ovs = String.valueOf(map.get("OVS"));
                                //流量
                                String ll = String.valueOf(map.get("LL"));
                                if (gateOpenThreshold != null && gateOpenThreshold > 0 && StringUtils.isNotBlank(ovs)) {
                                    String[] split = ovs.split(",");
                                    for (String s : split) {
                                        Double aDouble = Double.valueOf("null".equals(s) ? "0" : s);
                                        if (aDouble != null && aDouble > gateOpenThreshold) {
                                            szKqCount++;
                                            isKz = true;
                                            //排洪量，5分钟一条数据，计算5分钟的排洪量，最后统计所有开闸时候的排洪量
                                            if (StringUtils.isNotBlank(ll)) {
                                                String[] split1 = ll.split(",");
                                                for (String s1 : split1) {
                                                    Double aDouble1 = Double.valueOf("null".equals(s1) ? "0" : s1);
                                                    if (aDouble1 != null) {
                                                        //万立方米
                                                        ljphlStcd += aDouble1 * 5 * 60 / 10000;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (objectMap == null || Double.valueOf("null".equals(String.valueOf(objectMap.get("UPZ"))) ? "0" : String.valueOf(objectMap.get("UPZ"))) > upz) {
                                    objectMap = map;
                                }
                            }
                            if (isKz) {
                                sfkzCount++;
                                ljphl += ljphlStcd;
                            }
                            stringObjectMap.put("xh", szXh);
                            stringObjectMap.put("name", stName);
                            stringObjectMap.put("max", objectMap != null ? objectMap.get("UPZ") : "/");
                            stringObjectMap.put("tm", objectMap != null ? objectMap.get("TM") : "/");
                            //累计排洪量
                            stringObjectMap.put("ljphl", new BigDecimal(ljphlStcd).setScale(2, BigDecimal.ROUND_HALF_UP));
                            //水位数据5分钟一条，开闸时长(h)是 szKqCount * 5 / 60
                            stringObjectMap.put("hours", new BigDecimal(szKqCount).divide(new BigDecimal(12), 1, BigDecimal.ROUND_HALF_UP));
                            szXh++;
                            //查询
                            szWaterList.add(stringObjectMap);
                        }
                    }
                }


                //准备数据
                String format = DateUtil.format(new Date(), "yyyy-MM-dd HH");
                Map<String, Object> params = new HashMap<>();
                params.put("index", "");
                params.put("year", format.substring(0, 4));
                params.put("month", format.substring(5, 7));
                params.put("day", format.substring(8, 10));
                params.put("hour", format.substring(11, 13));
                //雨情
                params.put("rainfall", rainfall24);
                params.put("rainfall2", rainfall72);
                params.put("index2", index2);
                params.put("proprotion", rainfall24Data.size() > 0 && index2 > 0 ? new BigDecimal(index2).multiply(new BigDecimal(100))
                        .divide(new BigDecimal(rainfall24Data.size()), 1, BigDecimal.ROUND_HALF_UP).toString() + "%" : "0%");
                params.put("stnm", rainfall.getStationName());
                params.put("rainfall4", rainfall.getDrp());
                params.put("adnm", maxMyl.get().get("xzqh"));
                params.put("rainfall5", maxMyl.get().get("myl"));
                params.put("index3", index3);
                params.put("index4", index4);
                params.put("index5", index5);
                params.put("index6", index6);
                params.put("index7", index7);
                params.put("index8", index8);

                //水库水情
                List<Map<String, Object>> skCjMax = skWaterList.stream().sorted(Comparator.comparing(m -> Double.valueOf(String.valueOf(m.get("cjMax"))))).collect(Collectors.toList());
                Map<String, Object> skObjectMap = new HashMap<>();
                if (skCjMax != null && skCjMax.size() > 0) {
                    //超警戒水位最高的测站
                    skObjectMap = skCjMax.get(skCjMax.size() - 1);
                }
                params.put("index9", skSfCj);
                params.put("skName", skObjectMap.containsKey("name") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("name") : "/");
                params.put("skTm", skObjectMap.containsKey("tm") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("tm") : "/");
                params.put("waterLevel", skObjectMap.containsKey("max") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("max") : "/");
                params.put("waterLevel2", skObjectMap.containsKey("cjMax") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("cjMax") : "/");
                params.put("index10", notOneClass);

                //江河水情
                List<Map<String, Object>> hdCjMax = jhWaterList.stream().sorted(Comparator.comparing(m -> Double.valueOf(String.valueOf(m.get("cjMax"))))).collect(Collectors.toList());
                Map<String, Object> hdObjectMap = new HashMap<>();
                if (hdCjMax != null && hdCjMax.size() > 0) {
                    //超警戒水位最高的测站
                    hdObjectMap = hdCjMax.get(hdCjMax.size() - 1);
                }
                params.put("index11", hdSfCj);
                params.put("jhName", hdObjectMap.containsKey("name") && Double.valueOf(String.valueOf(hdObjectMap.get("cjMax"))) > 0 ? hdObjectMap.get("name") : "/");
                params.put("jhTm", hdObjectMap.containsKey("tm") && Double.valueOf(String.valueOf(hdObjectMap.get("cjMax"))) > 0 ? hdObjectMap.get("tm") : "/");
                params.put("waterLevel3", hdObjectMap.containsKey("max") && Double.valueOf(String.valueOf(hdObjectMap.get("cjMax"))) > 0 ? hdObjectMap.get("max") : "/");
                params.put("waterLevel4", hdObjectMap.containsKey("cjMax") && Double.valueOf(String.valueOf(hdObjectMap.get("cjMax"))) > 0 ? hdObjectMap.get("cjMax") : "/");

                //潮位水情
                List<Map<String, Object>> cwCjMax = cwWaterList.stream().sorted(Comparator.comparing(m -> Double.valueOf(String.valueOf(m.get("cjMax"))))).collect(Collectors.toList());
                Map<String, Object> cwObjectMap = new HashMap<>();
                if (cwCjMax != null && cwCjMax.size() > 0) {
                    //超警戒水位最高的测站
                    cwObjectMap = cwCjMax.get(cwCjMax.size() - 1);
                }
                params.put("index12", cwSfCj);
                params.put("cwName", cwObjectMap.containsKey("name") && Double.valueOf(String.valueOf(cwObjectMap.get("cjMax"))) > 0 ? cwObjectMap.get("name") : "/");
                params.put("cwTm", cwObjectMap.containsKey("tm") && Double.valueOf(String.valueOf(cwObjectMap.get("cjMax"))) > 0 ? cwObjectMap.get("tm") : "/");
                params.put("waterLevel5", cwObjectMap.containsKey("max") && Double.valueOf(String.valueOf(cwObjectMap.get("cjMax"))) > 0 ? cwObjectMap.get("max") : "/");
                params.put("waterLevel6", cwObjectMap.containsKey("cjMax") && Double.valueOf(String.valueOf(cwObjectMap.get("cjMax"))) > 0 ? cwObjectMap.get("cjMax") : "/");

                //水闸工情
                List<Map<String, Object>> szHourMax = szWaterList.stream().sorted(Comparator.comparing(m -> Double.valueOf(String.valueOf(m.get("hours"))))).collect(Collectors.toList());
                Map<String, Object> szObjectMap = new HashMap<>();
                if (szHourMax != null && szHourMax.size() > 0) {
                    //超警戒水位最高的测站
                    szObjectMap = szHourMax.get(szHourMax.size() - 1);
                }
                params.put("index13", sfkzCount);
                params.put("phl", new BigDecimal(ljphl).setScale(2, BigDecimal.ROUND_HALF_UP));
                params.put("szName", szObjectMap.containsKey("name") && Double.valueOf(String.valueOf(szObjectMap.get("hours"))) > 0 ? szObjectMap.get("name") : "/");
                params.put("sc", szObjectMap.containsKey("hours") && Double.valueOf(String.valueOf(szObjectMap.get("hours"))) > 0 ? szObjectMap.get("hours") : "/");

                //所有的列表
                params.put("mylList", mylList);
                params.put("byList", byList);
                params.put("skWaterList", skWaterList);
                params.put("jhWaterList", jhWaterList);
                params.put("cwWaterList", cwWaterList);
                params.put("szWaterList", szWaterList);

                InputStream is = this.getClass().getResourceAsStream("/office/防汛防台简报.docx");
                MyXWPFDocument doc = new MyXWPFDocument(is);
                WordExportUtil.exportWord07(doc, params);
                String filename = "防汛防台简报.docx";
                //response.setHeader("content-disposition","attachment;filename*=utf-8''"+URLEncoder.encode(filename, "utf-8"));
                //response.setContentType("application/msword;charset=UTF-8");
                //word.write(response.getOutputStream());
                //上传到文件服务器，并把文件id和时间保存到数据库文件中
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                doc.write(bos);
                MultipartFile multipartFile = new ByteArrayMultipartFile(bos.toByteArray(), filename, "application/msword;charset=UTF-8");
                LinkedHashMap resp = authenticationClient.upload(multipartFile);
                JSONObject date = JSONObject.parseObject(JSONObject.toJSONString(resp, SerializerFeature.WriteMapNullValue));
                String fileId = date.getJSONObject("data").getString("id");
                //保存到数据库
                giveAnAlarmMapper.saveResponseLevel(UUIDUtils.getUUID32(), endTime, fileId);
            }
        } catch (Exception e) {
            log.info("异常-----------------------" + e.toString());
            e.printStackTrace();
        }
    }
**/

    @Override
    public void floodPreventionBulletin() {
        try {
            //时间范围
            Date end = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.HOUR_OF_DAY, -24);
            Date start = c.getTime();
            String endTime = DateUtil.format(end, "yyyy-MM-dd HH:00:00");
            String startTime = DateUtil.format(start, "yyyy-MM-dd HH:00:00");
            //查询这个时间点数据是否存在
            int isFcp = giveAnAlarmMapper.isFloodControlPresentation(endTime);
            if (isFcp > 0) {
                log.info("isFcp > 0 ");
                return;
            } else {
                //获取模板文档
                // File rootFile = new File(ResourceUtils.getURL("classpath:").getPath());
                //  File templateFile = new File(rootFile, "/office/防汛防台简报.docx");
                //气象预报 后面业主不要了
//                String httpGet = HttpClientUtil.httpGet("http://www.wz121.com/map/getWeatherWarn");
//                List<JSONObject> qxybList = JSONArray.parseArray(httpGet,JSONObject.class);
//                String qxyb = "";
//                if (qxybList != null && qxybList.size() > 0){
//                    List<JSONObject> pid = qxybList.stream().filter(o -> "29709".equals(o.getString("PID"))).collect(Collectors.toList());
//                    if (!CollectionUtils.isEmpty(pid)){
//                        qxyb = pid.get(0).getString("WARN_CONTENT");
//                    }else {
//                        qxyb = "暂无气象预警信息";
//                    }
//                }else {
//                    qxyb = "暂无气象预警信息";
//                }
                //雨情
                //24小时面雨量表按照行政区划分
                List<Map<String, Object>> mylList = new ArrayList<>();
                //24小时暴雨报警表（前5）
                List<Map<String, Object>> byList = new ArrayList<>();
                CommonResult commonResult = floodDroughtDefenseController.rainfall(null, null, null);
                JSONObject data = (JSONObject) commonResult.getData();
                double rainfallone = data.getDoubleValue("rainfall_1");
                double rainfall24 = data.getDoubleValue("rainfall_24");
                double rainfall_ten_day = data.getDoubleValue("rainfall_ten_day");
                double rainfall72 = data.getDoubleValue("rainfall_72");
                JSONArray data1JSONArray = data.getJSONArray("rainfall_1_data");
                JSONArray data3JSONArray = data.getJSONArray("rainfall_3_data");
                JSONArray dataJSONArray = data.getJSONArray("rainfall_24_data");
                List<StPptnR> rainfall1Data = data1JSONArray.toJavaList(StPptnR.class);
                List<StPptnR> rainfall3Data = data3JSONArray.toJavaList(StPptnR.class);
                List<StPptnR> rainfall24Data = dataJSONArray.toJavaList(StPptnR.class);
                Map<String, List<StPptnR>> data1ByStcd = rainfall1Data.stream().collect(Collectors.groupingBy(StPptnR::getMgstcd));
                Map<String, List<StPptnR>> data3ByStcd = rainfall3Data.stream().collect(Collectors.groupingBy(StPptnR::getMgstcd));
                List<StPptnR> drp = rainfall24Data.stream().sorted(Comparator.comparing(StPptnR::getDrp).reversed()).collect(Collectors.toList());
                //查詢行政区划
                List<AttAdcdBase> adcdBases = giveAnAlarmMapper.getAdcdList();
                Map<String, String> adnmMap = adcdBases.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));

                StPptnR rainfall = drp.get(0);
                for (int i = 0; i < drp.size(); i++) {
                    StPptnR jsonObject = drp.get(i);
                    drp.get(i).setAreaName(adnmMap.get(jsonObject.getAreaCode()));
//                    if (i < 5) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("xh", i + 1);
                    map.put("areaName", adnmMap.get(jsonObject.getAreaCode()));
                    map.put("name", jsonObject.getStationName());
                    map.put("jyl24", jsonObject.getDrp());
                    if (data1ByStcd.containsKey(jsonObject.getMgstcd())){
                        map.put("jyl1",data1ByStcd.get(jsonObject.getMgstcd()).get(0).getDrp());
                    }else {
                        map.put("jyl1",0.0);
                    }
                    if (data3ByStcd.containsKey(jsonObject.getMgstcd())){
                        map.put("jyl3",data3ByStcd.get(jsonObject.getMgstcd()).get(0).getDrp());
                    }else {
                        map.put("jyl3",0.0);
                    }
                    byList.add(map);
//                    }
                }
                Map<Object, List<Map<String, Object>>> byListByAreaName = byList.stream().filter(m -> StringUtils.isNotBlank((String) m.get("areaName"))).collect(Collectors.groupingBy(m -> m.get("areaName")));
                AtomicInteger ylxh = new AtomicInteger(0);
                byListByAreaName.forEach((k, v) -> {
                            ylxh.getAndIncrement();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("xh", ylxh);
                            map.put("xzqh", k);
                            double myl1= 0d;
                            double myl3 = 0d;
                            double myl24 = 0d;
                            for (Map<String, Object> map1 : v) {
                                Double drp1 = (Double) map1.get("jyl1");
                                myl1 += drp1;
                                Double drp3 = (Double) map1.get("jyl3");
                                myl3 += drp3;
                                Double drp24 = (Double) map1.get("jyl24");
                                myl24 += drp24;
                            }
                            BigDecimal divide1 = new BigDecimal(myl1).divide(new BigDecimal(v.size()), 1, BigDecimal.ROUND_HALF_UP);
                            BigDecimal divide3 = new BigDecimal(myl3).divide(new BigDecimal(v.size()), 1, BigDecimal.ROUND_HALF_UP);
                            BigDecimal divide24 = new BigDecimal(myl24).divide(new BigDecimal(v.size()), 1, BigDecimal.ROUND_HALF_UP);
                            map.put("myl1", divide1);
                            map.put("myl3", divide3);
                            map.put("myl24", divide24);
                            mylList.add(map);
                        });
                /**
                Map<String, List<StPptnR>> areaName = drp.stream().filter(m -> StringUtils.isNotBlank(m.getAreaName())).collect(Collectors.groupingBy(StPptnR::getAreaName));

                AtomicInteger ylxh = new AtomicInteger(0);
                AtomicReference<HashMap<String, Object>> maxMyl = new AtomicReference<>(new HashMap<>());
                areaName.forEach((k, v) -> {
                    ylxh.getAndIncrement();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("xh", ylxh);
                    map.put("xzqh", k);
                    double myl = 0d;
                    for (StPptnR jsonObject : v) {
                        Double drp1 = jsonObject.getDrp();
                        myl += drp1;
                    }
                    BigDecimal divide = new BigDecimal(myl).divide(new BigDecimal(v.size()), 1, BigDecimal.ROUND_HALF_UP);
                    map.put("myl", divide);
                    if (maxMyl.get().size() > 0) {
                        BigDecimal o = (BigDecimal) maxMyl.get().get("myl");
                        if (divide.compareTo(o) > 0) {
                            maxMyl.set(map);
                        }
                    } else {
                        maxMyl.set(map);
                    }
                    mylList.add(map);
                });**/

                //0.1-10mm，10-25mm，25-50mm，50-100mm，100-250mm，大于250mm。
                int index2 = 0, index3 = 0, index4 = 0, index5 = 0, index6 = 0, index7 = 0, index8 = 0;
                for (int i = 0; i < rainfall24Data.size(); i++) {
                    StPptnR stPptnR = rainfall24Data.get(i);
                    Double aDouble = stPptnR.getDrp();
                    if (aDouble != null) {
                        if (aDouble > 30) {
                            index2++;
                        }
                        if (aDouble >= 0.1 && aDouble < 10) {
                            index3++;
                        }
                        if (aDouble >= 10 && aDouble < 25) {
                            index4++;
                        }
                        if (aDouble >= 25 && aDouble < 50) {
                            index5++;
                        }
                        if (aDouble >= 50 && aDouble < 100) {
                            index6++;
                        }
                        if (aDouble >= 100 && aDouble < 250) {
                            index7++;
                        }
                        if (aDouble >= 250) {
                            index8++;
                        }
                    }
                }

                //获取鹿城区的所有水库、河道、堰闸、潮位测站
                List<AttStBase> rainLevels = giveAnAlarmMapper.getwaterLevels();
                Map<String, List<AttStBase>> type = rainLevels.stream().collect(Collectors.groupingBy(AttStBase::getStType));

                //水库水情
                List<Map<String, Object>> skWaterList = new ArrayList<>();
                List<String> skTables = echoWaterRainTableByDateInternal(StationType.RESERVOIR.table(), start, end);
                //查询水库绑定的主测站，已及安全鉴定结论
                List<AttResBaseVo> skStBases = giveAnAlarmMapper.getSkListStcd();
                List<String> skCodes = skStBases.stream().map(AttResBaseVo::getStcd).collect(Collectors.toList());
                List<Map<String, Object>> sksqList = giveAnAlarmMapper.getsksqList(skTables, skCodes, startTime, endTime);

                int skXh = 1, skCjCount = 0, skSfCj = 0, notOneClass = 0;
                if (sksqList != null && sksqList.size() > 0) {
                    Map<Object, List<Map<String, Object>>> listMap = sksqList.stream().collect(Collectors.groupingBy(m -> m.get("STCD")));
                    for (AttResBaseVo skStBase : skStBases) {
                        Map<String, Object> stringObjectMap = new HashMap<>();
                        String resCode = skStBase.getResCode();
                        String resName = skStBase.getResName();
                        String stcd = skStBase.getStcd();
                        String safetyConclusion = skStBase.getSafetyConclusion();
                        Double flLowLimLevCap = skStBase.getFlLowLimLevCap();
                        Double level = skStBase.getLimitLevel();
                        Double normWatLev = skStBase.getNormWatLev();
                        Double normPoolStagCap = skStBase.getNormPoolStagCap();
                        Double yphoonFloodLlimitWaterLevel = skStBase.getYphoonFloodLlimitWaterLevel();
                        List<Map<String, Object>> list = listMap.get(stcd);
                        //是否超警
                        boolean isWarning = false;
                        //当前水位
                        Double waterLevel = null;
                        //最高水位的数据
                        Map<String, Object> objectMap = null;
                        if (list != null && list.size() > 0) {
                            Map<String, Object> map1 = list.stream().max(Comparator.comparing(o -> (Date) o.get("TM"))).get();
                            waterLevel = Double.valueOf(String.valueOf(map1.get("rz")));
                            for (Map<String, Object> map : list) {
                                Double o = Double.valueOf(String.valueOf(map.get("rz")));
                                if (level != null && level > 0 && level < o) {
                                    skCjCount++;
                                    isWarning = true;
                                }
                                if (objectMap == null || Double.valueOf(String.valueOf(objectMap.get("rz"))) < o) {
                                    objectMap = map;
                                }
                            }
                        }
                        if (isWarning) {
                            //超警的测站
                            skSfCj++;
                            //计算超警数据是多少m
                            stringObjectMap.put("cjMax", objectMap != null && level != null && level > 0 ? Double.valueOf(String.valueOf(objectMap.get("Z"))) - level : 0);
                        } else {
                            stringObjectMap.put("cjMax", 0);
                        }
                        //当前蓄水量
                        Float y = null;
                        if (waterLevel != null){
                            y = getXByY(Float.parseFloat(String.valueOf(waterLevel)), resCode);
                        }
                        stringObjectMap.put("xh", skXh);
                        stringObjectMap.put("name", resName);
                        stringObjectMap.put("dqsw",waterLevel !=null ? waterLevel : "/");
                        stringObjectMap.put("max", objectMap != null ? objectMap.get("rz") : "/");
                        stringObjectMap.put("tm", objectMap != null ? objectMap.get("TM") : "/");
                        stringObjectMap.put("zcsw", normWatLev!=null ? normWatLev : "/");
                        stringObjectMap.put("zckr", normPoolStagCap!=null ? normPoolStagCap : "/");
                        stringObjectMap.put("txsw", yphoonFloodLlimitWaterLevel!=null ? new BigDecimal(yphoonFloodLlimitWaterLevel).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() : "/");
                        stringObjectMap.put("dqxsl", y != null ? y : "/");
                        stringObjectMap.put("xsl", y != null && flLowLimLevCap != null?HydrologyUtils.calculateStorageRatio(new BigDecimal(y),new BigDecimal(flLowLimLevCap))+"%" : "/");
//                        stringObjectMap.put("aqjd", safetyConclusion);
                        //计算纳蓄能力
                        Float rz = getXByY(Float.parseFloat(String.valueOf(objectMap.get("rz"))), resCode);
                        BigDecimal subtract = new BigDecimal(flLowLimLevCap).subtract(new BigDecimal(rz)).setScale(1, BigDecimal.ROUND_HALF_UP);
                        if (subtract.compareTo(new BigDecimal(0)) > 0) {
                            stringObjectMap.put("nxnl", subtract);
                        } else {
                            stringObjectMap.put("nxnl", 0);
                        }
                        //水位数据5分钟一条，超警时长(h)是 cjCount * 5 / 60
//                        stringObjectMap.put("hours", new BigDecimal(skCjCount).divide(new BigDecimal(12), 1, BigDecimal.ROUND_HALF_UP));
                        skXh++;
                        if (!safetyConclusion.contains("一类")) {
                            //非一类坝
                            notOneClass++;
                        }
                        //查询
                        skWaterList.add(stringObjectMap);
                    }
                }

                //江河水情
                List<Map<String, Object>> jhWaterList = new ArrayList<>();
                List<String> hdTables = echoWaterRainTableByDateInternal(StationType.RIVER.table(), start, end);
                List<String> hdCodes = Arrays.asList("01653","01332","30032903","06731","010111","010112","01422","02074","06721","06722","06723","06724","06726","06729","06730","06732","30032900","30032908","30032909");
                List<AttStBase> hdStBases = rainLevels.stream().filter(o->hdCodes.contains(o.getStCode())).collect(Collectors.toList());
//                List<AttStBase> hdStBases = type.get(StationType.RIVER.type());
//                List<String> hdCodes = hdStBases.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                List<Map<String, Object>> hdsqList = giveAnAlarmMapper.getHdsqList(hdTables, hdCodes, startTime, endTime);

                int hdXh = 1, hdSfCj = 0;
                if (hdsqList != null && hdsqList.size() > 0) {
                    Map<Object, List<Map<String, Object>>> listMap = hdsqList.stream().collect(Collectors.groupingBy(m -> m.get("STCD")));
                    for (AttStBase hdStBase : hdStBases) {
                        Map<String, Object> stringObjectMap = new HashMap<>();
                        String stName = hdStBase.getStName();
                        String stCode = hdStBase.getStCode();
                        Double level = hdStBase.getWarningLevel();
                        if (listMap.containsKey(stCode)){
                            List<Map<String, Object>> list = listMap.get(stCode);
                            //是否超警
                            boolean isWarning = false;
                            //最高水位的数据
                            Map<String, Object> objectMap = null;
                            int hdCjCount = 0;
                            //当前水位
                            Double waterLevel = 0.0;
                            if (list != null && list.size() > 0) {
                                Map<String, Object> map1 = list.stream().max(Comparator.comparing(o -> (Date) o.get("TM"))).get();
                                waterLevel = Double.valueOf(String.valueOf(map1.get("Z")));
                                for (Map<String, Object> map : list) {
                                    Double o = Double.valueOf(String.valueOf(map.get("Z")));
                                    if (level != null && level < waterLevel) {
                                        hdCjCount++;
                                        isWarning = true;
                                    }
                                    if (objectMap == null || Double.valueOf(String.valueOf(objectMap.get("Z"))) < o) {
                                        objectMap = map;
                                    }
                                }
                            }
                            if (isWarning) {
                                //超警的测站
                                hdSfCj++;
                                //计算超警数据是多少m
                                stringObjectMap.put("cjMax", objectMap != null && level != null && level > 0 ? Double.valueOf(String.valueOf(objectMap.get("Z"))) - level : 0);
                            } else {
                                stringObjectMap.put("cjMax", 0);
                            }
                            stringObjectMap.put("xh", hdXh);
                            stringObjectMap.put("name", stName);
                            stringObjectMap.put("max", objectMap != null ? objectMap.get("Z") : "/");
                            stringObjectMap.put("tm", objectMap != null ? objectMap.get("TM") : "/");
                            stringObjectMap.put("jjsw",level !=null ? level : "/");

                            stringObjectMap.put("dqsw",waterLevel !=null ? waterLevel : "/");
                            stringObjectMap.put("cjj",level != null && (waterLevel - level) > 0? new BigDecimal(waterLevel - level + "").setScale(2,BigDecimal.ROUND_HALF_UP) : "0" );
                            //水位数据5分钟一条，超警时长(h)是 cjCount * 5 / 60F
//                        stringObjectMap.put("hours", new BigDecimal(hdCjCount).divide(new BigDecimal(12), 1, BigDecimal.ROUND_HALF_UP));
                            hdXh++;
                            //查询
                            jhWaterList.add(stringObjectMap);
                        }else {
                            stringObjectMap.put("xh", hdXh);
                            stringObjectMap.put("name", stName);
                            stringObjectMap.put("max", "/");
                            stringObjectMap.put("tm", "/");
                            stringObjectMap.put("jjsw","/");

                            stringObjectMap.put("dqsw","/");
                            stringObjectMap.put("cjj","/" );
                            stringObjectMap.put("cjMax", 0);
                            hdXh++;
                            //查询
                            jhWaterList.add(stringObjectMap);
                        }

                    }
                }

                //潮位水情
                List<Map<String, Object>> cwWaterList = new ArrayList<>();
                List<String> cwTables = echoWaterRainTableByDateInternal(StationType.TIDE.table(), start, end);
                //业主说固定测站
                List<String> cwCodes = Arrays.asList("06896","01421","014835");
                List<AttStBase> cwStBases = rainLevels.stream().filter(o->cwCodes.contains(o.getStCode())).collect(Collectors.toList());
//                List<AttStBase> cwStBases = type.get(StationType.TIDE.type());
//                List<String> cwCodes = cwStBases.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                List<Map<String, Object>> cwsqList = giveAnAlarmMapper.getCwsqList(cwTables, cwCodes, startTime, endTime);
                List<String> hdCodes1 = Arrays.asList("08052","06727","30032912");
                List<AttStBase> htStBases1 = rainLevels.stream().filter(o->hdCodes1.contains(o.getStCode())).collect(Collectors.toList());
                List<Map<String, Object>> hdsqList1 = giveAnAlarmMapper.getHdsqList(hdTables, hdCodes1, startTime, endTime);
                int cwXh = 1, cwSfCj = 0;
                if (hdsqList1 != null && hdsqList1.size() > 0) {
                    Map<Object, List<Map<String, Object>>> listMap = hdsqList1.stream().collect(Collectors.groupingBy(m -> m.get("STCD")));
                    for (AttStBase hdStBase : htStBases1) {
                        int cwCjCount = 0;
                        Map<String, Object> stringObjectMap = new HashMap<>();
                        String stName = hdStBase.getStName();
                        String stCode = hdStBase.getStCode();
                        Double level = hdStBase.getWarningLevel();
                        if (listMap.containsKey(stCode)){
                            List<Map<String, Object>> list = listMap.get(stCode);
                            //是否超警
                            boolean isWarning = false;
                            //最高水位的数据
                            Map<String, Object> objectMap = null;
                            Map<String, Object> minMap = null;
                            if (list != null && list.size() > 0) {
                                for (Map<String, Object> map : list) {
                                    Double o = Double.valueOf(String.valueOf(map.get("Z")));
                                    if (level != null && level > 0 && level < o) {
                                        cwCjCount++;
                                        isWarning = true;
                                    }
                                    if (objectMap == null || Double.valueOf(String.valueOf(objectMap.get("Z"))) < o) {
                                        objectMap = map;
                                    }
                                    if (minMap == null || Double.valueOf(String.valueOf(minMap.get("Z"))) > o) {
                                        minMap = map;
                                    }
                                }
                            }
                            if (isWarning) {
                                //超警的测站
                                cwSfCj++;
                                //计算超警数据是多少m
                                stringObjectMap.put("cjMax", objectMap != null && level != null && level > 0 ? Double.valueOf(String.valueOf(objectMap.get("Z"))) - level : 0);
                            } else {
                                stringObjectMap.put("cjMax", 0);
                            }
                            stringObjectMap.put("xh", cwXh);
                            stringObjectMap.put("name", stName);
                            stringObjectMap.put("max", objectMap != null ? objectMap.get("Z") : "/");
                            stringObjectMap.put("tm", objectMap != null ? objectMap.get("TM") : "/");
                            stringObjectMap.put("min", minMap != null ? minMap.get("Z") : "/");
                            stringObjectMap.put("minTime", minMap != null ? minMap.get("TM") : "/");
                            //水位数据5分钟一条，超警时长(h)是 cjCount * 5 / 60
//                        stringObjectMap.put("hours", new BigDecimal(cwCjCount).divide(new BigDecimal(12), 1, BigDecimal.ROUND_HALF_UP));
                            cwXh++;
                            //查询
                            cwWaterList.add(stringObjectMap);
                        }else {
                            stringObjectMap.put("xh", cwXh);
                            stringObjectMap.put("name", stName);
                            stringObjectMap.put("max",  "/");
                            stringObjectMap.put("tm", "/");
                            stringObjectMap.put("min", "/");
                            stringObjectMap.put("minTime", "/");
                            stringObjectMap.put("cjMax", 0);
                            cwXh++;
                            //查询
                            cwWaterList.add(stringObjectMap);
                        }
                    }
                }
                if (cwsqList != null && cwsqList.size() > 0) {
                    Map<Object, List<Map<String, Object>>> listMap = cwsqList.stream().collect(Collectors.groupingBy(m -> m.get("STCD")));
                    for (AttStBase cwStBase : cwStBases) {
                        int cwCjCount = 0;
                        Map<String, Object> stringObjectMap = new HashMap<>();
                        String stName = cwStBase.getStName();
                        String stCode = cwStBase.getStCode();
                        Double level = cwStBase.getWarningLevel();
                        if (listMap.containsKey(stCode)){
                            List<Map<String, Object>> list = listMap.get(stCode);
                            //是否超警
                            boolean isWarning = false;
                            //最高水位的数据
                            Map<String, Object> objectMap = null;
                            Map<String, Object> minMap = null;
                            if (list != null && list.size() > 0) {
                                for (Map<String, Object> map : list) {
                                    Double o = Double.valueOf(String.valueOf(map.get("TDZ")));
                                    if (level != null && level > 0 && level < o) {
                                        cwCjCount++;
                                        isWarning = true;
                                    }
                                    if (objectMap == null || Double.valueOf(String.valueOf(objectMap.get("TDZ"))) < o) {
                                        objectMap = map;
                                    }
                                    if (minMap == null || Double.valueOf(String.valueOf(minMap.get("TDZ"))) > o) {
                                        minMap = map;
                                    }
                                }
                            }
                            if (isWarning) {
                                //超警的测站
                                cwSfCj++;
                                //计算超警数据是多少m
                                stringObjectMap.put("cjMax", objectMap != null && level != null && level > 0 ? Double.valueOf(String.valueOf(objectMap.get("TDZ"))) - level : 0);
                            } else {
                                stringObjectMap.put("cjMax", 0);
                            }
                            stringObjectMap.put("xh", cwXh);
                            stringObjectMap.put("name", stName);
                            stringObjectMap.put("max", objectMap != null ? objectMap.get("TDZ") : "/");
                            stringObjectMap.put("tm", objectMap != null ? objectMap.get("TM") : "/");
                            stringObjectMap.put("min", minMap != null ? minMap.get("TDZ") : "/");
                            stringObjectMap.put("minTime", minMap != null ? minMap.get("TM") : "/");
                            //水位数据5分钟一条，超警时长(h)是 cjCount * 5 / 60
//                        stringObjectMap.put("hours", new BigDecimal(cwCjCount).divide(new BigDecimal(12), 1, BigDecimal.ROUND_HALF_UP));
                            cwXh++;
                            //查询
                            cwWaterList.add(stringObjectMap);
                        }else {
                            stringObjectMap.put("xh", cwXh);
                            stringObjectMap.put("name", stName);
                            stringObjectMap.put("max",  "/");
                            stringObjectMap.put("tm", "/");
                            stringObjectMap.put("min", "/");
                            stringObjectMap.put("minTime", "/");
                            stringObjectMap.put("cjMax", 0);
                            cwXh++;
                            //查询
                            cwWaterList.add(stringObjectMap);
                        }

                    }
                }

                //水闸工情
                List<Map<String, Object>> szWaterList = new ArrayList<>();
                List<String> szTables = echoWaterRainTableByDateInternal(StationType.GATE.table(), start, end);
                //业主说固定测站
                List<String> szCodes = Arrays.asList("101","2021101105","2021101106","100","2021101104","102","2021101101","2021101103");
                List<AttStBase> szStBases = rainLevels.stream().filter(o->szCodes.contains(o.getStCode())).collect(Collectors.toList());
//                List<AttStBase> cwStBases = type.get(StationType.GATE.type());
//                List<String> szCodes = szStBases.stream().map(AttStBase::getStCode).collect(Collectors.toList());
                List<Map<String, Object>> szgqList = giveAnAlarmMapper.getSzsqList(szTables, szCodes, startTime, endTime);

                int szXh = 1, sfkzCount = 0;
                double ljphl = 0d;
                if (szgqList != null && szgqList.size() > 0) {
                    Map<Object, List<Map<String, Object>>> listMap = szgqList.stream().collect(Collectors.groupingBy(m -> m.get("STCD")));
                    for (AttStBase szStBase : szStBases) {
                        Map<String, Object> stringObjectMap = new HashMap<>();
                        String stName = szStBase.getStName();
                        String stCode = szStBase.getStCode();
                        //闸门开度阈值
                        Double gateOpenThreshold = szStBase.getGateOpenThreshold();
                        if (listMap.containsKey(stCode)){
                            List<Map<String, Object>> list = listMap.get(stCode);
                            //最高水位的数据
                            Map<String, Object> objectMap = null;
                            //是否开闸
                            boolean isKz = false;
                            double ljphlStcd = 0d;
                            int szKqCount = 0;
                            if (list != null && list.size() > 0) {
                                for (Map<String, Object> map : list) {
                                    Double upz = Double.valueOf(String.valueOf(map.get("UPZ")).equals("null") ? "0" : String.valueOf(map.get("UPZ")));
                                    //开度
                                    String ovs = String.valueOf(map.get("OVS"));
                                    //流量
                                    String ll = String.valueOf(map.get("LL"));
                                    if (gateOpenThreshold != null && gateOpenThreshold > 0 && StringUtils.isNotBlank(ovs)) {
                                        String[] split = ovs.split(",");
                                        for (String s : split) {
                                            Double aDouble = Double.valueOf("null".equals(s) ? "0" : s);
                                            if (aDouble != null && aDouble > gateOpenThreshold) {
                                                szKqCount++;
                                                isKz = true;
                                                //排洪量，5分钟一条数据，计算5分钟的排洪量，最后统计所有开闸时候的排洪量
                                                if (StringUtils.isNotBlank(ll)) {
                                                    String[] split1 = ll.split(",");
                                                    for (String s1 : split1) {
                                                        Double aDouble1 = Double.valueOf("null".equals(s1) ? "0" : s1);
                                                        if (aDouble1 != null) {
                                                            //万立方米
                                                            ljphlStcd += aDouble1 * 5 * 60 / 10000;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (objectMap == null || Double.valueOf("null".equals(String.valueOf(objectMap.get("UPZ"))) ? "0" : String.valueOf(objectMap.get("UPZ"))) < upz) {
                                        objectMap = map;
                                    }
                                }
                                if (isKz) {
                                    sfkzCount++;
                                    ljphl += ljphlStcd;
                                }
                                stringObjectMap.put("xh", szXh);
                                stringObjectMap.put("name", stName);
                                stringObjectMap.put("max", objectMap != null ? objectMap.get("UPZ") : "/");
                                stringObjectMap.put("tm", objectMap != null && objectMap.get("UPZ") != null ? objectMap.get("TM") : "/");
                                //累计排洪量
                                stringObjectMap.put("ljphl", new BigDecimal(ljphlStcd).setScale(2, BigDecimal.ROUND_HALF_UP));
                                //水位数据5分钟一条，开闸时长(h)是 szKqCount * 5 / 60
                                stringObjectMap.put("hours", new BigDecimal(szKqCount).divide(new BigDecimal(12), 1, BigDecimal.ROUND_HALF_UP));
                                szXh++;
                                //查询
                                szWaterList.add(stringObjectMap);
                            }
                        }else {
                            stringObjectMap.put("xh", szXh);
                            stringObjectMap.put("name", stName);
                            stringObjectMap.put("max", "/");
                            stringObjectMap.put("tm", "/");
                            //累计排洪量
                            stringObjectMap.put("ljphl", "/");
                            //水位数据5分钟一条，开闸时长(h)是 szKqCount * 5 / 60
                            stringObjectMap.put("hours","/");
                            szXh++;
                            //查询
                            szWaterList.add(stringObjectMap);
                        }

                    }
                }


                //准备数据
                String format = DateUtil.format(new Date(), "yyyy-MM-dd HH");
                Map<String, Object> params = new HashMap<>();
//                params.put("qxyb",qxyb);
                params.put("index", "");
                params.put("year", format.substring(0, 4));
                params.put("month", format.substring(5, 7));
                params.put("day", format.substring(8, 10));
                params.put("hour", format.substring(11, 13));
                //雨情
                params.put("rainfall", rainfall24);
                params.put("rainfallone", rainfallone);
                params.put("rainfall1", rainfall_ten_day);
                params.put("rainfall2", rainfall72);
                params.put("index2", index2);
                params.put("proprotion", rainfall24Data.size() > 0 && index2 > 0 ? new BigDecimal(index2).multiply(new BigDecimal(100))
                        .divide(new BigDecimal(rainfall24Data.size()), 1, BigDecimal.ROUND_HALF_UP).toString() + "%" : "0%");
                params.put("stnm", rainfall.getStationName());
                params.put("rainfall4", rainfall.getDrp());
//                params.put("adnm", maxMyl.get().get("xzqh"));
//                params.put("rainfall5", maxMyl.get().get("myl"));
//                params.put("index3", index3);
//                params.put("index4", index4);
//                params.put("index5", index5);
//                params.put("index6", index6);
//                params.put("index7", index7);
//                params.put("index8", index8);

                //水库水情
                List<Map<String, Object>> skCjMax = skWaterList.stream().sorted(Comparator.comparing(m -> Double.valueOf(String.valueOf(m.get("cjMax"))))).collect(Collectors.toList());
                Map<String, Object> skObjectMap = new HashMap<>();
                if (skCjMax != null && skCjMax.size() > 0) {
                    //超警戒水位最高的测站
                    skObjectMap = skCjMax.get(skCjMax.size() - 1);
                }
                params.put("index9", skSfCj);
                params.put("skName", skObjectMap.containsKey("name") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("name") : "/");
                params.put("skTm", skObjectMap.containsKey("tm") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("tm") : "/");
                params.put("waterLevel", skObjectMap.containsKey("max") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("max") : "/");
                params.put("waterLevel2", skObjectMap.containsKey("cjMax") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("cjMax") : "/");
//                params.put("index10", notOneClass);

                //江河水情
                List<Map<String, Object>> hdCjMax = jhWaterList.stream().sorted(Comparator.comparing(m -> Double.valueOf(String.valueOf(m.get("cjMax"))))).collect(Collectors.toList());
                Map<String, Object> hdObjectMap = new HashMap<>();
                if (hdCjMax != null && hdCjMax.size() > 0) {
                    //超警戒水位最高的测站
                    hdObjectMap = hdCjMax.get(hdCjMax.size() - 1);
                }
                params.put("index11", hdSfCj);
                params.put("jhName", hdObjectMap.containsKey("name") && Double.valueOf(String.valueOf(hdObjectMap.get("cjMax"))) > 0 ? hdObjectMap.get("name") : "/");
                params.put("jhTm", hdObjectMap.containsKey("tm") && Double.valueOf(String.valueOf(hdObjectMap.get("cjMax"))) > 0 ? hdObjectMap.get("tm") : "/");
                params.put("waterLevel3", hdObjectMap.containsKey("max") && Double.valueOf(String.valueOf(hdObjectMap.get("cjMax"))) > 0 ? hdObjectMap.get("max") : "/");
                params.put("waterLevel4", hdObjectMap.containsKey("cjMax") && Double.valueOf(String.valueOf(hdObjectMap.get("cjMax"))) > 0 ? hdObjectMap.get("cjMax") : "/");

                //潮位水情
                List<Map<String, Object>> cwCjMax = cwWaterList.stream().sorted(Comparator.comparing(m -> Double.valueOf(String.valueOf(m.get("cjMax"))))).collect(Collectors.toList());
                Map<String, Object> cwObjectMap = new HashMap<>();
                if (cwCjMax != null && cwCjMax.size() > 0) {
                    //超警戒水位最高的测站
                    cwObjectMap = cwCjMax.get(cwCjMax.size() - 1);
                }
                params.put("index12", cwSfCj);
                params.put("cwName", cwObjectMap.containsKey("name") && Double.valueOf(String.valueOf(cwObjectMap.get("cjMax"))) > 0 ? cwObjectMap.get("name") : "/");
                params.put("cwTm", cwObjectMap.containsKey("tm") && Double.valueOf(String.valueOf(cwObjectMap.get("cjMax"))) > 0 ? cwObjectMap.get("tm") : "/");
                params.put("waterLevel5", cwObjectMap.containsKey("max") && Double.valueOf(String.valueOf(cwObjectMap.get("cjMax"))) > 0 ? cwObjectMap.get("max") : "/");
                params.put("waterLevel6", cwObjectMap.containsKey("cjMax") && Double.valueOf(String.valueOf(cwObjectMap.get("cjMax"))) > 0 ? cwObjectMap.get("cjMax") : "/");

                //水闸工情
                List<Map<String, Object>> szHourMax = szWaterList.stream().filter(o->!"/".equals(String.valueOf(o.get("hours")))).sorted(Comparator.comparing(m -> Double.valueOf(String.valueOf(m.get("hours"))))).collect(Collectors.toList());
                Map<String, Object> szObjectMap = new HashMap<>();
                if (szHourMax != null && szHourMax.size() > 0) {
                    //超警戒水位最高的测站
                    szObjectMap = szHourMax.get(szHourMax.size() - 1);
                }
                params.put("index13", sfkzCount);
                params.put("phl", new BigDecimal(ljphl).setScale(2, BigDecimal.ROUND_HALF_UP));
                params.put("szName", szObjectMap.containsKey("name") && Double.valueOf(String.valueOf(szObjectMap.get("hours"))) > 0 ? szObjectMap.get("name") : "/");
                params.put("sc", szObjectMap.containsKey("hours") && Double.valueOf(String.valueOf(szObjectMap.get("hours"))) > 0 ? szObjectMap.get("hours") : "/");

                //所有的列表
                params.put("mylList", mylList);
                params.put("byList", byList);
                params.put("skWaterList", skWaterList);
                params.put("jhWaterList", jhWaterList);
                params.put("cwWaterList", cwWaterList);
                params.put("szWaterList", szWaterList);

//                InputStream is = this.getClass().getResourceAsStream("/office/防汛防台简报.docx");
                InputStream is = this.getClass().getResourceAsStream("/office/防汛防台简报模板.docx");
                MyXWPFDocument doc = new MyXWPFDocument(is);
                WordExportUtil.exportWord07(doc, params);
                String filename = "防汛防台简报.docx";
                //response.setHeader("content-disposition","attachment;filename*=utf-8''"+URLEncoder.encode(filename, "utf-8"));
                //response.setContentType("application/msword;charset=UTF-8");
                //word.write(response.getOutputStream());
                //上传到文件服务器，并把文件id和时间保存到数据库文件中
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                doc.write(bos);
                MultipartFile multipartFile = new ByteArrayMultipartFile(bos.toByteArray(), filename, "application/msword;charset=UTF-8");
                LinkedHashMap resp = authenticationClient.upload(multipartFile);
                JSONObject date = JSONObject.parseObject(JSONObject.toJSONString(resp, SerializerFeature.WriteMapNullValue));
                String fileId = date.getJSONObject("data").getString("id");
                //保存到数据库
                giveAnAlarmMapper.saveResponseLevel(UUIDUtils.getUUID32(), endTime, fileId,"1");
            }
        } catch (Exception e) {
            log.info("异常-----------------------" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 获取时间范围内需要查询的表名称（分表）
     *
     * @param tablePrefix
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<String> echoWaterRainTableByDateInternal(String tablePrefix, Date start, Date end) {
        if (StringUtils.isBlank(tablePrefix) || start == null || end == null) {
            return Collections.emptyList();
        }
        LocalDate startTemp = DateUtils.date2LocalDate(start);
        LocalDate endTemp = DateUtils.date2LocalDate(end);
        LinkedHashSet<String> sequenceSet = new LinkedHashSet<>();
        do {
            String table = tablePrefix + startTemp.format(yyyyMM);
            if (StringUtils.isNotBlank(this.giveAnAlarmMapper.checkTableExist(table))) {
                sequenceSet.add(table);
            }
            startTemp = startTemp.plusDays(1);
        } while (startTemp.isBefore(endTemp));
        return new ArrayList<>(sequenceSet);
    }

    @Override
    public Object floodPreventionBulletinApp() {
        //时间范围
        Date end = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(end);
        c.add(Calendar.HOUR_OF_DAY, -24);
        Date start = c.getTime();
        String endTime = DateUtil.format(end, "yyyy-MM-dd HH:00:00");
        String startTime = DateUtil.format(start, "yyyy-MM-dd HH:00:00");

        CommonResult commonResult = floodDroughtDefenseController.rainfall(null, null, null);
        JSONObject data = (JSONObject) commonResult.getData();
        double rainfall24 = data.getDoubleValue("rainfall_24");
        double rainfall72 = data.getDoubleValue("rainfall_72");
        JSONArray dataJSONArray = data.getJSONArray("rainfall_24_data");
        List<StPptnR> rainfall24Data = dataJSONArray.toJavaList(StPptnR.class);
        List<StPptnR> drp = rainfall24Data.stream().sorted(Comparator.comparing(StPptnR::getDrp).reversed()).collect(Collectors.toList());
        //查詢行政区划
        List<AttAdcdBase> adcdBases = giveAnAlarmMapper.getAdcdList();
        Map<String, String> adnmMap = adcdBases.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        for (int i = 0; i < drp.size(); i++) {
            StPptnR jsonObject = drp.get(i);
            drp.get(i).setAreaName(adnmMap.get(jsonObject.getAreaCode()));
        }
        StPptnR rainfall = drp.get(0);
        Map<String, List<StPptnR>> areaName = drp.stream().filter(m -> StringUtils.isNotBlank(m.getAreaName())).collect(Collectors.groupingBy(StPptnR::getAreaName));

        AtomicInteger ylxh = new AtomicInteger(0);
        AtomicReference<HashMap<String, Object>> maxMyl = new AtomicReference<>(new HashMap<>());
        areaName.forEach((k, v) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("xzqh", k);
            double myl = 0d;
            for (StPptnR jsonObject : v) {
                Double drp1 = jsonObject.getDrp();
                myl += drp1;
            }
            BigDecimal divide = new BigDecimal(myl).divide(new BigDecimal(v.size()), 1, BigDecimal.ROUND_HALF_UP);
            map.put("myl", divide);
            if (maxMyl.get().size() > 0) {
                BigDecimal o = (BigDecimal) maxMyl.get().get("myl");
                if (divide.compareTo(o) > 0) {
                    maxMyl.set(map);
                }
            } else {
                maxMyl.set(map);
            }
        });
        int index = 0;
        for (int i = 0; i < rainfall24Data.size(); i++) {
            StPptnR stPptnR = rainfall24Data.get(i);
            Double aDouble = stPptnR.getDrp();
            if (aDouble != null) {
                if (aDouble > 30) {
                    index++;
                }
            }
        }

        String format = DateUtil.format(new Date(), "yyyy-MM-dd HH");
        Map<String, Object> params = new HashMap<>();
        params.put("day", format.substring(8, 10));
        params.put("hour", format.substring(11, 13));
        //雨情
        params.put("24Hour", rainfall24);
        params.put("72Hour", rainfall72);
        params.put("surpass30", index);
        params.put("proprotion", rainfall24Data.size() > 0 && index > 0 ? new BigDecimal(index).multiply(new BigDecimal(100))
                .divide(new BigDecimal(rainfall24Data.size()), 1, BigDecimal.ROUND_HALF_UP).toString() + "%" : "0%");
        params.put("rainMaxStnm", rainfall.getStationName());
        params.put("rainMax1", rainfall.getDrp());
        params.put("rainMaxAdnm", maxMyl.get().get("xzqh"));
        params.put("rainMax2", maxMyl.get().get("myl"));
        return params;
    }

    @Override
    public void waterAndRainBriefing() {
        //时间范围
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> params = new HashMap<>();
        //现在的年，月，日，时
        params.put("year", now.getYear());
        params.put("month", now.getMonthValue());
        params.put("day", now.getDayOfMonth());
        params.put("hour", now.getHour());
        //上个月份
        now = now.minusMonths(1);
        params.put("month2", now.getMonthValue());

        //获取word模板
        InputStream is = this.getClass().getResourceAsStream("/office/鹿城区水旱灾害防御分指挥部简报.docx");
        try {
            MyXWPFDocument doc = new MyXWPFDocument(is);
            //获取word中所有图表对象
            List<XWPFChart> charts = doc.getCharts();
            //获取第一个多系列柱状图
            XWPFChart barChar1 = charts.get(0);
            //获取第二个多系列柱状图
            XWPFChart barChar2 = charts.get(1);
            //获取第二个多系列折线图对象
            XWPFChart multiLineChar = charts.get(2);

            setRainData(barChar1,barChar2,params);
            summaryOfWater(multiLineChar, params);
            WordExportUtil.exportWord07(doc, params);
            String filename = "鹿城区水旱灾害防御分指挥部简报.docx";
/*            response.setHeader("content-disposition","attachment;filename*=utf-8''"+URLEncoder.encode(filename, "utf-8"));
            response.setContentType("application/msword;charset=UTF-8");
            doc.write(response.getOutputStream());*/
            //上传到文件服务器，并把文件id和时间保存到数据库文件中
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            doc.write(bos);
            MultipartFile multipartFile = new ByteArrayMultipartFile(bos.toByteArray(), filename, "application/msword;charset=UTF-8");
            LinkedHashMap resp = authenticationClient.upload(multipartFile);
            JSONObject date = JSONObject.parseObject(JSONObject.toJSONString(resp, SerializerFeature.WriteMapNullValue));
            String fileId = date.getJSONObject("data").getString("id");
            //保存到数据库
            giveAnAlarmMapper.saveResponseLevel(UUIDUtils.getUUID32(), DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"), fileId,"2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  > 30mm 明显降雨   > 0 降水日数
     * {{month}}月份以来，本区域降水日数仅{{day1}}天。其中{{day2}}日我区出现较明显降水，最大累计雨量出现在{{rainNameMax}}站{{rainCountMax}}毫米。
     * 单站累计雨量前三为{{rainNameOne}}站{{rainCountOne}}毫米，{{rainNameTwo}}站{{rainCountTwo}}毫米，{{rainNameThree}}站{{rainCountThree}}毫米。
     * {{year}}年1月---{{month2}}月（统计到上个月31日止），鹿城区面平均降水量{{rainAvg}}毫米，比常年同期降水量{{pdps}}{{bfb}}%（常年同期为{{cnRainAvg}}毫米），
     * 其中：{{month2}}月份鹿城区面雨量 {{rainMyl}}毫米，比常年同期平均面雨量{{pdps2}}{{bfb2}}%（常年同期平均面雨量为{{cnRainMyl}}毫米）。
     * 2022年当月1日—{{day}}日（统计当月截止日），鹿城区面雨量{{dyRainMyl}}毫米。
     * @param params
     */
    public void setRainData(XWPFChart barChar1,XWPFChart barChar2,Map<String,Object> params) throws Exception {

        //上个月初   上个月结束 的时间
        Map<String,String> lastMonthTime = com.ygkj.big.screen.utils.DateUtil.getLastMonthTime();
        String startLastDate = lastMonthTime.get("startDate");
        String endLastDate = lastMonthTime.get("endDate");
        //当月的开始和结束时间
        Map<String,String> monthTime = com.ygkj.big.screen.utils.DateUtil.getMonthTime();
        String startDate = monthTime.get("startDate");
        String endDate = monthTime.get("endDate");
        //本年开始时间
        String startYearDate =  startDate.substring(0, 4) + "-01-01 00:00:00";
        //上个月是12月份的时候取本月的本年1月~1月的数据比较，其他时候是取本年1月~本年上个月的数据比较
        Integer month2 = Integer.valueOf(endLastDate.substring(5, 7));
        String endMonth2Date = endLastDate;
        if (Integer.valueOf(startDate.substring(5,7)) == 1){
            month2 = 1;
            endMonth2Date = endDate;
        }
        //month2 对应的那个月的第一天
        String startMonth2Date = endMonth2Date.substring(0, 8) + "-01 00:00:00";

        // 本月 降雨  |  降雨量、测站名称       今年1月 - 上月31号，平均降雨量（总降雨量/测站数量） 常年同期。   上一个月面雨量 加起来即可  常年同月雨量加起来
        // 根据起止时间查询 降雨量列表            根据起止时间统计总降雨量
        //上月降雨量合   常年同期降水量合  按测站拆分   参数  起止日期  返回 按测站统计雨量值
        //查询所有雨量测站
        List<AttStBase> rainStationAll = giveAnAlarmMapper.getRainStationAll();
        List<String> stCodes = rainStationAll.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        //转换名字备用
        Map<String, String> names = rainStationAll.stream().collect(Collectors.toMap(AttStBase::getStCode, AttStBase::getStName));

        //获取本月的表名
        List<String> ylTables = echoWaterRainTableByDateInternal(StationType.RAINFALL.table(),DateUtil.parseDate("yyyy-MM-dd HH:mm:ss",startDate), DateUtil.parseDate("yyyy-MM-dd HH:mm:ss",endDate));
        //获取本年一月 到 endMonth2Date 时间的表名
        List<String> ylTables2 = echoWaterRainTableByDateInternal(StationType.RAINFALL.table(),DateUtil.parseDate("yyyy-MM-dd HH:mm:ss",startYearDate), DateUtil.parseDate("yyyy-MM-dd HH:mm:ss",endMonth2Date));

        //查询本月降雨天数
        Integer day1 = giveAnAlarmMapper.getJyDayCount(ylTables.get(0),stCodes);
        //查询本月有较明显降雨的天数，已经每天最大降雨的测站
        List<JSONObject> list = giveAnAlarmMapper.getJmxjyList(ylTables.get(0),stCodes);
        //拼接（其中x日我区出现较明显降水，最大累计雨量出现在x站x毫米）
        String str = "其中/日我区出现较明显降水，最大累计雨量出现在/站/毫米";
        if (list.size() > 0){
            StringBuffer stringBuffer = new StringBuffer();
            for (JSONObject jsonObject : list) {
                String stcd = jsonObject.getString("STCD");
                String tm = jsonObject.getString("TM");
                String drp = jsonObject.getString("DRP");
                String name = names.get(stcd);
                stringBuffer.append("其中").append(Integer.valueOf(tm.substring(8,10))).append("日我区出现较明显降水，最大累计雨量出现在").append(name).append(drp).append("毫米;");
            }

            if (stringBuffer.length() > 0){
                str = stringBuffer.toString().substring(0, stringBuffer.length()-1);
            }
        }
        //查询本月内每个站点的累计雨量值
        String rainNameOne = "/";
        String rainCountOne = "/";
        String rainNameTwo = "/";
        String rainCountTwo = "/";
        String rainNameThree = "/";
        String rainCountThree = "/";
        //本月降雨数据
        List<JSONObject> byList = giveAnAlarmMapper.getRainsSum(startDate, endDate, ylTables, stCodes);
        if (byList.size() > 1){
            String stcd = byList.get(0).getString("stcd");
            rainNameOne = names.get(stcd);
            rainCountOne = byList.get(0).getString("drp");
        }
        if (byList.size() > 2){
            String stcd = byList.get(1).getString("stcd");
            rainNameTwo = names.get(stcd);
            rainCountTwo = byList.get(1).getString("drp");
        }
        if (byList.size() > 3){
            String stcd = byList.get(2).getString("stcd");
            rainNameThree = names.get(stcd);
            rainCountThree = byList.get(2).getString("drp");
        }
        //当月面雨量
        String dyRainMyl = "/";
        if (byList != null && byList.size() > 0){
            BigDecimal bigDecimal = new BigDecimal(0);
            for (JSONObject jsonObject : byList) {
                BigDecimal drp = jsonObject.getBigDecimal("drp");
                bigDecimal = bigDecimal.add(drp);
            }
            dyRainMyl = bigDecimal.toString();
        }
        //查询本年一月 到 endMonth2Date 时间的雨量数据
        List<JSONObject> byList2 = giveAnAlarmMapper.getRainsSum(startYearDate, endMonth2Date, ylTables2, stCodes);
        //常年这个时间的数据
        List<JSONObject> cnList = giveAnAlarmMapper.getLastRainsSum(Integer.valueOf(startYearDate.substring(5,7)), Integer.valueOf(endMonth2Date.substring(5,7)), stCodes);
        //去年这个时间的数据
        List<JSONObject> qnList = giveAnAlarmMapper.getLastYearRainsSum(Integer.valueOf(startYearDate.substring(5,7)), Integer.valueOf(endMonth2Date.substring(5,7)), stCodes);
        String rainAvg = "/";
        String pdps = "偏多/偏少";
        String bfb = "/";
        String cnRainAvg = "/";
        String rainMyl = "/";
        String pdps2 = "偏多/偏少";
        String bfb2 = "/";
        String cnRainMyl = "/";
        BigDecimal rainAvgBig1 = new BigDecimal(0);
        BigDecimal rainAvgBig2 = new BigDecimal(0);
        if (byList2 != null && byList2.size() > 0){
            BigDecimal bigDecimal = new BigDecimal(0);
            for (JSONObject jsonObject : byList2) {
                BigDecimal drp = jsonObject.getBigDecimal("drp");
                bigDecimal = bigDecimal.add(drp);
            }
            rainAvgBig1 = bigDecimal.divide(new BigDecimal(byList2.size()), 1, BigDecimal.ROUND_HALF_UP);
            rainAvg = rainAvgBig1.toString();
        }
        if (qnList != null && qnList.size() > 0){
            BigDecimal bigDecimal = new BigDecimal(0);
            for (JSONObject jsonObject : qnList) {
                BigDecimal drp = jsonObject.getBigDecimal("drp");
                bigDecimal = bigDecimal.add(drp);
            }
            rainAvgBig2 = bigDecimal.divide(new BigDecimal(qnList.size()), 1, BigDecimal.ROUND_HALF_UP);
            cnRainAvg = rainAvgBig2.toString();
        }
        if (rainAvgBig2.compareTo(new BigDecimal(0)) > 0){
            //计算百分比， 查看偏多偏少
            BigDecimal divide = (rainAvgBig1.subtract(rainAvgBig2)).multiply(new BigDecimal(100)).divide(rainAvgBig2, 1, BigDecimal.ROUND_HALF_UP);
            if (divide.compareTo(new BigDecimal(0)) > 0){
                pdps = "偏多";
            } else {
                pdps = "偏少";
                divide = divide.abs();
            }
            bfb = divide.toString();
        }

        //查询startMonth2Date 到 endMonth2Date 时间的雨量数据 也就是month2 月份的数据
        List<JSONObject> byList3 = giveAnAlarmMapper.getRainsSum(startMonth2Date, endMonth2Date, ylTables2, stCodes);
        //常年这个时间的数据
        List<JSONObject> cnList1 = giveAnAlarmMapper.getLastRainsSum(Integer.valueOf(startMonth2Date.substring(5,7)), Integer.valueOf(endMonth2Date.substring(5,7)), stCodes);
        //去年这个时间的数据
        List<JSONObject> qnList1 = giveAnAlarmMapper.getLastRainsSum(Integer.valueOf(startMonth2Date.substring(5,7)), Integer.valueOf(endMonth2Date.substring(5,7)), stCodes);
        BigDecimal rainMylBig1 = new BigDecimal(0);
        BigDecimal cnRainMylBig2 = new BigDecimal(0);
        if (byList3 != null && byList3.size() > 0){
            BigDecimal bigDecimal = new BigDecimal(0);
            for (JSONObject jsonObject : byList3) {
                BigDecimal drp = jsonObject.getBigDecimal("drp");
                bigDecimal = bigDecimal.add(drp);
            }
            rainAvgBig1 = bigDecimal.divide(new BigDecimal(byList3.size()), 1, BigDecimal.ROUND_HALF_UP);
            rainMylBig1 = bigDecimal;
//            rainMyl = rainMylBig1.toString();
            rainMyl = rainAvgBig1.toString();
        }
        if (qnList1 != null && qnList1.size() > 0){
            BigDecimal bigDecimal = new BigDecimal(0);
            for (JSONObject jsonObject : qnList1) {
                BigDecimal drp = jsonObject.getBigDecimal("drp");
                bigDecimal = bigDecimal.add(drp);
            }
            rainAvgBig2 = bigDecimal.divide(new BigDecimal(qnList1.size()), 1, BigDecimal.ROUND_HALF_UP);
            cnRainMylBig2 = bigDecimal;
//            cnRainMyl = cnRainMylBig2.toString();
            cnRainMyl = rainAvgBig2.toString();
        }
        if (cnRainMylBig2.compareTo(new BigDecimal(0)) > 0){
            //计算百分比， 查看偏多偏少
            BigDecimal divide = (rainMylBig1.subtract(cnRainMylBig2)).multiply(new BigDecimal(100)).divide(cnRainMylBig2, 1, BigDecimal.ROUND_HALF_UP);
            if (divide.compareTo(new BigDecimal(0)) > 0){
                pdps2 = "偏多";
            } else {
                pdps2 = "偏少";
                divide = divide.abs();
            }
            bfb2 = divide.toString();
        }

        //柱形图固定显示这几个测站的数据
        String[] nameAll = {"下冯山","临江","上戍","泽雅水库","仰义水库","无敌坑山塘","丰门河岩门站","双屿","黄龙","划龙桥(水心殿)","七都水文雨量站"};
        String[] codes = {"03191","02070","02073","01589","01201","03189","010112","02072","06735","06731","30032917"};
        Integer value = Integer.valueOf(endMonth2Date.substring(5, 7));
        String year = endMonth2Date.substring(0, 4);
        String[] xl1 = {year + "年"+value+"月降水量", "同期"+value+"月份降水量"};
        String[] xl2 = {year + "年1月—"+value+"月面雨量", "去年同期1月—"+value+"月面雨量"};

        List<Number[]> multiLineValues = new ArrayList<>();
        List<Number[]> multiLineValues2 = new ArrayList<>();
        Number[] level = new Number[codes.length];
        Number[] level2 = new Number[codes.length];
        Number[] level3 = new Number[codes.length];
        Number[] level4 = new Number[codes.length];
        for (int i = 0; i < codes.length; i++) {
            level[i] = 0;
            level2[i] = 0;
            level3[i] = 0;
            level4[i] = 0;
            for (JSONObject jsonObject : byList2) {
                String stcd = jsonObject.getString("stcd");
                double drp = jsonObject.getDoubleValue("drp");
                if (stcd.equals(codes[i])){
                    level3[i] = drp;
                }
            }
            for (JSONObject jsonObject : qnList) {
                String stcd = jsonObject.getString("stcd").trim();
                double drp = jsonObject.getDoubleValue("drp");
                if (stcd.equals(codes[i])){
                    level4[i] = drp;
                }
            }
            for (JSONObject jsonObject : byList3) {
                String stcd = jsonObject.getString("stcd");
                double drp = jsonObject.getDoubleValue("drp");
                if (stcd.equals(codes[i])){
                    level[i] = drp;
                }
            }
            for (JSONObject jsonObject : qnList1) {
                String stcd = jsonObject.getString("stcd").trim();
                double drp = jsonObject.getDoubleValue("drp");
                if (stcd.equals(codes[i])){
                    level2[i] = drp;
                }
            }
        }
        multiLineValues.add(level3);
        multiLineValues.add(level4);
        multiLineValues2.add(level);
        multiLineValues2.add(level2);

        params.put("day1",day1);
        params.put("rainNameOne",rainNameOne);
        params.put("rainCountOne",rainCountOne);
        params.put("rainNameTwo",rainNameTwo);
        params.put("rainCountTwo",rainCountTwo);
        params.put("rainNameThree",rainNameThree);
        params.put("rainCountThree",rainCountThree);
        params.put("rainAvg",rainAvg);
        params.put("pdps",pdps);
        params.put("bfb",bfb);
        params.put("cnRainAvg",cnRainAvg);
        params.put("rainMyl",rainMyl);
        params.put("pdps2",pdps2);
        params.put("bfb2",bfb2);
        params.put("cnRainMyl",cnRainMyl);
        params.put("dyRainMyl",dyRainMyl);
        params.put("jmxjy",str);
        params.put("month2",month2);
        PoiUtil.wordExportChar(barChar1, "", xl1, nameAll, multiLineValues2);
        PoiUtil.wordExportChar(barChar2, "", xl2, nameAll, multiLineValues);
    }

    /**
     * 水雨情简报 水情概述部分  查询相关数据放入位置，
     * @param multiLineChar
     * @param params
     */
    private void summaryOfWater (XWPFChart multiLineChar,Map<String, Object> params){
        Date end = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(end);
        c.add(Calendar.HOUR_OF_DAY, -24);
        Date start = c.getTime();
        String endTime = DateUtil.format(end, "yyyy-MM-dd HH:00:00");
        String startTime = DateUtil.format(start, "yyyy-MM-dd HH:00:00");

        //水库水情表
        List<Map<String, Object>> skWaterList = new ArrayList<>();
        List<String> skTables = echoWaterRainTableByDateInternal(StationType.RESERVOIR.table(), start, end);
        //查询水库绑定的主测站和其他三座固定的水库水位站
        List<AttResBaseVo> skStBases = giveAnAlarmMapper.getWaterListStcd();
        List<String> skCodes = skStBases.stream().map(AttResBaseVo::getStcd).collect(Collectors.toList());
        List<Map<String, Object>> sksqList = giveAnAlarmMapper.getsksqList(skTables, skCodes, startTime, endTime);

        int skXh = 1, skSfCj = 0;
        String str = "泽雅水库蓄水率/（可供水量/万方），仰义水库蓄水率/（可供水量/万方），下冯山水库蓄水率/（可供水量/万方），东山里水库蓄水率/（可供水量/万方）。";
        StringBuffer stringBuffer = new StringBuffer();
        Map<Object, List<Map<String, Object>>> listMap = new HashMap<>();
        if (sksqList != null && sksqList.size() > 0) {
            listMap = sksqList.stream().collect(Collectors.groupingBy(m -> m.get("STCD")));
        }
            for (AttResBaseVo skStBase : skStBases) {
                Map<String, Object> stringObjectMap = new HashMap<>();
                String resCode = skStBase.getResCode();
                String resName = skStBase.getResName();
                String stcd = skStBase.getStcd();
                String safetyConclusion = skStBase.getSafetyConclusion();
                Double flLowLimLevCap = skStBase.getFlLowLimLevCap();
                Double level = skStBase.getLimitLevel();
                Double normWatLev = skStBase.getNormWatLev();
                Double normPoolStagCap = skStBase.getNormPoolStagCap();
                Double yphoonFloodLlimitWaterLevel = skStBase.getYphoonFloodLlimitWaterLevel();
                List<Map<String, Object>> list = listMap.get(stcd);
                //是否超警
                boolean isWarning = false;
                //当前水位
                Double waterLevel = null;
                //最高水位的数据
                Map<String, Object> objectMap = null;
                if (list != null && list.size() > 0) {
                    Map<String, Object> map1 = list.stream().max(Comparator.comparing(o -> (Date) o.get("TM"))).get();
                    waterLevel = Double.valueOf(String.valueOf(map1.get("rz")));
                    for (Map<String, Object> map : list) {
                        Double o = Double.valueOf(String.valueOf(map.get("rz")));
                        if (level != null && level > 0 && level < o) {
                            isWarning = true;
                        }
                        if (objectMap == null || Double.valueOf(String.valueOf(objectMap.get("rz"))) < o) {
                            objectMap = map;
                        }
                    }
                }
                if (isWarning) {
                    //超警的测站
                    skSfCj++;
                    //计算超警数据是多少m
                    stringObjectMap.put("cjMax", objectMap != null && level != null && level > 0 ? Double.valueOf(String.valueOf(objectMap.get("Z"))) - level : 0);
                } else {
                    stringObjectMap.put("cjMax", 0);
                }
                //当前蓄水量
                Float y = null;
                if (waterLevel != null && StringUtils.isNotBlank(resCode)){
                    y = getXByY(Float.parseFloat(String.valueOf(waterLevel)), resCode);
                }
                stringObjectMap.put("xh", skXh);
                stringObjectMap.put("name", resName);
                stringObjectMap.put("dqsw",waterLevel !=null ? waterLevel : "/");
                stringObjectMap.put("max", objectMap != null ? objectMap.get("rz") : "/");
                stringObjectMap.put("tm", objectMap != null ? objectMap.get("TM") : "/");
                stringObjectMap.put("zcsw", normWatLev!=null && normPoolStagCap > 0 ? normWatLev : "/");
                stringObjectMap.put("zckr", normPoolStagCap!=null && normPoolStagCap > 0 ? normPoolStagCap : "/");
                stringObjectMap.put("txsw", yphoonFloodLlimitWaterLevel!=null && normPoolStagCap > 0 ? new BigDecimal(yphoonFloodLlimitWaterLevel).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() : "/");
                stringObjectMap.put("dqxsl", y != null ? new BigDecimal(y).setScale(2,BigDecimal.ROUND_HALF_UP) : "/");
                stringObjectMap.put("xsl", y != null && flLowLimLevCap != null?HydrologyUtils.calculateStorageRatio(new BigDecimal(y),new BigDecimal(flLowLimLevCap)).setScale(2,BigDecimal.ROUND_HALF_UP)+"%" : "/");
                skXh++;
                //插入各个水库的蓄水率，可供水量的值拼接返回
                if (StringUtils.isNotBlank(resCode)){
                    stringBuffer.append(stringObjectMap.get("name")).append("蓄水率").append(stringObjectMap.get("xsl")).append("（可供水量").append(stringObjectMap.get("dqxsl")).append("万方），");
                }
                skWaterList.add(stringObjectMap);
            }

        List<Map<String, Object>> skCjMax = skWaterList.stream().sorted(Comparator.comparing(m -> Double.valueOf(String.valueOf(m.get("cjMax"))))).collect(Collectors.toList());
        Map<String, Object> skObjectMap = new HashMap<>();
        if (skCjMax != null && skCjMax.size() > 0) {
            //超汛限水位最高的测站
            skObjectMap = skCjMax.get(skCjMax.size() - 1);
        }
        if (stringBuffer.length() > 0){
            str = stringBuffer.toString().substring(0, stringBuffer.length() - 1) + "。";
        }
        params.put("str", str);
        params.put("cxxCount", skSfCj);
        params.put("cxxNameMax", skObjectMap.containsKey("name") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("name") : "/");
        params.put("cxxTmMax", skObjectMap.containsKey("tm") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("tm") : "/");
        params.put("cxxWaterMax", skObjectMap.containsKey("max") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("max") : "/");
        params.put("cxxWaterLevel", skObjectMap.containsKey("cjMax") && Double.valueOf(String.valueOf(skObjectMap.get("cjMax"))) > 0 ? skObjectMap.get("cjMax") : "/");
        params.put("skWaterList", skWaterList);

        //河网水情表
        String[] barSeriesNames = {"上戍","划龙桥(水心殿)","双屿康龙","卧旗水闸上游"," 七都内河"};
        String[] multiLineCats = {"戍浦江","温瑞塘河","丰门河","卧旗大河"," 七都内河"};
        String[] codes = {"30032908","06731","06722","06732"," 06730"};
        //获取上个月一号的时间和本月一号的时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatters1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatters2 = DateTimeFormatter.ofPattern("yyyy-MM-01 HH:mm:ss");
        //此时
        String nowStr = formatters1.format(now);
        //本月一号
        String nowStr2 = formatters2.format(now);
        LocalDateTime date = now.minusMonths(1);
        //上个月一号
        String dateStr = formatters2.format(date);
        //每列值对应的时间
        String[] time = {dateStr.substring(0,10),nowStr2.substring(0,10),nowStr.substring(0,10)};

        //本月的表名
        String table = StationType.RIVER.table().concat(LocalDate.now().format(yyyyMM));
        //上个月的表名
        String table2 = StationType.RIVER.table().concat(date.format(yyyyMM));

        List<StRiverR> stRiverRS = graphRpcService.selectRiverByTimeCode(table, Arrays.asList(codes), DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", nowStr));
        List<StRiverR> stRiverRS1 = graphRpcService.selectRiverByTimeCode(table, Arrays.asList(codes), DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", nowStr2));
        List<StRiverR> stRiverRS2 = graphRpcService.selectRiverByTimeCode(table2, Arrays.asList(codes), DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", dateStr));

        List<Number[]> multiLineValues = new ArrayList<>();
        //存放表格的数据
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < codes.length; i++) {
            Number[] level = new Number[3];
            HashMap<String, Object> map = new HashMap<>();
            map.put("hwName", multiLineCats[i]);
            map.put("czName", barSeriesNames[i]);
            map.put("data1", "/");
            map.put("data2", "/");
            map.put("data3", "/");
            for (StRiverR stRiverR : stRiverRS) {
                Double z = stRiverR.getZ();
                String mgstcd = stRiverR.getMgstcd();
                if (mgstcd.equals(codes[i])){
                    level[2] = z;
                    map.put("data3", z);
                }
            }
            for (StRiverR stRiverR : stRiverRS1) {
                Double z = stRiverR.getZ();
                String mgstcd = stRiverR.getMgstcd();
                if (mgstcd.equals(codes[i])){
                    level[1] = z;
                    map.put("data2", z);
                }
            }
            for (StRiverR stRiverR : stRiverRS2) {
                Double z = stRiverR.getZ();
                String mgstcd = stRiverR.getMgstcd();
                if (mgstcd.equals(codes[i])){
                    level[0] = z;
                    map.put("data1", z);
                }
            }
            if (level[0] == null) {
                level[0] = 0;
            }
            if (level[1] == null) {
                level[1] = 0;
            }
            if (level[2] == null) {
                level[2] = 0;
            }
            list.add(map);
            multiLineValues.add(level);
        }
        params.put("hwsqList", list);
        params.put("year3", date.getYear());
        params.put("month3", date.getMonthValue());
        PoiUtil.wordExportChar(multiLineChar, "", multiLineCats, time, multiLineValues);
    }

    @Override
    public  List<Map<String, Object>> rainfallReport(String startTime, String endTime, Double minRain, Double maxRain) {
        List<AttStBase> rainStations = giveAnAlarmMapper.getRainStation();
        List<String> stCodes = rainStations.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        //判断是否跨表查询
        Date start = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss",startTime);
        Date end = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss",endTime);
        List<String> rainTables = echoWaterRainTableByDateInternal(StationType.RAINFALL.table(), start, end);

        List<Map<String, Object>> rainsqlList = giveAnAlarmMapper.getRainsqlList(rainTables, stCodes, startTime, endTime,minRain,maxRain);

        List<Map<String, Object>> objects = new ArrayList<>();
        if (!CollectionUtils.isEmpty(rainsqlList)){
            Map<String, AttStBase> attStBaseMap = rainStations.stream().collect(Collectors.toMap(AttStBase::getStCode, Function.identity()));
            for (Map<String, Object> map : rainsqlList) {
                String stcd = String.valueOf(map.get("stcd"));
                AttStBase attStBase = attStBaseMap.get(stcd);
                map.put("bas",attStBase.getBas());
                map.put("stName",attStBase.getStName());
                map.put("adName",attStBase.getAdName());
                map.put("adCode",attStBase.getAreaCode());
            }
            Map<Object, List<Map<String, Object>>> adName = rainsqlList.stream().collect(Collectors.groupingBy(o -> o.get("adName")));

            List<Map<String, Object>> finalObjects = objects;
            AtomicInteger totalNum = new AtomicInteger();
            AtomicReference<Double> totalDrp = new AtomicReference<>(0.0);
            adName.forEach((k, v)->{
                finalObjects.addAll(v);
                Map<String, Object> map = v.get(0);
                Object adName2 = map.get("adName");
                Object adCode = map.get("adCode");
                int num = v.size();
                totalNum.addAndGet(num);
                Double drp = v.stream().mapToDouble(o -> Double.valueOf(String.valueOf(o.get("drp")))).sum();
                totalDrp.updateAndGet(v1 -> v1 + drp);
                Double avgDrp = v.stream().mapToDouble(o -> Double.valueOf(String.valueOf(o.get("drp")))).average().getAsDouble();
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("bas","合计");
                hashMap.put("adName",adName2);
                hashMap.put("stName",num + "站");
                hashMap.put("drp","平均雨量:" + new BigDecimal(avgDrp).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                hashMap.put("adCode",adCode);
                finalObjects.add(hashMap);
            });
            objects = finalObjects.stream().sorted(Comparator.comparing(o->String.valueOf(o.get("adCode")))).collect(Collectors.toList());
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("bas","");
            hashMap.put("adName","全区");
            hashMap.put("stName",totalNum.get() + "站");
            hashMap.put("drp","平均雨量:" + new BigDecimal(totalDrp.get()/totalNum.get() + "").setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            objects.add(hashMap);
        }
        return objects;
    }

    @Override
    public void exportRainfallReport(String startTime, String endTime, Double minRain, Double maxRain,HttpServletResponse response) throws IOException {
        List<Map<String, Object>> maps = rainfallReport(startTime, endTime, minRain, maxRain);
        String fileName = "雨量报表";
        Date start = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", startTime);
        int startYear = DateUtil.getYear(start);
        int startMonth = DateUtil.getMonth(start);
        int startDay = DateUtil.getDate(start);
        int startHour = DateUtil.getHour(start);
        Date end = DateUtil.parseDate("yyyy-MM-dd HH:mm:ss", endTime);
        int endYear = DateUtil.getYear(end);
        int endMonth = DateUtil.getMonth(end);
        int endDay = DateUtil.getDate(end);
        int endHour = DateUtil.getHour(end);
        if (!CollectionUtils.isEmpty(maps)){
            String json = JSON.toJSONString(maps);
            List<RainReportModule> rainReportModules = JSONArray.parseArray(json, RainReportModule.class);
            List<RainReportModule> rainReportModulesCopy = JSONArray.parseArray(json, RainReportModule.class);
            String secondTitle = "时间："+startYear+"年"+startMonth+"月"+startDay+"日"+startHour+"时至"+endYear+"年"+endMonth+"月"+endDay+"日"+endHour+"时" +" 单位：mm";
            ExportParams params1 = new ExportParams("雨量报表",secondTitle,"雨量报表");
            Workbook workbook = ExcelExportUtil.exportExcel(params1,RainReportModule.class ,rainReportModules);
            Sheet sheetAt = workbook.getSheetAt(0);
            //合并同一行政区划单元格
            String adName = rainReportModulesCopy.get(0).getAdName();
            int count = 0;
            int startRow = 3;
            for (RainReportModule rainReportModule : rainReportModulesCopy) {
                if (adName.equals(rainReportModule.getAdName())){
                    count++;
                }else {
                    adName = rainReportModule.getAdName();
                    int endRow = startRow + count -1;
                    CellRangeAddress region = new CellRangeAddress(startRow,endRow,0,0);
                    sheetAt.addMergedRegion(region);
                    count = 1;
                    startRow = endRow + 1;
                }
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="  + URLEncoder.encode(fileName, "utf-8")+ ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        }else {
            String secondTitle = "时间："+startYear+"年"+startMonth+"月"+startDay+"日"+startHour+"时至"+endYear+"年"+endMonth+"月"+endDay+"日"+endHour+"时" +" 单位：mm";
            ExportParams params1 = new ExportParams("雨量报表",secondTitle,"雨量报表");
            String json = JSON.toJSONString(maps);
            List<RainReportModule> rainReportModules = JSONArray.parseArray(json, RainReportModule.class);
            Workbook workbook = ExcelExportUtil.exportExcel(params1,RainReportModule.class ,rainReportModules);
            Sheet sheetAt = workbook.getSheetAt(0);
            //创建格式对象
            CellStyle cellStyle = (CellStyle) workbook.createCellStyle();
            //垂直居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            //水平居中
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            Font font = workbook.createFont();
            //字体大小
            font.setFontHeightInPoints((short)15);
            cellStyle.setFont(font);
            CellRangeAddress region = new CellRangeAddress(3, 3, 0,3);
            sheetAt.addMergedRegion(region);
            Row row = sheetAt.createRow(3);
            Cell cell=row.createCell(0);
            cell.setCellValue("暂时没有相关数据");
            cell.setCellStyle(cellStyle);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename="  + URLEncoder.encode(fileName, "utf-8")+ ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.flushBuffer();
            workbook.write(response.getOutputStream());

        }
    }

    @Override
    public JSONObject riskHiddenDanger() {
        JSONObject result = new JSONObject();
        //超限水库
        SynWaterRequestVo skRequestVo = new SynWaterRequestVo();
        skRequestVo.setStationType("RR");
        skRequestVo.setWarning(true);
        CommonResult<List<com.ygkj.big.screen.model.AttStBase>> listCommonResult1 = graphClient.waterStations(skRequestVo);
        List<com.ygkj.big.screen.model.AttStBase> resList = listCommonResult1.getData();
        result.put("resNum",resList!=null?resList.size():0);
        result.put("resList",resList);
        //海塘风险
        //海塘报警风险
        List<JSONObject> htList = new ArrayList<>();
        CommonResult seawallWarning = seawallWarning(null, null,true);
        HashMap<String, Object> data = (HashMap) seawallWarning.getData();
        List<com.ygkj.project.vo.response.AttSeawallBaseVo> baseVo = JSON.parseArray(JSON.toJSONString(data.get("list")), com.ygkj.project.vo.response.AttSeawallBaseVo.class);
        if (com.ygkj.utils.CollectionUtils.isNotBlank(baseVo)){
            for (com.ygkj.project.vo.response.AttSeawallBaseVo attSeawallBaseVo : baseVo) {
                JSONObject object = new JSONObject();
                object.put("name",attSeawallBaseVo.getSeawallName());
                object.put("seawallCode",attSeawallBaseVo.getSeawallCode());
                object.put("type","海塘报警");
                htList.add(object);
            }
        }
        //海塘预警风险
        CommonResult seawallGiveAnAlarm = seawallGiveAnAlarm(null, null,true);
        HashMap<String, Object> seawallWarningData = (HashMap) seawallGiveAnAlarm.getData();
        List<com.ygkj.project.vo.response.AttSeawallBaseVo> baseVos = JSON.parseArray(JSON.toJSONString(seawallWarningData.get("list")), com.ygkj.project.vo.response.AttSeawallBaseVo.class);
        if (!CollectionUtils.isEmpty(baseVo)){
            if (CollectionUtils.isEmpty(htList)){
                for (com.ygkj.project.vo.response.AttSeawallBaseVo attSeawallBaseVo : baseVos) {
                    JSONObject object = new JSONObject();
                    object.put("name",attSeawallBaseVo.getSeawallName());
                    object.put("seawallCode",attSeawallBaseVo.getSeawallCode());
                    object.put("type","海塘预警");
                    htList.add(object);
                }
            }else {
                c:for (com.ygkj.project.vo.response.AttSeawallBaseVo attSeawallBaseVo : baseVos) {
                    for (JSONObject object : htList) {
                        if (object.getString("seawallCode").equals(attSeawallBaseVo.getSeawallCode()) && !object.getString("type").contains("海塘预警")){
                            String type = object.getString("type");
                            object.put("type",type + ",海塘预警");
                            continue c;
                        }
                    }
                    JSONObject object = new JSONObject();
                    object.put("name",attSeawallBaseVo.getSeawallName());
                    object.put("seawallCode",attSeawallBaseVo.getSeawallCode());
                    object.put("type","海塘预警");
                    htList.add(object);
                }
            }
        }
        //沉降
        CommonResult cjList = displacement(null, 1);
        List<JSONObject> cjListData = (List<JSONObject>) cjList.getData();
        if (!CollectionUtils.isEmpty(baseVo)){
            if (CollectionUtils.isEmpty(htList)){
                for (JSONObject jsonObject : cjListData) {
                    JSONObject object = new JSONObject();
                    object.put("name",jsonObject.getString("gcmc"));
                    object.put("seawallCode",jsonObject.getString("gcbm"));
                    object.put("type","沉降报警");
                    htList.add(object);
                }
            }else {
                c:for (JSONObject jsonObject : cjListData) {
                    for (JSONObject object : htList) {
                        if (object.getString("seawallCode").equals(jsonObject.getString("gcbm")) && !object.getString("type").contains("沉降报警")){
                            String type = object.getString("type");
                            object.put("type",type + ",沉降报警");
                            continue c;
                        }
                    }
                    JSONObject object = new JSONObject();
                    object.put("name",jsonObject.getString("gcmc"));
                    object.put("seawallCode",jsonObject.getString("gcbm"));
                    object.put("type","沉降报警");
                    htList.add(object);
                }
            }
        }
        //位移
        CommonResult wyList = displacement(null, 2);
        List<JSONObject> wyListData = (List<JSONObject>) wyList.getData();
        if (!CollectionUtils.isEmpty(baseVo)){
            if (CollectionUtils.isEmpty(htList)){
                for (JSONObject jsonObject : wyListData) {
                    JSONObject object = new JSONObject();
                    object.put("name",jsonObject.getString("gcmc"));
                    object.put("seawallCode",jsonObject.getString("gcbm"));
                    object.put("type","位移预警");
                    htList.add(object);
                }
            }else {
                c:for (JSONObject jsonObject : wyListData) {
                    for (JSONObject object : htList) {
                        if (object.getString("seawallCode").equals(jsonObject.getString("gcbm")) && !object.getString("type").contains("位移预警")){
                            String type = object.getString("type");
                            object.put("type",type + ",位移预警");
                            continue c;
                        }
                    }
                    JSONObject object = new JSONObject();
                    object.put("name",jsonObject.getString("gcbm"));
                    object.put("seawallCode",jsonObject.getString("gcbm"));
                    object.put("type","位移预警");
                    htList.add(object);
                }
            }
        }
        if (!CollectionUtils.isEmpty(htList)){
            ProjectManageQueryVo queryVo = new ProjectManageQueryVo();
            queryVo.setType("海塘");
            CommonResult<List<JSONObject>> projects = projectManageClient.projects(queryVo);
            List<JSONObject> projectsData = projects.getData();
            c:for (JSONObject object : htList) {
                for (JSONObject projectsDatum : projectsData) {
                    if (object.getString("seawallCode").equals(projectsDatum.getString("seawallCode"))){
                        object.put("latitude",projectsDatum.getString("latitude"));
                        object.put("longitude",projectsDatum.getString("longitude"));
                        object.put("coorpot",projectsDatum.getString("coorpot"));
                        continue c;
                    }
                }
            }
        }
        result.put("htNum",htList!=null?htList.size():0);
        result.put("htList",htList);
        //超警河网
        SynWaterRequestVo hwRequestVo = new SynWaterRequestVo();
        hwRequestVo.setStationType("ZZ");
        hwRequestVo.setWarning(true);
        CommonResult<List<com.ygkj.big.screen.model.AttStBase>> listCommonResult2 = graphClient.waterStations(hwRequestVo);
        List<com.ygkj.big.screen.model.AttStBase> riverList = listCommonResult2.getData();
        result.put("riverNum",riverList!=null?riverList.size():0);
        result.put("riveList",riverList);

        //超警堰闸
        SynWaterRequestVo yzRequestVo = new SynWaterRequestVo();
        yzRequestVo.setStationType("DD");
        yzRequestVo.setWarning(true);
        CommonResult<List<com.ygkj.big.screen.model.AttStBase>> listCommonResult3 = graphClient.waterStations(yzRequestVo);
        List<com.ygkj.big.screen.model.AttStBase> wasList = listCommonResult3.getData();
        result.put("wasNum",wasList!=null?wasList.size():0);
        result.put("wasList",wasList);
        return result;
    }

    @Override
    public List<JSONObject> latestMonitoringData() {
        List<JSONObject> result = new ArrayList<>();
        List<JSONObject> points = giveAnAlarmMapper.getMonitoringPoint();
        for (JSONObject point : points) {
            CommonResult commonResult = getAqjcData(1, point.getString("st_code"), null, null);
            JSONArray data = (JSONArray)commonResult.getData();
            if (!CollectionUtils.isEmpty(data)){
                List<JSONObject> list = data.toJavaList(JSONObject.class);
                List<JSONObject> collect = list.stream().sorted(Comparator.comparing(o -> o.getDate("gcsj"))).collect(Collectors.toList());
                JSONObject object = collect.get(collect.size() - 1);
                object.put("stCode",point.getString("st_code"));
                object.put("stName",point.getString("st_name"));
                object.put("name",point.getString("NAME"));
                result.add(object);
            }
        }
        return result;
    }

    @Override
    public Float getXByY(Float rz, String prcd) {
        if (null == rz || null == prcd) {
            return (float) 0;
        }
        BmCtroChcuVo before = null;
        BmCtroChcuVo after = null;

        BmCtroChwlIndexVo bcc = new BmCtroChwlIndexVo();
        bcc.setCurtp("738AB4573AAF498DA2B3851D475FA194");
        bcc.setPrcd(prcd);
        List<BmCtroChcuVo> list = getBmCtroChcuList(bcc);
        // 曲线list
        if (list != null && list.size() > 0) {
            for (BmCtroChcuVo md : list) {
                if (null == md.getY() || null == md.getX()) {
                    continue;
                }
                if (md.getY().compareTo(rz) == 0) {
                    return md.getX();
                }

                if (md.getY().compareTo(rz) < 0) {
                    before = md;
                }

                if (md.getY().compareTo(rz) > 0) {
                    after = md;
                    break;
                }
            }
        }
        if (before != null && after != null) {
            // 开始
            Float bY = before.getY();
            // 结束
            Float aY = after.getY();
            Float bZ = before.getX();
            Float aZ = after.getX();

            Float zm = aZ - bZ;
            Float yb = aY - bY;
            if (0 == zm && 0 == yb) {
                return before.getX();
            }
            Float yb2 = rz - bY;
            Float i = (zm * yb2) / yb + bZ;
            return i;
        }
        return (float) 0;
    }

    public List<BmCtroChcuVo> getBmCtroChcuList(BmCtroChwlIndexVo bcc) {
        return giveAnAlarmMapper.getBmCtroChcuList(bcc);
    }

    public static void main(String[] args) {
        //当月的开始和结束时间
        Map<String,String> monthTime = com.ygkj.big.screen.utils.DateUtil.getMonthTime();
        String startDate = monthTime.get("startDate");
        String endDate = monthTime.get("endDate");
        System.out.println(endDate);
    }
}

