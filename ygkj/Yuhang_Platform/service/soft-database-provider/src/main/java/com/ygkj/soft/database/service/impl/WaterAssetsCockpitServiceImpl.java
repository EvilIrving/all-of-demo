package com.ygkj.soft.database.service.impl;

import com.ygkj.soft.database.service.WaterAssetsCockpitService;
import com.ygkj.soft.database.dao.WaterAssetsCockpitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huang.zh
 * @date 2020/9/21 15:32
 * @Description: 水利资产监控大屏服务层具体实现
 */
@Service
public class WaterAssetsCockpitServiceImpl implements WaterAssetsCockpitService {

    @Autowired
    private WaterAssetsCockpitDao waterAssetsCockpitDao;

    @Override
    public String queryData() {
        return waterAssetsCockpitDao.queryData();
    }
}
