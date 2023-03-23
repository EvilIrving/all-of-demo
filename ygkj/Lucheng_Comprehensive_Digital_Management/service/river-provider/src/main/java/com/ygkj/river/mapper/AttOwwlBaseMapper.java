package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttOwwlBaseReqVo;
import com.ygkj.river.vo.response.AttOwwlBaseRespVo;

import java.util.List;

public interface AttOwwlBaseMapper {
    List<AttOwwlBaseRespVo> loadPage(AttOwwlBaseReqVo vo);
}
