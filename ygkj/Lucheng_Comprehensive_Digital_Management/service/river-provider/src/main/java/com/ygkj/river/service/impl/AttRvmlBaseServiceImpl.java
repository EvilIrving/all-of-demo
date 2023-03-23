package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRvmlBaseMapper;
import com.ygkj.river.service.AttRvmlBaseService;
import com.ygkj.river.vo.request.AttRvmlBaseReqVo;
import com.ygkj.river.vo.response.AttRvmlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道管理范围线数据表
 * @date 2021-07-06
 */
@Service
public class AttRvmlBaseServiceImpl implements AttRvmlBaseService {

    @Resource
    private AttRvmlBaseMapper attRvmlBaseMapper;


    @Override
    public PageVO<AttRvmlBaseRespVo> loadPage(AttRvmlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRvmlBaseMapper.loadPage(vo));
    }
}
