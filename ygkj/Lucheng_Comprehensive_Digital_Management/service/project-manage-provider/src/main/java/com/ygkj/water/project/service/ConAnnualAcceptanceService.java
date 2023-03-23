package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.ConAnnualAcceptanceReqVo;

import javax.servlet.http.HttpServletResponse;

public interface ConAnnualAcceptanceService {
    void insert(ConAnnualAcceptanceReqVo vo);

    void update(ConAnnualAcceptanceReqVo vo);

    Object load(ConAnnualAcceptanceReqVo vo);

    int delete(String id);

    void exportAnnualAcceptance(ConAnnualAcceptanceReqVo vo, HttpServletResponse response);
}
