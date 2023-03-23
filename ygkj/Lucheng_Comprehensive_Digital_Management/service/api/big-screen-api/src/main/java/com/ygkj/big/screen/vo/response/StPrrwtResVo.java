package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/13
 */
@ApiModel("水雨工情测站检测值")
@Data
public class StPrrwtResVo {

    /**
     * 合并测站编码
     */
    @ApiModelProperty("合并测站编码")
    private String mgstcd;

    /**
     * 测站编码
     */
    @ApiModelProperty("测站编码")
    private String stcd;

    /**
     * 测量时间
     */
    @ApiModelProperty("测量时间")
    private Date tm;

    /**
     * 时段降水量
     */
    @ApiModelProperty("时段降水量")
    private Double drp;

    /**
     * 时段长
     */
    @ApiModelProperty("时段长")
    private Double intv;

    /**
     * 水位
     */
    @ApiModelProperty("河道测站水位")
    private Double z;

    /**
     * 水位
     */
    @ApiModelProperty("水库测站水位")
    private Double rz;

    /**
     * 潮位
     */
    @ApiModelProperty("潮位")
    private Double tdz;

    /**
     * 闸上水位
     */
    @ApiModelProperty("闸上水位")
    private Double upz;

    /**
     * 闸下水位
     */
    @ApiModelProperty("闸下水位")
    private Double dwz;

    /**
     * 最大过闸流量
     */
    @ApiModelProperty("最大过闸流量")
    private String mxgtq;

    /**
     * 开度
     */
    @ApiModelProperty("开度")
    private String ovs;

    /**
     * 流量
     */
    @ApiModelProperty("流量")
    private String ll;

    /**
     * tag
     */
    @ApiModelProperty("tag")
    private boolean tag;

    /**
     * upload_time
     */
    @ApiModelProperty("upload_time")
    private Date uploadTime;
}
