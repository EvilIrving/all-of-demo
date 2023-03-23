package com.ygkj.gragh;

import com.ygkj.gragh.vo.request.CameraRangeQueryVo;
import com.ygkj.gragh.vo.request.ProjectQueryVo;
import com.ygkj.gragh.vo.request.ProjectRangeQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水事务监管", value = "水事务监管")
public interface WaterTransactionExamineControllerApi {

    @ApiOperation("工程列表条件查询（地图和右侧列表通用）")
    CommonResult projects(ProjectQueryVo queryVo);

    @ApiOperation("工程详情中关联的视频点位")
    CommonResult camerasInRange(CameraRangeQueryVo cameraRangeQueryVo);

    @ApiOperation("水雨情详情中关联的工程点位")
    CommonResult projectsInRange(ProjectRangeQueryVo projectRangeQueryVo);
}
