package com.ygkj.flood.model;

import java.io.Serializable;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *  防汛仓库信息
 * @author huangzh 2021-01-18
 */
@Data
public class AttWarehouseBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 仓库名称
     */
    private String name;

    /**
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 行政区划名称（冗余字段，避免无谓的联表）
     */
    private String areaName;

    /**
     * 地理位置
     */
    private String geographicalPosition;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 仓库类型名称
     */
    private String type;

    /**
     * 物资价值
     */
    private String materialPrice;

    /**
     * 仓库面积(㎡）
     */
    private BigDecimal acreage;

    /**
     * 仓储条件
     */
    private String storageConditions;

    /**
     * 仓库结构
     */
    private String warehouseStructure;

    /**
     * 交通条件
     */
    private String trafficConditions;

    /**
     * 仓库物资
     */
    private String warehouseMaterials;

    /**
     * 建造时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date bulidTime;

    /**
     * 备注信息
     */
    private String remarkInformation;

    /**
     * 负责人名称
     */
    private String chargePerson;

    /**
     * 负责人联系方式
     */
    private String chargePhone;

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

    public AttWarehouseBase() {
    }

}

