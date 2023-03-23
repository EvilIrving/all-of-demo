package com.ygkj.river;

import com.ygkj.river.model.AttBlueProject;
import com.ygkj.river.vo.request.AttBlueProjectRecVo;
import com.ygkj.river.vo.request.AttBlueProjectReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2021-08-05 21:17
 * @description 蓝线管理监管项目接口
 */
@Api(tags = "蓝线管理监管项目接口")
public interface AttBlueProjectControllerApi {

    /**
     * 根据行政区划获取河流
     * @param areaCode 行政区划编码
     * @return
     */
    @ApiOperation("根据行政区划获取河流")
    CommonResult getRiverByAreaCode(String areaCode);

    /**
     * 新增监管项目
     * @param attBlueProject
     * @return
     */
    @ApiOperation("新增监管项目")
    CommonResult addProject(AttBlueProjectRecVo attBlueProject);

    /**
     * 条件筛选监管项目列表
     * @param reqVo
     * @return
     */
    @ApiOperation("条件筛选监管项目列表")
    CommonResult projectList(AttBlueProjectReqVo reqVo);

    /**
     * 更新监管项目
     * @param attBlueProject
     * @return
     */
    @ApiOperation("更新监管项目")
    CommonResult updateProject(AttBlueProject attBlueProject);

    /**
     * 根据id删除项目
     * @param id
     * @return
     */
    @ApiOperation("根据id删除项目")
    CommonResult deleteProjectById(String id);


    /**
     * 查看当前项目详情
     * @param projectId
     * @return 包括多次巡查记录，以及包括的问题异常等
     */
    @ApiOperation("查看当前项目详情，根据项目id ")
    CommonResult projectDetail(String projectId);

    @ApiOperation("批后监管按流域分布统计")
    CommonResult projectStatisByBasin();

    @ApiOperation("批后监管按河道分布统计")
    CommonResult projectStatisByRiverLevel();

    @ApiOperation("批后监管按行政区划统计")
    CommonResult projectStatisByAdcd();

    @ApiOperation("批后监管按项目阶段统计")
    CommonResult projectStatisByProjectStage();
    /**
     *
     * @return
     */
    @ApiOperation("C位统计")
    CommonResult homeOverView();

    @ApiOperation("新 首页统计")
    CommonResult projectStaticsForHome();

    @ApiOperation("首页统计ByAdcd")
    CommonResult homeOverViewByAdcd();

    @ApiOperation("导出详表")
    void exportDetailTable(AttBlueProjectReqVo reqVo, HttpServletResponse response);

    @ApiOperation("组件-按月份计算监管次数")
    CommonResult projectInspectionNum(Integer year);

    @ApiOperation("组件-按行政区划计算人数以及问题个数")
    CommonResult projectProblemNum();

    @ApiOperation("大屏 按行政区划计算监察完成率和次数")
    CommonResult inspectionFinishRateAndNumber();

}
