package com.ygkj.gragh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.enums.StationType;
import com.ygkj.gragh.mapper.AttAdcdBaseMapper;
import com.ygkj.gragh.mapper.AttStBaseMapper;
import com.ygkj.gragh.mapper.PrecedeAffectRainfallMapper;
import com.ygkj.gragh.mapper.SynRainAndWaterMapper;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.PrecedeAffectRainfall;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.gragh.service.PrecedeAffectRainfallService;
import com.ygkj.gragh.vo.request.PrecedeAffectRainfallQueryVo;
import com.ygkj.gragh.vo.request.StationRequestVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.ExportUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/9/24
 */
@Slf4j
@Service
public class PrecedeAffectRainfallServiceImpl implements PrecedeAffectRainfallService {

    @Resource
    private PrecedeAffectRainfallMapper precedeAffectRainfallMapper;

    @Resource
    private SynRainAndWaterMapper rainAndWaterMapper;

    @Resource
    private AttAdcdBaseMapper adcdBaseMapper;

    @Resource
    private AttStBaseMapper stationMapper;

    // 基准计算日期
    final static LocalDate beginLd = LocalDate.of(2021, 8, 1);

    final static Date beginDate = DateUtils.localDate2Date(beginLd);

    final static DateTimeFormatter standardDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    final static DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    final static BigDecimal factor = BigDecimal.valueOf(0.985);

    final static Integer parThreshold = 100;

    final static BigDecimal parValue = BigDecimal.valueOf(parThreshold);

    // 水文系统中每天八点才是一天的起点
    final static LocalTime dateStart = LocalTime.of(8, 0, 0);

    final static long dayMillis = 24 * 3600 * 1000;


