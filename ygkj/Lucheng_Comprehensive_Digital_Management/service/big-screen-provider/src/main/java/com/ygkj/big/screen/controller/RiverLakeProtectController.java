package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.RiverLakeProtectControllerApi;
import com.ygkj.big.screen.service.RiverLakeProtectService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
@RestController
@RequestMapping("riverLakeProtect")
public class RiverLakeProtectController implements RiverLakeProtectControllerApi {

    @Autowired
    RiverLakeProtectService riverLakeProtectService;

    @GetMapping("chiefPatrolProblemStats")
    @Override
    public CommonResult chiefPatrolProblemStats(@RequestParam("type") int type) {
        return riverLakeProtectService.chiefPatrolProblemStats(type);
    }
}
