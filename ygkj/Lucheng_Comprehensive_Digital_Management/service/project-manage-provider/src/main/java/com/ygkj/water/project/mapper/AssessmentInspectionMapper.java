package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.AssessmentInspection;
import com.ygkj.project.vo.request.AssessmentInspectionReqVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentInspectionMapper extends BaseMapper<AssessmentInspection> {
    //我的检查、我的督办
    List<AssessmentInspection> load(AssessmentInspectionReqVo vo);

    //我的检查
    List<AssessmentInspection> load1(AssessmentInspectionReqVo vo);

    //我的督办
    List<AssessmentInspection> load2(AssessmentInspectionReqVo vo);

    @Select("select * from assessment_inspection where id = #{id}")
    AssessmentInspection getById(String id);
}
