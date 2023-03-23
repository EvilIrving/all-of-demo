package com.ygkj.auth.api.authentication;

import com.ygkj.auth.VO.request.LoginReqVO;
import com.ygkj.auth.VO.request.UpdatePasswordReqVO;
import com.ygkj.auth.VO.request.ValidateTicketVo;
import com.ygkj.auth.VO.request.SingleSignOnRequestVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author Huang.zh
 * @date 2020/8/26 9:38
 * @Description: 用户认证服务接口
 */
@Api(value = "AuthenticationControllerApi", tags = "用户认证服务")
public interface AuthenticationControllerApi {
    @ApiOperation(value = "用户登录接口")
    public Object login(LoginReqVO loginReqVO);

    @ApiOperation(value = "修改密码")
    public Object updatePassword(UpdatePasswordReqVO updatePasswordReqVO);

    @ApiOperation(value = "加密登录信息")
    Object rdspwd();

    @ApiOperation(value = "退出当前账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken", value = "用户token", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "refreshToken", value = "用户刷新token", dataType = "string", paramType = "query", required = true),
    })
    Object logout(String accessToken, String refreshToken);

    @ApiOperation(value = "第三方登录验证接口")
    CommonResult validateTicket(ValidateTicketVo vo);

    @ApiOperation(value = "浙政钉登录验证接口")
    CommonResult zzdLogin(SingleSignOnRequestVo vo );

    @ApiOperation(value = "给浙政钉用户发送绑定验证码")
    CommonResult sendZzdVerification(String phone);

    @ApiOperation(value = "浙里办支付宝、微信登录验证接口")
    CommonResult zlbLogin(SingleSignOnRequestVo vo);

    @ApiOperation(value = "浙里办APP登录验证接口")
    CommonResult zwwLogin(SingleSignOnRequestVo vo);

    @ApiOperation(value = "浙里办app跳转获取用户信息")
    CommonResult getInfoByTicket(String ticketId);
}
