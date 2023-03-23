package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 另一个河段基础信息表
 * @date 2021-08-07
 */
@Data
@ApiModel("另一个河段基础信息表")
public class AttRivSegBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 河段编号
     */
    @ApiModelProperty("河段编号")
    private Integer id;

    /**
     * 河道编号
     */
    @ApiModelProperty("河道编号")
    private String riverNo;

    /**
     * 河段名称
     */
    @ApiModelProperty("河段名称")
    private String riverName;

    /**
     * 河道等级2:省级;3:市级;4:县级;5:镇级;6:村级
     */
    @ApiModelProperty("河道等级2:省级;3:市级;4:县级;5:镇级;6:村级")
    private Integer level;

    /**
     * 河段等级2:省级;3:市级;4:县级;5:镇级;6:村级
     */
    @ApiModelProperty("河段等级2:省级;3:市级;4:县级;5:镇级;6:村级")
    private Integer areaLevel;

    /**
     * 起点位置
     */
    @ApiModelProperty("起点位置")
    private String startName;

    /**
     * 终点位置
     */
    @ApiModelProperty("终点位置")
    private String endName;

    /**
     * 河段长度
     */
    @ApiModelProperty("河段长度")
    private String length;

    /**
     * 所属地区
     */
    @ApiModelProperty("所属地区")
    private String pathStr;

    /**
     * 流域名称
     */
    @ApiModelProperty("流域名称")
    private String watershedName;

    /**
     * 起点经纬度
     */
    @ApiModelProperty("起点经纬度")
    private String startLocation;

    /**
     * 终点经纬度
     */
    @ApiModelProperty("终点经纬度")
    private String endLocation;

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

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    private String areaCode;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    private String areaName;


    /**
     * 河湖“四乱”清零：是/否
     */
    @ApiModelProperty("河湖“四乱”清零：是/否")
    private Integer noneFourChaos;

    /**
     * 河湖管护到位：是/否
     */
    @ApiModelProperty("河湖管护到位：是/否")
    private Integer riverManageWell;

    /**
     * 无乱点河道创建：是/否
     */
    @ApiModelProperty("无乱点河道创建：是/否")
    private Integer noneIllegalRiverCreate;

    /**
     * 水域岸线占补平衡：是/否
     */
    @ApiModelProperty("水域岸线占补平衡：是/否")
    private Integer shorelineOccupyBalance;

    /**
     * 涉河建设项目批建相符：是/否
     */
    @ApiModelProperty("涉河建设项目批建相符：是/否")
    private Integer riverProjTallied;

    /**
     * 公众满意：是/否
     */
    @ApiModelProperty("公众满意：是/否")
    private Integer publicSatisfy;

    /**
     * 1绿码、2黄码、3红码；5个“是”为绿码，3~4个“是”为黄码，0~2个“是”为红码
     */
    @ApiModelProperty("1绿码、2黄码、3红码；5个“是”为绿码，3~4个“是”为黄码，0~2个“是”为红码")
    private Integer healthState;


    public AttRivSegBase() {
    }

}
