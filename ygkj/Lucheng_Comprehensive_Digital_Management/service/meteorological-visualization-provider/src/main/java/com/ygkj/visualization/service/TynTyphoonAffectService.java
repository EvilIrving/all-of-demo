package com.ygkj.visualization.service;

import com.ygkj.visualization.model.TynTyphoonAffect;
import com.ygkj.visualization.vo.request.TyphoonAffectReqVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;

public interface TynTyphoonAffectService {

    CommonResult<PageVO<TynTyphoonAffect>> typhoonAffects(TyphoonAffectReqVo vo);
}
