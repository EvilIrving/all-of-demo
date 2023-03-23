package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.DataMaintenance;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.DataMaintenanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wgf
 * @date 2022/4/24 10:32
 * @Description:
 */
@Api(tags = "数据维护")
@RestController
@RequestMapping("/dataMaintenance")
public class DataMaintenanceController {

    @Autowired
    private DataMaintenanceService dataMaintenanceService;

    @ApiOperation(value = "数据维护新增或修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(DataMaintenance dataMaintenance) {
        if (StringUtils.isNotBlank(dataMaintenance.getId())) {
            if (!dataMaintenanceService.updateById(dataMaintenance)) {
                return CommonResult.failed("更新失败");
            }
        } else {
            if (!dataMaintenanceService.save(dataMaintenance)) {
                return CommonResult.failed("新增失败");
            }
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "数据维护列表")
    @PostMapping("/list")
    public CommonResult list(DataMaintenance dataMaintenance) {
        dataMaintenance.page();
        QueryWrapper<DataMaintenance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(dataMaintenance.getPrcd()), "prcd", dataMaintenance.getPrcd());
        queryWrapper.eq(dataMaintenance.getMaterialDate() != null, "material_date", dataMaintenance.getMaterialDate());
        queryWrapper.like(StringUtils.isNotBlank(dataMaintenance.getMaintainUnit()),"maintain_unit",dataMaintenance.getMaintainUnit());
        queryWrapper.eq("del_flag", 0);
        queryWrapper.orderByDesc("material_date");
        List<DataMaintenance> list = dataMaintenanceService.list(queryWrapper);
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @ApiOperation(value = "数据维护数据详情")
    @GetMapping("/getById")
    public CommonResult getById(String id) {
        DataMaintenance dataMaintenance = dataMaintenanceService.getById(id);
        if (dataMaintenance != null) {
            //查询工程名称
            String prcd = dataMaintenance.getPrcd();
            String name = dataMaintenanceService.getByPrcdName(prcd);
            dataMaintenance.setName(name);
        }
        return CommonResult.success(dataMaintenance);
    }

    @ApiOperation(value = "数据维护删除")
    @GetMapping("/del")
    public CommonResult del(DataMaintenance dataMaintenance) {
        dataMaintenance.setDelFlag(1);
        if (!dataMaintenanceService.updateById(dataMaintenance)) {
            return CommonResult.failed("刪除失败");
        }
        return CommonResult.success();
    }
}
