package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.LoginReqVO;
import com.ygkj.auth.VO.request.ValidateTicketVo;
import com.ygkj.auth.VO.response.LoginRespVO;
import com.ygkj.auth.api.authentication.AuthenticationControllerApi;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * @author Huang.zh
 * @date 2020/8/26 10:17
 * @Description: 用户身份认证服务消费方
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController implements AuthenticationControllerApi {

    @Autowired
    private AuthenticationClient authenticationClient;

    @PostMapping("/login")
    @Override
    public Object login(@Valid LoginReqVO loginReqVO) {
        LoginRespVO login = authenticationClient.login(loginReqVO);
        if (StringUtils.isEmpty(login.getAccessToken())) {
            String msg = login.getMsg();
            return CommonResult.failed(StringUtils.isEmpty(msg) ? "登陆失败" : msg);
        } else {
            return CommonResult.success(login, "登录成功");
        }
    }

    @PostMapping("/logout")
    @Override
    public Object logout(String accessToken, String refreshToken) {
        return authenticationClient.logout(accessToken, refreshToken);
    }

    @PostMapping("/validateTicket")
    @Override
    public LoginRespVO validateTicket(@Valid ValidateTicketVo vo) {
        return authenticationClient.validateTicket(vo);
    }
}
