package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.ProjSupervisionReportInsVo;
import com.ygkj.project.vo.request.ProjSupervisionReportReqVo;
import com.ygkj.project.vo.response.ProjSupervisionReportRespVo;
import com.ygkj.vo.PageVO;

/**
 * @description 工程监督-监理月报表
 * @author hucong
 * @date 2021-07-09
 */
public interface ProjSupervisionReportService {

    /**
     * 新增
     */
    int insert(ProjSupervisionReportInsVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(ProjSupervisionReportInsVo vo);

    /**
     * 分页查找
     * @param vo
     * @return
     */
    PageVO<ProjSupervisionReportRespVo> loadPage(ProjSupervisionReportReqVo vo);
}
