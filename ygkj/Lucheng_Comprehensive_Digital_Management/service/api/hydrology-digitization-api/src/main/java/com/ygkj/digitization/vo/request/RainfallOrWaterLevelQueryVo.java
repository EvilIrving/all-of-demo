package com.ygkj.digitization.vo.request;

import lombok.Data;

import java.util.List;

@Data
public class RainfallOrWaterLevelQueryVo {

    private String start;

    private String end;

    private List<String> stationCodes;
}
