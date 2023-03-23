package com.ygkj.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StartEndTimeVo implements Serializable {
    String startTime;
    String endTime;
    int tag;
}
