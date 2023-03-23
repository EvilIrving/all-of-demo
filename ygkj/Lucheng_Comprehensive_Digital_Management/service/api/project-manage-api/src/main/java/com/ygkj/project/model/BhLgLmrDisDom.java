package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-19 17:51
 * @description
 */
@Data
@ApiModel("调度令管理")
public class BhLgLmrDisDom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调度令id
     */
    @ApiModelProperty("调度令id")
    private String id;

    /**
     * 工程编号
     */
    @ApiModelProperty("工程编号")
    private String pid;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private String dYear;

    /**
     * 调令下达时间
     */
    @ApiModelProperty("调令下达时间")
    private Date tm;

    /**
     * 调令编号
     */
    @ApiModelProperty("调令编号")
    private String sOrderCode;

    /**
     * 调令文件（扫描）
     */
    @ApiModelProperty("调令文件（扫描）")
    private String sOrder;

    /**
     * 调令闸门开启时间
     */
    @ApiModelProperty("调令闸门开启时间")
    private Date gateOpenTm;

    /**
     * 调令闸门关闭时间
     */
    @ApiModelProperty("调令闸门关闭时间")
    private Date gateCloseTm;

    /**
     * 数据来源 1-标化 2-本地新增
     */
    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private Integer sourceType;

    /**
     * 调度类型 1-上级调度 2-计划内调度
     */
    @ApiModelProperty("调度类型 1-上级调度 2-计划内调度")
    private String domType;

    @ApiModelProperty("文件上传人id")
    private String uploader;


    @ApiModelProperty("冗余字段 文件上传人名称")
    private String uploaderName;


    @ApiModelProperty("本地新增上传文件ids")
    private String uploadfile;

    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;

    @TableField(exist = false)
    private List<SysFile> uploadFiles;

    public BhLgLmrDisDom() {
    }

    public String getdYear() {
        return dYear;
    }

    public void setdYear(String dYear) {
        this.dYear = dYear;
    }

    public Date getTm() {
        return tm;
    }

    public void setTm(Date tm) {
        this.tm = tm;
    }

    public String getsOrderCode() {
        return sOrderCode;
    }

    public void setsOrderCode(String sOrderCode) {
        this.sOrderCode = sOrderCode;
    }
}
