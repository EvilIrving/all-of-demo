package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 预警部门表
 * @date 2021-07-26
 */
@Data
public class WarnDept implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 父id
     */
    private String pid;

    /**
     * 删除标识
     */
    private String delFlag;

    public WarnDept() {
    }

}