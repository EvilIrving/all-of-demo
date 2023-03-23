package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttAcaaBaseMapper;
import com.ygkj.river.service.AttAcaaBaseService;
import com.ygkj.river.vo.request.AttAcaaBaseReqVo;
import com.ygkj.river.vo.response.AttAcaaBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道水域面数据表
 * @date 2021-07-06
 */
@Service
public class AttAcaaBaseServiceImpl implements AttAcaaBaseService {

    @Resource
    private AttAcaaBaseMapper attAcaaBaseMapper;


    @Override
    public PageVO<AttAcaaBaseRespVo> loadPage(AttAcaaBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attAcaaBaseMapper.loadPage(vo));
    }
}
