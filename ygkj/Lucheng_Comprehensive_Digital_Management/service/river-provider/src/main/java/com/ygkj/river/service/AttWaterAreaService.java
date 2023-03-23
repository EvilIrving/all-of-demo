package com.ygkj.river.service;

import com.ygkj.river.model.AttBlueProject;
import com.ygkj.river.vo.request.AttBlueProjectRecVo;
import com.ygkj.river.vo.request.AttWaterAreaReqVo;
import com.ygkj.river.vo.response.AttWaterAreaRespVo;
import com.ygkj.river.vo.response.HistogramWaterArea;
import com.ygkj.river.vo.response.OverallStatisticsWaterArea;
import com.ygkj.river.vo.response.PositionStatisticsWaterArea;
import com.ygkj.vo.PageVO;

import java.util.List;
import java.util.Map;

public interface AttWaterAreaService {

    OverallStatisticsWaterArea overallStatisticsWaterArea(Integer year);

    /**
     * 水域面积柱状图
     *
     * @return
     */
    Map<Integer, List<HistogramWaterArea>> histogramWaterArea();

    /**
     * 分页查找
     *
     * @param vo
     * @return
     */
    PageVO<AttWaterAreaRespVo> loadPage(AttWaterAreaReqVo vo);

    /**
     * 水域变化C位统计
     *
     * @param year
     * @return
     */
    PositionStatisticsWaterArea cPositionStatisticsWaterArea(Integer year);

    List<AttWaterAreaRespVo> waterAreaEachAdministrative(Integer year);

    /**
     * 批后监管新增数据的水域面积实时更新操作
     *
     * @param attBlueProject
     */
    void UpdateWwaterBodyData(AttBlueProjectRecVo attBlueProject);

    /**
     * 批后监管更新、删除数据的水域面积实时更新操作
     *
     * @param attBlueProject
     * @param type           类型 1：更新 2：删除
     * @param id             主键id
     */
    void UpdateWwaterBodyDataTwo(AttBlueProject attBlueProject, String type, String id);
}
