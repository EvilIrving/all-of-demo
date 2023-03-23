package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRsapBaseMapper;
import com.ygkj.river.service.AttRsapBaseService;
import com.ygkj.river.vo.request.AttRsapBaseReqVo;
import com.ygkj.river.vo.response.AttRsapBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水库临水线点数据表
 * @date 2021-07-06
 */
@Service
public class AttRsapBaseServiceImpl implements AttRsapBaseService {

    @Resource
    private AttRsapBaseMapper attRsapBaseMapper;

    @Override
    public PageVO<AttRsapBaseRespVo> loadPage(AttRsapBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRsapBaseMapper.loadPage(vo));
    }
}
