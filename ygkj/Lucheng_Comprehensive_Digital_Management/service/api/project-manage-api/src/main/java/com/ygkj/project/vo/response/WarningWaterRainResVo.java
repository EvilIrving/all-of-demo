package com.ygkj.project.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class WarningWaterRainResVo {

    private String stCode;

    private Double value;

    private Date tm;
}
