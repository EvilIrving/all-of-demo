package com.ygkj.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class DeptRespVo {

    @ApiModelProperty("部门id")
    private String id;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("父部门id")
    private String pid;

    @ApiModelProperty("父部门id")
    private List<DeptRespVo> children;
}
