package com.ygkj.water.project.controller;

import com.ygkj.project.NewProjectManageControllerApi;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.NewProjectManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/newProjectManage")
public class NewProjectManageController implements NewProjectManageControllerApi {

    @Autowired
    private NewProjectManageService newProjectManageService;

    @PostMapping("reservoirProject")
    @Override
    public CommonResult<PageVO<ReservoirProjectResp>> reservoirProject(ReservoirProjectReq vo) {
        return CommonResult.success(newProjectManageService.reservoirProject(vo));
    }

//    @PostMapping("reservoirProjectStatic")
//    @Override
//    public CommonResult<List<StatisticsReservoirTypeResp>> reservoirProjectStatic(ReservoirProjectReq vo) {
//        return CommonResult.success(newProjectManageService.reservoirProjectStatic(vo));
//    }

//    @PostMapping("reservoirProjectAreaStatic")
//    @Override
//    public CommonResult<List<ProjectAreaStatic>> reservoirProjectAreaStatic(String areaCode) {
//        return CommonResult.success(newProjectManageService.reservoirProjectAreaStatic(areaCode));
//    }

    @PostMapping("seawallProject")
    @Override
    public CommonResult<PageVO<SeawallProjectResp>> seawallProject(SeawallProjectReq vo) {
        return CommonResult.success(newProjectManageService.seawallProject(vo));
    }

//    @PostMapping("seawallProjectStatic")
//    @Override
//    public CommonResult<List<StatisticsSeawallTypeResp>> seawallProjectStatic(SeawallProjectReq vo) {
//        return CommonResult.success(newProjectManageService.seawallProjectStatic(vo));
//    }

//    @PostMapping("seawallProjectAreaStatic")
//    @Override
//    public CommonResult<List<ProjectAreaStatic>> seawallProjectAreaStatic(String areaCode) {
//        return CommonResult.success(newProjectManageService.seawallProjectAreaStatic(areaCode));
//    }

    @PostMapping("dikeProject")
    @Override
    public CommonResult<PageVO<DikeProjectResp>> dikeProject(DikeProjectReq vo) {
        return CommonResult.success(newProjectManageService.dikeProject(vo));
    }

    @PostMapping("exportDikeProject")
    @Override
    public void exportDikeProject(DikeProjectReq vo, HttpServletResponse httpServletResponse) {
        newProjectManageService.exportDikeProject(vo,httpServletResponse);
    }

    //    @PostMapping("dikeProjectStatic")
//    @Override
//    public CommonResult<List<StatisticsSeawallTypeResp>> dikeProjectStatic(DikeProjectReq vo) {
//        return CommonResult.success(newProjectManageService.dikeProjectStatic(vo));
//    }

    @PostMapping("wagaProject")
    @Override
    public CommonResult<PageVO<WagaProjectResp>> wagaProject(WagaProjectReq vo) {
        return CommonResult.success(newProjectManageService.wagaProject(vo));
    }

//    @PostMapping("wagaProjectStatic")
//    @Override
//    public CommonResult<List<StatisticsWagaTypeResp>> wagaProjectStatic(WagaProjectReq vo) {
//        return CommonResult.success(newProjectManageService.wagaProjectStatic(vo));
//    }

    @PostMapping("irrProject")
    @Override
    public CommonResult<PageVO<IrrProjectResp>> IrrProject(IrrProjectReq vo) {
        return CommonResult.success(newProjectManageService.irrProject(vo));
    }

//    @PostMapping("irrProjectStatic")
//    @Override
//    public CommonResult<List<StatisticsIrrTypeResp>> IrrProjectStatic(IrrProjectReq vo) {
//        return CommonResult.success(newProjectManageService.irrProjectStatic(vo));
//    }

    @PostMapping("gateProject")
    @Override
    public CommonResult<PageVO<GateProjectResp>> gateProject(GateProjectReq vo) {
        return CommonResult.success(newProjectManageService.gateProject(vo));
    }

//    @PostMapping("gateProjectStatic")
//    @Override
//    public CommonResult<List<StatisticsGateTypeResp>> gateProjectStatic(GateProjectReq vo) {
//        return CommonResult.success(newProjectManageService.gateProjectStatic(vo));
//    }

    @PostMapping("pustProject")
    @Override
    public CommonResult<PageVO<PustProjectResp>> pustProject(PustProjectReq vo) {
        return CommonResult.success(newProjectManageService.pustProject(vo));
    }

