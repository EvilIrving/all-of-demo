package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class PushModel {

    private String id;

    private String assumedTime;

    private Date actualTime;

    private String dataValue;

}
