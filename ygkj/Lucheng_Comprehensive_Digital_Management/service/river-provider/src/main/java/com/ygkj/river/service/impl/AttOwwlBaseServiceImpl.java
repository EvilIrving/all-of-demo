package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttOwwlBaseMapper;
import com.ygkj.river.service.AttOwwlBaseService;
import com.ygkj.river.vo.request.AttOwwlBaseReqVo;
import com.ygkj.river.vo.response.AttOwwlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他水域水面线数据表
 * @date 2021-07-06
 */
@Service
public class AttOwwlBaseServiceImpl implements AttOwwlBaseService {

    @Resource
    private AttOwwlBaseMapper attOwwlBaseMapper;


    @Override
    public PageVO<AttOwwlBaseRespVo> loadPage(AttOwwlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attOwwlBaseMapper.loadPage(vo));
    }
}
