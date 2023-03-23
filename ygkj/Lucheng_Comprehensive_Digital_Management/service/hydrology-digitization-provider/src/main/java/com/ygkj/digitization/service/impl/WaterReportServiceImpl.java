package com.ygkj.digitization.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.mapper.StPptnRAssistMapper;
import com.ygkj.digitization.mapper.SynRainAndWaterMapper;
import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.model.SumRainfallPeriods;
import com.ygkj.digitization.service.WaterReportService;
import com.ygkj.digitization.vo.request.StationRequestVo;
import com.ygkj.digitization.vo.response.StPptnRAssistResp;
import com.ygkj.enums.StationType;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.ExportUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ygkj.com.util.DateUtil;
import ygkj.com.util.MydateUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service("waterReportService")
public class WaterReportServiceImpl implements WaterReportService {

    @Autowired
    private AttStBaseMapper stBaseMapper;
    @Autowired
    private SynRainAndWaterMapper rainAndWaterMapper;
    @Autowired
    private StPptnRAssistMapper stPptnRAssistMapper;

    @Override
    public LinkedList<Map<String, Object>> partitionRainfall(StationRequestVo requestVo) throws ParseException {
        String[] tableNames = DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(),
                requestVo.getStartTime(), requestVo.getEndTime());//表名称list

        StationRequestVo requestVo2 = new StationRequestVo();
        requestVo2.setIsRainStation(Boolean.TRUE);
        if (StringUtils.isNotBlank(requestVo.getAreaCode())) {
            requestVo2.setAreaCode(requestVo.getAreaCode());
        }
        if (StringUtils.isNotBlank(requestVo.getStationName())) {
            requestVo2.setStationName(requestVo.getStationName());
        }
        List<AttStBase> attStBases = stBaseMapper.selectByConditionT(requestVo2);//测站集合

        Map<String, List<AttStBase>> stBaseListMap = attStBases.stream().filter(e -> StringUtils.isNotBlank(e.getAreaName()))
                .collect(Collectors.groupingBy(AttStBase::getAreaName));

        List<SumRainfallPeriods> sumRainfallPeriods = rainAndWaterMapper.sumRainfallPeriods(Arrays.asList(tableNames),
                attStBases.stream().map(e -> e.getStCode()).collect(Collectors.toList()), requestVo.getStartTime(), requestVo.getEndTime())
                .stream().filter(e -> e.getDrp() != null).collect(Collectors.toList());

        for (List<AttStBase> attStBaseList : stBaseListMap.values()) {
            for (AttStBase attStBase : attStBaseList) {
                List<Double> drpList = sumRainfallPeriods.stream().filter(e -> e.getStcd().equals(attStBase.getStCode()))
                        .map(k -> k.getDrp()).collect(Collectors.toList());
                if (!drpList.isEmpty()) {
                    Double drp = drpList.get(0);
                    attStBase.setDrp(drp);
                }
            }
        }

        BigDecimal totalBg = new BigDecimal("0.00");

        List<AttStBase> attStBasest = new ArrayList<>();

        LinkedList<Map<String, Object>> lkl = new LinkedList<>();

        Iterator<Map.Entry<String, List<AttStBase>>> iterator = stBaseListMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<AttStBase>> entry = iterator.next();
            String areaName = entry.getKey();
            List<AttStBase> attStBaseList = entry.getValue();

            double sum = attStBaseList.stream().filter(k -> k.getDrp() != null).mapToDouble(AttStBase::getDrp).sum();

            totalBg = totalBg.add(new BigDecimal(sum + ""));

            Map<String, Object> map = new HashMap<>();

