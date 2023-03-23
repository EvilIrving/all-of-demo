package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.LoginReqVO;
import com.ygkj.auth.VO.request.SingleSignOnRequestVo;
import com.ygkj.auth.VO.request.UpdatePasswordReqVO;
import com.ygkj.auth.VO.request.ValidateTicketVo;
import com.ygkj.auth.VO.response.LoginRespVO;
import com.ygkj.auth.api.authentication.AuthenticationControllerApi;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
            return CommonResult.failed(login.getLockMsg());
        } else {
            return CommonResult.success(login, "登录成功");
        }
    }

    @PutMapping("/updatePassword")
    @Override
    public Object updatePassword(UpdatePasswordReqVO updatePasswordReqVO) {
         return authenticationClient.updatePassword(updatePasswordReqVO);
    }

    @PostMapping("/rdspwd")
    @Override
    public Object rdspwd() {
        return authenticationClient.rdspwd();
    }

    @PostMapping("/logout")
    @Override
    public Object logout(String accessToken, String refreshToken) {
        return authenticationClient.logout(accessToken, refreshToken);
    }

    @PostMapping("/validateTicket")
    @Override
    public CommonResult validateTicket(@Valid ValidateTicketVo vo) {
        LoginRespVO login = authenticationClient.validateTicket(vo);
        if (StringUtils.isEmpty(login.getAccessToken())) {
            return CommonResult.failed("用户名或密码错误");
        } else {
            return CommonResult.success(login, "登录成功");
        }
    }

    @PostMapping("/zzdLogin")
    @Override
    public CommonResult zzdLogin(SingleSignOnRequestVo vo){
        return authenticationClient.zzdLogin(vo);
    }

    @GetMapping("/sendZzdVerification")
    @Override
    public CommonResult sendZzdVerification(String phone) {
        try{
            authenticationClient.sendZzdVerification(phone);
        }catch (Exception e){
            return CommonResult.failed("发送失败");
        }
        return CommonResult.success("发送成功");

    }

    @PostMapping("/zlbLogin")
    @Override
    public CommonResult zlbLogin(SingleSignOnRequestVo vo){
        return authenticationClient.zlbLogin(vo);
    }

    @PostMapping("/zwwLogin")
    @Override
    public CommonResult zwwLogin(SingleSignOnRequestVo vo){
        return authenticationClient.zwwLogin(vo);
    }

    @GetMapping("/getInfoByTicket")
    @Override
    public CommonResult getInfoByTicket(@RequestParam String ticketId){
        return authenticationClient.getInfoByTicket(ticketId);
    }
}
