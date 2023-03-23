package com.ygkj.gragh.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 岸线功能分区基础信息表
 * @date 2020-12-11
 */
@Data
public class AttSlfzBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岸线功能分区代码
     */
    private String slfzCode;

    /**
     * 岸线功能分区名称
     */
    private String slfzName;

    /**
     * 左下角经度
     */
    private BigDecimal lowLeftLong;

    /**
     * 左下角纬度
     */
    private BigDecimal lowLeftLat;

    /**
     * 右上角经度
     */
    private BigDecimal upRightLong;

    /**
     * 右上角纬度
     */
    private BigDecimal upRightLat;

    /**
     * 岸线功能分区范围
     */
    private String slfzRang;

    /**
     * 岸线功能分区分类
     */
    private String slfzClas;

    /**
     * 岸线功能分区长度
     */
    private BigDecimal sflzLen;

    /**
     * 岸线功能分区面积
     */
    private BigDecimal slfzArea;

    /**
     * 备注
     */
    private String note;

    /**
     * 记录生效时间
     */
    private Date effDate;

    /**
     * 记录失效时间
     */
    private Date exprDate;

    /**
     * 点位
     */
    private String points;

    public AttSlfzBase() {
    }

}