            if (attStBaseList.size() == 0) {
                map.put("averageRain", "");
            } else {
                attStBasest.addAll(attStBaseList);
                map.put("averageRain", new BigDecimal(sum + "").divide(new BigDecimal(attStBaseList.size() + "")
                        , 2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            if (attStBaseList.size() < 10) {
                map.put("data", reconstitutRainfallStation(attStBaseList.stream().sorted(Comparator.comparing(AttStBase::getDrp,
                        Comparator.reverseOrder())).collect(Collectors.toList())));
            } else {
                List<AttStBase> stBases = attStBaseList.stream().filter(k -> k.getDrp() != null).sorted(Comparator.comparing(AttStBase::getDrp,
                        Comparator.reverseOrder())).limit(10).collect(Collectors.toList());
                map.put("data", reconstitutRainfallStation(stBases));
            }
            map.put("areaName", areaName);
            lkl.add(map);
        }

        //合计计算
        Map<String, Object> map = new HashMap<>();
        List<AttStBase> stBases = attStBasest.stream().filter(k -> k.getDrp() != null).sorted(Comparator.comparing(AttStBase::getDrp,
                Comparator.reverseOrder())).limit(10).collect(Collectors.toList());
        map.put("data", reconstitutRainfallStation(stBases));
        double totalSum = attStBasest.stream().filter(k -> k.getDrp() != null).mapToDouble(AttStBase::getDrp).sum();
        Double averageRain;
        int size = attStBasest.stream().filter(k -> k.getDrp() != null).
                collect(Collectors.toList()).size();
        if (size != 0){
            averageRain = new BigDecimal(totalSum + "").divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }else {
            averageRain = 0.00;
        }
        map.put("averageRain", averageRain);
        map.put("areaName", "合计");
        lkl.add(map);
        return lkl;
    }

    @Override
    public LinkedList<Object> periodExtremumRain(StationRequestVo requestVo) {
        long startTime = System.currentTimeMillis();
        String[] tableNames = DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(),
                requestVo.getStartTime(), requestVo.getEndTime());//表名称list

        Date startDate = DateUtil.parseDate("yyyy-MM-dd HH", requestVo.getStartTime());
        Date endDate = DateUtil.parseDate("yyyy-MM-dd HH", requestVo.getEndTime());

        Integer hourBetween = DateUtils.hourBetween(requestVo.getStartTime() + ":00:00", requestVo.getEndTime() + ":00:00");

        LinkedList<Object> list = new LinkedList<>();
        if (StringUtils.isEmpty(requestVo.getTimeTnterval())) {
            //1:1小时 3:3小时 6:6小时 12:12小时 24:24小时
//            int[] times = new int[]{1, 3, 6, 12, 24};
            int[] times = new int[]{1, 3, 6};
            for (int typeInt : times) {
                if (hourBetween < typeInt) {
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                if (1 == typeInt) {
                    List<JSONObject> reList = stPptnRAssistMapper.findIntervalDataRainOneRain(requestVo);
                    map.put("timeType", typeInt + "小时");
                    map.put("listData", reList);
                } else if (3== typeInt) {
                    List<JSONObject> reList = stPptnRAssistMapper.findIntervalDataRainThreeRain(requestVo);
                    map.put("timeType", typeInt + "小时");
                    map.put("listData", reList);
                } else {
                    map.put("timeType", typeInt + "小时");
                    map.put("listData", intervalDataRain(endDate, typeInt, requestVo, tableNames));
                }
                list.add(map);
            }
        } else {
            if (hourBetween < Integer.parseInt(requestVo.getTimeTnterval())) {
                return null;
            }
            Map<String, Object> map = new HashMap<>();
            if (Integer.parseInt(requestVo.getTimeTnterval()) == 1) {
                List<JSONObject> reList = stPptnRAssistMapper.findIntervalDataRainOneRain(requestVo);
                map.put("timeType", requestVo.getTimeTnterval() + "小时");
                map.put("listData", reList);
            } else if (3== Integer.parseInt(requestVo.getTimeTnterval())) {
                List<JSONObject> reList = stPptnRAssistMapper.findIntervalDataRainThreeRain(requestVo);
                map.put("timeType", requestVo.getTimeTnterval() + "小时");
                map.put("listData", reList);
            } else {
                map.put("timeType", requestVo.getTimeTnterval() + "小时");
                map.put("listData", intervalDataRain(endDate, Integer.parseInt(requestVo.getTimeTnterval()), requestVo, tableNames));
            }
            list.add(map);

        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
        return list;
    }

    public List<JSONObject> intervalDataRain(Date endDate, int typeInt, StationRequestVo requestVo, String[] tableNames) {
        StationRequestVo requestVo2 = new StationRequestVo();
        requestVo2.setIsRainStation(Boolean.TRUE);
        if (StringUtils.isNotBlank(requestVo.getAreaCode())) {
            requestVo2.setAreaCode(requestVo.getAreaCode());
        }
        List<AttStBase> attStBases = stBaseMapper.selectByConditionT(requestVo2);//测站集合
        List<JSONObject> reList = new ArrayList<>();

        long endTimeLo = endDate.getTime();
        String startTimeNew = "", endTimeNew;
        int i = 0;
        boolean whileFlag = true;
        do {
            if (i == 0) {
                startTimeNew = requestVo.getStartTime();
                endTimeNew = DateUtil.format(MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH", startTimeNew),
                        null, null, null, +typeInt, null, null), "yyyy-MM-dd HH");
                i++;
            } else {
                startTimeNew = DateUtil.format(MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH", startTimeNew),
                        null, null, null, +1, null, null), "yyyy-MM-dd HH");
                endTimeNew = DateUtil.format(MydateUtil.getNextDay(DateUtil.parseDate("yyyy-MM-dd HH", startTimeNew),
                        null, null, null, +typeInt, null, null), "yyyy-MM-dd HH");
            }


            List<SumRainfallPeriods> rainfallPeriods = rainAndWaterMapper.selectMaxRainDataTbs(Arrays.asList(tableNames),
                    startTimeNew, endTimeNew, attStBases.stream().map(e -> e.getStCode()).collect(Collectors.toList()));

            for (AttStBase attStBase : attStBases) {
                Double drp = attStBase.getDrp();
                List<SumRainfallPeriods> periods = rainfallPeriods.stream().filter(e -> e.getStcd().equals(attStBase.getStCode())).collect(Collectors.toList());
                if (drp == null) {
                    if (!periods.isEmpty()) {
                        Double drpDb = periods.get(0).getDrp();
                        if (drpDb != null) {
                            attStBase.setDrp(drpDb);
                            attStBase.setDrpTime(startTimeNew + "时-" + endTimeNew.split(" ")[1] + "时");
                        }
                    }
                } else {
                    if (!periods.isEmpty() && periods.get(0).getDrp() != null && periods.get(0).getDrp() > drp) {
                        attStBase.setDrp(periods.get(0).getDrp());
                        attStBase.setDrpTime(startTimeNew + "时-" + endTimeNew.split(" ")[1] + "时");
                    }
                }
            }

            if (DateUtil.parseDate("yyyy-MM-dd HH", endTimeNew).getTime() >= endTimeLo) {
                whileFlag = false;
            }
        } while (whileFlag);
        JSONObject jo = null;
        for (AttStBase attStBase : attStBases) {
            jo = new JSONObject();
            jo.put("tm", attStBase.getDrpTime());
            jo.put("stnm", attStBase.getStName());
            jo.put("data", attStBase.getDrp());

            reList.add(jo);
        }
        reList.sort(Comparator.comparing(o -> ((JSONObject)o).getDoubleValue("data")).reversed());
        return reList;
    }

