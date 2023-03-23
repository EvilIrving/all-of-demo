package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.BusDataDeptRel;
import com.dcy.datamanage.biz.dto.input.ColValInputDTO;
import com.dcy.datamanage.biz.dto.input.DataEditInputDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataDeptRelMapper extends BaseMapper<BusDataDeptRel> {

    /**
     * 数据科室关联关系列表
     *
     * @param dto
     * @param list
     * @return
     */
    List<BusDataDeptRel> listDept(@Param("dto") DataEditInputDTO dto, @Param("list") List<List<ColValInputDTO>> list);
}
