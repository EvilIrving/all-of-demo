package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.big.screen.mapper.HeavyRainsArchiveMapper;
import com.ygkj.big.screen.model.AttStBase;
import com.ygkj.big.screen.model.HeavyRainsArchive;
import com.ygkj.big.screen.service.GiveAnAlarmService;
import com.ygkj.big.screen.service.HeavyRainsArchiveService;
import com.ygkj.enums.StationType;
import com.ygkj.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2022/5/6 13:51
 * @Description:
 */
@Service
public class HeavyRainsArchiveServiceImpl extends ServiceImpl<HeavyRainsArchiveMapper, HeavyRainsArchive> implements HeavyRainsArchiveService {

    @Autowired
    private HeavyRainsArchiveMapper heavyRainsArchiveMapper;
    @Autowired
    private GiveAnAlarmService giveAnAlarmService;

    @Override
    public String precipitation(HeavyRainsArchive heavyRainsArchive) {

       List<AttStBase> attStBases = heavyRainsArchiveMapper.getRainStcd("1");
       List<String> codes = attStBases.stream().map(AttStBase::getStCode).collect(Collectors.toList());
       //查询时间范围内的雨量数据统计每个测站总雨量值，并且求平均值，不存在数据的测站不计算到求平均值的被除数中
        heavyRainsArchive.setCodes(codes);
        List<String> tableNames =  giveAnAlarmService.echoWaterRainTableByDateInternal(StationType.RAINFALL.table(), DateUtils.parseDate("yyyy-MM-dd HH",heavyRainsArchive.getStartTime()),DateUtils.parseDate("yyyy-MM-dd HH",heavyRainsArchive.getEndTime()));
       return heavyRainsArchiveMapper.getAvgSumDrp(tableNames,heavyRainsArchive);
    }

    @Override
    public List<JSONObject> hourlyRainfallvalue(HeavyRainsArchive heavyRainsArchive) {
        List<AttStBase> attStBases = heavyRainsArchiveMapper.getRainStcd("1");
        List<String> codes = attStBases.stream().map(AttStBase::getStCode).collect(Collectors.toList());
        //查询时间范围内的雨量数据统计每个测站小时雨量值，并且求平均值，不存在小时数据的测站不计算到求平均值的被除数中
        heavyRainsArchive.setCodes(codes);
        List<String> tableNames =  giveAnAlarmService.echoWaterRainTableByDateInternal(StationType.RAINFALL.table(), DateUtils.parseDate("yyyy-MM-dd HH",heavyRainsArchive.getStartTime()),DateUtils.parseDate("yyyy-MM-dd HH",heavyRainsArchive.getEndTime()));
        List<JSONObject> hourDrp = heavyRainsArchiveMapper.getAvgHourDrp(tableNames,heavyRainsArchive);
        return hourDrp;
    }
}

