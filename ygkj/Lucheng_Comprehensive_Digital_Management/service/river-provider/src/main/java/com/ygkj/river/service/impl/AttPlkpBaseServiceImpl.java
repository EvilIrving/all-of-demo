package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttPlkpBaseMapper;
import com.ygkj.river.service.AttPlkpBaseService;
import com.ygkj.river.vo.request.AttPlkpBaseReqVo;
import com.ygkj.river.vo.response.AttPlkpBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-船闸数据表
 * @date 2021-07-07
 */
@Service
public class AttPlkpBaseServiceImpl implements AttPlkpBaseService {

    @Resource
    private AttPlkpBaseMapper attPlkpBaseMapper;

    @Override
    public PageVO<AttPlkpBaseRespVo> loadPage(AttPlkpBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attPlkpBaseMapper.loadPage(vo));
    }
}
