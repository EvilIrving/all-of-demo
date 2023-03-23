package com.ygkj.big.screen.drill;

import com.ygkj.big.screen.mapper.BsUnitDataIndexMapper;
import com.ygkj.big.screen.mapper.BsUnitDataMapper;
import com.ygkj.big.screen.utils.GisRainfallSurfaceUtils;
import com.ygkj.big.screen.drill.notify.event.RainfallDrillEvent;
import com.ygkj.big.screen.drill.notify.event.RainfallSurfaceImgCaseEvent;
import com.ygkj.big.screen.mapper.*;
import com.ygkj.big.screen.model.BsUnitData;
import com.ygkj.big.screen.model.BsUnitDataIndex;
import com.ygkj.big.screen.service.DrillService;
import com.ygkj.big.screen.vo.request.DoDrillParamReqVo;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.common.notify.events.Event;
import com.ygkj.common.notify.subscribers.Subscriber;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author xq
 * @Description
 * @Date 2021/7/11
 */
@Component
@Slf4j
public class RainfallSurfaceImgDrillManager extends AbstractDrillManagerStrategy {

    @Resource
    BsUnitDataMapper unitDataMapper;

    @Resource
    BsUnitDataIndexMapper unitDataIndexMapper;

    @Autowired
    GenerateDistributionIDService idService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DrillService drillService;

    @Autowired
    GisRainfallSurfaceUtils gisRainfallSurfaceUtils;

    @Override
    public void doDrill(DoDrillParamReqVo doDrillParam) {
        log.info("降雨等值面更新被唤醒：{}", doDrillParam.getCaseId());
//        Map<Date,String> effectGroupIdMap=doDrillParam.getEffectGroupIdMap();
        List<BsUnitDataIndex> dataIndexList = unitDataIndexMapper.selectByCondition(Builder.of(BsUnitDataIndex::new)
                .with(BsUnitDataIndex::setDataApi, "/floodDrought/rainfall")
                .with(BsUnitDataIndex::setDataType, 0)
                .with(BsUnitDataIndex::setUnitName, "实时降雨")
                .with(BsUnitDataIndex::setDataParam, null)
                .with(BsUnitDataIndex::setCaseId, doDrillParam.getCaseId())
                .build());
        dataIndexList = dataIndexList == null ? Collections.emptyList() : dataIndexList;
        dataIndexList.sort(Comparator.comparing(BsUnitDataIndex::getTimeStamp));
        int size = dataIndexList.size();
        int i = 1;
        for (BsUnitDataIndex dataIndex : dataIndexList) {
            try {
                String dataValue = unitDataMapper.loadDataValue(dataIndex.getDataId());
                if (StringUtils.isNotBlank(dataValue)) {
                    String after = gisRainfallSurfaceUtils.doUpdateGisUrl(dataValue, false);
                    if (StringUtils.isNotBlank(after) && !after.equals(dataValue)) {
                        unitDataMapper.update(Builder.of(BsUnitData::new)
                                .with(BsUnitData::setDataId, dataIndex.getDataId())
                                .with(BsUnitData::setDataValue, after)
                                .build());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("方案：{}；更新：{}/{}", doDrillParam.getCaseId(), i, size);
            i++;
        }
    }

    @Override
    public void doSubscribe() {
        // 订阅降雨
        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                DoDrillParamReqVo param = (DoDrillParamReqVo) event.data();
                param.setEffect(true);
                doDrill(param);
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return RainfallDrillEvent.class;
            }

            @Override
            public Executor executor() {
                return getExecutorService();
            }
        });
        // 订阅方案
        NotifyCenter.registerSubscriber(new Subscriber() {
            @Override
            public void onEvent(Event event) {
                DoDrillParamReqVo param = new DoDrillParamReqVo();
                String caseId = (String) event.data();
                param.setCaseId(caseId);
                param.setEffect(true);
                System.out.println("实时降雨面雨量：" + caseId);
                doDrill(param);
            }

            @Override
            public Class<? extends Event> subscribeType() {
                return RainfallSurfaceImgCaseEvent.class;
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
