package com.ygkj.water.project.controller;

import com.ygkj.project.ProjTestCompletionControllerApi;
import com.ygkj.project.vo.request.ProjTestCompletionInsVo;
import com.ygkj.project.vo.request.ProjTestCompletionReqVo;
import com.ygkj.project.vo.response.ProjTestCompletionRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.ProjTestCompletionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description 工程监督-检测完成情况表
 * @author hucong
 * @date 2021-07-09
 */
@RestController
@RequestMapping(value = "/ProjTestCompletion")
public class ProjTestCompletionController implements ProjTestCompletionControllerApi {

    @Resource
    private ProjTestCompletionService projTestCompletionService;

    @PostMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(ProjTestCompletionInsVo vo) {
        if ("0".equals(vo.getCheckResult()) && !StringUtils.isEmpty(vo.getFileUrl())) {
            return CommonResult.failed("检测结果为合格不需要上传附件");
        }
        if (StringUtils.isEmpty(vo.getId())) {
            projTestCompletionService.insert(vo);
        } else {
            projTestCompletionService.update(vo);
        }
        return CommonResult.success("操作成功!");
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = projTestCompletionService.delete(id);
        if (flag > 0) {
            return CommonResult.success("删除成功!");
        } else {
            return CommonResult.failed("删除失败");
        }
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<ProjTestCompletionRespVo>> loadPage(ProjTestCompletionReqVo vo) {
        return CommonResult.success(projTestCompletionService.loadPage(vo));
    }
}
