package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.model.AttTideFcstValBase;
import com.ygkj.entity.SingletonMap;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttSeawallBase;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.project.model.SeawallData;
import com.ygkj.project.model.SeawallWarnRecord;
import com.ygkj.project.vo.request.ProjectManageQueryVo;
import com.ygkj.project.vo.request.SeawallQueryReqVo;
import com.ygkj.project.vo.response.AttStResVo;
import com.ygkj.project.vo.response.SeawallPointResVo;
import com.ygkj.project.vo.response.SeawallResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.AttSeawallMapper;
import com.ygkj.water.project.mapper.ProjectMapper;
import com.ygkj.water.project.mapper.WaterAndRainMapper;
import com.ygkj.water.project.service.AdcdBaseService;
import com.ygkj.water.project.service.SeawallService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/9/14
 */
@Service
@Slf4j
public class SeawallServiceImpl extends ServiceImpl<AttSeawallMapper,AttSeawallBase>implements SeawallService {

    @Resource
    ProjectMapper projectMapper;

    @Resource
    AttSeawallMapper seawallMapper;

    @Resource
    WaterAndRainMapper waterAndRainMapper;

    @Autowired
    AdcdBaseService adcdBaseService;

    private RestTemplate restTemplate = new RestTemplate();

    java.util.concurrent.ExecutorService executorService = Executors.newFixedThreadPool(2);

    /**
     * 导出海塘工程数据
     *
     * @param response
     * @param reqVo
     */
    @Override
    public void exportSeawall(HttpServletResponse response, SeawallQueryReqVo reqVo) {
        if (reqVo.getExportStrategy() == null) {
            reqVo.setExportStrategy(0);
        }
        // 禁止分页
        reqVo.noPage();
        List<AttSeawallBase> list = pageSeawall(reqVo).getList();
        switch (reqVo.getExportStrategy()) {
            case 0: {
                ExportUtils.exportExcel(list, "海塘",
                        new String[]{"海塘名称", "行政区划", "海塘级别", "所在乡镇", "海塘长度(m)", "设计防潮标准"},
                        new String[]{"seawallName", "areaName", "seawallLevel", "village", "seawallLength", "designTideStandard"},
                        response, null, Collections.emptyMap());

            }
            break;
        }
    }

