package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.gragh.vo.request.RivernetWaterLevelReqVo;
import com.ygkj.gragh.vo.response.RivernetWaterLevelResVo;
import com.ygkj.project.model.TrainingManagement;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.TrainingManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * @author wgf
 * @date 2021/11/5 11:03
 * @Description: 培训管理
 */
@Api(tags = "培训管理")
@RestController
@RequestMapping("/trainingManagement")
public class TrainingManagementController {

    @Autowired
    private TrainingManagementService trainingManagementService;

    @ApiOperation(value = "培训管理新增和修改")
    @RequestMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(TrainingManagement trainingManagement) {
        if (StringUtils.isNotBlank(trainingManagement.getId())) {
            //修改
            if (trainingManagementService.updateById(trainingManagement)) {
                return CommonResult.success();
            }
        } else {
            //新增
            trainingManagement.setId(IdWorker.get32UUID());
            if (trainingManagementService.save(trainingManagement)) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed("培训管理数据新增或修改失败");
    }

    @ApiOperation(value = "根据id删除培训管理数据")
    @RequestMapping("/delete")
    public CommonResult delete(String id) {
        if (trainingManagementService.removeById(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "根据id查询培训管理数据")
    @RequestMapping("/getById")
    public CommonResult getById(String id) {
        return CommonResult.success(trainingManagementService.getById(id));
    }

    @ApiOperation(value = "培训管理数据列表")
    @RequestMapping("/list")
    public CommonResult list(TrainingManagement trainingManagement) {
        QueryWrapper<TrainingManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(trainingManagement.getReasons()), "reasons", trainingManagement.getReasons());
        if (StringUtils.isNotBlank(trainingManagement.getStarttime()) && StringUtils.isNotBlank(trainingManagement.getEndtime())) {
            String startTime = trainingManagement.getStarttime();
            String endTime = trainingManagement.getEndtime() + " 23:59:59";
            queryWrapper.apply("tm >= '" + startTime + "' AND tm <= '" + endTime + "'");
        }
        queryWrapper.orderByDesc("creattime");
        trainingManagement.page();
        return CommonResult.success(PageUtils.getPageVO(trainingManagementService.list(queryWrapper)));
    }

    @ApiOperation(value = "培训管理数据导出")
    @RequestMapping("/exportList")
    public void exportRivernet(TrainingManagement trainingManagement, HttpServletResponse response) {
        QueryWrapper<TrainingManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(trainingManagement.getReasons()), "reasons", trainingManagement.getReasons());
        if (StringUtils.isNotBlank(trainingManagement.getStarttime()) && StringUtils.isNotBlank(trainingManagement.getEndtime())) {
            String startTime = trainingManagement.getStarttime();
            String endTime = trainingManagement.getEndtime() + " 23:59:59";
            queryWrapper.apply("tm >= '" + startTime + "' AND tm <= '" + endTime + "'");
        }
        queryWrapper.orderByDesc("creattime");
        List<TrainingManagement> managements = trainingManagementService.list(queryWrapper);

        ExportUtils.exportExcel(managements, "培训管理",
                new String[]{"培训时间", "培训地点", "培训事由", "培训人员", "培训人数"},
                new String[]{"tm", "address", "reasons", "person", "pnum"},
                response, null, Collections.emptyMap());
    }
}

