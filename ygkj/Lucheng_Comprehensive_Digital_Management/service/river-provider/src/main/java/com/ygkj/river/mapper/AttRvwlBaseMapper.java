package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRvwlBaseReqVo;
import com.ygkj.river.vo.response.AttRvwlBaseRespVo;

import java.util.List;

public interface AttRvwlBaseMapper {

    List<AttRvwlBaseRespVo> loadPage(AttRvwlBaseReqVo vo);

}
