package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttAcapBaseReqVo;
import com.ygkj.river.vo.response.AttAcapBaseRespVo;

import java.util.List;

public interface AttAcapBaseMapper {

    List<AttAcapBaseRespVo> loadPage(AttAcapBaseReqVo vo);

}