    @Override
    public LinkedList<Map<String, Object>> dailyRainfall(StationRequestVo requestVo) {

        List<StPptnRAssistResp> pptnRAssistResps = stPptnRAssistMapper.dailyRainfall(requestVo.getStartTime() + " 00:00:00",
                requestVo.getEndTime() + " 23:59:59",requestVo.getAreaCode(),requestVo.getStationName());
        Map<String, List<StPptnRAssistResp>> collect = pptnRAssistResps.stream().filter(e -> StringUtils.isNotBlank(e.getAreaName()))
                .collect(Collectors.groupingBy(StPptnRAssistResp::getAreaName));//行政区划分组集合

        LinkedList<Map<String, Object>> lkl = new LinkedList<>();

        int stcdSize = 0;//测站有效数据数量
        BigDecimal totalAllBg = new BigDecimal("0.00");

        Iterator<Map.Entry<String, List<StPptnRAssistResp>>> iterator = collect.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<StPptnRAssistResp>> entry = iterator.next();
            String areaName = entry.getKey();
            List<StPptnRAssistResp> stPptnRAssistResps = entry.getValue();

            Map<String, Object> map = new HashMap<>();

            JSONObject jo = null;
            List<JSONObject> jsonObjectList = new LinkedList<>();

            Map<String, List<StPptnRAssistResp>> collect1 = stPptnRAssistResps.stream().filter(e -> StringUtils.isNotBlank(e.getStnm()))
                    .collect(Collectors.groupingBy(StPptnRAssistResp::getStcd));//分组测站集合
            Iterator<Map.Entry<String, List<StPptnRAssistResp>>> iterator1 = collect1.entrySet().iterator();
            List<StPptnRAssistResp> mapll = new ArrayList<>();
            BigDecimal totalAreaBg = new BigDecimal("0.00");
            while (iterator1.hasNext()) {
                stcdSize++;
                jo = new JSONObject();
                Map.Entry<String, List<StPptnRAssistResp>> entry1 = iterator1.next();
                List<StPptnRAssistResp> rAssistResps = entry1.getValue();
                String stationCode = entry1.getKey();

                double totalSum = rAssistResps.stream().filter(k -> k.getData() != null).mapToDouble(StPptnRAssistResp::getData).sum();
                totalAreaBg = totalAreaBg.add(new BigDecimal(totalSum + ""));
                totalAllBg = totalAllBg.add(new BigDecimal(totalSum + ""));

                jo.put("stnm", rAssistResps.get(0).getStnm());
                jo.put("totalRain", totalSum);
                jo.put("list", rAssistResps);
                jsonObjectList.add(jo);

            }

            StPptnRAssistResp pptnRAssistResp = new StPptnRAssistResp();
            pptnRAssistResp.setStnm("合计");
            pptnRAssistResp.setData(totalAreaBg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mapll.add(pptnRAssistResp);

            jo = new JSONObject();
            jo.put("stnm", "合计");
            jo.put("totalRain", totalAreaBg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            jo.put("list", mapll);
            jsonObjectList.add(jo);

            map.put("areaName", areaName);
            map.put("stList", jsonObjectList);
            lkl.add(map);
        }
        return lkl;
    }

    @Override
    public JSONObject rainstormWarning(StationRequestVo requestVo) {
        JSONObject jo = new JSONObject();
        if ("1".equals(requestVo.getRainstormType())) {
            List<StPptnRAssistResp> pptnRAssistRespsHour = stPptnRAssistMapper
                    .selectDataGreaterByHour(requestVo.getStartTime(), requestVo.getEndTime(),requestVo.getAreaCode(),requestVo.getStationName(),requestVo.getBas());
            if (!pptnRAssistRespsHour.isEmpty()) {
                for (StPptnRAssistResp stPptnRAssistResp : pptnRAssistRespsHour) {
                    String time = stPptnRAssistResp.getTime();
                    Date date = DateUtil.parseDate("yyyy-MM-dd HH", time);
                    Date nexthour = MydateUtil.getNextDay(date, null, null, null, +1,
                            null, null);
                    stPptnRAssistResp.setTimeFormat(DateUtil.format(date, "yyyy年MM月dd日HH时") + "至" +
                            DateUtil.format(nexthour, "yyyy年MM月dd日HH时"));
                }
                Map<String, List<StPptnRAssistResp>> collect = pptnRAssistRespsHour.stream().filter(e ->
                        StringUtils.isNotBlank(e.getAreaName()))
                        .collect(Collectors.groupingBy(StPptnRAssistResp::getAreaName));
                collect.values().forEach(stPptnRAssistResps -> stPptnRAssistResps.sort(Comparator.comparing(StPptnRAssistResp::getData).reversed()));
                jo.put("oneData", collect);
            } else {
                jo.put("oneData", null);
            }
        } else if ("2".equals(requestVo.getRainstormType())) {
            List<StPptnRAssistResp> pptnRAssistRespsThreeHour = stPptnRAssistMapper
                    .selectDataGreaterByThreeHour(requestVo.getStartTime(), requestVo.getEndTime(),requestVo.getAreaCode(),requestVo.getStationName(),requestVo.getBas());
            if (!pptnRAssistRespsThreeHour.isEmpty()) {
                for (StPptnRAssistResp stPptnRAssistResp : pptnRAssistRespsThreeHour) {
                    String time = stPptnRAssistResp.getTime();
                    Date date = DateUtil.parseDate("yyyy-MM-dd HH", time);
                    Date nexthour = MydateUtil.getNextDay(date, null, null, null, +3,
                            null, null);
                    stPptnRAssistResp.setTimeFormat(DateUtil.format(date, "yyyy年MM月dd日HH时") + "至" +
                            DateUtil.format(nexthour, "yyyy年MM月dd日HH时"));
                }
                Map<String, List<StPptnRAssistResp>> collect = pptnRAssistRespsThreeHour.stream().filter(e ->
                        StringUtils.isNotBlank(e.getAreaName()))
                        .collect(Collectors.groupingBy(StPptnRAssistResp::getAreaName));
                collect.values().forEach(stPptnRAssistResps -> stPptnRAssistResps.sort(Comparator.comparing(StPptnRAssistResp::getData).reversed()));
                jo.put("oneData", collect);
            } else {
                jo.put("oneData", null);
            }
        } else {
            List<StPptnRAssistResp> pptnRAssistRespsDay = stPptnRAssistMapper
                    .selectDataGreaterByDay(requestVo.getStartTime(), requestVo.getEndTime(),requestVo.getAreaCode(),requestVo.getStationName(),requestVo.getBas());

            if (!pptnRAssistRespsDay.isEmpty()) {
                for (StPptnRAssistResp stPptnRAssistResp : pptnRAssistRespsDay) {
                    String time = stPptnRAssistResp.getTime();
                    Date date = DateUtil.parseDate("yyyy-MM-dd HH", time);
                    Date nexthour = MydateUtil.getNextDay(date, null, null, +1, null,
                            null, null);
                    stPptnRAssistResp.setTimeFormat(DateUtil.format(date, "yyyy年MM月dd日") + "08时至" +
                            DateUtil.format(nexthour, "yyyy年MM月dd日") + "08时");
                }
                Map<String, List<StPptnRAssistResp>> collect = pptnRAssistRespsDay.stream().filter(e ->
                        StringUtils.isNotBlank(e.getAreaName())).collect(Collectors.groupingBy(StPptnRAssistResp::getAreaName));
                collect.values().forEach(stPptnRAssistResps -> stPptnRAssistResps.sort(Comparator.comparing(StPptnRAssistResp::getData).reversed()));
                jo.put("oneData", collect);
            } else {
                jo.put("oneData", null);
            }
        }
        return jo;
    }


    @Override
    public Object rainstormWarnForOne(StationRequestVo requestVo) {

        List<StPptnRAssistResp> pptnRAssistResps;
        if ("1".equals(requestVo.getRainstormType())) {
            pptnRAssistResps = stPptnRAssistMapper
                    .selectDataGreaterByHour(requestVo.getStartTime(), requestVo.getEndTime(),requestVo.getAreaCode(),requestVo.getStationName(),requestVo.getBas());
        } else if ("2".equals(requestVo.getRainstormType())) {
            pptnRAssistResps = stPptnRAssistMapper
                    .selectDataGreaterByThreeHour(requestVo.getStartTime(), requestVo.getEndTime(),requestVo.getAreaCode(),requestVo.getStationName(),requestVo.getBas());
        } else {
            pptnRAssistResps = stPptnRAssistMapper
                    .selectDataGreaterByDay(requestVo.getStartTime(), requestVo.getEndTime(),requestVo.getAreaCode(),requestVo.getStationName(),requestVo.getBas());
        }
        return pptnRAssistResps.size();
    }

    /**
     * 重组单站雨量str
     *
     * @param stBases
     * @return
     */
    public String reconstitutRainfallStation(List<AttStBase> stBases) {
        StringBuffer stringBuffer = new StringBuffer();
        for (AttStBase stBase : stBases) {
            if (stringBuffer.toString().length() == 0) {
                stringBuffer.append(stBase.getStName() + stBase.getDrp());
            } else {
                stringBuffer.append("," + stBase.getStName() + stBase.getDrp());
            }
        }
        return stringBuffer.toString();
    }


    @Override
    public void exportDailyRainfall(StationRequestVo requestVo, HttpServletResponse response) {
        LinkedList<Map<String, Object>> maps = dailyRainfall(requestVo);
        LinkedList<Map<String,Object>> result = new LinkedList<>();
        String[] str = new String[]{};
        for (Map<String,Object> map : maps){
            String areaName = (String) map.get("areaName");
            List<JSONObject> stList = (List<JSONObject>) map.get("stList");
            for (JSONObject st : stList){
                Map<String,Object> index = new HashMap<>();
                index.put("stnm",st.get("stnm"));
                index.put("totalRain",st.get("totalRain"));
                List<StPptnRAssistResp> list = (List<StPptnRAssistResp>) st.get("list");
                if (ObjectUtils.isNotEmpty(list)){
                    for (StPptnRAssistResp jo : list){
                        index.put(jo.getTime(),jo.getData());
                    }
                }
                result.add(index);
            }
        }
        if (CollectionUtils.isNotBlank(result)){
            //提取导出的表头
            Map<String, Object> map = result.get(1);
            Set<String> keySet = map.keySet();
            str = keySet.toArray(new String[]{});
        }

        ExportUtils.exportExcel(result,"日雨量",str,str,response);
    }


    @Override
    public void exportRainstormWarning(StationRequestVo requestVo, HttpServletResponse response) {
        JSONObject jsonObject = rainstormWarning(requestVo);
        List<StPptnRAssistResp> list = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(jsonObject.get("oneData"))){
            Map<String, List<StPptnRAssistResp>> data = (Map<String, List<StPptnRAssistResp>>)jsonObject.get("oneData");
            Set<String> areas = data.keySet();
            for (String area : areas){
                List<StPptnRAssistResp> stPptnRAssistResps = data.get(area);
                list.addAll(stPptnRAssistResps);
            }
        }

        ExportUtils.exportExcel(list,"暴雨警报",
                new String[]{"所属区域","站名","时间","暴雨量"},
                new String[]{"areaName","stnm","timeFormat","data"},response);

    }

