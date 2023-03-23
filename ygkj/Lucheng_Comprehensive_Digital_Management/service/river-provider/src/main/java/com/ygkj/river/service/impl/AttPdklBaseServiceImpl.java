package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttPdklBaseMapper;
import com.ygkj.river.service.AttPdklBaseService;
import com.ygkj.river.vo.request.AttPdklBaseReqVo;
import com.ygkj.river.vo.response.AttPdklBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-堤防数据表
 * @date 2021-07-07
 */
@Service
public class AttPdklBaseServiceImpl implements AttPdklBaseService {

    @Resource
    private AttPdklBaseMapper attPdklBaseMapper;

    @Override
    public PageVO<AttPdklBaseRespVo> loadPage(AttPdklBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attPdklBaseMapper.loadPage(vo));
    }
}
