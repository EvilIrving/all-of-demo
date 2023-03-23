package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRvulBaseMapper;
import com.ygkj.river.service.AttRvulBaseService;
import com.ygkj.river.vo.request.AttRvulBaseReqVo;
import com.ygkj.river.vo.response.AttRvulBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-暗河数据表
 * @date 2021-07-06
 */
@Service
public class AttRvulBaseServiceImpl implements AttRvulBaseService {

    @Resource
    private AttRvulBaseMapper attRvulBaseMapper;


    @Override
    public PageVO<AttRvulBaseRespVo> loadPage(AttRvulBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRvulBaseMapper.loadPage(vo));
    }
}
