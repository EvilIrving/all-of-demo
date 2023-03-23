package com.dcy.datamanage.biz.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.DataAssessDeptRel;
import com.dcy.datamanage.biz.dto.input.DataAssessDeptSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.DataAssessDeptSearchOutputDTO;

import java.util.List;

public interface DataAssessDeptMapper extends BaseMapper<DataAssessDeptRel> {

    /**
     * 批量替换责任科室
     *
     * @param list
     */
    void batchReplace(List<DataAssessDeptRel> list);

    /**
     * 归属科室查询
     *
     * @param dto
     * @return
     */
    List<DataAssessDeptSearchOutputDTO> list(DataAssessDeptSearchInputDTO dto);
}
