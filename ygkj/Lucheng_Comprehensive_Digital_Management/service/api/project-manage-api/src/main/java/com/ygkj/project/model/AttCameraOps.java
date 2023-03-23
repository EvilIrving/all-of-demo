package com.ygkj.project.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 摄像头运维人员表
 * @date 2021-04-13
 */
@Data
public class AttCameraOps implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 运维人员名称
     */
    private String opsName;

    /**
     * 运维人员电话
     */
    private String opsPhone;

    /**
     * 标签
     */
    private String remark;

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
    private Integer delFlag;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 通知方式：站内信，浙政钉，短信
     */
    private String notifyType;

    public AttCameraOps() {
    }

}
