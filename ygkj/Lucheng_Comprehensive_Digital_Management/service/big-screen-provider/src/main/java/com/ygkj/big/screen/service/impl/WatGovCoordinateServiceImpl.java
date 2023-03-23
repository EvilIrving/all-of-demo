package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.service.WatGovCoordinateService;
import com.ygkj.big.screen.unit.OaAssetsManager;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.water.model.CommonResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xq
 * @Description
 * @Date 2021/8/18
 */
@Service
public class WatGovCoordinateServiceImpl implements WatGovCoordinateService {

    @Resource
    OaAssetsManager oaAssetsManager;

    /**
     * 资产管理统计
     *
     * @param type 1. 统计资产总数，到期件数，资产总额；2，按部门总计资产件数；3，按部门总计资产总额；4，大屏C位统计;5. 按部门统计报废，6. 按部门统计领用
     * @return
     */
    @Override
    public CommonResult stats4OaAssets(int type) {
        return (CommonResult) oaAssetsManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "stats4OaAssets")
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }
}
