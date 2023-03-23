package com.ygkj.platform.management.vo.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 每天错误的次数
 */
@Data
public class SysErrorDayCountVo implements Serializable {

    private String errorDay;
    private String errorType;
    private String errorNum;

}
