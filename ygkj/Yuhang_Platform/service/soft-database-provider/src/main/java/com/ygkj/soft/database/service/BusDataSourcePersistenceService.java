package com.ygkj.soft.database.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/17 18:33
 * @Description:
 */
public interface BusDataSourcePersistenceService {

    List<JSONObject> countPastDaysPersistenceRows(int pastDay);
}
