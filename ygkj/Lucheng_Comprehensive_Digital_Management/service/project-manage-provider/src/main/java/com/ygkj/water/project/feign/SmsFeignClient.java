package com.ygkj.water.project.feign;

import com.ygkj.project.model.SysWarnLog;
import com.ygkj.warning.model.SysSendMessageLog;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/14
 */
@FeignClient("warning-service")
public interface SmsFeignClient {
    @PostMapping("/sysWarnLog/addOrUpdate")
    CommonResult addOrUpdate(@SpringQueryMap SysWarnLog sysWarnLog);

    /**
     * 发送消息 、短信/浙政钉/站内
     *
     * @param list
     * @return
     */
    @PostMapping("/msg")
    CommonResult sendMessage(@RequestBody List<SysSendMessageLog> list);
}
