package com.ygkj.water.project.mapper;

import com.ygkj.project.model.ProjChangeManagement;
import com.ygkj.project.vo.request.ProjChangeManagementReqVo;
import com.ygkj.project.vo.response.ProjChangeManagementRespVo;

import java.util.List;

/**
 * @author hucong
 * @description 变更管理表
 * @date 2021-07-08
 */
public interface ProjChangeManagementMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/07/08
     **/
    int insert(ProjChangeManagement projChangeManagement);

    /**
     * 刪除
     *
     * @author hucong
     * @date 2021/07/08
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author hucong
     * @date 2021/07/08
     **/
    int update(ProjChangeManagement projChangeManagement);

    /**
     * 查找list数据
     *
     * @param vo
     * @return
     */
    List<ProjChangeManagementRespVo> loadPage(ProjChangeManagementReqVo vo);
}
