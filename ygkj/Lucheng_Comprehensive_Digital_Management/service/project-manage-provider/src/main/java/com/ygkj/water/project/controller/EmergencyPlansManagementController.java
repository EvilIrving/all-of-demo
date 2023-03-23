package com.ygkj.water.project.controller;

import com.ygkj.project.model.EmergencyPlansManagement;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.EmergencyPlansManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wgf
 * @date 2022/4/27 15:27
 * @Description:
 */
@Api(tags = "预案管理")
@RestController
@RequestMapping("/emergencyPlansManagement")
public class EmergencyPlansManagementController {

    @Autowired
    private EmergencyPlansManagementService emergencyPlansManagementService;

    @ApiOperation(value = "预案管理新增或修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(EmergencyPlansManagement emergencyPlansManagement) {
        if (StringUtils.isNotBlank(emergencyPlansManagement.getId())) {
            if (!emergencyPlansManagementService.updateById(emergencyPlansManagement)) {
                return CommonResult.failed("更新失败");
            }
        } else {
            if (!emergencyPlansManagementService.save(emergencyPlansManagement)) {
                return CommonResult.failed("新增失败");
            }
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "预案管理列表")
    @PostMapping("/list")
    public CommonResult list(EmergencyPlansManagement emergencyPlansManagement) {
        PageVO<EmergencyPlansManagement> list = emergencyPlansManagementService.getList(emergencyPlansManagement);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "预案管理删除")
    @GetMapping("/del")
    public CommonResult del(EmergencyPlansManagement emergencyPlansManagement) {
        emergencyPlansManagement.setDelFlag(1);
        if (!emergencyPlansManagementService.updateById(emergencyPlansManagement)) {
            return CommonResult.failed("刪除失败");
        }
        return CommonResult.success();
    }

}
