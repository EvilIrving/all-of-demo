package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttAcwlBaseReqVo;
import com.ygkj.river.vo.response.AttAcwlBaseRespVo;

import java.util.List;

public interface AttAcwlBaseMapper {
    List<AttAcwlBaseRespVo> loadPage(AttAcwlBaseReqVo vo);
}
