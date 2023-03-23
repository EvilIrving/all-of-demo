package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRsaaBaseMapper;
import com.ygkj.river.service.AttRsaaBaseService;
import com.ygkj.river.vo.request.AttRsaaBaseReqVo;
import com.ygkj.river.vo.response.AttRsaaBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-水库水域面数据表
 * @date 2021-07-06
 */
@Service
public class AttRsaaBaseServiceImpl implements AttRsaaBaseService {

    @Resource
    private AttRsaaBaseMapper attRsaaBaseMapper;

    @Override
    public PageVO<AttRsaaBaseRespVo> loadPage(AttRsaaBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRsaaBaseMapper.loadPage(vo));
    }
}
