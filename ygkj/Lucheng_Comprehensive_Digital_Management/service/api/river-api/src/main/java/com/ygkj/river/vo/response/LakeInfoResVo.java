package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("湖泊信息")
public class LakeInfoResVo {

    /**
     * 湖泊代码 数据来源：录入
     */
    private String lkCode;

    /**
     * 湖泊名称 数据来源：防洪库、地图平台
     */
    private String lkName;

    private String lowLeftLong;

    /**
     * 左下角维度 单位：°；填写规范：湖泊最小外接矩形左下角 东伟度数
     */
    private String lowLeftLat;

    /**
     * 湖泊容积
     */
    private BigDecimal lakeVolume;

    /**
     * 平均水深
     */
    private BigDecimal averageWaterDeep;

    private BigDecimal watArea;

    /**
     * 湖泊容积 单位：10四次方m3
     */
    private BigDecimal meaAnnLkVol;

    /**
     * 平均水深
     */
    private BigDecimal meaAnnWatDept;


    /**
     * 所属流域
     */
    private String basin;

    /**
     * 河长级别
     */
    private String riverManagerGrade;

    /**
     * 河长姓名
     */
    private String riverManagerName;

    /**
     * 河长电话
     */
    private String riverManagerPhone;

    /**
     * 河长职务
     */
    private String riverManagerJob;

    /**
     * 所属单位
     */
    private String department;
}
