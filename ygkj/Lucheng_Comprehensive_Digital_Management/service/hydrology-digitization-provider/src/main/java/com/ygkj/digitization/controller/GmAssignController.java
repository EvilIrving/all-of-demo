package com.ygkj.digitization.controller;

import com.ygkj.digitization.GmAssignControllerApi;
import com.ygkj.digitization.service.GmAssignService;
import com.ygkj.digitization.vo.request.AssManAddUpdateVo;
import com.ygkj.digitization.vo.request.AssManQueryVo;
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
@RequestMapping("assMan")
public class GmAssignController implements GmAssignControllerApi {

    @Autowired
    GmAssignService gmAssignService;

    @PostMapping("upperQuery")
    @Override
    public CommonResult upperQuery(AssManQueryVo queryVo) {
        return CommonResult.success(gmAssignService.upperQuery(queryVo));
    }

    @PostMapping("upperQueryOneById")
    @Override
    public CommonResult upperQueryOneById(String id) {
        return CommonResult.success(gmAssignService.upperQueryOneById(id));
    }

    @PostMapping("upperAddOrUpdate")
    @Override
    public CommonResult upperAddOrUpdate(AssManAddUpdateVo addUpdateVo) {
        if (StringUtils.isBlank(addUpdateVo.getId())){
            return CommonResult.success(gmAssignService.upperAdd(addUpdateVo));
        }else{
            return CommonResult.success(gmAssignService.upperUpdate(addUpdateVo));
        }
    }

    @PostMapping("upperDeleteById")
    @Override
    public CommonResult upperDeleteById(String id) {
        return CommonResult.success(gmAssignService.upperDeleteById(id));
    }

    @PostMapping("sendOrder")
    @Override
    public CommonResult sendOrder(String id) {
        return CommonResult.success(gmAssignService.sendOrder(id));
    }

    @PostMapping("syncTask")
    @Override
    public CommonResult syncTask(Integer year) {
        return CommonResult.success(gmAssignService.syncTask(year));
    }

    @PostMapping("alertTask")
    @Override
    public CommonResult alertTask(String id) {
        return CommonResult.success(gmAssignService.alertTask(id));
    }
}
