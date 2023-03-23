package com.ygkj.data.management.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author java.devtools.cn
 * @description 标段表
 * @date 2021-09-08
 */
@Data
public class AttBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 工程编码
     */
    private String projectCode;

    /**
     * 标段名称
     */
    private String blockName;

    /**
     * 是否授权 1：不授权 2：授权
     */
    private String authoriza;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private String creatorId;

    public AttBlock() {
    }

}
