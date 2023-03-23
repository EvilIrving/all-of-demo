package com.ygkj.river.service;

import com.ygkj.entity.SingletonMap;
import com.ygkj.river.model.AttRivInletBase;
import com.ygkj.river.vo.request.RivInletReqVo;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/6
 */
public interface RivInletService {
    /**
     * 查询雨水口/排污口列表
     *
     * @param reqVo
     * @return
     */
    List<AttRivInletBase> list(RivInletReqVo reqVo);

    /**
     * 雨水口/排污口 统计
     *
     * @param statType   统计类型 1. 根据行政区划统计
     * @param recordType 1.统计雨水口，2. 统计排污口
     * @return
     */
    List<SingletonMap<String, Long>> rivInletStats(Integer statType, Integer recordType);

}
