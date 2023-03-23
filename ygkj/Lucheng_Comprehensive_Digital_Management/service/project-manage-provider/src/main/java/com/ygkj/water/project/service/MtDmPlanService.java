package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.MtDmPlanReqVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-18 16:58
 * @description
 */
public interface MtDmPlanService {
    
    
    int insert(MtDmPlanReqVo vo);

    int update(MtDmPlanReqVo vo);

    int delete(String id);

    Object loadPage(MtDmPlanReqVo vo);

    void exportMtDmPlan(MtDmPlanReqVo vo, HttpServletResponse response);

    String getYgFile(String pid, String fileId);
}
