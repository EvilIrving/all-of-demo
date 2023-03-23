package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

/**
 * @author lxl
 * @create 2022-07-15 14:35
 * @description
 */
@Data
public class PushPpData {

    private String stationCode;

    private Date timeD;

    private Double precipitationTime;

    private String dailyPrecipitation = "0";


}
