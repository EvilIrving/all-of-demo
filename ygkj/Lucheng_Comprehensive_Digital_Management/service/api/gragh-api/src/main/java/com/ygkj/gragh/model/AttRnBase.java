package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xq
 * @description 河网表
 * @date 2021-05-14
 */
@Data
public class AttRnBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 河网名称
     */
    private String rnName;

    /**
     * 主测站编码
     */
    private String mainStCode;

    /**
     * 主测站名
     */
    private String mainStName;

    /**
     * 警戒水位
     */
    private BigDecimal warningLevel;

    /**
     * 保证水位
     */
    private BigDecimal guranteeLevel;

    /**
     * 最高水位
     */
    private BigDecimal highestLevel;

    /**
     * 最高时间
     */
    private Date highestDate;

    /**
     * 流域
     */
    private String bas;

    /**
     * 河网流域面积
     */
    private BigDecimal waterArea;

    /**
     * 行政区划编码
     */
    private String areaCode;

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

    /**
     * 部门id
     */
    private String deptId;

    public AttRnBase() {
    }

}
