package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRswlBaseMapper;
import com.ygkj.river.service.AttRswlBaseService;
import com.ygkj.river.vo.request.AttRswlBaseReqVo;
import com.ygkj.river.vo.response.AttRswlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水库水面线数据表
 * @date 2021-07-06
 */
@Service
public class AttRswlBaseServiceImpl implements AttRswlBaseService {

    @Resource
    private AttRswlBaseMapper attRswlBaseMapper;

    @Override
    public PageVO<AttRswlBaseRespVo> loadPage(AttRswlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRswlBaseMapper.loadPage(vo));
    }
}
