package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author pqf
 * @DESCRIPTION:避灾点信息
 * @create 2021-10-20 17:33
 */
@Data
@ApiModel("避灾点信息")
public class AvoidancePointReqVo {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("避灾点名称")
    private String pointName;

    @ApiModelProperty("避灾点经度")
    private BigDecimal longitude;


    @ApiModelProperty("避灾点纬度")
    private BigDecimal latitude;

    @ApiModelProperty("可容纳人数")
    private Integer pointCapacity;

    @ApiModelProperty("现容纳人数")
    private Integer pointNowCapacity;

    @ApiModelProperty("避灾点地址")
    private String pointAddress;


    @ApiModelProperty("避灾点说明(多行文字)")
    private String pointDirections;


    @ApiModelProperty("照片")
    private String pointPhoto;


    @ApiModelProperty("联系人")
    private String contacts;


    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("页数")
    private Integer pageNum;

    @ApiModelProperty("页面大小")
    private Integer pageSize;
}