    /**
     * 海塘工程分页列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<AttSeawallBase> pageSeawall(SeawallQueryReqVo reqVo) {
        ProjectManageQueryVo queryVo = Builder.of(ProjectManageQueryVo::new)
                .with(ProjectManageQueryVo::setType, "海塘")
                .with(ProjectManageQueryVo::setAreaCode, reqVo.getAdcd())
                .with(ProjectManageQueryVo::setGrade, reqVo.getGrade())
                .with(ProjectManageQueryVo::setProjectName, reqVo.getSeawallName())
                .with(ProjectManageQueryVo::setCode, reqVo.getSeawallCode())
                .with(ProjectManageQueryVo::setSafetyConclusion, reqVo.getSafetyConclusion())
                .with(ProjectManageQueryVo::setCodes, reqVo.getCodes())
                .build();
        if (StringUtils.isNotBlank(reqVo.getDesignTideStandard())) {
            if ("100年以上".equals(reqVo.getDesignTideStandard())) {
                queryVo.setDesignTideStandardStart(100);
            } else if ("10年以下".equals(reqVo.getDesignTideStandard())) {
                queryVo.setDesignTideStandardEnd(10);
            } else {
                try {
                    Integer temp = Integer.valueOf(reqVo.getDesignTideStandard().replace("年一遇", StringUtils.EMPTY));
                    queryVo.setDesignTideStandardStart(temp);
                    queryVo.setDesignTideStandardEnd(temp);
                } catch (Exception e) {

                }
            }
        }
        if (reqVo.isPaged()) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        PageVO<AttSeawallBase> result = PageUtils.getPageVO(projectMapper.selectSeawallByCondition(queryVo));
        List<AttSeawallBase> list = result.getList();
        Map<String, String> adcdMap = adcdBaseService.selectAdnmByAdcd(list.stream()
                .map(AttSeawallBase::getAreaCode)
                .filter(StringUtils::isNotBlank).collect(Collectors.toList()));
        for (AttSeawallBase temp : list) {
            temp.setAreaName(adcdMap.get(temp.getAreaCode()));
        }
        return result;
    }

    /**
     * 对海塘工程分页统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject stats4PageSeawall(SeawallQueryReqVo reqVo) {
        reqVo.noPage();
        String[] keys = {"lt10Year", "eq10Year", "eq20Year", "eq50Year", "eq100Year", "gt100Year", "notStandard"};
        List<? extends AttSeawallBase> list = pageSeawall(reqVo).getList();
        Map<String, Long> map = list.stream().collect(Collectors.groupingBy(o -> {
            if (o.getDesignTideStandard() == null) {
                return keys[6];
            } else if (o.getDesignTideStandard() < 10) {
                return keys[0];
            } else if (o.getDesignTideStandard() > 100) {
                return keys[5];
            } else {
                switch (o.getDesignTideStandard().intValue()) {
                    case 10:
                        return keys[1];
                    case 20:
                        return keys[2];
                    case 50:
                        return keys[3];
                    case 100:
                        return keys[4];
                    default:
                        return StringUtils.EMPTY;
                }
            }
        }, Collectors.counting()));
        JSONObject result = new JSONObject();
        for (String key : keys) {
            result.put(key, map.getOrDefault(key, 0L));
        }
        result.put("total", list.size());
        result.put("list", list);
        return result;
    }

    /**
     * 海塘报警热力图
     * 统计海塘报警次数
     *
     * @return
     */
    @Override
    public List<? extends AttSeawallBase> seawallHeatMap() {
        // 1. 先查询海塘
        List<AttSeawallBase> list = projectMapper.selectSeawallByCondition(null);
        // 2. 统计海塘报警次数
        List<SingletonMap<String, Long>> timeList = this.seawallMapper.warnTimes(null);
        Map<String, Long> map = SingletonMap.list2Map(timeList);
        List<SeawallResVo> result = new ArrayList<>(list.size());
        for (AttSeawallBase attSeawallBase : list) {
            SeawallResVo target = new SeawallResVo();
            BeanUtils.copyProperties(attSeawallBase, target);
            target.setMonitorWarnTimes(map.getOrDefault(attSeawallBase.getSeawallCode(), 0L).intValue());
            result.add(target);
        }
        return result;
    }

    /**
     * 海塘组件C位
     *
     * @param forceNewestWarnState true查询最新时刻的潮位
     * @return
     */
    @Override
    public JSONObject seawallUnitCWindow(boolean forceNewestWarnState, Date dataTime) {
        JSONObject result = new JSONObject();
        List<AttSeawallBase> list = projectMapper.selectSeawallByCondition(null);
        List<SeawallResVo> totalList = new ArrayList<>(list.size());
        for (AttSeawallBase attSeawallBase : list) {
            SeawallResVo target = new SeawallResVo();
            BeanUtils.copyProperties(attSeawallBase, target);
            totalList.add(target);
        }
        //
        fillTdzMonitorWarnState(totalList, forceNewestWarnState, dataTime);

        //
        fillTdzForecastWarnState(totalList, dataTime);

        List<SeawallResVo> monitorWarnList = totalList.stream().filter(o -> o.getMonitorWarnState() > 0).collect(Collectors.toList());
        List<SeawallResVo> forecastWarnList = totalList.stream().filter(o -> o.getForecastWarnState() > 0).collect(Collectors.toList());

        result.put("totalNum", totalList.size());
        result.put("totalList", totalList);
        result.put("monitorWarnNum", monitorWarnList.size());
        result.put("monitorWarnList", monitorWarnList);
        result.put("forecastWarnNum", forecastWarnList.size());
        result.put("forecastWarnList", forecastWarnList);
        return result;
    }

