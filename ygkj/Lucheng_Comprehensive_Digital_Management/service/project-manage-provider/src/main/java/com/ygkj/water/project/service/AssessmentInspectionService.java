package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.AssessmentInspection;
import com.ygkj.project.vo.request.AssessmentInspectionReqVo;

import javax.servlet.http.HttpServletResponse;

public interface AssessmentInspectionService extends IService<AssessmentInspection> {
    void insert(AssessmentInspectionReqVo vo);

    void update(AssessmentInspectionReqVo vo);

    Object load(AssessmentInspectionReqVo vo);

    void delete(String id);

    void export(AssessmentInspectionReqVo vo, HttpServletResponse response);

    void handing(AssessmentInspectionReqVo vo, String handType);

    Object getResponsibleUnits();

    Object appCount(AssessmentInspectionReqVo vo);
}
