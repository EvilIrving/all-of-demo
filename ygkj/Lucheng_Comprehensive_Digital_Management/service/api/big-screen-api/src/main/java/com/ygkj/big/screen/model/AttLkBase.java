package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 湖泊基础信息表
 */
@Data
public class AttLkBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("湖泊代码 数据来源：录入")
    private String lkCode;


    @ApiModelProperty("湖泊名称 数据来源：防洪库、地图平台")
    private String lkName;


    @ApiModelProperty("左下角经度 单位：°；填写规范：湖泊最小外接矩形左下角 东经度数")
    private Double lowLeftLong;


    @ApiModelProperty("左下角维度 单位：°；填写规范：湖泊最小外接矩形左下角 东伟度数")
    private Double lowLeftLat;


    @ApiModelProperty("右上角经度 单位：°；填写规范：湖泊最小外接矩形右上角东经度数")
    private Double upRightLong;


    @ApiModelProperty("右上角维度 单位：°；填写规范：湖泊最小外接矩形右上角 北纬度数")
    private Double upRightLat;


    @ApiModelProperty("跨界类型 数据来源：防洪库；")
    private String crOverType;


    @ApiModelProperty("湖泊所在位置 填写规范：填写湖泊所在位置，跨国 并跨省和跨国湖泊填外国 国名、我国省名和县名， 如“××国，××省××县、…”； 跨省湖泊填省名和县名， 如“××省××县、××县、 ××(设区市简称)")
    private String lkLoc;


    @ApiModelProperty("水面面积 单位：km2，；填写规范：湖泊常水位对应的水面面 积。计量单位为km2，记 至2 位小数。如梁子湖多 年平均水面面积为： 228.00km2。")
    private Double watArea;


    @ApiModelProperty("湖泊容积 单位：10四次方m3")
    private Double meaAnnLkVol;


    @ApiModelProperty("咸淡水属性 数据来源：防洪库；")
    private String salFreWat;


    @ApiModelProperty("平均水深")
    private Double meaAnnWatDept;


    @ApiModelProperty("最大水深")
    private Double maxDept;


    @ApiModelProperty("备注")
    private String note;


    @ApiModelProperty("记录生效时间")
    private String effDate;


    @ApiModelProperty("记录失效时间")
    private String exprDate;


    @ApiModelProperty("所属流域")
    private String basin;


    @ApiModelProperty("河长级别")
    private String riverManagerGrade;


    @ApiModelProperty("河长姓名")
    private String riverManagerName;


    @ApiModelProperty("河长电话")
    private String riverManagerPhone;


    @ApiModelProperty("河长职务")
    private String riverManagerJob;


    @ApiModelProperty("所属单位")
    private String department;


    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private int delFlag;
}
