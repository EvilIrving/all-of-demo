package com.ygkj.water.project.strategy;

import com.ygkj.project.vo.request.ProjectManageQueryVo;
import com.ygkj.water.project.enums.ProjectTypeEnum;
import com.ygkj.water.project.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
public class DikeStrategy implements Strategy{

    private ProjectMapper projectMapper;

    public DikeStrategy(@Autowired ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Override
    public String type() {
        return ProjectTypeEnum.DIKE.getType();
    }

    @Override
    public Collection records(String codes) {
        ProjectManageQueryVo vo = new ProjectManageQueryVo();
        vo.setCodes(Arrays.asList(codes.split(",")));
        return projectMapper.selectDikeByCondition(vo);
    }
}
