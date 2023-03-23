package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.WraClient;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author xq
 * @Description
 * @Date 2021/8/12
 */
@Component
public class WatUseEfficiencyManager extends MemStackCachedUnitManager {

    @Autowired
    WraClient wraClient;

    public WatUseEfficiencyManager(@Autowired ApplicationContext context) {
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
            case "wpgEffect": {
                String areaLevel = reqVo.getParam("areaLevel", String.class);
                Integer year = reqVo.getParam("year", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("wpgEffect", areaLevel, year))) == null) {
                    result = wpgEffect(areaLevel, year);
                }
                return result;
            }
            case "wpgEffect4CWindow": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("wpgEffect4CWindow"))) == null) {
                    result = wpgEffect4CWindow();
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
     * 用水效率组件卡片
     *
     * @param areaLevel 国际，国内，省内，市内
     * @param year      年份
     * @return
     */
    CommonResult wpgEffect(String areaLevel, Integer year) {
        return wraClient.wpgEffect(areaLevel, year);
    }

    /**
     * 用水效率组件C位
     *
     * @return
     */
    CommonResult wpgEffect4CWindow() {
        return wraClient.wpgEffect4CWindow();
    }
}
