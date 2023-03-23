package com.ygkj.digitization.service.impl;

import com.ygkj.digitization.service.HydroFeatureService;
import com.ygkj.digitization.mapper.HydroFeatureMapper;
import com.ygkj.digitization.model.AttHydroFeatureBase;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/7
 */
@Service
public class HydroFeatureServiceImpl implements HydroFeatureService {


    @Resource
    HydroFeatureMapper hydroFeatureMapper;

    /**
     * 查询水文特征表
     *
     * @param tableNameKey 表名关键字
     * @return
     */
    @Override
    public List<String> listFeatureTable(String tableNameKey) {
        return hydroFeatureMapper.listHydroFeatureTableName(tableNameKey);
    }

    /**
     * 查询水文特征值
     *
     * @param tableName 表明
     * @return
     */
    @Override
    public AttHydroFeatureBase loadFeatureValue(String tableName) {
        return hydroFeatureMapper.loadHydroFeatureTable(tableName);
    }
}
