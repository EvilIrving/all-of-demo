package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttOwmlBaseMapper;
import com.ygkj.river.service.AttOwmlBaseService;
import com.ygkj.river.vo.request.AttOwmlBaseReqVo;
import com.ygkj.river.vo.response.AttOwmlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他水域管理范围线数据表
 * @date 2021-07-06
 */
@Service
public class AttOwmlBaseServiceImpl implements AttOwmlBaseService {

    @Resource
    private AttOwmlBaseMapper attOwmlBaseMapper;

    @Override
    public PageVO<AttOwmlBaseRespVo> loadPage(AttOwmlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attOwmlBaseMapper.loadPage(vo));
    }
}
