package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ProjectPatrolReqVo extends PageEntity {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("是否有隐患 0没有 1有")
    private Integer hiddenTroubleCount;

    @ApiModelProperty("海塘名称")
    private String seawallName;
}
