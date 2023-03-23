package com.ygkj.river.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.river.vo.request.AttSwhsBaseReqVo;
import com.ygkj.river.vo.response.AttSwhsBaseRespVo;
import com.ygkj.river.vo.response.StatisSwarnRespVo;
import com.ygkj.vo.PageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AttSwhsBaseService {

    /**
     * 新增
     */
    int insert(AttSwhsBaseReqVo vo);

    /**
     * 删除
     */
    int delete(String swhsCode);

    /**
     * 更新
     */
    int update(AttSwhsBaseReqVo vo);

    /**
     * 分页查找
     *
     * @param vo
     * @return
     */
    PageVO<AttSwhsBaseRespVo> loadPage(AttSwhsBaseReqVo vo);

    /**
     * 水源详表组件导出
     * @param vo
     * @param response
     */
    void exportSwhsPageList(AttSwhsBaseReqVo vo, HttpServletResponse response);


    /**
     * 组件-水源地数量按规模统计
     *
     * @return
     */
    List<JSONObject> statisSwhsNumByScale(String type);

    /**
     * 组件-水源地数量按类型统计
     *
     * @return
     */
    List<JSONObject> statisSwhsNumByType(String type);

    /**
     * 组件-水源地数量按行政区划统计
     *
     * @return
     */
    List<JSONObject> statisSwhsNumByArea(String type);

    /**
     * 组件- 水量预警等级数量统计（数量OR人口）
     *
     * @return
     */
    List<JSONObject> statisSwarnGradeNum(String type);

    /**
     * 组件-水量预警等级数量按行政区划统计
     *
     * @return
     */
    List<JSONObject> statisSwarnGradeByAdcd(String type);

    /**
     * C位统计
     * @return
     */
    StatisSwarnRespVo statisC();

    /**
     * 水源地、预警数量统计
     * @param vo
     * @return
     */
    List<JSONObject> waterSuantityStatistic(AttSwhsBaseReqVo vo);

}
