package com.ygkj.big.screen.feign;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.project.model.SectionConfiguration;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.request.PustProjectReq;
import com.ygkj.project.vo.request.SeawallWarnReqVo;
import com.ygkj.project.vo.response.PustProjectResp;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/21
 */
@FeignClient(contextId = "project-manage", name = "project-manage")
public interface ProjectManageClient {

    /**
     * 十大工程查询
     *
     * @param projectManageQueryVo
     * @return
     */
    @PostMapping("projectManage/projects")
    CommonResult<List<JSONObject>> projects(@SpringQueryMap ProjectManageQueryVo projectManageQueryVo);

    /**
     * 根据流域/行政区划统计工程数量
     *
     * @param adcd
     * @param basin
     * @return
     */
    @GetMapping("projectManage/countProjTypeNumByAdcdBasin")
    CommonResult<List<LinkedHashMap>> countProjTypeNumByAdcdBasin(@RequestParam(value = "adcd", required = false) String adcd,
                                                                  @RequestParam(value = "basin", required = false) String basin);

    @GetMapping("/projectManage/dikeAndSeawallLengthCount")
    CommonResult dikeAndSeawallLengthCount();

    @GetMapping("/projectManage/projectCount")
    CommonResult projectCount(@RequestParam(value = "type") String type, @RequestParam(value = "stType", required = false) String stType);

    /**
     * 统计大中型水库档案当前年 数量 以及列表
     *
     * @return
     */
    @GetMapping("/projectManage/reservoirArchivesCount")
    CommonResult reservoirArchivesCount();


    /**
     * 统计
     *
     * @param type
     * @param stType
     * @return
     */
    @GetMapping("/projectManage/attStBaseCountByType")
    CommonResult attStBaseCountByType(@RequestParam("type") String type,
                                      @RequestParam(value = "stType", required = false) String stType,
                                      @RequestParam(value = "isWrp", required = false) Boolean isWrp);


    /**
     * 根据流域统计摄像头数量
     *
     * @return
     */
    @GetMapping("cameraOps/statsCameraByBasin")
    CommonResult<LinkedHashMap> statsCameraByBasin();

    /**
     * 根据流域统计摄像头数量
     *
     * @return
     */
    @GetMapping("cameraOps/statsCameraByAdcd")
    CommonResult<LinkedHashMap> statsCameraByAdcd();

    /**
     * 水库、测站、水位查询
     *
     * @param reqVo
     * @return
     */
    @PostMapping("projectManage/rsvrWaterLevel")
    CommonResult<PageVO<LinkedHashMap>> rsvrWaterLevel(@RequestBody RsvrStationWaterReqVo reqVo);

    /**
     * 水闸、测站、工情查询
     *
     * @param reqVo
     * @return
     */
    @PostMapping("projectManage/wagaWaterLevel")
    CommonResult<PageVO<LinkedHashMap>> wagaWaterLevel(@RequestBody WagaStationWaterReqVo reqVo);


    /**
     * 查询病险工程列表
     *
     * @param reqVo
     * @return
     */
    @GetMapping("projectManage/dilapProjList")
    CommonResult<PageVO<LinkedHashMap>> dilapProjList(@SpringQueryMap DilapProjReqVo reqVo);

    /**
     * 查询工程风险记录
     *
     * @param reqVo
     * @return
     */
    @GetMapping("projectManage/projRiskList")
    CommonResult<PageVO<LinkedHashMap>> projRiskList(@SpringQueryMap ProjRiskReqVo reqVo);

    /**
     * 海塘长度和段数统计
     *
     * @return
     */
    @GetMapping("projectManage/seawallStatsForBigScreen")
    CommonResult<JSONObject> seawallStatsForBigScreen();

    /**
     * 水库复盘统计
     *
     * @return
     */
    @GetMapping("projectManage/rsvrReplayStats")
    CommonResult rsvrReplayStats(@SpringQueryMap RsvrReplayReqVo reqVo);

