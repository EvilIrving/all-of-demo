package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.service.WaterDevPlanService;
import com.ygkj.big.screen.unit.DevPlanManager;
import com.ygkj.big.screen.unit.WaterResearchManager;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xq
 * @Description
 * @Date 2021/7/28
 */
@Service
public class WaterDevPlanServiceImpl implements WaterDevPlanService {

    @Autowired
    DevPlanManager devPlanManager;

    @Autowired
    WaterResearchManager waterResearchManager;

    @Override
    public CommonResult statsOfDevPlan(String ptId) {
        return (CommonResult) devPlanManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "statsOfDevPlan")
                .with(UnitDataReqVo::putParam, "ptId", ptId)
                .build());
    }

    @Override
    public CommonResult statsCwindowOfDevPlan(String ptId) {
        return (CommonResult) devPlanManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "statsCwindowOfDevPlan")
                .with(UnitDataReqVo::putParam, "ptId", ptId)
                .build());
    }

    /**
     * 水研究统计
     *
     * @return
     */
    @Override
    public CommonResult statsOfWaterResearch(Integer year) {
        return (CommonResult) waterResearchManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "statsOfWaterResearch")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

    /**
     * 水研究C位统计
     *
     * @return
     */
    @Override
    public CommonResult statsCwindowOfWaterResearch(Integer year) {
        return (CommonResult) waterResearchManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "statsCwindowOfWaterResearch")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }
}
