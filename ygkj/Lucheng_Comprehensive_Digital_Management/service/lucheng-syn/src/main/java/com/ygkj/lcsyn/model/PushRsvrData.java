package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

/**
 * @author lxl
 * @create 2022-07-15 15:47
 * @description
 */
@Data
public class PushRsvrData {


    private String stationCode;

    private Date timeD;

    private Double upwaterLevel;

}
