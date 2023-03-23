package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRvaaBaseMapper;
import com.ygkj.river.service.AttRvaaBaseService;
import com.ygkj.river.vo.request.AttRvaaBaseReqVo;
import com.ygkj.river.vo.response.AttRvaaBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道水域面数据表
 * @date 2021-07-06
 */
@Service
public class AttRvaaBaseServiceImpl implements AttRvaaBaseService {

    @Resource
    private AttRvaaBaseMapper attRvaaBaseMapper;

    @Override
    public PageVO<AttRvaaBaseRespVo> loadPage(AttRvaaBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRvaaBaseMapper.loadPage(vo));
    }
}
