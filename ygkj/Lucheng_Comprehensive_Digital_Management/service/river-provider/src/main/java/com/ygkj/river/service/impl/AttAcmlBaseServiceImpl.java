package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttAcmlBaseMapper;
import com.ygkj.river.service.AttAcmlBaseService;
import com.ygkj.river.vo.request.AttAcmlBaseReqVo;
import com.ygkj.river.vo.response.AttAcmlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道管理范围线数据表
 * @date 2021-07-06
 */
@Service
public class AttAcmlBaseServiceImpl implements AttAcmlBaseService {

    @Resource
    private AttAcmlBaseMapper attAcmlBaseMapper;


    @Override
    public PageVO<AttAcmlBaseRespVo> loadPage(AttAcmlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attAcmlBaseMapper.loadPage(vo));
    }
}
