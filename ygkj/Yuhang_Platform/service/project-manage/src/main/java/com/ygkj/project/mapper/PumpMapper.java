package com.ygkj.project.mapper;

import com.ygkj.project.api.vo.res.RelPumpResVo;

public interface PumpMapper {

    RelPumpResVo selectByPrcd(String prcd);
}
