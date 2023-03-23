package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("合同管理验收标准")
public class ConAcceptanceCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;


    @ApiModelProperty("验收事项")
    private String items;


    @ApiModelProperty("验收要求")
    private String requirement;


    @ApiModelProperty("标准分")
    private String standardScore;


    @ApiModelProperty("赋分标准")
    private String scoreCriteria;


    @ApiModelProperty("得分")
    private String score;


    @ApiModelProperty("扣分原因")
    private String reason;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;


    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("创建人")
    private String createId;


    @ApiModelProperty("修改时间")
    private Date updateTime;


    @ApiModelProperty("修改人")
    private String updateId;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门名称")
    @TableField(exist = false)
    private String deptName;
}
