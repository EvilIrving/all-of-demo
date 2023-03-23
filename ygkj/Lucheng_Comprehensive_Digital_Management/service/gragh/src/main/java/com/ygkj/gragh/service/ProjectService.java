package com.ygkj.gragh.service;

import com.ygkj.gragh.model.AttProjBase;
import com.ygkj.gragh.vo.request.ProjectQueryVo;
import com.ygkj.gragh.vo.request.ProjectRangeQueryVo;
import com.ygkj.gragh.vo.request.RiverProjectQueryVo;
import com.ygkj.water.model.CommonResult;

import java.util.List;

public interface ProjectService {

    CommonResult projectList(ProjectQueryVo queryVo);

    CommonResult projectsInRange(ProjectRangeQueryVo projectRangeQueryVo);

    List<AttProjBase> riverProjects(RiverProjectQueryVo riverProjectQueryVo);
}
