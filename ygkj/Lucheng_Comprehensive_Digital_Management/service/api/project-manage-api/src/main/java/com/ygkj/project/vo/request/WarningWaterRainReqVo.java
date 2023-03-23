package com.ygkj.project.vo.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WarningWaterRainReqVo {

    private List<String> stList;

    private String ym;

    private Date date;
}
