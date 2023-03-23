package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRvclBaseMapper;
import com.ygkj.river.service.AttRvclBaseService;
import com.ygkj.river.vo.request.AttRvclBaseReqVo;
import com.ygkj.river.vo.response.AttRvclBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-河道中心线数据表
 * @date 2021-07-06
 */
@Service
public class AttRvclBaseServiceImpl implements AttRvclBaseService {

    @Resource
    private AttRvclBaseMapper attRvclBaseMapper;


    @Override
    public PageVO<AttRvclBaseRespVo> loadPage(AttRvclBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRvclBaseMapper.loadPage(vo));
    }
}
