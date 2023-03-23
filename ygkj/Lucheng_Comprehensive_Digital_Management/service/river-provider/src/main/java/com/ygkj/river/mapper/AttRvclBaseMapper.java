package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRvclBaseReqVo;
import com.ygkj.river.vo.response.AttRvclBaseRespVo;

import java.util.List;

public interface AttRvclBaseMapper {
    List<AttRvclBaseRespVo> loadPage(AttRvclBaseReqVo vo);
}
