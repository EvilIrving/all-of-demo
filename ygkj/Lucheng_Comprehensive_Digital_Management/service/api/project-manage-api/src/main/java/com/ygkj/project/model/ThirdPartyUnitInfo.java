package com.ygkj.project.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdPartyUnitInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 责任单位名称
     */
    @ExcelProperty(value = "单位名称",index = 1)
    @ColumnWidth(30)
    private String unitName;

    /**
     * 责任单位负责人
     */
    @ExcelProperty(value = "负责人",index = 2)
    @ColumnWidth(20)
    private String unitDirector;

    /**
     * 责任单位负责人联系方式
     */
    @ExcelProperty(value = "联系方式", index = 3)
    @ColumnWidth(20)
    private String unitDirectorPhone;

    /**
     * 责任单位负责人职务
     */
    @ExcelProperty(value = "职务", index = 4)
    @ColumnWidth(20)
    private String unitJob;

    /**
     该责任人是否同步过来。同步数据时只修改该责任人
     1为同步过来，null或者0都是手动添加

     又改了，因为技术负责人放到上面的责任人，不在第三方，没有业主手动添加，所以没用了
     */
    @ExcelIgnore
    private String isSyn;

    /**
    责任单位处理情况 0 未处理 1 已处理
    */
    @ExcelIgnore
    private String status  = "0";


    /**
    反馈信息
    */
    @ExcelIgnore
    private String feedbackInfo;

    /**
     是否是工程运管同步的责任人
     */
    @ExcelIgnore
    private String projectType;

    @ExcelProperty(value = "目录名称",index = 0)
    @ColumnWidth(value = 55)
    @TableField(exist = false)
    private String directoryName;
}
