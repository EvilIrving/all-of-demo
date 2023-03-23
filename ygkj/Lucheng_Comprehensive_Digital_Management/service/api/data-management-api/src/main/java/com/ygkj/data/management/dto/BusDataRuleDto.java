package com.ygkj.data.management.dto;

import cn.hutool.core.util.EscapeUtil;
import com.google.common.base.Converter;
import com.ygkj.data.management.model.BusDataRule;
import com.ygkj.enums.Type;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;

@Data
public class BusDataRuleDto {

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 规则名称
     */
    @ApiModelProperty("规则名称")
    @NotBlank(message = "请指定规则名称")
    private String ruleName;

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
    @NotBlank(message = "请指定规则类型！")
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

    @ApiModelProperty("字段id")
    private String columnId;

    @ApiModelProperty("字段名称")
    private String columnName;

    private String columnComment;

    public void setRuleLogic(String ruleLogic) {
        if (StringUtils.isNotBlank(ruleLogic)) {
            this.ruleLogic = EscapeUtil.unescapeHtml4(ruleLogic);
        }
    }

    public BusDataRule convertToRuleEntity(BusDataRuleDto busDataRuleDto) {
        RuleConverter converter = new RuleConverter();
        return converter.convert(busDataRuleDto);
    }

    public BusDataRuleDto convertFor(BusDataRule busDataRule) {
        RuleConverter converter = new RuleConverter();
        return converter.reverse().convert(busDataRule);
    }

    public void checkField() {
        if (RuleType.candidate(this.ruleType).equals(RuleType.UPDATE_FREQUENCY) &&
                (StringUtils.isEmpty(this.timeUnit) || this.updateFrequency == null)) {
            throw new IllegalArgumentException("请指定更新频率！");
        }
        if (RuleType.candidate(this.ruleType).equals(RuleType.REGEX) && StringUtils.isBlank(this.ruleLogic)) {
            throw new IllegalArgumentException("请填写校验用的正则表达式！");
        }
    }

    public enum RuleType implements Type {
        REGEX("正则表达式"),
        UPDATE_FREQUENCY("更新频率");

        private String type;

        RuleType(String type) {
            this.type = type;
        }

        @Override
        public String type() {
            return type;
        }

        public static RuleType candidate(String type) {
            if (StringUtils.isEmpty(type)) {
                return null;
            }
            switch (type) {
                case "正则表达式":
                    return REGEX;
                case "更新频率":
                    return UPDATE_FREQUENCY;
            }
            return null;
        }
    }

    public static class RuleConverter extends Converter<BusDataRuleDto, BusDataRule> {
        @Override
        protected BusDataRule doForward(BusDataRuleDto busDataRuleDto) {
            BusDataRule rule = new BusDataRule();
            BeanUtils.copyProperties(busDataRuleDto, rule);
            if (StringUtils.isNotBlank(rule.getRuleLogic())) {
                rule.setRuleLogic(EscapeUtil.escapeHtml4(rule.getRuleLogic()));
            }
            return rule;
        }

        @Override
        protected BusDataRuleDto doBackward(BusDataRule busDataRule) {
            BusDataRuleDto ruleDto = new BusDataRuleDto();
            BeanUtils.copyProperties(busDataRule, ruleDto);
            return ruleDto;
        }
    }

}
