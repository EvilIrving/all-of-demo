package com.ygkj.water.project.enums;

import lombok.Getter;

/**
 * @author lxl
 * @create 2022-04-22 9:36
 * @description 隐患处理状态枚举
 */
@Getter
public enum HiddenDangerStatusEnum {

//1-已处理、2-待处理、3-处理中
    ALREADY(2, "已处理"),
    PEND(0, "待处理"),
    EXECUTION(1, "处理中")
    ;
    private Integer code;
    private String desc;

    HiddenDangerStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
