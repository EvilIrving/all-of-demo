package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttPslpBaseMapper;
import com.ygkj.river.service.AttPslpBaseService;
import com.ygkj.river.vo.request.AttPslpBaseReqVo;
import com.ygkj.river.vo.response.AttPslpBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水闸数据表
 * @date 2021-07-07
 */
@Service
public class AttPslpBaseServiceImpl implements AttPslpBaseService {

    @Resource
    private AttPslpBaseMapper attPslpBaseMapper;

    @Override
    public PageVO<AttPslpBaseRespVo> loadPage(AttPslpBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attPslpBaseMapper.loadPage(vo));
    }
}
