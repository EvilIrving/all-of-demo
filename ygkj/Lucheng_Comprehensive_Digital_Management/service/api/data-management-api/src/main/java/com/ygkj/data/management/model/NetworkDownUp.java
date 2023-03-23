package com.ygkj.data.management.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 网络io表
 * @date 2021-07-17
 */
@Data
public class NetworkDownUp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上行速率(kb/s)
     */
    private BigDecimal rxPercent;

    /**
     * 下行速率(kb/s)
     */
    private BigDecimal txPercent;

    public NetworkDownUp() {
    }

}
