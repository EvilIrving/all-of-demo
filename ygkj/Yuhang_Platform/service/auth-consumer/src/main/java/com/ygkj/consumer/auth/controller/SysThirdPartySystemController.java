package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.*;
import com.ygkj.auth.api.thirdPartSystem.SysThirdPartySystemControllerApi;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Huang.zh
 * @date 2020/10/27 11:11
 * @Description:
 */
@RestController
@RequestMapping("/thirdPartySystem")
public class SysThirdPartySystemController implements SysThirdPartySystemControllerApi {

    @Autowired
    private AuthenticationClient authenticationClient;

    @PostMapping("")
    @Override
    public CommonResult load(SysThirdPartySystemQueryVO queryVO) {
        return authenticationClient.loadSystem(queryVO);
    }

    @PutMapping("/insert")
    @Override
    public CommonResult insert(SysThirdPartySystemServiceAddVO addVo) {
        return authenticationClient.insertSystem(addVo);
    }

    @PutMapping("")
    @Override
    public CommonResult update(SysThirdPartySystemServiceUpdateVO updateVo) {
        return authenticationClient.updateSystem(updateVo);
    }

    @DeleteMapping("")
    @Override
    public CommonResult delete(String id) {
        return authenticationClient.deleteSystem(id);
    }

    @PutMapping("/apply")
    @Override
    public CommonResult apply(SysSystemApplyVO sysSystemApplyVO) {
        return authenticationClient.applySystem(sysSystemApplyVO);
    }

    @PutMapping("/examine")
    @Override
    public CommonResult examineApply(SysSystemApplyExamineVO examineVO) {
        return authenticationClient.examineApply(examineVO);
    }
}
