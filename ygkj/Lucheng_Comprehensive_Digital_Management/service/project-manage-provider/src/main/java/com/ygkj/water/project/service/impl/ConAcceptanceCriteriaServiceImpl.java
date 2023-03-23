package com.ygkj.water.project.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.ConAcceptanceCriteria;
import com.ygkj.project.vo.request.ConAcceptanceCriteriaReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.ConAcceptanceCriteriaMapper;
import com.ygkj.water.project.service.ConAcceptanceCriteriaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ConAcceptanceCriteriaServiceImpl implements ConAcceptanceCriteriaService {
    @Resource
    private ConAcceptanceCriteriaMapper criteriaMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Override
    public void insert(ConAcceptanceCriteriaReqVo vo) {
        ConAcceptanceCriteria criteria = new ConAcceptanceCriteria();
        BeanUtils.copyProperties(vo,criteria);
        criteria.setId(idService.nextID());
        criteria.setCreateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        criteriaMapper.insert(criteria);
    }

    @Override
    public void update(ConAcceptanceCriteriaReqVo vo) {
        ConAcceptanceCriteria criteria = new ConAcceptanceCriteria();
        BeanUtils.copyProperties(vo,criteria);
        criteria.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        criteriaMapper.updateById(criteria);
    }

    @Override
    public Object load(ConAcceptanceCriteriaReqVo vo) {
        if (vo.isPaged()){
            PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        }
        List<ConAcceptanceCriteria>  criteria = criteriaMapper.list(vo);
        PageVO<ConAcceptanceCriteria> pageVO = PageUtils.getPageVO(criteria);
        return pageVO;
    }

    @Override
    public void delete(String id) {
        ConAcceptanceCriteria criteria = new ConAcceptanceCriteria();
        criteria.setId(id);
        criteria.setDelFlag(1);
        criteriaMapper.updateById(criteria);
    }
}
