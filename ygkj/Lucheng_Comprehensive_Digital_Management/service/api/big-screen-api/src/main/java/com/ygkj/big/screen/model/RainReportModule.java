package com.ygkj.big.screen.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class RainReportModule {
    @Excel(name = "所属区域")
    private String adName;
    @Excel(name = "小流域")
    private String bas;
    @Excel(name = "测站名称",width = 15)
    private String stName;
    @Excel(name = "雨量",width = 15)
    private String drp;
}
