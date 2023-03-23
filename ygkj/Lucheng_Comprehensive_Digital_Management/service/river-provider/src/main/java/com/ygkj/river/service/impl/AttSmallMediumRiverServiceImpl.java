package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttSmallMediumRiverMapper;
import com.ygkj.river.service.AttSmallMediumRiverService;
import com.ygkj.river.vo.request.AttSmallMediumRiverReqVo;
import com.ygkj.river.vo.response.AttSmallMediumRiverRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 中小河流表
 * @date 2021-08-12
 */
@Service
public class AttSmallMediumRiverServiceImpl implements AttSmallMediumRiverService {

    @Resource
    private AttSmallMediumRiverMapper attSmallMediumRiverMapper;

    @Override
    public PageVO<AttSmallMediumRiverRespVo> loadPage(AttSmallMediumRiverReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attSmallMediumRiverMapper.loadPage(vo));
    }
}
