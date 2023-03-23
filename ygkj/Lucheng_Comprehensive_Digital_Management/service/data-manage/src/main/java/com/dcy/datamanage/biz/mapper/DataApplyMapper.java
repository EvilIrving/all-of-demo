package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.BusDataApply;
import com.dcy.datamanage.biz.dto.input.ColValInputDTO;
import com.dcy.datamanage.biz.dto.input.DataEditInputDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DataApplyMapper extends BaseMapper<BusDataApply> {

    /**
     * 该条数据待处理的数量
     *
     * @param dto
     * @param colValList
     * @return
     */
    int countPending(@Param("dto") DataEditInputDTO dto,@Param("colValList") List<ColValInputDTO> colValList);
}
