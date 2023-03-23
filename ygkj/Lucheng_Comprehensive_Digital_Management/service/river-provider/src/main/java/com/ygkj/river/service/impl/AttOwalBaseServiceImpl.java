package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttOwalBaseMapper;
import com.ygkj.river.service.AttOwalBaseService;
import com.ygkj.river.vo.request.AttOwalBaseReqVo;
import com.ygkj.river.vo.response.AttOwalBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他水域临水线数据表
 * @date 2021-07-06
 */
@Service
public class AttOwalBaseServiceImpl implements AttOwalBaseService {

    @Resource
    private AttOwalBaseMapper attOwalBaseMapper;

    @Override
    public PageVO<AttOwalBaseRespVo> loadPage(AttOwalBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attOwalBaseMapper.loadPage(vo));
    }
}
