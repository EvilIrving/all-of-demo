package com.ygkj.water.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.UnitResponsibility;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.project.mapper.UnitResponsibilityMapper;
import com.ygkj.water.project.service.UnitResponsibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnitResponsibilityServiceImpl implements UnitResponsibilityService {
    @Autowired
    private UnitResponsibilityMapper responsibilityMapper;
    @Autowired
    private GenerateDistributionIDService idService;

    @Override
    public int save(UnitResponsibility entity) {
        entity.setId(idService.nextID());
        return responsibilityMapper.insert(entity);
    }

    @Override
    public int update(UnitResponsibility entity) {
        return responsibilityMapper.updateById(entity);
    }

    @Override
    public Object queryUnit(String id,String unitType,String deptId) {
        QueryWrapper<UnitResponsibility> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(id),"id",id);
        queryWrapper.eq(StringUtils.isNotBlank(unitType),"unit_type",unitType);
        queryWrapper.eq(StringUtils.isNotBlank(deptId),"dept_id",deptId);
        queryWrapper.eq("del_flag","0");

        if ("2".equals(unitType)){
            return responsibilityMapper.selectOne(queryWrapper);
        }else {
            return responsibilityMapper.selectList(queryWrapper);
        }
    }

    @Override
    public int delUnit(String id) {
        UnitResponsibility entity = new UnitResponsibility();
        entity.setId(id);
        entity.setDelFlag("1");
        return responsibilityMapper.updateById(entity);
    }
}
