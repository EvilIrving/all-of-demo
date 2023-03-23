package com.ygkj.data.management.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据审批excel导出表
 */
@Data
public class DataApprovalExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    public DataApprovalExcel() {
    }

    @ColumnWidth(20)
    @ExcelProperty(value = "序号", index = 0)
    private String id;

    @ColumnWidth(20)
    @ExcelProperty(value = "操作类型", index = 1)
    private String operationType;

    @ColumnWidth(20)
    @ExcelProperty(value = "提交时间", index = 2)
    private String submitTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "记录名称", index = 3)
    private String recordPrimary;

    @ColumnWidth(20)
    @ExcelProperty(value = "表名称", index = 4)
    private String tableName;

    @ColumnWidth(20)
    @ExcelProperty(value = "提交人", index = 5)
    private String submitUserName;

    @ColumnWidth(20)
    @ExcelProperty(value = "审批状态", index = 6)
    private String status;
}
