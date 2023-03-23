package com.ygkj.digitization.controller;


import com.ygkj.digitization.ComprehensiveAnalysisControllerApi;
import com.ygkj.digitization.service.ComprehensiveAnalysisService;
import com.ygkj.digitization.vo.request.AnalysisListReqVo;
import com.ygkj.digitization.vo.request.DesignReqVo;
import com.ygkj.digitization.vo.response.DesignSoftResVo;
import com.ygkj.digitization.vo.response.ResSoftResVo;
import com.ygkj.digitization.vo.response.SeawallSoftResVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("comprehensiveAnalysis")
public class ComprehensiveAnalysisController implements ComprehensiveAnalysisControllerApi {

    @Resource
    private ComprehensiveAnalysisService comprehensiveAnalysisService;

    @Override
    @PostMapping("resSoftAnalysis")
    public CommonResult resSoftAnalysis(AnalysisListReqVo reqVo) {
        return CommonResult.success(comprehensiveAnalysisService.resSoftAnalysis(reqVo));
    }

    @Override
    @PostMapping("seawallSoftAnalysis")
    public CommonResult seawallSoftAnalysis(AnalysisListReqVo reqVo) {
        return CommonResult.success(comprehensiveAnalysisService.seawallSoftAnalysis(reqVo));
    }

    @Override
    @PostMapping("designSoftAnalysis")
    public CommonResult designSoftAnalysis(DesignReqVo reqVo) {
        return CommonResult.success(comprehensiveAnalysisService.designSoftAnalysis(reqVo));
    }

    @Override
    @PostMapping("exportResSoftAnalysis")
    public void exportResSoftAnalysis(AnalysisListReqVo reqVo, HttpServletResponse response) {
        reqVo.setFetch(true);
        PageVO<ResSoftResVo> resSoftResVoPageVO = comprehensiveAnalysisService.resSoftAnalysis(reqVo);
        List<ResSoftResVo> list = resSoftResVoPageVO.getList();
        String[] headers = new String[]{"水库名称", "所属流域", "行政区划", "实时水位（m）", "总库容（万m³）", "实时库容（万m³）", "集水面积（k㎡）", "纳蓄能力（mm）"};
        String[] cols = new String[]{"resName", "bas", "adnm", "rz", "totCap", "currentCap", "watShedArea", "storageCap"};
        ExportUtils.exportExcel(list, "水库安全分析" + getTime(), headers, cols, response);
    }

    private String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return dtf.format(LocalDateTime.now());
    }

    @Override
    @PostMapping("exportSeawallSoftAnalysis")
    public void exportSeawallSoftAnalysis(AnalysisListReqVo reqVo, HttpServletResponse response) {
        reqVo.setFetch(true);
        PageVO<SeawallSoftResVo> pageVO = comprehensiveAnalysisService.seawallSoftAnalysis(reqVo);
        List<SeawallSoftResVo> list = pageVO.getList();
        String[] headers = new String[]{"海塘名称", "所属流域", "行政区划", "关联潮位站名称", "关联潮位站实时水位（m）", "海塘起点坝顶高程（m）", "海塘终点坝顶高程（m）", "海塘最低点与潮位高差（m）"};
        String[] cols = new String[]{"seawallName", "bas", "adnm", "stName", "tdz", "startCrestAltitude", "finalCrestAltitude", "difTide"};
        ExportUtils.exportExcel(list, "海塘安全分析" + getTime(), headers, cols, response);
    }

    @Override
    @PostMapping("exportDesignSoftAnalysis")
    public void exportDesignSoftAnalysis(DesignReqVo reqVo, HttpServletResponse response) {
        reqVo.setFetch(true);
        PageVO<DesignSoftResVo> pageVO = comprehensiveAnalysisService.designSoftAnalysis(reqVo);
        List<DesignSoftResVo> list = pageVO.getList();
        String[] headers = new String[]{"工程类型", "工程名称", "所属流域", "行政区划", "设计标准（年）"};
        String[] cols = new String[]{"projectType", "projectName", "bas", "adnm", "designStandard"};
        ExportUtils.exportExcel(list, "设计安全分析" + getTime(), headers, cols, response);
    }

    @GetMapping("wenzhouCloudData")
    @Override
    public CommonResult wenzhouCloudData() {
        return CommonResult.success(comprehensiveAnalysisService.wenzhouCloudData());
    }

    @GetMapping("wenzhouRadarData")
    @Override
    public CommonResult wenzhouRadarData() {
        return CommonResult.success(comprehensiveAnalysisService.wenzhouRadarData());
    }
}