    /**
     * 前期影响雨量曲线
     *
     * @param areaCode
     * @return
     */
    @Override
    public List<PrecedeAffectRainfall> precedeAffectRainfallCurve(String areaCode) {
        if (StringUtils.isBlank(areaCode)) {
            areaCode = "330300000000";
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDate start = today.minusDays(30);
        return precedeAffectRainfallMapper.selectByAreaCodeTimeInterval(areaCode, DateUtils.localDate2Date(start), DateUtils.localDate2Date(today));
    }

    /**
     * 前期影响雨量列表
     *
     * @param theDay
     * @return
     */
    @Override
    public List<PrecedeAffectRainfall> todayPrecedeAffectRainfall(Date theDay) {
//        if (theDay == null) {
//            LocalDateTime now = LocalDateTime.now();
//            LocalDateTime stamp = now.withHour(8).withMinute(0).withSecond(0).withNano(0);
//            if (now.isBefore(stamp)) {
//                theDay = LocalDate.now().minusDays(1);
//            } else {
//                theDay = LocalDate.now();
//            }
//        }
//        List<AttAdcdBase> adcdList = adcdBaseMapper.selectWenZhouCounty();
//
//        List<PrecedeAffectRainfall> result = precedeAffectRainfallMapper.selectByAreaCodeListTime(adcdList.stream().map(AttAdcdBase::getAdcd).collect(Collectors.toList()), DateUtils.localDate2Date(theDay));
//        if (CollectionUtils.isNotBlank(result)) {
//            Map<String,String> adcdMap=adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd,AttAdcdBase::getAdnm));
//            for (PrecedeAffectRainfall precedeAffectRainfall : result) {
//                precedeAffectRainfall.setAreaName(adcdMap.get(precedeAffectRainfall.getAreaCode()));
//            }
//        }else{
//            List<PrecedeAffectRainfall> list = precedeAffectRainfallMapper.selectNewest();
//            list = list == null ? Collections.emptyList() : list;
//            Map<String, PrecedeAffectRainfall> map = list.stream().collect(Collectors.toMap(PrecedeAffectRainfall::getAreaCode, Function.identity()));
////            AttAdcdBase adcd = new AttAdcdBase();
////            adcd.setAdcd("330300000000");
////            adcd.setAdnm("温州市");
////            adcdList.add(adcd);
//            StationRequestVo reqVo = new StationRequestVo();
//            reqVo.setIsRainStation(true);
//            List<AttStBase> stBases = this.stationMapper.selectByCondition(reqVo);
//            Map<String, List<AttStBase>> stBaseMap = stBases.stream()
//                    .filter(o -> StringUtils.isNotBlank(o.getAreaCode()))
//                    .peek(o -> o.setAreaCode(o.getAreaCode().substring(0, 6).concat("000000"))).collect(Collectors.groupingBy(AttStBase::getAreaCode));
//
//            LocalDateTime now = LocalDateTime.now();
//
//            LocalDateTime startStamp = theDay.atTime(LocalTime.of(8, 0, 0, 0));
//            LocalDateTime endStamp = startStamp.plusDays(1);
//            Date yesterday = DateUtils.localDate2Date(theDay.minusDays(1));
//            String startTime = startStamp.format(standardDtf);
//            String endTime = endStamp.format(standardDtf);
//            List<String> tables = Arrays.asList(DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(), startTime, endTime));
//            result = new ArrayList<>(adcdList.size());
//            for (AttAdcdBase tempAdcd : adcdList) {
//                List<AttStBase> baseList = stBaseMap.getOrDefault(tempAdcd.getAdcd(), Collections.emptyList());
//                if (CollectionUtils.isBlank(baseList)) {
//                    if (!tempAdcd.getAdcd().equals("330300000000")) {
//                        continue;
//                    }
//                }
//                PrecedeAffectRainfall start = null;
//                if ((start = map.get(tempAdcd.getAdcd())) == null) {
//                    start = new PrecedeAffectRainfall();
//                    start.setDate(beginDate);
//                    start.setAreaCode(tempAdcd.getAdcd());
//                    start.setDrp(BigDecimal.valueOf(100));
//                }
//                PrecedeAffectRainfall pre = start;
//                Date tempDate = new Date(start.getDate().getTime() + dayMillis);
//                while (!tempDate.after(yesterday)) {
//                    BigDecimal avg = this.precedeAffectRainfallMapper.selectAvgDayRainfall(baseList == null ? null : baseList.stream().map(AttStBase::getStCode).collect(Collectors.toList()), tempDate);
//                    avg = avg == null ? BigDecimal.ZERO : avg;
//                    BigDecimal drp = pre.getDrp().multiply(BigDecimal.valueOf(0.985)).add(avg);
//                    if (drp.doubleValue() > 100) {
//                        drp = BigDecimal.valueOf(100);
//                    }
//                    pre = new PrecedeAffectRainfall();
//                    pre.setDate(tempDate);
//                    pre.setAreaCode(tempAdcd.getAdcd());
//                    pre.setDrp(drp);
//                    tempDate = new Date(tempDate.getTime() + dayMillis);
//                }
//                List<StPptnR> stPptnR1 = null;
//                if (CollectionUtils.isNotBlank(baseList)) {
//                    List<String> codes = baseList.stream().filter(attStBase -> attStBase.getStCode().startsWith("SWZ_"))
//                            .map(AttStBase::getStCode).collect(Collectors.toList());
//                    List<String> yjCodes = baseList.stream().filter(attStBase -> attStBase.getStCode().startsWith("YJ_"))
//                            .map(attStBase -> attStBase.getStCode().replaceAll("YJ_", "")).collect(Collectors.toList());
//                    stPptnR1 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, startTime, endTime, codes);
//                    stPptnR1.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(startTime, endTime, yjCodes));
//                } else {
//                    stPptnR1 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, startTime, endTime, null);
//                    stPptnR1.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(startTime, endTime, null));
//                }
//                BigDecimal avg = BigDecimal.valueOf(stPptnR1.stream().mapToDouble(StPptnR::getDrp).average().orElse(0d));
//                avg = avg == null ? BigDecimal.ZERO : avg;
//                BigDecimal drp = pre.getDrp().multiply(BigDecimal.valueOf(0.985)).add(avg);
//                if (drp.doubleValue() > 100) {
//                    drp = BigDecimal.valueOf(100);
//                }
//                pre = new PrecedeAffectRainfall();
//                pre.setDate(tempDate);
//                pre.setAreaCode(tempAdcd.getAdcd());
//                pre.setAreaName(tempAdcd.getAdnm());
//                pre.setDrp(drp);
//                result.add(pre);
//            }
//        }
//        result.stream().filter(o -> o.getDrp() != null).max(Comparator.comparing(PrecedeAffectRainfall::getDrp)).ifPresent(o -> o.setValueType(3));
//        result.stream().filter(o -> o.getDrp() != null).min(Comparator.comparing(PrecedeAffectRainfall::getDrp)).ifPresent(o -> o.setValueType(1));
//        return result;
        return pagePrecedeAffectRainfall(Builder.of(PrecedeAffectRainfallQueryVo::new)
                .with(PrecedeAffectRainfallQueryVo::setStartDate, theDay)
                .with(PrecedeAffectRainfallQueryVo::setEndDate, theDay)
                .build());
    }

    /**
     * 前期影响雨量c位窗口
     *
     * @return
     */
    @Override
    public JSONObject precedeAffectRainfall4CWindow() {
        List<PrecedeAffectRainfall> list = todayPrecedeAffectRainfall(null);
        JSONObject result = new JSONObject();
        PrecedeAffectRainfall aridest = list.stream().filter(o -> o.getDrp() != null).min(Comparator.comparing(PrecedeAffectRainfall::getDrp)).orElse(null);
        PrecedeAffectRainfall moistest = list.stream().filter(o -> o.getDrp() != null).max(Comparator.comparing(PrecedeAffectRainfall::getDrp)).orElse(null);
        result.put("aridest", aridest);
        result.put("moistest", moistest);
        return result;
    }

    /**
     * 前期影响雨量列表 分页查询
     *
     * @param queryVo
     * @return
     */
    @Override
    public List<PrecedeAffectRainfall> pagePrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo) {
        Date today = DateUtils.localDate2Date(getStartTime(LocalDateTime.now()).toLocalDate());
        if (queryVo.getStartDate() == null || queryVo.getEndDate() == null) {
            queryVo.setStartDate(today);
            queryVo.setEndDate(today);
        }
        queryVo.setStartDate(DateUtils.clearDateTimePrecision(queryVo.getStartDate(), Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND));
        queryVo.setEndDate(DateUtils.clearDateTimePrecision(queryVo.getEndDate(), Calendar.HOUR, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND));
        if (queryVo.getStartDate().before(beginDate)) {
            queryVo.setStartDate(beginDate);
        }
        if (queryVo.getEndDate().after(today)) {
            queryVo.setEndDate(today);
        }
        List<AttAdcdBase> adcdList = Collections.emptyList();
        if (StringUtils.isNotBlank(queryVo.getAreaCode())) {
            AttAdcdBase temp = adcdBaseMapper.load(queryVo.getAreaCode());
            if (temp != null) {
                adcdList = Collections.singletonList(temp);
            }
        } else {
            adcdList = adcdBaseMapper.selectWenZhouCounty();
        }
        if (CollectionUtils.isBlank(adcdList)) {
            return Collections.emptyList();
        }

        Date startPreDate = new Date(queryVo.getStartDate().getTime() - dayMillis);

        List<PrecedeAffectRainfall> paRainList = precedeAffectRainfallMapper.selectByAreaCodeListTimeInterval(adcdList.stream().map(AttAdcdBase::getAdcd).collect(Collectors.toList()),
                startPreDate,// 取起始日期前一天，方便计算起始当天的数据
                queryVo.getEndDate());
        Map<String, Map<Date, PrecedeAffectRainfall>> paRainMap = paRainList.stream().collect(Collectors.groupingBy(PrecedeAffectRainfall::getAreaCode, Collectors.toMap(PrecedeAffectRainfall::getDate, Function.identity())));

        StationRequestVo reqVo = new StationRequestVo();
        reqVo.setIsRainStation(true);
        List<AttStBase> stBases = this.stationMapper.selectByCondition(reqVo);
        Map<String, List<AttStBase>> stBaseMap = stBases.stream()
                .filter(o -> StringUtils.isNotBlank(o.getAreaCode()))
                .peek(o -> o.setAreaCode(o.getAreaCode().substring(0, 6).concat("000000"))).collect(Collectors.groupingBy(AttStBase::getAreaCode));

//        List<PrecedeAffectRainfall> newestList = precedeAffectRainfallMapper.selectNewest();
//        newestList = newestList == null ? Collections.emptyList() : newestList;
//        Map<String, PrecedeAffectRainfall> newestMap = newestList.stream().collect(Collectors.toMap(PrecedeAffectRainfall::getAreaCode, Function.identity()));

        // 预先计算日期，减少GC压力
        List<Date> durationList = new ArrayList<>((int) (DateUtils.getPeriod(queryVo.getStartDate(), queryVo.getEndDate()).get(ChronoUnit.DAYS) + 1));

        Date tempDate = queryVo.getStartDate();
        while (!tempDate.after(queryVo.getEndDate())) {
            durationList.add(tempDate);
            tempDate = new Date(tempDate.getTime() + dayMillis);
        }

        // 初始化返回值数组
        List<PrecedeAffectRainfall> result = new ArrayList<>((int) (adcdList.size() * (DateUtils.getPeriod(queryVo.getStartDate(), queryVo.getEndDate()).get(ChronoUnit.DAYS) + 1)));

        for (AttAdcdBase tempAdcd : adcdList) {
            String adcd = tempAdcd.getAdcd();
            List<AttStBase> baseList = stBaseMap.getOrDefault(adcd, Collections.emptyList());
            if (CollectionUtils.isBlank(baseList)) {
                continue;
            }
//            PrecedeAffectRainfall newestPar= newestMap.get(adcd);
            Map<Date, PrecedeAffectRainfall> dateRainMap = paRainMap.getOrDefault(adcd, Collections.emptyMap());
            PrecedeAffectRainfall pre = dateRainMap.getOrDefault(startPreDate, buildBeginDatePar(adcd));
            for (Date date : durationList) {
                PrecedeAffectRainfall current = dateRainMap.get(date);
                if (current == null) {
                    BigDecimal preDrp = pre == null ? parValue : pre.getDrp();
                    BigDecimal avg = date.equals(today) ? computeAvg(baseList, date) : computeTodayAvg(baseList);
                    current = computePrecedeAffectRainfall(preDrp, avg, date, adcd);
                }
                pre = current;
                result.add(current);
            }
        }
        // 最后填一下行政区划
        Map<String, String> adcdMap = adcdList.stream().collect(Collectors.toMap(AttAdcdBase::getAdcd, AttAdcdBase::getAdnm));
        for (PrecedeAffectRainfall precedeAffectRainfall : result) {
            precedeAffectRainfall.setAreaName(adcdMap.get(precedeAffectRainfall.getAreaCode()));
        }
        result.stream().filter(o -> o.getDrp() != null).max(Comparator.comparing(PrecedeAffectRainfall::getDrp)).ifPresent(o -> o.setValueType(3));
        result.stream().filter(o -> o.getDrp() != null).min(Comparator.comparing(PrecedeAffectRainfall::getDrp)).ifPresent(o -> o.setValueType(1));
        return result;
    }

