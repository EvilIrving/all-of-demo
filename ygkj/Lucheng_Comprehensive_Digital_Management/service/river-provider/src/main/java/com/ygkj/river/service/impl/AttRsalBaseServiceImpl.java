package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRsalBaseMapper;
import com.ygkj.river.service.AttRsalBaseService;
import com.ygkj.river.vo.request.AttRsalBaseReqVo;
import com.ygkj.river.vo.response.AttRsalBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水库临水线数据表
 * @date 2021-07-06
 */
@Service
public class AttRsalBaseServiceImpl implements AttRsalBaseService {

    @Resource
    private AttRsalBaseMapper attRsalBaseMapper;

    @Override
    public PageVO<AttRsalBaseRespVo> loadPage(AttRsalBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRsalBaseMapper.loadPage(vo));
    }
}
