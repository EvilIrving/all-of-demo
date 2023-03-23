package com.ygkj.project;

import com.ygkj.project.model.AttCameraOps;
import com.ygkj.project.model.HikvisionCameraRecord;
import com.ygkj.project.vo.request.CamBindObjReqVo;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.request.CameraOpsQueryReqVo;
import com.ygkj.project.vo.response.CamBindObjResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/4/13
 */
@Api(tags = "摄像头运维接口")
public interface CameraOpsControllerApi {
    @ApiOperation("摄像头区域树")
    CommonResult regionAsTree();

    @ApiOperation("分页查询摄像头")
    CommonResult pageCamera(HikCameraQueryReqVo reqVo);

    @ApiOperation("统计分页查询摄像头")
    CommonResult stats4PageCamera(HikCameraQueryReqVo reqVo);

    @ApiOperation("导出分页查询的摄像头")
    void exportPageCamera(HttpServletResponse response, HikCameraQueryReqVo reqVo);

    @ApiOperation("列出摄像头工程绑定历史")
    CommonResult listBindHistory(String cameraIndexCode, Integer pageSize, Integer pageNum);

    @ApiOperation("更新一项摄像头信息")
    CommonResult updateCameraInfo(HikvisionCameraRecord cameraRecord);

    @ApiOperation("删除一个摄像头")
    CommonResult delCamera(String cameraIndexCode);

    @ApiOperation("工程绑定根据区域绑定摄像头")
    CommonResult bindProjByRegion(String projectId, String regionIndexCode);

    @ApiOperation("工程绑定单摄像头")
    CommonResult bindProjByCamera(String projectId, String cameraIndexCode);

    @ApiOperation("运维人员绑定单摄像头")
    CommonResult bindOpsByCamera(String opsId, String cameraIndexCode);

    @ApiOperation("工程绑定解绑摄像头")
    CommonResult unbindProjCamera(String cameraIndexCode);

    @ApiOperation("运维人员解绑摄像头")
    CommonResult unbindOpsCamera(String opsId, String cameraIndexCode);

    @ApiOperation("分页查询运维人员信息")
    CommonResult pageOpsInfo(CameraOpsQueryReqVo reqVo);

    @ApiOperation("添加或新增一项运维人员信息")
    CommonResult addOrUpdateOps(AttCameraOps record);

    @ApiOperation("删除一项运维人员信息")
    CommonResult delOps(String opsId);

    @ApiOperation("统计流域全部/在线/离线摄像头数量")
    CommonResult statsCameraByBasin();

    @ApiOperation("统计工程全部/在线/离线摄像头数量")
    CommonResult statsCameraByProject();

    @ApiOperation("统计行政区划全部/在线/离线摄像头数量")
    CommonResult statsCameraByAdcd();

    @ApiOperation("统计摄像头在线、离线数量")
    CommonResult countCameraStatus();

    @ApiOperation("分页查询摄像头运维状态")
    CommonResult pageCameraOpsStatus(HikCameraQueryReqVo reqVo);

    @ApiOperation("更新摄像头预警规则")
    CommonResult updateCameraWarnNotifyRule(String cameraIndexCode, Boolean isWarnNotify, Integer warnNotifyRule);

    @Deprecated
    @ApiOperation(value = "查看摄像头警告任务当前执行状态")
    CommonResult getWarnTaskState(String cameraIndexCode);

    @ApiOperation("给摄像头运维人员发送短信")
    CommonResult sendCameraStatusToOps(String cameraIndexCode);

    @ApiOperation("摄像头历史发送短信记录")
    CommonResult historySmsLog(String cameraIndexCode);

    @ApiOperation("单表查询所有的摄像头")
    CommonResult selectCameras(HikCameraQueryReqVo vo);

    @ApiOperation("摄像头数量统计")
    CommonResult cameraNumStats(@ApiParam("统计类型：1：根据行政区划统计；2：根据流域统计") int type);

    @ApiOperation("摄像头根据行政区划统计，用于新版一张图")
    @ApiImplicitParams({@ApiImplicitParam(name = "sortType", value = "1. 按总数，2. 按在线数，3. 按离线数，4. 按在线率 降序")})
    CommonResult cameraStatusByAdcd4Amap(Integer sortType);

    @ApiOperation(value = "列出摄像头绑定对象", response = CamBindObjResVo.class)
    CommonResult listCameraBindObj(CamBindObjReqVo reqVo);

    @ApiOperation(value = "列出视频绑定对象类型")
    CommonResult listCameraBindObjType();
}
