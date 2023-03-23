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
public class WatSavNormManager extends MemStackCachedUnitManager {

    @Autowired
    WraClient wraClient;

    public WatSavNormManager(@Autowired ApplicationContext context) {
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
            case "watSavNormByYearInterval": {
                String adcd = reqVo.getParam("adcd", String.class);
                Integer startYear = reqVo.getParam("startYear", Integer.class);
                Integer endYear = reqVo.getParam("endYear", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("watSavNormByYearInterval", startYear, endYear))) == null) {
                    result = watSavNormByYearInterval(adcd, startYear, endYear);
                }
                return result;
            }
            case "watSavNormByYear": {
                Integer year = reqVo.getParam("year", Integer.class);
                Integer compareType = reqVo.getParam("compareType", Integer.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("watSavNormByYear", year, compareType))) == null) {
                    result = watSavNormByYear(year, compareType);
                }
                return result;
            }
            case "watSavNorm4CWindow": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("watSavNorm4CWindow"))) == null) {
                    result = watSavNorm4CWindow();
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
     * 查询指定行政区划的近年的节水指标
     *
     * @param adcd      行政区划
     * @param startYear 起始年份
     * @param endYear   结束年份
     * @return
     */
    public CommonResult watSavNormByYearInterval(String adcd, Integer startYear, Integer endYear) {
        return wraClient.watSavNormByYearInterval(adcd, startYear, endYear);
    }

    /**
     * 查询指定年份的节水指标
     *
     * @param year        年份
     * @param compareType 1. 年用水总量;2.万元GDP用水量；3.万元工业增加至用水量；4农业灌溉水有效利用系数；5.县域节水型社会创建达标率
     * @return
     */
    public CommonResult watSavNormByYear(Integer year, Integer compareType) {
        return wraClient.watSavNormByYear(year, compareType);
    }

    /**
     * 节水指标c位
     *
     * @return
     */
    public CommonResult watSavNorm4CWindow() {
        return wraClient.watSavNorm4CWindow();
    }
}
