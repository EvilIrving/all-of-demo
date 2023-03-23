package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.ProjChangeManagementInsVo;
import com.ygkj.project.vo.request.ProjChangeManagementReqVo;
import com.ygkj.project.vo.response.ProjChangeManagementRespVo;
import com.ygkj.vo.PageVO;

/**
 * @description 变更管理表
 * @author hucong
 * @date 2021-07-08
 */
public interface ProjChangeManagementService {

    /**
     * 新增
     */
    int insert(ProjChangeManagementInsVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(ProjChangeManagementInsVo vo);

    /**
     * 分页查找
     * @param vo
     * @return
     */
    PageVO<ProjChangeManagementRespVo> loadPage(ProjChangeManagementReqVo vo);
}
