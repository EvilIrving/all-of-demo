package com.ygkj.gragh.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.model.PrecedeAffectRainfall;
import com.ygkj.gragh.vo.request.PrecedeAffectRainfallQueryVo;
import com.ygkj.water.model.CommonResult;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/24
 */
public interface PrecedeAffectRainfallService {

    /**
     * 前期影响雨量曲线
     *
     * @param adcd
     * @return
     */
    List<PrecedeAffectRainfall> precedeAffectRainfallCurve(String areaCode);

    /**
     * 前期影响雨量列表
     *
     * @param theDay
     * @return
     */
    List<PrecedeAffectRainfall> todayPrecedeAffectRainfall(Date theDay);

    /**
     * 前期影响雨量c位窗口
     *
     * @return
     */
    JSONObject precedeAffectRainfall4CWindow();

    /**
     * 前期影响雨量列表 分页查询
     *
     * @param queryVo
     * @return
     */
    List<PrecedeAffectRainfall> pagePrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo);

    /**
     * 前期影响雨量列表-详表统计
     *
     * @param queryVo
     * @return
     */
    JSONObject stats4PrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo);

    /**
     * 前期影响雨量列表-详表统计
     *
     * @param queryVo
     * @param response
     */
    void exportPrecedeAffectRainfall(PrecedeAffectRainfallQueryVo queryVo, HttpServletResponse response);
}
