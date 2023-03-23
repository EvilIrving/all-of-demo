package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.mapper.AttAdcdBaseMapper;
import com.ygkj.big.screen.mapper.WaterResourceBulletinMapper;
import com.ygkj.big.screen.model.AttAdcdBase;
import com.ygkj.big.screen.model.WrbRainfall;
import com.ygkj.big.screen.model.WrbReducedWater;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.entity.SingletonMap;
import com.ygkj.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 降水量公报组件
 * @Date 2021/7/29
 */
@Component
public class WrbRainfallManager extends AbstractUnitManager {

    @Resource
    WaterResourceBulletinMapper wrbMapper;

    @Resource
    AttAdcdBaseMapper adcdBaseMapper;

    private final static double zeroD = 0D;

    private final static BigDecimal hundred = BigDecimal.valueOf(100);

    public WrbRainfallManager(@Autowired ApplicationContext context) {
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
            case "stats4WrbRainfall": {
                Integer year = reqVo.getParam("year", Integer.class);
                return stats4WrbRainfall(year);
            }
            case "statsCwindow4WrbRainfall": {
                Integer year = reqVo.getParam("year", Integer.class);
                return statsCwindow4WrbRainfall(year);
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }

    /**
     * 降水公报统计
     *
     * @param year
     * @return
     */
    public JSONObject stats4WrbRainfall(Integer year) {
        List<AttAdcdBase> adcdList = this.adcdBaseMapper.adcdAdnmCountyOfWzExcludeCityLevel();
        adcdList = adcdList == null ? Collections.emptyList() : adcdList;
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_rainfall", null);
        }
        List<WrbRainfall> rainfallList = wrbMapper.selectWrbRainfall(year, null);
        rainfallList = rainfallList == null ? Collections.emptyList() : rainfallList;
        Map<String, List<WrbRainfall>> groupMap = rainfallList.stream().collect(Collectors.groupingBy(WrbRainfall::getAdcd));
        List<JSONObject> list = new ArrayList<>(adcdList.size());
        JSONObject result = new JSONObject();
        OptionalDouble averageOption = rainfallList.stream().filter(o -> o.getRainfall() != null).mapToDouble(o -> o.getRainfall().doubleValue()).average();
        if (averageOption.isPresent()) {
            result.put("average", averageOption.getAsDouble());
        } else {
            result.put("average", 0d);
        }
        Map<String, BigDecimal> countyYearAvgRainMap = SingletonMap.list2Map(this.wrbMapper.yearAvgRainOfCounty());
        for (AttAdcdBase adcd : adcdList) {
            List<WrbRainfall> tempList = groupMap.getOrDefault(adcd.getAdcd(), Collections.emptyList());
            BigDecimal yearAvgRain = countyYearAvgRainMap.getOrDefault(adcd.getAdcd(), BigDecimal.ZERO);
            JSONObject temp = new JSONObject();
            temp.put("areaName", adcd.getAdnm());
            Double rainfall = tempList.stream().filter(o -> o.getRainfall() != null).mapToDouble(o -> o.getRainfall().doubleValue()).sum();
            temp.put("rainfall", rainfall);
            temp.put("yearAvgRain", yearAvgRain);
            // 比年均多>0 比年均少<0,持平=0
            temp.put("compareAvgRain", Double.compare(rainfall, yearAvgRain.doubleValue()));
            list.add(temp);
        }
        result.put("statsList", list);
        result.put("year", CollectionUtils.isNotBlank(rainfallList) ? rainfallList.get(0).getYear() : year);
        return result;
    }

    /**
     * 降水量公报C位统计
     *
     * @return
     */
    public JSONObject statsCwindow4WrbRainfall(Integer year) {
//      2021-09-18 应要求 已手动讲将温州市改为市本级，同时C位改为默认取市本级，请@黄依伦
        // 2021-09-27 发现处室要求“将温州市改为市本级不对”，因此又将温州改为市本级
        String wzAreaCode = "330300000000";
        List<WrbRainfall> rainfallList = wrbMapper.selectWrbRainfall(year, wzAreaCode);
        rainfallList = rainfallList == null ? Collections.emptyList() : rainfallList;
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_rainfall", wzAreaCode);
        }
        List<WrbReducedWater> reducedWaterList = wrbMapper.selectWrbReducedWater(year);
        reducedWaterList = reducedWaterList == null ? Collections.emptyList() : reducedWaterList;
        BigDecimal averageRainfall = BigDecimal.ZERO;
        if (CollectionUtils.isNotBlank(rainfallList)) {
            WrbRainfall record = rainfallList.get(0);
            year = record.getYear();
            averageRainfall = record.getRainfall();
        }
        JSONObject result = new JSONObject();
        result.put("averageRainfall", averageRainfall);
        result.put("totalReducedWater", reducedWaterList.stream().filter(o -> o.getReducedWater() != null).mapToDouble(o -> o.getReducedWater().doubleValue()).sum());
        result.put("year", year);
        BigDecimal anomaly = computeAnomaly(this.wrbMapper.countyYearAvgRain(wzAreaCode), averageRainfall);
        result.put("anomaly", anomaly);
        result.put("watResState", watResState(anomaly.doubleValue()));
        return result;
    }

    public BigDecimal computeAnomaly(BigDecimal yearAvgRain, BigDecimal curYearRain) {
        if (yearAvgRain == null) {
            yearAvgRain = BigDecimal.ZERO;
        }
        if (curYearRain == null) {
            curYearRain = BigDecimal.ZERO;
        }
        if (yearAvgRain.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return curYearRain.subtract(yearAvgRain).multiply(hundred).divide(yearAvgRain, 0, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 距平值＞20%为丰水年、距平值-20%~-20%为平水年、距平值＜-20%为枯水年
     *
     * @param anomaly 距平值
     * @return java.lang.Integer  1:丰水年，0：平水年，-1:枯水年
     */
    public Integer watResState(Double anomaly) {
        if (anomaly == null) {
            anomaly = zeroD;
        }
        if (anomaly > 20) {
            return 1;
        } else if (anomaly >= -20) {
            return 0;
        } else {
            return -1;
        }
    }
}
