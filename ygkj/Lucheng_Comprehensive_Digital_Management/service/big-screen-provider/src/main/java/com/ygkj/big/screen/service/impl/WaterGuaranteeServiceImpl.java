package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.service.WaterGuaranteeService;
import com.ygkj.big.screen.unit.*;
import com.ygkj.big.screen.mapper.WaterResourceBulletinMapper;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.unit.*;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.big.screen.vo.response.WrbStoreRsvResVo;
import com.ygkj.utils.BeanComparator;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/29
 */
@Service
public class WaterGuaranteeServiceImpl implements WaterGuaranteeService {

    @Autowired
    WrbRainfallManager wrbRainfallManager;


    @Autowired
    WrbRsvrStoreManager rsvrStoreManager;


    @Autowired
    WrbWaterConsumeManager waterConsumeManager;


    @Autowired
    WrbWaterSupplyManager waterSupplyManager;

    @Autowired
    WrbWraManager wrbWraManager;

    @Autowired
    WResourceAbilityManager wResourceAbilityManager;

    @Resource
    WaterResourceBulletinMapper wrbMapper;

    @Resource
    BasEcoFlowManager basEcoFlowManager;

    @Resource
    WatSrcGuaranteeManager watSrcGuaranteeManager;

    @Resource
    WatSavNormManager watSavNormManager;

    @Resource
    WatUseEfficiencyManager watUseEfficiencyManager;

    @Resource
    RedLine3Manager redLine3Manager;

    @Resource
    IntakeSuperviseManager intakeSuperviseManager;

    @Resource
    WiuUnitManager wiuUnitManager;

    /**
     * 降水公报统计
     *
     * @param year
     * @return
     */
    @Override
    public JSONObject stats4WrbRainfall(Integer year) {
        return (JSONObject) wrbRainfallManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "stats4WrbRainfall")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

    /**
     * 降水量公报C位统计
     *
     * @return
     */
    @Override
    public JSONObject statsCwindow4WrbRainfall(Integer year) {
        return (JSONObject) wrbRainfallManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "statsCwindow4WrbRainfall")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

    /**
     * 降水量公报详表
     *
     * @param year
     * @param orderColumn
     * @param ascOrDesc
     * @return
     */
    @Override
    public List<WrbRainfall> table4WrbRainfall(Integer year, String orderColumn, Boolean ascOrDesc) {
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_rainfall", null);
        }
        List<WrbRainfall> records = wrbMapper.selectWrbRainfall(year, null);
        records = records == null ? Collections.emptyList() : records;
        if (StringUtils.isNotBlank(orderColumn)) {
            Comparator<WrbRainfall> comparator = new BeanComparator<>(orderColumn);
            if (ascOrDesc != null && ascOrDesc) {
                comparator = comparator.reversed();
            }
            records = records.stream().sorted(comparator).collect(Collectors.toList());
        }
        return records;
    }

    /**
     * 水资源量公报统计
     *
     * @param year
     * @param type 1：水资源总量，2：人均水资源量
     * @return
     */
    @Override
    public List<JSONObject> stats4WrbWra(Integer year, Integer type) {
        return (List<JSONObject>) wrbWraManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "stats4WrbWra")
                .with(UnitDataReqVo::putParam, "year", year)
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }

    /**
     * 水资源量公报C位统计
     *
     * @return
     */
    @Override
    public JSONObject statsCwindow4WrbWra(Integer year) {
        return (JSONObject) wrbWraManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "statsCwindow4WrbWra")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

    /**
     * 水资源量公报详表
     *
     * @param year
     * @param orderColumn
     * @param ascOrDesc
     * @return
     */
    @Override
    public List<WrbWaterResource> table4WrbWra(Integer year, String orderColumn, Boolean ascOrDesc) {
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_water_resource", null);
        }
        List<WrbWaterResource> records = wrbMapper.selectWrbWaterResource(year, null);
        records = records == null ? Collections.emptyList() : records;
        if (StringUtils.isNotBlank(orderColumn)) {
            Comparator<WrbWaterResource> comparator = new BeanComparator<>(orderColumn);
            if (ascOrDesc != null && ascOrDesc) {
                comparator = comparator.reversed();
            }
            records = records.stream().sorted(comparator).collect(Collectors.toList());
        }
        return records;
    }

    /**
     * 供水量公报统计
     *
     * @param year
     * @param type 1：供水量，2：供水工程
     * @return
     */
    @Override
    public List<JSONObject> stats4WrbWaterSupply(Integer year, Integer type) {
        return (List<JSONObject>) waterSupplyManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "stats4WrbWaterSupply")
                .with(UnitDataReqVo::putParam, "year", year)
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }

    /**
     * 供水量公报C位统计
     *
     * @return
     */
    @Override
    public JSONObject statsCwindow4WrbWaterSupply(Integer year) {
        return (JSONObject) waterSupplyManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "statsCwindow4WrbWaterSupply")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