    /**
     * 温瑞平水闸复盘统计
     *
     * @return
     */
    @GetMapping("projectManage/wagaReplayStats")
    CommonResult wagaReplayStats(@SpringQueryMap WagaReplayReqVo reqVo);


    /**
     * 水文工程测站概化图
     *
     * @param reqVo
     * @return
     */
    @GetMapping("projectManage/hydrProjStaDiagram")
    CommonResult hydrProjStaDiagram(@SpringQueryMap HydrDiagReqVo reqVo);

    /**
     * 水文工程测站概化图统计
     *
     * @return
     */
    @GetMapping("projectManage/hydrProjStaDiagramStatistic")
    CommonResult hydrProjStaDiagramStatistic(@RequestParam("hours") Integer hours);

    @GetMapping("projectManage/getRsvrDrillByTime")
    CommonResult<List<LinkedHashMap>> getRsvrDrillByTime(@RequestParam(value = "dataTime") String dataTime);


    @PostMapping("floodRes/listTyphoonAllRealProject")
    CommonResult listTyphoonAllRealProject(@SpringQueryMap TyphoonProjectReqVo reqVo);


    @PostMapping("floodRes/countTyphoonAllRealProject")
    CommonResult<Integer> countTyphoonAllRealProject(@SpringQueryMap TyphoonProjectReqVo reqVo);

    @GetMapping("cameraOps/selectCamera")
    CommonResult<List<LinkedHashMap>> selectCameras(@SpringQueryMap HikCameraQueryReqVo vo);

    @GetMapping("cameraOps/pageCamera")
    CommonResult<PageVO<LinkedHashMap>> pageCamera(@SpringQueryMap HikCameraQueryReqVo reqVo);

    /**
     * 水源保证列表和柱状图
     *
     * @param watSrcName 水源地名称
     * @return
     */
    @GetMapping("rsvr/rsvrWatSrcTable")
    CommonResult rsvrWatSrcTable(@RequestParam("watSrcName") String watSrcName);

    /**
     * 水源保障c位
     *
     * @return
     */
    @GetMapping("rsvr/rsvrWatSrc4CWindow")
    CommonResult rsvrWatSrc4CWindow();

    /**
     * 水厂统计
     *
     * @param type 1. 根据行政区划统计：城市、乡镇、联村、单村;
     *             2. 根据水厂规模统计：两百吨千人以下、两百吨千人以上、千吨万人、城市水厂；
     *             3. 根据水厂类型统计：城市、乡镇、联村、单村；
     *             4. 按行政区划统计: 正常供水，供水预警，水质预警，水质供水预警
     *             5. 按水厂的运行状态统计:正常供水，供水预警，水质预警，水质供水预警
     *             6. 统计日累计供水量，异常供水次数
     */
    @GetMapping("waterWorks/waterWorksStats")
    CommonResult waterWorksStats(@RequestParam("type") int type);


    @GetMapping("seawall/seawallUnitCWindow")
    CommonResult seawallUnitCWindow(@SpringQueryMap SeawallWarnReqVo reqVo);

    @GetMapping("seawall/seawallUnitWarnCard")
    CommonResult seawallUnitWarnCard(@SpringQueryMap SeawallWarnReqVo reqVo);

    @GetMapping("projectRel/selectRuntimeWarn")
    CommonResult selectRuntimeWarn(@RequestParam(value = "projectCode",required = false) String projectCode);

    @PostMapping("newProjectManage/pustProject")
    CommonResult<PageVO<PustProjectResp>> pustProject(PustProjectReq req);

    @PostMapping("rsvr/stats4RsvrUnitList")
    CommonResult stats4RsvrUnitList(com.ygkj.project.vo.request.RsvrStationWaterReqVo reqVo);

    @GetMapping("/projectManage/projectCameras/{projectCode}")
    CommonResult projectCameras(@PathVariable("projectCode") String projectCode, @RequestParam(value = "cameraName", required = false) String cameraName);

    @GetMapping("/sectionConfiguration/forecastMaxHour")
    CommonResult forecastMaxHour(@SpringQueryMap SectionConfiguration sectionConfiguration);
}
