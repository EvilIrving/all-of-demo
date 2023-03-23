package com.ygkj.digitization.controller;

import com.ygkj.digitization.WaterWarnInformationControllerApi;
import com.ygkj.digitization.service.WaterWarnInformationService;
import com.ygkj.digitization.vo.request.WaterWarnInformationReqVo;
import com.ygkj.digitization.vo.response.WaterWarnInformationRespVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description 水位预警信息表
 * @author hucong
 * @date 2021-07-13
 */
@RestController
@RequestMapping(value = "/WaterWarnInformation")
public class WaterWarnInformationController implements WaterWarnInformationControllerApi {

    @Resource
    private WaterWarnInformationService waterWarnInformationService;

    @PutMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(WaterWarnInformationReqVo vo) throws Exception {
        if (vo.isSendSmsFlag()) {//判断是否发送预警
            waterWarnInformationService.sendMessage(vo);
        }
        if (StringUtils.isEmpty(vo.getId())) {
            waterWarnInformationService.insert(vo);
        } else {
            waterWarnInformationService.update(vo);
        }
        return CommonResult.success("操作成功!");
    }

    @DeleteMapping("del")
    @Override
    public CommonResult del(String id) {
        int flag = waterWarnInformationService.delete(id);
        if (flag > 0) {
            return CommonResult.success("删除成功!");
        } else {
            return CommonResult.success("删除失败!");
        }
    }

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<WaterWarnInformationRespVo>> loadPage(WaterWarnInformationReqVo vo) {
        return CommonResult.success(waterWarnInformationService.loadPage(vo));
    }
}
