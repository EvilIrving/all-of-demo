package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * dts_ra_basic_data_db_water_expert
 * @author 
 */
@Data
public class DtsRaBasicDataDbWaterExpert implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String dtsCmfId;

    /**
     * 专家名称
     */
    @ApiModelProperty(value = "专家名称")
    private String name;

    /**
     * 所在单位
     */
    @ApiModelProperty(value = "所在单位")
    private String department;

    /**
     * 职务/职称
     */
    @ApiModelProperty(value = "职务/职称")
    private String title;

    /**
     * 专家类型 1堤防海塘抢险 2水闸泵站抢险
     */
    @ApiModelProperty(value = "专家类型 1堤防海塘抢险 2水闸泵站抢险")
    private Integer type;

    /**
     * 擅长方向 1防汛抢险专家 2防汛调度专家
     */
    @ApiModelProperty(value = "擅长方向 1防汛抢险专家 2防汛调度专家")
    private Integer goodAt;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String mobile;

    /**
     * 地区级别 1省级专家 2市级专家 3县级专家
     */
    @ApiModelProperty(value = "地区级别 1省级专家 2市级专家 3县级专家")
    private Integer level;

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    private String dtsCmfCreatorId;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date dtsCmfCreateTime;

    /**
     * 更新人
     */
    @ApiModelProperty(hidden = true)
    private String dtsCmfUpdatorId;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Date dtsCmfUpdateTime;

    /**
     * 归属科室
     */
    @ApiModelProperty(hidden = true)
    private String dtsCmfDept;

    /**
     * 删除标记
     */
    @ApiModelProperty(hidden = true)
    private Integer dtsCmfDeleted;

    /**
     * 数据版本
     */
    @ApiModelProperty(hidden = true)
    private Integer dtsCmfVersion;

    private static final long serialVersionUID = 1L;
}