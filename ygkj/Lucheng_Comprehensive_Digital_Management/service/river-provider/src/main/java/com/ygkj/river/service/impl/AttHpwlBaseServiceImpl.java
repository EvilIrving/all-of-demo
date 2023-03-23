package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttHpwlBaseMapper;
import com.ygkj.river.service.AttHpwlBaseService;
import com.ygkj.river.vo.request.AttHpwlBaseReqVo;
import com.ygkj.river.vo.response.AttHpwlBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-山塘水面线数据表
 * @date 2021-07-06
 */
@Service
public class AttHpwlBaseServiceImpl implements AttHpwlBaseService {

    @Resource
    private AttHpwlBaseMapper attHpwlBaseMapper;

    @Override
    public PageVO<AttHpwlBaseRespVo> loadPage(AttHpwlBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attHpwlBaseMapper.loadPage(vo));
    }
}
