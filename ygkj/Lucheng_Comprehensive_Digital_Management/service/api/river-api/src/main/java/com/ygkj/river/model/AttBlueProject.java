package com.ygkj.river.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lxl
 * @create 2021-08-05 21:15
 * @description 蓝线河道监管项目表
 */
@Data
@ApiModel("蓝线河道监管项目")
public class AttBlueProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String projectName;

    /**
     * 项目编号
     */
    @ApiModelProperty("项目编号")
    private String projectCode;

    /**
     * 年份
     */
    @ApiModelProperty("项目年份")
    private Integer year;

    /**
     * 行政区划
     */
    @ApiModelProperty("所属行政区划编码")
    private String adcd;

    @ApiModelProperty("所属行政区划名称")
    private String adnm;

    /**
     * 所属河流
     */
    @ApiModelProperty("所属河流名称")
    private String riverName;

    /**
     * 所属河流id
     */
    @ApiModelProperty("所属河流id")
    private String riverId;

    /**
     * 地址
     */
    @ApiModelProperty("所在地址名称")
    private String address;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String unitName;

    /**
     * 法人代表
     */
    @ApiModelProperty("法人代表")
    private String legalRepresent;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String phone;

    /**
     * 涉及水域名称
     */
    @ApiModelProperty("涉及水域名称")
    private String involvedWaterName;

    /**
     * 占用水域时间
     */
    @ApiModelProperty("占用水域时间")
    private String occupyWaterTime;

    /**
     * 占用水域面积
     */
    @ApiModelProperty("占用水域面积")
    private String occupyWaterArea;

    @ApiModelProperty("补偿面积")
    private String filleArea;

    /**
     * 是否缩窄现状河宽 1-是 0-否
     */
    @ApiModelProperty("是否缩窄现状河宽 1-是 0-否")
    private Integer isNarrow;

    /**
     * 是否破坏提防结构、影响水工程安全 1-是 0-否
     */
    @ApiModelProperty("是否破坏提防结构、影响水工程安全 1-是 0-否")
    private Integer isAffectSafety;

    /**
     * 其他（多行文字）
     */
    @ApiModelProperty("其他（多行文字）")
    private String remark;

    /**
     * 多附件 ，隔开
     */
    @ApiModelProperty("多附件 ，隔开")
    private String fileIds;

    /**
     * 项目阶段 1-前期阶段 2-施工阶段 3-完工阶段
     */
    @ApiModelProperty("项目阶段 1-前期阶段 2-施工阶段 3-完工阶段")
    private Integer projectStage;

    /**
     * 监管状态 1-新项目 2-监管中 3-复核中 4-移交执法大队
     */
    @ApiModelProperty("监管状态 1-新项目 2-监管中 3-复核中 4-移交执法大队")
    private Integer regulatoryStatus;

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
    private Integer delFlag;

    public AttBlueProject() {
    }

}