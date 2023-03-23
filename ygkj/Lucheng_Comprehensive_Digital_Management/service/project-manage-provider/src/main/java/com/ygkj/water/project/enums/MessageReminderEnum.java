package com.ygkj.water.project.enums;

import lombok.Getter;

/**
 * @author lxl
 * @create 2022-06-09 10:05
 * @description
 */
@Getter
public enum MessageReminderEnum {


    UNREAD("未读","1"),
    READ("已读","2"),
    HIDDEN_DANGER("维修养护","维修养护"),
    CONTRACT("合同管理","合同管理");

    private String key;

    private String value;

    MessageReminderEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
