package com.ygkj.gragh.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 河道水质测站表
 * @date 2021-08-04
 */
@Data
@ApiModel("河道水质测站表")
public class AttRivQuaStBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 站点编号
     */
    @ApiModelProperty("站点编号")
    private String siteNo;

    /**
     * 站位名称
     */
    @ApiModelProperty("站位名称")
    private String siteName;

    /**
     * 站位级别[1:国控2:省控3:市控4:县控，5:乡镇控]
     */
    @ApiModelProperty("站位级别[1:国控2:省控3:市控4:县控，5:乡镇控]")
    private Integer level;

    /**
     * 关联河段
     */
    @ApiModelProperty("关联河段")
    private String riverNames;

    /**
     * 是否双覆盖0否1是
     */
    @ApiModelProperty("是否双覆盖0否1是")
    private Integer isBicovering;

    /**
     * 所属地区
     */
    @ApiModelProperty("所属地区")
    private String areaPath;

    /**
     * 功能区目标
     */
    @ApiModelProperty("功能区目标")
    private String functionalGrade;

    /**
     * 经纬度信息
     */
    @ApiModelProperty("经纬度信息")
    private String location;

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    private String areaCode;

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

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private Integer delFlag;

    public AttRivQuaStBase() {
    }

}
