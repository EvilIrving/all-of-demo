package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/14
 */
@Data
@ApiModel("病险工程查询入参")
public class DilapProjReqVo extends PageEntity {

    @ApiModelProperty("工程名称")
    String projName;

    @ApiModelProperty("工程类型：山塘/海塘/水闸/水库")
    String projType;

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiModelProperty("流域")
    String bas;

    @ApiModelProperty("是否查询水库实时水位")
    boolean resWaterLevel = false;

    @ApiModelProperty("是否查询水闸实时工情")
    boolean wagaWaterLevel = false;

    @ApiModelProperty("是否查询水闸工程详情")
    boolean seawallInfo = false;

    @ApiModelProperty("是否查询山塘工程详情")
    boolean mnpdInfo = false;

    @ApiModelProperty("是否查询山塘工程详情")
    boolean wagaInfo = false;

    @ApiModelProperty("是否查询山塘工程详情")
    boolean resInfo = false;

    @ApiModelProperty("复盘时刻")
    Date dataTime;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    boolean ascOrDesc = false;
}
