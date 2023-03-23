package com.ygkj.supervision;

import com.ygkj.supervision.model.SysOutsideUser;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;

@Api(tags = "外部人员管理接口", value = "外部人员管理接口")
public interface SysOutsideUserControllerApi {


    /**
     * 新增外部人员
     *
     * @param user
     * @return
     */
    CommonResult insertOutsideUser(SysOutsideUser user);


    /**
     * 更新外部人员内容
     *
     * @param user
     * @return
     */
    CommonResult updateOutsideUser(SysOutsideUser user);


    CommonResult OutsideUserList();


}
