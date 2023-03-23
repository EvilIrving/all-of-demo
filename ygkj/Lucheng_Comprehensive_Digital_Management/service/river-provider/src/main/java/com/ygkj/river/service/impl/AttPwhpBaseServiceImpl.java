package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttPwhpBaseMapper;
import com.ygkj.river.service.AttPwhpBaseService;
import com.ygkj.river.vo.request.AttPwhpBaseReqVo;
import com.ygkj.river.vo.response.AttPwhpBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-码头数据表
 * @date 2021-07-07
 */
@Service
public class AttPwhpBaseServiceImpl implements AttPwhpBaseService {

    @Resource
    private AttPwhpBaseMapper attPwhpBaseMapper;

    @Override
    public PageVO<AttPwhpBaseRespVo> loadPage(AttPwhpBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attPwhpBaseMapper.loadPage(vo));
    }
}
