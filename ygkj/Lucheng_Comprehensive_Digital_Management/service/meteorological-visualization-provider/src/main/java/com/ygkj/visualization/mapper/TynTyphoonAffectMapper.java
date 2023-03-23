package com.ygkj.visualization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.visualization.model.TynTyphoonAffect;
import com.ygkj.visualization.vo.request.TyphoonAffectReqVo;

import java.util.List;

public interface TynTyphoonAffectMapper {

    @DS("business")
    List<TynTyphoonAffect> getTyphoonAffects(TyphoonAffectReqVo vo);
}