    @Override
    public JSONObject seawallUnitWarnCard(Integer warnType, boolean forceNewestWarnState, Date dataTime) {
        if (warnType == null) {
            warnType = 1;
        }
        JSONObject result = new JSONObject();
        List<AttSeawallBase> list = projectMapper.selectSeawallByCondition(null);
        List<SeawallResVo> points = new ArrayList<>(list.size());
        for (AttSeawallBase attSeawallBase : list) {
            SeawallResVo target = new SeawallResVo();
            BeanUtils.copyProperties(attSeawallBase, target);
            points.add(target);
        }
        Function<SeawallResVo, Integer> groupClassifier = null;
        if (warnType == 1) {
            fillTdzMonitorWarnState(points, false, dataTime);
            groupClassifier = SeawallResVo::getMonitorWarnState;
        } else if (warnType == 2) {
            fillTdzForecastWarnState(points, dataTime);
            groupClassifier = SeawallResVo::getForecastWarnState;
        } else {
            return result;
        }

        /**
         * 1. 查询所有的海塘
         * 2. 根据海塘的代表测站，查询实时实时潮位
         * 3. 统计正常，蓝色，黄色，橙色，红色等物种潮位预警的长度
         */
        double normLength = 0D;
        double blueLength = 0D;
        double yellowLength = 0D;
        double orangeLength = 0D;
        double redLength = 0D;
        int normNum = 0;
        int blueNum = 0;
        int yellowNum = 0;
        int orangeNum = 0;
        int redNum = 0;
        Map<Integer, List<SeawallResVo>> projGroupMap = points.stream()
                .collect(Collectors.groupingBy(groupClassifier));
        for (Map.Entry<Integer, List<SeawallResVo>> entry : projGroupMap.entrySet()) {
            Integer key = entry.getKey();
            List<SeawallResVo> value = entry.getValue();
            switch (key) {
                case 0:
                    normLength = value.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(o -> o.getSeawallLength().doubleValue()).sum();
                    normNum = value.size();
                    break;
                case 1:
                    blueLength = value.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(o -> o.getSeawallLength().doubleValue()).sum();
                    blueNum = value.size();
                    break;
                case 2:
                    yellowLength = value.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(o -> o.getSeawallLength().doubleValue()).sum();
                    yellowNum = value.size();
                    break;
                case 3:
                    orangeLength = value.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(o -> o.getSeawallLength().doubleValue()).sum();
                    orangeNum = value.size();
                    break;
                case 4:
                    redLength = value.stream().filter(o -> o.getSeawallLength() != null).mapToDouble(o -> o.getSeawallLength().doubleValue()).sum();
                    redNum = value.size();
                    break;
                default:
                    break;
            }
        }
        result.put("normLength", Double.parseDouble(String.format("%.1f", normLength / 1000)));
        result.put("blueLength", Double.parseDouble(String.format("%.1f", blueLength / 1000)));
        result.put("yellowLength", Double.parseDouble(String.format("%.1f", yellowLength / 1000)));
        result.put("orangeLength", Double.parseDouble(String.format("%.1f", orangeLength / 1000)));
        result.put("redLength", Double.parseDouble(String.format("%.1f", redLength / 1000)));

        result.put("normNum", normNum);
        result.put("blueNum", blueNum);
        result.put("yellowNum", yellowNum);
        result.put("orangeNum", orangeNum);
        result.put("redNum", redNum);
        result.put("points", points);
        return result;
    }

