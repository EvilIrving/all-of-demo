package com.ygkj.project.mapper;

import com.ygkj.project.api.vo.res.PolderRangeResVo;

import java.util.List;

public interface PolderMapper {

    /**
     * 带范围线的圩区列表
     *
     * @return
     */
    List<PolderRangeResVo> listRange(String adcd);
}
