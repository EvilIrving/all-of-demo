package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttOwalBaseReqVo;
import com.ygkj.river.vo.response.AttOwalBaseRespVo;

import java.util.List;

public interface AttOwalBaseMapper {
    List<AttOwalBaseRespVo> loadPage(AttOwalBaseReqVo vo);
}
