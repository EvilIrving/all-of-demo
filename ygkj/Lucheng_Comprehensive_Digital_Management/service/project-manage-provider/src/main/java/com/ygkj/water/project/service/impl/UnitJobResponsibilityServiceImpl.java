package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.UnitJobResponsibility;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.mapper.UnitJobResponsibilityMapper;
import com.ygkj.water.project.service.UnitJobResponsibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitJobResponsibilityServiceImpl implements UnitJobResponsibilityService {
    @Autowired
    private UnitJobResponsibilityMapper jobResponsibilityMapper;
    @Autowired
    private GenerateDistributionIDService idService;

    @Override
    public int save(UnitJobResponsibility entity) {
        entity.setId(idService.nextID());
        return jobResponsibilityMapper.insert(entity);
    }

    @Override
    public int update(UnitJobResponsibility entity) {
        return jobResponsibilityMapper.updateById(entity);
    }

    @Override
    public UnitJobResponsibility queryJob(String id, String unitType,String jobType,String deptId) {
        QueryWrapper<UnitJobResponsibility> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(id),"id",id);
        queryWrapper.eq(StringUtils.isNotBlank(unitType),"unit_type",unitType);
        queryWrapper.eq(StringUtils.isNotBlank(jobType),"job_type",jobType);
        queryWrapper.eq(StringUtils.isNotBlank(deptId),"dept_id",deptId);
        queryWrapper.eq("del_flag","0");
        return jobResponsibilityMapper.selectOne(queryWrapper);
    }

    @Override
    public int delJob(String id) {
        UnitJobResponsibility entity = new UnitJobResponsibility();
        entity.setId(id);
        entity.setDelFlag("1");
        return jobResponsibilityMapper.updateById(entity);
    }
}
