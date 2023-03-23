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
public class IntakeSuperviseManager extends MemStackCachedUnitManager {

    @Autowired
    WaterSavingClient waterSavingClient;

    public IntakeSuperviseManager(@Autowired ApplicationContext context) {
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
            case "intakeSuperviseStats": {
                Integer type = reqVo.getParam("type", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("intakeSuperviseStats", type))) == null) {
                    result = intakeSuperviseStats(type);
                }
                return result;
            }
            case "intakeSupervise4CWindow": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("intakeSupervise4CWindow"))) == null) {
                    result = intakeSupervise4CWindow();
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
     * 取水监管可证统计
     *
     * @param type 1. 根据行政区划统计，2.按取水许可统计（过期、超许可、有证无点、取水偏快）
     * @return
     */
    public CommonResult intakeSuperviseStats(int type) {
        return waterSavingClient.wptStats(type);
    }

    /**
     * 取水监管C位窗口
     *
     * @return
     */
    public CommonResult intakeSupervise4CWindow() {
        return waterSavingClient.wptStats4CWindow();
    }
}
