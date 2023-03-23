package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 水域动态监督--水域问题信息（四乱）
 */
@Data
public class WtDynProblem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("四乱问题名称")
    private String name;


    @ApiModelProperty("问题来源（1督查、2自查）")
    private String proSource;


    @ApiModelProperty("上报层级（市/县）")
    private String reportLevel;


    @ApiModelProperty("处理权限（市/县）")
    private String processLimit;


    @ApiModelProperty("所在流域（瓯江、鳌江、飞云江）")
    private String waterArea;


    @ApiModelProperty("水域类型（1河流、2湖泊、3水库、4山塘）")
    private String waterType;


    @ApiModelProperty("所在河湖")
    private String river;


    @ApiModelProperty("河道等级")
    private String riverLevel;


    @ApiModelProperty("河长姓名")
    private String riverChief;


    @ApiModelProperty("行政区划")
    private String adcd;


    @ApiModelProperty("行政区划名")
    private String adnm;


    @ApiModelProperty("经度")
    private Double lng;


    @ApiModelProperty("纬度")
    private Double lat;


    @ApiModelProperty("详细地址")
    private String addr;


    @ApiModelProperty("四乱类型（1乱占、2乱采、3乱堆、4乱建）")
    private String proType;


    @ApiModelProperty("严重程度（轻微、中度、重大）")
    private String severity;


    @ApiModelProperty("问题描述")
    private String proDesc;


    @ApiModelProperty("现场图片(url，多个以逗号分割)")
    private String livePic;


    @ApiModelProperty("备注说明")
    private String remark;


    @ApiModelProperty("占用面积")
    private Double occupyArea;


    @ApiModelProperty("通知单位（单位id）")
    private String deptId;


    @ApiModelProperty("通知人员（人员id）")
    private String userId;


    @ApiModelProperty("整改期限-开始日期")
    private String startString;


    @ApiModelProperty("整改期限-结束日期")
    private String endString;


    @ApiModelProperty("整改描述")
    private String rectifyDesc;


    @ApiModelProperty("上报/下发时间")
    private String reportTime;


    @ApiModelProperty("事件来源（pc端、浙政钉、市生态环境局）")
    private String eventSource;


    @ApiModelProperty("状态（1未处理，2待审核，3已处理，4逾期未处理）")
    private String status;
}
