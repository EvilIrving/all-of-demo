package com.ygkj.visualization.vo.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AreaGdpStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private String year;

    private Double gdp;
}
