package com.ygkj.water.project.service;


import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.project.model.BmProject;
import com.ygkj.project.model.UnitPositionSet;
import com.ygkj.project.vo.request.UnitPositionSetReqVo;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface UnitPositionSetService {

    int save(UnitPositionSet entity);

    int update(UnitPositionSet entity);

    PageVO<UnitPositionSet> queryPositions(UnitPositionSetReqVo vo);

    UnitPositionSet queryPosition(UnitPositionSetReqVo vo);

    int delPosition(String id);


    List<AttAdcdBase> selectLCArea();

    List<BmProject>selectProjects();
}
