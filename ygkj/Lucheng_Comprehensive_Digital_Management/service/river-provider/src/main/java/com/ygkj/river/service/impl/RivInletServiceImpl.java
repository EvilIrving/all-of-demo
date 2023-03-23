package com.ygkj.river.service.impl;

import com.ygkj.river.mapper.AttRivInletBaseMapper;
import com.ygkj.entity.SingletonMap;
import com.ygkj.river.mapper.AttAdcdBaseMaper;
import com.ygkj.river.model.AttAdcdBase;
import com.ygkj.river.model.AttRivInletBase;
import com.ygkj.river.service.RivInletService;
import com.ygkj.river.vo.request.RivInletReqVo;
import com.ygkj.utils.Builder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/8/6
 */
@Service
public class RivInletServiceImpl implements RivInletService {

    @Resource
    AttRivInletBaseMapper rivInletBaseMapper;

    @Resource
    AttAdcdBaseMaper adcdBaseMaper;

    /**
     * 查询雨水口/排污口列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<AttRivInletBase> list(RivInletReqVo reqVo) {
        return rivInletBaseMapper.selectByCondition(reqVo);
    }

    /**
     * 雨水口/排污口 统计
     *
     * @param statType   统计类型 1. 根据行政区划统计
     * @param recordType 1.统计雨水口，2. 统计排污口
     * @return
     */
    @Override
    public List<SingletonMap<String, Long>> rivInletStats(Integer statType, Integer recordType) {
        if (statType == null || recordType == null) {
            return Collections.emptyList();
        }
        switch (statType) {
            case 1: {
                List<AttAdcdBase> adcdList = adcdBaseMaper.countyOfWenZhou();
                List<SingletonMap<String, Long>> records = this.rivInletBaseMapper.countByAreaCode(recordType);
                List<SingletonMap<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = SingletonMap.list2Map(records);
                for (AttAdcdBase adcd : adcdList) {
                    result.add(Builder.of(SingletonMap<String, Long>::new)
                            .with(SingletonMap::setKey, adcd.getAdnm())
                            .with(SingletonMap::setValue, recordMap.getOrDefault(adcd.getAdcd(), 0L))
                            .build());
                }
                return result.stream().sorted(Comparator.comparing(SingletonMap<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            default:
                return Collections.emptyList();
        }
    }
}
