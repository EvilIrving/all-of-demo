package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.visualization.model.StMeWarnR;

import java.util.List;

/**
 * 实时数据辅助mapper
 */
@DS("business")
public interface BusinessMapper {

    int batchInsertMeteorologicalWarningData(List<StMeWarnR> list);
}
