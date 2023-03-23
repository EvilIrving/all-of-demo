package com.ygkj.water.project.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.PointEntity;
import com.ygkj.gragh.model.HikvisionCameraRecord;
import com.ygkj.gragh.vo.request.CameraQueryVo;
import com.ygkj.project.model.AttProjBuildContent;
import com.ygkj.project.model.AttProjLink;
import com.ygkj.project.model.WarningConfig;
import com.ygkj.project.model.WarningTemplate;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.vo.PageVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ProjectManageService {

    List<?> projectList(ProjectManageQueryVo queryVo);

    /**
     * 工程详情查询的统计
     *
     * @param queryVo
     * @return
     * @see ProjectManageService#projectList(ProjectManageQueryVo)
     */
    Map<String, Object> stats4ProjectList(ProjectManageQueryVo queryVo);

    List<HikvisionCameraRecord> projectCameras(CameraQueryVo cameraQueryVo);

    List<JSONObject> projectChargePerson(ProjectManageQueryVo queryVo);

    PageVO<CameraResVo> projectCameras(ProjectCameraPageQueryVo queryVo);

    boolean addCamera(CameraAddVo cameraAddVo);

    boolean updateCameras(CameraAddVo cameraAddVo);

    boolean batchDeleteCameras(String cameraIndexCodes);

    List<Map<String, Object>> countCamerasByProjectType();

    List<Map<String, Object>> countCamerasByAdcd();

    PageVO<WarningTemplate> selectWarningTemplate(WarningTemplateQueryVo queryVo);

    List<WarningTemplate> listWarningTemplate(WarningTemplateQueryVo queryVo);

    boolean addWarningTemplate(WarningTemplateAddVo vo);

    boolean updateWarningTemplate(WarningTemplateAddVo vo);

    boolean batchDeleteWarningTemplate(String ids);

    PageVO<WarningConfig> warningConfigs(WarningConfigPageQueryVo queryVo);

    boolean addWarningConfig(WarningConfigAddVo warningConfigAddVo);

    boolean updateWarningConfig(WarningConfigAddVo warningConfigAddVo);

    boolean batchDeleteWarningConfig(String ids);

    PageVO<WarningRecordResVo> selectWarningRecord(WarningRecordPageQueryVo queryVo);

    List<Map<String, Object>> countByProjectType();

    JSONObject cameraStatistics();

    JSONObject cameraOutLineStatistics();

    List<OutLineCameraResVo> outLineCameras();

    List<OutLineCameraResVo> outLineCamerasOrderList();

    JSONArray outLineCamerasCountByBasin();

    JSONArray outLineCamerasCountByAdcd();

    JSONArray outLineCamerasCountByProjectType();

    JSONObject cameraStatisticByStatus();

    PageVO<RainWarningConfig> warningConfigQuery(WarningConfigQueryVo vo);

    List<WarnConfigStaInfo> staInfos(StaInfoQueryVo vo, String stCodes);

    String warnThresholdOperate(WarnThresholdOperateVo vo);

    List<DeptUserInfo> userInfos();

    String warningConfigAdd(WarningConfigAddReqVo vo);

    List<WaterStaInfoRespVo> waterStaInfos(StaInfoQueryVo vo);

    String waterWarnThresholdOperate(WaterWarnThresholdOperateVo vo);

    List<FlowStaInfoRespVo> flowStaInfos(StaInfoQueryVo vo);

    String flowWarnThresholdOperate(FlowWarnThresholdOperateVo vo);

    List<CapacityStaInfoRespVo> capacityStaInfos(StaInfoQueryVo vo);

    String capacityWarnThresholdOperate(CapacityWarnThresholdOperateVo vo);

    int warningConfigDel(String configIds, String stCodes);

    int warningConfigUpd(WarnConfigUdpReqVo vo);

    PageVO<WarnAnalyseRespVo> warnAnalysisQuery(WarningAnalyseQueryVo vo);

    int forwordWarnAnalyse(ForwordWarnAnalyseReqVo vo);

    List<AttProjBuildContent> proBuildContents(String projCode, String type);

    List<AttProjLink> proLinks(String projCode);

    List<ProjectResVo> projectOverview();

    Map<String, List<ProjectResVo>> projectInScope(List<PointEntity> list);

    Map<String, List<ProjectResVo>> projectInCircle(CircleReqVo reqVo);

    ProjectResVo projectInfoByCode(String projCode);

    /**
     * 根据请求地址 生成一个可以打开的二维码图片
     *
     * @param httpUrl  拼接了 projectCode 的请求路径
     * @param response
     */
    void getQrCodeByProjCode(String httpUrl, HttpServletResponse response) throws IOException;

    List<ChartResVo<String, Long>> countProjTypeNumByAdcdBasin(String adcd, String basin);

    Map<String, List<Object>> dikeAndSeawallLengthCount();

    Collection projectCount(String type, String stType);

    /**
     * 根据不同分类条件获取测站分析统计
     *
     * @return
     */
    List<StCountResVo> attStBaseCountByType(String type, String stType, Boolean isWrp);

    /**
     * 工程测站统计
     * 水库：无数据、不需要、正常、接近汛限、超汛限、超防洪高；
     * 河网/堰闸：无数据、正常、接近警戒、超警戒、超保证；
     * 潮位：无数据、正常、接近警戒、超警戒；
     *
     * @param type     水库/河网/堰闸/潮位
     * @param engScale 水库工程等别：大中型，小型
     * @return
     */
    Object projStatStatistic(String type, String engScale);

    /**
     * 查询病险工程
     *
     * @param reqVo
     * @return
     */
    PageVO<DilapProjResVo> dilapProjList(DilapProjReqVo reqVo);


    /**
     * 查询风险工程
     *
     * @param reqVo
     * @return
     */
    PageVO<ProjRiskResVo> projRiskList(ProjRiskReqVo reqVo);


    /**
     * 大屏海塘统计
     *
     * @return
     */
    JSONObject seawallStatsForBigScreen();

    List<Map<String, String>> haha();

    List<String> getAllProjType();

    /**
     * 保存用户选择的工程
     *
     * @param dataStr
     */
    void saveChooseProject(String dataStr);

    /**
     * 获取用户选择的工程
     *
     * @return
     */
    JSONArray getChooseProject();

    /**
     * 一张图工程详表统计
     *
     * @param type
     * @return
     */
    Map<String, Object> countProjectNum(String type);

    /**
     * 摄像头行政区划树
     * @return
     */
    Object getHikvisionTree();
}
