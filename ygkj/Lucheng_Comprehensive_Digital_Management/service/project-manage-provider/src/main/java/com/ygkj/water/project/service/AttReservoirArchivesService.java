package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @description 大中型水库档案
 * @author lxl
 * @date 2021-05-04
 */
public interface AttReservoirArchivesService {

    /**
     * 返回大中型水库数据 当前年统计数量列表
     * @return
     */
    JSONObject reservoirArchivesCount();

}

