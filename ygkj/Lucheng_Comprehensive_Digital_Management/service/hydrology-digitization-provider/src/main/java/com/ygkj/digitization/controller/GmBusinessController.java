package com.ygkj.digitization.controller;

import com.ygkj.digitization.GmBusinessControllerApi;
import com.ygkj.digitization.service.GmBusinessService;
import com.ygkj.digitization.vo.request.BusManAddUpdateVo;
import com.ygkj.digitization.vo.request.BusManQueryVo;
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
@RequestMapping("busMan")
public class GmBusinessController implements GmBusinessControllerApi {
    @Autowired
    GmBusinessService businessManageService;

    @PostMapping("upperQuery")
    @Override
    public CommonResult upperQuery(BusManQueryVo queryVo) {
        return CommonResult.success(businessManageService.upperQuery(queryVo));
    }

    @PostMapping("upperQueryOneById")
    @Override
    public CommonResult upperQueryOneById(String id) {
        return CommonResult.success(businessManageService.upperQueryOneById(id));
    }

    @PostMapping("upperAddOrUpdate")
    @Override
    public CommonResult upperAddOrUpdate(BusManAddUpdateVo addUpdateVo) {
        if (StringUtils.isBlank(addUpdateVo.getId())){
            return CommonResult.success(businessManageService.upperAdd(addUpdateVo));
        }else{
            return CommonResult.success(businessManageService.upperUpdate(addUpdateVo));
        }
    }

    @PostMapping("upperDeleteById")
    @Override
    public CommonResult upperDeleteById(String id) {
        return CommonResult.success(businessManageService.upperDeleteById(id));
    }

    @PostMapping("sendOrder")
    @Override
    public CommonResult sendOrder(String id) {
        return CommonResult.success(businessManageService.sendOrder(id));
    }

    @PostMapping("syncTask")
    @Override
    public CommonResult syncTask(Integer year) {
        return CommonResult.success(businessManageService.syncTask(year));
    }

    @PostMapping("alertTask")
    @Override
    public CommonResult alertTask(String id) {
        return CommonResult.success(businessManageService.alertTask(id));
    }
}
