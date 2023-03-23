package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRvwlBaseMapper;
import com.ygkj.river.service.AttRvwlBaseService;
import com.ygkj.river.vo.request.AttRvwlBaseReqVo;
import com.ygkj.river.vo.response.AttRvwlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道水面线数据表
 * @date 2021-07-06
 */
@Service
public class AttRvwlBaseServiceImpl implements AttRvwlBaseService {

    @Resource
    private AttRvwlBaseMapper attRvwlBaseMapper;


    @Override
    public PageVO<AttRvwlBaseRespVo> loadPage(AttRvwlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRvwlBaseMapper.loadPage(vo));
    }
}
