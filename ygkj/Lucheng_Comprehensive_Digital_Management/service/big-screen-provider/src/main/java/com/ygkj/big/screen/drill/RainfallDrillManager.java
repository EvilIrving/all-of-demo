package com.ygkj.big.screen.drill;

import com.ygkj.big.screen.mapper.BsDrillCaseMapper;
import com.ygkj.big.screen.drill.notify.event.RainfallDrillCaseEvent;
import com.ygkj.big.screen.drill.notify.event.RainfallDrillEvent;
import com.ygkj.big.screen.mapper.BsDrillStcaseMapper;
import com.ygkj.big.screen.mapper.*;
import com.ygkj.big.screen.model.BsDrillCase;
import com.ygkj.big.screen.model.BsDrillStcase;
import com.ygkj.big.screen.service.DrillService;
import com.ygkj.big.screen.vo.request.DoDrillParamReqVo;
import com.ygkj.big.screen.vo.request.DrillParamReqVo;
import com.ygkj.big.screen.vo.response.UnitDataResVo;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.common.notify.events.Event;
import com.ygkj.common.notify.subscribers.Subscriber;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author xq
 * @Description
 * @Date 2021/7/8
 */
@Component
@Slf4j
public class RainfallDrillManager extends AbstractDrillManagerStrategy {

    @Resource
    BsDrillCaseMapper drillCaseMapper;

    @Resource
    BsDrillStcaseMapper drillStcaseMapper;

    @Autowired
    GenerateDistributionIDService idService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DrillService drillService;

    @Override
    public void doDrill(DoDrillParamReqVo doDrillParam) {
        log.info("开始降雨" + doDrillParam.toString());
        String caseId = doDrillParam.getCaseId();
        Map<Date, String> effectPreGroupIdMap = doDrillParam.getEffectGroupIdMap();
        Map<Date, String> effectNextGroupIdMap = new HashMap<>();
        BsDrillCase drillCase = drillCaseMapper.load(caseId);
        Date startTime = drillCase.getStartTime();
        Date endTime = new Date(startTime.getTime() + drillCase.getHourNum() * 3600000);

        List<BsDrillStcase> stcaseList = drillStcaseMapper.selectByCondition(Builder.of(BsDrillStcase::new).with(BsDrillStcase::setCaseId, drillCase.getId()).build());
        DrillStrategy rainfallStatisticDrillStrategy = new RainfallDrillStrategy(applicationContext);
        DrillParamReqVo drillParam = new DrillParamReqVo();
        drillParam.setDrillCase(drillCase);
        drillParam.setStcaseList(stcaseList);
        drillParam.setUnitName("实时降雨");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            int timeIndex = 0;
            while (!calendar.getTime().after(endTime)) {
                String groupId = idService.nextID();
                drillParam.setDataGroupId(groupId);
                drillParam.setTimeIndex(timeIndex);
                Map<String, List<UnitDataResVo>> resultMap = rainfallStatisticDrillStrategy.buildData(drillParam, calendar.getTime());

                try {
                    String infGroupId = drillService.addOrUpdateDrillData(resultMap, effectPreGroupIdMap, calendar.getTime());
                    effectNextGroupIdMap.put(calendar.getTime(), infGroupId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                timeIndex++;
                calendar.add(Calendar.HOUR_OF_DAY, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotifyCenter.publishEvent(new RainfallDrillEvent(Builder.of(DoDrillParamReqVo::new)
                .with(DoDrillParamReqVo::setCaseId, caseId)
                .with(DoDrillParamReqVo::setEffect, true)
                .with(DoDrillParamReqVo::setEffectGroupIdMap, effectNextGroupIdMap).build()));
    }

    @Override
    public void doSubscribe() {

        // 订阅方案
        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                DoDrillParamReqVo param = new DoDrillParamReqVo();
                String caseId = (String) event.data();
                param.setCaseId(caseId);
                param.setEffect(false);
                System.out.println("降雨方案：" + caseId);
                doDrill(param);
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return RainfallDrillCaseEvent.class;
            }

            @Override
            public Executor executor() {
                return getExecutorService();
            }
        });
    }

    @Override
    public void shutdown() {
//        NotifyCenter.deregisterSubscriber(RainfallDrillCaseEvent.class);
    }

}
