package com.ygkj.river.controller;

import com.ygkj.river.AttBlueProjectControllerApi;
import com.ygkj.river.model.AttBlueProject;
import com.ygkj.river.service.AttBlueProjectService;
import com.ygkj.river.service.AttWaterAreaService;
import com.ygkj.river.service.RiverService;
import com.ygkj.river.vo.request.AttBlueProjectRecVo;
import com.ygkj.river.vo.request.AttBlueProjectReqVo;
import com.ygkj.river.vo.request.RiverInfoPageQueryVo;
import com.ygkj.river.vo.response.AttBlueProjectResVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2021-08-06 9:24
 * @description 蓝线监管工程
 */
@RestController
@RequestMapping(value = "/blueProj")
public class AttBlueProjectController implements AttBlueProjectControllerApi {


    private AttBlueProjectService attBlueProjectService;
    private RiverService riverService;

    @Resource
    private AttWaterAreaService attWaterAreaService;

    public AttBlueProjectController(@Autowired AttBlueProjectService attBlueProjectService,
                                    @Autowired RiverService riverService) {
        this.attBlueProjectService = attBlueProjectService;
        this.riverService = riverService;
    }

    @Override
    @GetMapping("/getRiver")
    public CommonResult getRiverByAreaCode(String areaCode) {
        RiverInfoPageQueryVo queryVo = new RiverInfoPageQueryVo();
        queryVo.setAreaCode(areaCode);
        return CommonResult.success(riverService.riverPageQuery(queryVo));
    }

    @Override
    @PostMapping("/addProject")
    public CommonResult addProject(AttBlueProjectRecVo attBlueProject) {
        try {
            if (attBlueProjectService.addProject(attBlueProject)){
//                attWaterAreaService.UpdateWwaterBodyData(attBlueProject);
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @Override
    @PostMapping("/projectList")
    public CommonResult<PageVO<AttBlueProjectResVo>> projectList(AttBlueProjectReqVo reqVo) {

        return CommonResult.success(attBlueProjectService.projectList(reqVo));
    }

    @Override
    @PostMapping("/updateProject")
    public CommonResult updateProject(AttBlueProject attBlueProject) {
        try {
//            attWaterAreaService.UpdateWwaterBodyDataTwo(attBlueProject,"1",null);
            if (attBlueProjectService.updateProject(attBlueProject)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }


    @Override
    @GetMapping("/delete")
    public CommonResult deleteProjectById(String id) {
        try {
//            attWaterAreaService.UpdateWwaterBodyDataTwo(null,"2",id);
            if (attBlueProjectService.deleteProjectById(id)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }
    }

    @Override
    @GetMapping("/detail")
    public CommonResult projectDetail(String projectId) {
        return CommonResult.success(attBlueProjectService.projectDetail(projectId));
    }

    @Override
    @GetMapping("/homeOverView")
    public CommonResult homeOverView() {
        return CommonResult.success(attBlueProjectService.homeOverView());
    }

    @Override
    @GetMapping("/projectStaticsForHome")
    public CommonResult projectStaticsForHome() {
        return CommonResult.success(attBlueProjectService.projectStaticsForHome());
    }

    @Override
    @GetMapping("/homeOverViewByAdcd")
    public CommonResult homeOverViewByAdcd() {
        return CommonResult.success(attBlueProjectService.homeOverViewByAdcd());
    }

    @GetMapping("projectStatisByBasin")
    @Override
    public CommonResult projectStatisByBasin() {
        return CommonResult.success(attBlueProjectService.projectStatisByBasin());
    }

    @GetMapping("projectStatisByRiverLevel")
    @Override
    public CommonResult projectStatisByRiverLevel() {
        return CommonResult.success(attBlueProjectService.projectStatisByRiverLevel());
    }

    @GetMapping("projectStatisByAdcd")
    @Override
    public CommonResult projectStatisByAdcd() {
        return CommonResult.success(attBlueProjectService.projectStatisByAdcd());
    }

    @GetMapping("projectStatisByProjectStage")
    @Override
    public CommonResult projectStatisByProjectStage() {
        return CommonResult.success(attBlueProjectService.projectStatisByProjectStage());
    }

    @Override
    @PostMapping("/exportDetailTable")
    public void exportDetailTable(AttBlueProjectReqVo reqVo, HttpServletResponse response) {
        attBlueProjectService.exportDetailTable(reqVo,response);
    }


    @Override
    @GetMapping("/inspectionNum")
    public CommonResult projectInspectionNum(Integer year) {
        return CommonResult.success(attBlueProjectService.projectInspectionNum(year));
    }

    @Override
    @GetMapping("/problemNum")
    public CommonResult projectProblemNum() {
        return CommonResult.success(attBlueProjectService.projectProblemNum());
    }


    @Override
    @GetMapping("/finishRateAndNumber")
    public CommonResult inspectionFinishRateAndNumber() {

        return CommonResult.success(attBlueProjectService.inspectionFinishRateAndNumber());
    }
}
