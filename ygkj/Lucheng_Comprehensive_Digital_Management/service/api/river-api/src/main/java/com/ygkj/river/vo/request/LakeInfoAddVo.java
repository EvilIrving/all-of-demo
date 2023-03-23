package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class LakeInfoAddVo {

    /**
     * 湖泊代码 数据来源：录入
     */
    @ApiModelProperty("湖泊编码，修改用")
    private String lkCode;

    /**
     * 湖泊名称 数据来源：防洪库、地图平台
     */
    @ApiModelProperty("湖泊名称")
    private String lkName;

    /**
     * 左下角经度 单位：°；填写规范：湖泊最小外接矩形左下角 东经度数
     */
    @ApiModelProperty("经度")
    private BigDecimal lowLeftLong;

    /**
     * 左下角维度 单位：°；填写规范：湖泊最小外接矩形左下角 东伟度数
     */
    @ApiModelProperty("纬度")
    private BigDecimal lowLeftLat;

//    /**
//     * 湖泊容积
//     */
//    @ApiModelProperty("湖泊容积")
//    private BigDecimal lakeVolume;

    //    /**
//     * 平均水深
//     */
//    @ApiModelProperty("平均水深")
//    private BigDecimal averageWaterDeep;
    @ApiModelProperty("水域面积")
    private BigDecimal watArea;

    /**
     * 湖泊容积 单位：10四次方m3
     */
    @ApiModelProperty("湖泊容积")
    private BigDecimal meaAnnLkVol;

    /**
     * 平均水深
     */
    @ApiModelProperty("平均水深")
    private BigDecimal meaAnnWatDept;


    /**
     * 所属流域
     */
    @ApiModelProperty("所属流域")
    private String basin;

    /**
     * 河长级别
     */
    @ApiModelProperty("河长级别")
    private String riverManagerGrade;

    /**
     * 河长姓名
     */
    @ApiModelProperty("河长姓名")
    private String riverManagerName;

    /**
     * 河长电话
     */
    @ApiModelProperty("河长电话")
    private String riverManagerPhone;

    /**
     * 河长职务
     */
    @ApiModelProperty("河长职务")
    private String riverManagerJob;

    /**
     * 所属单位
     */
    @ApiModelProperty("所属单位")
    private String department;
}
