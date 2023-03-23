package com.ygkj.visualization.vo.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdcdStatisticsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String adcd;

    private String adnm;

    private Double popNum;

    private Double gdp;
}