    @Override
    public void fillTdzMonitorWarnState(List<? extends SeawallResVo> list, boolean forceNewestWarnState, Date dataTime) {
        if (CollectionUtils.isBlank(list)) {
            return;
        }
        if (forceNewestWarnState) {
            dataTime = dataTime == null ? new Date() : dataTime;
            Map<String, StTideR> tdzMap = candidateSeawallTdz(list, dataTime);
            for (SeawallResVo attSeawallBase : list) {
                StTideR tdzTemp = tdzMap.get(attSeawallBase.getSeawallCode());
                if (tdzTemp == null || tdzTemp.getTdz() == null) {
                    continue;
                }
                Double tdzVal = tdzTemp.getTdz();
                int warnState = warnState(attSeawallBase, tdzVal);
                if (warnState == 0) {
                    continue;
                }
                attSeawallBase.setMonitorTdz(tdzVal);
                attSeawallBase.setMonitorWarnState(warnState);
                attSeawallBase.setMonitorTdzTime(tdzTemp.getTm());
                attSeawallBase.setMonitorWarnTime(dataTime);
            }
        } else {
            Map<String, SeawallWarnRecord> warnMap = candidateSeawallWarnRecord(list, dataTime);
            for (SeawallResVo seawallResVo : list) {
                SeawallWarnRecord warnRecord = warnMap.get(seawallResVo.getSeawallCode());
                if (warnRecord == null) {
                    continue;
                }
                seawallResVo.setMonitorTdz(warnRecord.getTdz());
                seawallResVo.setMonitorWarnState(warnRecord.getWarnState());
                seawallResVo.setMonitorTdzTime(warnRecord.getTdzTm());
                seawallResVo.setMonitorWarnTime(warnRecord.getWarnTm());
            }
        }
    }

    @Override
    public void fillTdzForecastWarnState(List<? extends SeawallResVo> list, Date dataTime) {
        dataTime = dataTime == null ? new Date() : dataTime;
        Map<String, AttTideFcstValBase> tdzMap = candidateForecastHighestTdz(list, dataTime, 3 * 24 * 60);
        for (SeawallResVo attSeawallBase : list) {
            AttTideFcstValBase tdzTemp = tdzMap.get(attSeawallBase.getSeawallCode());
            if (tdzTemp == null) {
                continue;
            }
            double tdzVal = tdzTemp.getTdz().doubleValue();
            int warnState = warnState(attSeawallBase, tdzVal);
            if (warnState == 0) {
                continue;
            }
            attSeawallBase.setForecastTdz(tdzVal);
            attSeawallBase.setForecastWarnState(warnState);
            attSeawallBase.setForecastTdzTime(tdzTemp.getTm());
            attSeawallBase.setForecastWarnTime(dataTime);
        }
    }

    /**
     * 监测海塘报警并入库
     */
    @Override
    public void monitorSeawallWarn() {
        // 1. 先查询海塘
        List<AttSeawallBase> list = projectMapper.selectSeawallByCondition(null);
        Map<String, StTideR> tdzMap = candidateSeawallTdz(list, null);
        Date now = new Date();
        for (AttSeawallBase attSeawallBase : list) {
            StTideR tdzTemp = tdzMap.get(attSeawallBase.getSeawallCode());
            if (tdzTemp == null || tdzTemp.getTdz() == null) {
                continue;
            }
            Double tdzVal = tdzTemp.getTdz();
            int warnState = warnState(attSeawallBase, tdzVal);
            if (warnState == 0) {
                continue;
            }
            SeawallWarnRecord target = new SeawallWarnRecord();
            target.setTdz(tdzVal);
            target.setTdzTm(tdzTemp.getTm());
            target.setWarnTm(now);
            target.setSeawallCode(attSeawallBase.getSeawallCode());
            target.setWarnState(warnState);
            this.seawallMapper.insertWarnRecord(target);
        }
    }


