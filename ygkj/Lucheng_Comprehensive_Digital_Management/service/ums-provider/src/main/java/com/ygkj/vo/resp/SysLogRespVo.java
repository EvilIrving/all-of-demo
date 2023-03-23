package com.ygkj.vo.resp;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysLogRespVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelProperty("用户id")
    @ColumnWidth(value = 30)
    private String userId;

    @ExcelProperty("用户名称")
    @ColumnWidth(value = 30)
    private String username;

    @ExcelProperty("用户操作")
    @ColumnWidth(value = 30)
    private String operation;

    @ExcelProperty("操作模块")
    @ColumnWidth(value = 30)
    private String module;

    @ExcelProperty("响应时间")
    @ColumnWidth(value = 30)
    private Integer time;

    @ExcelProperty("请求方法")
    @ColumnWidth(value = 30)
    private String method;

    @ExcelProperty("请求参数")
    @ColumnWidth(value = 30)
    private String params;

    @ExcelProperty("IP地址")
    @ColumnWidth(value = 30)
    private String ip;

    @ExcelProperty("访问时间")
    @ColumnWidth(value = 30)
    private Date createTime;

    @ExcelProperty("地区标识")
    @ColumnWidth(value = 30)
    private String region;
}