    @Override
    public void exportPeriodExtremumRain(StationRequestVo requestVo, HttpServletResponse response) {
        //取出要导出的数据
        LinkedList<Object> objects = periodExtremumRain(requestVo);

        //创建Excel文件对象
        XSSFWorkbook wb =new XSSFWorkbook();
        //创建Sheet列
        XSSFSheet sheet = wb.createSheet("极值");
        //创建第一行，即表头
        XSSFRow row0 =sheet.createRow(0);

        row0.createCell(0).setCellValue("频次");
        row0.createCell(1).setCellValue("列表");

        for (int i = 0 ; i < objects.size() ; i++){
            Map<String, Object> map = (Map<String, Object>) objects.get(i);
            //合并单元格和列
            if (ObjectUtils.isEmpty(sheet.getRow((i+1) * 3 - 2))){
                XSSFRow row = sheet.createRow((i+1) * 3 - 2);
                sheet.addMergedRegion(new CellRangeAddress((i+1) * 3 - 2,(i+1) * 3,0,0));
                row.createCell(0).setCellValue(map.get("timeType").toString());
                row.createCell(1).setCellValue("站名");
                XSSFRow nextRow = ObjectUtils.isNotEmpty(sheet.getRow((i+1) * 3 - 1))? sheet.getRow((i+1) * 3 - 1): sheet.createRow((i+1) * 3 - 1);
                nextRow.createCell(1).setCellValue("时间");
                XSSFRow endRow = ObjectUtils.isNotEmpty(sheet.getRow((i+1) * 3))? sheet.getRow((i+1) * 3) : sheet.createRow((i+1) * 3);
                endRow.createCell(1).setCellValue("极值");
            }

            if (ObjectUtils.isNotEmpty(map.get("listData"))){
                List<JSONObject> list = (List<JSONObject>) map.get("listData");
                for (int j = 0; j < list.size(); j++) {
                    JSONObject obj = list.get(j);
                    //设置表头，根据最长列表最后得出
                    row0.createCell(2+j).setCellValue(j+1);
                    XSSFRow row = sheet.getRow((i+1) * 3 - 2 );
                    row.createCell(j + 2).setCellValue(obj.getString("stnm"));
                    XSSFRow nextRow = sheet.getRow((i+1) * 3 - 1);
                    nextRow.createCell(j + 2).setCellValue(obj.getString("tm"));
                    XSSFRow endRow = sheet.getRow( (i+1) * 3 );
                    endRow.createCell(j + 2).setCellValue(obj.getString("data"));
                }
            }
        }

        //导出到客户端
        try {
            String fileName="极值表";
            OutputStream output = response.getOutputStream();
            response.reset();

            response.setHeader("Content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setContentType("*/*");
            response.setCharacterEncoding("utf-8");
            wb.write(output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
