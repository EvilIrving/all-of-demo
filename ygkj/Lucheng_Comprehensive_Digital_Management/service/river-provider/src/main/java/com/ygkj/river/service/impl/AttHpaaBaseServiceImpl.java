package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttHpaaBaseMapper;
import com.ygkj.river.service.AttHpaaBaseService;
import com.ygkj.river.vo.request.AttHpaaBaseReqVo;
import com.ygkj.river.vo.response.AttHpaaBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-山塘水域面数据表
 * @date 2021-07-06
 */
@Service
public class AttHpaaBaseServiceImpl implements AttHpaaBaseService {

    @Resource
    private AttHpaaBaseMapper attHpaaBaseMapper;


    @Override
    public PageVO<AttHpaaBaseRespVo> loadPage(AttHpaaBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attHpaaBaseMapper.loadPage(vo));
    }
}
