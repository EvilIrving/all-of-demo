package com.ygkj.river.controller;

import com.ygkj.river.RiverPatrolCameraControllerApi;
import com.ygkj.river.model.RiverPatrolCameraEvent;
import com.ygkj.river.service.RiverPatrolCameraService;
import com.ygkj.river.vo.request.RpcEventEditReqVo;
import com.ygkj.river.vo.request.RpcEventQueryReqVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/8
 */
@RestController
@RequestMapping("riverPatrolCamera")
public class RiverPatrolCameraController implements RiverPatrolCameraControllerApi {

    @Autowired
    RiverPatrolCameraService riverPatrolCameraService;

    @GetMapping("cameraEventPage")
    @Override
    public CommonResult cameraEventPage(RpcEventQueryReqVo reqVo) {
        return CommonResult.success(riverPatrolCameraService.cameraEventPage(reqVo));
    }

    @PostMapping("addOrUpdateCameraEvent")
    @Override
    public CommonResult addOrUpdateCameraEvent(@RequestBody RpcEventEditReqVo reqVo) {
        if (reqVo == null) {
            return CommonResult.failed("0");
        }
        if (StringUtils.isBlank(reqVo.getId())) {
            return CommonResult.success(riverPatrolCameraService.addCameraEvent(reqVo));
        } else {
            return CommonResult.success(riverPatrolCameraService.updateCameraEvent(reqVo));
        }
    }

    @PostMapping("delCameraEvent")
    @Override
    public CommonResult delCameraEvent(@RequestParam("id") String id) {
        return CommonResult.success(riverPatrolCameraService.delCameraEvent(id));
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("getOneCameraEvent")
    @Override
    public CommonResult getOneCameraEvent(@RequestParam("id") String id) {
        return CommonResult.success(riverPatrolCameraService.getOneCameraEvent(id));
    }

    /**
     * 导出视频巡河事件列表
     *
     * @param vo
     * @param response
     */
    @Override
    @PostMapping("/exportCameraEvent")
    public void exportCameraEvent(RpcEventQueryReqVo vo, HttpServletResponse response) {

        List<RiverPatrolCameraEvent> list = riverPatrolCameraService.selectForExport(vo);
        ExportUtils.exportExcel(list,
                "视频巡河列表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"摄像头名称", "抓拍方式", "抓拍时间", "河道名称", "行政区划", "所属流域", "事件类型", "事件描述"},
                new String[]{"cameraName", "captureMode", "eventTime", "riverName", "adnm", "riverBas", "eventType", "eventDesc"},
                response, RiverPatrolCameraEvent.class);
    }
}
