package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.project.model.PracticeManagement;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.PracticeManagementService;
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
@Api(tags = "演练管理")
@RestController
@RequestMapping("/practiceManagement")
public class PracticeManagementController {

    @Autowired
    private PracticeManagementService practiceManagementService;

    @ApiOperation(value = "演练管理新增和修改")
    @RequestMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(PracticeManagement practiceManagement) {
        if (StringUtils.isNotBlank(practiceManagement.getId())) {
            //修改
            if (practiceManagementService.updateById(practiceManagement)) {
                return CommonResult.success();
            }
        } else {
            //新增
            practiceManagement.setId(IdWorker.get32UUID());
            if (practiceManagementService.save(practiceManagement)) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed("演练管理数据新增或修改失败");
    }

    @ApiOperation(value = "根据id删除演练管理数据")
    @RequestMapping("/delete")
    public CommonResult delete(String id) {
        if (practiceManagementService.removeById(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "根据id查询演练管理数据")
    @RequestMapping("/getById")
    public CommonResult getById(String id) {
        return CommonResult.success(practiceManagementService.getById(id));
    }

    @ApiOperation(value = "演练管理数据列表")
    @RequestMapping("/list")
    public CommonResult list(PracticeManagement practiceManagement) {
        QueryWrapper<PracticeManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(practiceManagement.getReasons()), "reasons", practiceManagement.getReasons());
        if (StringUtils.isNotBlank(practiceManagement.getStarttime()) && StringUtils.isNotBlank(practiceManagement.getEndtime())) {
            String startTime = practiceManagement.getStarttime();
            String endTime = practiceManagement.getEndtime() + " 23:59:59";
            queryWrapper.apply("tm >= '" + startTime + "' AND tm <= '" + endTime + "'");
        }
        queryWrapper.orderByDesc("creattime");
        practiceManagement.page();
        return CommonResult.success(PageUtils.getPageVO(practiceManagementService.list(queryWrapper)));
    }

    @ApiOperation(value = "演练管理数据列表导出")
    @RequestMapping("/exportList")
    public void exportRivernet(PracticeManagement practiceManagement, HttpServletResponse response) {
        QueryWrapper<PracticeManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(practiceManagement.getReasons()), "reasons", practiceManagement.getReasons());
        if (StringUtils.isNotBlank(practiceManagement.getStarttime()) && StringUtils.isNotBlank(practiceManagement.getEndtime())) {
            String startTime = practiceManagement.getStarttime();
            String endTime = practiceManagement.getEndtime() + " 23:59:59";
            queryWrapper.apply("tm >= '" + startTime + "' AND tm <= '" + endTime + "'");
        }
        queryWrapper.orderByDesc("creattime");
        List<PracticeManagement> list = practiceManagementService.list(queryWrapper);
        ExportUtils.exportExcel(list, "演练管理",
                new String[]{"演练时间", "演练地点", "演练事由", "参与人员"},
                new String[]{"tm", "address", "reasons", "person"},
                response, null, Collections.emptyMap());
    }
}
