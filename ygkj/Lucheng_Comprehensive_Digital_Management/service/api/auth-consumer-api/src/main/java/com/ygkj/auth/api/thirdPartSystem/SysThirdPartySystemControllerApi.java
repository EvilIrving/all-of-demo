package com.ygkj.auth.api.thirdPartSystem;

import com.ygkj.auth.VO.request.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

/**
 * @author Huang.zh
 * @date 2020/10/27 11:09
 * @Description:
 */
@Api(value = "SysThirdPartySystemControllerApi", tags = "第三方系统接入")
public interface SysThirdPartySystemControllerApi {

    @ApiOperation(value = "查询已经接入的第三方系统")
    CommonResult load(SysThirdPartySystemQueryVO queryVO);

    @ApiOperation(value = "接入第三方系统信息")
    CommonResult insert(@Valid SysThirdPartySystemServiceAddVO addVo);

    @ApiOperation(value = "更改已经接入的第三方系统信息")
    CommonResult update(@Valid SysThirdPartySystemServiceUpdateVO updateVo);

    @ApiOperation(value = "删除已经接入的第三方系统信息")
    CommonResult delete(String id);

    @ApiOperation(value = "申请使用第三方系统")
    CommonResult apply(@Valid SysSystemApplyVO sysSystemApplyVO);

    @ApiOperation(value = "审核用户的第三方系统使用申请")
    CommonResult examineApply(@Valid SysSystemApplyExamineVO examineVO);
}
