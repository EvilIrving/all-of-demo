package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 预警人员表
 * @date 2021-07-26
 */
@Data
public class WarnUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 移动电话
     */
    private String phone;

    /**
     * 职务
     */
    private String duty;

    /**
     * 预警部门id
     */
    private String deptId;

    /**
     * 删除标识
     */
    private String delFlag;

    public WarnUser() {
    }

}
