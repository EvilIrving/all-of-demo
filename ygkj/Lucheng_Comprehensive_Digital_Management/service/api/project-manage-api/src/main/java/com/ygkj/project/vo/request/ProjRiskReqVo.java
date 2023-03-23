package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/15
 */
@Data
@ApiModel("风险隐患")
public class ProjRiskReqVo extends PageEntity {

    @ApiModelProperty("工程名称")
    String projName;

    @ApiModelProperty("工程类型")
    String projType;

    @ApiModelProperty("风险类型")
    String recType;

    @ApiModelProperty("隐患类型")
    String riskType;

    @ApiModelProperty("行政区划")
    String areaCode;

    @ApiModelProperty("发现渠道")
    String discoverChannel;

    @ApiModelProperty("整改情况")
    String rectifyStatus;

    @ApiModelProperty("发现日期起始点")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String startDate;

    @ApiModelProperty("发现日期截止点")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String endDate;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    boolean ascOrDesc = false;

}
