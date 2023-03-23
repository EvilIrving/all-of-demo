package com.ygkj.auth.api.thirdPartSystem.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 用户申请接入系统记录表
 * @author Huangzh
 * @date 2020-10-27
 */
@Data
public class SysSystemApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 系统id
     */
    private String systemId;

    /**
     * 审核状态 0待审核 1通过 2驳回
     */
    private boolean status;

    /**
     * 是否删除(1已删除；0未删除)
     */
    private boolean deleted;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 更新人
     */
    private String updateId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    public SysSystemApply() {
    }

}
