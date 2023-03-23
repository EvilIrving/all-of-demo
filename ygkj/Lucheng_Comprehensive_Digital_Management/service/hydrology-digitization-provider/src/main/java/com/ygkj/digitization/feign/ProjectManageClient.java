package com.ygkj.digitization.feign;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.vo.request.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
    CommonResult attStBaseCountByType(@RequestParam("type") String type, @RequestParam(value = "stType", required = false) String stType);


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
     * @param reqVo
     * @return
     */
    @GetMapping("projectManage/hydrProjStaDiagram")
    CommonResult hydrProjStaDiagram(@SpringQueryMap HydrDiagReqVo reqVo);

    /**
     * 查询对应供水天数
     * @param queryVo
     * @return
     */
    @GetMapping("rsvr/rsvrWatSrcTable")
    CommonResult rsvrWatSrcTable(RsvrWatSrcQueryVo queryVo);

}
