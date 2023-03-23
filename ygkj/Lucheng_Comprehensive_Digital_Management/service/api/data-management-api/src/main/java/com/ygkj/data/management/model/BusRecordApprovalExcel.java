package com.ygkj.data.management.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;

@Data
public class BusRecordApprovalExcel implements Serializable {


    private static final long serialVersionUID = 1L;

    @ColumnWidth(20)
    @ExcelProperty(value = "序号", index = 0)
    private String id;

    @ColumnWidth(20)
    @ExcelProperty(value = "表类型", index = 1)
    private String category;

    /**
     * 表名称，冗余字段避免过多join查询
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "表名称", index = 2)
    private String tableName;

    /**
     * 记录id，暂定主键值
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "记录名称", index = 3)
    private String recordPrimary;

    @ColumnWidth(20)
    @ExcelProperty(value = "提交人", index = 4)
    private String submitUserName;

    /**
     * 提交时间
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "提交时间", index = 5)
    private String submitTime;

    /**
     * 操作类型：新增、修改、删除
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "操作", index = 6)
    private String operationType;

    public BusRecordApprovalExcel() {
    }

}
