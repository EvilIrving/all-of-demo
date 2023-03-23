package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.RiverClient;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
@Component
public class RiverPatrolManager extends MemStackCachedUnitManager {

    @Autowired
    RiverClient riverClient;

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
            case "chiefPatrolProblemStats": {
                Integer type = reqVo.getParam("type", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("chiefPatrolProblemStats", type))) == null) {
                    result = chiefPatrolProblemStats(type);
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
     * 河长巡河问题统计
     *
     * @param type 1：根据行政区划统计；2. 根据处置状态统计；3. 根据问题程度统计；4.统计过去12个月每月问题数量曲线;5 大屏C位统计
     * @return
     */
    public CommonResult chiefPatrolProblemStats(int type) {
        return riverClient.problemStats(type);
    }
}
