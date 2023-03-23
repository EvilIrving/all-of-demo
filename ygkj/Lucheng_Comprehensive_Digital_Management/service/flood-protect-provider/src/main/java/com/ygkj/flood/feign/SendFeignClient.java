package com.ygkj.flood.feign;

import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.warning.vo.request.DingWorkReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

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
}
