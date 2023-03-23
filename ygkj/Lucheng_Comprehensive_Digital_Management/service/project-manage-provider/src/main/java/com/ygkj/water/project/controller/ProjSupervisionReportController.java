package com.ygkj.water.project.controller;

import com.ygkj.project.ProjSupervisionReportControllerApi;
import com.ygkj.project.vo.request.ProjSupervisionReportInsVo;
import com.ygkj.project.vo.request.ProjSupervisionReportReqVo;
import com.ygkj.project.vo.response.ProjSupervisionReportRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.ProjSupervisionReportService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description 工程监督-监理月报表
 * @author hucong
 * @date 2021-07-09
 */
@RestController
@RequestMapping(value = "/ProjSupervisionReport")
public class ProjSupervisionReportController implements ProjSupervisionReportControllerApi {

    @Resource
    private ProjSupervisionReportService projSupervisionReportService;

    @PostMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(ProjSupervisionReportInsVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            projSupervisionReportService.insert(vo);
        } else {
            projSupervisionReportService.update(vo);
        }
        return CommonResult.success("操作成功!");
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = projSupervisionReportService.delete(id);
        if (flag > 0) {
            return CommonResult.success("删除成功!");
        } else {
            return CommonResult.success("删除失败!");
        }
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<ProjSupervisionReportRespVo>> loadPage(ProjSupervisionReportReqVo vo) {
        return CommonResult.success(projSupervisionReportService.loadPage(vo));
    }
}
