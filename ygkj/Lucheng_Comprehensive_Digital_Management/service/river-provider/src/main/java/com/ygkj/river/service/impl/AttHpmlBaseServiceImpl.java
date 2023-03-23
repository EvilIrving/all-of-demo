package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttHpmlBaseMapper;
import com.ygkj.river.service.AttHpmlBaseService;
import com.ygkj.river.vo.request.AttHpmlBaseReqVo;
import com.ygkj.river.vo.response.AttHpmlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-山塘管理范围线数据表
 * @date 2021-07-06
 */
@Service
public class AttHpmlBaseServiceImpl implements AttHpmlBaseService {

    @Resource
    private AttHpmlBaseMapper attHpmlBaseMapper;

    @Override
    public PageVO<AttHpmlBaseRespVo> loadPage(AttHpmlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attHpmlBaseMapper.loadPage(vo));
    }
}
