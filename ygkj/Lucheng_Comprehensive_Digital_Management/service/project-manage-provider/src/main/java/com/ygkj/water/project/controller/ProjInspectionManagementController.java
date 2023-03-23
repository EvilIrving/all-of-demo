package com.ygkj.water.project.controller;

import com.ygkj.project.ProjInspectionManagementControllerApi;
import com.ygkj.project.vo.request.ProjInspectionManagementInsReqVo;
import com.ygkj.project.vo.request.ProjInspectionManagementReqVo;
import com.ygkj.project.vo.response.ProjInspectionManagementRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.ProjInspectionManagementService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description 检查管理
 * @author hucong
 * @date 2021-07-08
 */
@RestController
@RequestMapping(value = "/ProjInspectionManagement")
public class ProjInspectionManagementController implements ProjInspectionManagementControllerApi {

    @Resource
    private ProjInspectionManagementService projInspectionManagementService;

    @PostMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(ProjInspectionManagementInsReqVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            projInspectionManagementService.insert(vo);
            return CommonResult.success("新增成功");
        } else {
            projInspectionManagementService.update(vo);
            return CommonResult.success("修改成功");
        }
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = projInspectionManagementService.delete(id);
        if (flag >0) {
            return CommonResult.success("删除成功!");
        } else {
            return CommonResult.failed("删除失败!");
        }
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<ProjInspectionManagementRespVo>> loadPage(ProjInspectionManagementReqVo vo) {
        return CommonResult.success(projInspectionManagementService.loadPage(vo));
    }
}