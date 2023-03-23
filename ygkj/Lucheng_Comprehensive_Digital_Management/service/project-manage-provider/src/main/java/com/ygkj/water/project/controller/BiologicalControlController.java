package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.BiologicalControl;
import com.ygkj.project.model.SysFile;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.service.BiologicalControlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author wgf
 * @date 2022/4/24 10:32
 * @Description:
 */
@Api(tags = "生物防治")
@RestController
@RequestMapping("/biologicalControl")
public class BiologicalControlController {

    @Autowired
    private BiologicalControlService biologicalControlService;
    @Resource
    private SysFileMapper sysFileMapper;

    @ApiOperation(value = "生物防治新增或修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(BiologicalControl biologicalControl) {
        if (StringUtils.isNotBlank(biologicalControl.getId())) {
            if (!biologicalControlService.updateById(biologicalControl)) {
                return CommonResult.failed("更新失败");
            }
        } else {
            if (!biologicalControlService.save(biologicalControl)) {
                return CommonResult.failed("新增失败");
            }
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "生物防治列表")
    @PostMapping("/list")
    public CommonResult list(BiologicalControl biologicalControl) {
        biologicalControl.page();
        QueryWrapper<BiologicalControl> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(biologicalControl.getPrcd()), "prcd", biologicalControl.getPrcd());
        queryWrapper.eq(biologicalControl.getPtType() != null, "pt_type", biologicalControl.getPtType());
        queryWrapper.eq(biologicalControl.getMaterialDate() != null, "material_date", biologicalControl.getMaterialDate());
        queryWrapper.eq("del_flag", 0);
        queryWrapper.like(StringUtils.isNotBlank(biologicalControl.getManagementUnit()),"management_unit",biologicalControl.getManagementUnit()).or().like(StringUtils.isNotBlank(biologicalControl.getManagementUnit()),"client",biologicalControl.getClient());
        queryWrapper.orderByDesc("material_date");
        List<BiologicalControl> list = biologicalControlService.list(queryWrapper);
        for (BiologicalControl control : list) {
            if (StringUtils.isNotBlank(control.getContract())) {
                String[] split = control.getContract().split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                control.setContractFiles(sysFiles);
            }
            if (StringUtils.isNotBlank(control.getResultsReport())) {
                String[] split = control.getResultsReport().split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                control.setResultsReportFiles(sysFiles);
            }
        }
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @ApiOperation(value = "生物防治数据详情")
    @GetMapping("/getById")
    public CommonResult getById(String id) {
        BiologicalControl biologicalControl = biologicalControlService.getById(id);
        if (biologicalControl != null) {
            //查询工程名称
            String prcd = biologicalControl.getPrcd();
            String name = biologicalControlService.getByPrcdName(prcd);
            biologicalControl.setName(name);
            String contract = biologicalControl.getContract();
            String resultsReport = biologicalControl.getResultsReport();
            if (StringUtils.isNotBlank(contract)) {
                String[] split = contract.split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                biologicalControl.setContractFiles(sysFiles);
            }
            if (StringUtils.isNotBlank(resultsReport)) {
                String[] split = resultsReport.split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                biologicalControl.setResultsReportFiles(sysFiles);
            }
        }
        return CommonResult.success(biologicalControl);
    }

    @ApiOperation(value = "生物防治删除")
    @GetMapping("/del")
    public CommonResult del(BiologicalControl biologicalControl) {
        biologicalControl.setDelFlag(1);
        if (!biologicalControlService.updateById(biologicalControl)) {
            return CommonResult.failed("刪除失败");
        }
        return CommonResult.success();
    }
}
