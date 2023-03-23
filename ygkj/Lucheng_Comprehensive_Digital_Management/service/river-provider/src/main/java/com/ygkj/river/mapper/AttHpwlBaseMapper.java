package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttHpwlBaseReqVo;
import com.ygkj.river.vo.response.AttHpwlBaseRespVo;

import java.util.List;

public interface AttHpwlBaseMapper {
    List<AttHpwlBaseRespVo> loadPage(AttHpwlBaseReqVo vo);
}