    /**
     * @param preDrp
     * @param avgDrp
     * @param tempDate
     * @param adcd
     */
    public PrecedeAffectRainfall computePrecedeAffectRainfall(BigDecimal preDrp, BigDecimal avgDrp, Date tempDate, String adcd) {
        avgDrp = avgDrp == null ? BigDecimal.ZERO : avgDrp;
        BigDecimal drp = preDrp.multiply(factor).add(avgDrp);
        if (drp.doubleValue() > parThreshold) {
            drp = parValue;
        }
        return buildPar(adcd, drp, tempDate);
    }

    public BigDecimal computeAvg(List<AttStBase> baseList, Date tempDate) {
        BigDecimal avg = this.precedeAffectRainfallMapper.selectAvgDayRainfall(baseList == null ? null : baseList.stream().map(AttStBase::getStCode).collect(Collectors.toList()), tempDate);
        avg = avg == null ? BigDecimal.ZERO : avg;
        return avg;
    }

    public BigDecimal computeTodayAvg(List<AttStBase> baseList) {
        List<StPptnR> stPptnR1 = null;
        LocalDateTime endDt = LocalDateTime.now();
        LocalDateTime startDt = getStartTime(endDt);
        String startTime = startDt.format(standardDtf);
        String endTime = endDt.format(standardDtf);
        List<String> tables = Arrays.asList(DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(), startTime, endTime));
        if (CollectionUtils.isNotBlank(baseList)) {
            List<String> codes = baseList.stream().filter(attStBase -> attStBase.getStCode().startsWith("SWZ_"))
                    .map(AttStBase::getStCode).collect(Collectors.toList());
            List<String> yjCodes = baseList.stream().filter(attStBase -> attStBase.getStCode().startsWith("YJ_"))
                    .map(attStBase -> attStBase.getStCode().replaceAll("YJ_", "")).collect(Collectors.toList());
            stPptnR1 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, startTime, endTime, codes);
            stPptnR1.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(startTime, endTime, yjCodes));
        } else {
            stPptnR1 = rainAndWaterMapper.selectSumRainfallByTimeInterval(tables, startTime, endTime, null);
            stPptnR1.addAll(rainAndWaterMapper.selectYjSumRainfallByTimeInterval(startTime, endTime, null));
        }
        return BigDecimal.valueOf(stPptnR1.stream().mapToDouble(StPptnR::getDrp).average().orElse(0d));
    }

    public LocalDateTime getStartTime(LocalDateTime endTime) {
        if (!endTime.toLocalTime().isAfter(dateStart)) {
            return endTime.minusDays(1).toLocalDate().atTime(dateStart);
        } else {
            return endTime.toLocalDate().atTime(dateStart);
        }
    }

    public PrecedeAffectRainfall buildBeginDatePar(String adcd) {
        return buildPar(adcd, parValue, DateUtils.localDate2Date(beginLd));
    }

    public PrecedeAffectRainfall buildPar(String adcd, BigDecimal drp, Date date) {
        PrecedeAffectRainfall temp = new PrecedeAffectRainfall();
        temp.setDate(date);
        temp.setAreaCode(adcd);
        temp.setDrp(drp);
        return temp;
    }

    /**
     * 前期影响雨量列表-详表统计
     *
     * @param queryVo
     * @return
     */
    @Override
    public JSONObject stats4PrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo) {
        List<PrecedeAffectRainfall> list = this.pagePrecedeAffectRainfall(queryVo);
        JSONObject result = new JSONObject();
        PrecedeAffectRainfall aridest = list.stream().filter(o -> o.getDrp() != null).min(Comparator.comparing(PrecedeAffectRainfall::getDrp)).orElse(null);
        PrecedeAffectRainfall moistest = list.stream().filter(o -> o.getDrp() != null).max(Comparator.comparing(PrecedeAffectRainfall::getDrp)).orElse(null);
        if (aridest != null) {
            aridest.setDrp(aridest.getDrp().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        if (moistest != null) {
            moistest.setDrp(moistest.getDrp().setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        result.put("aridest", aridest);
        result.put("moistest", moistest);
        return result;
    }

    /**
     * 前期影响雨量列表-详表统计
     *
     * @param queryVo
     * @param response
     */
    @Override
    public void exportPrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo, HttpServletResponse response) {
        queryVo.noPage();
        List<PrecedeAffectRainfall> list = this.pagePrecedeAffectRainfall(queryVo);
        if (queryVo.getExportStrategy() == null) {
            queryVo.setExportStrategy(0);
        }
        switch (queryVo.getExportStrategy()) {
            case 0: {
                Function<Object, Object> dateFormatMapping = o -> {
                    Date date = (Date) o;
                    if (date == null) {
                        return StringUtils.EMPTY;
                    } else {
                        return DateUtils.date2LocalDate(date).format(yyyyMMdd);
                    }
                };
                ExportUtils.exportExcel(list, "前期影响雨量",
                        new String[]{"行政区划", "时刻", "前期影响雨量(mm)"},
                        new String[]{"areaName", "date", "drp"}, response, PrecedeAffectRainfall.class, Collections.singletonMap("date", dateFormatMapping));

            }
            break;
        }
    }
}
