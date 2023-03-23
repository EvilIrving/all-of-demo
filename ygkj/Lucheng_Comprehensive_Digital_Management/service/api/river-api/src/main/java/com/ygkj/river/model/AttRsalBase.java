package com.ygkj.river.model;

import java.io.Serializable;

import lombok.Data;

import java.util.Date;

/**
 * @author hucong
 * @description 水域调查数据-水库临水线数据表
 * @date 2021-07-06
 */
@Data
public class AttRsalBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    private String fid;

    /**
     * 水库编码
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
    private boolean delFlag;
    /**
     * shape_leng
     */
    private String shapeLeng;

    public AttRsalBase() {
    }

}

