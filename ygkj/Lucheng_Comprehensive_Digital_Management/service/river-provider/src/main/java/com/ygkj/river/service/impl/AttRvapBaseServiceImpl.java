package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRvapBaseMapper;
import com.ygkj.river.service.AttRvapBaseService;
import com.ygkj.river.vo.request.AttRvapBaseReqVo;
import com.ygkj.river.vo.response.AttRvapBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道临水线点数据表
 * @date 2021-07-06
 */
@Service
public class AttRvapBaseServiceImpl implements AttRvapBaseService {

    @Resource
    private AttRvapBaseMapper attRvapBaseMapper;

    @Override
    public PageVO<AttRvapBaseRespVo> loadPage(AttRvapBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRvapBaseMapper.loadPage(vo));
    }
}