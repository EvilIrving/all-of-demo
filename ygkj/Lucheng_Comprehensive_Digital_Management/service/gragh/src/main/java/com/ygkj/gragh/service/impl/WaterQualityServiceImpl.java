package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.mapper.AttAdcdBaseMapper;
import com.ygkj.gragh.mapper.AttBswqBaseMapper;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.gragh.model.AttBswqBase;
import com.ygkj.gragh.model.BswqRatio;
import com.ygkj.gragh.service.WaterQualityService;
import com.ygkj.gragh.vo.request.BswqReqVo;
import com.ygkj.gragh.vo.response.BswqResVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/4/21
 */
@Service
public class WaterQualityServiceImpl implements WaterQualityService {

    @Resource
    AttBswqBaseMapper bswqMapper;

    @Resource
    AttAdcdBaseMapper attAdcdBaseMapper;

    @Override
    public List<BswqResVo> list(BswqReqVo reqVo) {
        if (StringUtils.isNotBlank(reqVo.getAdcd()) && !StringUtils.startsWith(reqVo.getAdcd(), "3303")) {
            reqVo.setAdcd(attAdcdBaseMapper.selectWzAdcdByAdnm(reqVo.getAdcd()));
        }
        List<AttBswqBase> stations = bswqMapper.selectByCondition(reqVo);
        if (CollectionUtils.isBlank(stations)) {
            return Collections.emptyList();
        }
        List<BswqResVo> values = bswqMapper.selectNewestValueByCode(String.join(",", stations.stream().map(AttBswqBase::getCode).collect(Collectors.toList())));
        List<BswqResVo> result = new ArrayList<>(stations.size());
        if (CollectionUtils.isNotBlank(values)) {
            Map<String, BswqResVo> map = values.stream().collect(Collectors.toMap(BswqResVo::getCode, o -> o));
            for (AttBswqBase station : stations) {
                BswqResVo temp = map.get(station.getCode());
                if (temp == null) {
                    temp = new BswqResVo();
                }
                temp.setCode(station.getCode());
                temp.setLatitude(station.getLatitude());
                temp.setLongitude(station.getLongitude());
                temp.setName(station.getName());
                temp.setAdcd(station.getAdcd());
                temp.setPmLimit(station.getPmLimit());
                result.add(temp);
            }
        }
        return result;
    }

    @Override
    public List<BswqResVo> bswqLevel(String stationCode, int days) {
        AttBswqBase station = this.bswqMapper.load(stationCode);
        if (station == null) {
            return Collections.emptyList();
        }
        LocalDate now = LocalDate.now();
        Date end = DateUtils.localDate2Date(now);
        LocalDate ago = now.plusDays(-days);
        Date start = DateUtils.localDate2Date(ago);
        List<BswqResVo> values = this.bswqMapper.selectValueByCodeAndTime(stationCode, start, end);
        if (CollectionUtils.isNotBlank(values)) {
            for (BswqResVo temp : values) {
                temp.setLatitude(station.getLatitude());
                temp.setLongitude(station.getLongitude());
                temp.setName(station.getName());
                temp.setAdcd(station.getAdcd());
                temp.setPmLimit(station.getPmLimit());
            }
        }
        return values;
    }

    @Override
    public List<BswqRatio> bswqRatio(int type, int months) {
        LocalDate origin = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        Date start = DateUtils.localDate2Date(origin.minusMonths(months));
        Date end = DateUtils.localDate2Date(origin);
        return this.bswqMapper.bswqRatio(type, start, end);
    }

    /**
     * 水质监测站点统计
     *
     * @param type 1 根据行政区划统计
     * @return
     */
    @Override
    public List<ChartResVo<String, Long>> bswqStatistic(int type) {
        switch (type) {
            case 1: {
                List<AttAdcdBase> adcdList = attAdcdBaseMapper.selectWenZhouCounty();
                List<ChartResVo<String, Long>> records = this.bswqMapper.countByAdcd();
                List<ChartResVo<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = records.stream().collect(Collectors.toMap(ChartResVo::getKey, ChartResVo::getValue));
                for (AttAdcdBase adcd : adcdList) {
                    result.add(Builder.of(ChartResVo<String, Long>::new)
                            .with(ChartResVo::setKey, adcd.getAdnm())
                            .with(ChartResVo::setValue, recordMap.getOrDefault(adcd.getAdcd(), 0L))
                            .build());
                }
                return result.stream().sorted(Comparator.comparing(ChartResVo<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            default:
                return Collections.emptyList();
        }
    }
}
