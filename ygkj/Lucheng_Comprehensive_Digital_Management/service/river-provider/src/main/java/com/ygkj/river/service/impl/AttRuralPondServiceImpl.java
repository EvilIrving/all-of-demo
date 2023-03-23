package com.ygkj.river.service.impl;

import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttRuralPondMapper;
import com.ygkj.river.service.AttRuralPondService;
import com.ygkj.river.vo.request.AttRuralPondReqVo;
import com.ygkj.river.vo.response.AttRuralPondRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hucong
 * @description 农村池塘表
 * @date 2021-08-12
 */
@Service
public class AttRuralPondServiceImpl implements AttRuralPondService {

    @Resource
    private AttRuralPondMapper attRuralPondMapper;

    @Override
    public PageVO<AttRuralPondRespVo> loadPage(AttRuralPondReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attRuralPondMapper.loadPage(vo));
    }

    @Override
    public List<JSONObject> attSmallMediumRiverService() {
        return attRuralPondMapper.attSmallMediumRiverService();
    }
}