//    /**
//     * 供水量公报详表
//     *
//     * @param year
//     * @param orderColumn
//     * @param ascOrDesc
//     * @return
//     */
//    @Override
//    public List<WrbWaterSupply> table4WrbWaterSupply(Integer year, String orderColumn, Boolean ascOrDesc) {
////        if(year==null){
////            year=this.wrbMapper.selectMaxYearByCondition("wrb_water_supply",null);
////        }
//        List<WrbWaterSupply> records = wrbMapper.selectWrbWaterSupply(year);
//        records = records == null ? Collections.emptyList() : records;
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(orderColumn)) {
//            Comparator<WrbWaterSupply> comparator = new BeanComparator<>(orderColumn);
//            if (ascOrDesc != null && ascOrDesc) {
//                comparator = comparator.reversed();
//            }
//            records = records.stream().sorted(comparator).collect(Collectors.toList());
//        }
//        return records;
//    }

    /**
     * 用水量公报统计
     *
     * @param year
     * @param type 1：行业类别，2：行政区划
     * @return
     */
    @Override
    public List<JSONObject> stats4WrbWaterConsume(Integer year, Integer type) {
        return (List<JSONObject>) waterConsumeManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "stats4WrbWaterConsume")
                .with(UnitDataReqVo::putParam, "year", year)
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }

    /**
     * 用水量公报C位统计
     *
     * @return
     */
    @Override
    public JSONObject statsCwindow4WrbWaterConsume(Integer year) {
        return (JSONObject) waterConsumeManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "statsCwindow4WrbWaterConsume")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

    /**
     * 用水量公报详表
     *
     * @param year
     * @param orderColumn
     * @param ascOrDesc
     * @return
     */
    @Override
    public List<WrbWaterConsume> table4WrbWaterConsume(Integer year, String orderColumn, Boolean ascOrDesc) {
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_water_consume", null);
        }
        List<WrbWaterConsume> records = wrbMapper.selectWrbWaterConsume(year, null);
        records = records == null ? Collections.emptyList() : records;
        if (StringUtils.isNotBlank(orderColumn)) {
            Comparator<WrbWaterConsume> comparator = new BeanComparator<>(orderColumn);
            if (ascOrDesc != null && ascOrDesc) {
                comparator = comparator.reversed();
            }
            records = records.stream().sorted(comparator).collect(Collectors.toList());
        }
        return records;
    }

    /**
     * 水库蓄水量公报统计
     *
     * @param year
     * @return
     */
    @Override
    public Object stats4WrbRsvrStore(Integer year) {
        return rsvrStoreManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "stats4WrbRsvrStore")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

    /**
     * 水库蓄水量公报C位统计
     *
     * @return
     */
    @Override
    public JSONObject statsCwindow4WrbRsvrStore(Integer year) {
        return (JSONObject) rsvrStoreManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "statsCwindow4WrbRsvrStore")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

    /**
     * 水库蓄水量公报详表
     *
     * @param orderColumn
     * @param ascOrDesc
     * @return
     */
    @Override
    public List<WrbStoreRsvResVo> table4WrbRsvrStore(Integer year, String orderColumn, Boolean ascOrDesc) {
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_store_rsvr", null);
        }
        Integer currentYear = year;
        Integer lastYear = currentYear - 1;
        List<WrbStoreRsvr> currentYearRecords = wrbMapper.selectWrbStoreRsvr(currentYear, null);
        currentYearRecords = currentYearRecords == null ? Collections.emptyList() : currentYearRecords;

        List<WrbStoreRsvr> lastYearRecords = wrbMapper.selectWrbStoreRsvr(lastYear, null);
        lastYearRecords = lastYearRecords == null ? Collections.emptyList() : lastYearRecords;
        Map<String, WrbStoreRsvr> lastYearMap = lastYearRecords.stream().collect(Collectors.toMap(WrbStoreRsvr::getResCode, Function.identity()));

        List<WrbStoreRsvResVo> resultList = new ArrayList<>(currentYearRecords.size());
        for (WrbStoreRsvr curRecord : currentYearRecords) {
            WrbStoreRsvResVo temp = new WrbStoreRsvResVo();
            BeanUtils.copyProperties(curRecord, temp);
            temp.setCurrentStore(curRecord.getEndYearStore());
            WrbStoreRsvr lastYearRecord = lastYearMap.get(curRecord.getResCode());
            if (lastYearRecord != null) {
                temp.setLastYearStore(lastYearRecord.getEndYearStore());
            }
            temp.doSubtract();
            resultList.add(temp);
        }
        if (StringUtils.isNotBlank(orderColumn)) {
            Comparator<WrbStoreRsvResVo> comparator = new BeanComparator<>(orderColumn);
            if (ascOrDesc != null && ascOrDesc) {
                comparator = comparator.reversed();
            }
            resultList = resultList.stream().sorted(comparator).collect(Collectors.toList());
        }
        return resultList;
    }

    /**
     * 水库蓄水量公报蓄水曲线
     *
     * @param resCode
     * @return
     */
    @Override
    public List<WrbStoreRsvr> curve4WrbRsvrStore(String resCode) {
        if (StringUtils.isBlank(resCode)) {
            return Collections.emptyList();
        }
        List<WrbStoreRsvr> currentYearRecords = wrbMapper.selectWrbStoreRsvr(null, resCode);
        currentYearRecords = currentYearRecords == null ? Collections.emptyList() : currentYearRecords;
        return currentYearRecords.stream().sorted(Comparator.comparing(WrbStoreRsvr::getYear)).collect(Collectors.toList());
    }

    /**
     * 水资源承载能力大屏组件统计接口
     *
     * @param type 1：用水量评价；2：水质评价；3：经济评价；4：人口评价；5：用水评价
     * @return
     */
    @Override
    public CommonResult stats4WraUnit(Integer type) {
        return (CommonResult) wResourceAbilityManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "stats4WraUnit")
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }

    /**
     * 水资源承载能力大屏组件C位统计接口
     *
     * @return
     */
    @Override
    public JSONObject statsCwindow4WraUnit() {
        return (JSONObject) wResourceAbilityManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "statsCwindow4WraUnit")
                .build());
    }

    /**
     * 流域生态流量组件统计以及C位窗口
     *
     * @return
     */
    @Override
    public CommonResult stats4SectFlow() {
        return (CommonResult) basEcoFlowManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "stats4SectFlow")
                .build());
    }

    /**
     * 水源保证列表和柱状图
     *
     * @param watSrcName 水源名称
     * @return
     */
    @Override
    public CommonResult rsvrWatSrcTable(String watSrcName) {
        return (CommonResult) watSrcGuaranteeManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "rsvrWatSrcTable")
                .with(UnitDataReqVo::putParam, "watSrcName", watSrcName)
                .build());
    }

    /**
     * 水源保障c位
     *
     * @return
     */
    @Override
    public CommonResult rsvrWatSrc4CWindow() {
        return (CommonResult) watSrcGuaranteeManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "rsvrWatSrc4CWindow")
                .build());
    }

    /**
     * 查询指定行政区划的近年的节水指标
     *
     * @param adcd      行政区划
     * @param startYear 起始年份
     * @param endYear   结束年份
     * @return
     */
    @Override
    public CommonResult watSavNormByYearInterval(String adcd, Integer startYear, Integer endYear) {
        return (CommonResult) watSavNormManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "watSavNormByYearInterval")
                .with(UnitDataReqVo::putParam, "adcd", adcd)
                .with(UnitDataReqVo::putParam, "startYear", startYear)
                .with(UnitDataReqVo::putParam, "endYear", endYear)
                .build());
    }

    /**
     * 查询指定年份的节水指标
     *
     * @param year        年份
     * @param compareType 1. 年用水总量;2.万元GDP用水量；3.万元工业增加至用水量；4农业灌溉水有效利用系数；5.县域节水型社会创建达标率
     * @return
     */
    @Override
    public CommonResult watSavNormByYear(Integer year, Integer compareType) {
        return (CommonResult) watSavNormManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "watSavNormByYear")
                .with(UnitDataReqVo::putParam, "year", year)
                .with(UnitDataReqVo::putParam, "compareType", compareType)
                .build());
    }

    /**
     * 节水指标c位
     *
     * @return
     */
    @Override
    public CommonResult watSavNorm4CWindow() {
        return (CommonResult) watSavNormManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "watSavNorm4CWindow")
                .build());
    }

    /**
     * 用水效率组件卡片
     *
     * @param areaLevel 国际，国内，省内，市内
     * @param year      年份
     * @return
     */
    @Override
    public CommonResult wpgEffect(String areaLevel, Integer year) {
        return (CommonResult) watUseEfficiencyManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "wpgEffect")
                .with(UnitDataReqVo::putParam, "areaLevel", areaLevel)
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

    /**
     * 用水效率组件C位
     *
     * @return
     */
    @Override
    public CommonResult wpgEffect4CWindow() {
        return (CommonResult) watUseEfficiencyManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "wpgEffect4CWindow")
                .build());
    }

    /**
     * 三条红线用水总量
     *
     * @param year
     * @return
     */
    @Override
    public CommonResult redLineWatCsm(Integer year) {
        return (CommonResult) redLine3Manager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "redLineWatCsm")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

    /**
     * 三条红线用水效率
     *
     * @param year
     * @return
     */
    @Override
    public CommonResult redLineWpgEffect(Integer year) {
        return (CommonResult) redLine3Manager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "redLineWpgEffect")
                .with(UnitDataReqVo::putParam, "year", year)
                .build());
    }

    /**
     * 三条红线C位窗口
     *
     * @return
     */
    @Override
    public CommonResult redLine4CWindow() {
        return (CommonResult) redLine3Manager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "redLine4CWindow")
                .build());
    }

    /**
     * 取水监管可证统计
     *
     * @param type 1. 根据行政区划统计，2.按取水许可统计（过期、超许可、有证无点、取水偏快）
     * @return
     */
    @Override
    public CommonResult intakeSuperviseStats(int type) {
        return (CommonResult) intakeSuperviseManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "intakeSuperviseStats")
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }

    /**
     * 取水监管C位窗口
     *
     * @return
     */
    @Override
    public CommonResult intakeSupervise4CWindow() {
        return (CommonResult) intakeSuperviseManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "intakeSupervise4CWindow")
                .build());
    }

    /**
     * 取水户统计
     *
     * @param type 1. 根据行政区划统计，2.超许可统计：超许可、接近许可、正常
     * @return
     */
    @Override
    public CommonResult wiuStats(int type) {
        return (CommonResult) wiuUnitManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "wiuStats")
                .with(UnitDataReqVo::putParam, "type", type)
                .build());
    }

    /**
     * 取水户组件C位窗口
     *
     * @return
     */
    @Override
    public CommonResult wiuUnit4CWindow() {
        return (CommonResult) wiuUnitManager.getData(Builder.of(UnitDataReqVo::new)
                .with(UnitDataReqVo::setStrategy, "wiuUnit4CWindow")
                .build());
    }
}
