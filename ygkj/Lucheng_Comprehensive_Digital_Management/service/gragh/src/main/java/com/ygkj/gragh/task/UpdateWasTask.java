package com.ygkj.gragh.task;

import com.ygkj.gragh.mapper.UpdateWasTaskMapper;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.gragh.model.StWasR;
import com.ygkj.project.model.StUpzDwz;
import com.ygkj.utils.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.ygkj.gragh.enums.StationType;


import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-12-16 18:34
 */
@Component
public class UpdateWasTask {

    @Resource
    public UpdateWasTaskMapper updateWasTaskMapper;

    @Scheduled(cron = "0 0/15 * * * ?")
    public void updateWas() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        calendar.add(Calendar.DATE, -1);
        String zero = month < 10 ? "0" : "";
        //获取水闸、河道水位、潮位三张表的表名
        String riverTable = StationType.RIVER.table().concat("" + year + zero + month);
        String tideTable = StationType.TIDE.table().concat("" + year + zero + month);
        String wasTable = StationType.GATE.table().concat("" + year + zero + month);
        //获取绑定关系
        List<StUpzDwz> list = updateWasTaskMapper.selectAll();
        List<String> list1 = list.stream().filter(o -> "0".equals(o.getFlag())).map(StUpzDwz::getUpzCode).collect(Collectors.toList());
        List<String> list2 = list.stream().filter(o -> "0".equals(o.getFlag())).map(StUpzDwz::getDwzCode).collect(Collectors.toList());
        List<String> list3 = list.stream().filter(o -> "0".equals(o.getFlag())).map(StUpzDwz::getStCode).collect(Collectors.toList());
        list1.addAll(list2);
        List<String> list4 = list1.stream().distinct().collect(Collectors.toList());
        //获取三个表的数据
        List<StRiverR> riverLevel = updateWasTaskMapper.selectRiverByTimeCode(riverTable, list4, null);
        List<StTideR> tideList = updateWasTaskMapper.selectTideByTimeCode(tideTable, list4, null);
        List<StWasR> wasList = updateWasTaskMapper.selectWasByTimeCode(wasTable, list3, null);
        Map<String, List<StRiverR>> map1 = riverLevel.stream().collect(Collectors.groupingBy(StRiverR::getStcd));
        Map<String, List<StTideR>> map2 = tideList.stream().collect(Collectors.groupingBy(StTideR::getStcd));
        for (StWasR stWasR : wasList) {
            for (StUpzDwz stUpzDwz : list) {
                if (stUpzDwz.getStCode() != null && stWasR.getStcd().equals(stUpzDwz.getStCode())) {
                    if (map1.size() > 0 && map1.containsKey(stUpzDwz.getUpzCode())) {
                        List<StRiverR> list5 = map1.get(stUpzDwz.getUpzCode());
                        for (StRiverR stRiverR : list5) {
                            calendar.setTime(stRiverR.getTm());
                            calendar.add(Calendar.HOUR, -1);
                            Date startTime = calendar.getTime();
                            calendar.add(Calendar.HOUR, 2);
                            Date endTime = calendar.getTime();
                            if (belongCalendar(stWasR.getTm(), startTime, endTime)) {
                                stWasR.setUpz(stRiverR.getZ());
                                stWasR.setFlag("1");
                            }
                        }
                    }
                    if (map1.size() > 0 && map1.containsKey(stUpzDwz.getDwzCode())) {
                        List<StRiverR> list5 = map1.get(stUpzDwz.getDwzCode());
                        for (StRiverR stRiverR : list5) {
                            calendar.setTime(stRiverR.getTm());
                            calendar.add(Calendar.HOUR, -1);
                            Date startTime = calendar.getTime();
                            calendar.add(Calendar.HOUR, 2);
                            Date endTime = calendar.getTime();
                            if (belongCalendar(stWasR.getTm(), startTime, endTime)) {
                                stWasR.setDwz(stRiverR.getZ());
                                stWasR.setFlag("1");
                            }
                        }
                    }
                    if (map2.size() > 0 && map2.containsKey(stUpzDwz.getUpzCode())) {
                        List<StTideR> list5 = map2.get(stUpzDwz.getUpzCode());
                        for (StTideR stTideR : list5) {
                            calendar.setTime(stTideR.getTm());
                            calendar.add(Calendar.MINUTE, -5);
                            Date startTime = calendar.getTime();
                            calendar.add(Calendar.MINUTE, 10);
                            Date endTime = calendar.getTime();
                            if (belongCalendar(stWasR.getTm(), startTime, endTime)) {
                                stWasR.setUpz(stTideR.getTdz());
                                stWasR.setFlag("1");
                            }

                        }
                    }
                    if (map2.size() > 0 && map2.containsKey(stUpzDwz.getDwzCode())) {
                        List<StTideR> list5 = map2.get(stUpzDwz.getDwzCode());
                        for (StTideR stTideR : list5) {
                            calendar.setTime(stTideR.getTm());
                            calendar.add(Calendar.MINUTE, -5);
                            Date startTime = calendar.getTime();
                            calendar.add(Calendar.MINUTE, 10);
                            Date endTime = calendar.getTime();
                            if (belongCalendar(stWasR.getTm(), startTime, endTime)) {
                                stWasR.setDwz(stTideR.getTdz());
                                stWasR.setFlag("1");
                            }
                        }
                    }
                }

            }

        }
        if(wasList.size()>0){
            for (StWasR stWasR : wasList) {
                updateWasTaskMapper.update(wasTable,stWasR);
            }
        }

    }


    @Scheduled(cron = "30 0/5 * * * ?")
    public void insertWas() {
        Calendar calendar = Calendar.getInstance();
        Date now = new Date();
        calendar.setTime(now);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        calendar.add(Calendar.DATE, -1);
        String zero = month < 10 ? "0" : "";
        //获取水闸、河道水位、潮位三张表的表名
        String riverTable = StationType.RIVER.table().concat("" + year + zero + month);
        String tideTable = StationType.TIDE.table().concat("" + year + zero + month);
        String wasTable = StationType.GATE.table().concat("" + year + zero + month);
        //获取绑定关系
        List<StUpzDwz> list = updateWasTaskMapper.selectAll();
        List<String> list1 = list.stream().filter(o -> "1".equals(o.getFlag())).map(StUpzDwz::getUpzCode).collect(Collectors.toList());
        List<String> list2 = list.stream().filter(o -> "1".equals(o.getFlag())).map(StUpzDwz::getDwzCode).collect(Collectors.toList());
        List<StUpzDwz> list3 = list.stream().filter(o -> "1".equals(o.getFlag())).collect(Collectors.toList());
        list1.addAll(list2);
        List<String> list4 = list1.stream().distinct().collect(Collectors.toList());
        //获取三个表的数据
        List<StRiverR> riverLevel = updateWasTaskMapper.selectRiverByTimeCode(riverTable, list4, null);
        List<StTideR> tideList = updateWasTaskMapper.selectTideByTimeCode(tideTable, list4, null);
        Map<String, StRiverR> latestStRiverRs = riverLevel.parallelStream().collect(Collectors.toMap(StRiverR::getStcd, Function.identity(), (c1, c2) -> c1.getTm().compareTo(c2.getTm()) >0 ? c1 : c2));
        Map<String, StTideR> latestStTideRs = tideList.parallelStream().collect(Collectors.toMap(StTideR::getStcd, Function.identity(), (c1, c2) -> c1.getTm().compareTo(c2.getTm()) >0 ? c1 : c2));
        if(list3.size()>0){
            for (StUpzDwz stUpzDwz : list3) {
                StWasR stWasR = new StWasR();
                stWasR.setMgstcd(stUpzDwz.getStCode());
                stWasR.setStcd(stUpzDwz.getStCode());
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
                stWasR.setTm(DateUtils.parseDate("yyyy-MM-dd HH:mm:00",sdf.format(new Date())));
                if(latestStRiverRs.size() > 0 && latestStRiverRs.containsKey(stUpzDwz.getUpzCode())){
                    stWasR.setUpz(latestStRiverRs.get(stUpzDwz.getUpzCode()).getZ());
                }
                if(latestStRiverRs.size() > 0 && latestStRiverRs.containsKey(stUpzDwz.getDwzCode())){
                    stWasR.setDwz(latestStRiverRs.get(stUpzDwz.getDwzCode()).getZ());
                }
                if(latestStTideRs.size() > 0 && latestStTideRs.containsKey(stUpzDwz.getUpzCode())){
                    stWasR.setUpz(latestStTideRs.get(stUpzDwz.getUpzCode()).getTdz());
                }
                if(latestStTideRs.size() > 0 && latestStTideRs.containsKey(stUpzDwz.getDwzCode())){
                    stWasR.setDwz(latestStTideRs.get(stUpzDwz.getDwzCode()).getTdz());
                }
                updateWasTaskMapper.insert(wasTable,stWasR);
            }
        }
    }


    public  boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        }else if(nowTime.compareTo(beginTime)==0 || nowTime.compareTo(endTime) == 0 ){
            return true;
        }else {
            return false;
        }
    }
}
