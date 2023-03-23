package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttAcclBaseMapper;
import com.ygkj.river.service.AttAcclBaseService;
import com.ygkj.river.vo.request.AttAcclBaseReqVo;
import com.ygkj.river.vo.response.AttAcclBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道中心线数据表
 * @date 2021-07-06
 */
@Service
public class AttAcclBaseServiceImpl implements AttAcclBaseService {

    @Resource
    private AttAcclBaseMapper attAcclBaseMapper;

    @Override
    public PageVO<AttAcclBaseRespVo> loadPage(AttAcclBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attAcclBaseMapper.loadPage(vo));
    }
}
