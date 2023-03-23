package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wgf
 * @date 2022/4/24 15:23
 * @Description: 数据维护
 */
@Data
public class DataMaintenance extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 工程编码
     */
    @ApiModelProperty(value = "工程编码")
    private String prcd;

    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
    private Date materialDate;

    /**
     * 维护单位
     */
    @ApiModelProperty(value = "维护单位")
    private String maintainUnit;

    /**
     * 存在问题
     */
    @ApiModelProperty(value = "存在问题")
    private String existingProblem;

    /**
     * 项目负责人
     */
    @ApiModelProperty(value = "项目负责人")
    private String projectLeader;

    /**
     * 处理详情（问题处理记录）
     */
    @ApiModelProperty(value = "处理详情（问题处理记录）")
    private String particulars;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标记 0未删除 1已删除")
    private int delFlag;

    @TableField(exist = false)
    @ApiModelProperty(value = "工程名称")
    private String name;

    public DataMaintenance() {}
}
