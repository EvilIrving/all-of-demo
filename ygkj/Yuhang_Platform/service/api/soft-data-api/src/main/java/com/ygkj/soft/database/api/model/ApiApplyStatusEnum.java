package com.ygkj.soft.database.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 接口审核状态操作枚举类
 */
@ToString
@AllArgsConstructor
public enum ApiApplyStatusEnum {
    AUDIT_PASS(1,"审核通过"),
    REJECTED(2,"驳回");

    @Getter
    private Integer status;
    @Getter
    private String statusName;
}
