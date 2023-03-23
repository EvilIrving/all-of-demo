package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.ChooseOperatorReqVo;
import com.ygkj.auth.VO.request.ProcessCustomizePageReqVo;
import com.ygkj.auth.api.process.ProcessCustomizeControllerApi;
import com.ygkj.consumer.auth.client.authentication.ProcessCustomizeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: fml
 * @Date: 2020/10/10 15:50
 * @Description:
 */
@RestController
@RequestMapping("/processCustomize")
public class ProcessCustomizeController implements ProcessCustomizeControllerApi {

    @Autowired
    private ProcessCustomizeClient customizeClient;

    @Override
    @PostMapping("/queryOperator")
    public Object queryOperator(ProcessCustomizePageReqVo vo) {
        return customizeClient.queryOperator(vo);
    }

    @Override
    @PostMapping("/chooseOperator")
    public Object chooseOperator(ChooseOperatorReqVo vo) {
        return customizeClient.chooseOperator(vo);
    }

    @Override
    @GetMapping("/getOperators")
    public Object getOperators(@RequestParam("step") Integer step) {
        return customizeClient.getOperators(step);
    }
}
