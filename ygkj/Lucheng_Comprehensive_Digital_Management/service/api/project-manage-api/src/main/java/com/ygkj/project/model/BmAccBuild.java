package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-11 13:59
 */
@Data
@ApiModel("附属建筑物")
public class BmAccBuild implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 附属工程编码
     */
    @TableId("ID")
    @ApiModelProperty("附属工程编码")
    private String id;

    /**
     * 主体工程(海塘工程编码)
     */
    @ApiModelProperty("主体工程(海塘工程编码)")
    private String prcd;

    /**
     * 附属建筑物名称
     */
    @ApiModelProperty("附属建筑物名称")
    private String proName;

    /**
     * 1管理房 2 码头 3桥梁 4道路
     */
    @ApiModelProperty("1管理房 2 码头 3桥梁 4道路")
    private String proType;

    /**
     * 位置
     */
    @ApiModelProperty("位置")
    private String proLoc;

    /**
     * 面积
     */
    @ApiModelProperty("面积")
    private Double proArea;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String lng;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String lat;

    /**
     * 简介
     */
    @ApiModelProperty("简介")
    private String proInfo;

    /**
     * 照片
     */
    @ApiModelProperty("照片")
    private String imgFile;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String creatorId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updatorId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private int delFlag;
}
