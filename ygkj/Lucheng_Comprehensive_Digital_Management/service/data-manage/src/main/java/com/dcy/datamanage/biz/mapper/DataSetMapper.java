package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.BusDataSet;
import com.dcy.datamanage.biz.dto.input.BusDataSetSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSetOutputDTO;

import java.util.List;

public interface DataSetMapper extends BaseMapper<BusDataSet> {

    /**
     * 数据集列表查询
     *
     * @param dto
     * @return
     */
    List<BusDataSetOutputDTO> list(BusDataSetSearchInputDTO dto);
}
