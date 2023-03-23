package com.ygkj.water.project.enums;

/**
 * @author xq
 * @Description
 * @Date 2021/9/10
 */
public enum CameraWarnRuleEnum {
    HOURLY(1, "每时"),
    DAILY(2, "每日"),
    WEEKLY(3, "每周"),
    MONTHLY(4, "每月"),
    ;
    private Integer ruleCode;
    private String ruleDesc;

    CameraWarnRuleEnum(Integer ruleCode, String ruleDesc) {
        this.ruleCode = ruleCode;
        this.ruleDesc = ruleDesc;
    }

    public static CameraWarnRuleEnum candidateByRuleCode(Integer ruleCode) {
        CameraWarnRuleEnum result = null;
        if (ruleCode == null) {
            return result;
        }
        switch (ruleCode) {
            case 1:
                return HOURLY;
            case 2:
                return DAILY;
            case 3:
                return WEEKLY;
            case 4:
                return MONTHLY;
            default:
                return null;
        }
    }

    public Integer getRuleCode() {
        return ruleCode;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }
}
