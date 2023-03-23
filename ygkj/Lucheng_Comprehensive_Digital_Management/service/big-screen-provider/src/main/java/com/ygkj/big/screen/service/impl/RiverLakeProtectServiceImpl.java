package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.service.RiverLakeProtectService;
import com.ygkj.big.screen.unit.RiverPatrolManager;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
@Service
public class RiverLakeProtectServiceImpl implements RiverLakeProtectService {
    @Autowired
    RiverPatrolManager riverPatrolManager;

    /**
     * 河长巡河问题统计
     *
     * @param type 1：根据行政区划统计；2. 根据处置状态统计；3. 根据问题程度统计；4.统计过去12个月每月问题数量曲线;5 大屏C位统计
     * @return
     */
    @Override
    public CommonResult chiefPatrolProblemStats(int type) {
        return (CommonResult) riverPatrolManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "chiefPatrolProblemStats")
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }
}
