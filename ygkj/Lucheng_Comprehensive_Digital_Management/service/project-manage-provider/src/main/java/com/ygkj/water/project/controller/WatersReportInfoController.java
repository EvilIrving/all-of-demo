package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.WatersReportInfo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.WatersReportInfoService;
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
@Api(tags = "水域报表详情数据", value = "水域报表详情数据")
@RestController
@RequestMapping("/watersReportInfo")
public class WatersReportInfoController {

    @Autowired
    private WatersReportInfoService watersReportInfoService;

    @ApiOperation(value = "水域报表详情数据新增或修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(WatersReportInfo watersReportInfo) {
        if (StringUtils.isBlank(watersReportInfo.getReportId())){
            return CommonResult.failed("必须选择一个水域");
        }
        if (StringUtils.isBlank(watersReportInfo.getId())){
            QueryWrapper<WatersReportInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("report_id",watersReportInfo.getReportId());
            int count = watersReportInfoService.count(queryWrapper);
            if(count > 0){
                return CommonResult.failed("该水域数据以及存在");
            }
        }
        if (watersReportInfoService.saveOrUpdate(watersReportInfo)){
            return CommonResult.success();
        }
       return CommonResult.failed("新增或修改失败");
    }

    @ApiOperation(value = "水域报表详情数据删除")
    @PostMapping("/del")
    public CommonResult del(String id) {
        if (watersReportInfoService.removeById(id)){
            return CommonResult.success();
        }
        return CommonResult.failed("删除失败");
    }


    @ApiOperation(value = "水域报表详情数据列表")
    @PostMapping("/list")
    public CommonResult list(WatersReportInfo watersReportInfo) {
        return CommonResult.success(watersReportInfoService.listPage(watersReportInfo));
    }

    @PostMapping("/exportList")
    public void exportList(WatersReportInfo watersReportInfo, HttpServletResponse response) {
        PageVO<WatersReportInfo> pageVO = watersReportInfoService.listPage(watersReportInfo);
        List<WatersReportInfo> list = pageVO.getList();
        ExportUtils.exportExcel(list,
                "违法占用报表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"水域名称","行政区划","经度","纬度","发现时间","违法占用行为","涉事单位（个人）信息"},
                new String[]{"name","adcdNames","longitude","latitude","findTime","illegalOccupy","unitsInvolved"},
                response, WatersReportInfo.class);
    }
}
