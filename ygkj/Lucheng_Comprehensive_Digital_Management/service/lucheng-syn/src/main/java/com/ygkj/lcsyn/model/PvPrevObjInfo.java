package com.ygkj.lcsyn.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 防御对象信息表
 * @author
 * @date 2022-05-10
 */
@Data
public class PvPrevObjInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * prev_code
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
     * 防治对象类型
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
     * 行政区划编码
     */
    private String adcd;

    /**
     * 同步时间
     */
    private Date tongTime;

    /**
     * 删除标记 0未删除 1已删除
     */
    private Integer delFlag;

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

    public PvPrevObjInfo() {
    }

}

