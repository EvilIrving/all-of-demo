package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 数据规则
 * @date 2021-06-15
 */
@Data
@ApiModel("数据规则")
public class BusDataRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 规则名称
     */
    @ApiModelProperty("规则名称")
    private String ruleName;

    /**
     * 用途（清洗、校验）
     */
    @ApiModelProperty("用途（清洗、校验）")
    private String ruleUse;

    /**
     * 处理规则（校验规则、处理规则）
     */
    @ApiModelProperty("处理规则（校验规则、处理规则）")
    private String process;

    /**
     * 规则详细逻辑
     */
    @ApiModelProperty("规则详细逻辑")
    private String ruleLogic;

    /**
     * 规则描述
     */
    @ApiModelProperty("规则描述")
    private String description;

    /**
     * 规则类型
     */
    @ApiModelProperty("规则类型：正则表达式、更新频率")
    private String ruleType;

    /**
     * 频率单位：周、天、时、分
     */
    @ApiModelProperty("频率单位：周、天、时、分")
    private String timeUnit;

    /**
     * 更新频率
     */
    @ApiModelProperty("更新频率")
    private Integer updateFrequency;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private boolean delFlag;

    public BusDataRule() {
    }

}

