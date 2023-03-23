package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.PlanDigitalClient;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xq
 * @Description 发展规划组件
 * @Date 2021/7/28
 */
@Component
public class DevPlanManager extends AbstractUnitManager {

    @Autowired
    PlanDigitalClient planDigitalClient;

    private ConcurrentHashMap<String, Object> cache;

    public DevPlanManager(@Autowired ApplicationContext context) {
        super(context);
        this.cache = new ConcurrentHashMap<>();
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
            case "statsOfDevPlan": {
                String ptId = reqVo.getParam("ptId", String.class);
                return statsOfDevPlan(ptId);
            }
            case "statsCwindowOfDevPlan": {
                String ptId = reqVo.getParam("ptId", String.class);
                return statsCwindowOfDevPlan(ptId);
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }

    /**
     * 大屏统计
     *
     * @param ptId
     * @return
     */
    public CommonResult statsOfDevPlan(String ptId) {
        return planDigitalClient.statsDevPlan4BigScreen(ptId);
    }

    /**
     * 大屏C位窗口统计
     *
     * @param ptId
     * @return
     */
    public CommonResult statsCwindowOfDevPlan(String ptId) {
        return planDigitalClient.statsDevPlan4BigScreenCWindow(ptId);
    }


}
