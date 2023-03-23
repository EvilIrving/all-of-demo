package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Huangzh
 * @description 实时数据字段测站规则绑定表
 * @date 2021-07-20
 */
@Data
@ApiModel("实时数据字段测站规则绑定表")
public class BusBusinessDataRuleColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 测站编码
     */
    @ApiModelProperty("测站编码")
    @NotBlank(message = "请指定一个测站！")
    private String stationCode;

    /**
     * 规则字段名称
     */
    @ApiModelProperty("规则字段名称")
    @NotBlank(message = "请指定要校验的字段！")
    private String columnName;

    @ApiModelProperty("规则字段中文释义")
    private String columnComment;

    /**
     * 测站类型
     */
    @ApiModelProperty("测站类型")
    @NotBlank(message = "请指定要校验的测站类型！")
    private String stationType;

    /**
     * 范围最小值
     */
    @ApiModelProperty("范围最小值")
    private Double minValue;

    /**
     * 范围最大值
     */
    @ApiModelProperty("范围最大值")
    private Double maxValue;

    public BusBusinessDataRuleColumn() {
    }

}

