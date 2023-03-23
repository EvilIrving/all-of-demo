package com.ygkj.project.mapper;

import com.ygkj.project.api.vo.res.RelSluiceResVo;

public interface SluiceMapper {


    RelSluiceResVo selectByPrcd(String prcd);
}
