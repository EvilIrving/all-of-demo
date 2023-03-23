package com.dcy.datamanage.biz.enums;

import lombok.Getter;

@Getter
public enum DataApplyEnum {


    ADD(1),
    DELETE(2),
    UPDATE(3);


    private Integer applyType;

    DataApplyEnum(Integer applyType){
        this.applyType = applyType;
    }
}
