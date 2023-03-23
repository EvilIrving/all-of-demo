package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.BhLgLmrDisDomReqVo;

import javax.servlet.http.HttpServletResponse;

public interface BhLgLmrDisDomService {
    int insert(BhLgLmrDisDomReqVo vo);

    int update(BhLgLmrDisDomReqVo vo);

    int delete(String id);

    Object loadPage(BhLgLmrDisDomReqVo vo);

    void exportDisDom(BhLgLmrDisDomReqVo vo, HttpServletResponse response);
}
