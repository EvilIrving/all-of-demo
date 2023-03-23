package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.OaClient;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
@Component
public class OaAssetsManager extends MemStackCachedUnitManager {

    @Autowired
    OaClient oaClient;

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
            case "stats4OaAssets": {
                Integer type = reqVo.getParam("type", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("stats4OaAssets", type))) == null) {
                    result = stats4OaAssets(type);
                }
                return result;
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }

    /**
     * 资产管理统计
     *
     * @param type 1. 统计资产总数，到期件数，资产总额；2，按部门总计资产件数；3，按部门总计资产总额；4，大屏C位统计;5. 按部门统计报废，6. 按部门统计领用
     * @return
     */
    public CommonResult stats4OaAssets(int type) {
        if (type < 5) {
            return oaClient.statsAssets4ScreenUnit(type);
        } else if (type == 5) {
            return oaClient.statsScrapNumByDept();
        } else if (type == 6) {
            return oaClient.statsCollectNumByDept();
        }
        return null;
    }
}
