package com.ygkj.data.management.vo.request;

import com.ygkj.data.management.dto.BusDataRuleDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

@Data
public class CandidateRuleRequestVo {

    @NotBlank(message = "必须指定规则类型！")
    @ApiModelProperty("规则类型：正则表达式、更新频率")
    private String ruleType;

    @ApiModelProperty("字段id，查正则表达式的时候用")
    private String columnId;

    @ApiModelProperty("数据记录主键值，查更新频率的时候用")
    private String primaryValue;
    @ApiModelProperty("前端忽略")
    private String tableId;

    public void checkField() {
        BusDataRuleDto.RuleType candidateRule = BusDataRuleDto.RuleType.candidate(ruleType);
        if (null == candidateRule) {
            throw new IllegalArgumentException("请指定正确的规则类型！");
        }
        if (StringUtils.isBlank(columnId) && StringUtils.isBlank(primaryValue)) {
            throw new IllegalArgumentException("需要指定一个字段或一条记录！");
        }
    }
}
