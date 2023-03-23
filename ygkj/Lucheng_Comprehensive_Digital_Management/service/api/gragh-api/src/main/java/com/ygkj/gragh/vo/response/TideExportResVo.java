package com.ygkj.gragh.vo.response;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.converters.date.DateStringConverter;
import lombok.Data;

import java.util.Date;

@Data
public class TideExportResVo {

    @ExcelProperty(value = "时间",converter = DateStringConverter.class)
    @ColumnWidth(20)
    private Date tm;

    @ExcelProperty("潮位(m)")
    @ColumnWidth(10)
    private Double tdz;
}
