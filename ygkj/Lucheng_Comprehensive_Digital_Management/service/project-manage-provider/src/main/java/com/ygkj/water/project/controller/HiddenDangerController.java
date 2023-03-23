package com.ygkj.water.project.controller;

import com.ygkj.project.HiddenDangerControllerApi;
import com.ygkj.project.vo.request.MtTaskNodeReqVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.HiddenDangerService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lxl
 * @create 2022-04-21 17:40
 * @description
 */
@Api(tags = "物业管理事项-隐患处理")
@RestController
@RequestMapping("/hiddenDanger")
public class HiddenDangerController implements HiddenDangerControllerApi {

    @Resource
    private HiddenDangerService hiddenDangerService;

    @Override
    @GetMapping("/getHiddenDangerDetail")
    public CommonResult getHiddenDangerDetail(String id) {

        return CommonResult.success(hiddenDangerService.getHiddenDangerDetail(id));
    }

    @Override
    @PostMapping("/implementSubmit")
    public CommonResult implementSubmit(MtTaskNodeReqVo reqVo) {

        try {
            return CommonResult.success(hiddenDangerService.implementSubmit(reqVo));
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }

    }

    @Override
    @PostMapping("/submitProgressInfo")
    public CommonResult submitProgressInfo(MtTaskNodeReqVo reqVo) {
        return CommonResult.success(hiddenDangerService.submitProgressInfo(reqVo));
    }


    @Override
    @GetMapping("/sendNoticeMsg")
    public CommonResult sendNoticeMsg(String id) {

        try {
            if (hiddenDangerService.sendNoticeMsg(id)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("提醒失败");
            }
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed("提醒失败");
    }

    @Override
    @GetMapping("/msgList")
    public CommonResult msgList() {

        return CommonResult.success(hiddenDangerService.msgList());
    }

    @Override
    @GetMapping("/msgRead")
    public CommonResult msgRead(String id) {

        if (hiddenDangerService.msgRead(id)){
            return CommonResult.success();
        }else {
            return CommonResult.failed();
        }
    }

    @Override
    @GetMapping("/hiddenAppTotal")
    public CommonResult hiddenAppTotal() {
        return CommonResult.success(hiddenDangerService.hiddenAppTotal());
    }
}
