package com.ygkj.river.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.river.vo.response.AttWaterareaCompensationRespVo;

import java.util.List;

public interface AttWaterareaCompensationService {

    AttWaterareaCompensationRespVo statisticsReclaimedAreaWater(Integer year);

    /**
     * 水域占补面积按年份统计
     *
     * @return
     */
    List<JSONObject> statisticsReclaimedByYear();

    List<AttWaterareaCompensationRespVo> waterCompensationEachAdministrative(Integer year);

}
