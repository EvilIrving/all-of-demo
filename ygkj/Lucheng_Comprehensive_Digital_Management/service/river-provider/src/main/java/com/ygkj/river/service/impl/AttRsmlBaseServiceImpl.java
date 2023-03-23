package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRsmlBaseMapper;
import com.ygkj.river.service.AttRsmlBaseService;
import com.ygkj.river.vo.request.AttRsmlBaseReqVo;
import com.ygkj.river.vo.response.AttRsmlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水库管理范围线数据表
 * @date 2021-07-06
 */
@Service
public class AttRsmlBaseServiceImpl implements AttRsmlBaseService {

    @Resource
    private AttRsmlBaseMapper attRsmlBaseMapper;

    @Override
    public PageVO<AttRsmlBaseRespVo> loadPage(AttRsmlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRsmlBaseMapper.loadPage(vo));
    }
}
