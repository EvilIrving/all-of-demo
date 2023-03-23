package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.WraClient;
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
 * @Date 2021/8/12
 */
@Component
public class RedLine3Manager extends MemStackCachedUnitManager {

    @Autowired
    WraClient wraClient;

    public RedLine3Manager(@Autowired ApplicationContext context) {
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
            case "redLineWatCsm": {
                Integer year = reqVo.getParam("year", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("redLineWatCsm", year))) == null) {
                    result = redLineWatCsm(year);
                }
                return result;
            }
            case "redLineWpgEffect": {
                Integer year = reqVo.getParam("year", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("redLineWpgEffect", year))) == null) {
                    result = redLineWpgEffect(year);
                }
                return result;
            }
            case "redLine4CWindow": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("redLine4CWindow"))) == null) {
                    result = redLine4CWindow();
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
     * 三条红线用水总量
     *
     * @param year
     * @return
     */
    public CommonResult redLineWatCsm(Integer year) {
        return wraClient.redLineWatCsm(year);
    }

    /**
     * 三条红线用水效率
     *
     * @param year
     * @return
     */
    public CommonResult redLineWpgEffect(Integer year) {
        return wraClient.redLineWpgEffect(year);
    }

    /**
     * 三条红线C位窗口
     *
     * @return
     */
    public CommonResult redLine4CWindow() {
        return wraClient.redLine4CWindow();
    }
}
