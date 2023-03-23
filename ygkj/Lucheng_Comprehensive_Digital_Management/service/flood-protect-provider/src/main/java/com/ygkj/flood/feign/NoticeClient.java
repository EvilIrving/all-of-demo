package com.ygkj.flood.feign;

import com.ygkj.warning.model.SysSendMessageLog;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "warning-service")
public interface NoticeClient {
    //统一发送信息接口
    @PostMapping("/msg")
    CommonResult sendMessage(@RequestBody List<SysSendMessageLog> list);
}