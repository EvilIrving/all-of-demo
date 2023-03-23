package com.ygkj.sendprovider.controller;

import com.ygkj.sendprovider.service.DingService;
import com.ygkj.sendprovider.service.SendService;
import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.warning.vo.request.DingWorkReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("send")
@Api(tags = "消息发送")
public class SendController {

    @Resource
    private SendService sendService;

    @Resource
    private DingService dingService;

    @PostMapping("submit")
    @ApiOperation("提交发送内容")
    public CommonResult submit(@RequestBody MsgSendRecord msgSendRecord){
        return CommonResult.success(sendService.submit(msgSendRecord));
    }

    @PostMapping("synSubmit")
    @ApiOperation("同步发送")
    public CommonResult synSubmit(@RequestBody MsgSendRecord msgSendRecord){
        return CommonResult.success(sendService.synSubmit(msgSendRecord));
    }

    @PostMapping("addWork")
    @ApiOperation("新增待办")
    public CommonResult addWork(@RequestBody DingWorkReqVo reqVo){
        return CommonResult.success(dingService.addWork(reqVo));
    }

    @PostMapping("endWork")
    @ApiOperation("结束待办")
    public CommonResult endWork(@RequestBody String reqId){
        return CommonResult.success(dingService.endWork(reqId));
    }

    @PostMapping("/sendDingMsg")
    @ApiOperation("发送浙政钉消息")
    public CommonResult sendDingDing(@RequestBody MsgSendRecord msgSendRecord){
        return CommonResult.success(dingService.sendDingDing(msgSendRecord));
    }


}
