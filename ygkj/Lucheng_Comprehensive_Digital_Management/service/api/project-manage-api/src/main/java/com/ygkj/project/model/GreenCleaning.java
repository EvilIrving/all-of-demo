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
 * @date 2022/4/24 10:25
 * @Description: 绿化保洁
 */
@Data
public class GreenCleaning extends PageEntity implements Serializable {

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
     * 实施日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "实施日期")
    private Date materialDate;

    /**
     * 实施人员
     */
    @ApiModelProperty(value = "实施人员")
    private String implementer;

    /**
     * 实施前照片id 多个逗号拼接
     */
    @ApiModelProperty(value = "实施前照片id 多个逗号拼接")
    private String startPhoto;

    /**
     * 实施后照片id 多个逗号拼接
     */
    @ApiModelProperty(value = "施后照片id 多个逗号拼接")
    private String endPhoto;

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
    private List<SysFile> startPhotoFiles;

    @TableField(exist = false)
    @ApiModelProperty(value = "施后照片地址")
    private List<SysFile> endPhotoFiles;

    public GreenCleaning() {}
}
