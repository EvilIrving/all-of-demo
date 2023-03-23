package com.ygkj.project.vo.response;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProjectDangerResVo {

    @ApiModelProperty("主键")
    @ExcelIgnore
    private Integer id;

    @ApiModelProperty("简称")
    @ExcelProperty(value = "简称",index = 0)
    @ColumnWidth(10)
    private String abbreviation;

    @ApiModelProperty("部位")
    @ExcelProperty(value = "部位",index = 2)
    @ColumnWidth(20)
    private String position;

    @ApiModelProperty("描述及说明")
    @ExcelProperty(value = "描述及说明",index = 3)
    @ColumnWidth(20)
    private String description;

    @ApiModelProperty("发现时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(value = "发现时间",index = 7)
    @ColumnWidth(20)
    private Date foundtime;

    @ApiModelProperty("发现方式")
    @ExcelProperty(value = "发现方式",index = 5)
    @ColumnWidth(20)
    private String foundmethod;

    @ApiModelProperty("照片")
    @ExcelIgnore
    private String picaid;

    @ApiModelProperty("处置方式")
    @ExcelProperty(value = "处置方式",index = 6)
    @ColumnWidth(20)
    private String disposalmethod;

    @ApiModelProperty("计划治理完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(value = "计划治理完成时间",index = 8)
    @ColumnWidth(20)
    private Date planCompTm;

    @ApiModelProperty("治理责任人")
    @ExcelProperty(value = "责任人",index = 1)
    @ColumnWidth(10)
    private String personliable;

    @ApiModelProperty("实际完成治理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(value = "实际完成治理时间",index = 9)
    @ColumnWidth(20)
    private Date actlCompTm;

    @ApiModelProperty("治理后照片")
    @ExcelIgnore
    private String afteraid;

    @ApiModelProperty("工程编号")
    @ExcelIgnore
    private String pid;

    @ApiModelProperty("治理负责人电话")
    @ExcelProperty(value = "治理负责人电话",index = 4)
    @ColumnWidth(20)
    private String phone;

    @ApiModelProperty("治理说明")
    @ExcelProperty(value = "治理说明",index = 10)
    @ColumnWidth(20)
    private String manageDesc;
}
