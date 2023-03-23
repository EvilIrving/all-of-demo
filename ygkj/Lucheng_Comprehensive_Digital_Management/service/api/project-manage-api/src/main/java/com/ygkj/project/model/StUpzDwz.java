package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-12-16 15:40
 */
@Data
public class StUpzDwz {
    /**
     * 测站编码
     */
    @ApiModelProperty("测站编码")
    private String stCode;

    /**
     * 工程编码 (有些水闸没有绑定测站的时候用到)
     */
    @ApiModelProperty("工程编码 (有些水闸没有绑定测站的时候用到)")
    private String wagaCode;

    /**
     * 提供闸上水位的测站编码
     */
    @ApiModelProperty("提供闸上水位的测站编码")
    private String upzCode;

    /**
     * 提供闸下水位的测站编码
     */
    @ApiModelProperty("提供闸下水位的测站编码")
    private String dwzCode;

    /**
     * 没有测站要手动插入水位站的标志 0-不用插
     */
    @ApiModelProperty("没有测站要手动插入水位站的标志 0-不用插")
    private String flag;
}
