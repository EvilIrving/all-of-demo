package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.PlanDigitalClient;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author xq
 * @Description
 * @Date 2021/7/28
 */
@Component
public class WaterResearchManager extends AbstractUnitManager {

    @Autowired
    PlanDigitalClient planDigitalClient;

    public WaterResearchManager(@Autowired ApplicationContext context) {
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
            case "statsOfWaterResearch": {
                Integer year = reqVo.getParam("year", Integer.class);
                return statsOfWaterResearch(year);
            }
            case "statsCwindowOfWaterResearch": {
                Integer year = reqVo.getParam("year", Integer.class);
                return statsCwindowOfWaterResearch(year);
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
     * @return
     */
    public CommonResult statsOfWaterResearch(Integer year) {
        return planDigitalClient.statsWaterResearch4BigScreen(year);
    }

    /**
     * 大屏C位窗口统计
     *
     * @return
     */
    public CommonResult statsCwindowOfWaterResearch(Integer year) {
        return planDigitalClient.statsWaterResearchByCondition(year == null ? null : year.toString());
    }
}
