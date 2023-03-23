package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttHpapBaseMapper;
import com.ygkj.river.service.AttHpapBaseService;
import com.ygkj.river.vo.request.AttHpapBaseReqVo;
import com.ygkj.river.vo.response.AttHpapBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-山塘临水线点数据表
 * @date 2021-07-06
 */
@Service
public class AttHpapBaseServiceImpl implements AttHpapBaseService {

    @Resource
    private AttHpapBaseMapper attHpapBaseMapper;


    @Override
    public PageVO<AttHpapBaseRespVo> loadPage(AttHpapBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attHpapBaseMapper.loadPage(vo));
    }
}
