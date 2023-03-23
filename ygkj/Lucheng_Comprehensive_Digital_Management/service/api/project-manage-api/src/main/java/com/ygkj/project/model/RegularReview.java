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
 * @date 2022/4/25 9:50
 * @Description: 定期考评
 */
@Data
public class RegularReview extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "日期")
    private Date examTime;

    /**
     * 考评单位id
     */
    @ApiModelProperty(value = "考评单位id")
    private String examUnitId;

    /**
     * 被考评单位id
     */
    @ApiModelProperty(value = "被考评单位id")
    private String evaluatedUnitId;

    /**
     * 考核人员id
     */
    @ApiModelProperty(value = "考核人员id")
    private String staffAppraisalId;

    /**
     * 综合得分
     */
    @ApiModelProperty(value = "综合得分")
    private String synthesisScore;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标记 0未删除 1已删除")
    private int delFlag;

    @TableField(exist = false)
    @ApiModelProperty(value = "考评单位")
    private String examUnit;

    @TableField(exist = false)
    @ApiModelProperty(value = "被考评单位")
    private String evaluatedUnit;

    @TableField(exist = false)
    @ApiModelProperty(value = "考核人员")
    private String staffAppraisal;

    @TableField(exist = false)
    @ApiModelProperty(value = "年份")
    private String year;

    @TableField(exist = false)
    @ApiModelProperty(value = "评定等级")
    private String grade;

    public RegularReview() {}
}
