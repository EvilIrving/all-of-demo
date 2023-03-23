package com.ygkj.auth.api.user;

import com.ygkj.auth.VO.response.PageVO;
import com.ygkj.auth.api.user.model.SysUser;
import com.ygkj.auth.VO.request.UpdatePasswordReqVO;
import com.ygkj.auth.VO.request.UserAddReqVO;
import com.ygkj.auth.VO.request.UserPageReqVO;
import com.ygkj.auth.VO.request.UserUpdateReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Huang.zh
 * @date 2020/8/26 10:13
 * @Description: 用户服务对外接口抽象
 */
@Api(tags = "用户信息服务")
public interface UserControllerApi {
    @ApiOperation(value = "用户详情")
    SysUser detailInfo(String id);

    @ApiOperation(value = "用户分页查询")
    PageVO<SysUser> pageInfo(UserPageReqVO userPageReqVO);

    @ApiOperation(value = "新增用户")
    Object addUser(UserAddReqVO userAddReqVO);

    @ApiOperation(value = "删除用户")
    Object deleteUser(String userIds);

    @ApiOperation(value = "更新用户")
    Object updateUser(UserUpdateReqVO userUpdateReqVO);

    @ApiOperation(value = "当前用户密码修改")
    Object updatePassword(UpdatePasswordReqVO updatePasswordReqVO);

    @ApiOperation(value = "改变指定用户的状态 1正常2锁定")
    Object changeUserStatus(String userId, Integer status);
}
