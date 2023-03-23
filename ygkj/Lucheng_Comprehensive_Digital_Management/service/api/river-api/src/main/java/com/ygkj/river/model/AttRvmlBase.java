package com.ygkj.river.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;

/**
 * @author hucong
 * @description 水域调查数据-河道管理范围线数据表
 * @date 2021-07-06
 */
@Data
public class AttRvmlBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    private String fid;

    /**
     * 河道（段）编码
     */
    private String code;

    /**
     * geometry
     */
    private String geometry;

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
    private boolean delFlag;

    public AttRvmlBase() {
    }

}

