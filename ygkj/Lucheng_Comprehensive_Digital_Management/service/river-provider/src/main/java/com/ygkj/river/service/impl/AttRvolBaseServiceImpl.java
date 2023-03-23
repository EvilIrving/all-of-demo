package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRvolBaseMapper;
import com.ygkj.river.service.AttRvolBaseService;
import com.ygkj.river.vo.request.AttRvolBaseReqVo;
import com.ygkj.river.vo.response.AttRvolBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他沟渠数据表
 * @date 2021-07-06
 */
@Service
public class AttRvolBaseServiceImpl implements AttRvolBaseService {

    @Resource
    private AttRvolBaseMapper attRvolBaseMapper;


    @Override
    public PageVO<AttRvolBaseRespVo> loadPage(AttRvolBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRvolBaseMapper.loadPage(vo));
    }
}
