package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.model.AttCameraOps;
import com.ygkj.project.model.CameraWarnRecord;
import com.ygkj.project.model.HikvisionCameraRecord;
import com.ygkj.project.vo.request.CamBindObjReqVo;
import com.ygkj.project.vo.request.CameraOpsQueryReqVo;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.response.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.task.CameraWarnNotifyTask;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/13
 */
public interface CameraOpsService {
    /**
     * 摄像头区域树
     *
     * @return
     */
    List<RegionResVo> regionAsTree();

    /**
     * 分页查询摄像头
     *
     * @param reqVo
     * @return
     */
    PageVO<HikCameraResVo> pageCamera(HikCameraQueryReqVo reqVo);

    /**
     * 根据条件统计
     *
     * @param reqVo
     * @return
     */
    JSONObject stats4PageCamera(HikCameraQueryReqVo reqVo);

    /**
     * 列出摄像头工程绑定历史
     *
     * @param cameraIndexCode
     * @return
     */
    PageVO<HikCameraResVo> listBindHistory(String cameraIndexCode, Integer pageSize, Integer pageNum);

    /**
     * 更新一项摄像头信息
     *
     * @param cameraRecord
     * @return
     */
    int updateCameraInfo(HikvisionCameraRecord cameraRecord);

    /**
     * 删除一个摄像头
     *
     * @param cameraIndexCode
     * @return
     */
    int delCamera(String cameraIndexCode);

    /**
     * 工程根据区域绑定摄像头
     *
     * @param regionIndexCode
     * @return
     */
    int bindProjByRegion(String opsId, String regionIndexCode);

    /**
     * 工程绑定单摄像头
     *
     * @param projectId
     * @param cameraIndexCode
     * @return
     */
    int bindProjByCamera(String projectId, String cameraIndexCode);

    /**
     * 运维人员绑定单摄像头
     *
     * @param opsId
     * @param cameraIndexCode
     * @return
     */
    int bindOpsByCamera(String opsId, String cameraIndexCode);

    /**
     * 工程解绑摄像头
     *
     * @param cameraIndexCode
     * @return
     */
    int unbindProjCamera(String cameraIndexCode);

    /**
     * 运维人员解绑摄像头
     *
     * @param opsId
     * @param cameraIndexCode
     * @return
     */
    int unbindOpsCamera(String opsId, String cameraIndexCode);

    /**
     * 分页查询运维人员信息
     *
     * @param reqVo
     * @return
     */
    PageVO<AttCameraOps> pageOpsInfo(CameraOpsQueryReqVo reqVo);

    /**
     * 添加或新增一项运维人员信息
     *
     * @param record
     * @return
     */
    int addOrUpdateOps(AttCameraOps record);

    /**
     * 删除一项运维人员信息
     *
     * @param opsId
     * @return
     */
    int delOps(String opsId);

    /**
     * 统计流域全部/在线/离线摄像头数量
     *
     * @return
     */
    HikCameraStatsResVo statsCameraByBasin();

    /**
     * 统计工程全部/在线/离线摄像头数量
     *
     * @return
     */
    HikCameraStatsResVo statsCameraByProject();

    /**
     * 统计行政区划全部/在线/离线摄像头数量
     *
     * @return
     */
    HikCameraStatsResVo statsCameraByAdcd();

    /**
     * 分页查询摄像头运维状态
     *
     * @return
     */
    PageVO<HikOpsQuaResVo> pageCameraOpsStatus(HikCameraQueryReqVo reqVo);

    /**
     * 更新摄像头预警规则
     *
     * @param cameraIndexCode 摄像头唯一编码
     * @param isWarnNotify    是否报警
     * @param warnNotifyRule  预警规则，1，每小时，2，每日，3，每周，4，每月
     * @return
     */
    int updateCameraWarnNotifyRule(String cameraIndexCode, Boolean isWarnNotify, Integer warnNotifyRule);

    /**
     * 查看摄像头警告任务当前执行状态
     *
     * @param cameraIndexCode 摄像头编码
     * @return
     */
    @Deprecated
    CameraWarnNotifyTask getWarnTaskState(String cameraIndexCode);

    /**
     * 给摄像头运维人员发送短信
     *
     * @return
     */
    CommonResult sendCameraStatusToOps(String cameraIndexCode);

    /**
     * 摄像头历史发送短信记录
     *
     * @param cameraIndexCode
     * @return
     */
    List<CameraWarnRecord> historySmsLog(String cameraIndexCode);

    /**
     * 查询所有的摄像头
     *
     * @return
     */
    List<HikCameraResVo> selectCameras(HikCameraQueryReqVo vo);

    /**
     * 摄像头数量统计
     *
     * @param type 统计类型：1：根据行政区划统计；2：根据流域统计
     * @return
     */
    List<ChartResVo<String, Long>> cameraNumStats(int type);

    /**
     * 统计摄像头在线、离线数量
     *
     * @return JSONObject
     */
    List<CountCameraStatus> countCameraStatus();

    /**
     * 发送预警短信
     *
     * @param cameraIndexCode
     * @param forceSend
     * @return
     */
    boolean sendMsgV2(String cameraIndexCode, boolean forceSend);

    /**
     * 发送预警短信
     *
     * @param camera
     * @param forceSend
     * @return
     */
    boolean sendMsgV2(HikCameraResVo camera, boolean forceSend);

    /**
     * 摄像头根据行政区划统计，用于新版一张图
     *
     * @return
     */
    JSONObject cameraStatusByAdcd4Amap(Integer sortType);

    /**
     * 列出摄像头绑定对象
     *
     * @param reqVo
     * @return
     */
    PageVO<CamBindObjResVo> listCameraBindObj(CamBindObjReqVo reqVo);

    /**
     * 列出视频绑定对象类型
     *
     * @return
     */
    List<String> listAllCameraBindObjType();


}
