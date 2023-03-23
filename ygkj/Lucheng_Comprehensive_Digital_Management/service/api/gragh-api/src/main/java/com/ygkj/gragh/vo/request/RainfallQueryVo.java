package com.ygkj.gragh.vo.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RainfallQueryVo {

    private double min;

    private double max;

    private String stationName;

    private String stationCode;

    private Date start;

    private Date end;

    private List<String> codes;

    private Boolean needSum;

    public RainfallQueryVo() {
    }

    public RainfallQueryVo(double min, double max, String stationName, Date start, Date end,List<String> codes, Boolean needSum) {
        this.min = min;
        this.max = max;
        this.stationName = stationName;
        this.start = start;
        this.end = end;
        this.codes = codes;
        this.needSum = needSum;
    }
}
