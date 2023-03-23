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
import java.util.List;

/**
 * @author wgf
 * @date 2022/4/24 14:02
 * @Description: 生物防治
 */
@Data
public class BiologicalControl extends PageEntity implements Serializable {

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
     * 防治类型1.白蚁防治，2.外来生物防治
     */
    @ApiModelProperty(value = "防治类型1.白蚁防治，2.外来生物防治")
    private Integer ptType;

    /**
     * 管理单位
     */
    @ApiModelProperty(value = "管理单位")
    private String managementUnit;

    /**
     * 委托单位
     */
    @ApiModelProperty(value = "委托单位")
    private String client;

    /**
     * 合同文件 多个逗号拼接
     */
    @ApiModelProperty(value = "合同文件 多个逗号拼接")
    private String contract;

    /**
     * 成果报告 多个逗号拼接
     */
    @ApiModelProperty(value = "成果报告 多个逗号拼接")
    private String resultsReport;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标记 0未删除 1已删除")
    private int delFlag;

    @TableField(exist = false)
    @ApiModelProperty(value = "工程名称")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty(value = "合同文件地址")
    private List<SysFile> contractFiles;

    @TableField(exist = false)
    @ApiModelProperty(value = "成果报告文件地址")
    private List<SysFile> resultsReportFiles;

    public BiologicalControl() {}
}
