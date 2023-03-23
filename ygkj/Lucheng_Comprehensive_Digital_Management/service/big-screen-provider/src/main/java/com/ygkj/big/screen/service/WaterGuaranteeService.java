package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.response.WrbStoreRsvResVo;
import com.ygkj.entity.SingletonMap;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.*;

import java.util.List;

/**
 * @author xq
 * @Description 水资源保障服务层
 * @Date 2021/7/29
 */
public interface WaterGuaranteeService {

    /**
     * 降水公报统计
     *
     * @param year
     * @return
     */
    JSONObject stats4WrbRainfall(Integer year);


    /**
     * 降水量公报C位统计
     *
     * @return
     */
    JSONObject statsCwindow4WrbRainfall(Integer year);


    /**
     * 降水量公报详表
     *
     * @param year
     * @param orderColumn
     * @param ascOrDesc
     * @return
     */
    List<WrbRainfall> table4WrbRainfall(Integer year, String orderColumn, Boolean ascOrDesc);


    /**
     * 水资源量公报统计
     *
     * @param year
     * @param type 1：水资源总量，2：人均水资源量
     * @return
     */
    List<JSONObject> stats4WrbWra(Integer year, Integer type);

    /**
     * 水资源量公报C位统计
     *
     * @return
     */
    JSONObject statsCwindow4WrbWra(Integer year);


    /**
     * 水资源量公报详表
     *
     * @param year
     * @param orderColumn
     * @param ascOrDesc
     * @return
     */
    List<WrbWaterResource> table4WrbWra(Integer year, String orderColumn, Boolean ascOrDesc);

    /**
     * 供水量公报统计
     *
     * @param year
     * @param type 1：供水量，2：供水工程
     * @return
     */
    List<JSONObject> stats4WrbWaterSupply(Integer year, Integer type);

    /**
     * 供水量公报C位统计
     *
     * @return
     */
    JSONObject statsCwindow4WrbWaterSupply(Integer year);

//    /**
//     * 供水量公报详表
//     *
//     * @param year
//     * @param orderColumn
//     * @param ascOrDesc
//     * @return
//     */
//    List<WrbWaterSupply> table4WrbWaterSupply(Integer year, String orderColumn, Boolean ascOrDesc);

    /**
     * 用水量公报统计
     *
     * @param year
     * @param type 1：行业类别，2：行政区划
     * @return
     */
    List<JSONObject> stats4WrbWaterConsume(Integer year, Integer type);

    /**
     * 用水量公报C位统计
     *
     * @return
     */
    JSONObject statsCwindow4WrbWaterConsume(Integer year);


    /**
     * 用水量公报详表
     *
     * @param year
     * @param orderColumn
     * @param ascOrDesc
     * @return
     */
    List<WrbWaterConsume> table4WrbWaterConsume(Integer year, String orderColumn, Boolean ascOrDesc);

    /**
     * 水库蓄水量公报统计
     *
     * @param year
     * @return
     */
    Object stats4WrbRsvrStore(Integer year);

    /**
     * 水库蓄水量公报C位统计
     *
     * @return
     */
    JSONObject statsCwindow4WrbRsvrStore(Integer year);

    /**
     * 水库蓄水量公报详表
     *
     * @param orderColumn
     * @param ascOrDesc
     * @return
     */
    List<WrbStoreRsvResVo> table4WrbRsvrStore(Integer year, String orderColumn, Boolean ascOrDesc);

    /**
     * 水库蓄水量公报蓄水曲线
     *
     * @param resCode
     * @return
     */
    List<WrbStoreRsvr> curve4WrbRsvrStore(String resCode);

    /**
     * 水资源承载能力大屏组件统计接口
     *
     * @param type 1：用水量评价；2：水质评价；3：经济评价；4：人口评价
     * @return
     */
    CommonResult stats4WraUnit(Integer type);

    /**
     * 水资源承载能力大屏组件C位统计接口
     *
     * @return
     */
    JSONObject statsCwindow4WraUnit();

    /**
     * 流域生态流量组件统计以及C位窗口
     *
     * @return
     */
    CommonResult stats4SectFlow();


    /**
     * 水源保证列表和柱状图
     *
     * @param watSrcName 水源名称
     * @return
     */
    CommonResult rsvrWatSrcTable(String watSrcName);

    /**
     * 水源保障c位
     *
     * @return
     */
    CommonResult rsvrWatSrc4CWindow();

    /**
     * 查询指定行政区划的近年的节水指标
     *
     * @param adcd      行政区划
     * @param startYear 起始年份
     * @param endYear   结束年份
     * @return
     */
    CommonResult watSavNormByYearInterval(String adcd, Integer startYear, Integer endYear);

    /**
     * 查询指定年份的节水指标
     *
     * @param year        年份
     * @param compareType 1. 年用水总量;2.万元GDP用水量；3.万元工业增加至用水量；4农业灌溉水有效利用系数；5.县域节水型社会创建达标率
     * @return
     */
    CommonResult watSavNormByYear(Integer year, Integer compareType);

    /**
     * 节水指标c位
     *
     * @return
     */
    CommonResult watSavNorm4CWindow();


    /**
     * 用水效率组件卡片
     *
     * @param areaLevel 国际，国内，省内，市内
     * @param year      年份
     * @return
     */
    CommonResult wpgEffect(String areaLevel, Integer year);

    /**
     * 用水效率组件C位
     *
     * @return
     */
    CommonResult wpgEffect4CWindow();

    /**
     * 三条红线用水总量
     *
     * @param year
     * @return
     */
    CommonResult redLineWatCsm(Integer year);

    /**
     * 三条红线用水效率
     *
     * @param year
     * @return
     */
    CommonResult redLineWpgEffect(Integer year);

    /**
     * 三条红线C位窗口
     *
     * @return
     */
    CommonResult redLine4CWindow();


    /**
     * 取水监管可证统计
     *
     * @param type 1. 根据行政区划统计，2.按取水许可统计（过期、超许可、有证无点、取水偏快）
     * @return
     */
    CommonResult intakeSuperviseStats(int type);

    /**
     * 取水监管C位窗口
     *
     * @return
     */
    CommonResult intakeSupervise4CWindow();

    /**
     * 取水户统计
     *
     * @param type 1. 根据行政区划统计，2.超许可统计：超许可、接近许可、正常
     * @return
     */
    CommonResult wiuStats(int type);

    /**
     * 取水户组件C位窗口
     *
     * @return
     */
    CommonResult wiuUnit4CWindow();
}
