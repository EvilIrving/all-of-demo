package com.ygkj.flood.model;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 山洪防治对象信息表
 * @date 2021-06-08
 */
@Data
public class AttPrevBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 防治对象编码
     */
    private String prevCode;

    /**
     * 辖区面积
     */
    private BigDecimal fldarea;

    /**
     * 总户数
     */
    private Integer htcount;

    /**
     * 总人口
     */
    private Integer pcount;

    /**
     * 经度
     */
    private BigDecimal lgtd;

    /**
     * 纬度
     */
    private BigDecimal lttd;

    /**
     * 防治对象类型:关联enu_code_name表（一般村落、重要村落）
     */
    private String prevtp;

    /**
     * 山洪灾害风险类型
     */
    private String risktp;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 数据更新时间
     */
    private Date modifyTime;

    /**
     * 同步时刻
     */
    private Date tongTime;

    /**
     * 行政区划
     */
    private String adcd;

    public AttPrevBase() {
    }

}
