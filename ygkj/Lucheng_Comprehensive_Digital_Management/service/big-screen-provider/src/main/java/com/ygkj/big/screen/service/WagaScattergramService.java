package com.ygkj.big.screen.service;

import com.ygkj.water.model.CommonResult;

/**
 * @author wgf
 * @date 2021/12/24 10:24
 * @Description:
 */
public interface WagaScattergramService {

    CommonResult latestData();

    CommonResult latestGatage();

    CommonResult newGateOvs(String stcd);
}
