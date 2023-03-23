package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.BusDataSource;
import com.dcy.datamanage.biz.dto.input.BusDataSourceSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSourceOutputDTO;

import java.util.List;

public interface DataSourceMapper extends BaseMapper<BusDataSource> {

    /**
     * 数据源列表查询
     *
     * @param dto
     * @return
     */
    List<BusDataSourceOutputDTO> list(BusDataSourceSearchInputDTO dto);
}
