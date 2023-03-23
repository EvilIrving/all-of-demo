package com.ygkj.water.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.project.model.IrsInvestProjectBase;
import com.ygkj.project.model.IrsInvestProjectConstruction;
import com.ygkj.project.model.IrsInvestProjectFinance;
import com.ygkj.project.model.IrsInvestProjectPlan;
import com.ygkj.project.vo.request.InvestProjectReqVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.mapper.IrsInvestProjectBaseMapper;
import com.ygkj.water.project.service.IrsInvestProjectBaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author lxl
 * @create 2022-06-21 16:13
 * @description
 */
@Service
public class IrsInvestProjectBaseServiceImpl extends ServiceImpl<IrsInvestProjectBaseMapper, IrsInvestProjectBase> implements IrsInvestProjectBaseService {


    @Override
    public boolean editInvestProject(InvestProjectReqVo vo) {

        IrsInvestProjectBase base = new IrsInvestProjectBase();
        BeanUtil.copyProperties(vo,base);
        int i = baseMapper.updateById(base);
        return i > 0;
    }

    @Override
    public List<IrsInvestProjectBase> loadPage(InvestProjectReqVo vo) {
        return baseMapper.loadPage(vo);
    }

    @Override
    public List<IrsInvestProjectPlan> queryInvestProjectPlan(IrsInvestProjectPlan plan) {
        return baseMapper.queryInvestProjectPlan(plan);
    }

    @Override
    public int editInvestProjectPlan(IrsInvestProjectPlan plan) {
        int i = 0;
        if (StringUtils.isNotBlank(plan.getId())){
            i = baseMapper.editInvestProjectPlan(plan);
        }else{
            plan.setId(UUID.randomUUID().toString());
            i = baseMapper.saveInvestProjectPlan(plan);
        }
        return i;
    }

    @Override
    public List<IrsInvestProjectFinance> queryInvestProjectFinance(IrsInvestProjectFinance finance) {
        return baseMapper.queryInvestProjectFinance(finance);
    }

    @Override
    public int editInvestProjectFinance(IrsInvestProjectFinance finance) {
        int i = 0;
        if (StringUtils.isNotBlank(finance.getId())){
            i = baseMapper.editInvestProjectFinance(finance);
        }else{
            finance.setId(UUID.randomUUID().toString());
            i = baseMapper.saveInvestProjectFinance(finance);
        }
        return i;
    }

    @Override
    public List<IrsInvestProjectConstruction> queryInvestProjectConstruction(IrsInvestProjectConstruction construction) {
        return baseMapper.queryInvestProjectConstruction(construction);
    }

    @Override
    public int editInvestProjectConstruction(IrsInvestProjectConstruction construction) {
        int i = 0;
        if (StringUtils.isNotBlank(construction.getId())){
            i = baseMapper.editInvestProjectConstruction(construction);
        }else{
            construction.setId(UUID.randomUUID().toString());
            i = baseMapper.saveInvestProjectConstruction(construction);
        }
        return i;
    }
}
