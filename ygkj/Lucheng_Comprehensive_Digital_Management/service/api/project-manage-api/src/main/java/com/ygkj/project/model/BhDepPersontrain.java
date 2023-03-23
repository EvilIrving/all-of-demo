package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("人员培训信息表")
public class BhDepPersontrain implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("编号")
    private String id;


    @ApiModelProperty("培训人员")
    private String person;


    @ApiModelProperty("培训时间")
    private Date time;


    @ApiModelProperty("培训内容")
    private String content;


    @ApiModelProperty("培训经费")
    private BigDecimal fund;


    @ApiModelProperty("培训证明")
    private String prove;


    @ApiModelProperty("附件")
    private String aid;


    @ApiModelProperty("dep_id")
    private Integer depId;


    @ApiModelProperty("pjcd")
    private String pjcd;


    @ApiModelProperty("培训计划")
    private String trainPlan;


    @ApiModelProperty("执行情况")
    private String execStatus;


    @ApiModelProperty("状态")
    private Integer status;


    @ApiModelProperty("批号")
    private String batch;


    @ApiModelProperty("提交人")
    private Integer submitPerson;


    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private int sourceType;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;

    @TableField(exist = false)
    private List<SysFile> files;
}
