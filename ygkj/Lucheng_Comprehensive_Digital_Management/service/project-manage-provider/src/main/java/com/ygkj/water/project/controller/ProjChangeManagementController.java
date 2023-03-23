package com.ygkj.water.project.controller;

import com.ygkj.project.ProjChangeManagementControllerApi;
import com.ygkj.project.vo.request.ProjChangeManagementInsVo;
import com.ygkj.project.vo.request.ProjChangeManagementReqVo;
import com.ygkj.project.vo.response.ProjChangeManagementRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.ProjChangeManagementService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description 变更管理表
 * @author hucong
 * @date 2021-07-08
 */
@RestController
@RequestMapping(value = "/ProjChangeManagement")
public class ProjChangeManagementController implements ProjChangeManagementControllerApi {

    @Resource
    private ProjChangeManagementService projChangeManagementService;

    @PostMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(ProjChangeManagementInsVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            projChangeManagementService.insert(vo);
            return CommonResult.success("保存成功!");
        } else {
            projChangeManagementService.update(vo);
            return CommonResult.success("修改成功!");
        }
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = projChangeManagementService.delete(id);
        if (flag > 0) {
            return CommonResult.success("删除成功!");
        } else {
            return CommonResult.failed("删除失败!");
        }
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<ProjChangeManagementRespVo>> loadPage(ProjChangeManagementReqVo vo) {
        return CommonResult.success(projChangeManagementService.loadPage(vo));
    }
}