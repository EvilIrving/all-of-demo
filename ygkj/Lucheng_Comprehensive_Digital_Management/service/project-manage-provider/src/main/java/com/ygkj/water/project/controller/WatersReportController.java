package com.ygkj.water.project.controller;

import com.ygkj.project.model.WatersReport;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.WatersReportService;
import com.ygkj.water.project.utils.ExportUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author wgf
 * @date 2022/5/23 11:33
 * @Description:
 */
@Api(tags = "水域报表", value = "水域报表")
@RestController
@RequestMapping("/watersReport")
public class WatersReportController {

    @Autowired
    private WatersReportService watersReportService;

    @ApiOperation(value = "水域报表新增或修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(WatersReport watersReport) {
        if (watersReportService.saveOrUpdate(watersReport)){
            return CommonResult.success();
        }
       return CommonResult.failed("新增或修改失败");
    }

    @ApiOperation(value = "水域报表删除")
    @PostMapping("/del")
    public CommonResult del(String id) {
        if (watersReportService.removeById(id)){
            return CommonResult.success();
        }
        return CommonResult.failed("删除失败");
    }


    @ApiOperation(value = "水域报表列表")
    @PostMapping("/list")
    public CommonResult list(WatersReport watersReport) {
        return CommonResult.success(watersReportService.listPage(watersReport));
    }

    @PostMapping("/exportList")
    public void exportList(WatersReport watersReport, HttpServletResponse response) {

        PageVO<WatersReport> pageVO = watersReportService.listPage(watersReport);
        List<WatersReport> list = pageVO.getList();
        ExportUtils.exportExcel(list,
                "水域面积报表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"水域名称","行政区划","水域面积m³"},
                new String[]{"name","adcdNames","area"},
                response, WatersReport.class);
    }
}
