package com.ygkj.auth.api.authentication;

import com.ygkj.auth.VO.request.LoginReqVO;
import com.ygkj.auth.VO.request.ValidateTicketVo;
import com.ygkj.auth.VO.response.LoginRespVO;
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
    @ApiOperation(value = "退出当前账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name="accessToken",value="用户token",dataType="string", paramType = "query",required = true),
            @ApiImplicitParam(name="refreshToken",value="用户刷新token",dataType="string", paramType = "query",required = true),
    })
    Object logout(String accessToken,String refreshToken);

    @ApiOperation(value = "第三方登录验证接口")
    LoginRespVO validateTicket(ValidateTicketVo vo);
}
