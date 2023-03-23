package com.ygkj.enums;

/**
 * 旬月枚举
 */
public enum MonthDaysType implements Type{
    FIRST_TEN_DAYS("上旬"),SECOND_TEN_DAYS("中旬"),LAST_TEN_DAYS("下旬");

    private String type;

    MonthDaysType(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }
}
