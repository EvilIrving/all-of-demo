package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.BusDataSetSql;
import com.dcy.datamanage.biz.dto.output.BusDataSetSqlOutputDTO;

public interface DataSetSqlMapper extends BaseMapper<BusDataSetSql> {

    /**
     * 根据数据集id查询详情
     *
     * @param dataSetId
     * @return
     */
    BusDataSetSqlOutputDTO selectByDataSetId(String dataSetId);
}
