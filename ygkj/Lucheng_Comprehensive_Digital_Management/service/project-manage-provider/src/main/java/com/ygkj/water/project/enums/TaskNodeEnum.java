package com.ygkj.water.project.enums;

import lombok.Getter;

/**
 * @author lxl
 * @create 2022-04-22 10:07
 * @description
 */
@Getter
public enum TaskNodeEnum {


    //1待处理、2已通过、3已驳回、4已处理、5待审核 、6归档
    PEND(1, "待处理"),
    PASS(2, "已通过"),
    REJECT(3, "已驳回"),
    AUDIT(4, "已处理"),
    REVIEW(5,"待审核"),
    ARCHIVE(6,"归档")
    ;
    private Integer code;
    private String desc;

    TaskNodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
