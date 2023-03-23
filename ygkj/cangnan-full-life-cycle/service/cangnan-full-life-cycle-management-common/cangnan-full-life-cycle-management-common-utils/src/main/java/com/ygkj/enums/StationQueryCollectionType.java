package com.ygkj.enums;

/**
 * 测站查询集合类型枚举
 */
public enum  StationQueryCollectionType implements Type{
    BASIN("流域"),AREA_CODE("行政区划");

    String type;

    StationQueryCollectionType(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }
}
