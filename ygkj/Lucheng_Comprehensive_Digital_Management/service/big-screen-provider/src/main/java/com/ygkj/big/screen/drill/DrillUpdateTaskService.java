package com.ygkj.big.screen.drill;

import com.ygkj.big.screen.mapper.BsDrillCaseMapper;
import com.ygkj.big.screen.mapper.BsUnitDataMapper;
import com.ygkj.big.screen.utils.GisRainfallSurfaceUtils;
import com.ygkj.big.screen.drill.notify.event.RsvrDrillCaseEvent;
import com.ygkj.big.screen.mapper.BsUnitDataIndexMapper;
import com.ygkj.big.screen.model.BsDrillCase;
import com.ygkj.big.screen.model.BsUnitData;
import com.ygkj.big.screen.model.BsUnitDataIndex;
import com.ygkj.big.screen.vo.request.DoDrillParamReqVo;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author xq
 * @Description
 * @Date 2021/7/8
 */
@Component
@Slf4j
public class DrillUpdateTaskService implements InitializingBean {

    ExecutorService executorService;

    @Autowired
    BsDrillCaseMapper drillCaseMapper;

    @Autowired
    RsvrDrillManager rsvrDrillManager;

    @Autowired
    BsUnitDataIndexMapper unitDataIndexMapper;

    @Autowired
    BsUnitDataMapper unitDataMapper;

    @Autowired
    GisRainfallSurfaceUtils gisRainfallSurfaceUtils;

    // 更新受实时实时降雨模块影响的演练数据
    @Scheduled(cron = "0 0 3 * * ?")
    public void updateRsvrDrillInfByRain() {
        log.info("开始更新受暴雨影响的演练水库");
        List<BsDrillCase> list = drillCaseMapper.selectByCondition(Builder.of(BsDrillCase::new).with(BsDrillCase::setUnitName, "实时降雨").build());
        if (CollectionUtils.isNotBlank(list)) {
            for (BsDrillCase drillCase : list) {
                //通知水库更新数据
                NotifyCenter.publishEvent(new RsvrDrillCaseEvent(drillCase.getId()));
            }
        }
        log.info("更新受暴雨影响的演练水库完毕");
    }

    //    @Scheduled(cron = "0 30 0/2 * * ? ")
    public void doUpdateGisRainfallSurface() {
        executorService.execute(() -> {
            List<BsUnitDataIndex> dataIndexList = unitDataIndexMapper.selectByCondition(Builder.of(BsUnitDataIndex::new)
                    .with(BsUnitDataIndex::setDataApi, "/floodDrought/rainfall").build());
            dataIndexList = dataIndexList == null ? Collections.emptyList() : dataIndexList;
            int size = dataIndexList.size();
            int i = 0;
            for (BsUnitDataIndex dataIndex : dataIndexList) {
                String dataValue = unitDataMapper.loadDataValue(dataIndex.getDataId());
                if (StringUtils.isNotBlank(dataValue)) {
                    dataValue = gisRainfallSurfaceUtils.doUpdateGisUrl(dataValue, false);
                    if (StringUtils.isNotBlank(dataValue)) {
                        unitDataMapper.update(Builder.of(BsUnitData::new)
                                .with(BsUnitData::setDataId, dataIndex.getDataId())
                                .with(BsUnitData::setDataValue, dataValue)
                                .build());
                    }
                }
                log.info("更新：{}/{}", i, size);
                i++;
            }
        });
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 8,
                60,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(10));
    }
}
