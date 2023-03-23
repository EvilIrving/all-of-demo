package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttHpaaBaseReqVo;
import com.ygkj.river.vo.response.AttHpaaBaseRespVo;

import java.util.List;

public interface AttHpaaBaseMapper {
    List<AttHpaaBaseRespVo> loadPage(AttHpaaBaseReqVo vo);
}
