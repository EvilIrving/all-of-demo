package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.vo.request.HydrDiagReqVo;

/**
 * @author xq
 * @Description 工程工情服务
 * @Date 2021/5/27
 */
public interface ProjectStatusService {

    /**
     * 水文工程测站概化图
     *
     * @return
     */
    Object hydrProjStaDiagram(HydrDiagReqVo reqVo);

    /**
     * 水文工程测站概化图统计窗口
     *
     * @return
     */
    JSONObject hydrProjStaDiagramStatistic(Integer hours);
}
