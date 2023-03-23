package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据规则新增入参")
public class BusDataRuleAddVo {

    @ApiModelProperty("id，更新用")
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
}
