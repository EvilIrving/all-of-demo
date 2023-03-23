package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.UnitPositionSet;
import com.ygkj.project.vo.request.UnitPositionSetReqVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitPositionSetMapper extends BaseMapper<UnitPositionSet> {
    List<UnitPositionSet> queryList(UnitPositionSetReqVo vo);

    void updateBatch(UnitPositionSet positionSet);
}
