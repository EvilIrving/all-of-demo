package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ygkj.project.model.AttStBase;
import com.ygkj.project.vo.request.AttStBaseReqVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.StMapper;
import com.ygkj.water.project.service.StService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("stService")
public class StServiceImpl implements StService {

    @Resource
    private StMapper stMapper;

    /**
     * 分页查询
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<AttStBase> page(AttStBaseReqVo reqVo) {
        LambdaQueryWrapper<AttStBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(reqVo.getStCode()),AttStBase::getStCode,reqVo.getStCode());
        queryWrapper.eq(StringUtils.isNotEmpty(reqVo.getStType()),AttStBase::getStType,reqVo.getStType());
        queryWrapper.like(StringUtils.isNotEmpty(reqVo.getStName()),AttStBase::getStName,reqVo.getStName());
        reqVo.page();
        return PageUtils.getPageVO(stMapper.selectList(queryWrapper));
    }

    /**
     * 更新
     *
     * @param attStBase
     */
    @Override
    public void update(AttStBase attStBase) {
        stMapper.updateById(attStBase);
    }
}
