package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttOwapBaseMapper;
import com.ygkj.river.service.AttOwapBaseService;
import com.ygkj.river.vo.request.AttOwapBaseReqVo;
import com.ygkj.river.vo.response.AttOwapBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他水域临水线点数据表
 * @date 2021-07-06
 */
@Service
public class AttOwapBaseServiceImpl implements AttOwapBaseService {

    @Resource
    private AttOwapBaseMapper attOwapBaseMapper;


    @Override
    public PageVO<AttOwapBaseRespVo> loadPage(AttOwapBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attOwapBaseMapper.loadPage(vo));
    }
}
