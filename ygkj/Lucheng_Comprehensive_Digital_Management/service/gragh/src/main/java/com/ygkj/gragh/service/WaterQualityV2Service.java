package com.ygkj.gragh.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.vo.request.RivQuaReqVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.gragh.vo.response.RivQuaResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/5
 */
public interface WaterQualityV2Service {


    /**
     * 查询最新的水质监测列表
     *
     * @param reqVo
     * @return
     */
    List<RivQuaResVo> list(RivQuaReqVo reqVo);

    /**
     * 查询最新的水质监测列表-表头统计
     *
     * @param reqVo
     * @return
     */
    JSONObject stats4List(RivQuaReqVo reqVo);

    /**
     * 导出最新的水质监测列表
     *
     * @param reqVo
     * @param response
     */
    void exportList(RivQuaReqVo reqVo, HttpServletResponse response);

    /**
     * 查询水质监测
     *
     * @param siteNo
     * @param month
     * @return
     */
    List<RivQuaResVo> qualityCurve(String siteNo, int month);

    /**
     * @param type 1. 根据行政区划统计，2. 根据测站级别统计，3，根据水质类别统计
     * @return
     */
    List<ChartResVo<String, Long>> qualityStats(int type);

    /**
     * 水质监测组件C位
     *
     * @return
     */
    JSONObject qualityUnitCWindow();
}
