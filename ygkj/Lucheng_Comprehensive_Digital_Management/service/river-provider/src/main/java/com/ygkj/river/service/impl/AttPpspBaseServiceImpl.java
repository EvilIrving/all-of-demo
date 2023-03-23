package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttPpspBaseMapper;
import com.ygkj.river.service.AttPpspBaseService;
import com.ygkj.river.vo.request.AttPpspBaseReqVo;
import com.ygkj.river.vo.response.AttPpspBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-泵站数据表
 * @date 2021-07-07
 */
@Service
public class AttPpspBaseServiceImpl implements AttPpspBaseService {

    @Resource
    private AttPpspBaseMapper attPpspBaseMapper;

    @Override
    public PageVO<AttPpspBaseRespVo> loadPage(AttPpspBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attPpspBaseMapper.loadPage(vo));
    }
}
