package com.ygkj.platform.management.vo.response;

import lombok.Data;

/**
 * 人员登录排行实体类
 */
@Data
public class UserLoginRankingVo {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 总登录次数
     */
    private String loginNum;
    /**
     * pc端登录次数
     */
    private String pcNum;
    /**
     * app登录的次数
     */
    private String appNum;

}
