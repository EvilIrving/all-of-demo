package com.ygkj.warning.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 感知数据汇聚-设备表
 *
 * @author xq 2021-03-15
 */
@Data
public class PdaEquip implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 设备编码
     */
    private String eqcd;

    /**
     * 设备名称
     */
    private String eqnm;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 协议id
     */
    private String protId;

    /**
     * 协议名称
     */
    private String protName;

    /**
     * 运维单位id
     */
    private String opsId;

    /**
     * 最后数据时间
     */
    private Date lastDataTime;

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
    private int delFlag;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 设备类型 1:雨量 2:水库水情表-水位 3:河道水情表-水位 4: 闸位 5:潮位
     */
    private String equipType;

    /**
     * 插值补偿：0不补偿；1：补偿
     */
    private String missComplete;
    /**
     * 数据频次,单位为s
     */
    private Integer frequency;

    /**
     * 负责人电话
     */
    private String chargePhone;

    public PdaEquip() {
    }

}

