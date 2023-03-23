package com.ygkj.controller;

import com.ygkj.auth.VO.request.*;
import com.ygkj.service.SysThirdPartySystemService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Huang.zh
 * @date 2020/10/27 11:04
 * @Description:
 */
@RestController
@RequestMapping("/thirdPartySystem")
public class SysThirdPartySystemController {

    @Autowired
    private SysThirdPartySystemService sysThirdPartySystemService;

    @PostMapping("")
    public CommonResult load(@RequestBody SysThirdPartySystemQueryVO queryVO) {
        return sysThirdPartySystemService.load(queryVO);
    }

    @PutMapping("/insert")
    public CommonResult insert(@RequestBody SysThirdPartySystemServiceAddVO addVo) {
        return sysThirdPartySystemService.insert(addVo);
    }

    @PutMapping("")
    public CommonResult update(@RequestBody SysThirdPartySystemServiceUpdateVO updateVo) {
        return sysThirdPartySystemService.update(updateVo);
    }

    @DeleteMapping("")
    public CommonResult delete(String id) {
        return sysThirdPartySystemService.delete(id);
    }

    @PutMapping("/apply")
    public CommonResult applySystem(@RequestBody SysSystemApplyVO sysSystemApplyVO) {
        return sysThirdPartySystemService.applySystem(sysSystemApplyVO);
    }

    @PutMapping("/examine")
    public CommonResult examineApply(@RequestBody SysSystemApplyExamineVO examineVO) {
        return sysThirdPartySystemService.examineApply(examineVO);
    }


}
