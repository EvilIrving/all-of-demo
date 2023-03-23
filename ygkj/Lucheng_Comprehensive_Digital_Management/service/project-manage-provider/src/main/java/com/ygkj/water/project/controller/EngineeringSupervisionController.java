package com.ygkj.water.project.controller;

import com.ygkj.project.EngineeringSupervisionControllerApi;
import com.ygkj.project.vo.request.AllProjectReqVo;
import com.ygkj.project.vo.request.DelProjectReqVo;
import com.ygkj.project.vo.request.ProjectEditReqVo;
import com.ygkj.project.vo.response.AllProjectResVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.EngineeringSupervisionService;
import com.ygkj.water.project.utils.ExportUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("engineeringSupervision")
public class EngineeringSupervisionController implements EngineeringSupervisionControllerApi {

    @Resource
    private EngineeringSupervisionService engineeringSupervisionService;

    @Override
    @GetMapping("countProject")
    public CommonResult countProject(Integer type) {
        return CommonResult.success(engineeringSupervisionService.countProject(type));
    }

    @Override
    @GetMapping("listBas")
    public CommonResult listBas() {
        return CommonResult.success(engineeringSupervisionService.listBas());
    }

    @Override
    @GetMapping("listScale")
    public CommonResult listScale() {
        return CommonResult.success(engineeringSupervisionService.listScale());
    }

    @Override
    @GetMapping("listGrad")
    public CommonResult listGrad() {
        return CommonResult.success(engineeringSupervisionService.listGrad());
    }

    @Override
    @PostMapping("listProject")
    public CommonResult listProject(AllProjectReqVo allProjectReqVo) {
        return CommonResult.success(engineeringSupervisionService.pageProject(allProjectReqVo));
    }

    @Override
    @PostMapping("exportProjectList")
    public void exportProjectList(AllProjectReqVo allProjectReqVo, HttpServletResponse response) {
        List<AllProjectResVo> list = engineeringSupervisionService.listProject(allProjectReqVo);
        String projectType = allProjectReqVo.getProjectType();
        String[] heads = new String[]{"名称", "工程类型"};
        String[] cols = new String[]{"projectName", "projectType"};
        switch (projectType) {
            case "水库":
            case "水电站":
            case "泵站":
            case "水闸":
                heads = ArrayUtils.addAll(heads, "工程规模", "所属流域", "行政区划", "病险工程");
                cols = ArrayUtils.addAll(cols, "engScal", "bas", "adnm", "isSafe");
                break;
            case "堤防":
            case "海塘":
                heads = ArrayUtils.addAll(heads, "工程级别", "所属流域", "行政区划", "病险工程");
                cols = ArrayUtils.addAll(cols, "engGrad", "bas", "adnm", "isSafe");
                break;
            case "山塘":
                heads = ArrayUtils.addAll(heads, "所属流域", "行政区划", "病险工程");
                cols = ArrayUtils.addAll(cols, "bas", "adnm", "isSafe");
                break;
            case "河道":
            case "其他":
                heads = ArrayUtils.addAll(heads, "所属流域", "行政区划");
                cols = ArrayUtils.addAll(cols, "bas", "adnm");
        }
        heads = ArrayUtils.addAll(heads, "政府责任人", "水行政主管部门责任人", "主管部门（产权人）责任人",
                "管理单位责任人", "技术负责人", "巡查负责人", "关联摄像机数量");
        cols = ArrayUtils.addAll(cols, "govermentChargeName", "waterDepartmentChargeName", "competentDepartmentChargeName",
                "manageDepartmentChargeName", "technologyChargeName", "patrolChargeName", "cameraNum");
        ExportUtils.exportExcel(list,
                "工程列表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                heads, cols, response);
    }

    @Override
    @GetMapping("projectDetail")
    public CommonResult projectDetail(String projectCode) {
        return CommonResult.success(engineeringSupervisionService.projectDetail(projectCode));
    }

    @Override
    @PostMapping("editProject")
    public CommonResult editProject(ProjectEditReqVo projectEditReqVo) {
        engineeringSupervisionService.editProject(projectEditReqVo);
        return CommonResult.success("");
    }

    @Override
    @GetMapping("threeCharge")
    public CommonResult threeCharge(String projectCode) {
        return CommonResult.success(engineeringSupervisionService.threeCharge(projectCode));
    }

    @Override
    @PostMapping("delProject")
    public CommonResult delProject(DelProjectReqVo reqVo) {
        engineeringSupervisionService.delProject(reqVo);
        return CommonResult.success("");
    }


    @Override
    @PostMapping("/sendMsg")
    public CommonResult sendMsg(String phone, String content, String sendType) {

        return CommonResult.success(engineeringSupervisionService.sendMsg(phone,content,sendType));
    }
}
