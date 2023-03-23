package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SingleSignOnRequestVo {
    //浙政钉单点登录参数
    private String authCode;

    private String appKey;

    private String secretKey;

    private String plattype = "";

    private Boolean isNeedBind = false ;

    private String username;

    private String password;

    private String openid;

    @ApiModelProperty("验证码")
    private String verificationCode;

    //浙里办单点登录参数
    @ApiModelProperty("单点登录票据")
    private String ticketId;

    @ApiModelProperty("应用id")
    private String appId;

    private String token;
}
