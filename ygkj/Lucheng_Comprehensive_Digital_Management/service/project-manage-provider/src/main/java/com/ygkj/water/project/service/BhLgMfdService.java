package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.BhLgMfdReqVo;

import javax.servlet.http.HttpServletResponse;


public interface BhLgMfdService {
    int insert(BhLgMfdReqVo vo);

    int update(BhLgMfdReqVo vo);

    int delete(String id);

    Object loadPage(BhLgMfdReqVo vo);

    void exportMtFund(BhLgMfdReqVo vo, HttpServletResponse response);
}
