package com.ygkj.gragh.vo.request;

import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/7/8
 */
@Data
public class Rainfall4DrillReqVo {

    String stcds;
    String startTime;
    String endTime;
    Integer hour;
}
