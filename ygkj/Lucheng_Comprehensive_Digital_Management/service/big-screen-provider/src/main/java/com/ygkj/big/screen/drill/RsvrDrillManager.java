package com.ygkj.big.screen.drill;

import com.ygkj.big.screen.mapper.BsDrillCaseMapper;
import com.ygkj.big.screen.drill.notify.event.RainfallDrillEvent;
import com.ygkj.big.screen.drill.notify.event.RsvrDrillCaseEvent;
import com.ygkj.big.screen.mapper.*;
import com.ygkj.big.screen.model.BsDrillCase;
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
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

/**
 * @author xq
 * @Description
 * @Date 2021/7/10
 */
@Component
@Slf4j
public class RsvrDrillManager extends AbstractDrillManagerStrategy {

    @Resource
    BsDrillCaseMapper drillCaseMapper;

    @Autowired
    GenerateDistributionIDService idService;

    @Autowired
    DrillService drillService;

    @Autowired
    ApplicationContext applicationContext;

    CopyOnWriteArraySet<String> caseSet = new CopyOnWriteArraySet<>();


    @Override
    public void doDrill(DoDrillParamReqVo doDrillParam) {
        log.info("水库" + doDrillParam.toString());
        String caseId = doDrillParam.getCaseId();
        caseSet.add(caseId);
        Map<Date, String> effectPreGroupIdMap = doDrillParam.getEffectGroupIdMap();
        Map<Date, String> effectNextGroupIdMap = new HashMap<>();
        BsDrillCase drillCase = drillCaseMapper.load(caseId);
        Date startTime = drillCase.getStartTime();
        Date endTime = new Date(startTime.getTime() + drillCase.getHourNum() * 3600000);

        DrillStrategy drillStrategy = new RsvrDrillStrategy(applicationContext);
        DrillParamReqVo drillParam = new DrillParamReqVo();
        drillParam.setDrillCase(drillCase);
        drillParam.setUnitName("水库");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            int timeIndex = 0;
            while (!calendar.getTime().after(endTime)) {
                String groupId = idService.nextID();
                drillParam.setDataGroupId(groupId);
                drillParam.setTimeIndex(timeIndex);
                Map<String, List<UnitDataResVo>> resultMap = drillStrategy.buildData(drillParam, calendar.getTime());
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
        caseSet.remove(caseId);
//        NotifyCenter.publishEvent(new RsvrDrillEvent(Builder.of(DoDrillParamReqVo::new)
//                .with(DoDrillParamReqVo::setCaseId, caseId)
//                .with(DoDrillParamReqVo::setEffect,true)
//                .with(DoDrillParamReqVo::setEffectGroupIdMap, effectNextGroupIdMap).build()));
    }

    @Override
    public void doSubscribe() {
        // 订阅降雨
        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                DoDrillParamReqVo param = (DoDrillParamReqVo) event.data();
                param.setEffect(true);
                // 避免重复执行
                if (!caseSet.contains(param.getCaseId())) {
                    doDrill(param);
                }
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return RainfallDrillEvent.class;
            }
        });
        // 订阅方案
        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                DoDrillParamReqVo param = new DoDrillParamReqVo();
                String caseId = (String) event.data();
                param.setCaseId(caseId);
                param.setEffect(false);
                System.out.println("水库：" + caseId);
                // 避免重复执行
                if (!caseSet.contains(param.getCaseId())) {
                    doDrill(param);
                }
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return RsvrDrillCaseEvent.class;
            }

            @Override
            public Executor executor() {
                return getExecutorService();
            }
        });
    }

    @Override
    public void shutdown() {

    }
}
