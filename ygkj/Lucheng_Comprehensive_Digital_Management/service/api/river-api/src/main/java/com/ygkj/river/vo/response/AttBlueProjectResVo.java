package com.ygkj.river.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.river.model.AttBlueProject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2021-08-06 19:16
 * @description 蓝线监管项目返回类
 */
@Data
@ApiModel("蓝线监管项目返回类")
@JsonIgnoreProperties(value = {"handler"})
public class AttBlueProjectResVo extends AttBlueProject {

    /**
     * 所属流域
     */
    @ApiModelProperty("所属流域")
    private String bas;


    /**
     * 巡查次数
     */
    @ApiModelProperty("巡查次数")
    private Integer inspectionNum;

    /**
     * 未整改问题数量
     */
    @ApiModelProperty("未整改问题数量")
    private Integer unRectifyProblemNum;

    /**
     * 最后一次巡查时间
     */
    @ApiModelProperty("最后一次巡查时间")
    private String lastInspectionTime;

    @ApiModelProperty("河道等级 1、2、3、4、5级")
    private String riverLevel;

    @ApiModelProperty("所属流域")
    private String basin;

    private String proState;

    private String regulaState;


    /**
     * 是否缩窄现状河宽 1-是 0-否
     */
    private String narrow;

    /**
     * 是否破坏提防结构、影响水工程安全 1-是 0-否
     */
    private String affectSafety;

    @ApiModelProperty("携带监管信息")
    private AttBlueInspectionResVo inspections;


    public AttBlueProjectResVo() {
    }


}
