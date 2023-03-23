package com.ygkj.river.service.impl;

import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttBeautVillageMapper;
import com.ygkj.river.service.AttBeautVillageService;
import com.ygkj.river.vo.request.AttBeautVillageReqVo;
import com.ygkj.river.vo.response.AttBeautVillageRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hucong
 * @description 水美乡镇表
 * @date 2021-08-12
 */
@Service
public class AttBeautVillageServiceImpl implements AttBeautVillageService {

    @Resource
    private AttBeautVillageMapper attBeautVillageMapper;

    @Override
    public PageVO<AttBeautVillageRespVo> loadPage(AttBeautVillageReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(attBeautVillageMapper.loadPage(vo));
    }

    @Override
    public List<JSONObject> attSmallMediumRiverService() {
        return attBeautVillageMapper.attSmallMediumRiverService();
    }
}
