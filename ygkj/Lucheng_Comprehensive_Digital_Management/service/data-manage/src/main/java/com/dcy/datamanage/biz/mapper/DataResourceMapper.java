package com.dcy.datamanage.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dcy.datamanage.api.model.BusDataResource;
import com.dcy.datamanage.biz.dto.input.BusDataResourceSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataResourceOutputDTO;

import java.util.List;

public interface DataResourceMapper extends BaseMapper<BusDataResource> {

    /**
     * 资源目录查询
     *
     * @param dto
     * @return
     */
    List<BusDataResourceOutputDTO> list(BusDataResourceSearchInputDTO dto);
}
