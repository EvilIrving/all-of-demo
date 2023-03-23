package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttPbrlBaseMapper;
import com.ygkj.river.service.AttPbrlBaseService;
import com.ygkj.river.vo.request.AttPbrlBaseReqVo;
import com.ygkj.river.vo.response.AttPbrlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-桥梁数据表
 * @date 2021-07-07
 */
@Service
public class AttPbrlBaseServiceImpl implements AttPbrlBaseService {

    @Resource
    private AttPbrlBaseMapper attPbrlBaseMapper;

    @Override
    public PageVO<AttPbrlBaseRespVo> loadPage(AttPbrlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attPbrlBaseMapper.loadPage(vo));
    }
}
