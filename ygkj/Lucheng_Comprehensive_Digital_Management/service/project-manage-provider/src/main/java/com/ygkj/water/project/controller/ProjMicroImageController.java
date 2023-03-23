package com.ygkj.water.project.controller;

import com.ygkj.project.ProjMicroImageControllerApi;
import com.ygkj.project.vo.request.ProjMicroImageInsVo;
import com.ygkj.project.vo.request.ProjMicroImageReqVo;
import com.ygkj.project.vo.response.ProjMicroImageRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.ProjMicroImageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description 工程监督-微影像管理表
 * @author hucong
 * @date 2021-07-09
 */
@RestController
@RequestMapping(value = "/ProjMicroImage")
public class ProjMicroImageController implements ProjMicroImageControllerApi {

    @Resource
    private ProjMicroImageService projMicroImageService;

    @PostMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(ProjMicroImageInsVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            projMicroImageService.insert(vo);
        } else {
            projMicroImageService.update(vo);
        }
        return CommonResult.success("操作成功!");
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = projMicroImageService.delete(id);
        if (flag > 0) {
            return CommonResult.success("删除成功!");
        } else {
            return CommonResult.failed("删除失败");
        }
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult loadPage(ProjMicroImageReqVo vo) {
        return CommonResult.success(projMicroImageService.loadPage(vo));
    }

    @GetMapping("yearList")
    @Override
    public CommonResult yearList(String type) {
        return CommonResult.success(projMicroImageService.yearList(type));
    }

    @PostMapping("listData")
    @Override
    public CommonResult<PageVO<ProjMicroImageRespVo>> listData(ProjMicroImageReqVo vo) {
        return CommonResult.success(projMicroImageService.listData(vo));
    }

    @PutMapping("saveUserChoosepData")
    @Override
    public CommonResult saveUserChoosepData(String data) {
        projMicroImageService.saveUserChoosepData(data);
        return CommonResult.success();
    }

    @GetMapping("getUserChoosepData")
    @Override
    public CommonResult getUserChoosepData() {
        return CommonResult.success(projMicroImageService.getUserChoosepData());
    }


}
