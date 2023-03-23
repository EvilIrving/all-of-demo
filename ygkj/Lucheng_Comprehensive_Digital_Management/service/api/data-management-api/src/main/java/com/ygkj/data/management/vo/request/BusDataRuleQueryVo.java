package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据规则查询入参入参")
public class BusDataRuleQueryVo extends PageEntity {

    @ApiModelProperty("id，查单条用")
    private String id;

    @ApiModelProperty("规则名称")
    private String ruleName;

    @ApiModelProperty("规则类型：正则表达式、更新频率")
    private String ruleType;
}
