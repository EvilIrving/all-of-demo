package com.ygkj.water.project.mapper;

import com.ygkj.project.model.ProjSupervisionReport;
import com.ygkj.project.vo.request.ProjSupervisionReportReqVo;
import com.ygkj.project.vo.response.ProjSupervisionReportRespVo;

import java.util.List;

/**
 * @author hucong
 * @description 工程监督-监理月报表
 * @date 2021-07-09
 */
public interface ProjSupervisionReportMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/07/09
     **/
    int insert(ProjSupervisionReport projSupervisionReport);

    /**
     * 刪除
     *
     * @author hucong
     * @date 2021/07/09
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author hucong
     * @date 2021/07/09
     **/
    int update(ProjSupervisionReport projSupervisionReport);

    List<ProjSupervisionReportRespVo> loadPage(ProjSupervisionReportReqVo vo);

}
