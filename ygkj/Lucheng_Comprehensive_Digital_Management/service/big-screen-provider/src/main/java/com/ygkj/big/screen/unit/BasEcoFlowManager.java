package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.HydrologyDigitizationClient;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author xq
 * @Description
 * @Date 2021/7/31
 */
@Component
public class BasEcoFlowManager extends AbstractUnitManager {

    @Autowired
    HydrologyDigitizationClient hydrologyDigitizationClient;


    public BasEcoFlowManager(@Autowired ApplicationContext context) {
        super(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void doSubscribe() {

    }

    @Override
    public void doPublish() {

    }

    @Override
    public Object getData(UnitDataReqVo reqVo) {
        switch (reqVo.getStrategy()) {
            case "stats4SectFlow": {
                return stats4SectFlow();
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }

    /**
     * 流域生态流量组件统计以及C位窗口
     *
     * @return
     */
    public CommonResult stats4SectFlow() {
        return hydrologyDigitizationClient.sectFlowStats();
    }
}
