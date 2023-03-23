package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttAcalBaseMapper;
import com.ygkj.river.service.AttAcalBaseService;
import com.ygkj.river.vo.request.AttAcalBaseReqVo;
import com.ygkj.river.vo.response.AttAcalBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道临水线数据表
 * @date 2021-07-06
 */
@Service
public class AttAcalBaseServiceImpl implements AttAcalBaseService {

    @Resource
    private AttAcalBaseMapper attAcalBaseMapper;


    @Override
    public PageVO<AttAcalBaseRespVo> loadPage(AttAcalBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attAcalBaseMapper.loadPage(vo));
    }
}
