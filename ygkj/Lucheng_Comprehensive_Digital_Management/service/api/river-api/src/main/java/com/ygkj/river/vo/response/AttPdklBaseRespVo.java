package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水域调查数据-堤防数据表
 * @date 2021-07-07
 */
@Data
@ApiModel("水域调查数据-堤防数据表")
public class AttPdklBaseRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fid
     */
    @ApiModelProperty("fid")
    private String fid;

    /**
     * 堤防名称
     */
    @ApiModelProperty("堤防名称")
    private String name;

    /**
     * 所在水域名称
     */
    @ApiModelProperty("所在水域名称")
    private String wname;

    /**
     * 所在水域编码
     */
    @ApiModelProperty("所在水域编码")
    private String wcode;

    /**
     * 所在市
     */
    @ApiModelProperty("所在市")
    private String city;

    /**
     * 所在县（市、区）
     */
    @ApiModelProperty("所在县（市、区）")
    private String county;

    /**
     * 所在乡镇（街道）
     */
    @ApiModelProperty("所在乡镇（街道）")
    private String town;

    /**
     * 长度
     */
    @ApiModelProperty("长度")
    private String length;

    /**
     * 起点高程
     */
    @ApiModelProperty("起点高程")
    private String selev;

    /**
     * 终点高程
     */
    @ApiModelProperty("终点高程")
    private String eelev;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 设计标准
     */
    @ApiModelProperty("设计标准")
    private String ds;

    /**
     * 起点经度
     */
    @ApiModelProperty("起点经度")
    private String slon;

    /**
     * 起点纬度
     */
    @ApiModelProperty("起点纬度")
    private String slat;

    /**
     * 终点经度
     */
    @ApiModelProperty("终点经度")
    private String elon;

    /**
     * 终点纬度
     */
    @ApiModelProperty("终点纬度")
    private String elat;

    /**
     * geometry
     */
    @ApiModelProperty("geometry")
    private String geometry;

    public AttPdklBaseRespVo() {
    }

}