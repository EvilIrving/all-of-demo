package com.ygkj.digitization.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.mapper.SynRainAndWaterMapper;
import com.ygkj.digitization.service.SectFlowService;
import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.vo.response.SectFlowQueryReqVo;
import com.ygkj.digitization.vo.response.SectFlowResVo;
import com.ygkj.digitization.vo.response.ZqCurveConfigResVo;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StZqrlB;
import com.ygkj.gragh.vo.response.StPrrwtResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.ExportUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/31
 */
@Service
public class SectFlowServiceImpl implements SectFlowService {

    @Resource
    AttStBaseMapper stBaseMapper;

    @Resource
    SynRainAndWaterMapper rainAndWaterMapper;

    DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");

    final static Integer redWarnType = 2;
    final static Integer orangeWarnType = 1;

    /**
     * 查询断面流量列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<SectFlowResVo> getSectFlowList(SectFlowQueryReqVo reqVo) {
        List<SectFlowResVo> records = stBaseMapper.selectSectFlowByCondition(reqVo);
        records = records == null ? Collections.emptyList() : records;
        List<String> mainStcdList = records.stream().map(SectFlowResVo::getMainStCode).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (CollectionUtils.isBlank(mainStcdList)) {
            return records;
        }
        List<ZqCurveConfigResVo> configList = stBaseMapper.selectMultiNewestLnnmByStcds(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, mainStcdList));
        configList = configList == null ? Collections.emptyList() : configList;
        Map<String, String> configMap = configList.stream().collect(Collectors.toMap(ZqCurveConfigResVo::getMgstcd, ZqCurveConfigResVo::getLnnm));
        List<StZqrlB> zqrlList = this.stBaseMapper.selectZqCurveByStcdLnnm(configMap);
        zqrlList = zqrlList == null ? Collections.emptyList() : zqrlList;
        Map<String, List<StZqrlB>> zqrlGroupMap = zqrlList.stream().collect(Collectors.groupingBy(StZqrlB::getMgstcd));

        List<StRiverR> waterLevels = rainAndWaterMapper.selectRiverByTimeCode(StationType.RIVER.table() + LocalDate.now().format(yyyyMM), mainStcdList, null);
        waterLevels = waterLevels == null ? Collections.emptyList() : waterLevels;
        Map<String, StRiverR> waterLevelMap = waterLevels.stream().collect(Collectors.toMap(StRiverR::getMgstcd, Function.identity()));

        for (SectFlowResVo record : records) {
            if (StringUtils.isBlank(record.getMainStCode())) {
                continue;
            }
            List<StZqrlB> flowList = zqrlGroupMap.getOrDefault(record.getMainStCode(), Collections.emptyList());
            StRiverR waterLevel = waterLevelMap.get(record.getMainStCode());
            if (waterLevel != null && CollectionUtils.isNotBlank(flowList)) {
                BigDecimal flow = computeFlow(flowList, BigDecimal.valueOf(waterLevel.getZ()));
                if (flow != null) {
                    record.setFlow(flow.doubleValue());
                    // 判断警戒状态
                    if (record.getFlow() < record.getRedFlow()) {
                        record.setWarnType(redWarnType);
                    } else if (record.getFlow() < record.getOrangeFlow()) {
                        record.setWarnType(orangeWarnType);
                    }
                }
                record.setTime(waterLevel.getTm());
                record.setWaterLevel(waterLevel.getZ());
            }
        }
        if (reqVo.getWarnType() != null && (reqVo.getWarnType() == 1 || reqVo.getWarnType() == 2)) {
            records = records.stream().filter(o -> o.getWarnType() != null).filter(o -> reqVo.getWarnType().equals(o.getWarnType())).collect(Collectors.toList());
        }
        return records;
    }

    /**
     * 断面流量列表-表头统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject stats4SectFlowList(SectFlowQueryReqVo reqVo) {
        List<SectFlowResVo> list=getSectFlowList(reqVo);
        Map<Integer,Long> warnTypeNumMap=list.stream().collect(Collectors.groupingBy(SectFlowResVo::getWarnType,Collectors.counting()));
        Long normNum=warnTypeNumMap.getOrDefault(0,0L);
        Long redWarnNum=warnTypeNumMap.getOrDefault(1,0L);
        Long orangeWarnNum=warnTypeNumMap.getOrDefault(2,0L);
        JSONObject result=new JSONObject();
        result.put("normNum",normNum);
        result.put("redWarnNum",redWarnNum);
        result.put("orangeWarnNum",orangeWarnNum);
        return result;
    }

    /**
     * 导出断面流量列表
     *
     * @param reqVo
     * @param response
     */
    @Override
    public void exportSectFlowList(SectFlowQueryReqVo reqVo, HttpServletResponse response) {
        reqVo.noPage();
        if (reqVo.getExportStrategy()==null){
            reqVo.setExportStrategy(0);
        }
        List<SectFlowResVo> list=getSectFlowList(reqVo);
        switch (reqVo.getExportStrategy()){
            case 0:{
                ExportUtils.exportExcel(list,"流域生态流量",
                        new String[]{"断面名称","流域","实时流量(m³/s)","橙色预警指标(m³/s)","红色预警指标(m³/s)"},
                        new String[]{"sectName","bas","flow","orangeFlow","redFlow"},response,null);
            }
                break;
        }
    }

