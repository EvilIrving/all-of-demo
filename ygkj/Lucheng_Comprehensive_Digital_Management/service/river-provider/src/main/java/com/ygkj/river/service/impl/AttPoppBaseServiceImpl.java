package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttPoppBaseMapper;
import com.ygkj.river.service.AttPoppBaseService;
import com.ygkj.river.vo.request.AttPoppBaseReqVo;
import com.ygkj.river.vo.response.AttPoppBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他工程数据表
 * @date 2021-07-07
 */
@Service
public class AttPoppBaseServiceImpl implements AttPoppBaseService {

    @Resource
    private AttPoppBaseMapper attPoppBaseMapper;

    @Override
    public PageVO<AttPoppBaseRespVo> loadPage(AttPoppBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attPoppBaseMapper.loadPage(vo));
    }
}
