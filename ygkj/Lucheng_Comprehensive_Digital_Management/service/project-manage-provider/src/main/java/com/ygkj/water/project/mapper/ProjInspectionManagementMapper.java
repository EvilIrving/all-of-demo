package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.project.model.ProjInspectionManagement;
import com.ygkj.project.vo.request.ProjInspectionManagementReqVo;
import com.ygkj.project.vo.response.ProjInspectionManagementRespVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjInspectionManagementMapper {

    /**
     * 新增
     */
    int insert(ProjInspectionManagement projInspectionManagement);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(ProjInspectionManagement projInspectionManagement);

    /**
     * 分页查找
     *
     * @param vo
     * @return
     */
    List<ProjInspectionManagementRespVo> loadPage(ProjInspectionManagementReqVo vo);

}
