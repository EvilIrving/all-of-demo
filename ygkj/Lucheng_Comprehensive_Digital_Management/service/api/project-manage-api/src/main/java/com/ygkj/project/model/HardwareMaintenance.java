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
 * @date 2022/4/24 14:47
 * @Description: 硬件设备维护
 */
@Data
public class HardwareMaintenance extends PageEntity implements Serializable {

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
     * 管理单位
     */
    @ApiModelProperty(value = "管理单位")
    private String managementUnit;

    /**
     * 维护单位
     */
    @ApiModelProperty(value = "维护单位")
    private String maintainUnit;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String equipmentName;

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
     * 处理意见（1.日常维护，2.更换设备）
     */
    @ApiModelProperty(value = "处理意见（1.日常维护，2.更换设备）")
    private Integer handlingSuggestion;

    /**
     * 实施前照片 多个逗号拼接
     */
    @ApiModelProperty(value = "实施前照片 多个逗号拼接")
    private String beforePhoto;

    /**
     * 实施后照片 多个逗号拼接
     */
    @ApiModelProperty(value = "实施后照片 多个逗号拼接")
    private String afterPhoto;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标记 0未删除 1已删除")
    private int delFlag;

    @TableField(exist = false)
    @ApiModelProperty(value = "工程名称")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty(value = "实施前照片地址")
    private List<SysFile> beforePhotoFiles;

    @TableField(exist = false)
    @ApiModelProperty(value = "实施后照片地址")
    private List<SysFile> afterPhotoFiles;

    @TableField(exist = false)
    private String pid;
    public HardwareMaintenance() {}
}
