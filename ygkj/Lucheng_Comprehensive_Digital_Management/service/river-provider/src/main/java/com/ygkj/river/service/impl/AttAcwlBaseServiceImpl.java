package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttAcwlBaseMapper;
import com.ygkj.river.service.AttAcwlBaseService;
import com.ygkj.river.vo.request.AttAcwlBaseReqVo;
import com.ygkj.river.vo.response.AttAcwlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道水面线数据表
 * @date 2021-07-06
 */
@Service
public class AttAcwlBaseServiceImpl implements AttAcwlBaseService {

    @Resource
    private AttAcwlBaseMapper attAcwlBaseMapper;

    @Override
    public PageVO<AttAcwlBaseRespVo> loadPage(AttAcwlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attAcwlBaseMapper.loadPage(vo));
    }
}
