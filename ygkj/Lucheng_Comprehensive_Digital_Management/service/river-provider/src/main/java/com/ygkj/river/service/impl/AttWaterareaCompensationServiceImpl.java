package com.ygkj.river.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.river.mapper.AttWaterareaCompensationMapper;
import com.ygkj.river.service.AttWaterareaCompensationService;
import com.ygkj.river.vo.response.AttWaterareaCompensationRespVo;
import com.ygkj.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author hucong
 * @description 水域占用补偿表
 * @date 2021-08-12
 */
@Service
public class AttWaterareaCompensationServiceImpl implements AttWaterareaCompensationService {

    @Resource
    private AttWaterareaCompensationMapper attWaterareaCompensationMapper;

    @Override
    public AttWaterareaCompensationRespVo statisticsReclaimedAreaWater(Integer year) {
        if (year == null) {
            year = Integer.parseInt(DateUtils.format(new Date(), "yyyy"));
        }
        AttWaterareaCompensationRespVo jo = attWaterareaCompensationMapper.statisticsReclaimedAreaWater(year);
        return jo;
    }


    @Override
    public List<JSONObject> statisticsReclaimedByYear() {
        List<Integer> years = attWaterareaCompensationMapper.calYear();
        List<JSONObject> objectList = new LinkedList<>();
        JSONObject jsonObject = null;
        for (Integer year : years) {
            jsonObject = new JSONObject();
            jsonObject.put("year", year);
            List<JSONObject> dataList = attWaterareaCompensationMapper.statisticsReclaimedByYear(year);
            jsonObject.put("list", dataList);
            objectList.add(jsonObject);
        }
        return objectList;
    }

    @Override
    public List<AttWaterareaCompensationRespVo> waterCompensationEachAdministrative(Integer year) {
        if (year == null) {
            year = Integer.parseInt(DateUtils.format(new Date(), "yyyy"));
        }
        List<AttWaterareaCompensationRespVo> attWaterareaCompensationRespVoList =
                attWaterareaCompensationMapper.waterAreaEachAdministrative(year);
        if (!attWaterareaCompensationRespVoList.isEmpty() && attWaterareaCompensationRespVoList.size() >= 2) {
            attWaterareaCompensationRespVoList.get(0).setValueType("3");
            attWaterareaCompensationRespVoList.get(attWaterareaCompensationRespVoList.size() - 1).setValueType("1");
        }
        return attWaterareaCompensationRespVoList;
    }
}
