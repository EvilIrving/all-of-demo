package com.ygkj.lcsyn.mapper;


import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface BusinessMapper {

    /**
     * 批量保存雨量
     *
     * @param list
     */
    void batchRelaceRainfall(List<JSONObject> list);

    /**
     * 批量保存水库水位
     *
     * @param list
     */
    void batchRelaceRsvr(List<JSONObject> list);

    /**
     * 批量保存河道水位
     *
     * @param list
     */
    void batchRelaceRiver(List<JSONObject> list);
}
