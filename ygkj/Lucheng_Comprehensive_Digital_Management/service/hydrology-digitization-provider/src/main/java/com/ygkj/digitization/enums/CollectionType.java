package com.ygkj.digitization.enums;

public enum CollectionType implements Type{
    ARCHIVES("档案");

    private String type;


    CollectionType(String type) {
        this.type = type;
    }


    @Override
    public String type() {
        return type;
    }
}
