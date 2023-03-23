package com.ygkj.water.project.service;

import com.ygkj.project.model.UnitJobResponsibility;

public interface UnitJobResponsibilityService {

    int save(UnitJobResponsibility entity);

    int update(UnitJobResponsibility entity);

    UnitJobResponsibility queryJob(String id,String unitType,String jobType,String deptId);

    int delJob(String id);

}
