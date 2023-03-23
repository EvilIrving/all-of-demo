package com.ygkj.flood.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 抢险队伍信息
 *
 * @author huangzh 2021-01-18
 */
@Data
public class AttTeamBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 队伍类型id，关联字典表，编码为teamtype
     */
    private String typeId;

    /**
     * 队伍类型（冗余字段，避免无谓的表级联）
     */
    private String typeName;

    /**
     * 队伍详细地址
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
     * 行政区划编码
     */
    private String areaCode;

    /**
     * 行政区划名称（冗余字段，避免无谓的表级联）
     */
    private String areaName;

    /**
     * 负责人姓名
     */
    private String chargePerson;

    /**
     * 负责人联系电话
     */
    private String chargePhone;

    /**
     * 编制人数
     */
    private Integer organizationNumber;

    /**
     * 现有人数
     */
    private Integer existingNumber;

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
     * 专业性质
     */
    private String technicality;

    /**
     * 现有资产（万元）
     */
    private String existingAssets;

    /**
     * 单位性质
     */
    private String unitNature;

    /**
     * 抢险专业
     */
    private String rescueSpecialty;

    /**
     * 组织形式
     */
    private String organizationalForm;

    /**
     * 救援领域
     */
    private String rescueField;

    /**
     * 救援规模能力
     */
    private String rescueCapability;

    /**
     * 传真
     */
    private String fax;

    /**
     * 备注
     */
    private String remarks;

    public AttTeamBase() {
    }

}

