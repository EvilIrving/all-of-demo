package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttHpalBaseMapper;
import com.ygkj.river.service.AttHpalBaseService;
import com.ygkj.river.vo.request.AttHpalBaseReqVo;
import com.ygkj.river.vo.response.AttHpalBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-山塘临水线数据表
 * @date 2021-07-06
 */
@Service
public class AttHpalBaseServiceImpl implements AttHpalBaseService {

    @Resource
    private AttHpalBaseMapper attHpalBaseMapper;

    @Override
    public PageVO<AttHpalBaseRespVo> loadPage(AttHpalBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attHpalBaseMapper.loadPage(vo));
    }
}
