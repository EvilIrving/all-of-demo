package com.ygkj.consumer.auth.client.authentication;

import com.ygkj.auth.VO.request.ChooseOperatorReqVo;
import com.ygkj.auth.VO.request.ProcessCustomizePageReqVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @author: huangzh
 * @Date: 2020/10/10 15:53
 * @Description:
 */
@FeignClient(contextId = "ProcessCustomize", name = "authentication-system")
public interface ProcessCustomizeClient {

    @PostMapping("/processCustomize/queryOperator")
    Object queryOperator(@RequestBody @Valid ProcessCustomizePageReqVo vo);

    @PostMapping("/processCustomize/chooseOperator")
    Object chooseOperator(@RequestBody @Valid ChooseOperatorReqVo vo);

    @GetMapping("/processCustomize/getOperators")
    Object getOperators(@RequestParam("step") Integer step);
}
