package com.ygkj.water.project.feign;

import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.warning.vo.request.DingWorkReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "send-provider", name = "send-provider")
public interface SendFeignClient {

    @PostMapping("/send/submit")
    CommonResult send(MsgSendRecord msgSendRecord);

    @PostMapping("/send/synSubmit")
    CommonResult synSend(MsgSendRecord msgSendRecord);

    @PostMapping("/send/addWork")
    CommonResult addWork(DingWorkReqVo reqVo);

    @PostMapping("/send/endWork")
    CommonResult endWork(String reqId);

    @PostMapping("/send/sendDingMsg")
    CommonResult sendDingDing(MsgSendRecord msgSendRecord);

}
