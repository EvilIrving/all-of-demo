package com.ygkj.platform.management.vo.response;

import lombok.Data;

/**
 * 用户欢迎的系统
 */
@Data
public class UserPopularSystemVo {

    /**
     * 系统代码
     */
    String sysCode;
    /**
     * 系统名称
     */
    String sysName;
    /**
     * 路由访问的次数
     */
    Integer routeNum;


}
