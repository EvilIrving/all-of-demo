package com.ygkj.gragh.vo.response;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

@Data
public class AqjcExportResVo {

    @ExcelProperty("时间")
    @ColumnWidth(20)
    private String gcsj;

    @ExcelProperty("观测值(mm)")
    @ColumnWidth(15)
    private String zzjg;
}
