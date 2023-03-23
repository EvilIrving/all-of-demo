package com.ygkj.digitization.service;

import com.ygkj.digitization.model.AttHydroFeatureBase;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/7
 */
public interface HydroFeatureService {

    /**
     * 查询水文特征表
     * @param tableNameKey 表名关键字
     * @return java.util.List
     */
    List<String> listFeatureTable(String tableNameKey);

    /**
     * 查询水文特征值
     * @param tableName 表明
     * @return com.ygkj.digitization.model.AttHydroFeatureBase
     */
    AttHydroFeatureBase loadFeatureValue(String tableName);
}
