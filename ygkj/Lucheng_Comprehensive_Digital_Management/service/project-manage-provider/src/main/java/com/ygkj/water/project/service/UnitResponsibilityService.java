package com.ygkj.water.project.service;

import com.ygkj.project.model.UnitResponsibility;

public interface UnitResponsibilityService {
    int save(UnitResponsibility entity);

    int update(UnitResponsibility entity);

    Object queryUnit(String id,String unitType,String deptId);

    int delUnit(String id);
}
