package com.ygkj.data.management.vo.response;

import lombok.Data;

@Data
public class BusTableColumnRuleResVo {

    private String id;

    private String tableId;

    private String columnName;

    private String columnComment;

    private String ruleId;

    private String ruleName;

    private String process;

    private String ruleLogic;

}
