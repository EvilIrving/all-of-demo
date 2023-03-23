package com.ygkj.digitization.task;

import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.mapper.StPptnRAssistMapper;
import com.ygkj.digitization.mapper.SynRainAndWaterMapper;
import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.model.StPptnRAssist;
import com.ygkj.digitization.vo.PptnRAssistReq;
import com.ygkj.enums.StationType;
import com.ygkj.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import ygkj.com.util.DateUtil;
import ygkj.com.util.MydateUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RecursiveTask;


@Slf4j
public class CountTask extends RecursiveTask<Integer> {

    private List<AttStBase> attStBases;

    private SynRainAndWaterMapper rainAndWaterMapper;
    private StPptnRAssistMapper stPptnRAssistMapper;
    private AttStBaseMapper stBaseMapper;

    public CountTask(List<AttStBase> attStBases, SynRainAndWaterMapper rainAndWaterMapper, StPptnRAssistMapper stPptnRAssistMapper
    , AttStBaseMapper stBaseMapper) {
        this.attStBases = attStBases;
        this.rainAndWaterMapper = rainAndWaterMapper;
        this.stPptnRAssistMapper = stPptnRAssistMapper;
        this.stBaseMapper = stBaseMapper;
    }

    @Override
    protected Integer compute() {
        int num = 0;
        if (attStBases.size() <= 8) {
            try {
                num += insertCom(attStBases);
            } catch (ParseException e) {
                log.error("雨量辅助表数据同步错误,错误信息:{}", e.getMessage());
            }
        } else {
            int i = attStBases.size() / 2;
            CountTask countTaskLeft = new CountTask(attStBases.subList(0, i), rainAndWaterMapper, stPptnRAssistMapper
                    , stBaseMapper);
            CountTask countTaskRight = new CountTask(attStBases.subList(i,attStBases.size() -1), rainAndWaterMapper,  stPptnRAssistMapper
                    , stBaseMapper);
            // 执行子任务
            countTaskLeft.fork();
            countTaskRight.fork();

            //得到结果
            Integer joinLeft = countTaskLeft.join();
            Integer joinRight = countTaskRight.join();

            //合并
            num = joinLeft + joinRight;

        }

        return num;
    }


    public int insertCom(List<AttStBase> attStBaseList) throws ParseException {
        for (int i = 0; i < attStBaseList.size(); i++) {
            AttStBase attStBase = attStBaseList.get(i);
            String stcd = attStBase.getStCode();

            List<StPptnRAssist> stPptnRAssistList = new ArrayList<>();
            List<String> dateTimeList = DateUtils.getDateListStr("2021-07-05", "2021-07-11");
            for (String dateTime : dateTimeList) {
                try {
                    String tableName = StationType.RAINFALL.table() + dateTime.replaceAll("-", "").substring(0, 6);
                    String startTime = dateTime;

                    List<PptnRAssistReq> pptnRAssistReqs = rainAndWaterMapper.selectStcdDataHour(startTime + " 00:00:00",
                            startTime + " 23:59:59", stcd, tableName);
                    for (PptnRAssistReq pptnRAssistReq : pptnRAssistReqs) {

                        StPptnRAssist stPptnRAssist = new StPptnRAssist();
                        stPptnRAssist.setStcd(pptnRAssistReq.getStcd());

                        //1小时累计雨量
                        stPptnRAssist.setHour(pptnRAssistReq.getDrp());
                        //查询3小时累计雨量
                        Date time = DateUtil.parseDate("yyyy-MM-dd HH", pptnRAssistReq.getTm());
                        Date nextThreeHour = MydateUtil.getNextDay(time, null, null, null, +3, null, null);
                        PptnRAssistReq pptnRAssistReqthreeHour = rainAndWaterMapper.sumStcdDataThreeHour(pptnRAssistReq.getTm() + ":00:00",
                                DateUtil.format(nextThreeHour, "yyyy-MM-dd HH") + ":59:59", stcd, tableName);
                        if (pptnRAssistReqthreeHour != null && pptnRAssistReqthreeHour.getDrp() != null) {
                            stPptnRAssist.setThreehour(pptnRAssistReqthreeHour.getDrp());
                        }
                        if (pptnRAssistReq.getTm().split(" ")[1].equals("08")) {
                            stPptnRAssist.setType("2");

                            //查询1天累计流量
                            Date time1 = DateUtil.parseDate("yyyy-MM-dd", startTime);
                            Date nextDay = MydateUtil.getNextDay(time1, null, null, +1, null, null, null);
                            PptnRAssistReq pptnRAssistReqDay = rainAndWaterMapper.sumStcdDataThreeHour(startTime + " 08:00:00",
                                    DateUtil.format(nextDay, "yyyy-MM-dd") + " 08:00:00", stcd, tableName);
                            if (pptnRAssistReqDay != null && pptnRAssistReqDay.getDrp() != null) {
                                stPptnRAssist.setDay(pptnRAssistReqDay.getDrp());
                            }
                        } else {
                            stPptnRAssist.setType("1");
                        }

                        stPptnRAssist.setTime(DateUtil.parseDate("yyyy-MM-dd HH", pptnRAssistReq.getTm()));
                        stPptnRAssistList.add(stPptnRAssist);
                    }

                } catch (Exception e) {
                    log.error("雨量辅助表数据同步错误,错误信息:{}", e.getMessage());
                }
            }
            if (!stPptnRAssistList.isEmpty()) {
                stPptnRAssistMapper.insertForeach(stPptnRAssistList);
            }
        }
        return 1;
    }
}
