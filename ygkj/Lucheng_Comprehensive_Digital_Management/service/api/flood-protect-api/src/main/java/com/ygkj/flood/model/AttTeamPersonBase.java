package com.ygkj.flood.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 *  队伍人员信息
 * @author huangzh 2021-01-18
 */
@Data
public class AttTeamPersonBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 队伍人员姓名
     */
    private String personName;

    /**
     *办公电话
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private String delFlag;

    public AttTeamPersonBase() {
    }

}

