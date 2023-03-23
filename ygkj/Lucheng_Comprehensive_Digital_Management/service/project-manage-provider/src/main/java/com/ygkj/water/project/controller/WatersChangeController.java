package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.WatersChange;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.WatersChangeService;
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
@RequestMapping("/watersChange")
public class WatersChangeController {

    @Autowired
    private WatersChangeService WatersChangeService;

    @ApiOperation(value = "水域变化新增或修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(WatersChange watersChange) {
        if (WatersChangeService.saveOrUpdate(watersChange)){
            return CommonResult.success();
        }
        return CommonResult.failed("新增或修改失败");
    }

    @ApiOperation(value = "水域变化删除")
    @PostMapping("/del")
    public CommonResult del(String id) {
        if (WatersChangeService.removeById(id)){
            return CommonResult.success();
        }
        return CommonResult.failed("删除失败");
    }


    @ApiOperation(value = "水域变化列表")
    @PostMapping("/list")
    public CommonResult list(WatersChange watersChange) {
        watersChange.page();
        QueryWrapper<WatersChange> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(watersChange.getReportId()), "report_id", watersChange.getReportId());
        queryWrapper.eq(watersChange.getYearTime()!= null, "year_time", watersChange.getYearTime());
        queryWrapper.orderByDesc("year_time");
        List<WatersChange> list = WatersChangeService.list(queryWrapper);
        return CommonResult.success(list);
    }

    @PostMapping("/exportList")
    public void exportList(WatersChange watersChange, HttpServletResponse response) {

        QueryWrapper<WatersChange> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(watersChange.getReportId()), "report_id", watersChange.getReportId());
        queryWrapper.eq(watersChange.getYearTime()!= null, "year_time", watersChange.getYearTime());
        queryWrapper.orderByDesc("year_time");
        List<WatersChange> list = WatersChangeService.list(queryWrapper);
        ExportUtils.exportExcel(list,
                "水域变化报表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"年度","水域面积m³","库容量","河底高程","实际岸线"},
                new String[]{"yearTime","area","capacity","bottomElevation","shoreline"},
                response, WatersChange.class);
    }
}
