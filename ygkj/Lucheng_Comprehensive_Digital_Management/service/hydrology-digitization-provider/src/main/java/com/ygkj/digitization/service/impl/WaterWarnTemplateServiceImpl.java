package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.mapper.WaterWarnTemplateMapper;
import com.ygkj.digitization.model.WaterWarnTemplate;
import com.ygkj.digitization.service.WaterWarnTemplateService;
import com.ygkj.digitization.vo.request.WaterWarnTemplateReqVo;
import com.ygkj.digitization.vo.response.WaterWarnTemplateRespVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @description 水位预警模板表
 * @author hucong
 * @date 2021-07-13
 */
@Service
public class WaterWarnTemplateServiceImpl implements WaterWarnTemplateService {

    @Resource
    private WaterWarnTemplateMapper waterWarnTemplateMapper;
    @Resource
    private SnowFlake snowFlake;

    @Override
    public int insert(WaterWarnTemplateReqVo vo) {
        WaterWarnTemplate entity = new WaterWarnTemplate();
        BeanUtils.copyProperties(vo,entity);
        entity.setCreateTime(new Date());
        entity.setCreatorId(JwtTokenUtil.getCurrentUserId().orElse(""));
        entity.setCreateName(JwtTokenUtil.getCurrentUserName().orElse(""));
        entity.setId(String.valueOf(snowFlake.nextId()));
        return waterWarnTemplateMapper.insert(entity);
    }

    @Override
    public int delete(String id) {
        return waterWarnTemplateMapper.delete(id);
    }

    @Override
    public int update(WaterWarnTemplateReqVo vo) {
        WaterWarnTemplate entity = new WaterWarnTemplate();
        BeanUtils.copyProperties(vo,entity);
        return waterWarnTemplateMapper.update(entity);
    }

    @Override
    public PageVO<WaterWarnTemplateRespVo> loadPage(WaterWarnTemplateReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        return PageUtils.getPageVO(waterWarnTemplateMapper.loadPage(vo));
    }
}
