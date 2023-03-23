package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttRvalBaseReqVo;
import com.ygkj.river.vo.response.AttRvalBaseRespVo;

import java.util.List;

/**
 * @author hucong
 * @description 水域调查数据-河道临水线数据表
 * @date 2021-07-06
 */
public interface AttRvalBaseMapper {


    List<AttRvalBaseRespVo> loadPage(AttRvalBaseReqVo vo);
}
