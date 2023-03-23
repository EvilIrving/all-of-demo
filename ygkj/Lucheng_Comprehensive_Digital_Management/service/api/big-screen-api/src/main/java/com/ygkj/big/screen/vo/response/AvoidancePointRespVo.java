package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-10-20 17:34
 */
@Data
public class AvoidancePointRespVo {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("避灾点名称")
    private String pointName;


    @ApiModelProperty("可容纳人数")
    private Integer pointCapacity;

    @ApiModelProperty("现容纳人数")
    private Integer pointNowCapacity;

    @ApiModelProperty("避灾点经度")
    private BigDecimal longitude;


    @ApiModelProperty("避灾点纬度")
    private BigDecimal latitude;

    @ApiModelProperty("避灾点地址")
    private String pointAddress;


    @ApiModelProperty("避灾点说明(多行文字)")
    private String pointDirections;


    @ApiModelProperty("照片")
    private String pointPhoto;

    @ApiModelProperty("多组照片")
    private List<String> pointPhotos;

    @ApiModelProperty("联系人")
    private String contacts;


    @ApiModelProperty("联系方式")
    private String phone;
}
