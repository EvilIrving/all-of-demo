package com.ygkj.project;

import com.ygkj.entity.PointEntity;
import com.ygkj.project.vo.request.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Api(tags = "水利工程---地图管理", value = "水利工程---地图管理")
public interface ProjectManageControllerApi {

    @ApiOperation(value = "工程条件查询")
    CommonResult projects(ProjectManageQueryVo projectManageQueryVo);

    @ApiOperation(value = "工程条件查询统计")
    CommonResult stats4ProjectList(ProjectManageQueryVo projectManageQueryVo);

    @ApiOperation(value = "单个工程对应的视频列表")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectCode", value = "工程编码", required = true),
            @ApiImplicitParam(name = "cameraName", value = "摄像头名称", required = false)})
    CommonResult projectCameras(String projectCode, String cameraName);

    @ApiOperation(value = "单个工程对应的各个单位负责人信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projectCode", value = "工程编码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "projectType", value = "工程类型:大中型水库/小型水库/水库/泵站/水闸/海塘/堤防/山塘/水电站/农村供水工程/在建工程", required = true, paramType = "query")
    })
    CommonResult projectChargePerson(String projectCode, String projectType);

    @ApiOperation(value = "按三个指标统计摄像机")
    @ApiImplicitParam(name = "index", value = "统计类型：1全部，2离线", required = true)
    CommonResult cameraStatistics(Integer index);

    @ApiOperation(value = "获取当前离线的摄像机列表")
    CommonResult outLineCameras();

    @ApiOperation(value = "获取离线次数最多的五个摄像机")
    CommonResult outLineCamerasOrderList();

    @ApiOperation(value = "根据流域统计摄像机离线率")
    CommonResult outLineCamerasCountByBasin();

    @ApiOperation(value = "根据行政区划统计摄像机离线率")
    CommonResult outLineCamerasCountByAdcd();

    @ApiOperation(value = "根据工程类型统计摄像机离线率")
    CommonResult outLineCamerasCountByProjectType();

    @ApiOperation(value = "根据状态统计摄像机数量")
    CommonResult countCamerasByStatus();

    @ApiOperation(value = "大中型水库最新水位")
    CommonResult reservoirNewestWaterLevel(String projectCode);

    @ApiOperation(value = "获取工程对应建设内容信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "projCode", value = "工程编码", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "1.工程建设，2规划计划", paramType = "query")
    })
    CommonResult proBuildContents(String projCode, String type);

    @ApiOperation(value = "获取工程对应外链信息")
    CommonResult proLinks(String projCode);

    @ApiOperation(value = "所有的工程简要信息")
    CommonResult projectOverview();

    @ApiOperation(value = "通过工程编码获取工程信息")
    CommonResult projectInfoByCode(String projCode);

    @ApiOperation(value = "获取二维码流图片")
    void getProjectQrCode(String httpUrl, HttpServletResponse response) throws IOException;


    @ApiOperation("地图画面筛选工程")
    CommonResult projectInScope(List<PointEntity> list);

    @ApiOperation("地图画圆筛选工程")
    CommonResult projectInCircle(CircleReqVo reqVo);

    @ApiOperation("根据行政区划流域统计十大工程数量")
    CommonResult countProjTypeNumByAdcdBasin(String adcd, String basin);

    @ApiOperation("统计海塘和堤防的总长度")
    CommonResult dikeAndSeawallLengthCount();

    @ApiOperation("统计大中型水库档案当前年数量")
    CommonResult reservoirArchivesCount();

    @ApiOperation("获取 测站分析 统计 根据不同条件")
    CommonResult attStBaseCountByType(String type, String stType, Boolean isWrp);

    @ApiOperation("工程分析")
    CommonResult projectCount(String type, String stType);

    /**
     * 水库：无数据、不需要、正常、接近汛限、超汛限、超防洪高；
     * 河网/堰闸：无数据、正常、接近警戒、超警戒、超保证；
     * 潮位：无数据、正常、接近警戒、超警戒；
     *
     * @param type
     * @return
     */
    @ApiOperation("一张图工程统计")
    CommonResult projStatStatistic(@ApiParam("水库/河道/水闸/潮位") String type,
                                   @ApiParam("统计维度：水库（类型/区域/流域）,河道（区域/流域）,潮位（区域/流域）,水闸（区域/流域）") String dimension);


    @ApiOperation(value = "水库最新水位")
    CommonResult rsvrNewestWaterLevel(RsvrStationWaterReqVo reqVo);

    @ApiOperation(value = "水库及其测站及其水位数据")
    CommonResult rsvrWaterLevel(RsvrStationWaterReqVo reqVo);

    @ApiOperation(value = "水闸及其测站及其水位数据")
    CommonResult wagaWaterLevel(WagaStationWaterReqVo reqVo);

    @ApiOperation(value = "病险工程列表")
    CommonResult dilapProjList(DilapProjReqVo reqVo);

    @ApiOperation(value = "风险工程列表")
    CommonResult projRiskList(ProjRiskReqVo reqVo);

    @ApiOperation(value = "海塘大屏统计")
    CommonResult seawallStatsForBigScreen();

    @ApiOperation("水库复盘统计")
    CommonResult rsvrReplayStats(RsvrReplayReqVo reqVo);

    @ApiOperation("温瑞平水闸复盘统计")
    CommonResult wagaReplayStats(WagaReplayReqVo reqVo);

    @ApiOperation("水文工程测站概化图")
    CommonResult hydrProjStaDiagram(HydrDiagReqVo reqVo);

    @ApiOperation("水文工程测站概化图统计")
    CommonResult hydrProjStaDiagramStatistic(Integer hours);

    @ApiOperation("一张图左侧树的节点数量")
    CommonResult countProjectNumber();

    @ApiOperation("水库工程统计，统计大中型水库24小时的泄流量")
    CommonResult rsvrProjDrainageStats(@ApiParam("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值") Date dataTime);

    @ApiOperation("")
    CommonResult getRsvrDrillByTime(String dataTime) throws ParseException;

    @ApiOperation("查询所有的工程类型")
    CommonResult getAllProjType();

    @ApiOperation("保存用户选择的工程")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "dataStr", value = "数据json字符串"),
    })
    CommonResult saveChooseProject(String dataStr);

    @ApiOperation("获取用户选择的工程")
    CommonResult getChooseProject();

    /**
     * 一张图工程详表统计
     *
     * @param type
     * @return
     */
    @ApiOperation("一张图工程详表统计")
    CommonResult countProjectNum(String type);

}
