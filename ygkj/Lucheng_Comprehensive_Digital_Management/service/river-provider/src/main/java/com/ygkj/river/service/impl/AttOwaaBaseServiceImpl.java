package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttOwaaBaseMapper;
import com.ygkj.river.service.AttOwaaBaseService;
import com.ygkj.river.vo.request.AttOwaaBaseReqVo;
import com.ygkj.river.vo.response.AttOwaaBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-其他水域水域面数据表
 * @date 2021-07-06
 */
@Service
public class AttOwaaBaseServiceImpl implements AttOwaaBaseService {

    @Resource
    private AttOwaaBaseMapper attOwaaBaseMapper;

    @Override
    public PageVO<AttOwaaBaseRespVo> loadPage(AttOwaaBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attOwaaBaseMapper.loadPage(vo));
    }
}
