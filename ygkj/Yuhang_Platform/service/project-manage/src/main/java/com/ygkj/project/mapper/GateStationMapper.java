package com.ygkj.project.mapper;

import com.ygkj.project.api.vo.res.RelGatestationResVo;

public interface GateStationMapper {


    RelGatestationResVo selectByPrcd(String prcd);
}
