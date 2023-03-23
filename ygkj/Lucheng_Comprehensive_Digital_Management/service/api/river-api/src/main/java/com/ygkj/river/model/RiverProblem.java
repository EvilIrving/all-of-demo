package com.ygkj.river.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @description 河长通-问题信息表
 * @date 2021-08-09
 */
@Data
@ApiModel("河长通-问题信息表")
@ToString
public class RiverProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 问题标签
     */
    @ApiModelProperty("问题标签")
    private String label;

    /**
     * 问题内容
     */
    @ApiModelProperty("问题内容")
    private String content;

    /**
     * 问题类型 0 举报问题 1 巡河问题 2排口四乱
     */
    @ApiModelProperty("问题类型 0 举报问题 1 巡河问题 2排口四乱")
    private Integer problemType;

    /**
     * 河段名
     */
    @ApiModelProperty("河段名")
    private String riverName;

    /**
     * 轻微程度 0 轻微 1 一般问题 2重大问题
     */
    @ApiModelProperty("轻微程度 0 轻微 1 一般问题 2重大问题")
    private Integer level;

    /**
     * 举报人姓名
     */
    @ApiModelProperty("举报人姓名")
    private String realName;

    /**
     * 所属地区
     */
    @ApiModelProperty("所属地区")
    private String areaPath;

    /**
     * 状态[1:未受理;2:处理中;3:已处理待评价;4:已处理已评价]
     */
    @ApiModelProperty("状态[1:未受理;2:处理中;3:已处理待评价;4:已处理已评价]")
    private Integer state;

    /**
     * 问题创建时间
     */
    @ApiModelProperty("问题创建时间")
    private Date createDate;

    /**
     * 问题处理截止时间
     */
    @ApiModelProperty("问题处理截止时间")
    private Date deadline;

    /**
     * 问题发现位置
     */
    @ApiModelProperty("问题发现位置")
    private String address;

    /**
     * 问题发现位置信息
     */
    @ApiModelProperty("问题发现位置信息")
    private String location;

    /**
     * 问题图片
     */
    @ApiModelProperty("问题图片")
    private String picPath;

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

    public RiverProblem() {
    }

}
