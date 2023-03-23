package com.ygkj.river.service;

import com.ygkj.river.vo.request.AttRvalBaseReqVo;
import com.ygkj.river.vo.response.AttRvalBaseRespVo;
import com.ygkj.vo.PageVO;

/**
 * @author hucong
 * @description 水域调查数据-河道临水线数据表
 * @date 2021-07-06
 */
public interface AttRvalBaseService {
    /**
     * 分页查找
     *
     * @param vo
     * @return
     */
    PageVO<AttRvalBaseRespVo> loadPage(AttRvalBaseReqVo vo);
}
