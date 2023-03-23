package com.ygkj.gragh.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FloodControlDispatchingPlan  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;


    @ApiModelProperty("超过时间")
    private String overtime;


    @ApiModelProperty("预案展示类型 1.戍浦江暴雨 2.泽雅防水 3.内河超过控制水位 4.预计内河超过控制水位 5.预计外河水位超过内河水位")
    private String type;


    @ApiModelProperty("删除标志 0-未删除 1-已删除")
    private String delFlag;


    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("更新时间")
    private Date updateTime;
}
