package com.ygkj.platform.management.vo.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeptLoginRankingVo implements Serializable {

    /**
     * 部门名称
     */
    private String deptName;
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
