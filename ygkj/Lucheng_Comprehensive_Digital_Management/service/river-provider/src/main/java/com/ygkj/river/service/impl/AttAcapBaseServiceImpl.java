package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttAcapBaseMapper;
import com.ygkj.river.service.AttAcapBaseService;
import com.ygkj.river.vo.request.AttAcapBaseReqVo;
import com.ygkj.river.vo.response.AttAcapBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 水域调查数据-人工水道临水线点数据表
 * @date 2021-07-06
 */
@Service
public class AttAcapBaseServiceImpl implements AttAcapBaseService {

    @Resource
    private AttAcapBaseMapper attAcapBaseMapper;

    @Override
    public PageVO<AttAcapBaseRespVo> loadPage(AttAcapBaseReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attAcapBaseMapper.loadPage(vo));
    }
}
