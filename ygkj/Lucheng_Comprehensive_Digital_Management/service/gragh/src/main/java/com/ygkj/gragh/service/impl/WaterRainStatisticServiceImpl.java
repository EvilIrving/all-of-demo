package com.ygkj.gragh.service.impl;

import com.ygkj.enums.StationType;
import com.ygkj.gragh.mapper.AttAdcdBaseMapper;
import com.ygkj.gragh.mapper.AttStBaseMapper;
import com.ygkj.gragh.mapper.WaterRainStatisticMapper;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.gragh.service.WaterRainStatisticService;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Date 2021/2/23
 */
@Service
public class WaterRainStatisticServiceImpl implements WaterRainStatisticService {
    @Resource
    AttAdcdBaseMapper adcdMapper;

    @Resource
    WaterRainStatisticMapper statisticMapper;

    @Resource
    AttStBaseMapper attStBaseMapper;

    final static List<String> icAdcd = Arrays.asList("330351000000",
            "330353000000",
            "330354000000",
            "330355000000");

    @Override
    public Object statisticStationNumByAdcd(String stTypeString) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<AttAdcdBase> adList = adcdMapper.selectAllWzAdNotIncludeCommittee(icAdcd);
        Map<String, Integer> stNumMap = null;
        if (StringUtils.isNotBlank(stTypeString) && CollectionUtils.isNotBlank(adList)) {
            List<String> stTypeList = Arrays.asList(stTypeString.split(","));
            List<Map<String, Object>> stNumList = statisticMapper.statisticStationNumByAdcd(stTypeList);
            stNumMap = stNumList.stream().collect(Collectors.toMap(o -> (String) o.get("area_code"), o -> ((Long) o.get("num")).intValue()));
        } else {
            stNumMap = Collections.emptyMap();
        }
        int numSum = 0;
        for (AttAdcdBase attAdcdBase : adList) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("adcd", attAdcdBase.getAdcd());
            temp.put("adnm", attAdcdBase.getAdnm());
            int num = stNumMap.getOrDefault(attAdcdBase.getAdcd(), 0);
            numSum += num;
            temp.put("num", num);
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<ChartResVo<String, Long>> countStTypeNumByAdcdBasin(String adcd, String basin) {
        List<ChartResVo<String, Long>> records = attStBaseMapper.countStTypeNumByAdcdBasin(adcd, basin);
        records = records == null ? Collections.emptyList() : records;
        Map<String, Long> countMap = records.stream().collect(Collectors.toMap(ChartResVo::getKey, ChartResVo::getValue));
        StationType[] projectTypes = StationType.values();
        List<ChartResVo<String, Long>> result = new ArrayList<>(projectTypes.length);
        for (StationType stationType : projectTypes) {
            if (stationType.equals(StationType.RESERVOIR2) || stationType.equals(StationType.RESERVOIR3)) {
                continue;
            }
            result.add(Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, stationType.getDescription())
                    .with(ChartResVo::setValue, countMap.getOrDefault(stationType.type(), 0L))
                    .build());
        }
        return result;
    }
}
