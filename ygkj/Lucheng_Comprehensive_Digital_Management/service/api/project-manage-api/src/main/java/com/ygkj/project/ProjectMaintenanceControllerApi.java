package com.ygkj.project;

import com.ygkj.project.model.WarningConfig;
import com.ygkj.project.model.WarningTemplate;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "水利工程---工程运维管理", value = "水利工程---工程运维管理")
public interface ProjectMaintenanceControllerApi {

    @ApiOperation(value = "视频管理摄像头查询接口", response = CameraResVo.class)
    CommonResult projectCameras(ProjectCameraPageQueryVo queryVo);

    @ApiOperation("摄像头新增")
    CommonResult cameraAdd(CameraAddVo cameraAddVo);

    @ApiOperation("摄像头修改")
    CommonResult cameraUpdate(CameraAddVo cameraAddVo);

    @ApiOperation("摄像头删除，支持批量")
    @ApiImplicitParam(name = "cameraIndexCodes", value = "摄像头编码，多个用逗号隔开", paramType = "query")
    CommonResult cameraDelete(String cameraIndexCodes);

    @ApiOperation("根据工程类型统计摄像头数量")
    CommonResult countCamerasByProjectType();

    @ApiOperation("根据行政区划统计摄像头数量")
    CommonResult countCamerasByAdcd();

    @ApiOperation(value = "报警模板查询接口", response = WarningTemplate.class)
    CommonResult WarningTemplatePageQuery(WarningTemplateQueryVo queryVo);

    @ApiOperation("报警模板新增")
    CommonResult addWarningTemplate(WarningTemplateAddVo vo);

    @ApiOperation("报警模板修改")
    CommonResult updateWarningTemplate(WarningTemplateAddVo vo);

    @ApiOperation("报警模板删除，支持批量")
    @ApiImplicitParam(name = "ids", value = "id，多个用逗号隔开", paramType = "query")
    CommonResult warningTypeDelete(String ids);

    @ApiOperation(value = "报警配置查询接口", response = WarningConfig.class)
    CommonResult warningConfigs(WarningConfigPageQueryVo warningConfigPageQueryVo);

    @ApiOperation("报警配置新增")
    CommonResult addWarningConfig(WarningConfigAddVo warningConfigAddVo);

    @ApiOperation("报警配置修改")
    CommonResult updateWarningConfig(WarningConfigAddVo warningConfigAddVo);

    @ApiOperation("报警配置删除，支持批量")
    @ApiImplicitParam(name = "ids", value = "id，多个用逗号隔开", paramType = "query")
    CommonResult deleteWarningConfig(String ids);

    @ApiOperation(value = "报警管理查询接口", response = WarningRecordResVo.class)
    CommonResult warningRecords(WarningRecordPageQueryVo queryVo);
//    @ApiOperation(value = "根据工程统计摄像头的数量",response = WarningRecordResVo.class)
//    CommonResult countCamerasByProjectType();

    @ApiOperation(value = "报警管理导出")
    void exportWarningRecords(WarningRecordPageQueryVo vo, HttpServletResponse response);

    @ApiOperation("导出模板列表")
    void exportTemplateList(WarningTemplateQueryVo queryVo, HttpServletResponse response);

    @ApiOperation("报警配置-导出")
    void exportwarningConfigs(WarningConfigQueryVo vo, HttpServletResponse response);

    @ApiOperation("报警配置-查询")
    CommonResult warningConfigQuery(WarningConfigQueryVo vo);

    @ApiOperation("报警配置-查询测站信息(降雨)")
    CommonResult staInfos(StaInfoQueryVo vo, String stCodes);

    @ApiOperation("报警配置-更新插入测站降雨历时对应阈值-返回配置id")
    CommonResult warnThresholdOperate(WarnThresholdOperateVo vo);

    @ApiOperation("报警配置-获取人员信息")
    CommonResult userInfos();

    @ApiOperation("报警配置-新增")
    CommonResult warningConfigAdd(WarningConfigAddReqVo vo);

    @ApiOperation("报警配置-查询测站信息(水位)")
    CommonResult<List<WaterStaInfoRespVo>> waterStaInfos(StaInfoQueryVo vo);

    @ApiOperation("报警配置-水位更新插入测站类型对应阈值类型-返回配置id")
    CommonResult waterWarnThresholdOperate(WaterWarnThresholdOperateVo vo);

    @ApiOperation("报警配置-查询测站信息(流量)")
    CommonResult<List<FlowStaInfoRespVo>> flowStaInfos(StaInfoQueryVo vo);

    @ApiOperation("报警配置-流量更新插入报警条件对应流量阈值-返回配置id")
    CommonResult flowWarnThresholdOperate(FlowWarnThresholdOperateVo vo);

    @ApiOperation("报警配置-查询测站信息(蓄水量)")
    CommonResult<List<CapacityStaInfoRespVo>> capacityStaInfos(StaInfoQueryVo vo);

    @ApiOperation("报警配置-蓄水量更新插入对应平均蓄水量-返回配置id")
    CommonResult capacityWarnThresholdOperate(CapacityWarnThresholdOperateVo vo);

    @ApiOperation("报警配置-修改（根据测站id）")
    CommonResult warningConfigUpd(WarnConfigUdpReqVo vo);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "configIds", value = "报警配置id,多个以逗号分隔"),
            @ApiImplicitParam(name = "stCodes", value = "测站ids,多个以逗号分隔")
    })
    @ApiOperation("报警配置-删除")
    CommonResult warningConfigDel(String configIds, String stCodes);

    @ApiOperation("报警分析-查询")
    CommonResult warnAnalysisQuery(WarningAnalyseQueryVo vo);

    @ApiOperation("报警分析-导出")
    void exportWarningAnalyses(WarningAnalyseQueryVo vo, HttpServletResponse response);

    @ApiOperation("报警分析-转发")
    CommonResult forwordWarnAnalyse(ForwordWarnAnalyseReqVo vo);
}
