package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRvapBaseReqVo;
import com.ygkj.river.vo.response.AttRvapBaseRespVo;

import java.util.List;

/**
 * @author hucong
 * @description 水域调查数据-河道临水线点数据表
 * @date 2021-07-06
 */
public interface AttRvapBaseMapper {
    List<AttRvapBaseRespVo> loadPage(AttRvapBaseReqVo vo);
}
