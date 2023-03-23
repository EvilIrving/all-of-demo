package com.ygkj.river.feign;

import com.ygkj.warning.model.SysSendMessageLog;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lxl
 * @create 2021-08-13 9:33
 * @description 短信发送feign
 */
@FeignClient(contextId = "warning-service", name = "warning-service")
public interface SmsFeignClient {


    /**
     * 发送消息 、短信/浙政钉/站内
     *
     * @param list
     * @return
     */
    @PostMapping("/msg")
    CommonResult sendMessage(@RequestBody List<SysSendMessageLog> list);


}
