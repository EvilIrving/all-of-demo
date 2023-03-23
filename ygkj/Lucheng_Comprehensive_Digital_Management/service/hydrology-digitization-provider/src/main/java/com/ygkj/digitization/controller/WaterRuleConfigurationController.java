package com.ygkj.digitization.controller;

import com.ygkj.digitization.service.WaterRuleConfigurationService;
import com.ygkj.digitization.WaterRuleConfigurationControllerApi;
import com.ygkj.digitization.vo.request.WaterRuleConfigurationReqVo;
import com.ygkj.digitization.vo.response.WaterRuleConfigurationRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description 水位规则配置表
 * @author hucong
 * @date 2021-07-13
 */
@RestController
@RequestMapping(value = "/WaterRuleConfiguration")
public class WaterRuleConfigurationController implements WaterRuleConfigurationControllerApi {

    @Resource
    private WaterRuleConfigurationService waterRuleConfigurationService;

    @PutMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(WaterRuleConfigurationReqVo vo) {
        if (StringUtils.isEmpty(vo.getId())) {
            waterRuleConfigurationService.insert(vo);
        } else {
            waterRuleConfigurationService.update(vo);
        }
        return CommonResult.success("操作成功!");
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = waterRuleConfigurationService.delete(id);
        if (flag > 0) {
            return CommonResult.success("删除成功!");
        } else {
            return CommonResult.success("删除失败!");
        }
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<WaterRuleConfigurationRespVo>> loadPage(WaterRuleConfigurationReqVo vo) {
        return CommonResult.success(waterRuleConfigurationService.loadPage(vo));
    }
}
