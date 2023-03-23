package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.OverhaulReqVo;
import com.ygkj.vo.PageVO;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-18 14:14
 * @description 物业化管理-运行操作-检修操作
 */
public interface OverhaulService {


    int insert(OverhaulReqVo vo);


    int update(OverhaulReqVo vo);

    int delete(OverhaulReqVo vo);

    PageVO loadPage(OverhaulReqVo vo);

    void exportOverhaul(OverhaulReqVo vo, HttpServletResponse response);
}
