package com.ygkj.digitization.controller;

import com.ygkj.digitization.WaterWarnTemplateControllerApi;
import com.ygkj.digitization.service.WaterWarnTemplateService;
import com.ygkj.digitization.vo.request.WaterWarnTemplateReqVo;
import com.ygkj.digitization.vo.response.WaterWarnTemplateRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description 水位预警模板表
 * @author hucong
 * @date 2021-07-13
 */
@RestController
@RequestMapping(value = "/WaterWarnTemplate")
public class WaterWarnTemplateController implements WaterWarnTemplateControllerApi {

    @Resource
    private WaterWarnTemplateService waterWarnTemplateService;

    @PutMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(WaterWarnTemplateReqVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            waterWarnTemplateService.insert(vo);
        } else {
            waterWarnTemplateService.update(vo);
        }
        return CommonResult.success("操作成功!");
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = waterWarnTemplateService.delete(id);
        if (flag > 0) {
            return CommonResult.success("删除成功!");
        } else {
            return CommonResult.success("删除失败!");
        }
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<WaterWarnTemplateRespVo>> loadPage(WaterWarnTemplateReqVo vo) {
        return CommonResult.success(waterWarnTemplateService.loadPage(vo));
    }
}
