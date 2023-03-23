package com.ygkj.service;

import com.ygkj.auth.VO.request.*;
import com.ygkj.water.model.CommonResult;

/**
 * @author Huang.zh
 * @date 2020/10/27 10:36
 * @Description:
 */
public interface SysThirdPartySystemService {

    CommonResult insert(SysThirdPartySystemServiceAddVO addVo);

    CommonResult update(SysThirdPartySystemServiceUpdateVO updateVo);

    CommonResult delete(String id);

    CommonResult load(SysThirdPartySystemQueryVO queryVO);

    CommonResult applySystem(SysSystemApplyVO applyVO);

    CommonResult examineApply(SysSystemApplyExamineVO examineVO);
}
