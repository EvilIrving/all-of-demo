package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/4/21
 */
@Data
@ApiModel(description = "新版水质监测查询入参")
public class RivQuaReqVo {
    @ApiModelProperty("行政区划编码")
    String areaCode;

    @ApiModelProperty("测站名称")
    String siteName;

    @ApiModelProperty("测站编号")
    String siteNo;

    @ApiModelProperty("河流名称")
    String riverName;

    @ApiModelProperty("站位级别[1:国控2:省控3:市控4:县控，5:乡镇控]")
    Integer level;

    @ApiModelProperty("是否双覆盖0否1是")
    String isBicovering;

    @ApiModelProperty("水质等级分为：Ⅰ类(1)、Ⅱ类(2)、Ⅲ类(3)、Ⅳ类(4)、Ⅴ类(5)、劣Ⅴ类(6)")
    Integer qualityLevel;

    @ApiModelProperty("是否计算水质好转或恶化")
    boolean computeWorseOrBetter = false;
}
