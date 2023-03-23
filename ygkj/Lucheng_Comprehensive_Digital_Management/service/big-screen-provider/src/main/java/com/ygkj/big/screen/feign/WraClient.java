package com.ygkj.big.screen.feign;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Year;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author xq
 * @Description 水资源承载能力 feign client
 * @Date 2021/7/29
 */
@FeignClient(contextId = "water-resource-ability", name = "water-resource-ability")
public interface WraClient {

    /**
     * 用水量评价
     *
     * @param year
     * @return
     */
    @GetMapping("waterAbilityEva/waterConsumeEvaluation")
    CommonResult waterConsumeEvaluation(@RequestParam("year") Integer year);

    /**
     * 水质评价
     *
     * @param year
     * @return
     */
    @GetMapping("waterAbilityEva/waterQualityEvaluation")
    CommonResult waterQualityEvaluation(@RequestParam("year") Integer year);

    // 人口评价
    @GetMapping("waterAbilityEva/popAppraisal")
    CommonResult popAppraisal(@RequestParam("year") Integer year);

    // 经济评价
    @GetMapping("waterAbilityEva/ecoAppraisal")
    CommonResult ecoAppraisal(@RequestParam("year") Integer year);

    /**
     * @param factor            平滑指数
     * @param watPerGdpDecrRate 用水效率控制指标
     * @return
     */
    @GetMapping("waterAbilityEva/watGdpPreCurveByDouExpoModel")
    CommonResult<LinkedHashMap> watGdpPreCurveByDouExpoModel(@RequestParam("factor") Double factor, @RequestParam("watPerGdpDecrRate") Double watPerGdpDecrRate);

    /**
     * 根据趋势预测计算用水gdp预测曲线
     *
     * @param ecoIncrRate       经济增长率
     * @param watPerGdpDecrRate 用水效率控制指标
     * @return
     */
    @GetMapping("waterAbilityEva/watGdpPreCurveByTrendPreModel")
    CommonResult<LinkedHashMap> watGdpPreCurveByTrendPreModel(@RequestParam("ecoIncrRate") Double ecoIncrRate, @RequestParam("watPerGdpDecrRate") Double watPerGdpDecrRate);

    @GetMapping("extraInfo/watSavNormByYearInterval")
    CommonResult watSavNormByYearInterval(@RequestParam("adcd") String adcd, @RequestParam("startYear") Integer startYear, @RequestParam("endYear") Integer endYear);

    @GetMapping("extraInfo/watSavNormByYear")
    CommonResult watSavNormByYear(@RequestParam("year") Integer year, @RequestParam("compareType") Integer compareType);

    @GetMapping("extraInfo/watSavNorm4CWindow")
    CommonResult watSavNorm4CWindow();

    /**
     * 用水效率组件卡片
     *
     * @param areaLevel 国际，国内，省内，市内
     * @param year      年份
     * @return
     */
    @GetMapping("extraInfo/wpgEffect")
    CommonResult wpgEffect(@RequestParam("areaLevel") String areaLevel, @RequestParam("year") Integer year);

    /**
     * 用水效率组件C位
     *
     * @return
     */
    @GetMapping("extraInfo/wpgEffect4CWindow")
    CommonResult wpgEffect4CWindow();

    /**
     * 三条红线用水总量
     *
     * @param year
     * @return
     */
    @GetMapping("extraInfo/redLineWatCsm")
    CommonResult redLineWatCsm(@RequestParam("year") Integer year);

    /**
     * 三条红线用水效率
     *
     * @param year
     * @return
     */
    @GetMapping("extraInfo/redLineWpgEffect")
    CommonResult redLineWpgEffect(@RequestParam("year") Integer year);

    /**
     * 三条红线C位窗口
     *
     * @return
     */
    @GetMapping("extraInfo/redLine4CWindow")
    CommonResult redLine4CWindow();

    /**
     * @param year
     * @param type
     * @return
     */
    @GetMapping("wrbWaterResource/unitCard4WrbWatRes")
    CommonResult unitCard4WrbWatRes(@RequestParam(value = "year", required = false) Integer year, @RequestParam(value = "type", required = false) Integer type);

    /**
     * @param year 年份
     * @return
     */
    @GetMapping("wrbWaterResource/unitCWindows4WrbWatRes")
    CommonResult unitCWindows4WrbWatRes(@RequestParam(value = "year", required = false) Integer year);

    /**
     * @param year
     * @param type
     * @return
     */
    @GetMapping("wrbWaterConsume/unitCard4WrbWatCon")
    CommonResult unitCard4WrbWatCon(@RequestParam(value = "year", required = false) Integer year, @RequestParam(value = "type", required = false) Integer type);

    /**
     * @param year
     * @return
     */
    @GetMapping("wrbWaterConsume/unitCWindows4WrbWatCon")
    CommonResult unitCWindows4WrbWatCon(@RequestParam(value = "year", required = false) Integer year);


    @GetMapping("wrbStoreRsvr/unitCard4WrbStoreRsvr")
    CommonResult unitCard4WrbStoreRsvr(@RequestParam(value = "year", required = false) Integer year);

    @GetMapping("wrbStoreRsvr/unitCWindows4WrbStoreRsvr")
    CommonResult unitCWindows4WrbStoreRsvr(@RequestParam(value = "year", required = false) Integer year);
}
