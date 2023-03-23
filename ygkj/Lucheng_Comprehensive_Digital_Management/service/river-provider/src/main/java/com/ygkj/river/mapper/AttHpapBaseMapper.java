package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttHpapBaseReqVo;
import com.ygkj.river.vo.response.AttHpapBaseRespVo;

import java.util.List;

public interface AttHpapBaseMapper {
    List<AttHpapBaseRespVo> loadPage(AttHpapBaseReqVo vo);
}
