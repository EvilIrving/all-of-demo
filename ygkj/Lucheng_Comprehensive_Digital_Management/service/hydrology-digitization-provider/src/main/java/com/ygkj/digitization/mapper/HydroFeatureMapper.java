package com.ygkj.digitization.mapper;

import com.ygkj.digitization.model.AttFeatureHeaderBase;
import com.ygkj.digitization.model.AttFeatureValueBase;
import com.ygkj.digitization.model.AttHydroFeatureBase;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/7
 */
public interface HydroFeatureMapper {

    List<String> listHydroFeatureTableName(String tableNameKey);

    AttHydroFeatureBase loadHydroFeatureTable(String tableName);
}