    /**
     * 计算根据单站的库容曲线和水位求库容
     *
     * @param curveList         单站库容曲线记录
     * @param currentWaterLevel 水位
     * @return 库容
     */
    private BigDecimal computeFlow(List<StZqrlB> curveList, BigDecimal currentWaterLevel) {
        if (org.springframework.util.CollectionUtils.isEmpty(curveList) || currentWaterLevel == null) {
            return null;
        }
        // 将曲线按水位升序
        curveList = curveList.stream().sorted(Comparator.comparing(StZqrlB::getZ)).collect(Collectors.toList());

        StZqrlB before = null, after = null;
        for (StZqrlB reservoirCapacityCurve : curveList) {
            //水位直接找到相等的值，直接返回
            if (reservoirCapacityCurve.getZ().compareTo(currentWaterLevel) == 0) {
                return reservoirCapacityCurve.getQ();
            } else if (reservoirCapacityCurve.getZ().doubleValue() < currentWaterLevel.doubleValue()) {
                before = reservoirCapacityCurve;
            } else if (reservoirCapacityCurve.getZ().doubleValue() > currentWaterLevel.doubleValue() && after == null) {
                after = reservoirCapacityCurve;
            }
        }
        //当传入水位小于最小水位返回最小库容
        if (before == null) {
            return null;
        }
        //当传入水位大于最大水位返回最大库容
        if (after == null) {
            return null;
        }

        // a = (x1-x2)/(y1-y2)
        BigDecimal a = (before.getQ().subtract(after.getQ()))
                .divide(before.getZ().subtract(after.getZ()), 20, BigDecimal.ROUND_DOWN);
        // b =   y1 -a*x1
        BigDecimal b = before.getQ().subtract(a.multiply(before.getZ()));

        BigDecimal capacity = a.multiply(currentWaterLevel).add(b);
        //返回结果保留四位小数
        return capacity.setScale(4, BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * 断面流量统计
     *
     * @return
     */
    @Override
    public JSONObject sectFlowStats() {
        List<SectFlowResVo> records = getSectFlowList(new SectFlowQueryReqVo());
        int totalNum = records.size();
        long ojNum = records.stream().filter(o -> "瓯江".equals(o.getBas())).count();
        long fyjNum = records.stream().filter(o -> "飞云江".equals(o.getBas())).count();
        long ajNum = records.stream().filter(o -> "鳌江".equals(o.getBas())).count();
        long normNum = records.stream().filter(o -> !orangeWarnType.equals(o.getWarnType()) && !redWarnType.equals(o.getWarnType())).count();
        long orangeWarnNum = records.stream().filter(o -> orangeWarnType.equals(o.getWarnType())).count();
        long redWarnNum = records.stream().filter(o -> redWarnType.equals(o.getWarnType())).count();
        JSONObject result = new JSONObject();
        result.put("totalNum", totalNum);
        result.put("ojNum", ojNum);
        result.put("fyjNum", fyjNum);
        result.put("ajNum", ajNum);
        result.put("normNum", normNum);
        result.put("orangeWarnNum", orangeWarnNum);
        result.put("redWarnNum", redWarnNum);
        return result;
    }

    /**
     * 断面流量曲线
     *
     * @param sectName 断面名称
     * @param hours    往前推的小时数
     * @return
     */
    @Override
    public List<StPrrwtResVo> sectFlowCurve(String sectName, Integer hours) {
        if (StringUtils.isBlank(sectName) || hours == null) {
            return Collections.emptyList();
        }
        List<SectFlowResVo> records = stBaseMapper.selectSectFlowByCondition(Builder.of(SectFlowQueryReqVo::new)
                .with(SectFlowQueryReqVo::setSectName, sectName).build());
        if (CollectionUtils.isBlank(records)) {
            return Collections.emptyList();
        }
        SectFlowResVo record = records.get(0);
        if (StringUtils.isBlank(record.getMainStCode())) {
            return Collections.emptyList();
        }
        String lnnm = this.stBaseMapper.selectNewestLnnm(record.getMainStCode());
        if (StringUtils.isBlank(lnnm)) {
            return Collections.emptyList();
        }
        List<StZqrlB> zqrlList = this.stBaseMapper.selectZqCurveBySingleStcdLnnm(record.getMainStCode(), lnnm);
        if (CollectionUtils.isBlank(zqrlList)) {
            return Collections.emptyList();
        }
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime()-hours*3600*1000);
        List<StPrrwtResVo> waterLevels = this.rainAndWaterMapper.selectRiverByTimeInterval(echoWaterRainTableByDateInternal(StationType.RIVER.table(), startTime, endTime), startTime, endTime, Arrays.asList(record.getMainStCode()));
        if (CollectionUtils.isBlank(waterLevels)) {
            return Collections.emptyList();
        }
        for (StPrrwtResVo waterLevel : waterLevels) {
            BigDecimal flow = computeFlow(zqrlList, BigDecimal.valueOf(waterLevel.getZ()));
            if (flow != null) {
                waterLevel.setFlow(flow.doubleValue());
            }
        }
        return waterLevels;
    }

    List<String> echoWaterRainTableByDateInternal(String tablePrefix, Date start, Date end) {
        if (org.apache.commons.lang3.StringUtils.isBlank(tablePrefix) || start == null || end == null) {
            return Collections.emptyList();
        }
        LocalDate startTemp = DateUtils.date2LocalDate(start);
        LocalDate endTemp = DateUtils.date2LocalDate(end);
        LinkedHashSet<String> sequenceSet = new LinkedHashSet<>();
        do {
            String table = tablePrefix + startTemp.format(yyyyMM);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(this.rainAndWaterMapper.checkTableExist(table))) {
                sequenceSet.add(table);
            }
            startTemp = startTemp.plusDays(1);
        } while (startTemp.isBefore(endTemp));
        return new ArrayList<>(sequenceSet);
    }
}
