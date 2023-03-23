package com.ygkj.digitization.controller;

import com.ygkj.digitization.GmGeneralControllerApi;
import com.ygkj.digitization.service.GmGeneralService;
import com.ygkj.digitization.vo.request.GenManAddUpdateVo;
import com.ygkj.digitization.vo.request.GenManUpperQueryVo;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/2/1
 */
@RestController
@RequestMapping("genMan")
public class GmGeneralController implements GmGeneralControllerApi {

    @Autowired
    GmGeneralService gmGeneralService;

    @PostMapping("upperQuery")
    @Override
    public CommonResult upperQuery(GenManUpperQueryVo queryVo) {
        return CommonResult.success(gmGeneralService.upperQuery(queryVo));
    }

    @PostMapping("subQuery")
    @Override
    public CommonResult subQuery(GenManUpperQueryVo queryVo) {
        // todo 这里的adcd 需要根据各区县的用户来判断行政区划，而不是要求前端来传参数
        return CommonResult.success(gmGeneralService.subQuery(queryVo));
    }

    @PostMapping("subAddAndUpdate")
    @Override
    public CommonResult subAddAndUpdate(GenManAddUpdateVo addUpdateVo) {
        if (StringUtils.isBlank(addUpdateVo.getId())) {
            return CommonResult.success(gmGeneralService.subAdd(addUpdateVo));
        } else {
            return CommonResult.success(gmGeneralService.subUpdate(addUpdateVo));
        }
    }

    @PostMapping("subDelete")
    @Override
    public CommonResult subDelete(String genManId) {
        return CommonResult.success(gmGeneralService.subDelete(genManId));
    }
}
