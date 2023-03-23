package com.ygkj.platform.management.vo.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginNumVo implements Serializable {

    private String loginTime;//登录时间
    private String loginNum;//登录次数

}
