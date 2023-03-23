package com.ygkj.flood.vo.response;

import lombok.Data;

@Data
public class AttTeamPersonResVo {

    private String id;

    /**
     * 队伍人员姓名
     */
    private String personName;

    /**
     * 办公电话
     */
    private String officePhone;

    /**
     * 队伍人员联系方式
     */
    private String personPhone;

    /**
     * 队伍人员职务
     */
    private String personJob;

    /**
     * 队伍id
     */
    private String teamId;
}
