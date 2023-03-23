package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Huangzh
 * @description 测站更新频率规则关联表
 * @date 2021-07-20
 */
@Data
@ApiModel("测站更新频率规则关联表")
public class BusBusinessDataRuleRow implements Serializable {

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
     * 测站类型
     */
    @ApiModelProperty("测站类型")
    @NotBlank(message = "请指定当前测站的类型！")
    private String stationType;

    /**
     * 更新频率规则id
     */
    @ApiModelProperty("更新频率规则id")
    private String ruleId;

    public BusBusinessDataRuleRow() {
    }

}

