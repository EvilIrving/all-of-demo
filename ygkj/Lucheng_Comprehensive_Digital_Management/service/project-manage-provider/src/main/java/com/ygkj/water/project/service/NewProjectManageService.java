package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface NewProjectManageService {

    PageVO<ReservoirProjectResp> reservoirProject(ReservoirProjectReq vo);

    List<StatisticsReservoirTypeResp> reservoirProjectStatic(ReservoirProjectReq vo);

    List<StatisticsSeawallTypeResp> seawallProjectStatic(SeawallProjectReq vo);

    PageVO<SeawallProjectResp> seawallProject(SeawallProjectReq vo);

    PageVO<DikeProjectResp> dikeProject(DikeProjectReq vo);

    /**
     * 堤防工程导出
     *
     * @param vo
     * @param httpServletResponse
     */
    void exportDikeProject(DikeProjectReq vo, HttpServletResponse httpServletResponse);

    List<StatisticsDikeTypeResp> dikeProjectStatic(DikeProjectReq vo);

    PageVO<WagaProjectResp> wagaProject(WagaProjectReq vo);

    List<StatisticsWagaTypeResp> wagaProjectStatic(WagaProjectReq vo);

    PageVO<IrrProjectResp> irrProject(IrrProjectReq vo);

    List<StatisticsIrrTypeResp> irrProjectStatic(IrrProjectReq vo);

    List<StatisticsGateTypeResp> gateProjectStatic(GateProjectReq vo);

    PageVO<GateProjectResp> gateProject(GateProjectReq vo);

    PageVO<PustProjectResp> pustProject(PustProjectReq vo);

    /**
     * 泵站工程导出
     *
     * @param vo                  请求入参
     * @param httpServletResponse 响应流
     * @return
     */
    void exportPustProject(PustProjectReq vo, HttpServletResponse httpServletResponse);

    List<StatisticsPustTypeResp> pustProjectStatic(PustProjectReq vo);

    List<ProjectAreaStatic> reservoirProjectAreaStatic(String areaCode);

    List<ProjectAreaStatic> seawallProjectAreaStatic(String areaCode);

    List<ProjectAreaStatic> dikeProjectAreaStatic(String areaCode);

    List<ProjectAreaStatic> wagaProjectAreaStatic(String areaCode);

    List<ProjectAreaStatic> irrProjectAreaStatic(String areaCode);

    List<ProjectAreaStatic> gateProjectAreaStatic(String areaCode);

    List<ProjectAreaStatic> pustProjectAreaStatic(String areaCode);

    List<DictResVo> countProject(String areaCode);

    CommonResult projectEngscalStatic(String type, String areaCode);

    /**
     * 工程数量统计
     *
     * @param type
     * @return
     */
    Integer statisticalProjectNum(String type);

    /**
     * 工程按行政区划数量统计
     *
     * @param type
     * @return
     */
    List<JSONObject> statisticalNumByArea(String type);

    /**
     * 工程C位统计
     *
     * @param type
     * @return
     */
    JSONObject cAStatisticalProject(String type);

    PageVO<AttMnpdBaseReqVo> mnpdProject(MnpdProjectReq vo);

    /**
     * 导出山塘数据
     *
     * @param response
     * @param reqVo
     */
    void exportMnpdProject(HttpServletResponse response, MnpdProjectReq reqVo);

    /**
     * 大屏灌区统计接口
     *
     * @param type 1. 统计大型，中型，小型灌区数目，2. c位统计
     */
    Object irrStats4BigScreen(int type);

    /**
     * 大屏闸站统计接口
     *
     * @param type 1. 统计大型，中型，小型灌区数目，2. c位统计
     */
    Object gateStats4BigScreen(int type);


    /**
     * 导出闸站数据
     *
     * @param response
     * @param reqVo
     */
    void exportGateProject(HttpServletResponse response, GateQueryReqVo reqVo);

    /**
     * 导出灌区数据
     *
     * @param response
     * @param reqVo
     */
    void exportIrrProject(HttpServletResponse response, IrrQueryReqVo reqVo);
}
