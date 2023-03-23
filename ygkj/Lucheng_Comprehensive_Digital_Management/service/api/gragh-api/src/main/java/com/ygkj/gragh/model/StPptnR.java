package com.ygkj.gragh.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StPptnR implements Cloneable, Serializable {
    @ExcelIgnore
    private static final long serialVersionUID = 1L;

    /**
     * 合并测站编码
     */
    @ExcelIgnore
    private String mgstcd;

    /**
     * 测站编码
     */
    @ExcelIgnore
    private String stcd;

    /**
     * 测量时间
     */
    @ExcelProperty("时间")
    @ColumnWidth(20)
    private Date tm;

    /**
     * 时段降水量
     */
    @ExcelProperty("雨量(mm)")
    @ColumnWidth(12)
    private Double drp;

    /**
     * 时段长
     */
    @ExcelIgnore
    private Double intv;

    /**
     * 上报时间
     */
    @ExcelIgnore
    private Date uploadTime;

    @ExcelIgnore
    private String stationName;

    @ExcelIgnore
    private boolean warning;

    @ExcelIgnore
    private double latitude;

    @ExcelIgnore
    private double longitude;

    @ExcelIgnore
    private String stType;

    @ExcelIgnore
    private boolean isCollected;

    @ExcelIgnore
    private String areaName;

    @ExcelIgnore
    private String areaCode;

    @ExcelIgnore
    private String hourTime;

    @ExcelIgnore
    private String stLoc;

    @ExcelIgnore
    private String bas;

    public StPptnR() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
