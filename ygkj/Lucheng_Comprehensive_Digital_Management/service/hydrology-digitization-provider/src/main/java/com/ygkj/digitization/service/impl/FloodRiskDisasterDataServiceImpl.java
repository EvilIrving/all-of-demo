package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.mapper.FloodRiskDisasterDataMapper;
import com.ygkj.digitization.service.FloodRiskDisasterDataService;
import com.ygkj.digitization.vo.request.FloodRiskDisasterDataReqVo;
import com.ygkj.digitization.vo.response.FloodRiskDisasterDataRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description 洪水风险图灾情数据表
 * @author hucong
 * @date 2021-07-21
 */
@Service
public class FloodRiskDisasterDataServiceImpl implements FloodRiskDisasterDataService {

    @Resource
    private FloodRiskDisasterDataMapper floodRiskDisasterDataMapper;


    @Override
    public PageVO<FloodRiskDisasterDataRespVo> loadPage(FloodRiskDisasterDataReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(floodRiskDisasterDataMapper.loadPage(vo));
    }
}