    /**
     * 每5分钟提交一次任务
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void doMonitorSeawallWarn() {
        Runnable runnable = () -> {
            log.info("提交了一项海塘监测报警");
            this.monitorSeawallWarn();
        };
        executorService.submit(runnable);
    }

    public int warnState(AttSeawallBase attSeawallBase, double tdzVal) {
        int warnState = 0;
        if (attSeawallBase.getTideStationRedLevel() != null && tdzVal > attSeawallBase.getTideStationRedLevel().doubleValue()) {
            warnState = 4;
        } else if (attSeawallBase.getTideStationOrangeLevel() != null && tdzVal > attSeawallBase.getTideStationOrangeLevel().doubleValue()) {
            warnState = 3;
        } else if (attSeawallBase.getTideStationYellowLevel() != null && tdzVal > attSeawallBase.getTideStationYellowLevel().doubleValue()) {
            warnState = 2;
        } else if (attSeawallBase.getTideStationBlueLevel() != null && tdzVal > attSeawallBase.getTideStationBlueLevel().doubleValue()) {
            warnState = 1;
        }
        return warnState;
    }

    public Map<String, StTideR> candidateSeawallTdz(List<? extends AttSeawallBase> list, Date dataTime) {
        List<String> tideStcdList = list.stream().map(AttSeawallBase::getTideStationCode).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        dataTime = dataTime == null ? new Date() : dataTime;
        List<StTideR> tdzList = waterAndRainMapper.candidateTideWithTime(candidateR(StationType.TIDE, dataTime), tideStcdList, dataTime);
        Map<String, StTideR> tempMap = tdzList.stream().collect(Collectors.toMap(StTideR::getMgstcd, Function.identity()));
        Map<String, StTideR> resultMap = new HashMap<>(tempMap.size());
        for (AttSeawallBase attSeawallBase : list) {
            resultMap.put(attSeawallBase.getSeawallCode(), tempMap.get(attSeawallBase.getTideStationCode()));
        }
        return resultMap;
    }


    public Map<String, AttTideFcstValBase> candidateForecastHighestTdz(List<? extends AttSeawallBase> list, Date dataTime, int futureMinutes) {
        List<String> tideStcdList = list.stream().map(AttSeawallBase::getTideStationCode).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        dataTime = dataTime == null ? new Date() : dataTime;
        List<AttTideFcstValBase> tdzList = this.seawallMapper.selectForecastHighestTdz(tideStcdList, dataTime, futureMinutes);
        Map<String, AttTideFcstValBase> tempMap = tdzList.stream().collect(Collectors.toMap(AttTideFcstValBase::getStCode, Function.identity()));
        Map<String, AttTideFcstValBase> resultMap = new HashMap<>(tempMap.size());
        for (AttSeawallBase attSeawallBase : list) {
            resultMap.put(attSeawallBase.getSeawallCode(), tempMap.get(attSeawallBase.getTideStationCode()));
        }
        return resultMap;
    }

    public Map<String, SeawallWarnRecord> candidateSeawallWarnRecord(List<? extends AttSeawallBase> list, Date dataTime) {
        if (CollectionUtils.isBlank(list)) {
            return Collections.emptyMap();
        }
        List<String> seawallCodeList = list.stream().map(AttSeawallBase::getSeawallCode).collect(Collectors.toList());
        // 五分钟内监测一次，7分钟筛选7分钟内的报警数据
        List<SeawallWarnRecord> warnRecordList = this.seawallMapper.candidateSeawallWarnRecord(seawallCodeList, dataTime, 7);
        warnRecordList = warnRecordList == null ? Collections.emptyList() : warnRecordList;
        return warnRecordList.stream().collect(Collectors.toMap(SeawallWarnRecord::getSeawallCode, Function.identity()));
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

    DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");

    public String candidateR(StationType stationType, Date now) {
        return stationType.table().concat(sdf.format(now));
    }

    public String candidateR(StationType stationType, ChronoLocalDate now) {
        return stationType.table().concat(now.format(yyyyMM));
    }

    /**
     * 海塘工程列表
     *
     * @return
     */
    @Override
    public List<SeawallResVo> warnSeawallList(Integer warnType, boolean forceNewestWarnState, Date dataTime) {
        if (warnType == null) {
            warnType = 1;
        }
        List<AttSeawallBase> list = pageSeawall(new SeawallQueryReqVo()).getList();
        List<SeawallResVo> result = new ArrayList<>(list.size());
        for (AttSeawallBase attSeawallBase : list) {
            SeawallResVo target = new SeawallResVo();
            BeanUtils.copyProperties(attSeawallBase, target);
            result.add(target);
        }
        if (warnType == 1) {
            fillTdzMonitorWarnState(result, forceNewestWarnState, dataTime);
            result = result.stream().filter(o -> o.getMonitorWarnState() > 0).collect(Collectors.toList());
        } else if (warnType == 2) {
            fillTdzForecastWarnState(result, dataTime);
            result = result.stream().filter(o -> o.getForecastWarnState() > 0).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 导出海塘工程数据
     *
     * @param response
     * @param warnType
     * @param forceNewestWarnState
     * @param dataTime
     */
    @Override
    public void exportWarnSeawallList(HttpServletResponse response, Integer warnType, boolean forceNewestWarnState, Date dataTime) {
        List<SeawallResVo> list = warnSeawallList(warnType, forceNewestWarnState, dataTime);
        if (warnType == null) {
            warnType = 1;
        }
        if (warnType == 1) {
            ExportUtils.exportExcel(list, "监测预警海塘",
                    new String[]{"海塘名称", "行政区划", "海塘级别", "海塘长度(m)", "关联潮位站", "潮位值", "四色预警", "监测时间"},
                    new String[]{"seawallName", "areaName", "seawallLevel", "seawallLength", "tideStationName", "monitorTdz", "monitorWarnState", "monitorWarnTime"},
                    response, null, Collections.emptyMap());
        } else if (warnType == 2) {
            ExportUtils.exportExcel(list, "预报预警海塘",
                    new String[]{"海塘名称", "行政区划", "海塘级别", "海塘长度(m)", "关联潮位站", "潮位值", "四色预警", "预警时间"},
                    new String[]{"seawallName", "areaName", "seawallLevel", "seawallLength", "tideStationName", "forecastTdz", "forecastWarnState", "forecastWarnTime"},
                    response, null, Collections.emptyMap());
        }

    }

    @Override
    public HashMap<String, Object> seawallAqjd() {
        return projectMapper.seawallAqjd();
    }

    @Override
    public HashMap<String, Object> seawallHtdj() {
        return projectMapper.seawallHtdj();
    }

    /**
     * 海塘关联测站
     *
     * @param seawallCode
     * @return
     */
    @Override
    public List<AttStResVo> listSt(String seawallCode) {
        return seawallMapper.listSt(seawallCode);
    }

    @Override
    public AttSeawallBase getSeawallInfo(String seawallCode) {
        List<AttSeawallBase> attSeawallBases = seawallMapper.selectByCode(seawallCode);
        if (attSeawallBases != null) {
            return attSeawallBases.get(0);
        } else {
            return null;
        }
    }

    @Override
    public int isScheduling(String seawallCode) {
        return seawallMapper.isScheduling(seawallCode);
    }

    @Override
    public SeawallData getSeawallGcyg(String seawallCode) {
        return seawallMapper.getSeawallGcyg(seawallCode);
    }

    @Override
    public int isBindSt(String seawallCode) {
        return seawallMapper.isBindSt(seawallCode);
    }

    /**
     * 降雨预报
     *
     * @param seawallCode
     * @return
     */
    @Override
    public JSONArray forecastRainfall(String seawallCode) {
        List<SeawallPointResVo> list = seawallMapper.listSeawallPoint(seawallCode);
        if(StringUtils.isEmpty(seawallCode)){
            seawallCode = String.join(",",list.stream().map(SeawallPointResVo::getSeawallCode).collect(Collectors.toList()));
        }
        JSONObject jsonObject = restTemplate.getForObject(
                "https://sk.zjwater.com/mgt/st/rainybprcd/listJson?token=f725d2871e1412b720882dcb6b78c813&prcd=" + seawallCode, JSONObject.class);
        JSONArray jsonArray = jsonObject.getJSONArray("rows");
        if (CollectionUtils.isNotBlank(jsonArray)) {
            if (CollectionUtils.isNotBlank(list)) {
                Map<String, SeawallPointResVo> map = list.stream().collect(Collectors.toMap(SeawallPointResVo::getSeawallCode, Function.identity()));
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject row = jsonArray.getJSONObject(i);
                    String prcd = row.getString("prcd");
                    if (map.containsKey(prcd)) {
                        SeawallPointResVo seawallPointResVo = map.get(prcd);
                        row.put("longitude", seawallPointResVo.getLongitude());
                        row.put("latitude", seawallPointResVo.getLatitude());
                        row.put("seawallName",seawallPointResVo.getSeawallName());
                    }
                }
            }
        }
        return jsonArray;
    }
}