    @PostMapping("exportPustProject")
    @Override
    public void exportPustProject(PustProjectReq vo, HttpServletResponse httpServletResponse) {
        newProjectManageService.exportPustProject(vo, httpServletResponse);
    }

    @PostMapping("mnpdProject")
    @Override
    public CommonResult<PageVO<AttMnpdBaseReqVo>> mnpdProject(MnpdProjectReq vo) {
        return CommonResult.success(newProjectManageService.mnpdProject(vo));
    }

    @PostMapping("exportMnpdProject")
    @Override
    public void exportMnpdProject(HttpServletResponse response, MnpdProjectReq reqVo) {
        newProjectManageService.exportMnpdProject(response,reqVo);
    }

    //    @PostMapping("pustProjectStatic")
//    @Override
//    public CommonResult<List<StatisticsGateTypeResp>> pustProjectStatic(PustProjectReq vo) {
//        return CommonResult.success(newProjectManageService.pustProjectStatic(vo));
//    }

    @PostMapping("countProject")
    @Override
    public CommonResult<List<DictResVo>> countProject(String areaCode) {
        return CommonResult.success(newProjectManageService.countProject(areaCode));
    }

    @PostMapping("projectEngscalStatic")
    @Override
    public CommonResult projectEngscalStatic(String type,String areaCode) {
        return CommonResult.success(newProjectManageService.projectEngscalStatic(type,areaCode));
    }

    @GetMapping("statisticalProjectNum")
    @Override
    public CommonResult statisticalProjectNum(String type) {
        return CommonResult.success(newProjectManageService.statisticalProjectNum(type));
    }

    @GetMapping("statisticalDiskNumByArea")
    @Override
    public CommonResult statisticalNumByArea(String type) {
        return CommonResult.success(newProjectManageService.statisticalNumByArea(type));
    }

    @GetMapping("cAStatisticalProject")
    @Override
    public CommonResult cAStatisticalProject(String type) {
        return CommonResult.success(newProjectManageService.cAStatisticalProject(type));
    }

//    @PostMapping("dikeProjectAreaStatic")
//    @Override
//    public CommonResult<List<ProjectAreaStatic>> dikeProjectAreaStatic(String areaCode) {
//        return CommonResult.success(newProjectManageService.dikeProjectAreaStatic(areaCode));
//    }
//
//    @PostMapping("wagaProjectAreaStatic")
//    @Override
//    public CommonResult<List<ProjectAreaStatic>> wagaProjectAreaStatic(String areaCode) {
//        return CommonResult.success(newProjectManageService.wagaProjectAreaStatic(areaCode));
//    }
//
//    @PostMapping("irrProjectAreaStatic")
//    @Override
//    public CommonResult<List<ProjectAreaStatic>> irrProjectAreaStatic(String areaCode) {
//        return CommonResult.success(newProjectManageService.irrProjectAreaStatic(areaCode));
//    }
//
//    @PostMapping("gateProjectAreaStatic")
//    @Override
//    public CommonResult<List<ProjectAreaStatic>> gateProjectAreaStatic(String areaCode) {
//        return CommonResult.success(newProjectManageService.gateProjectAreaStatic(areaCode));
//    }
//
//    @PostMapping("pustProjectAreaStatic")
//    @Override
//    public CommonResult<List<ProjectAreaStatic>> pustProjectAreaStatic(String areaCode) {
//        return CommonResult.success(newProjectManageService.pustProjectAreaStatic(areaCode));
//    }


    @GetMapping("irrStats4BigScreen")
    @Override
    public CommonResult irrStats4BigScreen(@RequestParam("type") int type) {
        return CommonResult.success(newProjectManageService.irrStats4BigScreen(type));
    }

    @GetMapping("gateStats4BigScreen")
    @Override
    public CommonResult gateStats4BigScreen(@RequestParam("type") int type) {
        return CommonResult.success(newProjectManageService.gateStats4BigScreen(type));
    }

    @PostMapping("exportGateProject")
    @Override
    public void exportGateProject(HttpServletResponse response, GateQueryReqVo reqVo) {
        newProjectManageService.exportGateProject(response,reqVo);
    }

    @PostMapping("exportIrrProject")
    @Override
    public void exportIrrProject(HttpServletResponse response, IrrQueryReqVo reqVo) {
        newProjectManageService.exportIrrProject(response,reqVo);
    }
}
