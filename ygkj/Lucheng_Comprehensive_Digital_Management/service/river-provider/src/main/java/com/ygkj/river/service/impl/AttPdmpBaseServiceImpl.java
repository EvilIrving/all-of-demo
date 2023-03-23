package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttPdmpBaseMapper;
import com.ygkj.river.service.AttPdmpBaseService;
import com.ygkj.river.vo.request.AttPdmpBaseReqVo;
import com.ygkj.river.vo.response.AttPdmpBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-拦水坝（堰）数据表
 * @date 2021-07-07
 */
@Service
public class AttPdmpBaseServiceImpl implements AttPdmpBaseService {

    @Resource
    private AttPdmpBaseMapper attPdmpBaseMapper;

    @Override
    public PageVO<AttPdmpBaseRespVo> loadPage(AttPdmpBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attPdmpBaseMapper.loadPage(vo));
    }
}
