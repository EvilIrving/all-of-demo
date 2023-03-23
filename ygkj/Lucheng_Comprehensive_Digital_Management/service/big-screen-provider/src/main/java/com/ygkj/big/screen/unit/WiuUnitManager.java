package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.WaterSavingClient;
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
 * @Date 2021/8/13
 */
@Component
public class WiuUnitManager extends MemStackCachedUnitManager {

    @Autowired
    WaterSavingClient waterSavingClient;

    public WiuUnitManager(@Autowired ApplicationContext context) {
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
            case "wiuStats": {
                Integer type = reqVo.getParam("type", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("wiuStats", type))) == null) {
                    result = wiuStats(type);
                }
                return result;
            }
            case "wiuUnit4CWindow": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("wiuUnit4CWindow"))) == null) {
                    result = wiuUnit4CWindow();
                }
                return result;
            }
            default:
                break;
        }
        return null;
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }


    /**
     * 取水户统计
     *
     * @param type 1. 根据行政区划统计，2.超许可统计：超许可、接近许可、正常
     * @return
     */
    public CommonResult wiuStats(int type) {
        return waterSavingClient.wiuStats(type);
    }

    /**
     * 取水户组件C位窗口
     *
     * @return
     */
    public CommonResult wiuUnit4CWindow() {
        return waterSavingClient.wiuUnit4CWindow();
    }
}
