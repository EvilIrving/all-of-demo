package com.dcy.datamanage.biz.enums;

import lombok.Getter;

@Getter
public enum DataApplySearchEnum {

    SEND(1),
    PENDING(2),
    PROCESSED(3);

    private Integer type;

    DataApplySearchEnum(Integer type){
        this.type = type;
    }
}
