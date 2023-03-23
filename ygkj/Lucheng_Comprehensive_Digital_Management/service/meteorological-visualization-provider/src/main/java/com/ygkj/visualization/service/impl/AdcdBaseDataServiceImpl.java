package com.ygkj.visualization.service.impl;

import com.ygkj.utils.CollectionUtils;
import com.ygkj.visualization.mapper.AdcdBaseDataMapper;
import com.ygkj.visualization.model.AntiTyphoonInfo;
import com.ygkj.visualization.model.GdpInfo;
import com.ygkj.visualization.model.PopulationInfo;
import com.ygkj.visualization.service.AdcdBaseDataService;
import com.ygkj.visualization.vo.response.AdcdBaseDataStatisticsRespVo;
import com.ygkj.visualization.vo.response.AdcdStatisticsInfo;
import com.ygkj.visualization.vo.response.AreaGdpStatistics;
import com.ygkj.visualization.vo.response.EcoPopuStatisticsResVo;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdcdBaseDataServiceImpl implements AdcdBaseDataService {

    @Resource
    private AdcdBaseDataMapper adcdBaseDataMapper;

    private final static String wzAdcd = "330300000000";

    @Override
    public CommonResult<AdcdBaseDataStatisticsRespVo> dataStatistics(String adnm, String type) {

        AdcdBaseDataStatisticsRespVo respVo = new AdcdBaseDataStatisticsRespVo();

        if (StringUtils.isEmpty(adnm)){
            return CommonResult.failed("行政区划信息为空");
        }

        if ("1".equals(type)) {
            // 区县人口
            List<AdcdStatisticsInfo> populationInfos = adcdBaseDataMapper.getPopInfos(adnm);

            if (CollectionUtils.isNotBlank(populationInfos)) {
                // 统计总人口
                double sum = populationInfos.stream().filter(pop -> pop.getPopNum() != null).mapToDouble(pop -> pop.getPopNum()).sum();
                respVo.setTotalPop(sum);
                respVo.setAdcdStatisticsInfos(populationInfos);
            }
        }
        if ("2".equals(type)) {
            // 区县经济，10年经济统计
            List<AreaGdpStatistics> areaGdps = adcdBaseDataMapper.getAreaGdps(adnm);
            if (CollectionUtils.isNotBlank(areaGdps)){
                double sum = areaGdps.stream().filter(gdp -> gdp.getGdp() != null).mapToDouble(gdp -> gdp.getGdp()).sum();
                respVo.setTotalGDP(sum);
                respVo.setGpdYearStatistics(areaGdps);
            }
        }
        if ("3".equals(type)) {
            // 防台联系人
            List<AntiTyphoonInfo> antiTyphoonInfos = adcdBaseDataMapper.getAntiTyphoonInfos(adnm);
            respVo.setAntiTyphoonInfos(antiTyphoonInfos);
        }
        return CommonResult.success(respVo);
    }

    final static String yearKey="year";
    final static String valueKey="value";
    @Override
    public Object ecoPopuStatistics(String adcd, Integer years) {
        EcoPopuStatisticsResVo result=new EcoPopuStatisticsResVo();
        if (years < 1) {
            return result;
        }
        Integer endYear=LocalDate.now().getYear() + 1;
        Integer startYear = endYear-years;
        List<GdpInfo> gdpInfos= adcdBaseDataMapper.getGdpByYearRangeAndAdcd(startYear.toString(),endYear.toString(),adcd);
        if (gdpInfos==null){
            gdpInfos=Collections.emptyList();
        }
        List<PopulationInfo> popuInfos= adcdBaseDataMapper.getPopByYearRangeAndAdcd(startYear.toString(),endYear.toString(),adcd,2);
        if (popuInfos==null){
            popuInfos=Collections.emptyList();
        }
        Map<String,List<GdpInfo>> gdpInfoMap=gdpInfos.stream().collect(Collectors.groupingBy(GdpInfo::getYear));
        Map<String,List<PopulationInfo>> popuInfoMap=popuInfos.stream().collect(Collectors.groupingBy(PopulationInfo::getYear));

        List<Map<String,Object>> gdpSumByYear=new ArrayList<>(years+1);
        List<Map<String,Object>> popuSumByYear=new ArrayList<>(years+1);
        for (int cYear = startYear; cYear < endYear; cYear++) {
            String cYearKey=String.valueOf(cYear);
            List<GdpInfo> tempGdpList=gdpInfoMap.getOrDefault(cYearKey,Collections.emptyList());
            List<PopulationInfo> tempPopuList=popuInfoMap.getOrDefault(cYearKey,Collections.emptyList());
            double gdpSum=tempGdpList.stream().mapToDouble(o->{
                if (o.getGdp()==null){
                    return 0;
                }
                return o.getGdp();
            }).sum();

            double popuSum=tempPopuList.stream().mapToDouble(o->{
                if (o.getPopNum()==null){
                    return 0;
                }
                return o.getPopNum();
            }).sum();
            Map<String,Object> gdpSumByYearTemp=new HashMap<>();
            gdpSumByYearTemp.put(yearKey,cYear);
            gdpSumByYearTemp.put(valueKey,gdpSum);
            Map<String,Object> popuSumByYearTemp=new HashMap<>();
            popuSumByYearTemp.put(yearKey,cYear);
            popuSumByYearTemp.put(valueKey,popuSum);
            gdpSumByYear.add(gdpSumByYearTemp);
            popuSumByYear.add(popuSumByYearTemp);
        }
        result.setGdpSumByYear(gdpSumByYear);
        result.setPopuSumByYear(popuSumByYear);
        return result;
    }

    @Override
    public Object ecoPopuStatisticsByYears(Integer years) {
        EcoPopuStatisticsResVo result=new EcoPopuStatisticsResVo();
        if (years < 1) {
            return result;
        }
        Integer endYear=LocalDate.now().getYear() + 1;
        Integer startYear = endYear-years;
        List<GdpInfo> gdpInfos= adcdBaseDataMapper.getGdpByYearRange(startYear.toString(),endYear.toString());
        if (gdpInfos==null){
            gdpInfos=Collections.emptyList();
        }
        List<PopulationInfo> popuInfos= adcdBaseDataMapper.getPopByYearRange(startYear.toString(),endYear.toString(),2);
        if (popuInfos==null){
            popuInfos=Collections.emptyList();
        }
        Map<String,List<GdpInfo>> gdpInfoMap=gdpInfos.stream().collect(Collectors.groupingBy(GdpInfo::getYear));
        Map<String,List<PopulationInfo>> popuInfoMap=popuInfos.stream().collect(Collectors.groupingBy(PopulationInfo::getYear));

        List<Map<String,Object>> gdpSumByYear=new ArrayList<>(years+1);
        List<Map<String,Object>> popuSumByYear=new ArrayList<>(years+1);

        DecimalFormat decimalFormat=new DecimalFormat("0.##");
        for (int cYear = startYear; cYear < endYear; cYear++) {
            String cYearKey=String.valueOf(cYear);
            List<GdpInfo> tempGdpList=gdpInfoMap.getOrDefault(cYearKey,Collections.emptyList());
            List<PopulationInfo> tempPopuList=popuInfoMap.getOrDefault(cYearKey,Collections.emptyList());
            double gdpSum=tempGdpList.stream().mapToDouble(o->{
                if (o.getGdp()==null){
                    return 0;
                }
                return o.getGdp();
            }).sum();

            double popuSum=tempPopuList.stream().mapToDouble(o->{
                if (o.getPopNum()==null){
                    return 0;
                }
                return o.getPopNum();
            }).sum();
            Map<String,Object> gdpSumByYearTemp=new HashMap<>();
            gdpSumByYearTemp.put(yearKey,cYear);
            gdpSumByYearTemp.put(valueKey,decimalFormat.format(gdpSum));
            Map<String,Object> popuSumByYearTemp=new HashMap<>();
            popuSumByYearTemp.put(yearKey,cYear);
            popuSumByYearTemp.put(valueKey,decimalFormat.format(popuSum));
            gdpSumByYear.add(gdpSumByYearTemp);
            popuSumByYear.add(popuSumByYearTemp);
        }
        // 倒序遍历，移除最近最近无数据的年份
        for (int i = gdpSumByYear.size() - 1; i >= 0; i--){
            String value = (String) gdpSumByYear.get(i).get("value");
            if ("0".equals(value)){
                gdpSumByYear.remove(gdpSumByYear.size() - 1);
            } else {
                break;
            }
        }

        for (int i = popuSumByYear.size() - 1; i >= 0; i--){
            String value = (String) popuSumByYear.get(i).get("value");
            if ("0".equals(value)){
                popuSumByYear.remove(popuSumByYear.size() - 1);
            } else {
                break;
            }
        }
        result.setGdpSumByYear(gdpSumByYear);
        result.setPopuSumByYear(popuSumByYear);
        return result;
    }
}
