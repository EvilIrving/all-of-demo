package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.ProjInspectionManagementInsReqVo;
import com.ygkj.project.vo.request.ProjInspectionManagementReqVo;
import com.ygkj.project.vo.response.ProjInspectionManagementRespVo;
import com.ygkj.vo.PageVO;

public interface ProjInspectionManagementService {

    /**
     * 新增
     */
    int insert(ProjInspectionManagementInsReqVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(ProjInspectionManagementInsReqVo vo);

    /**
     * 分页查找
     * @param vo
     * @return
     */
    PageVO<ProjInspectionManagementRespVo> loadPage(ProjInspectionManagementReqVo vo);
}
