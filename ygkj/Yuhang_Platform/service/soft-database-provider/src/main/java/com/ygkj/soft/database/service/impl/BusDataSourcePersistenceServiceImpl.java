package com.ygkj.soft.database.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.service.BusDataSourcePersistenceService;
import com.ygkj.soft.database.dao.BusDataSourcePersistenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/17 18:37
 * @Description:
 */
@Service("busDataSourcePersistenceService")
public class BusDataSourcePersistenceServiceImpl implements BusDataSourcePersistenceService {

    @Autowired
    private BusDataSourcePersistenceDao busDataSourcePersistenceDao;

    @Override
    public List<JSONObject> countPastDaysPersistenceRows(int pastDay) {
        return busDataSourcePersistenceDao.countPastDaysPersistenceRows(pastDay);
    }
}
