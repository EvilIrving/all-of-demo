package com.ygkj.supervision;


import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @date 2021-05-17
 */
@Api(tags = "用户服务", value = "用户服务")
public interface UserControllerApi {

    /**
     * 查询所有用户
     *
     * @return
     */
//    @ApiOperation("查询所有用户 - fetchAll 为true")
//    PageVO<SysUser> getUserList(UserPageReqVO userPageReqVO);
    @ApiOperation("查询所有用户 - fetchAll 为true")
    Object getUserList(String key);


    @ApiOperation("查询包括用户的部门树")
    CommonResult getDeptTreeAboutUser(String key);


}
