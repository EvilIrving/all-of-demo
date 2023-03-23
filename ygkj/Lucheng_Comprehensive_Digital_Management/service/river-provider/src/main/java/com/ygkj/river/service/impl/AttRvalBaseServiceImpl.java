package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRvalBaseMapper;
import com.ygkj.river.service.AttRvalBaseService;
import com.ygkj.river.vo.request.AttRvalBaseReqVo;
import com.ygkj.river.vo.response.AttRvalBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道临水线数据表
 * @date 2021-07-06
 */
@Service
public class AttRvalBaseServiceImpl implements AttRvalBaseService {

    @Resource
    private AttRvalBaseMapper attRvalBaseMapper;


    @Override
    public PageVO<AttRvalBaseRespVo> loadPage(AttRvalBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRvalBaseMapper.loadPage(vo));
    }

}
