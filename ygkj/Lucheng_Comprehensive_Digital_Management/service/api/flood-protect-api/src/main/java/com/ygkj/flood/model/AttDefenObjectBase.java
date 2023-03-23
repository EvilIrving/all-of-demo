package com.ygkj.flood.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 山洪灾害防御对象清单
 * @author lxl
 * @date 2022-05-17
 */
@Data
@ApiModel("山洪灾害防御对象清单")
public class AttDefenObjectBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 行政村
     */
    @ApiModelProperty("行政村")
    private String adminAdnm;

    /**
     * 自然村
     */
    @ApiModelProperty("自然村")
    private String naturalAdnm;

    /**
     * 乡（镇、街道）
     */
    @ApiModelProperty("乡（镇、街道）")
    private String town;

    /**
     * 风险区等级
     */
    @ApiModelProperty("风险区等级")
    private String lev;

    /**
     * 防御对象
     */
    @ApiModelProperty("防御对象")
    private String defenObject;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 联系人
     */
    @ApiModelProperty("联系人")
    private String contact;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    private String contactTel;

    /**
     * 人口数量
     */
    @ApiModelProperty("人口数量")
    private Integer pcount;

    /**
     * 转移责任人
     */
    @ApiModelProperty("转移责任人")
    private String tcharger;

    /**
     * 转移责任人联系方式
     */
    @ApiModelProperty("转移责任人联系方式")
    private String tchargertel;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty("删除标记 0未删除 1已删除")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    public AttDefenObjectBase() {
    }

}

