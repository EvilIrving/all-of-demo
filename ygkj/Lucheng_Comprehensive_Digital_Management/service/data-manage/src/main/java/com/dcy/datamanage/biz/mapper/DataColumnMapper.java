package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.BusDataColumn;
import com.dcy.datamanage.biz.dto.input.BusDataColumnSearchInputDTO;

import java.util.List;


public interface DataColumnMapper extends BaseMapper<BusDataColumn> {

    /**
     * 列别名查询
     *
     * @param dto
     * @return
     */
    List<BusDataColumn> listColumnExtend(BusDataColumnSearchInputDTO dto);

    /**
     * 批量保存新的
     *
     * @param list
     */
    void replaceAll(List<BusDataColumn> list);
}
