package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Huangzh
 * @description 工程建设项目基础信息表
 * @date 2020-12-07
 */
@Data
public class AttProjBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目代码
     */
    private String projCode;

    /**
     * 项目名称
     */
    private String projName;

    /**
     * 位置
     */
    private String posit;

    /**
     * 建设单位
     */
    private String conUnit;

    /**
     * 年度
     */
    private String year;

    /**
     * 批复文号
     */
    private String appNum;

    /**
     * 项目类型
     */
    private String projType;

    /**
     * 计划投资
     */
    private String planFund;

    /**
     * 完成投资
     */
    private String compFund;

    /**
     * 项目负责人
     */
    private String projLegPers;

    /**
     * 状态
     */
    private String status;

    /**
     * 项目简介
     */
    private String projBrin;

    /**
     * 项目立项时间
     */
    private Date projSetDate;

    /**
     * 项目竣工时间
     */
    private Date projCompDate;

    /**
     * 项目验收时间
     */
    private Date accDate;

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
     * 精度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 流域编码
     */
    private String basinCode;

    /**
     * 行政区划编码
     */
    private String areaCode;
    /**
     * 批复文件id
     */
    private String fileIds;
    /**
     * 项目面积
     */
    private double area;

    public AttProjBase() {
    }

}
